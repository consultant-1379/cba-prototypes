package com.ericsson.oss.mediation.netconf.subsystem;

import com.ericsson.oss.mediation.netconf.subsystem.parser.DefaultNetconfHandler;
import com.ericsson.oss.mediation.util.netconf.api.Datastore;
import com.ericsson.oss.mediation.util.netconf.api.Filter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class NetconfSubsystem implements Command, CommandListener, Killable, Runnable {

    private final static Logger logger = LoggerFactory.getLogger(NetconfSubsystem.class);

    public static class Factory implements NamedFactory<Command> {

        final private CommandListener commandListener;

        public Factory(final CommandListener commandListener) {
            this.commandListener = commandListener != null ? commandListener : new DefaultCommandListener();
        }

        public Command create() {
            return new NetconfSubsystem(commandListener);
        }

        public String getName() {
            return "netconf";
        }
    }

    private static final AtomicInteger sessionIdGenerator;
    private static final Map<Integer, Killable> killableSessions;

    static {
        killableSessions = new Hashtable<>();
        sessionIdGenerator = new AtomicInteger(1);
    }

    private ExitCallback callback;
    private InputStream in;
    private OutputStream out;
    private Environment env;
    private final AtomicBoolean closed;
    private int sessionId;

    final private CommandListener commandListener;

    protected NetconfSubsystem(final CommandListener commandListener) {
        this.commandListener = commandListener;
        this.closed = new AtomicBoolean(false);
    }

    @Override
    public void setInputStream(final InputStream in) {
        this.in = in;
    }

    @Override
    public void setOutputStream(final OutputStream out) {
        this.out = out;
    }

    @Override
    public void setErrorStream(final OutputStream err) {
    }

    @Override
    public void setExitCallback(final ExitCallback callback) {
        this.callback = callback;
    }

    @Override
    public void start(final Environment env) throws IOException {
        this.env = env;
        new Thread(this).start();
    }

    @Override
    public void destroy() {
        this.release();
    }

    @Override
    public void run() {
        try {
            final XMLReader parser = XMLReaderFactory.createXMLReader();
            final DefaultNetconfHandler xmlHandler = new DefaultNetconfHandler(this, new PrintWriter(
                    new OutputStreamWriter(out, "UTF-8"), true), this.closed);
            parser.setContentHandler(xmlHandler);
            parser.setErrorHandler(xmlHandler);

            final NetconfInputStream nin = new NetconfInputStream(in);
            while (!closed.get()) {
                try {
                    parser.parse(new InputSource(nin));
                } catch (IOException e) {
                    logger.error("IOException parse", e);
                } catch (SAXException e) {
                    if (!xmlHandler.isNop()) {
                        logger.error("SAXException parse", e);
                    }
                }
            }
            try {
                nin.forceClose();
            } catch (IOException e) {
                in = null;
            }
            callback.onExit(0);
        } catch (SAXException e) {
            callback.onExit(1, "I wasn't able to create parser");
        } catch (UnsupportedEncodingException e) {
            logger.error(
                    "There is no support of UTF-8 on this platform. I cannot start Netconf subsystem. It requires UTF-8",
                    e);
            callback.onExit(1,
                    "There is no support of UTF-8 on this platform. I cannot start Netconf subsystem. It requires UTF-8");
        } finally {
            this.release();
        }
    }

    private void release() {
        killableSessions.remove(this.sessionId);
        this.closed.set(true);
    }

    @Override
    public void closeSession(final String messageId, final PrintWriter out) {
        this.commandListener.closeSession(messageId, out);
        this.release();
    }

    @Override
    public void hello(final List<String> capabilities, final int sessionId, final PrintWriter out) {
        this.sessionId = sessionIdGenerator.getAndIncrement();
        this.commandListener.hello(capabilities, this.sessionId, out);
        killableSessions.put(this.sessionId, this);
    }

    @Override
    public void get(final String messageId, final Filter filter, final PrintWriter out) {
        this.commandListener.get(messageId, filter, out);
    }

    @Override
    public void getConfig(final String messageId, final Datastore datastore, final Filter filter, final PrintWriter out) {
        this.commandListener.getConfig(messageId, datastore, filter, out);
    }

    @Override
    public void killSession(final String messageId, final int sessionId, final PrintWriter out) {
        Killable killable;
        synchronized (killableSessions) {
            killable = killableSessions.remove(sessionId);
        }
        if (killable != null) {
            killable.kill();
        }
        this.commandListener.killSession(messageId, sessionId, out);
    }

    @Override
    public void kill() {
        this.closed.set(true);
    }

}

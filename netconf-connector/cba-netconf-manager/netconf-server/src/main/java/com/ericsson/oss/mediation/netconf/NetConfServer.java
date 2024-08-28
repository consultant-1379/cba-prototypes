/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.mediation.netconf;

import ch.qos.logback.classic.Level;
import com.ericsson.oss.mediation.netconf.subsystem.CommandListener;
import com.ericsson.oss.mediation.netconf.subsystem.DefaultCommandListener;
import com.ericsson.oss.mediation.netconf.subsystem.NetconfSubsystem;
import com.ericsson.oss.mediation.util.netconf.api.Filter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.impl.type.FileArgumentType;
import net.sourceforge.argparse4j.inf.Argument;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.ArgumentType;
import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.sshd.SshServer;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetConfServer {

    private static final Logger logger = LoggerFactory.getLogger(NetConfServer.class);

    private final SshServer sshd;
    private final int port;
    private final String username;
    private final String password;
    private final File workingFolderPath;
    private final NamedFactory<Command> netconfFactory;

    public NetConfServer(final CommandListener commandListener, final int port, final String username,
            final String password, final File workingFolderPath) {
        sshd = SshServer.setUpDefaultServer();
        this.port = port;
        this.username = username;
        this.password = password;
        this.workingFolderPath = workingFolderPath;
        netconfFactory = new NetconfSubsystem.Factory(commandListener);
    }

    public void run() throws IOException {
        sshd.setPort(port);
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(Paths.get(this.workingFolderPath.getAbsolutePath(),
                "hostkey.ser").toString()));
        sshd.setPasswordAuthenticator(new PasswordAuthenticator() {
            public boolean authenticate(final String u, final String p, final ServerSession session) {
                return username.equals(u) && password.equals(p);
            }
        });
        final List<NamedFactory<Command>> namedFactoryList = new ArrayList<>();
        namedFactoryList.add(this.netconfFactory);
        sshd.setSubsystemFactories(namedFactoryList);
        sshd.start();
    }

    public void stop() throws InterruptedException {
        sshd.stop();
    }
    
     private static class GetOperationWithRpcErrorBadFilter extends DefaultCommandListener {

        @Override
        public void get(final String messageId, final Filter filter, final PrintWriter out) {
            final String rpcError = 
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
"<rpc-reply xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\" message-id=\""+messageId+"\">\n" +
"<rpc-error>\n" +
"    <error-type>application</error-type>\n" +
"    <error-tag>operation-failed</error-tag>\n" +
"    <error-severity>error</error-severity>\n" +
"    <error-info>\n" +
"        <missing-element>systemFunctionsId</missing-element>\n" +
"    </error-info>\n" +
"    <error-message xml:lang=\"en\">Error info:\n "
            + "{unknown_attribute,\"sgsnMmeTop:ManagedElement\",'SysM'}\n" +
"    </error-message>\n" +
"</rpc-error>\n" +
"</rpc-reply>";
            out.println(rpcError);

        }

    }

    private static class GetOperationWithRpcError extends DefaultCommandListener {

        @Override
        public void get(final String messageId, final Filter filter, final PrintWriter out) {
            final String rpcError = "<rpc-reply message-id=\"" + messageId
                    + "\" xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">\n" + "       <rpc-error>\n"
                    + "         <error-type>rpc</error-type>\n" + "         <error-tag>missing-attribute</error-tag>\n"
                    + "         <error-severity>error</error-severity>\n" + "         <error-info>\n"
                    + "           <bad-attribute>message-id</bad-attribute>\n"
                    + "           <bad-element>rpc</bad-element>\n" + "         </error-info>\n"
                    + "       </rpc-error>\n" + "     </rpc-reply>\n" + "]]>]]>";
            out.println(rpcError);

        }

    }

    private static class NoSessionIDCommandListener extends DefaultCommandListener {

        @Override
        public void hello(final List<String> capabilities, final int sessionId, final PrintWriter out) {
            out.println("<hello xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">");
            out.println("\t<capabilities>");
            out.print("\t\t<capability>");
            out.print("urn:ietf:params:netconf:base:1.1");
            out.println("</capability>");
            out.print("\t\t<capability>");
            out.print("urn:ietf:params:netconf:capability:startup:1.0");
            out.println("</capability>");
            out.print("\t\t<capability>");
            out.print("http://example.net/router/2.3/myfeature");
            out.println("</capability>");
            out.println("\t</capabilities>");
            //			out.print("\t<session-id>");
            //			out.print(sessionId.getAndIncrement());
            //			out.println("</session-id>");
            out.println("</hello>");
            out.println("]]>]]>");
        }
    }

    private static class EmptySessionIDCommandListener extends DefaultCommandListener {

        @Override
        public void hello(final List<String> capabilities, final int sessionId, final PrintWriter out) {
            out.println("<hello xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">");
            out.println("\t<capabilities>");
            out.print("\t\t<capability>");
            out.print("urn:ietf:params:netconf:base:1.1");
            out.println("</capability>");
            out.print("\t\t<capability>");
            out.print("urn:ietf:params:netconf:capability:startup:1.0");
            out.println("</capability>");
            out.print("\t\t<capability>");
            out.print("http://example.net/router/2.3/myfeature");
            out.println("</capability>");
            out.println("\t</capabilities>");
            out.print("\t<session-id>");
            //			out.print(sessionId.getAndIncrement());
            out.println("</session-id>");
            out.println("</hello>");
            out.println("]]>]]>");
        }
    }

    private static class MalformedSessionIDCommandListener extends DefaultCommandListener {

        @Override
        public void hello(final List<String> capabilities, final int sessionId, final PrintWriter out) {
            out.println("<hello xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">");
            out.println("\t<capabilities>");
            out.print("\t\t<capability>");
            out.print("urn:ietf:params:netconf:base:1.1");
            out.println("</capability>");
            out.print("\t\t<capability>");
            out.print("urn:ietf:params:netconf:capability:startup:1.0");
            out.println("</capability>");
            out.print("\t\t<capability>");
            out.print("http://example.net/router/2.3/myfeature");
            out.println("</capability>");
            out.println("\t</capabilities>");
            out.print("\t<session-id>");
            out.print("123456aaa");
            out.println("</session-id>");
            out.println("</hello>");
            out.println("]]>]]>");
        }
    }

    private static class WrongCapabilityCommandListener extends DefaultCommandListener {

        @Override
        public void hello(final List<String> capabilities, final int sessionId, final PrintWriter out) {
            out.println("<hello xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">");
            out.println("\t<capabilities>");
            out.print("\t\t<capability>");
            out.print("urn:ietf:params:netconf:base:3.0");
            out.println("</capability>");
            out.print("\t\t<capability>");
            out.print("urn:ietf:params:netconf:base:4.0");
            out.println("</capability>");
            out.print("\t\t<capability>");
            out.print("urn:ietf:params:netconf:capability:startup:1.0");
            out.println("</capability>");
            out.print("\t\t<capability>");
            out.print("http://example.net/router/2.3/myfeature");
            out.println("</capability>");
            out.println("\t</capabilities>");
            out.print("\t<session-id>");
            out.print("1234");
            out.println("</session-id>");
            out.println("</hello>");
            out.println("]]>]]>");
        }
    }

    public static void main(final String[] args) {
        logger.info("Starting...");
        final ArgumentParser parser = getArgumentParser();
        try {
            final Namespace namespace = parser.parseArgs(args);
            final int portParam = namespace.getInt("port");
            final String userParam = namespace.getString("username");
            final String passParam = namespace.getString("password");
            final File workingDirParam = namespace.get("workdir");
            final String usecase = namespace.getString("story");
            final Level level = namespace.get("level");
            final ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory
                    .getLogger(Logger.ROOT_LOGGER_NAME);
            root.setLevel(level);

            final NetConfServer netconfServer;
            switch (usecase) {
            case "NOSESSIONID":
                netconfServer = new NetConfServer(new NoSessionIDCommandListener(), portParam, userParam, passParam,
                        workingDirParam);
                break;
            case "EMPTYSESSIONID":
                netconfServer = new NetConfServer(new EmptySessionIDCommandListener(), portParam, userParam, passParam,
                        workingDirParam);
                break;
            case "MALFORMEDSESSIONID":
                netconfServer = new NetConfServer(new MalformedSessionIDCommandListener(), portParam, userParam,
                        passParam, workingDirParam);
                break;
            case "WRONGCAPABILITY":
                netconfServer = new NetConfServer(new WrongCapabilityCommandListener(), portParam, userParam,
                        passParam, workingDirParam);
                break;
            case "GETRPCERROR":
                netconfServer = new NetConfServer(new GetOperationWithRpcError(), portParam, userParam, passParam,
                        workingDirParam);
                break;
            case "GETRPCERRORBADFILTER":
                netconfServer = new NetConfServer(new GetOperationWithRpcErrorBadFilter(), portParam, userParam, passParam,
                        workingDirParam);
                break;
            default:
                netconfServer = new NetConfServer(new DefaultCommandListener(), portParam, userParam, passParam,
                        workingDirParam);
                break;
            }
            netconfServer.run();
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        netconfServer.stop();
                    } catch (InterruptedException e) {
                        logger.warn("Shutdown thread was interrupted", e);
                    }
                }
            }));
            logger.info("Started...");
            while (true) {
                Thread.sleep(10 * 10000);
            }
        } catch (ArgumentParserException e) {
            parser.handleError(e);
        } catch (InterruptedException e) {
            logger.warn("Application was interrupted", e);
        } catch (IOException e) {
            logger.warn("IO exception", e);
        } finally {
            logger.info("Finished...");
        }
    }

    private static ArgumentParser getArgumentParser() {
        final ArgumentParser parser = ArgumentParsers.newArgumentParser("netconf-server").defaultHelp(true);
        final Argument argument = parser.addArgument("-w", "--workdir").type(new FileArgumentType().verifyExists());
        try {
            argument.setDefault(new File(new File(NetConfServer.class.getProtectionDomain().getCodeSource()
                    .getLocation().toURI()).getParent()));
        } catch (URISyntaxException e) {
            System.out.println("I wasn't able to get a current directory of netconf server");
        }
        parser.addArgument("--port").type(Integer.class).choices(Arguments.range(1, 65535)).setDefault(22);
        parser.addArgument("-u", "--username").required(true);
        parser.addArgument("-p", "--password").required(true);
        parser.addArgument("-s", "--story").setDefault("");
        parser.addArgument("-l", "--level").setDefault(Level.INFO)
                .choices(Level.ALL, Level.TRACE, Level.DEBUG, Level.WARN, Level.ERROR, Level.INFO, Level.OFF)
                .type(new ArgumentType<Level>() {
                    @Override
                    public Level convert(final ArgumentParser parser, final Argument arg, final String value)
                            throws ArgumentParserException {
                        return Level.valueOf(value);
                    }
                }).help("logging level");
        return parser;
    }

}

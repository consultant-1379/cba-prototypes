package com.ericsson.oss.mediation.netconf;

import com.ericsson.oss.mediation.netconf.shell.NetconfShell;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import jline.TerminalFactory;
import jline.console.ConsoleReader;
import jline.console.history.FileHistory;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.type.FileArgumentType;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetconfClient {

    private static final Logger logger = LoggerFactory.getLogger(NetconfClient.class);

    private final File historyFile;

    public NetconfClient(final File historyFile) {
        this.historyFile = historyFile;
    }

    public static void main(final String[] args) {
        final String osName = System.getProperty("os.name").toLowerCase();
        final ArgumentParser parser = initParser();
        try {
            final Namespace ns = parser.parseArgs(args);
            final File historyFile = ns.get("history");
            final NetconfClient client = new NetconfClient(getHistoryFile(historyFile, osName.startsWith("windows")));
            client.loop();
        } catch (ArgumentParserException e) {
            parser.handleError(e);
        }
    }

    public void loop() {
        try {
            final ConsoleReader console = new ConsoleReader();
            final FileHistory history = setAndReturnHistory(console);
            new NetconfShell(console).loop();
            if (history != null) {
                history.flush();
            }
            console.shutdown();
        } catch (IOException e) {
            logger.error("IOException", e);
        } finally {
            try {
                TerminalFactory.get().restore();
            } catch (final Exception e) {
                logger.error("Terminal restore error", e);
            }
        }
    }

    private static File getHistoryFile(final File historyFile, final boolean isWindows) {
        if (historyFile != null) {
            return historyFile;
        } else {
            final String userHome;
            if (isWindows) {
                final String userProfileDir = System.getenv("USERPROFILE");
                userHome = userProfileDir != null ? userProfileDir : System.getProperty("user.home");
            } else {
                userHome = System.getProperty("user.home");
            }
            return Paths.get(userHome, ".netconf_cli_history").toFile();
        }
    }

    private FileHistory setAndReturnHistory(final ConsoleReader console) {
        try {
            final FileHistory history = new FileHistory(historyFile);
            console.setHistory(history);
            return history;
        } catch (IOException e) {
            logger.error("I wasn't able to create a file for commands history. No history will be available", e);
        }
        return null;
    }

    private static ArgumentParser initParser() {
        final ArgumentParser parser = ArgumentParsers.newArgumentParser("netconf-client").defaultHelp(true);
        parser.addArgument("-ch", "--history").required(false).type(new FileArgumentType());
        return parser;
    }

}

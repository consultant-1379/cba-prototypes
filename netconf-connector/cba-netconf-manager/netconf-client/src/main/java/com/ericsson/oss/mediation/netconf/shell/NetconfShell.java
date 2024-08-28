package com.ericsson.oss.mediation.netconf.shell;

import ch.qos.logback.classic.Level;
import jline.console.ConsoleReader;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.type.FileArgumentType;
import net.sourceforge.argparse4j.inf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.apache.tools.ant.types.Commandline.translateCommandline;

public class NetconfShell {

    private final Logger logger = LoggerFactory.getLogger(NetconfShell.class);

    private final ConsoleReader console;
    private final ArgumentParser argumentParser;

    public NetconfShell(final ConsoleReader console) {
        this.console = console;
        this.argumentParser = initParser();
    }

    public void loop() throws IOException {
        final String prompt = "netconf> ";
        console.setPrompt(prompt);
        loop: while (true) {
            try {
                final String line = this.console.readLine();
                final Namespace ns = this.argumentParser.parseArgs(translateCommandline(line));
                final String action = ns.getString("action");
                switch (action) {
                case "exit":
                    this.console.shutdown();
                    break loop;
                case "connect":
                    final NetconfConnectionShell netconfConnectionShell = new NetconfConnectionShell(this.console,
                            ns.getString("host"), ns.getInt("port"), ns.getString("username"),
                            ns.getString("password"), ns.getInt("timeout"), getCapabilities(ns));
                    netconfConnectionShell.loop();
                    console.setPrompt(prompt);
                    break;
                case "logging":
                    final ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory
                            .getLogger(Logger.ROOT_LOGGER_NAME);
                    root.setLevel((Level) ns.get("level"));
                    break;
                }
            } catch (final ArgumentParserException e) {
                this.argumentParser.handleError(e);
            }
        }
    }

    private List<String> getCapabilities(final Namespace ns) {
        final File capabilitiesFile = ns.get("cf");
        if (capabilitiesFile != null) {
            try (final Scanner scanner = new Scanner(capabilitiesFile, "UTF-8")) {
                final List<String> capabilities = new ArrayList<>();
                while (scanner.hasNextLine()) {
                    final String capability = scanner.nextLine().trim();
                    if (!capability.isEmpty()) {
                        capabilities.add(capability);
                    }
                }
                return capabilities;
            } catch (IOException e) {
                logger.warn("I wasn't able to read capabilities from file. Default will be used", e);
            }
        }
        return ns.get("capabilities");
    }

    private ArgumentParser initParser() {
        final ArgumentParser parser = ArgumentParsers.newArgumentParser("netconf").defaultHelp(true);
        final Subparsers subparsers = parser.addSubparsers().dest("action");
        subparsers.addParser("exit");
        final Subparser loggingParser = subparsers.addParser("logging");
        loggingParser.addArgument("-l", "--level").setDefault(Level.INFO)
                .choices(Level.ALL, Level.TRACE, Level.DEBUG, Level.WARN, Level.ERROR, Level.INFO, Level.OFF)
                .type(new ArgumentType<Level>() {
                    @Override
                    public Level convert(final ArgumentParser parser, final Argument arg, final String value)
                            throws ArgumentParserException {
                        return Level.valueOf(value);
                    }
                });
        final Subparser connectParser = subparsers.addParser("connect");
        connectParser.addArgument("--host");
        connectParser.addArgument("--port").setDefault(22).type(Integer.class);
        connectParser.addArgument("-u", "--username").required(true);
        connectParser.addArgument("-p", "--password").required(true);
        final MutuallyExclusiveGroup capabilitiesGroup = connectParser.addMutuallyExclusiveGroup();
        capabilitiesGroup
                .addArgument("-c", "--capabilities")
                .nargs("*")
                .setDefault("urn:ietf:params:ns:netconf:base:1.0", "urn:ietf:params:xml:ns:netconf:base:1.0",
                        "urn:ietf:params:netconf:base:1.1").help("space separated list of capabilities");
        capabilitiesGroup.addArgument("-cf").help("path to a file from where to load capabilities line by line")
                .required(false).type(new FileArgumentType().verifyExists().verifyIsFile().verifyCanRead());
        connectParser.addArgument("-t", "--timeout").setDefault(20000).type(Integer.class);
        return parser;
    }

}

package com.ericsson.oss.mediation.netconf.shell;

import com.ericsson.oss.mediation.util.netconf.api.Datastore;
import com.ericsson.oss.mediation.util.netconf.api.Filter;
import com.ericsson.oss.mediation.util.netconf.api.NetconManagerConstants;
import com.ericsson.oss.mediation.util.netconf.api.NetconfConnectionStatus;
import com.ericsson.oss.mediation.util.netconf.api.NetconfManager;
import com.ericsson.oss.mediation.util.netconf.api.NetconfResponse;
import com.ericsson.oss.mediation.util.netconf.api.exception.NetconfManagerException;
import com.ericsson.oss.mediation.util.netconf.manager.NetconfManagerFactory;
import com.ericsson.oss.mediation.util.transport.api.TransportManager;
import com.ericsson.oss.mediation.util.transport.api.TransportManagerCI;
import com.ericsson.oss.mediation.util.transport.api.TransportSessionType;
import com.ericsson.oss.mediation.util.transport.api.factory.TransportFactory;
import com.ericsson.oss.mediation.util.transport.provider.TransportProviderImpl;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jline.console.ConsoleReader;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.*;

import static org.apache.tools.ant.types.Commandline.translateCommandline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetconfConnectionShell {

    private final Logger logger = LoggerFactory.getLogger(NetconfConnectionShell.class);

    private final ConsoleReader console;
    private final TransportManagerCI transportManagerCI;
    private final Map<String, Object> configMap;
    private String prompt;
    private final ArgumentParser argumentParser;
    private final String host;
    private final Integer port;

    public NetconfConnectionShell(final ConsoleReader console, final String host, final Integer port,
            final String username, final String password, final Integer timeout, final List<String> capabilities) {
        this.console = console;
        this.argumentParser = initParser();
        this.transportManagerCI = new TransportManagerCI();
        transportManagerCI.setHostname(host);
        transportManagerCI.setPort(port);
        transportManagerCI.setUsername(username);
        transportManagerCI.setPassword(password);
        transportManagerCI.setSocketConnectionTimeoutInMillis(timeout);
        transportManagerCI.setSessionType(TransportSessionType.SUBSYSTEM);
        transportManagerCI.setSessionTypeValue("netconf");
        this.configMap = new HashMap<>();
        this.configMap.put(NetconManagerConstants.CAPABILITIES_KEY, capabilities);
        this.host = host;
        this.port = port;
        this.prompt = String.format("netconf/%s:%d> ", host, port);
    }

    public void loop() throws IOException {
        final TransportFactory transportFactory = TransportProviderImpl.getFactory("SSH");
        final TransportManager transportManager = transportFactory.createTransportManager(transportManagerCI);
        NetconfManager netconfManager = null;
        try {
            netconfManager = NetconfManagerFactory.createNetconfManager(transportManager, configMap);
            netconfManager.connect();
            this.prompt = String.format("netconf/%s:%d/%s> ", this.host, this.port, netconfManager.getSessionId());
            this.console.setPrompt(this.prompt);
            loop: while (true) {
                try {
                    final String line = this.console.readLine();
                    final Namespace ns = this.argumentParser.parseArgs(translateCommandline(line));
                    final String action = ns.getString("action");
                    switch (action) {
                    case "exit":
                        netconfManager.disconnect();
                        break loop;
                    case "get":
                        final NetconfResponse getResponse = netconfManager.get(getFilter(ns));
                        final String get_output = getResponse.isError() ? getResponse.getErrorMessage() : getResponse
                                .getData();
                        decideOutput(get_output, ns);
                        break;
                    case "get-config":
                        final Datastore source = ns.get("source");
                        final NetconfResponse getConfigResponse = netconfManager.getConfig(source, getFilter(ns));
                        final String getconfig_output = getConfigResponse.isError() ? getConfigResponse
                                .getErrorMessage() : getConfigResponse.getData();
                        decideOutput(getconfig_output, ns);
                        break;
                    case "kill-session":
                        final String sessionId = ns.getString("sessionid");
                        final NetconfResponse sessionResponse = netconfManager.killSession(sessionId);
                        console.println(getMessageFromNetconfResponse(sessionResponse));
                        break;
                    }
                } catch (final ArgumentParserException e) {
                    this.argumentParser.handleError(e);
                }
            }
        } catch (NetconfManagerException e) {
            logger.error("Netconf error", e);
        } catch (Exception e) {
            logger.error("Unpredicted error.", e);
        } finally {
            if (netconfManager != null) {
                if (netconfManager.getStatus() == NetconfConnectionStatus.CONNECTED) {
                    try {
                        netconfManager.disconnect();
                    } catch (NetconfManagerException e) {
                        logger.error("I wasn't able to disconnect from netconf server", e);
                    }
                }
            }
        }
    }

    private String getMessageFromNetconfResponse(final NetconfResponse response) {
        return response.isError() ? "Error received in netconf session:\n" + response.getErrorMessage() : response
                .getData();
    }

    private void decideOutput(final String output, final Namespace ns) throws IOException {
        final File outputFile = ns.get("output");
        if (outputFile != null) {
            try {
                final List<StandardOpenOption> options = ns.get("options");
                if (!options.contains(StandardOpenOption.WRITE)) {
                    options.add(StandardOpenOption.WRITE);
                }
                Files.write(outputFile.toPath(), output.getBytes(ns.getString("encoding")),
                        options.toArray(new StandardOpenOption[options.size()]));
            } catch (IOException e) {
                logger.warn("I wasn't able to write output to a file", e);
                console.println(output);
            }
        } else {
            console.println(output);
        }
    }

    private Filter getFilter(final Namespace ns) {
        final String type = ns.getString("type");
        final String filterBody = ns.getString("filter");
        if (filterBody == null) {
            final File filterFile = ns.get("ff");
            if (filterFile != null) {
                try {
                    final String filterAsString = new String(Files.readAllBytes(Paths.get(filterFile.getPath())),
                            ns.getString("encoding"));
                    return new Filter() {
                        @Override
                        public String getType() {
                            return type;
                        }

                        @Override
                        public String asString() {
                            return filterAsString;
                        }
                    };
                } catch (IOException e) {
                    logger.warn("I wasn't able to create a filter", e);
                }
            }
        } else {
            return new Filter() {
                @Override
                public String getType() {
                    return type;
                }

                @Override
                public String asString() {
                    return filterBody;
                }
            };
        }
        return null;
    }

    private ArgumentParser initParser() {
        final ArgumentParser parser = ArgumentParsers.newArgumentParser("netconf-connected").defaultHelp(true);
        final Subparsers subparsers = parser.addSubparsers().dest("action");
        subparsers.addParser("exit");
        final Subparser getParser = subparsers.addParser("get");
        addGetOperationArguments(getParser);
        final Subparser getConfigParser = subparsers.addParser("get-config");
        getConfigParser.addArgument("-s", "--source").setDefault(Datastore.RUNNING).type(Datastore.class)
                .choices(Datastore.values()).help("source of configuration to get");
        addGetOperationArguments(getConfigParser);
        final Subparser killSessionParser = subparsers.addParser("kill-session");
        killSessionParser.addArgument("-sid", "--sessionid").required(true);
        return parser;
    }

    private void addGetOperationArguments(final Subparser parser) {
        parser.addArgument("-t", "--type").setDefault("subtree").help("type of filtering");
        parser.addArgument("-e", "--encoding").setDefault("UTF-8")
                .help("encoding of a file from where to read a filter body or where to write response output");
        parser.addArgument("-o", "--output").type(Arguments.fileType().verifyCanWriteParent())
                .help("file where to write an output of operation");
        parser.addArgument("-oo", "--options")
                .type(StandardOpenOption.class)
                .nargs("+")
                .choices(StandardOpenOption.CREATE, StandardOpenOption.CREATE_NEW,
                        StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.APPEND)
                .setDefault(StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
                .help("Options what to do with an output file. By default client will overwrite content of a file.");
        final MutuallyExclusiveGroup getFilterGroup = parser.addMutuallyExclusiveGroup();
        getFilterGroup.addArgument("-f", "--filter").help("Content of a filter");
        getFilterGroup.addArgument("-ff").type(Arguments.fileType().verifyExists().verifyCanRead())
                .help("Content of a filter read from a file");
    }

}

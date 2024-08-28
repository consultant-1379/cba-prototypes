package com.ericsson.oss.mediation.netconf.rule;

import com.ericsson.oss.mediation.netconf.subsystem.CommandListener;
import com.ericsson.oss.mediation.netconf.subsystem.NetconfSubsystem;
import org.apache.sshd.SshServer;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SSHServerResource extends ExternalResource {

    private final Logger logger = LoggerFactory.getLogger(SSHServerResource.class);

    private final SshServer sshd;
    private final int port;
    private final String username;
    private final String password;
    private final String workingFolderPath;
    private final NamedFactory<Command> netconfFactory;

    public SSHServerResource() {
        this(null);
    }

    public SSHServerResource(final CommandListener commandListener) {
        this.sshd = SshServer.setUpDefaultServer();
        this.port = Integer.parseInt(System.getProperty("sshd.port"));
        this.username = System.getProperty("sshd.username");
        this.password = System.getProperty("sshd.password");
        this.workingFolderPath = System.getProperty("sshd.working.folder");
        this.netconfFactory = new NetconfSubsystem.Factory(commandListener);
    }

    @Override
    protected void before() throws Throwable {
        this.sshd.setPort(port);
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(Paths.get(this.workingFolderPath, "hostkey.ser")
                .toString()));
        sshd.setPasswordAuthenticator(new PasswordAuthenticator() {
            public boolean authenticate(String u, String p, ServerSession session) {
                return username.equals(u) && password.equals(p);
            }
        });
        List<NamedFactory<Command>> namedFactoryList = new ArrayList<>();
        namedFactoryList.add(this.netconfFactory);
        sshd.setSubsystemFactories(namedFactoryList);
        sshd.start();
    }

    @Override
    protected void after() {
        try {
            sshd.stop();
        } catch (InterruptedException e) {
            logger.error("Error occurred while stopping ssh server", e);
        }
    }
}

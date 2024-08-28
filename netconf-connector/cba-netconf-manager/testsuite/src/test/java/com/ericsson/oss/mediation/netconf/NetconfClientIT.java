package com.ericsson.oss.mediation.netconf;

import com.ericsson.oss.mediation.netconf.rule.SSHServerResource;
import expectj.*;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.nio.file.Paths;

@RunWith(JUnit4.class)
public class NetconfClientIT {

    @ClassRule
    public static final SSHServerResource sshd = new SSHServerResource();

    private Spawn spawn;

    @Before
    public void before() throws IOException {
        final String path = System.getProperty("cli.exec.path");
        this.spawn = new ExpectJ(15L).spawn(new Executor() {
            @Override
            public Process execute() throws IOException {
                //Do not remove -Djline.WindowsTerminal.directConsole until it is fixed https://github.com/jline/jline2/issues/103
                return new ProcessBuilder("java", "-Djline.WindowsTerminal.directConsole=false", "-jar", path,
                        "--history", Paths.get(Paths.get(path).getParent().toString(), ".netconf_cli_history")
                                .toString()).start();
            }
        });
    }

    @Test
    public void testClientCanConnectAndDisconnect() throws IOException, TimeoutException, ExpectJException {
        final String host = System.getProperty("sshd.host");
        final String port = System.getProperty("sshd.port");
        final String username = System.getProperty("sshd.username");
        final String password = System.getProperty("sshd.password");

        spawn.expect("netconf>", 4L);
        spawn.send("connect --host " + host + " --port " + port + " -u " + username + " -p " + password + "\n");
        spawn.expect("netconf/" + host + ":" + port + "/", 5L);
        spawn.send("exit\n");
        spawn.expect("netconf>", 3L);
        spawn.send("exit\n");
        spawn.expectClose(3L);
    }

    @After
    public void after() throws IOException {
        if (spawn != null && !spawn.isClosed()) {
            spawn.stop();
        }
    }

}

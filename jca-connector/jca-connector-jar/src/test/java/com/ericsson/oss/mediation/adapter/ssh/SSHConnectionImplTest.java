/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.mediation.adapter.ssh;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.mediation.adapter.ssh.api.SSHSessionRequest;
import com.ericsson.oss.mediation.adapter.ssh.provider.CLISessionException;

@RunWith(MockitoJUnitRunner.class)
@Ignore
public class SSHConnectionImplTest {

    @Mock
    private SSHManagedConnection sshMC;

    @Mock
    private SSHManagedConnectionFactory sshMCFactory;

    @Mock
    private SSHSessionRequest sshSessionRequest;

    private SSHConnectionImpl sshConnectionImpl;

    private static final String COMMAND = "Command";

    @Before
    public void setUp() throws Exception {
        sshConnectionImpl = new SSHConnectionImpl(sshMC, sshMCFactory,
                sshSessionRequest);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testClose() {
        sshConnectionImpl.close();
    }

    @Test
    public void testGetMcf() {
        assertEquals(sshMCFactory, sshConnectionImpl.getMcf());
    }

    @Test
    public void testGetReqInfo() {
        assertEquals(sshSessionRequest, sshConnectionImpl.getReqInfo());
    }

    @Test
    public void testExecuteCommandwithBytes() {
        sshConnectionImpl.executeCommandBytes(COMMAND);
    }

    @Test
    public void testIsAlive() {
        assertFalse(sshConnectionImpl.isAlive());
    }

    @Test
    public void testCloseAndTerminateSSH() {
        sshConnectionImpl.closeAndTerminateSSH();
    }

    @Test(expected = CLISessionException.class)
    public void testCreateSSHSession() throws CLISessionException {
        sshConnectionImpl.createSSHSession();
    }

}

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

import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.resource.ResourceException;
import javax.resource.spi.*;
import javax.security.auth.Subject;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.mediation.adapter.ssh.api.SSHConnection;
import com.ericsson.oss.mediation.adapter.ssh.api.SSHSessionRequest;

@RunWith(MockitoJUnitRunner.class)
@Ignore
public class SSHManagedConnectionFactoryTest {
    private SSHManagedConnectionFactory sshManagedConnectionFactory;

    @Mock
    private ConnectionManager connectionManager;

    @Mock
    private SSHSessionRequest sshSessionRequest;

    @Mock
    private SSHConnection sshConnection;

    private Subject subject;

    @Mock
    private ConnectionRequestInfo cxRequestInfo;

    private Set connectionSet;

    @Mock
    private SSHManagedConnection sshManagedConnection;

    @Mock
    private SSHConnectionImpl sshConnectionImpl;

    @Mock
    private PrintWriter printWriter;

    @Mock
    private ResourceAdapter resourceAdapter;

    @Before
    public void setUp() throws Exception {
        sshManagedConnectionFactory = new SSHManagedConnectionFactory();

        when(
                connectionManager.allocateConnection(
                        sshManagedConnectionFactory, cxRequestInfo))
                        .thenReturn(sshConnection);
        when(sshSessionRequest.getIpAddress()).thenReturn("1.1.1.1");

        sshManagedConnectionFactory.setLogWriter(printWriter);
        sshManagedConnectionFactory.getLogWriter();

        sshManagedConnectionFactory.setResourceAdapter(resourceAdapter);
        sshManagedConnectionFactory.getResourceAdapter();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCreateConnectionFactoryConnectionManager()
            throws ResourceException {
        sshManagedConnectionFactory.createConnectionFactory(connectionManager);
    }

    @Test(expected = ResourceException.class)
    public void testCreateConnectionFactory() throws ResourceException {
        sshManagedConnectionFactory.createConnectionFactory();
    }

    @Test
    public void testCreateManagedConnection() throws ResourceException {
        sshManagedConnectionFactory.createManagedConnection(subject,
                sshSessionRequest);
    }

    @Test
    public void testMatchManagedConnections() throws ResourceException {
        connectionSet = new LinkedHashSet();
        connectionSet.add(sshManagedConnection);

        when(sshManagedConnection.getConnection())
        .thenReturn(sshConnectionImpl);
        when(sshConnectionImpl.getReqInfo()).thenReturn(sshSessionRequest);

        sshManagedConnectionFactory.matchManagedConnections(connectionSet,
                subject, sshSessionRequest);
    }

}

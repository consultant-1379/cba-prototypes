package com.ericsson.oss.mediation.adapter.ssh;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionEventListener;
import javax.security.auth.Subject;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.mediation.adapter.ssh.api.SSHSessionRequest;

@RunWith(MockitoJUnitRunner.class)
@Ignore
public class SSHManagedConnectionTest {

    SSHManagedConnection sshManagedConnection;

    @Mock
    private SSHManagedConnectionFactory sshManagedConnectionFactory;

    @Mock
    private SSHSessionRequest sshSessionRequest;

    @Mock
    private SSHConnectionImpl sshConnectionImpl;

    @Mock
    private PrintWriter logwriter;

    @Mock
    private ConnectionEventListener connectionEventListener;

    @Mock
    private XAResource xares;

    private Subject subject;

    @Before
    public void setUp() throws Exception {
        sshManagedConnection = new SSHManagedConnection(
                sshManagedConnectionFactory, "");
        sshManagedConnection.getUser();
        sshManagedConnection.getLogWriter();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test(expected = ResourceException.class)
    public void testGetConnectionSubjectConnectionRequestInfoWithException()
            throws ResourceException {
        sshManagedConnection.getConnection(subject, sshSessionRequest);
    }

    public void testGetConnectionSubjectConnectionRequestInfo()
            throws ResourceException {
        when(sshConnectionImpl.isAlive()).thenReturn(Boolean.FALSE);
        sshManagedConnection.associateConnection(sshConnectionImpl);
        sshManagedConnection.getConnection(subject, sshSessionRequest);
        assertNotNull(sshManagedConnection.getConnection());
    }

    @Test(expected = ResourceException.class)
    public void testAssociateConnectionWithNullConnection()
            throws ResourceException {
        sshManagedConnection.associateConnection(null);
    }

    @Test
    public void testAssociateConnection() throws ResourceException {
        sshManagedConnection.associateConnection(sshConnectionImpl);
    }

    @Test
    public void testCleanup() throws ResourceException {
        when(sshSessionRequest.getEndCommand()).thenReturn("");
        when(sshConnectionImpl.getReqInfo()).thenReturn(sshSessionRequest);
        sshManagedConnection.associateConnection(sshConnectionImpl);
        sshManagedConnection.cleanup();
    }

    @Test
    public void testDestroy() throws ResourceException {
        sshManagedConnection.destroy();
    }

    @Test
    public void testCloseHandle() {
        sshManagedConnection
        .addConnectionEventListener(connectionEventListener);
        sshManagedConnection.closeHandle(sshConnectionImpl);
    }

    @Test
    public void testSetLogWriter() throws ResourceException {
        sshManagedConnection.setLogWriter(logwriter);
    }

    @Test
    public void testGetLocalTransaction() throws ResourceException {
        assertNotNull(sshManagedConnection.getLocalTransaction());
    }

    @Test
    public void testGetXAResource() throws ResourceException {
        assertNotNull(sshManagedConnection.getXAResource());
    }

    @Test
    public void testGetMetaData() throws ResourceException {
        sshManagedConnection.getMetaData();
    }

    @Test
    public void testAddConnectionEventListener() {
        sshManagedConnection
        .addConnectionEventListener(connectionEventListener);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddConnectionEventListenerWithException() {
        sshManagedConnection.addConnectionEventListener(null);
    }

    @Test
    public void testRemoveConnectionEventListener() {
        sshManagedConnection
        .removeConnectionEventListener(connectionEventListener);
    }

    @Test
    public void testIsSameRM() throws XAException {
        sshManagedConnection.isSameRM(xares);
    }

}

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

import javax.naming.Reference;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.mediation.adapter.ssh.api.SSHConnection;
import com.ericsson.oss.mediation.adapter.ssh.api.SSHSessionRequest;

@RunWith(MockitoJUnitRunner.class)
@Ignore
public class SSHConnectionFactoryImplTest {

    private SSHConnectionFactoryImpl sshConnectionFactoryImpl;

    @Mock
    private SSHManagedConnectionFactory sshManagedConnectionFactory;

    @Mock
    private ConnectionManager connectionManager;

    @Mock
    private SSHSessionRequest cxRequestInfo;

    @Mock
    private SSHConnection sshConnection;

    @Mock
    private Reference reference;

    @Before
    public void setUp() throws Exception {
        when(
                connectionManager.allocateConnection(
                        sshManagedConnectionFactory, cxRequestInfo))
                        .thenReturn(sshConnection);
        sshConnectionFactoryImpl = new SSHConnectionFactoryImpl(
                sshManagedConnectionFactory, connectionManager);
        sshConnectionFactoryImpl.setReference(reference);
        sshConnectionFactoryImpl.getReference();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetConnection() throws ResourceException {
        sshConnectionFactoryImpl.getConnection(cxRequestInfo);
    }

}

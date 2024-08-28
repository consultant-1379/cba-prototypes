/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2013
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.oss.mediation.util.transport.ssh.manager;

import com.ericsson.oss.mediation.util.transport.ssh.manager.SshTransportManagerImpl;
import com.ericsson.oss.mediation.util.transport.ssh.manager.SSHTransportFactory;
import com.ericsson.oss.mediation.util.transport.api.TransportData;
import com.ericsson.oss.mediation.util.transport.api.TransportManagerCI;
import com.ericsson.oss.mediation.util.transport.api.TransportSessionType;
import com.ericsson.oss.mediation.util.transport.api.exception.TransportException;
import com.ericsson.oss.mediation.util.transport.ssh.SshContext;

import java.util.logging.Level;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expectLastCall;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.api.easymock.PowerMock;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.verify;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SshTransportManagerImpl.class)
public class SshTransportManagerImplTest {

    private SshContext sshContext;
    private SshTransportManagerImpl sshTranportManager;
    private final SSHTransportFactory factory;
    private static TransportManagerCI transportManagerCI;

    public SshTransportManagerImplTest() {

        transportManagerCI = new TransportManagerCI();
        transportManagerCI.setHostname("192.168.100.208");
        transportManagerCI.setPassword("sgsn123");
        transportManagerCI.setUsername("borusgsn");
        transportManagerCI.setPort(22);
        transportManagerCI.setSocketConnectionTimeoutInMillis(1000);
        transportManagerCI.setSessionType(TransportSessionType.SUBSYSTEM);
        transportManagerCI.setSessionTypeValue("netconf");

        factory = new SSHTransportFactory();

    }

    @Test
    public void testOpentConnection() {
        try {

            sshContext = createMock(SshContext.class);
            PowerMock.expectNew(SshContext.class, anyObject()).andReturn(sshContext);

            sshContext.connect();
            expectLastCall();
            sshContext.openChannel();
            expectLastCall();
            PowerMock.replay(sshContext, SshContext.class);

            sshTranportManager = (SshTransportManagerImpl) factory.createTransportManager(transportManagerCI);
            sshTranportManager.openConnection();

            verify(sshContext, SshContext.class);

        } catch (TransportException ex) {
            java.util.logging.Logger.getLogger(SshTransportManagerImplTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(SshTransportManagerImplTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Test
    public void testCloseConnection() {
        try {

            sshContext = createMock(SshContext.class);
            PowerMock.expectNew(SshContext.class, anyObject()).andReturn(sshContext);

            sshContext.closeConnection();
            expectLastCall();
            PowerMock.replay(sshContext, SshContext.class);

            sshTranportManager = (SshTransportManagerImpl) factory.createTransportManager(transportManagerCI);
            sshTranportManager.closeConnection();

            verify(sshContext, SshContext.class);

        } catch (TransportException ex) {
            java.util.logging.Logger.getLogger(SshTransportManagerImplTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(SshTransportManagerImplTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Test
    public void testSendData() {
        try {

            sshContext = createMock(SshContext.class);
            PowerMock.expectNew(SshContext.class, anyObject()).andReturn(sshContext);

            sshContext.sendRequest((TransportData) anyObject());
            expectLastCall();
            PowerMock.replay(sshContext, SshContext.class);

            sshTranportManager = (SshTransportManagerImpl) factory.createTransportManager(transportManagerCI);
            sshTranportManager.sendData(new TransportData());

            verify(sshContext, SshContext.class);

        } catch (TransportException ex) {
            java.util.logging.Logger.getLogger(SshTransportManagerImplTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(SshTransportManagerImplTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Test
    public void testReadData() {
        try {

            sshContext = createMock(SshContext.class);
            PowerMock.expectNew(SshContext.class, anyObject()).andReturn(sshContext);

            sshContext.getResponse((TransportData) anyObject(), EasyMock.eq(false));
            expectLastCall();
            PowerMock.replay(sshContext, SshContext.class);

            sshTranportManager = (SshTransportManagerImpl) factory.createTransportManager(transportManagerCI);
            sshTranportManager.readData(new TransportData(), false);

            verify(sshContext, SshContext.class);

        } catch (TransportException ex) {
            java.util.logging.Logger.getLogger(SshTransportManagerImplTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(SshTransportManagerImplTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
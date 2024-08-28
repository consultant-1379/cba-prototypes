/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.mediation.util.netconf.context;

import com.ericsson.oss.mediation.util.netconf.api.NetconManagerConstants;
import com.ericsson.oss.mediation.util.netconf.api.NetconfConnectionStatus;
import com.ericsson.oss.mediation.util.netconf.api.exception.NetconfManagerErrorMessages;
import com.ericsson.oss.mediation.util.netconf.api.exception.NetconfManagerException;
import com.ericsson.oss.mediation.util.netconf.manger.NetconfTestConstants;
import com.ericsson.oss.mediation.util.netconf.server.NetconfServerTestUtility;
import com.ericsson.oss.mediation.util.transport.api.TransportData;
import com.ericsson.oss.mediation.util.transport.api.TransportManager;
import com.ericsson.oss.mediation.util.transport.api.exception.TransportException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ NetconfSession.class })
public class NetconfSessionTest {

    @Mock
    NetconfFSM netconfFSM;

    @Mock
    TransportManager transportManager;

    private final Map<String, Object> configProperties = new HashMap<String, Object>();
    private final List<String> cabability;

    @Captor
    ArgumentCaptor<TransportData> argTransData;

    public NetconfSessionTest() {
        cabability = new ArrayList<String>();
        cabability.add(NetconfTestConstants.BASE_CAPABILITY);
        configProperties.put(NetconManagerConstants.CAPABILITIES_KEY, cabability);
    }

    @Test
    public void testConnect() throws NetconfManagerException {
        try {
            final List<String> cabability = new ArrayList<String>();
            cabability.add(NetconfTestConstants.HELLO_CAPABILITY);
            configProperties.put(NetconManagerConstants.CAPABILITIES_KEY, cabability);
            transportManager = Mockito.mock(TransportManager.class);

            NetconfServerTestUtility.prepareSendHello(transportManager);

            final NetconfSession netconfSession = new NetconfSession(transportManager, configProperties);
            netconfSession.connect();

            assertEquals(netconfSession.getStatus(), NetconfConnectionStatus.CONNECTED);
        } catch (final Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testDisconnect() throws NetconfManagerException {
        try {
            final List<String> cabability = new ArrayList<String>();
            cabability.add(NetconfTestConstants.HELLO_CAPABILITY);
            configProperties.put(NetconManagerConstants.CAPABILITIES_KEY, cabability);
            transportManager = Mockito.mock(TransportManager.class);

            NetconfServerTestUtility.prepareSendHello(transportManager);

            final NetconfSession netconfSession = new NetconfSession(transportManager, configProperties);
            netconfSession.connect();
            assertEquals(netconfSession.getStatus(), NetconfConnectionStatus.CONNECTED);

            NetconfServerTestUtility.prepareSendCloseSession(transportManager);

            netconfSession.disconnect();
            assertEquals(netconfSession.getStatus(), NetconfConnectionStatus.NOT_CONNECTED);

        } catch (final Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testKillSession() throws NetconfManagerException {
        try {
            final List<String> cabability = new ArrayList<String>();
            cabability.add(NetconfTestConstants.HELLO_CAPABILITY);
            configProperties.put(NetconManagerConstants.CAPABILITIES_KEY, cabability);
            transportManager = Mockito.mock(TransportManager.class);

            final NetconfSession netconfSession = new NetconfSession(transportManager, configProperties);
            NetconfServerTestUtility.prepareSendHello(transportManager);

            netconfSession.connect();

            assertEquals(netconfSession.getStatus(), NetconfConnectionStatus.CONNECTED);

            NetconfServerTestUtility.prepareSendRpcOk(transportManager);

            netconfSession.killSession("12");

            assertEquals(netconfSession.getStatus(), NetconfConnectionStatus.CONNECTED);

        } catch (final Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testSetState() throws NetconfManagerException {
        final NetconfSession netconfSession = new NetconfSession(transportManager, configProperties);
        netconfSession.setState(NetconfFSM.READY);

        // at this point only disconnect is allowed for state READY, otherwise
        // exception is thrown
        NetconfServerTestUtility.prepareSendCloseSession(transportManager);

        netconfSession.disconnect();

        // if exception is not thrown than it is ok at this point and status is
        // disconnected
        assertEquals(netconfSession.getStatus(), NetconfConnectionStatus.NOT_CONNECTED);

    }

    @Test
    public void testGetNetconfConnectionStatus() throws NetconfManagerException {
        final NetconfSession netconfSession = new NetconfSession(transportManager, configProperties);

        assertEquals(netconfSession.getStatus(), NetconfConnectionStatus.NEVER_CONNECTED);
    }

    @Test
    public void testDoConnect() throws NetconfManagerException, TransportException {
        final NetconfSession netconfSession = new NetconfSession(transportManager, configProperties);
        assertEquals(netconfSession.getStatus(), NetconfConnectionStatus.NEVER_CONNECTED);

        NetconfServerTestUtility.prepareSendHello(transportManager);

        // set status to HAND_SHAKE as it is possible to connect only from this
        // state
        netconfSession.setState(NetconfFSM.HAND_SHAKE);
        netconfSession.doConnect();
        assertEquals(netconfSession.getStatus(), NetconfConnectionStatus.CONNECTED);
    }

    @Test
    public void testDoHandshake() throws TransportException, NetconfManagerException {
        final List<String> cabability = new ArrayList<String>();
        cabability.add(NetconfTestConstants.HELLO_CAPABILITY);
        configProperties.put(NetconManagerConstants.CAPABILITIES_KEY, cabability);

        NetconfServerTestUtility.prepareSendHello(transportManager);
        final NetconfSession netconfSession = new NetconfSession(transportManager, configProperties);
        netconfSession.setState(NetconfFSM.HAND_SHAKE);
        netconfSession.doHandshake();

        assertEquals(NetconfConnectionStatus.CONNECTED, netconfSession.getStatus());
        assertTrue("1".equals(netconfSession.getSessionId()));

    }

    @Test
    public void testDoHandshakeWithException() throws NetconfManagerException {
        final NetconfSession netconfSession = new NetconfSession(transportManager, configProperties);
        try {

            netconfSession.setState(NetconfFSM.HAND_SHAKE);
            NetconfServerTestUtility.prepareSendHelloWithoutSessionID(transportManager);

            netconfSession.doHandshake();

        } catch (final Exception e) {
            Assert.assertTrue(NetconfConnectionStatus.NOT_CONNECTED.equals(netconfSession.getStatus()));
            assertEquals(NetconfManagerErrorMessages.SESSIONID_WAS_NOT_RECEIVED, e.getMessage());
        }
    }

    /*
     * @Test public void testSendQuery() throws TransportException, NetconfManagerException {
     * NetconfServerTestUtility.prepareSendHello(transportManager);
     * 
     * final NetconfSession netconfSession = new NetconfSession(transportManager, configProperties);
     * netconfSession.setState(NetconfFSM.QUERY);
     * 
     * final TransportData transportData = new TransportData(); transportData.setMessageId(245L);
     * 
     * netconfSession.sendQuery(transportData);
     * 
     * final String res = netconfSession.getQueryResult(245L); assertEquals(NetconfTestConstants.HELLO, res); }
     */

    @Test
    public void testDoDisconnect() throws NetconfManagerException {
        final NetconfSession netconfSession = new NetconfSession(transportManager, configProperties);
        assertEquals(netconfSession.getStatus(), NetconfConnectionStatus.NEVER_CONNECTED);

        // set status to CLOSE_SESSION as it is possible to doDisconnect only
        // from this state
        netconfSession.setState(NetconfFSM.CLOSE_SESSION);
        NetconfServerTestUtility.prepareSendCloseSession(transportManager);
        netconfSession.doDisconnect();
        assertEquals(netconfSession.getStatus(), NetconfConnectionStatus.NOT_CONNECTED);
    }

    @Test
    public void testDoKillSession() throws NetconfManagerException, TransportException {
        final NetconfSession netconfSession = new NetconfSession(transportManager, configProperties);
        netconfSession.setConnectionStatus(NetconfConnectionStatus.CONNECTED);
        netconfSession.setState(NetconfFSM.READY);
        assertEquals(netconfSession.getStatus(), NetconfConnectionStatus.CONNECTED);
        netconfSession.setState(NetconfFSM.KILL_SESSION);
        NetconfServerTestUtility.prepareSendRpcOk(transportManager);
        netconfSession.doKillSession("12");
        assertEquals(netconfSession.getStatus(), NetconfConnectionStatus.CONNECTED);
    }

    @Test
    public void testDoCloseSession() throws NetconfManagerException, TransportException {
        final NetconfSession netconfSession = new NetconfSession(transportManager, configProperties);
        assertEquals(netconfSession.getStatus(), NetconfConnectionStatus.NEVER_CONNECTED);

        netconfSession.setState(NetconfFSM.CLOSE_SESSION);
        NetconfServerTestUtility.prepareSendCloseSession(transportManager);
        netconfSession.doCloseSession();

        // first way of checking by capturing arguments
        // verify(transportManager).sendData(argTransData.capture()); comented
        // before
        // String passedArg = argTransData.getValue().getData().toString();
        // comented before
        // assertTrue(passedArg.startsWith(TransportCloseRequestMatcher.START)
        // && passedArg.endsWith(TransportCloseRequestMatcher.END));comented
        // before

        // alternative way of checking by using Matchers
        // verify(transportManager).sendData(Matchers.argThat(new
        // TransportCloseRequestMatcher(CLOSE_SESSION)));
        assertEquals(netconfSession.getStatus(), NetconfConnectionStatus.NOT_CONNECTED);
    }

}

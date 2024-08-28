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
package com.ericsson.oss.mediation.util.netconf.manger;

import com.ericsson.oss.mediation.util.netconf.api.Datastore;
import com.ericsson.oss.mediation.util.netconf.api.NetconManagerConstants;
import com.ericsson.oss.mediation.util.netconf.api.NetconfManager;
import com.ericsson.oss.mediation.util.netconf.api.NetconfResponse;
import com.ericsson.oss.mediation.util.netconf.api.exception.NetconfManagerException;
import com.ericsson.oss.mediation.util.netconf.context.NetconfSession;
import com.ericsson.oss.mediation.util.netconf.manager.NetconfManagerFactory;
import com.ericsson.oss.mediation.util.netconf.server.NetconfServerTestUtility;
import com.ericsson.oss.mediation.util.transport.api.TransportManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class NetconfManagerImplTest {

    private static final String GET_ANSWER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<rpc-reply xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\" message-id=\"101\"><data><ManagedElement xmlns=\"urn:com:ericsson:ecim:ComTop\"><managedElementId>TCU</managedElementId><siteLocation/></ManagedElement></data></rpc-reply>"
            + "]";

    private static final String GET_EXPECTED = "<ManagedElement xmlns=\"urn:com:ericsson:ecim:ComTop\"><managedElementId>TCU</managedElementId><siteLocation></siteLocation></ManagedElement>";

    private final String ERROR_BAD_ATTRIBUTE_MESSAGE_EXPECTED = 
"Error Type: rpc\n" +
"Error Tag: MISSING_ATTRIBUTE\n" +
"Error Severity: error\n" +
"Error Message: \n" +
"Error Info:\n" +
"    bad-attribute: message-id\n" +
"    bad-element: rpc\n" +
"";
    private final String RPC_ERROR_BAD_FITER_MESSAGE_EXPECTED = 
"Error Type: application\n" +
"Error Tag: OPERATION_FAILED\n" +
"Error Severity: error\n" +
"Error Message: Error info:\n" +
" {unknown_attribute,\"sgsnMmeTop:ManagedElement\",'SysM'}\n" ;
    
    private final String RPC_ERROR_UNEXPECTED_ERROR = 
"Error Type: application\n" +
"Error Tag: OPERATION_FAILED\n" +
"Error Severity: error\n" +
"Error Message: \n" +
"      Unexpected error: {failed_to_parse_xml,\n" +
"    {fatal,\n" +
"        {{endtag_does_not_match,{was,ggSysM,should_have_been,'SysM'}},\n" +
"         {file,file_name_unknown},\n" +
"         {line,4},\n" +
"         {col,266}}}}\n";
    
private final String RPC_ERROR_MISSING_ATTRIBUTE = 
"Error Tag: MISSING_ELEMENT\n" +
"Error Severity: error\n" +
"Error Message: MO key attribute missing\n" +
"Error Info:\n" +
"    missing-element: systemFunctionsId\n";
    
    
    private final Map<String, Object> configProperties = new HashMap<String, Object>();

    @Mock
    NetconfSession netconfSessionMock;

    @Mock
    TransportManager transportManager;

    public NetconfManagerImplTest() {
        final List<String> cabability = new ArrayList<String>();
        cabability.add(NetconfTestConstants.HELLO_CAPABILITY);
        configProperties.put(NetconManagerConstants.CAPABILITIES_KEY, cabability);
    }

    @Test
    public void testConnect() throws NetconfManagerException {
        try {
            final NetconfManager netconfManager = NetconfManagerFactory.createNetconfManager(transportManager,
                    configProperties);

            NetconfServerTestUtility.prepareSendHello(transportManager);
            netconfManager.connect();

            verify(transportManager).openConnection();
        } catch (final Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testDisconnect() throws NetconfManagerException {
        try {
            final NetconfManager netconfManager = NetconfManagerFactory.createNetconfManager(transportManager,
                    configProperties);

            NetconfServerTestUtility.prepareSendHello(transportManager);
            netconfManager.connect();

            verify(transportManager).openConnection();

            NetconfServerTestUtility.prepareSendCloseSession(transportManager);
            netconfManager.disconnect();

            verify(transportManager).closeConnection();
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testKillSession() throws NetconfManagerException {
        try {
            final NetconfManager netconfManager = NetconfManagerFactory.createNetconfManager(transportManager,
                    configProperties);

            NetconfServerTestUtility.prepareSendHello(transportManager);
            netconfManager.connect();
            verify(transportManager).openConnection();

            NetconfServerTestUtility.prepareSendRpcOk(transportManager);
            netconfManager.killSession("12");
        } catch (final Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetWithErrorRpc() throws NetconfManagerException {
        try {
            final NetconfManager netconfManager = NetconfManagerFactory.createNetconfManager(transportManager,
                    configProperties);

            NetconfServerTestUtility.prepareSendHello(transportManager);

            netconfManager.connect();
            NetconfServerTestUtility.prepareSendQuery(transportManager, NetconfTestConstants.RPC_ERROR);

            final NetconfResponse response = netconfManager.get();
            assertTrue(response.isError());           
            assertTrue(ERROR_BAD_ATTRIBUTE_MESSAGE_EXPECTED.equals(response.getErrorMessage()));
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testGetWithFilterErrorMissingElement () throws NetconfManagerException {
        try {
            final NetconfManager netconfManager = NetconfManagerFactory.createNetconfManager(transportManager,
                    configProperties);

            NetconfServerTestUtility.prepareSendHello(transportManager);

            netconfManager.connect();
            NetconfServerTestUtility.prepareSendQuery(transportManager, NetconfTestConstants.RPC_ERROR_MISSING_ELEMENT);

            final NetconfResponse response = netconfManager.get();
            assertTrue(response.isError());           
            //assertTrue(RPC_ERROR_MISSING_ATTRIBUTE.equals(response.getErrorMessage()));
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testGetWithErrorRpc2() throws NetconfManagerException {
        try {
            final NetconfManager netconfManager = NetconfManagerFactory.createNetconfManager(transportManager,
                    configProperties);
            NetconfServerTestUtility.prepareSendHello(transportManager);

            netconfManager.connect();
            NetconfServerTestUtility.prepareSendQuery(transportManager, NetconfTestConstants.RPC_ERROR_2);

            final NetconfResponse response = netconfManager.get();
            assertTrue(response.isError());
            assertTrue(RPC_ERROR_BAD_FITER_MESSAGE_EXPECTED.equals(response.getErrorMessage()));
        } catch (final Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testGetWithErrorRpc3() throws NetconfManagerException {
        try {
            final NetconfManager netconfManager = NetconfManagerFactory.createNetconfManager(transportManager,
                    configProperties);
            NetconfServerTestUtility.prepareSendHello(transportManager);

            netconfManager.connect();
            NetconfServerTestUtility.prepareSendQuery(transportManager, NetconfTestConstants.RPC_ERROR_3);

            final NetconfResponse response = netconfManager.get();
            assertTrue(response.isError());
            assertTrue(RPC_ERROR_UNEXPECTED_ERROR.equals(response.getErrorMessage()));
        } catch (final Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
    
    
    

    @Test
    public void testGetConfigWithParam() throws NetconfManagerException {
        try {
            final NetconfManager netconfManager = NetconfManagerFactory.createNetconfManager(transportManager,
                    configProperties);

            NetconfServerTestUtility.prepareSendHello(transportManager);

            netconfManager.connect();
            NetconfServerTestUtility.prepareSendQuery(transportManager, GET_ANSWER);

            final NetconfResponse response = netconfManager.getConfig(Datastore.RUNNING);
            assertEquals(GET_EXPECTED, response.getData());
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetConfig() throws NetconfManagerException {
        try {
            final NetconfManager netconfManager = NetconfManagerFactory.createNetconfManager(transportManager,
                    configProperties);

            NetconfServerTestUtility.prepareSendHello(transportManager);
            netconfManager.connect();

            NetconfServerTestUtility.prepareSendQuery(transportManager, GET_ANSWER);
            final NetconfResponse response = netconfManager.getConfig();

            assertEquals(GET_EXPECTED, response.getData());
        } catch (final Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

}

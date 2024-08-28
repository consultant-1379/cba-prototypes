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

package com.ericsson.oss.mediation.util.netconf.io;

import com.ericsson.oss.mediation.util.netconf.api.Capability;
import com.ericsson.oss.mediation.util.netconf.capability.NetconfSessionCapabilities;
import com.ericsson.oss.mediation.util.netconf.context.NetconfSession;
import com.ericsson.oss.mediation.util.netconf.io.TransportManagerHelper;
import com.ericsson.oss.mediation.util.netconf.operation.OperationType;
import com.ericsson.oss.mediation.util.netconf.server.NetconfServerTestUtility;
import com.ericsson.oss.mediation.util.netconf.vo.HelloVo;
import com.ericsson.oss.mediation.util.netconf.vo.NetconfVo;
import com.ericsson.oss.mediation.util.transport.api.TransportData;
import com.ericsson.oss.mediation.util.transport.api.TransportManager;
import com.ericsson.oss.mediation.util.transport.api.exception.TransportException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 
 * @author xvaltda
 */

public class TransportManagerHelperTest {

    private TransportManager transportManager;
    private TransportManagerHelper transportManagerHelper;
    private NetconfSession session;
    private NetconfSessionCapabilities netconfSessionCapabilities;

    public TransportManagerHelperTest() {
        transportManager = Mockito.mock(TransportManager.class);
        session = Mockito.mock(NetconfSession.class);
        netconfSessionCapabilities = Mockito.mock(NetconfSessionCapabilities.class);

        transportManagerHelper = new TransportManagerHelper(transportManager);

        List<Capability> ecimCapabilities = new ArrayList<Capability>();

        Capability capabilityBase = new Capability("urn:ietf:params:ns:netconf:base:1.0", "base",
                "ietf:params:ns:netconf", "1.0");
        Capability capabilityBaseXML = new Capability("urn:ietf:params:xml:ns:netconf:base:1.0", "base",
                "ietf:params:xml:ns:netconf", "1.0");

        ecimCapabilities.add(capabilityBase);
        ecimCapabilities.add(capabilityBaseXML);

        when(session.getNetconfSessionCapabilities()).thenReturn(netconfSessionCapabilities);
        when(netconfSessionCapabilities.getEcimCapabilities()).thenReturn(ecimCapabilities);

    }

    @Test
    public void testSendOperation() throws TransportException, Exception {

        NetconfServerTestUtility.prepareSendHello(transportManager);
        try {
            Assert.assertTrue(transportManagerHelper.sendNetconfOperation(OperationType.HELLO, null, session));
            verify(transportManager).sendData((TransportData) Matchers.anyObject());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetOperationResponse() throws Exception {

        NetconfServerTestUtility.prepareSendHello(transportManager);

        Assert.assertTrue(transportManagerHelper.sendNetconfOperation(OperationType.HELLO, null, session));
        verify(transportManager).sendData((TransportData) Matchers.anyObject());

        NetconfVo netconfVo = transportManagerHelper.getNetconfResponse(false);

        Assert.assertTrue((netconfVo instanceof HelloVo));

    }
}

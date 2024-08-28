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
import com.ericsson.oss.mediation.util.netconf.context.NetconfSession;
import com.ericsson.oss.mediation.util.netconf.flow.FlowInput;
import com.ericsson.oss.mediation.util.netconf.flow.FlowOutput;
import com.ericsson.oss.mediation.util.netconf.flow.FlowSession;
import com.ericsson.oss.mediation.util.netconf.io.RpcWriterComponent;
import com.ericsson.oss.mediation.util.netconf.operation.KillSession;
import com.ericsson.oss.mediation.util.netconf.operation.Operation;
import com.ericsson.oss.mediation.util.transport.api.TransportData;
import com.ericsson.oss.mediation.util.transport.api.TransportManager;
import com.ericsson.oss.mediation.util.transport.api.exception.TransportException;
import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mockito.Matchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 
 * @author xvaltda
 */
public class RpcWriterComponentTest {

    @Test
    public void testSendRpc() throws TransportException {

        final RpcWriterComponent rpcWriterComponent = new RpcWriterComponent();

        final FlowInput input = mock(FlowInput.class);
        final NetconfSession netconfSession = mock(NetconfSession.class);
        final FlowSession flowSession = mock(FlowSession.class);
        final List<Capability> activeCapabilities = mock(List.class);
        final TransportManager transportManager = mock(TransportManager.class);

        when(input.getFlowSession()).thenReturn(flowSession);
        when(input.getNetconfSession()).thenReturn(netconfSession);
        when(netconfSession.getTransportManager()).thenReturn(transportManager);

        when(activeCapabilities.isEmpty()).thenReturn(false);

        final FlowOutput output = rpcWriterComponent.execute(input);

        verify(flowSession, Mockito.times(1)).get("OPERATION");
        verify(transportManager, Mockito.times(1)).sendData((TransportData) any());
        assertFalse(output.isError());
    }

    @Test
    public void testSendRpcWithError() throws TransportException {

        final RpcWriterComponent rpcWriterComponent = new RpcWriterComponent();
        final Operation operation = new KillSession("12");

        final FlowInput input = mock(FlowInput.class);
        final NetconfSession netconfSession = mock(NetconfSession.class);
        final FlowSession flowSession = mock(FlowSession.class);
        final List<Capability> activeCapabilities = mock(List.class);
        final TransportManager transportManager = mock(TransportManager.class);

        when(input.getFlowSession()).thenReturn(flowSession);
        when(input.getFlowSession().get("OPERATION")).thenReturn(operation);

        when(input.getNetconfSession()).thenReturn(netconfSession);
        when(netconfSession.getTransportManager()).thenReturn(transportManager);

        when(activeCapabilities.isEmpty()).thenReturn(false);
        doThrow(new TransportException(new Exception("Transport Exception Test"), 10)).when(transportManager).sendData(
                (TransportData) any());

        final FlowOutput output = rpcWriterComponent.execute(input);

        verify(flowSession, Mockito.times(1)).get("OPERATION");
        verify(transportManager, Mockito.times(1)).sendData((TransportData) any());
        assertTrue(output.isError());
        assertTrue(output.getErrorMessage().contains("Transport Exception Test"));
    }

}

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

package com.ericsson.oss.mediation.util.netconf.operation;

import com.ericsson.oss.mediation.util.netconf.context.NetconfSession;
import com.ericsson.oss.mediation.util.netconf.flow.FlowInput;
import com.ericsson.oss.mediation.util.netconf.flow.FlowOutput;
import com.ericsson.oss.mediation.util.netconf.flow.FlowSession;
import com.ericsson.oss.mediation.util.transport.api.TransportData;
import com.ericsson.oss.mediation.util.transport.api.TransportManager;
import com.ericsson.oss.mediation.util.transport.api.exception.TransportException;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * 
 * @author xvaltda
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ OperationFactory.class })
public class OperationComponentTest {

    @Test
    public void testCreateOperation() throws TransportException {

        final KillSession killSession = new KillSession("123");
        final OperationComponent operationComponent = new OperationComponent();

        final FlowInput input = mock(FlowInput.class);
        final NetconfSession netconfSession = mock(NetconfSession.class);
        final FlowSession flowSession = mock(FlowSession.class);
        final TransportManager transportManager = mock(TransportManager.class);

        PowerMockito.mockStatic(OperationFactory.class);

        when(input.getOperationType()).thenReturn(OperationType.KILL_SESSION);
        when(input.getFlowSession()).thenReturn(flowSession);
        when(input.getNetconfSession()).thenReturn(netconfSession);
        when(netconfSession.getTransportManager()).thenReturn(transportManager);
        when(OperationFactory.create(eq(OperationType.KILL_SESSION), any(), (NetconfSession) any())).thenReturn(
                killSession);

        final FlowOutput output = operationComponent.execute(input);

        verify(flowSession, Mockito.times(1)).put(eq("OPERATION"), any());
        verify(transportManager, Mockito.times(0)).sendData((TransportData) any());
        assertFalse(output.isError());
    }

    @Test
    public void testCreateOperationWithError() throws TransportException {

        final KillSession killSession = new KillSession("123");
        final OperationComponent operationComponent = new OperationComponent();

        final FlowInput input = mock(FlowInput.class);
        final NetconfSession netconfSession = mock(NetconfSession.class);
        final FlowSession flowSession = mock(FlowSession.class);
        final TransportManager transportManager = mock(TransportManager.class);

        PowerMockito.mockStatic(OperationFactory.class);

        when(input.getOperationType()).thenReturn(null);
        when(input.getFlowSession()).thenReturn(flowSession);
        when(input.getNetconfSession()).thenReturn(netconfSession);
        when(netconfSession.getTransportManager()).thenReturn(transportManager);
        when(OperationFactory.create(eq(OperationType.KILL_SESSION), any(), (NetconfSession) any())).thenReturn(null);

        final FlowOutput output = operationComponent.execute(input);

        verify(flowSession, Mockito.times(0)).put(eq("OPERATION"), any());
        verify(transportManager, Mockito.times(0)).sendData((TransportData) any());
        assertTrue(output.isError());
        assertTrue(("operation type " + null + " is not supported by NetconfManager.").equals(output.getErrorMessage()));
    }

}

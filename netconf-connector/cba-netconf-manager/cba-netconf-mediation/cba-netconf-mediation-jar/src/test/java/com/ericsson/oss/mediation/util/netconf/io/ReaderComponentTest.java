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
import com.ericsson.oss.mediation.util.netconf.io.ReaderComponent;
import com.ericsson.oss.mediation.util.transport.api.TransportData;
import com.ericsson.oss.mediation.util.transport.api.TransportManager;
import com.ericsson.oss.mediation.util.transport.api.exception.TransportException;
import java.util.List;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import static org.mockito.Matchers.any;
import org.mockito.Mockito;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 
 * @author xvaltda
 */
public class ReaderComponentTest {
    @Test
    public void testReadTransportData() throws TransportException {

        final ReaderComponent readerComponent = new ReaderComponent();

        final FlowInput input = mock(FlowInput.class);
        final NetconfSession netconfSession = mock(NetconfSession.class);
        final FlowSession flowSession = mock(FlowSession.class);
        final TransportData transportData = new TransportData();
        final List<Capability> activeCapabilities = mock(List.class);
        final TransportManager transportManager = mock(TransportManager.class);

        when(input.getFlowSession()).thenReturn(flowSession);
        when(input.getNetconfSession()).thenReturn(netconfSession);
        when(netconfSession.getTransportManager()).thenReturn(transportManager);

        when(activeCapabilities.isEmpty()).thenReturn(false);

        final FlowOutput output = readerComponent.execute(input);

        verify(flowSession, Mockito.times(1)).put((String) any(), any());
        verify(transportManager, Mockito.times(1)).readData((TransportData) any(), anyBoolean());
        assertFalse(output.isError());
    }

    @Test
    public void testReadTransportDataTransportWithException() throws TransportException {

        final ReaderComponent readerComponent = new ReaderComponent();

        final FlowInput input = mock(FlowInput.class);
        final NetconfSession netconfSession = mock(NetconfSession.class);
        final FlowSession flowSession = mock(FlowSession.class);
        final TransportData transportData = new TransportData();
        final List<Capability> activeCapabilities = mock(List.class);
        final TransportManager transportManager = mock(TransportManager.class);

        when(input.getFlowSession()).thenReturn(flowSession);
        when(input.getNetconfSession()).thenReturn(netconfSession);
        when(netconfSession.getTransportManager()).thenReturn(transportManager);

        when(activeCapabilities.isEmpty()).thenReturn(false);
        when(flowSession.get("TRANSPORT_DATA")).thenReturn(transportData);

        final FlowOutput output = readerComponent.execute(input);

        verify(flowSession, Mockito.times(1)).put((String) any(), any());
        assertFalse(output.isError());
    }
}

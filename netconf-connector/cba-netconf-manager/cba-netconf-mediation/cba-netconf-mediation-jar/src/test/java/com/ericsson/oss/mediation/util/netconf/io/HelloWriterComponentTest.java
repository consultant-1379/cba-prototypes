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
import com.ericsson.oss.mediation.util.netconf.flow.FlowInput;
import com.ericsson.oss.mediation.util.netconf.flow.FlowOutput;
import com.ericsson.oss.mediation.util.netconf.flow.FlowSession;
import com.ericsson.oss.mediation.util.netconf.io.HelloWriterComponent;
import com.ericsson.oss.mediation.util.netconf.operation.Hello;
import com.ericsson.oss.mediation.util.transport.api.TransportData;
import com.ericsson.oss.mediation.util.transport.api.TransportManager;
import com.ericsson.oss.mediation.util.transport.api.exception.TransportException;
import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 
 * @author xvaltda
 */
public class HelloWriterComponentTest {

    @Test
    public void testExecuteWithoutError() throws TransportException {

        final HelloWriterComponent helloWriterComponent = new HelloWriterComponent();

        final FlowInput input = mock(FlowInput.class);
        final NetconfSession netconfSession = mock(NetconfSession.class);
        final FlowSession flowSession = mock(FlowSession.class);
        final NetconfSessionCapabilities netconfSessionCapabilities = mock(NetconfSessionCapabilities.class);
        final Hello hello = mock(Hello.class);
        final TransportData transportData = new TransportData();
        final List<Capability> activeCapabilities = mock(List.class);
        final TransportManager transportManager = mock(TransportManager.class);

        when(input.getFlowSession()).thenReturn(flowSession);
        when(input.getNetconfSession()).thenReturn(netconfSession);
        when(netconfSession.getNetconfSessionCapabilities()).thenReturn(netconfSessionCapabilities);
        when(netconfSession.getTransportManager()).thenReturn(transportManager);

        when(netconfSessionCapabilities.getActiveCapabilities()).thenReturn(activeCapabilities);
        when(activeCapabilities.isEmpty()).thenReturn(false);
        when(flowSession.get("OPERATION")).thenReturn(hello);
        when(hello.getTransportData()).thenReturn(transportData);

        final FlowOutput output = helloWriterComponent.execute(input);

        verify(transportManager, Mockito.times(1)).sendData(transportData);
        assertFalse(output.isError());
    }

    @Test
    public void testExecuteWithTransportError() {
        FlowOutput output = new FlowOutput();
        try {
            final HelloWriterComponent helloWriterComponent = new HelloWriterComponent();

            final FlowInput input = mock(FlowInput.class);
            final NetconfSession netconfSession = mock(NetconfSession.class);
            final FlowSession flowSession = mock(FlowSession.class);
            final NetconfSessionCapabilities netconfSessionCapabilities = mock(NetconfSessionCapabilities.class);
            final Hello hello = mock(Hello.class);
            final TransportData transportData = new TransportData();
            final List<Capability> activeCapabilities = mock(List.class);
            final TransportManager transportManager = mock(TransportManager.class);

            when(input.getFlowSession()).thenReturn(flowSession);
            when(input.getNetconfSession()).thenReturn(netconfSession);
            when(netconfSession.getNetconfSessionCapabilities()).thenReturn(netconfSessionCapabilities);
            when(netconfSession.getTransportManager()).thenReturn(transportManager);

            when(netconfSessionCapabilities.getActiveCapabilities()).thenReturn(activeCapabilities);
            when(activeCapabilities.isEmpty()).thenReturn(false);
            when(flowSession.get("OPERATION")).thenReturn(hello);
            when(hello.getTransportData()).thenReturn(transportData);
            doThrow(new TransportException(new Exception("Transport Exception Test"), 10)).when(transportManager)
                    .sendData(transportData);

            output = helloWriterComponent.execute(input);
            verify(transportManager, Mockito.times(1)).sendData(transportData);
            assertTrue(output.isError());
            assertTrue(output.getErrorMessage().contains("Transport Exception Test"));

        } catch (TransportException ex) {
            assertTrue(output.isError());

        }
    }
}

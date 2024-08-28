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
package com.ericsson.oss.mediation.util.netconf.flow;

import com.ericsson.oss.mediation.util.netconf.api.exception.NetconfManagerException;
import com.ericsson.oss.mediation.util.netconf.capability.CapabilityCreatorComponent;
import com.ericsson.oss.mediation.util.netconf.context.NetconfSession;
import com.ericsson.oss.mediation.util.netconf.flow.component.FlowComponent;
import com.ericsson.oss.mediation.util.netconf.io.HelloWriterComponent;
import com.ericsson.oss.mediation.util.netconf.io.ReaderComponent;
import com.ericsson.oss.mediation.util.netconf.io.RpcWriterComponent;
import com.ericsson.oss.mediation.util.netconf.operation.OperationComponent;
import com.ericsson.oss.mediation.util.netconf.parser.ParserComponent;
import com.ericsson.oss.mediation.util.netconf.validator.HelloVoValidator;
import com.ericsson.oss.mediation.util.netconf.validator.KillSessionValidator;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * 
 * @author xvaltda
 */
public class FlowFactoryTest {

    @Test
    public void testCreateHandshakeFlow() throws NetconfManagerException {

        final FlowComponent flowComponent = mock(FlowComponent.class);
        final FlowInput input = mock(FlowInput.class);
        final FlowOutput output = mock(FlowOutput.class);
        final NetconfSession netconfSession = mock(NetconfSession.class);
        final FlowSession flowSession = mock(FlowSession.class);

        when(input.getFlowSession()).thenReturn(flowSession);
        when(input.getNetconfSession()).thenReturn(netconfSession);
        when(flowComponent.execute((FlowInput) anyObject())).thenReturn(output);
        when(output.isError()).thenReturn(false);

        FlowComposite flowComposite = FlowFactory.createHandshakeFlow(netconfSession);

        assertTrue(flowComposite.getComponents().get(0) instanceof CapabilityCreatorComponent);
        assertTrue(flowComposite.getComponents().get(1) instanceof OperationComponent);
        assertTrue(flowComposite.getComponents().get(2) instanceof HelloWriterComponent);
        assertTrue(flowComposite.getComponents().get(3) instanceof ReaderComponent);
        assertTrue(flowComposite.getComponents().get(4) instanceof ParserComponent);
        assertTrue(flowComposite.getComponents().get(5) instanceof HelloVoValidator);

    }

    @Test
    public void testCreateGetConfigFlow() throws NetconfManagerException {

        final FlowComponent flowComponent = mock(FlowComponent.class);
        final FlowInput input = mock(FlowInput.class);
        final FlowOutput output = mock(FlowOutput.class);
        final NetconfSession netconfSession = mock(NetconfSession.class);
        final FlowSession flowSession = mock(FlowSession.class);

        when(input.getFlowSession()).thenReturn(flowSession);
        when(input.getNetconfSession()).thenReturn(netconfSession);
        when(flowComponent.execute((FlowInput) anyObject())).thenReturn(output);
        when(output.isError()).thenReturn(false);

        FlowComposite flowComposite = FlowFactory.createGetConfigFlow(netconfSession, null, null);

        assertTrue(flowComposite.getComponents().get(0) instanceof OperationComponent);
        assertTrue(flowComposite.getComponents().get(1) instanceof RpcWriterComponent);
        assertTrue(flowComposite.getComponents().get(2) instanceof ReaderComponent);
        assertTrue(flowComposite.getComponents().get(3) instanceof ParserComponent);

    }

    @Test
    public void testCreateGetFlow() throws NetconfManagerException {

        final FlowComponent flowComponent = mock(FlowComponent.class);
        final FlowInput input = mock(FlowInput.class);
        final FlowOutput output = mock(FlowOutput.class);
        final NetconfSession netconfSession = mock(NetconfSession.class);
        final FlowSession flowSession = mock(FlowSession.class);

        when(input.getFlowSession()).thenReturn(flowSession);
        when(input.getNetconfSession()).thenReturn(netconfSession);
        when(flowComponent.execute((FlowInput) anyObject())).thenReturn(output);
        when(output.isError()).thenReturn(false);

        FlowComposite flowComposite = FlowFactory.createGetFlow(netconfSession, null);

        assertTrue(flowComposite.getComponents().get(0) instanceof OperationComponent);
        assertTrue(flowComposite.getComponents().get(1) instanceof RpcWriterComponent);
        assertTrue(flowComposite.getComponents().get(2) instanceof ReaderComponent);
        assertTrue(flowComposite.getComponents().get(3) instanceof ParserComponent);

    }

    @Test
    public void testCreateCloseSessionFlow() throws NetconfManagerException {

        final FlowComponent flowComponent = mock(FlowComponent.class);
        final FlowInput input = mock(FlowInput.class);
        final FlowOutput output = mock(FlowOutput.class);
        final NetconfSession netconfSession = mock(NetconfSession.class);
        final FlowSession flowSession = mock(FlowSession.class);

        when(input.getFlowSession()).thenReturn(flowSession);
        when(input.getNetconfSession()).thenReturn(netconfSession);
        when(flowComponent.execute((FlowInput) anyObject())).thenReturn(output);
        when(output.isError()).thenReturn(false);

        FlowComposite flowComposite = FlowFactory.createCloseFlow(netconfSession);

        assertTrue(flowComposite.getComponents().get(0) instanceof OperationComponent);
        assertTrue(flowComposite.getComponents().get(1) instanceof RpcWriterComponent);
        assertTrue(flowComposite.getComponents().get(2) instanceof ReaderComponent);
        assertTrue(flowComposite.getComponents().get(3) instanceof ParserComponent);

    }

    @Test
    public void testCreateKillSessionFlow() throws NetconfManagerException {

        final FlowComponent flowComponent = mock(FlowComponent.class);
        final FlowInput input = mock(FlowInput.class);
        final FlowOutput output = mock(FlowOutput.class);
        final NetconfSession netconfSession = mock(NetconfSession.class);
        final FlowSession flowSession = mock(FlowSession.class);

        when(input.getFlowSession()).thenReturn(flowSession);
        when(input.getNetconfSession()).thenReturn(netconfSession);
        when(flowComponent.execute((FlowInput) anyObject())).thenReturn(output);
        when(output.isError()).thenReturn(false);

        FlowComposite flowComposite = FlowFactory.createKillSessionFlow("234", netconfSession);

        assertTrue(flowComposite.getComponents().get(0) instanceof KillSessionValidator);
        assertTrue(flowComposite.getComponents().get(1) instanceof OperationComponent);
        assertTrue(flowComposite.getComponents().get(2) instanceof RpcWriterComponent);
        assertTrue(flowComposite.getComponents().get(3) instanceof ReaderComponent);
        assertTrue(flowComposite.getComponents().get(4) instanceof ParserComponent);

    }

}

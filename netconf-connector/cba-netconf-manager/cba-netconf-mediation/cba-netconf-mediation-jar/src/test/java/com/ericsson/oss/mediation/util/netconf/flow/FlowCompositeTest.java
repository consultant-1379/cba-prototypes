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
import com.ericsson.oss.mediation.util.netconf.context.NetconfSession;
import com.ericsson.oss.mediation.util.netconf.flow.component.FlowComponent;
import com.ericsson.oss.mediation.util.netconf.operation.OperationType;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 
 * @author xvaltda
 */
public class FlowCompositeTest {

    private FlowComposite flowComposite;

    @Test
    public void compositeTestOutputWithoutError() throws NetconfManagerException {

        final FlowComponent flowComponent = mock(FlowComponent.class);
        final FlowInput input = mock(FlowInput.class);
        final FlowOutput output = mock(FlowOutput.class);
        final NetconfSession netconfSession = mock(NetconfSession.class);
        final FlowSession flowSession = mock(FlowSession.class);

        when(input.getFlowSession()).thenReturn(flowSession);
        when(input.getNetconfSession()).thenReturn(netconfSession);
        when(flowComponent.execute((FlowInput) anyObject())).thenReturn(output);
        when(output.isError()).thenReturn(false);

        flowComposite = new FlowComposite(netconfSession, OperationType.GET);

        flowComposite.addComponent(flowComponent);

        final FlowOutput output_test = flowComposite.execute();

        assertFalse(output_test.isError());

    }

    @Test
    public void compositeTestOutputError() throws NetconfManagerException {

        final FlowComponent flowComponent = mock(FlowComponent.class);
        final FlowInput input = mock(FlowInput.class);
        final FlowOutput output = mock(FlowOutput.class);
        final NetconfSession netconfSession = mock(NetconfSession.class);
        final FlowSession flowSession = mock(FlowSession.class);

        when(input.getFlowSession()).thenReturn(flowSession);
        when(input.getNetconfSession()).thenReturn(netconfSession);
        when(flowComponent.execute((FlowInput) anyObject())).thenReturn(output);
        when(output.isError()).thenReturn(true);

        flowComposite = new FlowComposite(netconfSession, OperationType.GET);

        flowComposite.addComponent(flowComponent);

        final FlowOutput output_test = flowComposite.execute();

        assertTrue(output_test.isError());

    }

    @Test
    public void compositeStopFlowWhenAnErrorIsReceived() throws NetconfManagerException {

        final FlowComponent flowComponent = mock(FlowComponent.class);
        final FlowComponent flowComponent2 = mock(FlowComponent.class);
        final FlowInput input = mock(FlowInput.class);
        final FlowOutput output = mock(FlowOutput.class);
        final FlowOutput output2 = mock(FlowOutput.class);
        final NetconfSession netconfSession = mock(NetconfSession.class);
        final FlowSession flowSession = mock(FlowSession.class);

        when(input.getFlowSession()).thenReturn(flowSession);
        when(input.getNetconfSession()).thenReturn(netconfSession);

        when(flowComponent.execute((FlowInput) anyObject())).thenReturn(output);
        when(flowComponent2.execute((FlowInput) anyObject())).thenReturn(output2);

        when(output.isError()).thenReturn(true);
        when(output2.isError()).thenReturn(false);

        flowComposite = new FlowComposite(netconfSession, OperationType.GET);

        flowComposite.addComponent(flowComponent);

        final FlowOutput output_test = flowComposite.execute();

        assertTrue(output_test.isError());
        verify(flowComponent2, times(0)).execute((FlowInput) anyObject());

    }

}

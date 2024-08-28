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

package com.ericsson.oss.mediation.util.netconf.validator;

import com.ericsson.oss.mediation.util.netconf.api.Capability;
import com.ericsson.oss.mediation.util.netconf.api.exception.NetconfManagerErrorMessages;
import com.ericsson.oss.mediation.util.netconf.api.exception.NetconfManagerException;
import com.ericsson.oss.mediation.util.netconf.capability.NetconfSessionCapabilities;
import com.ericsson.oss.mediation.util.netconf.context.NetconfSession;
import com.ericsson.oss.mediation.util.netconf.flow.FlowInput;
import com.ericsson.oss.mediation.util.netconf.flow.FlowOutput;
import com.ericsson.oss.mediation.util.netconf.flow.FlowSession;
import com.ericsson.oss.mediation.util.netconf.validator.HelloVoValidator;
import com.ericsson.oss.mediation.util.netconf.vo.HelloVo;
import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 
 * @author xvaltda
 */
public class HelloVoValidatorTest {

    @Test
    public void helloValidateTestNoError() throws NetconfManagerException {

        final HelloVoValidator helloValidator = new HelloVoValidator();
        final FlowInput input = mock(FlowInput.class);
        final NetconfSession netconfSession = mock(NetconfSession.class);
        final FlowSession flowSession = mock(FlowSession.class);
        final NetconfSessionCapabilities netconfSessionCapabilities = mock(NetconfSessionCapabilities.class);
        final HelloVo hello = mock(HelloVo.class);

        final List<Capability> activeCapabilities = mock(List.class);

        when(input.getFlowSession()).thenReturn(flowSession);
        when(input.getNetconfSession()).thenReturn(netconfSession);
        when(netconfSession.getNetconfSessionCapabilities()).thenReturn(netconfSessionCapabilities);
        when(netconfSessionCapabilities.getActiveCapabilities()).thenReturn(activeCapabilities);
        when(activeCapabilities.isEmpty()).thenReturn(false);
        when(flowSession.get("NETCONF_VO")).thenReturn(hello);

        when(hello.getSessionId()).thenReturn("12");

        final FlowOutput output = helloValidator.execute(input);

        assertFalse(output.isError());
        verify(netconfSessionCapabilities, times(1)).processCapabilities(hello);
    }

    @Test
    public void helloValidateTestSessionBadFormatError() {

        final HelloVoValidator helloValidator = new HelloVoValidator();
        final FlowInput input = mock(FlowInput.class);
        final NetconfSession netconfSession = mock(NetconfSession.class);
        final FlowSession flowSession = mock(FlowSession.class);
        final NetconfSessionCapabilities netconfSessionCapabilities = mock(NetconfSessionCapabilities.class);
        final HelloVo hello = mock(HelloVo.class);

        final List<Capability> activeCapabilities = mock(List.class);

        when(input.getFlowSession()).thenReturn(flowSession);
        when(input.getNetconfSession()).thenReturn(netconfSession);
        when(netconfSession.getNetconfSessionCapabilities()).thenReturn(netconfSessionCapabilities);
        when(netconfSessionCapabilities.getActiveCapabilities()).thenReturn(activeCapabilities);
        when(activeCapabilities.isEmpty()).thenReturn(false);
        when(flowSession.get("NETCONF_VO")).thenReturn(hello);

        when(hello.getSessionId()).thenReturn("12a");

        final FlowOutput output = helloValidator.execute(input);

        assertTrue(output.isError());
        assertTrue(NetconfManagerErrorMessages.SESSIONID_BAD_FORMAT.equals(output.getErrorMessage()));
    }

    @Test
    public void helloValidateTestSessionIdNotReceived() {

        final HelloVoValidator helloValidator = new HelloVoValidator();
        final FlowInput input = mock(FlowInput.class);
        final NetconfSession netconfSession = mock(NetconfSession.class);
        final FlowSession flowSession = mock(FlowSession.class);
        final NetconfSessionCapabilities netconfSessionCapabilities = mock(NetconfSessionCapabilities.class);
        final HelloVo hello = mock(HelloVo.class);

        final List<Capability> activeCapabilities = mock(List.class);

        when(input.getFlowSession()).thenReturn(flowSession);
        when(input.getNetconfSession()).thenReturn(netconfSession);
        when(netconfSession.getNetconfSessionCapabilities()).thenReturn(netconfSessionCapabilities);
        when(netconfSessionCapabilities.getActiveCapabilities()).thenReturn(activeCapabilities);
        when(activeCapabilities.isEmpty()).thenReturn(false);
        when(flowSession.get("NETCONF_VO")).thenReturn(hello);

        when(hello.getSessionId()).thenReturn("");

        final FlowOutput output = helloValidator.execute(input);

        assertTrue(output.isError());
        assertTrue(NetconfManagerErrorMessages.SESSIONID_WAS_NOT_RECEIVED.equals(output.getErrorMessage()));
    }

    @Test
    public void helloValidateTestRpcError() {

        final HelloVoValidator helloValidator = new HelloVoValidator();
        final FlowInput input = mock(FlowInput.class);
        final NetconfSession netconfSession = mock(NetconfSession.class);
        final FlowSession flowSession = mock(FlowSession.class);
        final NetconfSessionCapabilities netconfSessionCapabilities = mock(NetconfSessionCapabilities.class);
        final HelloVo hello = mock(HelloVo.class);

        final List<Capability> activeCapabilities = mock(List.class);

        when(input.getFlowSession()).thenReturn(flowSession);
        when(input.getNetconfSession()).thenReturn(netconfSession);
        when(netconfSession.getNetconfSessionCapabilities()).thenReturn(netconfSessionCapabilities);
        when(netconfSessionCapabilities.getActiveCapabilities()).thenReturn(activeCapabilities);
        when(activeCapabilities.isEmpty()).thenReturn(false);
        when(flowSession.get("NETCONF_VO")).thenReturn(hello);

        when(hello.getSessionId()).thenReturn("12");
        when(hello.isErrorReceived()).thenReturn(true);

        final FlowOutput output = helloValidator.execute(input);

        assertFalse(output.isError());

    }

}

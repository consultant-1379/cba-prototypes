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

import com.ericsson.oss.mediation.util.netconf.api.exception.NetconfManagerException;
import com.ericsson.oss.mediation.util.netconf.context.NetconfSession;
import com.ericsson.oss.mediation.util.netconf.flow.FlowInput;
import com.ericsson.oss.mediation.util.netconf.flow.FlowOutput;
import com.ericsson.oss.mediation.util.netconf.flow.FlowSession;
import com.ericsson.oss.mediation.util.netconf.validator.KillSessionValidator;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * 
 * @author xvaltda
 */
public class KillSessionValidatorTest {

    private final String ERROR_SESSION_EQUALS_TO_CURRENT_SESSION = "KillSession,  sessionId received is equals to"
            + "current sessionId " + ", " + "command cannot be executed";
    private final String ERROR_SESSION_SHOULD_BE_ONLY_DIGITS = "KillSession,  sessionId received is not valid, "
            + "it should be only digits " + "command cannot be executed";

    @Test
    public void KillSessionTestNoError() throws NetconfManagerException {

        final KillSessionValidator killSessionValidator = new KillSessionValidator("12");
        final FlowInput input = mock(FlowInput.class);
        final NetconfSession netconfSession = mock(NetconfSession.class);
        final FlowSession flowSession = mock(FlowSession.class);

        when(input.getFlowSession()).thenReturn(flowSession);
        when(input.getNetconfSession()).thenReturn(netconfSession);

        final FlowOutput output = killSessionValidator.execute(input);
        assertFalse(output.isError());

    }

    @Test
    public void KillSessionTestNoDigitsError() throws NetconfManagerException {

        final KillSessionValidator killSessionValidator = new KillSessionValidator("123");
        final FlowInput input = mock(FlowInput.class);
        final NetconfSession netconfSession = mock(NetconfSession.class);
        final FlowSession flowSession = mock(FlowSession.class);

        when(input.getFlowSession()).thenReturn(flowSession);
        when(input.getNetconfSession()).thenReturn(netconfSession);
        when(netconfSession.getSessionId()).thenReturn("123");

        final FlowOutput output = killSessionValidator.execute(input);
        assertTrue(output.isError());

        assertTrue(ERROR_SESSION_EQUALS_TO_CURRENT_SESSION.equals(output.getErrorMessage()));

    }

    @Test
    public void KillSessionTestSameSessionIdError() throws NetconfManagerException {

        final KillSessionValidator killSessionValidator = new KillSessionValidator("12a");
        final FlowInput input = mock(FlowInput.class);
        final NetconfSession netconfSession = mock(NetconfSession.class);
        final FlowSession flowSession = mock(FlowSession.class);

        when(input.getFlowSession()).thenReturn(flowSession);
        when(input.getNetconfSession()).thenReturn(netconfSession);

        final FlowOutput output = killSessionValidator.execute(input);
        assertTrue(output.isError());

        assertTrue(ERROR_SESSION_SHOULD_BE_ONLY_DIGITS.equals(output.getErrorMessage()));

    }

}

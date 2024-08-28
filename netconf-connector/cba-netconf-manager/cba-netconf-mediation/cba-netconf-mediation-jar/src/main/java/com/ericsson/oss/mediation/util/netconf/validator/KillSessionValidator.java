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

import com.ericsson.oss.mediation.util.netconf.flow.FlowInput;
import com.ericsson.oss.mediation.util.netconf.flow.FlowOutput;
import com.ericsson.oss.mediation.util.netconf.flow.component.FlowComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author xvaltda
 */
public class KillSessionValidator implements FlowComponent {

    private final String ERROR_SESSION_EQUALS_TO_CURRENT_SESSION = "KillSession,  sessionId received is equals to"
            + "current sessionId " + ", " + "command cannot be executed";
    private final String ERROR_SESSION_SHOULD_BE_ONLY_DIGITS = "KillSession,  sessionId received is not valid, "
            + "it should be only digits " + "command cannot be executed";
    private static final Logger logger = LoggerFactory.getLogger(KillSessionValidator.class);
    private final String sessionId;

    public KillSessionValidator(final String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public FlowOutput execute(final FlowInput input) {

        final FlowOutput output = new FlowOutput();

        if (!ValidatorUtil.isOnlyDigits(sessionId)) {
            logger.error(ERROR_SESSION_SHOULD_BE_ONLY_DIGITS);
            output.setErrorMessage(ERROR_SESSION_SHOULD_BE_ONLY_DIGITS);
            output.setError(true);

        } else if (sessionId.equals(input.getNetconfSession().getSessionId())) {
            logger.error(ERROR_SESSION_EQUALS_TO_CURRENT_SESSION);
            output.setErrorMessage(ERROR_SESSION_EQUALS_TO_CURRENT_SESSION);
            output.setError(true);
        }

        return output;
    }

}

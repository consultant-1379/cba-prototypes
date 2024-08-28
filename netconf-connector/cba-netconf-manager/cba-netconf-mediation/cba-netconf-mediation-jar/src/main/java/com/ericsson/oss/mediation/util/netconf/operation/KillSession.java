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

public class KillSession extends Operation {

    private final String sessionId;

    public KillSession(final String aSessionId) {
        operationType = OperationType.KILL_SESSION;
        sessionId = aSessionId;
    }

    @Override
    public String getBody() {
        final String body = "<kill-session>\n" + "<session-id>" + sessionId + "</session-id>\n" + "</kill-session>\n";

        return body;
    }
}
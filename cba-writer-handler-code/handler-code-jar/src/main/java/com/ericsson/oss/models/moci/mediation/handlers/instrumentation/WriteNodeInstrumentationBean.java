/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2015
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.models.moci.mediation.handlers.instrumentation;

import static com.ericsson.oss.itpf.sdk.recording.ErrorSeverity.ERROR;
import static com.ericsson.oss.itpf.sdk.recording.EventLevel.COARSE;
import static com.ericsson.oss.models.moci.mediation.handlers.api.Constants.MO_FDN_EQUALS;
import static com.ericsson.oss.models.moci.mediation.handlers.api.Constants.RIGHT_SQUARE_BRAC;
import static com.ericsson.oss.models.moci.mediation.handlers.api.Constants.WRITE_NODE_CONTROLLER_HANDLER_THREAD_ID;
import static com.ericsson.oss.models.moci.mediation.handlers.api.Constants.WRITE_NODE_ENTRY_OPERATION;
import static com.ericsson.oss.models.moci.mediation.handlers.api.Constants.WRITE_NODE_EXIT_OPERATION;
import static com.ericsson.oss.models.moci.mediation.handlers.api.Constants.WRITE_NODE_OPERATION_IN_ERROR;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.ericsson.oss.itpf.sdk.recording.SystemRecorder;

/**
 * Instrumented Bean for <code>WriteNodeControllerHandler</code>
 *
 * @author eranagg
 *
 */
@ApplicationScoped
public class WriteNodeInstrumentationBean {

    @Inject
    private SystemRecorder recorder;

    /**
     * Records the entry point to <code>WriteNodeControllerHandler</code> for a mediation request.
     *
     * @param fdn
     *            of the ManagedObject
     * @param operation
     *            operation being performed
     * @param action
     *            action on an MO if performed otherwise null
     *
     */
    public void recordWriteNodeEntryPoint(final String fdn, final String operation, final String action) {
        recorder.recordEvent(buildEventString(WRITE_NODE_ENTRY_OPERATION, operation), COARSE, buildThreadId(),
                buildMoFdnString(fdn), action);
    }

    /**
     * Records the exit point to <code>WriteNodeControllerHandler</code> for a mediation request.
     *
     * @param fdn
     *            of the ManagedObject
     * @param operation
     *            operation being performed
     * @param action
     *            action on an MO if performed otherwise null
     */
    public void recordWriteNodeExitPoint(final String fdn, final String operation, final String action) {
        recorder.recordEvent(buildEventString(WRITE_NODE_EXIT_OPERATION, operation), COARSE, buildThreadId(),
                buildMoFdnString(fdn), action);
    }

    /**
     * Records an error during processing in the <code>WriteNodeControllerHandler</code>.
     *
     * @param fdn
     *            of the ManagedObject
     * @param operation
     *            operation being performed
     * @param exceptionMessage
     *            The Exception message.
     */

    public void recordError(final String fdn, final String operation, final String exceptionMessage) {
        recorder.recordError(buildEventString(WRITE_NODE_OPERATION_IN_ERROR, operation), ERROR, buildThreadId(),
                buildMoFdnString(fdn), exceptionMessage);
    }

    private String buildMoFdnString(final String fdn) {
        final StringBuilder sb = new StringBuilder(MO_FDN_EQUALS);
        sb.append(fdn);
        sb.append(RIGHT_SQUARE_BRAC);
        return sb.toString();
    }

    private String buildEventString(final String eventType, final String operation) {
        final StringBuilder event = new StringBuilder(eventType);
        event.append(" = ");
        event.append(operation);
        return event.toString();

    }

    private String buildThreadId(){
        final StringBuilder threadId = new StringBuilder(WRITE_NODE_CONTROLLER_HANDLER_THREAD_ID);
        threadId.append(Thread.currentThread().getId());
        return threadId.toString();
    }

}

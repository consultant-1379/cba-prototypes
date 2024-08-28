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

package com.ericsson.oss.mediation.util.transport.ssh.exception;

/**
 * 
 * @author xvaltda
 */

public enum ExceptionReason {

    VALIDATION_FAILURE(-1, "VALIDATION_FAILURE"), APPLICATION_LEVEL_FAILURE(0, "APPLICATION_LEVEL_FAILURE"), TRANSPORT_LEVEL_FAILURE(
            1, "TRANSPORT_LEVEL_FAILURE"), CONNECTION_ESTABLISHMENT_FAILURE(2, "CONNECTION_ESTABLISHMENT_FAILURE",
            "(Check login credentials and server availability)"), SUBSYTEM_CONNECTION_FAILURE(3,
            "SUBSYTEM_CONNECTION_FAILURE"), SYNCH_QUERY_FAILURE(4, "SYNCH_QUERY_FAILURE"), ASYNCH_QUERY_FAILURE(5,
            "ASYNCH_QUERY_FAILURE"), SESSION_DISCONNECTION_FAILURE(6, "SESSION_DISCONNECTION_FAILURE"), CONNECTION_REMOVAL_FAILURE(
            7, "CONNECTION_REMOVAL_FAILURE"), UNSUPPORTED_QUERY_TYPE(8, "UNSUPPORTED_QUERY_TYPE"),
    //UNSUPPORTED_SESSION_TYPE(8, "UNSUPPORTED_SESSION_TYPE", "valid session Types are : "
    //+ java.util.Arrays.asList(SessionType.values())), 
    INPUT_STREAM_READING_FAILURE(9, "INPUT_STREAM_READING_FAILURE"), SSH_SESSION_NOT_AVAILABLE(10,
            "SSH_SESSION_NOT_AVAILABLE"), SSH_CONNECTION_NOT_AVAILABLE(11, "SSH_CONNECTION_NOT_AVAILABLE"), QUERY_EXECUTION_FAILURE(
            12, "QUERY_EXECUTION_FAILURE"), INVALID_INPUT_NULL_BUFFER(11, "TRANPOSRT INPUT BUFFER CANNOT BE NULL"),

    CONNECTION_WAS_LOST(20, "The Node Connection was lost, nothing to do...");

    private int causeCodeId;
    private String causeName;
    private String helpMessage = "";

    /**
     * @param causeCodeId
     * @param causeName
     */
    private ExceptionReason(final int causeCodeId, final String causeName) {
        this.causeCodeId = causeCodeId;
        this.causeName = causeName;
    }

    /**
     * @param causeCodeId
     * @param causeName
     * @param helpMessage
     */
    private ExceptionReason(final int causeCodeId, final String causeName, final String helpMessage) {
        this.causeCodeId = causeCodeId;
        this.causeName = causeName;
        this.helpMessage = helpMessage;
    }

    /**
     * @return
     */
    public int getCauseCodeId() {
        return causeCodeId;
    }

    /**
     * @return
     */
    public String getCauseName() {
        return causeName;
    }

    /**
     * @return
     */
    public String getHelpMessage() {
        return helpMessage;
    }
}

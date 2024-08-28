/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.mediation.adapter.tls.exception;

public enum ExceptionReason {

    APPLICATION_LEVEL_FAILURE(0, "APPLICATION_LEVEL_FAILURE"), TRANSPORT_LEVEL_FAILURE(1, "TRANSPORT_LEVEL_FAILURE"), HANDSHAKE_FAILURE(2,
            "TLS handshake failure"), TLS_WRITE_FAILURE(3, "CONNECTION_REMOVAL_FAILURE"), TLS_READ_FAILURE(4, "CONNECTION_REMOVAL_FAILURE"), TIMEOUT(
            5, "TIMEOUT"), SECURITY_FAILURE(6, "SECURITY_FAILURE"), ALREADY_CONNECTED(7, "ALREADY_CONNECTED"), ERROR_STATE(8,
            "SYSTEM IS IN ERROR STATE");

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

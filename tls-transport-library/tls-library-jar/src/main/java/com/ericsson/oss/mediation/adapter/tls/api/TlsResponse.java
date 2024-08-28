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
package com.ericsson.oss.mediation.adapter.tls.api;

/**
 * This class is used to hold any response messages received from the peer
 * or any error message received when performing operations.
 * @author Team Sovereign
 * @version 1.0.42
 * @since 2015-01-15
 */
public class TlsResponse {

    private final StringBuilder responseMessage = new StringBuilder();
    private String errorMessage;
    private String failedCommand;
    private boolean isSuccess;

    public String getResponseMessageString() {
        return responseMessage.toString();
    }

    public StringBuilder getResponseMessage() {
        return responseMessage;
    }

    public void addToResponseMessage(final String responseMessage) {
        this.responseMessage.append(responseMessage);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(final boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getFailedCommand() {
        return failedCommand;
    }

    public void setFailedCommand(final String failedCommand) {
        this.failedCommand = failedCommand;
    }

    @Override
    public String toString() {
        String ret = null;
        if (isSuccess()) {
            ret = "responseMessage: " + responseMessage;
        } else {
            ret = "errorMessage: " + errorMessage;
        }
        return ret;
    }
}

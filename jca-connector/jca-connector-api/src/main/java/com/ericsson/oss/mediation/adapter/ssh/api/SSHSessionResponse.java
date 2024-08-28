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
package com.ericsson.oss.mediation.adapter.ssh.api;

/**
 * 
 */
public class SSHSessionResponse {

    private final StringBuilder responseMessage = new StringBuilder();

    private String errorMessage;
    
    private String failedCommand;

    private boolean isSuccess;

    /**
     * @return responseMessage as a String
     */
    public String getResponseMessageString() {
        return responseMessage.toString();
    }
    
    /**
     * @return responseMessage
     */
    public StringBuilder getResponseMessage() {
        return responseMessage;
    }


    /**
     * @param responseMessage
     */
    public void addToResponseMessage(final String responseMessage) {
        this.responseMessage.append(responseMessage);
    }

    /**
     * @return errorMessage String
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage
     */
    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return isSuccess
     */
    public boolean isSuccess() {
        return isSuccess;
    }

    /**
     * @param isSuccess
     */
    public void setSuccess(final boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    
    public String getFailedCommand() {
        return failedCommand;
    }

    /**
     * @param failedCommand
     */
    public void setFailedCommand(final String failedCommand) {
        this.failedCommand = failedCommand;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
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

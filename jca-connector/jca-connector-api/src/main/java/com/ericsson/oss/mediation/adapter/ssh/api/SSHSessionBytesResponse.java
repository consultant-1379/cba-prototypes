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
public class SSHSessionBytesResponse {

    private byte[] responseBytes ;

    private String errorMessage;

    private String failedCommand;

    private boolean isSuccess;

    /**
     * @return responseMessage as a String
     */
    public String getResponseMessageString() {
        return responseBytes.toString();
    }




    /**
     * @return the responseBytes
     */
    public byte[] getResponseBytes() {
        return responseBytes;
    }




    /**
     * @param responseBytes the responseBytes to set
     */
    public void addResponseBytes(final byte[] responseBytes) {
        //        if(this.responseBytes== null)
        this.responseBytes = responseBytes;
        //        else
        //        {
        //            final int currentLength = this.responseBytes.length;
        //            final int lengthToAdd = responseBytes.length;
        //            final int finalLength = currentLength + lengthToAdd;
        //            final byte[] responseArr = new byte[finalLength];
        //            System.arraycopy(this.responseBytes, 0, responseArr, 0, currentLength);
        //            System.arraycopy(responseBytes, 0, responseArr, currentLength, lengthToAdd);
        //        }
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
            ret = "responseMessage: " + responseBytes.toString();
        } else {
            ret = "errorMessage: " + errorMessage;
        }
        return ret;
    }
}

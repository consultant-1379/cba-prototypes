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
package com.ericsson.cba.prototype.protocol.sshclient.model;

/**
 * indicates validation failed when the provided ssh-input is not valid.
 * 
 */
public class SshClientInputException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * @param exception
     */
    public SshClientInputException(final Exception exception) {
        super(exception);
    }

    /**
     * @param message
     */
    public SshClientInputException(final String message) {
        super(message);
    }
}

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
 * Indicates exception during the query reading/writing.
 * 
 */
public class QueryProcessingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * @param message
     */
    public QueryProcessingException(final String message) {
        super(message);
    }

    /**
     * @param message
     * @param throwable
     */
    public QueryProcessingException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}

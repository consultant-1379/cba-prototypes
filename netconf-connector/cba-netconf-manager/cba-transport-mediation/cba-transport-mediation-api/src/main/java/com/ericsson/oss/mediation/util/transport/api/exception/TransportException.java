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
package com.ericsson.oss.mediation.util.transport.api.exception;

/**
 * 
 * @author xvaltda
 */
public class TransportException extends Exception {
    private int severity = 0;

    public TransportException(final Exception ex) {
        super(ex);
    }

    public TransportException(final Exception ex, final int severity) {
        super(ex);
        this.severity = severity;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(final int severity) {
        this.severity = severity;
    }

}

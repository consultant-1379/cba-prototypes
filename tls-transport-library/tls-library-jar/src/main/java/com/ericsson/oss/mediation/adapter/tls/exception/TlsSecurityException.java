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

public class TlsSecurityException extends Exception {

    private static final long serialVersionUID = 1L;

    public TlsSecurityException(final Exception exception) {
        super(exception);
    }

    /**
     * @param causeCode
     */
    public TlsSecurityException(final ExceptionReason causeCode) {
        super(" TLS TransportException CAUSE_CODE : " + causeCode.getCauseName() + " (" + causeCode.getCauseCodeId()
                + ")");
    }

    /**
     * @param causeCode
     * @param description
     */
    public TlsSecurityException(final ExceptionReason causeCode, final String description) {
        super(" TLS TransportException CAUSE_CODE : " + causeCode.getCauseName() + " (" + causeCode.getCauseCodeId()
                + ")" + " description : " + description);

    }

    public TlsSecurityException(final String message, final TlsException exception) {
        super(message, exception);
    }
}
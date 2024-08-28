package com.ericsson.oss.mediation.adapter.tls.exception;
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

public class TlsException extends Exception {

    private static final long serialVersionUID = 1L;

    public TlsException(final Exception exception) {
        super(exception);
    }

    public TlsException(final String message, final Exception exception) {
        super(message, exception);
    }

    public TlsException(final ExceptionReason causeCode) {
        super(" TlsException CAUSE_CODE : " + causeCode.getCauseName() + " (" + causeCode.getCauseCodeId()
                + ")");
    }

    public TlsException(final ExceptionReason causeCode, final String message) {
        super(" TlsException CAUSE_CODE : " + causeCode.getCauseName() + " (" + causeCode.getCauseCodeId()
                + ")" + " description : " + message);

    }
    
    public TlsException(final ExceptionReason causeCode, final Exception exception) {
        super(" TlsException CAUSE_CODE : " + causeCode.getCauseName() + " (" + causeCode.getCauseCodeId()
                + ")", exception);
    }
}

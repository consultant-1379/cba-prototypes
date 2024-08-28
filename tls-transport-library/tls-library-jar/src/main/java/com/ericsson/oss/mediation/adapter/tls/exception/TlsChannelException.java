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

public class TlsChannelException extends Exception {

    private static final long serialVersionUID = 1L;

    public TlsChannelException(final Exception exception) {
        super(exception);
    }

    public TlsChannelException(final String str) {
        super(str);
    }

    public TlsChannelException(final Exception exception, final String message) {
        super(message, exception);
    }

    public TlsChannelException(final ExceptionReason causeCode) {
        super("TlsChannelException CAUSE_CODE : " + causeCode.getCauseName() + " (" + causeCode.getCauseCodeId() + ")");
    }

    public TlsChannelException(final Exception exception, final ExceptionReason causeCode) {
        super(" TLS TransportException CAUSE_CODE : " + causeCode.getCauseName() + " (" + causeCode.getCauseCodeId() + ")", exception);
    }

}

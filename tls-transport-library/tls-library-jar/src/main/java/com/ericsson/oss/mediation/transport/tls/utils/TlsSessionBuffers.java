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
package com.ericsson.oss.mediation.transport.tls.utils;

public class TlsSessionBuffers {

    private final StringBuffer input;
    private Boolean isReadComplete = true;

    public TlsSessionBuffers() {
        input = new StringBuffer();
    }

    public synchronized StringBuffer getInput() {
        return input;
    }

    public synchronized Boolean isReadComplete() {
        return isReadComplete;
    }

    public synchronized void setReadComplete(final Boolean value) {
        isReadComplete = value;
    }

    public synchronized void clearInputBuffer() {
        getInput().delete(0, getInput().length());
    }

}

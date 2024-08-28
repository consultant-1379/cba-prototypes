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

package com.ericsson.oss.mediation.util.netconf.api.exception;

public class NetconfManagerException extends Exception {

    private static final long serialVersionUID = 3195270654268678170L;

    public NetconfManagerException() {
        super();
    }

    public NetconfManagerException(final String message) {
        super(message);
    }

    public NetconfManagerException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NetconfManagerException(final Throwable cause) {
        super(cause);
    }

}
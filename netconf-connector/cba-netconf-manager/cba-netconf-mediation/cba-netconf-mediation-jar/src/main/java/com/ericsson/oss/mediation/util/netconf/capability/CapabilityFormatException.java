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

package com.ericsson.oss.mediation.util.netconf.capability;

/**
 * 
 * @author xshakku
 * 
 */
public class CapabilityFormatException extends Exception {

    private static final long serialVersionUID = -8197325206323857034L;

    public CapabilityFormatException() {
        super();
    }

    public CapabilityFormatException(final String message) {
        super(message);
    }

    public CapabilityFormatException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CapabilityFormatException(final Throwable cause) {
        super(cause);
    }

}
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
package com.ericsson.oss.mediation.adapter.ssh.provider;

public class InitializeSSHSessionException extends CLISessionException {

    private static final long serialVersionUID = 1L;

    InitializeSSHSessionException(final String message) {
        super(message);
    }

    InitializeSSHSessionException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
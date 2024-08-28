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

/**
 *
 */
public class CLISessionException extends Exception {

    private static final long serialVersionUID = 1L;
    private String lastCommandOuput;

    CLISessionException(final String message) {
        super(message);
        this.lastCommandOuput = "";
    }

    CLISessionException(final String message, final String lastCommandOuput) {
        super(message);
        this.lastCommandOuput = lastCommandOuput;
    }

    CLISessionException(final String message, final Throwable cause) {
        super(message, cause);
        this.lastCommandOuput = "";
    }

    CLISessionException(final String message, final Throwable cause, final String lastCommandOuput) {
        super(message, cause);
        this.lastCommandOuput = lastCommandOuput;
    }

    final void setLastCommandOuput(final String lastCommandOuput) {
        this.lastCommandOuput = lastCommandOuput;
    }

    public final String getLastCommandOuput() {
        return this.lastCommandOuput;
    }
}
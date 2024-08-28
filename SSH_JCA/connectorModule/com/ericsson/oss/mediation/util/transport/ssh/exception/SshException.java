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

package com.ericsson.oss.mediation.util.transport.ssh.exception;

/**
 *
 * @author xvaltda
 */
public class SshException extends Exception {

    private static final long serialVersionUID = 1L;

    public SshException (final Exception e) {
        super (e);
    }
    /**
     * @param causeCode
     */
    public SshException(final ExceptionReason causeCode) {
        super(" SSH TransportException CAUSE_CODE : " + causeCode.getCauseName() + " (" + causeCode.getCauseCodeId() + ")");
    }

    /**
     * @param causeCode
     * @param description
     */
    public SshException(final ExceptionReason causeCode, final String description) {
        super(" SSH TransportException CAUSE_CODE : " + causeCode.getCauseName() + " (" + causeCode.getCauseCodeId() + ")" + " description : "
                + description);
    }
}

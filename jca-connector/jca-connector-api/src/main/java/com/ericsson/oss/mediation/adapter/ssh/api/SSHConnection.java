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
package com.ericsson.oss.mediation.adapter.ssh.api;


/**
 * 
 */
public interface SSHConnection {

    /**
     * method to close the connection and give back to the pool.
     * This method will not terminate the underlying SSH session.
     */
    void close();

    /**
     * Method to close the connection and underlying SSH session.
     */
    void closeAndTerminateSSH();

    /**
     * Method to check whether the underlying SSH session is alive or not.
     */
    boolean isAlive();

    /**
     * @param commands
     * @return
     */
    SSHSessionBytesResponse executeCommandBytes(String... commands);

    /**
     * @param commands
     * @return
     */
    SSHSessionBytesResponse readResponseBytes();
}

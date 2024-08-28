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
package com.ericsson.cba.prototype.protocol.sshclient.ssh;

import com.ericsson.cba.prototype.protocol.sshclient.exception.SshTransportException;
import com.ericsson.cba.prototype.protocol.sshclient.model.SessionType;
import com.ericsson.cba.prototype.protocol.sshclient.model.query.Query;

public interface SshClient {

    /**
     * @param sshConnection
     * @param sessionType
     * @param sessionTypeValue
     * @return
     * @throws SshTransportException
     */
    SshSession createSessionToSubsystem(SshConnection sshConnection, SessionType sessionType, String sessionTypeValue) throws SshTransportException;

    /**
     * @param hostname
     * @param port
     * @param username
     * @param password
     * @return
     * @throws SshTransportException
     */
    SshConnection createSshConnection(String hostname, int port, String username, String password) throws SshTransportException;

    /**
     * @param hostname
     * @param port
     * @param username
     * @param password
     * @param socketConnectionTimeoutInMillis
     * @return
     * @throws SshTransportException
     */
    SshConnection createSshConnection(String hostname, int port, String username, String password, int socketConnectionTimeoutInMillis)
            throws SshTransportException;

    /**
     * @param sshSession
     * @return
     * @throws SshTransportException
     */
    boolean disconnectSession(SshSession sshSession) throws SshTransportException;

    /**
     * @param sshConnection
     * @return
     * @throws SshTransportException
     */
    boolean removeConnection(SshConnection sshConnection) throws SshTransportException;

    /**
     * @param sshSession
     * @param query
     * @return
     * @throws SshTransportException
     */
    String runQuery(SshSession sshSession, Query query) throws SshTransportException;

}

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
package com.ericsson.cba.prototype.protocol.sshclient.handler;

import com.ericsson.cba.prototype.protocol.sshclient.exception.ExceptionReason;
import com.ericsson.cba.prototype.protocol.sshclient.exception.SshTransportException;
import com.ericsson.cba.prototype.protocol.sshclient.model.SessionType;
import com.ericsson.cba.prototype.protocol.sshclient.model.SshClientInputException;
import com.ericsson.cba.prototype.protocol.sshclient.ssh.*;

/**
 * initializes connection parameters.
 * 
 */
public class SshHandlerInitializer {

    private SshConnection sshConnection = null;
    private SshSession sshSession = null;
    private final SshClient sshClient;

    public SshHandlerInitializer(final HandlerContext context) {
        sshClient = new JschSshHandler();
        try {
            initialize(context);
        } catch (final Exception e) {
            throw new SshClientInputException(e);
        }
    }

    public SshClient getSshClient() {
        return sshClient;
    }

    public SshConnection getSshConnection() {
        return sshConnection;
    }

    public SshSession getSshSession() {
        return sshSession;
    }

    private void initialize(final HandlerContext context) throws SshTransportException {
        final String hostname = context.getHostName();
        final Integer port = context.getPort();
        final Integer socketTimeoutValueInMilli = context.getSocketTimeoutValueInMilli();
        final String username = context.getUsername();
        final String password = context.getPassword();
        final String sessionType = context.getSessionType();
        final String sessionTypeValue = context.getSessionTypeValue();

        if (socketTimeoutValueInMilli == -1) {//infinite timeout
            sshConnection = sshClient.createSshConnection(hostname, port, username, password);
        } else {
            sshConnection = sshClient.createSshConnection(hostname, port, username, password, socketTimeoutValueInMilli);
        }
        if ("subsystem".equalsIgnoreCase(sessionType) && "netconf".equalsIgnoreCase(sessionTypeValue)) {
            sshSession = sshClient.createSessionToSubsystem(sshConnection, SessionType.SUBSYSTEM, sessionTypeValue);
        } else {
            sshClient.removeConnection(sshConnection);
            throw new SshClientInputException(new SshTransportException(ExceptionReason.UNSUPPORTED_SESSION_TYPE));
        }
    }

}

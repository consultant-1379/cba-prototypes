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
package com.ericsson.oss.mediation.adapter.ssh;

import javax.resource.spi.ConnectionRequestInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.adapter.ssh.api.*;
import com.ericsson.oss.mediation.adapter.ssh.provider.CLISessionException;
import com.ericsson.oss.mediation.adapter.ssh.provider.SSHSessionProvider;

/**
 * SSHConnectionImpl
 * 
 * @author eilanag
 */
public class SSHConnectionImpl implements SSHConnection {

    /** The logger */
    private static Logger log = LoggerFactory.getLogger(SSHConnectionImpl.class.getName());

    /** ManagedConnection */
    private final SSHManagedConnection managedConnection;

    /** ManagedConnectionFactory */
    private SSHManagedConnectionFactory managedConnectionFactory;

    private final SSHSessionRequest reqInfo;

    SSHSessionProvider session;

    /**
     * Default constructor
     * 
     * @param managedConnection
     *            SSHManagedConnection
     * @param managedConnectionFactory
     *            SSHManagedConnectionFactory
     */
    public SSHConnectionImpl(final SSHManagedConnection managedConnection, final SSHManagedConnectionFactory managedConnectionFactory,
            final ConnectionRequestInfo cxRequestInfo) {
        log.debug("In SSHConnectionImpl constructor");
        this.managedConnection = managedConnection;
        this.setMcf(managedConnectionFactory);
        this.reqInfo = (SSHSessionRequest) cxRequestInfo;
    }

    /**
     * Close
     */
    @Override
    public void close() {
        log.debug("In close method");
        managedConnection.closeHandle(this);
    }

    /**
     * @return the mcf
     */
    public SSHManagedConnectionFactory getMcf() {
        return managedConnectionFactory;
    }

    public SSHSessionRequest getReqInfo() {
        return reqInfo;
    }

    /**
     * @param managedConnectionFactory
     *            the mcf to set
     */
    private void setMcf(final SSHManagedConnectionFactory managedConnectionFactory) {
        this.managedConnectionFactory = managedConnectionFactory;
    }


    @Override
    public  SSHSessionBytesResponse executeCommandBytes(final String... commands) {
        SSHSessionBytesResponse response =null;
        String cmd = null;
        if (session != null) {
            try {
                response = new SSHSessionBytesResponse();
                for (final String cliCommand : commands) {
                    log.debug("calling executeCommandBytes for the command" + cmd );
                    cmd = cliCommand;
                    this.composeResponse(session.executeCommandwithBytes(cliCommand), response);
                }
            } catch (final Exception e) {
                log.trace("executeCommand for the command" + cmd + " Failed : " + e.getMessage());
            }
        }
        return response;
    }



    private void composeResponse(final byte[] reply, final SSHSessionBytesResponse response) {
        if ((reply != null) && (reply.length > 0)) {
            response.addResponseBytes(reply);
            response.setSuccess(true);
        }
    }


    @Override
    public boolean isAlive() {
        if (this.session != null) {
            return this.session.isConnected();
        }
        return false;
    }

    @Override
    public void closeAndTerminateSSH() {
        closeSSHSession();
        close();
    }

    public void closeSSHSession() {
        if (this.session != null) {
            this.session.closeSession();
            this.session = null;
        }

    }

    public void createSSHSession() throws CLISessionException {
        if (this.session == null) {
            this.session = new SSHSessionProvider(reqInfo.getIpAddress(), reqInfo.getPort(), reqInfo.getUsername(), reqInfo.getPassword(),
                    reqInfo.getWaitTimeForResponse(), reqInfo.getCommandPromptPattern(), reqInfo.getSubsystem(), reqInfo.getPatternPrefix(),
                    reqInfo.getPatternSuffix());
            this.session.openSession(reqInfo.getMaxSessionAuthReachedMsgPattern());
        }

    }

    /* (non-Javadoc)
     * @see com.ericsson.oss.mediation.adapter.ssh.api.SSHConnection#readResponseBytes(java.lang.String)
     */
    @Override
    public SSHSessionBytesResponse readResponseBytes() {
        SSHSessionBytesResponse response =null;
        final String cmd = null;
        if (session != null) {
            try {
                response = new SSHSessionBytesResponse();
                log.debug("calling executeCommandBytes for the command" + cmd );
                this.composeResponse(session.readCommandResBytes(), response);
            } catch (final Exception e) {
                log.trace("executeCommand for the command" + cmd + " Failed : " + e.getMessage());
            }
        }
        return response;
    }

}

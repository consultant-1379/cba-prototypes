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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maverick.ssh.ChannelOpenException;
import com.maverick.ssh.PasswordAuthentication;
import com.maverick.ssh.SshAuthentication;
import com.maverick.ssh.SshClient;
import com.maverick.ssh.SshConnector;
import com.maverick.ssh.SshException;
import com.maverick.ssh.SshIOException;
import com.maverick.ssh.SshSession;
import com.maverick.ssh2.Ssh2Session;
import com.sshtools.net.SocketTransport;

public final class SSHSessionProvider extends CLISessionProvider {

    private static final Logger LOG = LoggerFactory.getLogger(SSHSessionProvider.class);

    static {
        try {
            final String fileName = "/license/maverick_license.txt";
            final BufferedReader bufferReader = new BufferedReader(new InputStreamReader(SSHSessionProvider.class.getResourceAsStream(fileName)));
            final StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\r\n");
            }
            LOG.debug("license String" + stringBuilder.toString());
            com.maverick.ssh.LicenseManager.addLicense(stringBuilder.toString());
        } catch (Exception exc) {
            LOG.error("J2SSH Maverick License setting failure", exc);
        }

    }

    private SshClient sshClient;
    private SshSession sshSession;
    private SocketTransport socketTransport;

    public SSHSessionProvider(final String ipAddress, final int port, final String username, final String password, final int waitTimeForResponse,
                              final String commandPromptPattern, final String subsystem, final String patternPrefix, final String patterSuffix)
            throws InvalidPatternException {
        super(ipAddress, port, username, password, waitTimeForResponse, commandPromptPattern, subsystem, patternPrefix, patterSuffix);
    }

    @Override
    public final void openSession(final String maxSessionAuthReachedMsgPattern) throws OpenConnectionException, InitializeSSHSessionException,
            TimeoutExpiredException, AuthenticationErrorException, ErrorPatternFoundException, InvalidPatternException, SocketTimeoutChangeException,
            CLISessionException {
        try {
            /* Socket initialization */
            LOG.debug("Initialization of Socket (" + this.ipAddress + ":" + this.port + ") in progress...");
            this.socketTransport = new SocketTransport(this.ipAddress, this.port);
            this.changeSocketTimeout(this.getWaitTimeForResponse()); // Wait time for response setting

            LOG.debug("Initialization of Socket (" + this.ipAddress + ":" + this.port + ") completed");
        } catch (IOException exc) {
            throw new OpenConnectionException("Socket initialization failure", exc);
        }

        try {
            LOG.debug("SSH connection to NE " + this.ipAddress + " in progress...");
            // Connect to Network Element by SSHv2 protocol (SSHv1 protocol version is used if SSHv2 is not supported)
            // Unused buffered mode in order to permit socket timeout changing
            this.sshClient = SshConnector.getInstance().connect(this.socketTransport, this.username, false);
            LOG.info("SSH connection to NE " + this.ipAddress + this.port + " established successfully");
        } catch (SshException exc) {
            throw new OpenConnectionException("SSH connection failed", exc);
        }

        int authenticationState = -1;
        try {
            final PasswordAuthentication pwd = new PasswordAuthentication();
            pwd.setPassword(this.password);
            LOG.debug("Authentication on NE " + this.ipAddress + " in progress...");
            authenticationState = this.sshClient.authenticate(pwd); // User authentication
        } catch (SshException exc) {
            throw new AuthenticationErrorException("Authentication error", exc);
        }

        switch (authenticationState) {
            case SshAuthentication.COMPLETE: {
                LOG.info("Authentication on NE " + this.ipAddress + this.port + " completed successfully");
                if (this.subsystem != null && !this.subsystem.trim().isEmpty()) {
                    this.startSessionAndSubsystem();
                } else {
                    this.startSession(this.validateMaxSessionAuthReachedMsgPattern(maxSessionAuthReachedMsgPattern)); // Starting of the user shell
                }
                break;
            }
            default: {
                throw new AuthenticationErrorException("Authentication failed - Error code: " + authenticationState);
            }
        }
        LOG.info("SSH Session to NE " + this.ipAddress + this.port + " opened successfully");
    }

    private void startSession(final Pattern maxSessionAuthReachedMsgPattern) throws InitializeSSHSessionException, TimeoutExpiredException,
            ErrorPatternFoundException, CLISessionException {
        try {
            LOG.debug("Opening of a SSH Session Channel to NE " + this.ipAddress + " in progress...");
            this.sshSession = this.sshClient.openSessionChannel();
            LOG.info("SSH Session Channel to NE " + this.ipAddress + " opened successfully");

            // vt100,0,0
            if (!this.sshSession.requestPseudoTerminal("vt100", 0, 0, 0, 0)) {
                throw new InitializeSSHSessionException("Cannot allocate Pseudo Terminal into the SSH Session Channel");
            }
            // Starting of the user default shell
            if (!this.sshSession.startShell()) {
                throw new InitializeSSHSessionException("Cannot start the User Default Shell");
            }
            // Getting of the Session Channels OutputStream and InputStream
            this.setStdIn(this.sshSession.getOutputStream());
            this.setStdOut(this.sshSession.getInputStream());
            LOG.info("Waiting for \"" + this.getCommandPrompt() + "\" pattern...");
            // Waiting for command prompt
            final String output = readOutputCommand(this.getCommandPrompt(), maxSessionAuthReachedMsgPattern);
            LOG.debug("Output after login: \"" + output + "\"");
        } catch (ChannelOpenException exc) {
            throw new InitializeSSHSessionException("Opening of a SSH Session Channel failed", exc);
        } catch (SshIOException exc) {
            throw new InitializeSSHSessionException("Cannot get the Session Channels OutputStream and InputStream", exc);
        } catch (SshException exc) {
            throw new InitializeSSHSessionException("Cannot open/configure the SSH Session Channel", exc);
        }
    }

    private void startSessionAndSubsystem() throws InitializeSSHSessionException, TimeoutExpiredException, ErrorPatternFoundException,
            CLISessionException {
        try {
            LOG.debug("Opening of a SSH Session Channel to NE " + this.ipAddress + " in progress...");
            this.sshSession = this.sshClient.openSessionChannel();
            LOG.info("SSH Session Channel to NE " + this.ipAddress + " opened successfully");
            ((Ssh2Session) this.sshSession).startSubsystem(this.subsystem);

            // Getting of the Session Channels OutputStream and InputStream
            this.setStdIn(this.sshSession.getOutputStream());
            this.setStdOut(this.sshSession.getInputStream());
        } catch (ChannelOpenException exc) {
            throw new InitializeSSHSessionException("Opening of a SSH Session Channel failed", exc);
        } catch (SshIOException exc) {
            throw new InitializeSSHSessionException("Cannot get the Session Channels OutputStream and InputStream", exc);
        } catch (SshException exc) {
            throw new InitializeSSHSessionException("Cannot open/configure the SSH Session Channel", exc);
        }
    }

    @Override
    protected final void setSocketTimeout(final int timeout) throws SocketException {
        this.socketTransport.setSoTimeout(timeout);
        LOG.trace("SocketTimeout changed: " + this.socketTransport.getSoTimeout());
    }

    @Override
    public final boolean isConnected() {
        return (this.sshClient != null && this.sshClient.isConnected() && !sshSession.isClosed());
    }

    @Override
    public final void closeSession() {
        try {
            LOG.info("Closing of the SSH Session to NE " + this.ipAddress + " in progress...");
            if (this.sshSession != null) {
                // Closing of the SSH Session
                this.sshSession.close();
                if (LOG.isInfoEnabled()) {
                    LOG.info("SSH Session Channel to NE " + this.ipAddress + " closed");
                }
            }
            if (this.sshClient != null) {
                // Disconnecting of the SSH Client
                this.sshClient.disconnect();
                if (LOG.isInfoEnabled()) {
                    LOG.info("Connection to NE " + this.ipAddress + " terminated");
                }
            }
            if (this.socketTransport != null) {
                // Closing of the Socket and Session Channel�s OutputStream and InputStream
                this.socketTransport.close();
                if (LOG.isInfoEnabled()) {
                    LOG.info("Socket (" + this.ipAddress + ":" + this.port + ") closed");
                }
            }
            if (LOG.isInfoEnabled()) {
                LOG.info("Closing of the SSH Session to NE " + this.ipAddress + " completed");
            }
        } catch (IOException exc) {
            LOG.error("An I/O error is occurred during the SSH Session closing for NE " + this.ipAddress, exc);
        }
    }
}
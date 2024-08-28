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

package com.ericsson.oss.mediation.util.transport.ssh;

import com.ericsson.oss.mediation.util.transport.api.TransportData;
import com.ericsson.oss.mediation.util.transport.api.TransportManagerCI;
import com.ericsson.oss.mediation.util.transport.api.exception.TransportException;
import com.ericsson.oss.mediation.util.transport.ssh.exception.ExceptionReason;
import com.ericsson.oss.mediation.util.transport.ssh.exception.SshException;
import com.ericsson.oss.mediation.util.transport.ssh.exception.SshIllegalStateException;
import com.jcraft.jsch.ChannelSubsystem;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author xvaltda
 */
public class SshContext {

    private final TransportManagerCI transportManagerCI;

    private StringBuilder transportManagerDataBuffer;
    private SshConnection sshConnection = null;
    private SshSession sshSession = null;
    private TransportData transportData;
    private JSch sshLibrary = null;

    private SshTransportFSM sshTransportFSM;
    private static final Logger logger = LoggerFactory.getLogger(SshContext.class);

    public SshTransportFSM getState() {
        return sshTransportFSM;
    }

    public void setState(final SshTransportFSM state) {
        sshTransportFSM = state;
    }

    protected void setsshLibrary(final JSch sshLibraryparam) {
        sshLibrary = sshLibraryparam;
    }

    public SshContext(final TransportManagerCI transportManagerCI) {
        this.transportManagerCI = transportManagerCI;
        sshTransportFSM = SshTransportFSM.IDLE;
        sshLibrary = new JSch();
    }

    public void openChannel() throws SshException {
        try {
            sshTransportFSM.openchannel(this);
        } catch (final SshIllegalStateException ex) {
            logger.error("Error openning ssh's channel");
            throw new SshException(ex);
        }
    }

    public void closeConnection() throws SshException {
        try {
            sshTransportFSM.disconnect(this);
        } catch (final SshIllegalStateException ex) {
            logger.error("Error closing the ssh's connection");
            throw new SshException(ex);
        }
    }

    public void connect() throws SshException {
        try {
            sshTransportFSM.connect(this);
        } catch (final SshIllegalStateException ex) {
            logger.error("Error creating the ssh's connection");
            throw new SshException(ex);
        }
    }

    public void sendRequest(final TransportData request) throws SshException, TransportException {
        if (request == null) {
            throw new SshException(ExceptionReason.INVALID_INPUT_NULL_BUFFER);
        }
        try {
            this.transportData = request;
            sshTransportFSM.write(this);
        } catch (final SshIllegalStateException ex) {
            logger.error("Error sending the ssh's request");
            throw new SshException(ex);
        }
    }

    public void getResponse(final TransportData buffer, final boolean close) throws SshException, TransportException {
        try {
            if (buffer == null) {
                throw new SshException(ExceptionReason.INVALID_INPUT_NULL_BUFFER);
            }
            transportData = buffer;
            sshTransportFSM.read(this, close);

        } catch (final SshIllegalStateException ex) {
            logger.error("Error reading the ssh's connection");
            throw new SshException(ex);
        }

    }

    public boolean disconnect() throws SshException {
        try {

            final ChannelSubsystem channel = (ChannelSubsystem) sshSession.getSession();
            channel.disconnect();
            logger.info("ssh's channel was closed ");

            final Session session = (Session) sshConnection.getConnection();
            session.disconnect();
            logger.info("ssh's connection was closed ");

        } catch (final Exception e) {
            throw new SshException(ExceptionReason.SESSION_DISCONNECTION_FAILURE, e.getMessage());
        }
        return true;
    }

    private void write(final TransportData request) throws IOException {

        transportManagerDataBuffer = new StringBuilder();
        request.writeTo(transportManagerDataBuffer);
        logger.info("sending ssh's request \n" + transportManagerDataBuffer.toString());
        request.writeTo(sshSession.getOutputStream());
    }

    public void doRead(final boolean close) throws SshException, TransportException {
        try {
            readSocket(close);
            sshTransportFSM.onSuccess(this);
        } catch (final IOException e) {
            sshTransportFSM.onFailure(this);
            throw new SshException(e);
        }
    }

    public void doWrite() {
        try {
            write(transportData);
            sshTransportFSM.onSuccess(this);
        } catch (final IOException ex) {
            logger.error("Error writing in the ssh's socket");
            sshTransportFSM.onFailure(this);
        }
    }

    public void doCreateSshConnection() throws SshException {
        sshConnection = new SshConnection();

        // final JSch sshLibrary = new JSch();

        try {

            final Session session = sshLibrary.getSession(transportManagerCI.getUsername(),
                    transportManagerCI.getHostname(), transportManagerCI.getPort());

            session.setPassword(transportManagerCI.getPassword());
            session.setTimeout(transportManagerCI.getSocketConnectionTimeoutInMillis());

            final java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");

            session.setConfig(config);
            session.connect();

            sshConnection.setConnection(session);

            Thread.sleep(100l);
            sshTransportFSM.onSuccess(this);
        } catch (final Exception e) {

            sshTransportFSM.onFailure(this);
            throw new SshException(ExceptionReason.CONNECTION_ESTABLISHMENT_FAILURE, " hostname : "
                    + transportManagerCI.getHostname() + " port : " + transportManagerCI.getPort() + " username : "
                    + transportManagerCI.getUsername() + " password : " + "" + " exception : " + e.getMessage());
        }
    }

    public void doCreateSshSession() throws SshException {
        try {
            crateSshSession();
            sshTransportFSM.onSuccess(this);
        } catch (final SshException ex) {
            logger.error("Error creating the ssh's connection");
            sshTransportFSM.onFailure(this);
            throw new SshException(ex);
        }
    }

    private void crateSshSession() throws SshException {

        try {
            final Session session = (com.jcraft.jsch.Session) sshConnection.getConnection();

            final ChannelSubsystem sshChannel = (ChannelSubsystem) session.openChannel(transportManagerCI
                    .getSessionType().getValue());

            sshChannel.setSubsystem(transportManagerCI.getSessionTypeValue());
            sshSession = new SshSession(sshChannel, sshChannel.getInputStream(), sshChannel.getOutputStream());
            sshChannel.connect();

        } catch (final Exception e) {
            throw new SshException(ExceptionReason.SUBSYTEM_CONNECTION_FAILURE, e.getMessage());
        }
    }

    public void doDisconnect() throws SshException {
        try {
            disconnect();
            sshTransportFSM.onSuccess(this);
        } catch (final SshException ex) {
            logger.error("Error in ssh's disconnect");
            throw new SshException(ex);
        }
    }

    private void readSocket(final boolean close) throws IOException, SshException, TransportException {
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(sshSession.getInputStream(),
                    Charset.forName("UTF-8")));
            final StringBuilder response = new StringBuilder();
            int ascii;
            ascii = reader.read();
            while (ascii != transportData.getEndData() && ascii != -1) {
                response.append((char) ascii);
                ascii = reader.read();
            }
            if (!close && !isConnected()) {
                // trying to clean resource
                logger.error("The SSH connection was lost while trying to read the buffer");
                sshTransportFSM.onFailure(this);
                throw new TransportException(new SshException(ExceptionReason.CONNECTION_WAS_LOST), 10);
            }
            transportData.setData(response);
            logger.debug("reading the ssh connection, data received:\n " + response.toString());
        } catch (final IOException ex) {
            logger.error("Error reading the ssh socket");
            throw ex;
        }

    }

    public boolean isConnected() {
        final Session session = (Session) sshConnection.getConnection();
        if (session.isConnected()) {
            final ChannelSubsystem channel = (ChannelSubsystem) sshSession.getSession();
            return !channel.isClosed() && channel.isConnected();
        }
        return false;
    }

}

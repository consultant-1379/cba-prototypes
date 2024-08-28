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

package com.ericsson.oss.mediation.util.transport.ssh.manager;

import com.ericsson.oss.mediation.util.transport.api.TransportData;
import com.ericsson.oss.mediation.util.transport.api.TransportManager;
import com.ericsson.oss.mediation.util.transport.api.TransportManagerCI;
import com.ericsson.oss.mediation.util.transport.api.exception.TransportConnectionRefusedException;
import com.ericsson.oss.mediation.util.transport.api.exception.TransportException;
import com.ericsson.oss.mediation.util.transport.ssh.SshContext;
import com.ericsson.oss.mediation.util.transport.ssh.exception.SshException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author xvaltda
 */
public class SshTransportManagerImpl implements TransportManager {

    private static final Logger logger = LoggerFactory.getLogger(SshTransportManagerImpl.class);
    private final SshContext sshContext;
    private final TransportManagerCI transportManagerCI;

    SshTransportManagerImpl(final TransportManagerCI transportManagerCI) {
        sshContext = new SshContext(transportManagerCI);
        this.transportManagerCI =transportManagerCI;
    }

    @Override
    public void openConnection() throws TransportConnectionRefusedException, TransportException {
        try {
            sshContext.connect();
            sshContext.openChannel();
        } catch (final SshException ex) {
            throw new TransportConnectionRefusedException(ex);
        }
    }

    @Override
    public void closeConnection() throws TransportException {
        try {
            sshContext.closeConnection();
        } catch (final SshException ex) {
            throw new TransportException(ex);
        }
    }

    @Override
    public void sendData(final TransportData request) throws TransportException {
        try {
            sshContext.sendRequest(request);
        } catch (final SshException ex) {
            logger.error("Error sending requesto to the ssh connection ");
            throw new TransportException(ex);
        }
    }

    @Override
    public void readData(final TransportData response, final boolean close) throws TransportException {
        try {
            sshContext.getResponse(response, close);
        } catch (final SshException ex) {
            logger.error("Error reading the ssh connection");
            throw new TransportException(ex);
        }
    }

    @Override
    public void readData(final TransportData response) throws TransportException {
        this.readData(response, false);
    }

    @Override
    public String getProtocolType() {
        return "SSH";
    }

    @Override
    public TransportManagerCI getTransportManagerCI() {
        return transportManagerCI;
    }
}

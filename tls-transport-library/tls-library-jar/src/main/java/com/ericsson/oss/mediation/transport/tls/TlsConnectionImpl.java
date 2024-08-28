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
package com.ericsson.oss.mediation.transport.tls;

import com.ericsson.oss.mediation.adapter.tls.api.TlsConnection;
import com.ericsson.oss.mediation.adapter.tls.api.TlsConnectionRequest;
import com.ericsson.oss.mediation.adapter.tls.api.TlsRequest;
import com.ericsson.oss.mediation.adapter.tls.api.TlsResponse;
import com.ericsson.oss.mediation.adapter.tls.exception.ExceptionReason;
import com.ericsson.oss.mediation.adapter.tls.exception.TlsChannelException;
import com.ericsson.oss.mediation.adapter.tls.exception.TlsException;
import com.ericsson.oss.mediation.adapter.tls.exception.TlsIllegalStateException;
import com.ericsson.oss.mediation.transport.tls.utils.TlsSessionBuffers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.ericsson.oss.mediation.adapter.tls.exception.ExceptionReason.TLS_WRITE_FAILURE;
import static com.ericsson.oss.mediation.adapter.tls.exception.ExceptionReason.TRANSPORT_LEVEL_FAILURE;

public class TlsConnectionImpl implements TlsConnection {

    private static final long DEFAULT_TIMEOUT = 5000;
    private static final Logger logger = LoggerFactory.getLogger(TlsConnectionImpl.class);

    private TlsTransportFSM tlsTransportFSM;
    private TlsChannel tlsChannel;
    private TlsSessionBuffers tlsSessionBuffers = null;

    public TlsConnectionImpl(final TlsConnectionRequest reqInfo) {
        this.tlsTransportFSM = TlsTransportFSM.IDLE;
        this.tlsSessionBuffers = new TlsSessionBuffers();
        this.tlsChannel = new TlsChannel(reqInfo, this.tlsSessionBuffers);
    }

    @Override
    public void connect() throws TlsException {
        try {
            tlsTransportFSM.connect(this);
        } catch (final TlsIllegalStateException | IOException e) {
            logger.error(e.getMessage());
            throw new TlsException(TRANSPORT_LEVEL_FAILURE, e);
        }
    }

    protected void doConnect() throws TlsException, IOException {
        logger.info("Initializing TLS connection.");
        try {
        	getTlsChannel().initialise();
            logger.info("channel initialisation complete.. Beginning handshake.....");
        	getTlsChannel().handshake();
            logger.info("handshake complete.. Connected to node.");
        	getTlsChannel().initializeResponseHandler();
        } catch (final TlsChannelException e) {
            logger.error("Error initialising TLS Channel." + e.getMessage());
            tlsTransportFSM.onFailure(this);
            throw new TlsException(e.getMessage(), e);
        }
    }

    @Override
    public void disconnect() throws TlsException {
        try {
            tlsTransportFSM.disconnect(this);
        } catch (final TlsIllegalStateException ex) {
            logger.error("Error closing the TLS connection : " + ex.getMessage());
            throw new TlsException(TRANSPORT_LEVEL_FAILURE);
        }
    }

    protected void doDisconnect() throws TlsException {
        try {
            //stop the response handler before shutting down the connection
        	getTlsChannel().stopResponseHandler();
        	getTlsChannel().shutDownTlsConnection();
        } catch (final TlsChannelException e) {
            logger.error("Error closing the TLS connection : " + e.getMessage());
            tlsTransportFSM.onFailure(this);
            throw new TlsException(TRANSPORT_LEVEL_FAILURE);
        }
    }

    @Override
    public void sendNonBlocking(final TlsRequest tlsRequest) throws TlsException {
        try {
            this.tlsTransportFSM.sendNonBlocking(this, tlsRequest);
        } catch (final TlsIllegalStateException e) {
            logger.error("Error writing to the TLS connection : " + e.getMessage());
            tlsTransportFSM.onFailure(this);
            throw new TlsException(TLS_WRITE_FAILURE);
        }
    }

    protected void doNonBlockingWrite(final TlsRequest tlsRequest) throws TlsException {
        try {

            //If read thread is not running before we do a write then start a new thread
            if(!tlsChannel.getResponseHandler().isAlive()){
                tlsChannel.initializeResponseHandler();
            }
        	getTlsChannel().writeData(tlsRequest.getRequest());
            this.tlsTransportFSM.onNonBlockingWriteSuccess(this);
        } catch (TlsIllegalStateException | TlsChannelException e) {
            logger.error("Exception while writing data to the network " + e.getMessage());
            tlsTransportFSM.onFailure(this);
            throw new TlsException(TLS_WRITE_FAILURE);
        }
    }

    @Override
    public TlsResponse read() throws TlsException {

    	final long startTime = System.currentTimeMillis();

        //wait until data is available in buffer
        while (!tlsSessionBuffers.isReadComplete()) {
        	if (timedOut(startTime)) {
                throw new TlsException(ExceptionReason.TIMEOUT);
            }

            if(!tlsChannel.getResponseHandler().isAlive()){
                throw new TlsException(ExceptionReason.TLS_READ_FAILURE);
            }
        }

        final TlsResponse response = new TlsResponse();
        response.addToResponseMessage(tlsSessionBuffers.getInput().toString());
        tlsSessionBuffers.clearInputBuffer();
        tlsSessionBuffers.setReadComplete(false);
        return response;
    }

    /**
     * @return the tlsChannel
     */
    @Override
    public TlsChannel getTlsChannel() {
        return tlsChannel;
    }

    protected TlsTransportFSM getState() {
        return tlsTransportFSM;
    }

    protected void setState(final TlsTransportFSM state) {
        tlsTransportFSM = state;
    }

    private boolean timedOut(final long startTime) {
        return (System.currentTimeMillis() - startTime > DEFAULT_TIMEOUT);
    }

    /**
     * Unit Test Only
     * 
     */

    protected void setTlsSessionBuffers(final TlsSessionBuffers sessionBuffers){
        this.tlsSessionBuffers = sessionBuffers;
    }

    protected void setTlsChannel(final TlsChannel tlsChannel) {
        this.tlsChannel = tlsChannel;
    }
}

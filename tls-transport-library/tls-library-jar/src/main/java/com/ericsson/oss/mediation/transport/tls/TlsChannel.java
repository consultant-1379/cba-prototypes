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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.*;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.adapter.tls.api.TlsConnectionRequest;
import com.ericsson.oss.mediation.adapter.tls.exception.*;
import com.ericsson.oss.mediation.transport.tls.utils.TlsSecurityUtil;
import com.ericsson.oss.mediation.transport.tls.utils.TlsSessionBuffers;

/**
 * This class is the implementation of the Tls protocol and is used
 * by {@link TlsConnectionImpl} to interact with a Tls Node
 *
 * @author Team Sovereign
 * @version 1.0.42
 * @since 2015-01-15
 */
public class TlsChannel {

    private static final long DEFAULT_TIMEOUT = 5000;
    private static final Logger logger = LoggerFactory.getLogger(TlsChannel.class);

    private SSLEngine engine;
    private SocketChannel channel;
    private long timeOut;

    private TlsConnectionRequest reqInfo;
    private TlsSessionBuffers tlsSessionBuffers;
    private ByteBuffer outputApplicationData;
    private ByteBuffer outputNetworkData;
    private ByteBuffer peerApplicationData;
    private ByteBuffer peerNetworkData;
    private CharsetDecoder charsetDecoder;
    private TlsResponseHandler tlsResponseHandler;

    /**
     * @param reqInfo {@link TlsConnectionRequest} Object containing all details needed to connect to node
     * @param tlsSessionBuffers {@link TlsSessionBuffers} object which is used to store data read from node
     */
    protected TlsChannel(final TlsConnectionRequest reqInfo, final TlsSessionBuffers tlsSessionBuffers) {
        this.reqInfo = reqInfo;
        this.tlsSessionBuffers = tlsSessionBuffers;
        timeOut = DEFAULT_TIMEOUT;
    }

    /**
     * Used to create the necessary java.net and java.nio objects
     * used as part of the TLS implementation in java.
     * @throws TlsChannelException If there was an issue with the creation of any of these objects
     */
    protected void initialise() throws TlsChannelException {
        try {
            setupEngine();
        } catch (final TlsSecurityException e) {
            logger.error("TLS security error");
            throw new TlsChannelException(e, ExceptionReason.SECURITY_FAILURE);
        }

        try {
            setupChannel();
        } catch (final TimeoutException e) {
            logger.error("TLS channel setup timeout");
            throw new TlsChannelException(e, ExceptionReason.TIMEOUT);
        } catch (final IOException e) {
            logger.error("IO error while connecting to TLS channel");
            throw new TlsChannelException(e, ExceptionReason.TRANSPORT_LEVEL_FAILURE);
        }
        initialiseBuffers();
        setCharsetDecoder();
    }

    /**
     * Creates the {@link SSLEngine} object used to enable secure
     * communications with the node.
     * @throws TlsSecurityException If there was a problem with creation of SSLContext.
     */
    protected void setupEngine() throws TlsSecurityException {
        logger.info("Setting up TLS Engine");
        SSLContext sslContext = TlsSecurityUtil.createSslContext();
        engine = sslContext.createSSLEngine();

        //
        engine.setUseClientMode(true);
        logger.info("TLS Engine setup complete");
    }

    /**
     * Creates the SocketChannel and connects to the node using the details provided.
     * @throws TimeoutException If connection to node is not successful in the given timeout length.
     * @throws IOException If there is a problem when connecting to the node.
     */

    protected void setupChannel() throws TimeoutException, IOException {
        logger.info("Setting up Socket Channel for TLS connection");
        openSocketChannel();
        logger.info("openSocketChannel complete");
        connectChannel(getReqInfoIPAddress(), getReqInfoPort());
        logger.info("Socket Channel for TLS connection setup complete");
    }

    /**
     * Creates the SocketChannel to be used for communication with the node.
     * @throws IOException IOException If there is a problem when connection to the node.
     */
    protected void openSocketChannel() throws IOException {
        channel = SocketChannel.open();
        channel.configureBlocking(false);
    }

    /**
     * Connects the SocketChannel to the node.
     * @throws IOException if there is a problem when connection to the node.
     * @throws TimeoutException If connection to node is not successful in the given timeout length.
     */
    protected void connectChannel(final String ipAddress, final int port) throws IOException, TimeoutException {
        final long startTime = System.currentTimeMillis();
        channel.connect(new InetSocketAddress(ipAddress, port));
        // waiting for channel to connect.
        while (!channel.finishConnect()) {
            if (timedOut(startTime)) {
                logger.error("Timeout while trying to establish socket connection to IP address " + ipAddress + " on port " + port);
                throw new TimeoutException("Timeout while trying to open socket channel");
            }
        }

    }

    /**
     * Initialises the ByteBuffers used to handle the reading and writing of data.
     */
    protected void initialiseBuffers() {
        // peerApplicationData - holds the unencrypted data which was received from the node and unwrapped by the SSLEngine.
        peerApplicationData = ByteBuffer.allocate(getSession().getApplicationBufferSize());

        // peerNetworkData - holds the data read from the node in bytes, ready to be unwrapped to peerApplicationData.
        peerNetworkData = ByteBuffer.allocate(getSession().getPacketBufferSize());

        // outputApplicationData - holds the data to be sent to the node before encryption (SSLEngine.wrap())
        outputApplicationData = ByteBuffer.allocate(getSession().getApplicationBufferSize());

        // outNetworkData - holds the data ready to be sent to the node after the call to SSLEngine.wrap().
        outputNetworkData = ByteBuffer.allocate(getSession().getPacketBufferSize());

        logger.info("Initialized buffers for TLS connection");
    }

    /**
     * Performs the Tls Handshake with the node, handling the different handshake states in the SSLEngine.
     * @throws TlsChannelException If there is an issue while performing the TLS Handshake with the node.
     */
    protected void handshake() throws TlsChannelException {
        final long startTime = System.currentTimeMillis();
        SSLEngineResult sslEngineResult;
        try {
            engine.beginHandshake();
            SSLEngineResult.HandshakeStatus handShakeStatus = engine.getHandshakeStatus();

            // Process handshaking message
            while (handShakeStatus != SSLEngineResult.HandshakeStatus.FINISHED && handShakeStatus != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {

                switch (handShakeStatus) {
                    case NEED_UNWRAP:
                        // Receive handshaking data from node
                        sslEngineResult = readFromSocketChannel();
                        handShakeStatus = sslEngineResult.getHandshakeStatus();
                        switch (sslEngineResult.getStatus()) {
                            case OK:
                                break;
                            case BUFFER_OVERFLOW:
                                logger.error("BUFFER_OVERFLOW during NEED_UNWRAP during handshake");
                                throw new TlsChannelException("BUFFER_OVERFLOW during handshake");
                            case CLOSED:
                                logger.error("The channel was closed during handshake");
                                throw new TlsChannelException("The channel was closed during handshake");

                        }
                        break;

                    case NEED_WRAP:
                        // Empty the local network packet buffer.
                        getOutputNetworkData().clear();

                        // Generate handshaking data
                        sslEngineResult = engine.wrap(getOutputApplicationData(), getOutputNetworkData());
                        handShakeStatus = sslEngineResult.getHandshakeStatus();
                        switch (sslEngineResult.getStatus()) {
                            case OK:
                                getOutputNetworkData().flip();
                                // Send the handshaking data to node
                                while (getOutputNetworkData().hasRemaining()) {
                                    getSocketChannel().write(getOutputNetworkData());
                                    if (timedOut(startTime)) {
                                        throw new TimeoutException("Timeout while handshaking");
                                    }
                                }
                                break;
                            case BUFFER_OVERFLOW:
                                throw new TlsChannelException("BUFFER_OVERFLOW during handshake");
                            case BUFFER_UNDERFLOW:
                                throw new TlsChannelException("BUFFER_UNDERFLOW during handshake");
                            case CLOSED:
                                throw new TlsChannelException("The channel was closed during handshake");

                        }
                        break;

                    case NEED_TASK:

                        Runnable task;
                        while ((task = engine.getDelegatedTask()) != null) {
                            new Thread(task).start();
                            if (timedOut(startTime)) {
                                throw new TimeoutException("Timeout while handshaking");
                            }
                        }
                        handShakeStatus = engine.getHandshakeStatus();
                        break;
                }
                if (timedOut(startTime)) {
                    throw new TimeoutException("Timeout while handshaking");
                }
            }

        } catch (IOException | TimeoutException e) {
            forciblyCloseConnection();
            throw new TlsChannelException(e);
        }
    }

    /**
     * Used to create a TlsResponseHandler and get it ready to perform the read operation.
     * @throws TlsChannelException If there is an issue in creating the TlsResponseHandler or
     * registering for the read operation.
     */
    protected void initializeResponseHandler() throws TlsChannelException {
        try {
            this.tlsResponseHandler = new TlsResponseHandler(this);
            this.tlsResponseHandler.registerForRead();
            this.tlsResponseHandler.start();

        } catch (final IOException e) {
            logger.error("exception while creating TLS response handler");
            throw new TlsChannelException(e, ExceptionReason.TRANSPORT_LEVEL_FAILURE);
        }
    }

    /**
     * Used to stop the TlsResponseHandler before disconnecting from the node.
     */
    protected void stopResponseHandler() {
        this.getResponseHandler().stopTlsResponseHandler();
    }

    /**
     * Used to get the Bytes from the String provided and put them on the
     * outputApplication buffer, ready to be encrytped and sent to the node.
     * @param data String containing the data that needs to be sent.
     * @throws TlsChannelException If the Engine or Channel are null or
     * If there is an issue with the writing to the node.
     */
    protected void writeData(final String data) throws TlsChannelException {
        final byte[] dataInBytes = data.getBytes();
        if (getSSLEngine() == null || getSocketChannel() == null) {
            logger.error("Write was attempted when not connected to a node");
            throw new TlsChannelException("Write was attempted when not connected to a node");
        } else {
            getOutputApplicationData().clear();
            if (dataInBytes.length > getOutputApplicationData().capacity()) {
                setOutputApplicationData(ByteBuffer.allocate(dataInBytes.length));
            }
            getOutputApplicationData().put(dataInBytes);
            try {
                writeDataToNode(getOutputApplicationData());
            } catch (final IOException | TimeoutException e) {
                logger.error("Error while writing to TLS channel");
                throw new TlsChannelException(e, ExceptionReason.TLS_WRITE_FAILURE);
            }
        }

    }

    /**
     * Used to encrypt (SSLEngine.wrap()) and put the bytes from the buffer provided to
     * the outNetworkBuffer. It then writes the data from the outNetworkBuffer to the SocketChannel.
     * @param applicationDataToWrite The buffer holding the bytes to be encrypted and sent to the node.
     * @return An SSLEngine result holding the status from the operation SSLEngine.wrap().
     * @throws IOException If there is an issue writing the data to the SocketChannel.
     */
    protected SSLEngineResult writeDataToNode(final ByteBuffer applicationDataToWrite) throws IOException, TimeoutException {
        final long startTime = System.currentTimeMillis();
        SSLEngineResult result;
        do {
            getOutputNetworkData().clear();
            applicationDataToWrite.flip();

            result = engine.wrap(applicationDataToWrite, getOutputNetworkData());

            applicationDataToWrite.compact();
            getOutputNetworkData().flip();

            logger.debug("TLS engine wrap - Status: " + result.getStatus().toString());
            logger.debug("TLS engine wrap - Bytes consumed: " + result.bytesConsumed() + " Bytes produced: " + result.bytesProduced());
            logger.debug("TLS engine wrap - Bytes remaining: " + applicationDataToWrite.position());

            // Check status 
            switch (result.getStatus()) {
                case OK:
                case CLOSED:
                    // Send SSL/TLS encoded data to node
                    while (getOutputNetworkData().hasRemaining()) {
                        if (getSocketChannel().write(getOutputNetworkData()) == 0) {
                            throw new IOException("Cannot write any bytes to SocketChannel");
                        }
                        if (timedOut(startTime)) {
                            throw new TimeoutException("Timeout while handshaking");
                        }
                    }

                    break;

                case BUFFER_OVERFLOW:
                    applicationDataToWrite.clear();
                    throw new IOException("Buffer Overflow during write");
                case BUFFER_UNDERFLOW:
                    applicationDataToWrite.clear();
                    throw new IOException("Buffer Underflow during write");
            }
        } while (applicationDataToWrite.position() > 0);

        return result;
    }

    /**
     * Used to read data from the SocketChannel, decrypts and converts it to a String
     * to be placed on the StringBuffer in {@link TlsSessionBuffers}.
     * @return SSLEngineResult holding the status after the operation SSLEngine.unwrap()
     * @throws IOException If there is an issue when reading from the SocketChannel
     */
    protected SSLEngineResult readFromSocketChannel() throws IOException, TimeoutException {
        final long startTime = System.currentTimeMillis();
        SSLEngineResult result;
        channel.read(getPeerNetworkData());
        do {
            getPeerApplicationData().clear();
            getPeerNetworkData().flip();

            result = engine.unwrap(getPeerNetworkData(), getPeerApplicationData());

            getPeerNetworkData().compact();

            switch (result.getStatus()) {
                case BUFFER_OVERFLOW:
                    throw new IOException("Buffer Overflow during read");
                case OK:
                    if (result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK) {
                        return result;
                    } else {
                        peerApplicationData.flip();
                        tlsSessionBuffers.getInput().append(byteBufferToString(getPeerApplicationData()));
                    }
                    break;

                case BUFFER_UNDERFLOW:
                    channel.read(getPeerNetworkData());
            }

            if (timedOut(startTime)) {
                throw new TimeoutException("Timeout while handshaking");
            }
        } while (getPeerNetworkData().position() > 0);
        return result;
    }

    /**
     * Used to close the connection to the node. It sends a close message and
     * waits for a close message in response.
     * @throws TlsChannelException If there is an issue when trying to close the connection to the node.
     * If there is a timeout while waiting for the close message from the node.
     */
    protected void shutDownTlsConnection() throws TlsChannelException {

        engine.closeOutbound();
        try {
            // clear buffer
            getOutputApplicationData().clear();
            // sending close message to node.
            writeDataToNode(getOutputApplicationData());

            final long startTime = System.currentTimeMillis();

            // receiving close-notify message from node.
            while (readFromSocketChannel().getStatus() != SSLEngineResult.Status.CLOSED) {
                if (timedOut(startTime)) {
                    logger.error("Timeout while waiting to receive close-notify message from node");
                    throw new TlsChannelException("Timeout while waiting to receive close-notify message from node");
                }
            }

            logger.info("Received close notify from the node");

            engine.closeInbound();
            channel.close();

        } catch (IOException | TimeoutException e) {
            forciblyCloseConnection();
            logger.error("Exception while shutting down TLS connection" + e.getMessage());
            throw new TlsChannelException(e);
        }

        engine = null;
        channel = null;
    }

    /**
     * Called if there is any issue with the connection to the node.
     * It closes the SocketChannel sets the SSLEngine and SocketChannel to null, stopping anymore
     * operation from being performed.
     */
    protected void forciblyCloseConnection() {
        logger.info("Forcibly closing the connection");
        try {
            if (!engine.isOutboundDone()) {
                engine.closeOutbound();
            }
            if (!engine.isInboundDone()) {
                engine.closeInbound();
            }
            channel.close();
            engine = null;
            channel = null;
        } catch (final IOException e) {
            logger.error("Exception while forcibly closing the connection due to " + e.getMessage());
        }
    }

    /**
     * Used to compare time elapsed between time provided and the current system time with the timeout
     * @param startTime The time at which the operation was started.
     * @return a boolean for whether or not the time elapsed between time provided and the current system time is greater than the timeout.
     */
    protected boolean timedOut(final long startTime) {
        return (System.currentTimeMillis() - startTime > timeOut);
    }

    private String byteBufferToString(final ByteBuffer buffer) throws IOException {
        String data = "";
        final int oldPosition = buffer.position();

        try {
            data = charsetDecoder.decode(buffer).toString();
        } catch (final CharacterCodingException e) {
            throw new IOException(e.getMessage());
        } finally {
            // reset buffer's position to its original so it is not altered:
            buffer.position(oldPosition);
        }
        return data;
    }

    protected ByteBuffer getOutputApplicationData() {
        return outputApplicationData;
    }

    protected ByteBuffer getOutputNetworkData() {
        return outputNetworkData;
    }

    protected ByteBuffer getPeerApplicationData() {
        return peerApplicationData;
    }

    protected ByteBuffer getPeerNetworkData() {
        return peerNetworkData;
    }

    protected SSLSession getSession() {
        return engine.getSession();
    }

    protected TlsResponseHandler getResponseHandler() {
        return this.tlsResponseHandler;
    }

    protected SocketChannel getSocketChannel() {
        return this.channel;
    }

    protected void setTimeOut(final long timeOut) {
        this.timeOut = timeOut;
    }

    protected SSLEngine getSSLEngine() {
        return this.engine;
    }

    protected TlsSessionBuffers getTlsSessionBuffers() {
        return tlsSessionBuffers;
    }

    protected void setCharsetDecoder() {
        this.charsetDecoder = Charset.forName("UTF-8").newDecoder();
    }

    private String getReqInfoIPAddress() {
        return this.reqInfo.getIpAddress();
    }

    private int getReqInfoPort() {
        return this.reqInfo.getPort();
    }

    /*
     * Used for Unit testing only
     */

    protected void setChannel(final SocketChannel sChannel) {
        this.channel = sChannel;
    }

    protected void setEngine(final SSLEngine testEngine) {
        engine = testEngine;
    }

    protected void setOutputApplicationData(final ByteBuffer bufferToSet) {
        outputApplicationData = bufferToSet;
    }

    protected void setPeerApplicationData(final ByteBuffer bufferToSet) {
        peerApplicationData = bufferToSet;
    }

    protected void setPeerNetworkDataBuffer(ByteBuffer bufferToSet) {
        peerNetworkData = bufferToSet;
    }

    protected void setTlsSessionBuffers(TlsSessionBuffers sessionBuffers) {
        this.tlsSessionBuffers = sessionBuffers;
    }

    protected void setTlsResponseHandler(TlsResponseHandler responseHandler) {
        this.tlsResponseHandler = responseHandler;
    }

}

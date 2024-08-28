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
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLEngineResult.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to start a thread which will constantly loop while
 * there is a connection to the peer. This loop will check if there is data
 * to be read from the SocketChannel and if so will perform the read operation
 * storing the data received in a buffer.
 *
 * @author Team Sovereign
 * @version 1.0.42
 * @since 2015-01-15
 */
public class TlsResponseHandler extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(TlsResponseHandler.class);
    private static Boolean keepAlive = true;

    private Selector selector;
    private TlsChannel tlsChannel;

    /**
     * Constructor creating the TlsResponseHandler. Also creates the Selector used.
     *
     * @param tlsChannel The {@link TlsChannel} object which is connected to a peer.
     * @throws IOException If there is and issue with the creation of the Selector.
     */
    public TlsResponseHandler(final TlsChannel tlsChannel) throws IOException {
        this.tlsChannel = tlsChannel;
        selector = Selector.open();
    }

    /**
     * This run method will loop until the thread is told to stop during a disconnect or
     * if there was an exception. The loop will check the selector to see if the SocketChannel
     * is ready for a read operation and if so will perform that read operation.
     */
    @Override
    public void run() {
        logger.debug("Starting the Read thread");
        try {
            Status result;
            while (keepAlive) {

                if (selector.select(10) > 0) {
                    final Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
                    while (selectedKeys.hasNext()) {
                        final SelectionKey selectedKey = selectedKeys.next();
                        if (selectedKey.isReadable()) {
                            getTlsChannel().readFromSocketChannel();
                        }
                        selectedKeys.remove();
                    }
                    logger.debug("A read has been completed");
                    getTlsChannel().getTlsSessionBuffers().setReadComplete(true);
                }
            }
            logger.debug("read thread is stopping");

        } catch (final Exception e) {
            logger.error("Exception in Read Thread: " + e.getMessage());
        }
    }

    /**
     * Stops the TlsResponseHandler thread. Called before disconnecting from the peer.
     */
    public void stopTlsResponseHandler() {
        logger.debug("Stopping TlsResponseHandler thread...");
        keepAlive = false;
    }

    /**
     * This method registers the Selector with the SocketChannel for OP_READ operations.
     *
     * @throws ClosedChannelException If the SocketChannel used is currently closed.
     */
    protected void registerForRead() throws ClosedChannelException {
        this.tlsChannel.getSocketChannel().register(selector, SelectionKey.OP_READ);
    }

    private TlsChannel getTlsChannel() {
        return tlsChannel;
    }

    /*
     * Used for Unit testing only
     */

    protected void setSelector(Selector selector) {
        this.selector = selector;
    }

}

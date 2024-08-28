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
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;

public class MockChannel extends SocketChannel {

    public boolean writeSuccessful = false;

    /**
     * @param provider
     */
    protected MockChannel(final SelectorProvider provider) {
        super(provider);
    }

    protected MockChannel() {
        super(null);
    }
    
    @Override
    public SocketAddress getLocalAddress() throws IOException {
        return null;
    }

    @Override
    public <T> T getOption(final SocketOption<T> arg0) throws IOException {
        return null;
    }

    @Override
    public Set<SocketOption<?>> supportedOptions() {
        return null;
    }

    @Override
    public SocketChannel bind(final SocketAddress local) throws IOException {
        return null;
    }

    @Override
    public boolean connect(final SocketAddress remote) throws IOException {
        return false;
    }

    @Override
    public boolean finishConnect() throws IOException {
        return false;
    }

    @Override
    public SocketAddress getRemoteAddress() throws IOException {
        return null;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public boolean isConnectionPending() {
        return false;
    }

    @Override
    public int read(final ByteBuffer dst) throws IOException {
        return 0;
    }

    @Override
    public long read(final ByteBuffer[] dsts, final int offset, final int length) throws IOException {
        return 0;
    }

    @Override
    public <T> SocketChannel setOption(final SocketOption<T> name, final T value) {
        return null;
    }

    @Override
    public SocketChannel shutdownInput() throws IOException {
        return null;
    }

    @Override
    public SocketChannel shutdownOutput() throws IOException {
        return null;
    }

    @Override
    public Socket socket() {
        return null;
    }

    @Override
    public int write(final ByteBuffer src) throws IOException {
        this.writeSuccessful = true;
        src.position(src.limit());
        return src.position();
    }

    @Override
    public long write(final ByteBuffer[] srcs, final int offset, final int length) throws IOException {
        this.writeSuccessful = true;
        return 0;
    }

    @Override
    protected void implCloseSelectableChannel() throws IOException {

    }

    @Override
    protected void implConfigureBlocking(final boolean arg0) throws IOException {

    }

    boolean getWriteStatus() {
        return this.writeSuccessful;
    }

    protected void register(Selector selector, SelectionKey selectionKey){
        System.out.print(">>>>>>>register");
    }
}

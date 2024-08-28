/*
 * ------------------------------------------------------------------------------
 *  *******************************************************************************
 *  * COPYRIGHT Ericsson 2014
 *  *
 *  * The copyright to the computer program(s) herein is the property of
 *  * Ericsson Inc. The programs may be used and/or copied only with written
 *  * permission from Ericsson Inc. or in accordance with the terms and
 *  * conditions stipulated in the agreement/contract under which the
 *  * program(s) have been supplied.
 *  *******************************************************************************
 *  *----------------------------------------------------------------------------
 */

package com.ericsson.oss.mediation.transport.tls;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.SSLEngineResult;

import com.ericsson.oss.mediation.adapter.tls.api.TlsResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.mediation.adapter.tls.api.TlsConnectionRequest;
import com.ericsson.oss.mediation.adapter.tls.api.TlsRequest;
import com.ericsson.oss.mediation.adapter.tls.exception.TlsChannelException;
import com.ericsson.oss.mediation.adapter.tls.exception.TlsException;
import com.ericsson.oss.mediation.transport.tls.utils.TlsSessionBuffers;

@RunWith(MockitoJUnitRunner.class)
public class TlsConnectionImplTest {
    TlsConnectionRequest reqInfo;
    TlsSessionBuffers tlsSessionBuffers;
    private TlsChannel tlsChannel;

    @Mock
    SSLEngineResult sslEngineResult;
    private TlsConnectionImpl tlsConnectionImpl;

    @Before
    public void setUp() throws TlsException, TlsChannelException, IOException {
        tlsChannel = Mockito.spy(new TlsChannel(reqInfo, tlsSessionBuffers));
        
        tlsConnectionImpl = Mockito.spy(new TlsConnectionImpl(reqInfo));
        tlsConnectionImpl.setTlsChannel(tlsChannel);
    }

    @After
    public void tearDown(){
        if(tlsConnectionImpl.getTlsChannel().getResponseHandler() != null){
            tlsConnectionImpl.getTlsChannel().getResponseHandler().stopTlsResponseHandler();
        }
    }

    @Test
    public void connectExceptionInvalidStateTest() {
        try {
            tlsConnectionImpl.setState(TlsTransportFSM.READY);
            tlsConnectionImpl.connect();
            fail("TlsException exception was expected");
        } catch (final Exception e) {
            assertTrue(e instanceof TlsException);
        }

        try {
            tlsConnectionImpl.setState(TlsTransportFSM.ERROR);
            tlsConnectionImpl.connect();
            fail("TlsException exception was expected");
        } catch (final Exception e) {
            assertTrue(e instanceof TlsException);
        }
        try {
            tlsConnectionImpl.setState(TlsTransportFSM.BLOCKING_READ);
            tlsConnectionImpl.connect();
            fail("TlsException exception was expected");
        } catch (final Exception e) {
            assertTrue(e instanceof TlsException);
        }
        try {
            tlsConnectionImpl.setState(TlsTransportFSM.WRITING);
            tlsConnectionImpl.connect();
            fail("TlsException exception was expected");
        } catch (final Exception e) {
            assertTrue(e instanceof TlsException);
        }
    }

    @Test
    public void disconnectExceptionInvalidStateTest() {
        try {
            tlsConnectionImpl.setState(TlsTransportFSM.IDLE);
            tlsConnectionImpl.disconnect();
            fail("TlsException exception was expected");
        } catch (final Exception e) {
            assertTrue(e instanceof TlsException);
        }

        try {
            tlsConnectionImpl.setState(TlsTransportFSM.ERROR);
            tlsConnectionImpl.disconnect();
            fail("TlsException exception was expected");
        } catch (final Exception e) {
            assertTrue(e instanceof TlsException);
        }
        try {
            tlsConnectionImpl.setState(TlsTransportFSM.BLOCKING_READ);
            tlsConnectionImpl.disconnect();
            fail("TlsException exception was expected");
        } catch (final Exception e) {
            assertTrue(e instanceof TlsException);
        }
        try {
            tlsConnectionImpl.setState(TlsTransportFSM.WRITING);
            tlsConnectionImpl.disconnect();
            fail("TlsException exception was expected");
        } catch (final Exception e) {
            assertTrue(e instanceof TlsException);
        }
    }

    @Test
    public void sendNonBlockingExceptionInvalidStateTest() {
        try {
            tlsConnectionImpl.setState(TlsTransportFSM.IDLE);
            tlsConnectionImpl.sendNonBlocking(new TlsRequest("dummy request"));
            fail("TlsException exception was expected");
        } catch (final Exception e) {
            assertTrue(e instanceof TlsException);
        }

        try {
            tlsConnectionImpl.setState(TlsTransportFSM.ERROR);
            tlsConnectionImpl.sendNonBlocking(new TlsRequest("dummy request"));
            fail("TlsException exception was expected");
        } catch (final Exception e) {
            assertTrue(e instanceof TlsException);
        }
        try {
            tlsConnectionImpl.setState(TlsTransportFSM.BLOCKING_READ);
            tlsConnectionImpl.sendNonBlocking(new TlsRequest("dummy request"));
            fail("TlsException exception was expected");
        } catch (final Exception e) {
            assertTrue(e instanceof TlsException);
        }
        try {
            tlsConnectionImpl.setState(TlsTransportFSM.WRITING);
            tlsConnectionImpl.sendNonBlocking(new TlsRequest("dummy request"));
            fail("TlsException exception was expected");
        } catch (final Exception e) {
            assertTrue(e instanceof TlsException);
        }
    }

    @Test
    public void tlsChannelInitializeFailureTest() {
    	tlsConnectionImpl.setState(TlsTransportFSM.IDLE);
    	try {
    		Mockito.doThrow(TlsChannelException.class).when(tlsChannel).initialise();
    		Mockito.doNothing().when(tlsChannel).handshake();
    		tlsConnectionImpl.connect();
    	} catch (final Exception e) {
    		assertTrue(e instanceof TlsException);
    	}
    	assertEquals("ERROR State was expected after initialization failure", tlsConnectionImpl.getState().name(), "ERROR");
    }
    
    @Test
    public void connectTestSuccess() throws TlsException, IOException, TlsChannelException {
        tlsConnectionImpl.setState(TlsTransportFSM.IDLE);
        Mockito.doNothing().when(tlsChannel).initialise();
        Mockito.doNothing().when(tlsChannel).handshake();
        Mockito.doNothing().when(tlsChannel).initializeResponseHandler();
        tlsConnectionImpl.connect();
        tlsConnectionImpl.getState();
        assertEquals("READY State was expected after successfully connecting", tlsConnectionImpl.getState().name(), "READY");
    }


    @Test
    public void tlsChannelHandshakeFailureTest() {
        tlsConnectionImpl.setState(TlsTransportFSM.IDLE);
        try {
            Mockito.doNothing().when(tlsChannel).initialise();
            Mockito.doThrow(TlsChannelException.class).when(tlsChannel).handshake();
            tlsConnectionImpl.connect();
        } catch (final Exception e) {
            assertTrue(e instanceof TlsException);
        }
        assertEquals("ERROR State was expected after handshake failure", tlsConnectionImpl.getState().name(), "ERROR");
    }

    @Test
    public void disconnectTestSuccess() throws TlsException, IOException, TlsChannelException {
        Mockito.doNothing().when(tlsChannel).stopResponseHandler();
        Mockito.doNothing().when(tlsChannel).shutDownTlsConnection();
        tlsConnectionImpl.setState(TlsTransportFSM.READY);
        tlsConnectionImpl.disconnect();
        tlsConnectionImpl.getState();
        assertEquals("IDLE State was expected after disconnect", tlsConnectionImpl.getState().name(), "IDLE");
    }

    @Test
    public void sendNonBlockingTestState() throws TlsException, IOException, TlsChannelException {
        final TlsRequest tlsRequest = new TlsRequest("String");
        Mockito.doNothing().when(tlsConnectionImpl).doNonBlockingWrite(tlsRequest);
        tlsConnectionImpl.setState(TlsTransportFSM.READY);
        tlsConnectionImpl.sendNonBlocking(tlsRequest);
        tlsConnectionImpl.getState();
        assertEquals("WRITING State was expected after sendNonBlocking call", tlsConnectionImpl.getState().name(), "WRITING");
    }

    @Test
    public void sendNonBlockingSuccessTest() throws TlsException, IOException, TlsChannelException {
        setupMocksForResponseHandler();

        Mockito.doNothing().when(tlsChannel).writeData("Test");
        tlsConnectionImpl.setState(TlsTransportFSM.READY);
        tlsConnectionImpl.getTlsChannel().getResponseHandler().start();
        tlsConnectionImpl.sendNonBlocking(new TlsRequest("Test"));
        tlsConnectionImpl.getState();
        assertEquals("READY State was expected after completing doNonBlockingWrite", tlsConnectionImpl.getState().name(), "READY");
    }

    @Test
    public void sendNonBlockingExceptionTest() throws TlsException, IOException, TlsChannelException {
        setupMocksForResponseHandler();

        try {
            tlsConnectionImpl.setState(TlsTransportFSM.READY);
            tlsConnectionImpl.getTlsChannel().getResponseHandler().start();
            tlsConnectionImpl.sendNonBlocking(new TlsRequest("String"));
        } catch (final Exception e) {
            assertTrue(e instanceof TlsException);
        }
        tlsConnectionImpl.getState();
        assertEquals("ERROR State was expected after write failure", tlsConnectionImpl.getState().name(), "ERROR");
    }

    @Test
    public void disconnectExceptionTest() {
        tlsConnectionImpl.setState(TlsTransportFSM.READY);
       try {
           Mockito.doNothing().when(tlsChannel).stopResponseHandler();
           Mockito.doThrow(TlsChannelException.class).when(tlsChannel).shutDownTlsConnection();
           tlsConnectionImpl.disconnect();
        } catch (TlsChannelException | TlsException e) {
            assertTrue(e instanceof TlsException);
        }
        assertEquals("ERROR State was expected after close failure", tlsConnectionImpl.getState().name(), "ERROR");
    }

    @Test
    public void readTest() throws TlsException, IOException {
        TlsSessionBuffers sessionBuffers = Mockito.spy(new TlsSessionBuffers());
        Mockito.doReturn(true).when(sessionBuffers).isReadComplete();

        TlsResponseHandler mockResponseHandler = Mockito.spy(new TlsResponseHandler(tlsChannel));
        tlsConnectionImpl.getTlsChannel().setTlsResponseHandler(mockResponseHandler);
        tlsConnectionImpl.getTlsChannel().getResponseHandler().start();

        sessionBuffers.getInput().append("Test");
        tlsConnectionImpl.setTlsSessionBuffers(sessionBuffers);

        TlsResponse response = tlsConnectionImpl.read();
        tlsConnectionImpl.getTlsChannel().getResponseHandler().stopTlsResponseHandler();
        assertEquals(response.getResponseMessageString(), "Test");
    }

    @Test(expected = TlsException.class)
    public void timeoutDuringReadTest() throws TlsException, IOException {
        TlsSessionBuffers sessionBuffers = Mockito.spy(new TlsSessionBuffers());
        Mockito.doReturn(false).when(sessionBuffers).isReadComplete();

        TlsResponseHandler mockResponseHandler = Mockito.spy(new TlsResponseHandler(tlsChannel));
        tlsConnectionImpl.getTlsChannel().setTlsResponseHandler(mockResponseHandler);
        tlsConnectionImpl.getTlsChannel().getResponseHandler().start();

        sessionBuffers.getInput().append("Test");
        tlsConnectionImpl.setTlsSessionBuffers(sessionBuffers);

        tlsConnectionImpl.read();
    }

    @Test(expected = TlsException.class)
    public void readThreadNotRunningTest() throws TlsChannelException, IOException, TlsException, InterruptedException, TimeoutException {
        TlsSessionBuffers sessionBuffers = Mockito.spy(new TlsSessionBuffers());
        Mockito.doReturn(false).when(sessionBuffers).isReadComplete();
        sessionBuffers.getInput().append("Test");
        tlsConnectionImpl.setTlsSessionBuffers(sessionBuffers);

        Selector selector = Mockito.spy(getNewSelector());
        Mockito.doReturn(1).when(selector).select(10);
        Mockito.doReturn(getMockSelectionKeys()).when(selector).selectedKeys();

        TlsResponseHandler mockResponseHandler = Mockito.spy(new TlsResponseHandler(tlsChannel));
        mockResponseHandler.setSelector(selector);

        tlsConnectionImpl.getTlsChannel().setTlsResponseHandler(mockResponseHandler);
        Mockito.doThrow(TlsChannelException.class).when(tlsChannel).readFromSocketChannel();

        tlsConnectionImpl.getTlsChannel().getResponseHandler().start();
        Thread.sleep(10000);
        tlsConnectionImpl.read();
    }

    @Test
    public void sendNonBlockingThreadNotAliveTest() throws TlsException, IOException, TlsChannelException {
        setupMocksForResponseHandler();
        Mockito.doNothing().when(tlsChannel).writeData("Test");
        Mockito.doNothing().when(tlsChannel).initializeResponseHandler();

        tlsConnectionImpl.setState(TlsTransportFSM.READY);
        tlsConnectionImpl.sendNonBlocking(new TlsRequest("Test"));
        tlsConnectionImpl.getState();
        Mockito.verify(tlsChannel).initializeResponseHandler();
    }

    private void setupMocksForResponseHandler() throws IOException {
        Selector selector = Mockito.spy(getNewSelector());
        Mockito.doReturn(0).when(selector).select(10);
        TlsResponseHandler mockResponseHandler = Mockito.spy(new TlsResponseHandler(tlsChannel));
        mockResponseHandler.setSelector(selector);
        tlsConnectionImpl.getTlsChannel().setTlsResponseHandler(mockResponseHandler);
    }

    private SelectionKey getNewSelectionKey(){
        SelectionKey selectionKey = Mockito.spy(new SelectionKey() {
            @Override
            public SelectableChannel channel() {
                return null;
            }

            @Override
            public Selector selector() {
                return null;
            }

            @Override
            public boolean isValid() {
                return false;
            }

            @Override
            public void cancel() {

            }

            @Override
            public int interestOps() {
                return 0;
            }

            @Override
            public SelectionKey interestOps(int ops) {
                return null;
            }

            @Override
            public int readyOps() {
                return 0;
            }
        });

        Mockito.doReturn(SelectionKey.OP_READ).when(selectionKey).isReadable();
        return selectionKey;
    }

    private Selector getNewSelector(){
        return new Selector() {
            @Override
            public boolean isOpen() {
                return false;
            }

            @Override
            public SelectorProvider provider() {
                return null;
            }

            @Override
            public Set<SelectionKey> keys() {
                return null;
            }

            @Override
            public Set<SelectionKey> selectedKeys() {
                return null;
            }

            @Override
            public int selectNow() throws IOException {
                return 0;
            }

            @Override
            public int select(long timeout) throws IOException {
                return 0;
            }

            @Override
            public int select() throws IOException {
                return 0;
            }

            @Override
            public Selector wakeup() {
                return null;
            }

            @Override
            public void close() throws IOException {

            }
        };
    }

    private Set<SelectionKey> getMockSelectionKeys(){
        Set<SelectionKey> selectionKeys = new HashSet<SelectionKey>();
        selectionKeys.add(getNewSelectionKey());
        return selectionKeys;
    }
}
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

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.mediation.adapter.tls.api.TlsConnectionRequest;
import com.ericsson.oss.mediation.adapter.tls.exception.TlsChannelException;
import com.ericsson.oss.mediation.adapter.tls.exception.TlsSecurityException;
import com.ericsson.oss.mediation.transport.tls.utils.TlsSessionBuffers;
import org.mockito.stubbing.Answer;

@RunWith(MockitoJUnitRunner.class)
public class TlsChannelTest {
    private TlsChannel tlsChannel;
    private MockChannel channel;
    private SSLEngine mockEngine;

    @Mock
    SSLEngineResult sslEngineResult;

    @Before
    public void setUp() throws TlsSecurityException, TlsChannelException {
        TlsConnectionRequest reqInfo = new TlsConnectionRequest("1.1.1.1", 1234);
        TlsSessionBuffers tlsSessionBuffers = new TlsSessionBuffers();
        tlsChannel = Mockito.spy(new TlsChannel(reqInfo, tlsSessionBuffers));
        tlsChannel.setOutputApplicationData(ByteBuffer.allocate(0));
    }

    @Test
    public void TimeOutDuringSetUpChannelTest() {
        try {
            tlsChannel.setTimeOut(100);
            tlsChannel.initialise();
            fail("Timout exception was expected");
        } catch (final Exception e) {
            assertTrue(e instanceof TlsChannelException);
            assertTrue(e.getMessage().contains("TIMEOUT"));
        }
    }

    @Test(expected = TlsChannelException.class)
    public void ExceptionDuringSetupEngineTest() throws TlsSecurityException, TlsChannelException {
        Mockito.doThrow(TlsSecurityException.class).when(tlsChannel).setupEngine();
        tlsChannel.setTimeOut(100);
        tlsChannel.initialise();
    }

    @Test(expected = TlsChannelException.class)
    public void ExceptionDuringSetupChannelTest() throws TlsSecurityException, TlsChannelException, TimeoutException, IOException {
        Mockito.doNothing().when(tlsChannel).setupEngine();
        Mockito.doThrow(IOException.class).when(tlsChannel).setupChannel();
        tlsChannel.setTimeOut(100);
        tlsChannel.initialise();
    }

    @Test
    public void bufferInitializationTest() throws TlsChannelException, IOException, TlsSecurityException, TimeoutException {
        Mockito.doNothing().when(tlsChannel).setupChannel();
        tlsChannel.initialise();
        assertNotNull(tlsChannel.getPeerApplicationData());
        assertNotNull(tlsChannel.getPeerNetworkData());
        assertNotNull(tlsChannel.getOutputApplicationData());
        assertNotNull(tlsChannel.getOutputNetworkData());
    }

    @Test
    public void shutdownTest() throws IOException, TlsChannelException, TimeoutException {
        channel = Mockito.spy(new MockChannel());
        mockEngine = Mockito.spy(new MockSSLEngine());

        tlsChannel.setChannel(channel);
        tlsChannel.setEngine(mockEngine);
        tlsChannel.setTimeOut(100);

        Mockito.doNothing().when(mockEngine).closeInbound();
        Mockito.doNothing().when(mockEngine).closeOutbound();
        Mockito.doNothing().when(channel).close();

        final SSLEngineResult mockSSLEngineResultForWrite = new SSLEngineResult(SSLEngineResult.Status.OK,
                SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING, 0, 0);

        final SSLEngineResult mockSSLEngineResultForRead = new SSLEngineResult(SSLEngineResult.Status.CLOSED,
                SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING, 0, 0);

        ByteBuffer buffer = ByteBuffer.allocate(mockEngine.getSession().getApplicationBufferSize());
        Mockito.doReturn(buffer).when(tlsChannel).getOutputApplicationData();

        Mockito.doReturn(mockSSLEngineResultForWrite).when(tlsChannel).writeDataToNode(buffer);
        Mockito.doReturn(mockSSLEngineResultForRead).when(tlsChannel).readFromSocketChannel();

        tlsChannel.shutDownTlsConnection();
        assertTrue(tlsChannel.getSSLEngine() == null);
    }

    @Test(expected = TlsChannelException.class)
    public void shutdownShouldForciblyCloseTest() throws TlsChannelException, IOException, TimeoutException {
        channel = Mockito.spy(new MockChannel());
        mockEngine = Mockito.spy(new MockSSLEngine());

        tlsChannel.setChannel(channel);
        tlsChannel.setEngine(mockEngine);
        tlsChannel.setTimeOut(1);

        Mockito.doReturn(false).when(mockEngine).isInboundDone();
        Mockito.doReturn(false).when(mockEngine).isOutboundDone();

        Mockito.doNothing().when(mockEngine).closeInbound();
        Mockito.doNothing().when(mockEngine).closeOutbound();
        Mockito.doNothing().when(channel).close();

        Mockito.doThrow(TimeoutException.class).when(tlsChannel).timedOut(0);

        final SSLEngineResult mockSSLEngineResult = new SSLEngineResult(SSLEngineResult.Status.OK, SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING,
                0, 0);

        ByteBuffer buffer = ByteBuffer.allocate(mockEngine.getSession().getApplicationBufferSize());
        Mockito.doReturn(buffer).when(tlsChannel).getOutputApplicationData();

        Mockito.doReturn(mockSSLEngineResult).when(tlsChannel).writeDataToNode(buffer);
        Mockito.doReturn(mockSSLEngineResult).when(tlsChannel).readFromSocketChannel();

        tlsChannel.shutDownTlsConnection();
    }

    @Test(expected = TlsChannelException.class)
    public void shutdownThrowTlsChannelExceptionTest() throws TlsChannelException, IOException, TimeoutException {
        channel = Mockito.spy(new MockChannel());
        mockEngine = Mockito.spy(new MockSSLEngine());

        tlsChannel.setChannel(channel);
        tlsChannel.setEngine(mockEngine);
        tlsChannel.setTimeOut(1);

        Mockito.doNothing().when(mockEngine).closeOutbound();
        ByteBuffer buffer = ByteBuffer.allocate(mockEngine.getSession().getApplicationBufferSize());
        Mockito.doReturn(buffer).when(tlsChannel).getOutputApplicationData();

        Mockito.doThrow(IOException.class).when(tlsChannel).writeDataToNode(buffer);
        tlsChannel.shutDownTlsConnection();
    }

    @Test(expected = TlsChannelException.class)
    public void writeDataWhenAlreadyClosedThrowExceptionTest() throws TlsChannelException, IOException {
        tlsChannel.setEngine(Mockito.spy(new MockSSLEngine()));
        tlsChannel.setChannel(Mockito.spy(new MockChannel()));
        tlsChannel.forciblyCloseConnection();
        tlsChannel.writeData("");
    }

    @Test(expected = TlsChannelException.class)
    public void timeOutDuringHandshakeTest() throws IOException, TlsChannelException, TlsSecurityException {
        channel = new MockChannel();
        tlsChannel.setTimeOut(100);
        tlsChannel.setChannel(channel);
        tlsChannel.setupEngine();
        tlsChannel.initialiseBuffers();
        tlsChannel.handshake();
    }

    @Test
    public void writeDataSuccessfulTest() throws TimeoutException, IOException, TlsChannelException, TlsSecurityException {
        final MockChannel mockChannel = new MockChannel();
        mockEngine = Mockito.spy(new MockSSLEngine());

        Mockito.doNothing().when(tlsChannel).setupChannel();

        tlsChannel.initialise();
        tlsChannel.setChannel(mockChannel);
        tlsChannel.setEngine(mockEngine);

        tlsChannel.writeData("Test");
        assertTrue("Successful write operation expected", mockChannel.getWriteStatus());
    }

    @Test(expected = TlsChannelException.class)
    public void writeDataBufferOverFlowTest() throws TlsChannelException, IOException {

        final MockSSLEngine mockSSLEngine = new MockSSLEngine() {
            @Override
            public SSLEngineResult wrap(final ByteBuffer src, final ByteBuffer dst) {
                return new SSLEngineResult(SSLEngineResult.Status.BUFFER_OVERFLOW, SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING, 0, 0);
            }
        };

        final MockChannel mockChannel = new MockChannel();
        tlsChannel.setEngine(mockSSLEngine);
        tlsChannel.setChannel(mockChannel);
        tlsChannel.initialiseBuffers();
        tlsChannel.writeData("Test");
    }

    @Test
    public void readFromSocketChannelTest() throws IOException, TlsSecurityException, TimeoutException {
        mockEngine = Mockito.spy(new MockSSLEngine());
        channel = Mockito.spy(new MockChannel());

        tlsChannel.setEngine(mockEngine);
        tlsChannel.setChannel(channel);
        tlsChannel.setCharsetDecoder();
        tlsChannel.setTlsSessionBuffers(new TlsSessionBuffers());

        ByteBuffer networkBuffer = createAndSetNetworkBuffer(mockEngine, tlsChannel);
        createAndSetApplicationBuffer(mockEngine, tlsChannel);

        Mockito.doReturn(8).when(channel).read(networkBuffer);
        String testData = "TestData";
        networkBuffer.put(testData.getBytes());

        tlsChannel.readFromSocketChannel();
        assertEquals("TestData", tlsChannel.getTlsSessionBuffers().getInput().toString());
    }

    @Test(expected = IOException.class)
    public void readFromSocketChannelBufferOverflowTest() throws IOException, TlsSecurityException, TimeoutException {
        channel = Mockito.spy(new MockChannel());

        mockEngine = new MockSSLEngine() {
            @Override
            public SSLEngineResult unwrap(final ByteBuffer src, final ByteBuffer dst) {
                return new SSLEngineResult(SSLEngineResult.Status.BUFFER_OVERFLOW, SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING, 0, 0);
            }
        };

        tlsChannel.setEngine(mockEngine);
        tlsChannel.setChannel(channel);

        ByteBuffer networkBuffer = createAndSetNetworkBuffer(mockEngine, tlsChannel);
        createAndSetApplicationBuffer(mockEngine, tlsChannel);

        Mockito.doReturn(8).when(channel).read(networkBuffer);
        String testData = "TestData";
        networkBuffer.put(testData.getBytes());

        tlsChannel.readFromSocketChannel();
    }

    @Test(expected = TlsChannelException.class)
    public void HandshakeNeedUnwrapClosedTest() throws TlsSecurityException, IOException, TlsChannelException, TimeoutException {
        channel = Mockito.spy(new MockChannel());
        mockEngine = Mockito.spy(new MockSSLEngine());

        SSLEngineResult.HandshakeStatus hsStatus = SSLEngineResult.HandshakeStatus.NEED_UNWRAP;
        Mockito.doReturn(hsStatus).when(mockEngine).getHandshakeStatus();

        SSLEngineResult result = new SSLEngineResult(SSLEngineResult.Status.CLOSED, SSLEngineResult.HandshakeStatus.NEED_UNWRAP, 0, 0);
        Mockito.doReturn(result).when(tlsChannel).readFromSocketChannel();

        tlsChannel.setChannel(channel);
        tlsChannel.setEngine(mockEngine);
        tlsChannel.initialiseBuffers();

        tlsChannel.handshake();
    }

    @Test(expected = TlsChannelException.class)
    public void HandshakeNeedUnwrapOverflowTest() throws TlsSecurityException, IOException, TlsChannelException, TimeoutException {
        channel = Mockito.spy(new MockChannel());
        mockEngine = Mockito.spy(new MockSSLEngine());

        SSLEngineResult.HandshakeStatus hsStatus = SSLEngineResult.HandshakeStatus.NEED_UNWRAP;
        Mockito.doReturn(hsStatus).when(mockEngine).getHandshakeStatus();

        Mockito.doAnswer(new Answer() {
            private int count = 0;
            SSLEngineResult overflowResult = new SSLEngineResult(SSLEngineResult.Status.BUFFER_OVERFLOW, SSLEngineResult.HandshakeStatus.NEED_UNWRAP, 0, 0);
            SSLEngineResult okResult = new SSLEngineResult(SSLEngineResult.Status.OK, SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING, 0, 0);

            @Override
            public SSLEngineResult answer(InvocationOnMock invocation) throws Throwable {
                if (count == 0) {
                    count++;
                    return overflowResult;
                } else {
                    return okResult;
                }
            }
        }).when(tlsChannel).readFromSocketChannel();

        tlsChannel.setChannel(channel);
        tlsChannel.setEngine(mockEngine);
        tlsChannel.initialiseBuffers();
        tlsChannel.getPeerApplicationData().limit(0);

        tlsChannel.handshake();
    }

    @Test
    public void HandshakeNeedWrapClosedOverflowUnderflowTest() throws TlsSecurityException, IOException {
        channel = Mockito.spy(new MockChannel());
        mockEngine = Mockito.spy(new MockSSLEngine());

        ByteBuffer networkBuffer = createAndSetNetworkBuffer(mockEngine, tlsChannel);
        ByteBuffer applicationBuffer = createAndSetApplicationBuffer(mockEngine, tlsChannel);

        SSLEngineResult.HandshakeStatus hsStatus = SSLEngineResult.HandshakeStatus.NEED_WRAP;
        Mockito.doReturn(hsStatus).when(mockEngine).getHandshakeStatus();

        Mockito.doAnswer(new Answer() {
            private int count = 0;
            SSLEngineResult closedResult = new SSLEngineResult(SSLEngineResult.Status.CLOSED, SSLEngineResult.HandshakeStatus.NEED_WRAP, 0, 0);
            SSLEngineResult overflowResult = new SSLEngineResult(SSLEngineResult.Status.BUFFER_OVERFLOW, SSLEngineResult.HandshakeStatus.NEED_WRAP, 0, 0);
            SSLEngineResult underflowResult = new SSLEngineResult(SSLEngineResult.Status.BUFFER_UNDERFLOW, SSLEngineResult.HandshakeStatus.NEED_WRAP, 0, 0);

            @Override
            public SSLEngineResult answer(InvocationOnMock invocation) throws Throwable {
                if (count == 0) {
                    count++;
                    return closedResult;
                } else if(count == 1) {
                    count++;
                    return overflowResult;
                } else {
                    return underflowResult;
                }
            }
        }).when(mockEngine).wrap(networkBuffer, applicationBuffer);

        tlsChannel.setChannel(channel);
        tlsChannel.setEngine(mockEngine);
        tlsChannel.initialiseBuffers();

        try {
            tlsChannel.handshake();
        } catch (TlsChannelException e) {
            assertTrue(e.getMessage().contains("The channel was closed during handshake"));
        }

        try {
            tlsChannel.handshake();
        } catch (TlsChannelException e) {
            assertTrue(e.getMessage().contains("BUFFER_OVERFLOW during handshake"));
        }

        try {
            tlsChannel.handshake();
        } catch (TlsChannelException e) {
            assertTrue(e.getMessage().contains("BUFFER_UNDERFLOW during handshake"));
        }
    }

    private ByteBuffer createAndSetNetworkBuffer(SSLEngine engine, TlsChannel tlsChannel){
        ByteBuffer networkBuffer = ByteBuffer.allocate(engine.getSession().getPacketBufferSize());
        tlsChannel.setPeerNetworkDataBuffer(networkBuffer);
        return networkBuffer;
    }

    private ByteBuffer createAndSetApplicationBuffer(SSLEngine engine, TlsChannel tlsChannel){
        ByteBuffer applicationBuffer = ByteBuffer.allocate(engine.getSession().getApplicationBufferSize());
        tlsChannel.setPeerApplicationData(applicationBuffer);
        return applicationBuffer;
    }
}

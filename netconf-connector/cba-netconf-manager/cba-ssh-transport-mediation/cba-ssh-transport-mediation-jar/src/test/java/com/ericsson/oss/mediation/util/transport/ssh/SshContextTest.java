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
import com.ericsson.oss.mediation.util.transport.api.TransportSessionType;
import com.ericsson.oss.mediation.util.transport.ssh.exception.SshException;
import com.jcraft.jsch.ChannelSubsystem;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replay;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Test is ignored for some time. It is not readable now and behavior of this test is unpredictable. Please, someone,
 * rewrite it. Problems I see here:
 * <ul>
 * <li>unit test usually tests one class. Here this approach is violated</li>
 * <li>some classes are not really mocked. If we are under vpn, connection will be established and some tests will fail</li>
 * </ul>
 */
@RunWith(PowerMockRunner.class)
//@Ignore
public class SshContextTest {

    private final JSch jsch;

    private final Session session;

    private final ChannelSubsystem channelSubsystem;

    private final InputStream inputStream;

    private final OutputStream outputStream;

    private final BufferedReader bufferedReader;

    private final InputStreamReader inputStreamReader;

    private static TransportManagerCI transportManagerCI;

    private static com.ericsson.oss.mediation.util.transport.api.TransportData request;

    public SshContextTest() {

        transportManagerCI = new TransportManagerCI();
        transportManagerCI.setHostname("192.168.100.208");
        transportManagerCI.setPassword("sgsn123");
        transportManagerCI.setUsername("borusgsn");
        transportManagerCI.setPort(22);
        transportManagerCI.setSocketConnectionTimeoutInMillis(1000);
        transportManagerCI.setSessionType(TransportSessionType.SUBSYSTEM);
        transportManagerCI.setSessionTypeValue("netconf");

        jsch = createMock(JSch.class);
        session = createMock(Session.class);
        channelSubsystem = createMock(ChannelSubsystem.class);
        inputStream = createMock(InputStream.class);
        outputStream = createMock(OutputStream.class);
        bufferedReader = createMock(BufferedReader.class);
        inputStreamReader = createMock(InputStreamReader.class);
    }

    @Test
    public void testOpentConnection() throws JSchException, IOException, Exception {
        prepareMockSession();
        replaySession();
        final SshContext sshContext = new SshContext(transportManagerCI);
        sshContext.setsshLibrary(jsch);
        sshContext.connect();
        assertEquals(SshTransportFSM.CONNECTION_ESTABLISHED, sshContext.getState());
        verify(jsch, session);
    }

    @Test(expected = SshException.class)
    public void testOpentConnection_Exception() throws JSchException, IOException, Exception {
        prepareMockSession_Exception();
        replaySession();
        final SshContext sshContext = new SshContext(transportManagerCI);
        sshContext.setsshLibrary(jsch);
        sshContext.connect();
        verify(jsch, session);
    }

    @Test
    public void testOpenSession() throws JSchException, IOException, Exception {
        prepareMockSession();
        prepareMockChannelSubsystem();
        replayAll();
        final SshContext sshContext = new SshContext(transportManagerCI);
        sshContext.setsshLibrary(jsch);
        sshContext.connect();
        sshContext.openChannel();
        assertEquals(SshTransportFSM.READY, sshContext.getState());
    }

    @Test
    public void testOpenSession_Exception() throws JSchException, IOException {
        SshContext sshContex = null;
        try {

            prepareMockSession();
            prepareMockChannelSubsystem_Exception();
            replayAll();
            sshContex = new SshContext(transportManagerCI);
            sshContex.setsshLibrary(jsch);
            sshContex.connect();
            sshContex.openChannel();

        } catch (final SshException ex) {
        } catch (final Exception ex) {
        }

        assertEquals(SshTransportFSM.ERROR, sshContex.getState());
    }

    @Test
    public void testRead() throws JSchException, IOException, Exception {

        SshContext sshContex = null;
        try {

            prepareMockSessionWithIsConnectedCall();
            prepareMockChannelSubsystem();
            prepareRequest();
            replayAll();
            sshContex = new SshContext(transportManagerCI);
            sshContex.setsshLibrary(jsch);
            sshContex.connect();
            sshContex.openChannel();

            expectLastCall();
            sshContex.getResponse(request, false);

            assertEquals(SshTransportFSM.READY, sshContex.getState());
        } catch (final SshException ex) {
        } catch (final Exception ex) {
        }
        request = null;
    }

    @Test(expected = SshException.class)
    public void testRead_NullData() throws JSchException, IOException, Exception {
        request = null;
        prepareMockSession();
        prepareMockChannelSubsystem();
        replayAll();
        final SshContext sshContext = new SshContext(transportManagerCI);
        sshContext.setsshLibrary(jsch);
        sshContext.connect();
        sshContext.openChannel();
        sshContext.getResponse(request, false);
        request = null;
    }

    @Test
    public void testRead_Exception() throws JSchException, IOException {
        try {
            prepareMockSession();
            prepareMockChannelSubsystem_ReadException();
            request = new TransportData();
            replayAll();
            final SshContext sshContext = new SshContext(transportManagerCI);
            sshContext.setsshLibrary(jsch);
            sshContext.connect();
            sshContext.openChannel();
            sshContext.getResponse(request, false);
            assertEquals(SshTransportFSM.ERROR, sshContext.getState());
            request = null;
        } catch (final SshException ex) {
        } catch (final Exception ex) {
        }
    }

    @Test
    public void testWrite() throws JSchException, IOException, Exception {
        try {
            prepareMockSession();
            prepareMockChannelSubsystem();
            prepareRequest();
            replayAll();
            final SshContext sshContext = new SshContext(transportManagerCI);
            sshContext.setsshLibrary(jsch);
            sshContext.connect();
            sshContext.openChannel();
            sshContext.sendRequest(request);
            assertEquals(SshTransportFSM.READY, sshContext.getState());
            request = null;
        } catch (final SshException ex) {
        } catch (final Exception ex) {
        }
    }

    @Test(expected = SshException.class)
    public void testWrite_NullData() throws JSchException, IOException, Exception {

        prepareMockSession();
        prepareMockChannelSubsystem();
        replayAll();
        final SshContext sshContext = new SshContext(transportManagerCI);
        sshContext.setsshLibrary(jsch);
        sshContext.connect();
        sshContext.openChannel();

        request = null;
        sshContext.sendRequest(request);

    }

    @Test
    public void testWrite_Exception() throws JSchException, IOException, Exception {
        try {
            prepareMockSession();
            prepareMockChannelSubsystem_WriteException();
            request = new TransportData();
            request.setData(new StringBuilder(""));
            replayAll();
            final SshContext sshContext = new SshContext(transportManagerCI);
            sshContext.setsshLibrary(jsch);
            sshContext.connect();
            sshContext.openChannel();
            sshContext.sendRequest(request);
            assertEquals(SshTransportFSM.ERROR, sshContext.getState());
            request = null;
        } catch (final SshException ex) {
        } catch (final Exception ex) {
        }
    }

    @Test
    public void testCloseConnection() throws Exception {
        prepareMockSession();
        prepareMockChannelSubsystem();
        prepareMockforCloseSession();
        replayAll();

        final SshContext sshContext = new SshContext(transportManagerCI);
        sshContext.setsshLibrary(jsch);
        sshContext.connect();
        sshContext.openChannel();
        sshContext.closeConnection();

        assertEquals(SshTransportFSM.IDLE, sshContext.getState());
    }

    private void prepareRequest() throws Exception {
        request = new TransportData();
        request.setData(new StringBuilder("Hello"));
    }

    private void prepareMockSession() throws Exception, JSchException, IOException {
        prepareSession();
        session.connect();
        expectLastCall().times(1);
    }

    private void prepareMockSessionWithIsConnectedCall() throws Exception, JSchException, IOException {
        prepareSessionSessionIsConnectedCall();
        session.connect();
        expectLastCall().times(1);
    }

    private void prepareMockSession_Exception() throws Exception, JSchException, IOException {
        prepareSession();
        session.connect();
        expectLastCall().andThrow(new JSchException()).times(1);
    }

    private void prepareSession() throws Exception, JSchException {
        PowerMock.expectNew(JSch.class).andReturn(jsch);

        expect(jsch.getSession("borusgsn", "192.168.100.208", 22)).andReturn(session);

        final java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");

        session.setConfig(config);
        expectLastCall();
        session.setTimeout(1000);
        expectLastCall();
        session.setPassword("sgsn123");
        expectLastCall();
    }

    private void prepareSessionSessionIsConnectedCall() throws Exception, JSchException {
        PowerMock.expectNew(JSch.class).andReturn(jsch);

        expect(jsch.getSession("borusgsn", "192.168.100.208", 22)).andReturn(session);

        final java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");

        session.setConfig(config);
        expectLastCall();
        session.setTimeout(1000);
        expectLastCall();
        session.setPassword("sgsn123");
        expectLastCall();
        expect(session.isConnected()).andReturn(true);

    }

    private void prepareMockChannelSubsystem() throws Exception, JSchException, IOException {
        final InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                return 48;
            }
        };
        expect(session.openChannel(anyObject(String.class))).andReturn(channelSubsystem);
        channelSubsystem.setSubsystem("netconf");
        expectLastCall();
        channelSubsystem.connect();
        expectLastCall();
        expect(channelSubsystem.getOutputStream()).andReturn(outputStream);
        expect(channelSubsystem.getInputStream()).andReturn(inputStream);
        expect(channelSubsystem.isClosed()).andReturn(false);
        expect(channelSubsystem.isConnected()).andReturn(true);
    }

    private void prepareMockChannelSubsystem_Exception() throws Exception, JSchException, IOException {
        prepareChannelSystem(inputStream, outputStream);
    }

    private void prepareChannelSystem(final InputStream inputStream, final OutputStream outputStream)
            throws JSchException, IOException {
        expect(session.openChannel(anyObject(String.class))).andReturn(channelSubsystem);
        channelSubsystem.setSubsystem("netconf");
        expectLastCall();
        channelSubsystem.connect();
        expectLastCall().andThrow(new JSchException());
        expect(channelSubsystem.getOutputStream()).andReturn(outputStream);
        expect(channelSubsystem.getInputStream()).andReturn(inputStream);

    }

    private void prepareMockChannelSubsystem_ReadException() throws Exception, JSchException, IOException {
        final InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException();
            }
        };
        prepareChannelSystem(inputStream, outputStream);
    }

    private void prepareMockChannelSubsystem_WriteException() throws Exception, JSchException, IOException {
        final OutputStream outputStream = new OutputStream() {

            @Override
            public void write(final int b) throws IOException {
                throw new IOException();
            }
        };
        prepareChannelSystem(inputStream, outputStream);
    }

    private void prepareMockforCloseSession() throws Exception {
        session.disconnect();
        expectLastCall();
        channelSubsystem.disconnect();
        expectLastCall();
    }

    private void replayAll() {
        replaySession();
        replay(channelSubsystem, ChannelSubsystem.class);
    }

    private void replaySession() {
        replay(jsch, JSch.class);
        replay(session, Session.class);
    }

}
///*------------------------------------------------------------------------------
// *******************************************************************************
// * COPYRIGHT Ericsson 2014
// *
// * The copyright to the computer program(s) herein is the property of
// * Ericsson Inc. The programs may be used and/or copied only with written
// * permission from Ericsson Inc. or in accordance with the terms and
// * conditions stipulated in the agreement/contract under which the
// * program(s) have been supplied.
// *******************************************************************************
// *----------------------------------------------------------------------------*/
//package com.ericsson.oss.mediation.adapter.ssh.provider;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.net.SocketException;
//import java.util.concurrent.atomic.AtomicBoolean;
//import java.util.regex.Pattern;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import com.maverick.ssh.ChannelOpenException;
//import com.maverick.ssh.SshClient;
//import com.maverick.ssh.SshException;
//import com.maverick.ssh.SshSession;
//import com.maverick.ssh2.Ssh2Session;
//import com.sshtools.net.SocketTransport;
//
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//
//import static org.junit.Assert.*;
//import static org.mockito.Mockito.*;
//import static com.ericsson.oss.mediation.unit.reflection.ReflectionTestUtils.setNonPrimitiveField;
//
//@RunWith(MockitoJUnitRunner.class)
//public class SSHSessionProviderTest {
//
//	@Mock
//	private SocketTransport socketTransport;
//
//	@Mock
//	private SshClient sshClient;
//
//	@Mock
//	private SshSession sshSession;
//
//	@Mock
//	private Ssh2Session ssh2Session;
//
//	@Mock
//	private OutputStream stdOut;
//
//	String str = "hello";
//
//	private final InputStream stdIn = new ByteArrayInputStream(str.getBytes());
//
//	SSHSessionProvider sshSessionProvider;
//
//	private final AtomicBoolean askedSessionAbort = new AtomicBoolean();
//
//	final Class sshClass = SSHSessionProvider.class;
//
//	@Before
//	public void setUp() throws Exception {
//		sshSessionProvider = new SSHSessionProvider("1.1.1.1", 1000, "", "",
//				10, ".", "", "\\w", "\\w");
//
//		setNonPrimitiveField(SshClient.class, sshSessionProvider, sshClient);
//		setNonPrimitiveField(SshSession.class, sshSessionProvider, sshSession);
//		setNonPrimitiveField(SocketTransport.class, sshSessionProvider,
//				socketTransport);
//
//		when(sshSession.requestPseudoTerminal("vt100", 0, 0, 0, 0)).thenReturn(
//				Boolean.TRUE);
//		when(sshSession.startShell()).thenReturn(Boolean.TRUE);
//		when(sshSession.getOutputStream()).thenReturn(stdOut);
//		when(sshSession.getInputStream()).thenReturn(stdIn);
//		when(sshClient.isConnected()).thenReturn(Boolean.TRUE);
//		when(sshSession.isClosed()).thenReturn(Boolean.FALSE);
//
//		sshSessionProvider.setStdIn(stdOut);
//		sshSessionProvider.setStdOut(stdIn);
//		sshSessionProvider.getStdOut();
//		sshSessionProvider.getStdIn();
//		sshSessionProvider.setWaitTimeForResponse(200);
//		sshSessionProvider.getWaitTimeForResponse();
//		sshSessionProvider.setAbortRequestState(askedSessionAbort);
//		sshSessionProvider.toString();
//	}
//
//	@After
//	public void tearDown() throws Exception {
//	}
//
//	@Test
//	public void testSetSocketTimeout() throws SocketException {
//		sshSessionProvider.setSocketTimeout(10);
//	}
//
//	@Test(expected = OpenConnectionException.class)
//	public void testOpenSession() throws OpenConnectionException,
//			InitializeSSHSessionException, TimeoutExpiredException,
//			AuthenticationErrorException, ErrorPatternFoundException,
//			InvalidPatternException, SocketTimeoutChangeException,
//			CLISessionException {
//		sshSessionProvider.openSession("");
//	}
//
//	@Test
//	public void testIsConnectedReturningTrue() {
//		sshSessionProvider.isConnected();
//	}
//
//	@Test
//	public void testIsConnected() {
//		sshSessionProvider.isConnected();
//	}
//
//	@Test
//	public void testCloseSession() {
//		sshSessionProvider.closeSession();
//	}
//
//	@Test
//	public void testStartSession() throws NoSuchMethodException,
//			IllegalAccessException, InvocationTargetException,
//			InitializeSSHSessionException, TimeoutExpiredException,
//			ErrorPatternFoundException, CLISessionException, SshException,
//			ChannelOpenException {
//		when(sshClient.openSessionChannel()).thenReturn(sshSession);
//
//		final Class[] argTypes = new Class[] { Pattern.class };
//		final Pattern pattern = Pattern.compile("\\w");
//		final Method startSessionMethod = sshClass.getDeclaredMethod(
//				"startSession", argTypes);
//		startSessionMethod.setAccessible(true);
//		startSessionMethod.invoke(sshSessionProvider, (Object) pattern);
//	}
//
//	@Test
//	public void testStartSessionAndSubSystem() throws NoSuchMethodException,
//			IllegalAccessException, InvocationTargetException,
//			InitializeSSHSessionException, TimeoutExpiredException,
//			ErrorPatternFoundException, CLISessionException, SshException,
//			ChannelOpenException {
//		when(sshClient.openSessionChannel()).thenReturn(ssh2Session);
//
//		final Class[] argTypes = new Class[] {};
//		final Method startSessionAndSubsystemMethod = sshClass
//				.getDeclaredMethod("startSessionAndSubsystem", argTypes);
//		startSessionAndSubsystemMethod.setAccessible(true);
//		startSessionAndSubsystemMethod.invoke(sshSessionProvider);
//	}
//
//	@Test
//	public void testSetCommandPrompt() throws InvalidPatternException {
//		sshSessionProvider.setCommandPrompt("\\w");
//	}
//
//	@Test
//	public void testExecuteCommand1() throws TimeoutExpiredException,
//			AbortRequestException, CLISessionException {
//		sshSessionProvider.executeCommand("");
//	}
//
//	@Test
//	public void testExecuteCommand2() throws TimeoutExpiredException,
//			AbortRequestException, CLISessionException, InvalidPatternException {
//		final Pattern pattern = Pattern.compile("\\w");
//		sshSessionProvider.executeCommand("", pattern);
//	}
//
//	@Test
//	public void testSendCommand() throws SocketTimeoutChangeException,
//			CLISessionException {
//		sshSessionProvider.sendCommand("hello");
//	}
//
//	@Test
//	public void testReadLastCharsByStdOut() throws NoSuchMethodException,
//			IllegalAccessException, InvocationTargetException {
//		final Class cliClass = CLISessionProvider.class;
//		final Class[] argTypes = new Class[] {};
//		final Method readLastCharsByStdOutMethod = cliClass.getDeclaredMethod(
//				"readLastCharsByStdOut", argTypes);
//		readLastCharsByStdOutMethod.setAccessible(true);
//		readLastCharsByStdOutMethod.invoke(sshSessionProvider);
//	}
//
//	@Test(expected = CLISessionException.class)
//	public void testReadOutputCommand1() throws SocketTimeoutChangeException,
//			CLISessionException {
//		final Pattern pattern = Pattern.compile("\\W");
//		sshSessionProvider.readOutputCommand(pattern);
//	}
//
//	@Test(expected = CLISessionException.class)
//	public void testReadOutputCommand2() throws SocketTimeoutChangeException,
//			CLISessionException {
//		final Pattern pattern1 = Pattern.compile("\\W");
//		final Pattern pattern2 = Pattern.compile("\\w");
//		sshSessionProvider.readOutputCommand(pattern1, pattern2);
//	}
//
//	@Test
//	public void testValidateMaxSessionAuthReachedMsgPattern()
//			throws SocketTimeoutChangeException, CLISessionException {
//		sshSessionProvider.validateMaxSessionAuthReachedMsgPattern(str);
//	}
//
//	@Test
//	public void testIsBusy() throws SocketTimeoutChangeException,
//			CLISessionException {
//		sshSessionProvider.setBusy(Boolean.TRUE);
//		assertTrue(sshSessionProvider.isBusy());
//	}
//
//}

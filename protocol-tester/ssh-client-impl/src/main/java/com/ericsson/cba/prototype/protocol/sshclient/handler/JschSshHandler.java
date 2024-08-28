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
package com.ericsson.cba.prototype.protocol.sshclient.handler;

import static com.ericsson.cba.prototype.protocol.sshclient.model.StaticValues.NETCONF_MESSAGE_END;
import static com.ericsson.cba.prototype.protocol.sshclient.model.StaticValues.helloQuery;
import static com.ericsson.cba.prototype.protocol.sshclient.util.Util.hidePassword;
import static com.ericsson.cba.prototype.protocol.sshclient.util.Util.isHelloHeader;

import java.io.*;

import com.ericsson.cba.prototype.protocol.sshclient.exception.ExceptionReason;
import com.ericsson.cba.prototype.protocol.sshclient.exception.SshTransportException;
import com.ericsson.cba.prototype.protocol.sshclient.model.SessionType;
import com.ericsson.cba.prototype.protocol.sshclient.model.query.Query;
import com.ericsson.cba.prototype.protocol.sshclient.ssh.*;
import com.jcraft.jsch.*;

/**
 * This is ssh-client implementation using Jsch library
 * 
 */
public class JschSshHandler implements SshClient {

	/**
	 * @param reader
	 * @return
	 */
	public String call(final BufferedReader reader) {
		String callbackMessage = "";
		try {
			callbackMessage = readResponseFromStream(reader);
			if (isHelloHeader(callbackMessage)) {// this means netconf agent
													// will provide one more
													// message.
				System.err.println("Found hello header reading again...");
				callbackMessage = readResponseFromStream(reader);
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return callbackMessage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ericsson.pman.ssh.SshClient#createSessionToSubsystem(com.ericsson
	 * .pman.ssh.SshConnection, com.ericsson.pman.model.SessionType,
	 * java.lang.String)
	 */
	public SshSession createSessionToSubsystem(
			final SshConnection sshConnection, final SessionType sessionType,
			final String sessionTypeValue) throws SshTransportException {
		isConnectionUp(sshConnection);
		final SshSession sshSession = new SshSession();
		try {
			final ChannelSubsystem channelSubSystem = (ChannelSubsystem) ((com.jcraft.jsch.Session) (sshConnection
					.getConnection()))
					.openChannel(sessionType.getSessionType());
			channelSubSystem.setSubsystem(sessionTypeValue);
			channelSubSystem.connect();
			sshSession.setSession(channelSubSystem);
			sshSession.setOutputStream(((ChannelSubsystem) (sshSession
					.getSession())).getOutputStream());
			sshSession.setInputStream(((ChannelSubsystem) (sshSession
					.getSession())).getInputStream());
			Thread.sleep(100l);
			System.err.println("channelSubSystem is live : "
					+ channelSubSystem.isConnected());
			sshSession.getOutputStream()
					.write(helloQuery.getQuery().getBytes());
			sshSession.getOutputStream().flush();
		} catch (final Exception e) {
			throw new SshTransportException(
					ExceptionReason.SUBSYTEM_CONNECTION_FAILURE, e.getMessage());
		}
		return sshSession;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ericsson.pman.ssh.SshClient#createSshConnection(java.lang.String,
	 * int, java.lang.String, java.lang.String)
	 */
	public SshConnection createSshConnection(final String hostname,
			final int port, final String username, final String password)
			throws SshTransportException {
		return createSshConnection(hostname, port, username, password, 0);// infinite
																			// timeout
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ericsson.pman.ssh.SshClient#createSshConnection(java.lang.String,
	 * int, java.lang.String, java.lang.String, int)
	 */
	public SshConnection createSshConnection(final String hostname,
			final int port, final String username, final String password,
			final int socketConnectionTimeoutInMillis)
			throws SshTransportException {
		final SshConnection sshConnection = new SshConnection();
		final JSch jsch = new JSch();
		try {
			final Session session = jsch.getSession(username, hostname, port);
			session.setPassword(password);
			session.setTimeout(socketConnectionTimeoutInMillis);// defined
																// timeout
			final java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			sshConnection.setConnection(session);
			Thread.sleep(100l);
		} catch (final Exception e) {
			throw new SshTransportException(
					ExceptionReason.CONNECTION_ESTABLISHMENT_FAILURE,
					" hostname : " + hostname + " port : " + port
							+ " username : " + username + " password : "
							+ hidePassword(password) + " exception : "
							+ e.getMessage());
		}
		return sshConnection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ericsson.pman.ssh.SshClient#disconnectSession(com.ericsson.pman.ssh
	 * .SshSession)
	 */
	public boolean disconnectSession(final SshSession sshSession)
			throws SshTransportException {
		isSessionUp(sshSession);
		try {
			((ChannelSubsystem) sshSession.getSession()).disconnect();
		} catch (final Exception e) {
			throw new SshTransportException(
					ExceptionReason.SESSION_DISCONNECTION_FAILURE,
					e.getMessage());
		}
		return true;
	}

	/**
	 * @param sshSession
	 * @return
	 * @throws SshTransportException
	 */
	private String handleInput(final SshSession sshSession)
			throws SshTransportException {
		System.err.println("Handling response..");
		final BufferedReader reader = new BufferedReader(new InputStreamReader(
				sshSession.getInputStream()));
		String deviceResponse = null;
		try {
			deviceResponse = call(reader);
		} catch (final Exception e) {
			throw new SshTransportException(
					ExceptionReason.SUBSYTEM_CONNECTION_FAILURE, e.getMessage());
		}
		return deviceResponse;
	}

	/**
	 * @param sshConnection
	 * @throws SshTransportException
	 */
	private void isConnectionUp(final SshConnection sshConnection)
			throws SshTransportException {
		if ((sshConnection == null) || (sshConnection.getConnection() == null)) {
			throw new SshTransportException(
					ExceptionReason.SSH_CONNECTION_NOT_AVAILABLE,
					"No connection established");
		}
	}

	/**
	 * @param sshSession
	 * @throws SshTransportException
	 */
	private void isSessionUp(final SshSession sshSession)
			throws SshTransportException {
		if ((sshSession == null) || (sshSession.getSession() == null)) {
			throw new SshTransportException(
					ExceptionReason.SSH_SESSION_NOT_AVAILABLE,
					"No session established");
		}

	}

	/**
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	private String readResponseFromStream(final BufferedReader reader)
			throws IOException {
		String callbackMessage = "";
		System.err.println("Reading from input stream..");
		String response = reader.readLine();
		while ((response != null)
				&& !response.equalsIgnoreCase(NETCONF_MESSAGE_END)) {
			System.err.println("response fregment : " + response);
			response = reader.readLine();
			callbackMessage += response;
		}
		System.err.println("input stream contents : " + callbackMessage);
		return callbackMessage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ericsson.pman.ssh.SshClient#removeConnection(com.ericsson.pman.ssh
	 * .SshConnection)
	 */
	public boolean removeConnection(final SshConnection sshConnection)
			throws SshTransportException {
		isConnectionUp(sshConnection);
		try {
			((com.jcraft.jsch.Session) sshConnection.getConnection())
					.disconnect();
		} catch (final Exception e) {
			throw new SshTransportException(
					ExceptionReason.SESSION_DISCONNECTION_FAILURE,
					e.getMessage());
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ericsson.pman.ssh.SshClient#runQuery(com.ericsson.pman.ssh.SshSession
	 * , com.ericsson.pman.model.query.Query)
	 */
	public String runQuery(final SshSession sshSession, final Query query)
			throws SshTransportException {
		isSessionUp(sshSession);
		try {
			System.err.println("Executing query over ssh...");
			writeToOutputStream(sshSession, query);
			return handleInput(sshSession);
		} catch (final Exception e) {
			throw new SshTransportException(
					ExceptionReason.QUERY_EXECUTION_FAILURE, e.getMessage());
		}
	}

	/**
	 * @param sshSession
	 * @param query
	 * @throws IOException
	 */
	private void writeToOutputStream(final SshSession sshSession,
			final Query query) throws IOException {
		System.err.println("Writing to output stream..");
		sshSession.getOutputStream().write(query.getQuery().getBytes());
		sshSession.getOutputStream().flush();
	}

}

/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.cba.prototype.protocol.netconf;

import java.io.*;

import com.jcraft.jsch.*;

/**
 * 
 * @author xshakku
 * 
 */
public class NetconfHelper {
	private Session session;
	private Channel channel;
	private OutputStream outputStream;
	private InputStream inputStream;
	public static final String NETCONF_HELLO_HEADER = "hello xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\"";
	public static final String NETCONF_MESSAGE_END = "]]>]]>";
	private static final String RPC_REPLY = "</rpc-reply>]]>]]>";

	public void openSession(CredentialHolder credentialHolder)
			throws JSchException, InterruptedException, IOException {
		JSch jsch = new JSch();
		session = jsch.getSession(credentialHolder.getUserName(),
				credentialHolder.getIPAddress(), credentialHolder.getPort());
		session.setPassword(credentialHolder.getPassword());
		session.setTimeout(20000);// defined timeout
		final java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.connect();
		Thread.sleep(100l);
		channel = (Channel) session.openChannel(credentialHolder
				.getChannelType());
		((ChannelSubsystem) channel).setSubsystem(credentialHolder
				.getChannelName());
		((ChannelSubsystem) channel).setPty(true);
		outputStream = channel.getOutputStream();
		inputStream = channel.getInputStream();
		channel.connect();
		System.err.println("Session is live : " + session.isConnected() + ", "
				+ "connection is live : " + channel.isConnected());

		System.err.println("Sending capabilities...");
		write(Queries.HELLO_QUERY);
//		write(Queries.getConfigQuery("300"));

		String response = readResponse();
		if (response.contains(NETCONF_HELLO_HEADER)) {
			System.err.println("receivedCapabilities : " + response);
			//response = readResponse();
		}
	}

	public void closeSession() throws IOException, JSchException {
		runQuery(Queries.closeSession("2090"));
		channel.disconnect();
		session.disconnect();
		System.err.println("Session is live : " + session.isConnected() + ", "
				+ "connection is live : " + channel.isConnected());
	}

	private String readResponse() throws IOException, JSchException {
		if (channel.isClosed()) {
			System.err.println("exit-status: " + channel.getExitStatus());
			return "";
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));
		String responseText = "";
		System.err.println("Reading from input stream..");
		String line = reader.readLine();
		while ((line != null && !line.contains(RPC_REPLY))
				&& !line.equalsIgnoreCase(NETCONF_MESSAGE_END)) {
			responseText += line;
			responseText += "\n";
			line = reader.readLine();
		}
		return responseText;
	}

	public String runQuery(String query) throws IOException, JSchException {
		write(query);
		String response = readResponse();
		if (response.contains("rpc-error")) {
			throw new RuntimeException(response);
		}
		return response;
	}

	private void write(String query) throws IOException {
		outputStream.write(query.getBytes());
		outputStream.flush();
	}

}

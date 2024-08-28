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
package com.ericsson.oss.mediation.adapter.tls.api;

/**
 * This class is used to hold the data required for the library
 * to connect to a peer.
 *
 * @author Team Sovereign
 * @version 1.0.42
 * @since 2015-01-15
 */
public class TlsConnectionRequest {
	private final String ipAddress;
	private final int port;

	/**
	 * @param ipAddress The IP Address of the peer.
	 * @param port The port to use for the connection.
	 */
	public TlsConnectionRequest(String ipAddress, int port) {
		super();
		this.ipAddress = ipAddress;
		this.port = port;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public int getPort() {
		return port;
	}

}

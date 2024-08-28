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
package com.ericsson.cba.prototype.protocol.sshclient.handler;

public class HandlerContext {
	private String hostName;
	private int port;
	private int socketTimeoutValueInMilli;
	private String username;
	private String password;
	private String sessionType;
	private String sessionTypeValue;

	/**
	 * @param hostName
	 *            the hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @param socketTimeoutValueInMilli
	 *            the socketTimeoutValueInMilli to set
	 */
	public void setSocketTimeoutValueInMilli(int socketTimeoutValueInMilli) {
		this.socketTimeoutValueInMilli = socketTimeoutValueInMilli;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param sessionType
	 *            the sessionType to set
	 */
	public void setSessionType(String sessionType) {
		this.sessionType = sessionType;
	}

	/**
	 * @param sessionTypeValue
	 *            the sessionTypeValue to set
	 */
	public void setSessionTypeValue(String sessionTypeValue) {
		this.sessionTypeValue = sessionTypeValue;
	}

	/**
	 * @return
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @return
	 */
	public Integer getPort() {
		return port;
	}

	/**
	 * @return
	 */
	public Integer getSocketTimeoutValueInMilli() {
		return socketTimeoutValueInMilli;
	}

	/**
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return
	 */
	public String getSessionTypeValue() {
		return sessionTypeValue;
	}

	/**
	 * @return
	 */
	public String getSessionType() {
		return sessionType;
	}

}

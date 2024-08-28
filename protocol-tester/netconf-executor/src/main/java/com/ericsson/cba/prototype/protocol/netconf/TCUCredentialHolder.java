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

/**
 * 
 * @author xshakku
 * 
 */
public class TCUCredentialHolder implements CredentialHolder {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ericsson.cba.prototype.protocol.netconf.CredentialHolder#getIPAddress
	 * ()
	 */
	@Override
	public String getIPAddress() {
		return "192.168.100.240";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ericsson.cba.prototype.protocol.netconf.CredentialHolder#getUserName
	 * ()
	 */
	@Override
	public String getUserName() {
		return "tcuuser";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ericsson.cba.prototype.protocol.netconf.CredentialHolder#getPassword
	 * ()
	 */
	@Override
	public String getPassword() {
		return "tcu123";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ericsson.cba.prototype.protocol.netconf.CredentialHolder#getPort()
	 */
	@Override
	public int getPort() {
		return 22;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ericsson.cba.prototype.protocol.netconf.CredentialHolder#getChannelType
	 * ()
	 */
	@Override
	public String getChannelType() {
		return "subsystem";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ericsson.cba.prototype.protocol.netconf.CredentialHolder#getSessionType
	 * ()
	 */
	@Override
	public String getChannelName() {
		return "netconf";
	}

}

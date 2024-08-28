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
package com.ericsson.oss.mediation.pm.handlers.error;

public enum HandlerError {
	GENERIC_EXCEPTION((short)1),
	NETCONF_OPERATION_FORMAT_ERROR((short)2),
	NETCONFMANAGER_EXCEPTION((short)3),
	NETCONFMANAGER_NOT_CONNECTED((short)4),
	NETCONF_OPERATION_FAILED((short)5);
	
	private short code;
	
	private HandlerError(final short code) {
		this.code = code;	
	}

	/**
	 * @return the code
	 */
	public short getCode() {
		return code;
	}	
}


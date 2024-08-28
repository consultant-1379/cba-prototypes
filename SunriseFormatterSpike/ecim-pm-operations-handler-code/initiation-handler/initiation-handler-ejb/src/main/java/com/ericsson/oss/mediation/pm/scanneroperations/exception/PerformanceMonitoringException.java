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
package com.ericsson.oss.mediation.pm.scanneroperations.exception;

import com.ericsson.oss.mediation.pm.handlers.error.HandlerError;

@SuppressWarnings("serial")
public class PerformanceMonitoringException extends Exception {
	
	private final HandlerError error;

	public PerformanceMonitoringException(final String message, final HandlerError error) {
		super(message);
		this.error = error;
	}

	/**
	 * @return the error
	 */
	public HandlerError getError() {
		return error;
	}
	
	public short getErrorCode() {
		return error.getCode();
	}
}

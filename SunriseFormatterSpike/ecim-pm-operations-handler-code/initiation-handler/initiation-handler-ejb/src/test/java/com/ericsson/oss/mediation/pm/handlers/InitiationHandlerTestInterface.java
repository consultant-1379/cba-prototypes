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
package com.ericsson.oss.mediation.pm.handlers;

public interface InitiationHandlerTestInterface {
	void prepareEvent();
	void testonEvent();
	void testonEventWhenNetconfNotConnected();
	void testonEventWhenNetconfException();
	void testonEventWhenLockFail();
	void testonEventWhenEditConfigFail();
	void testonEventWhenValidateFail();
	void testonEventWhenCommitFail();
	void testonEventWhenUnlockFail();
	void testonEventWhenJAXBException();
	void testonEventWhenSAXException();
	void testonEventWhenScannerOperationExecutorNull();
}

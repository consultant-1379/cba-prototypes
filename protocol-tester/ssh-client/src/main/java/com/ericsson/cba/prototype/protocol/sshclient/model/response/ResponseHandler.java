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
package com.ericsson.cba.prototype.protocol.sshclient.model.response;

import java.util.concurrent.Callable;

/**
 * SSH-Client implementation which implements asynchronous query handling implements ResponseHandler
 * 
 */
public interface ResponseHandler extends Callable<String> {

}

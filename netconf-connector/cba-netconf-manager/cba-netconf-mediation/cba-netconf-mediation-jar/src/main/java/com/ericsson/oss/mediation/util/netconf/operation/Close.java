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

package com.ericsson.oss.mediation.util.netconf.operation;

/**
 * 
 * @author xvaltda
 */
public class Close extends Operation {

    public Close() {
        operationType = OperationType.CLOSE_SESSION;
    }

    @Override
    public String getBody() {
        final String body = "<close-session/>\n";

        return body;
    }

}

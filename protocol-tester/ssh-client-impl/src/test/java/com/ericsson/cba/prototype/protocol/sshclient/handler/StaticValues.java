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

import com.ericsson.cba.prototype.protocol.sshclient.model.query.Query;

public abstract class StaticValues {

    public static final Query getChassisQuery = new Query("<rpc message-id=\"2\" xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">" + "<get>"
            + "<filter type=\"subtree\">" + "<ManagedElement xmlns=\"urn:com:ericsson:ecim:ComTop\">" + "<managedElementId>1</managedElementId>"
            + "<Chassis xmlns=\"urn:com:ericsson:ecim:IPOS_ChassisManagement\"/>" + "</ManagedElement>" + "</filter>" + "</get>" + "</rpc>"
            + "]]>]]>", 150000);

    public static void main(final String[] args) {
        System.out.println(getChassisQuery.getQuery());
    }

}

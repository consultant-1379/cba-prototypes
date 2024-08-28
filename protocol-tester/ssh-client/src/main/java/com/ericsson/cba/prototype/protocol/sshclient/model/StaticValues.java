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
package com.ericsson.cba.prototype.protocol.sshclient.model;

import com.ericsson.cba.prototype.protocol.sshclient.model.query.Query;

public abstract class StaticValues {
    public static final String NETCONF_HELLO_HEADER = "hello xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\"";
    public static final String NETCONF_MESSAGE_END = "]]>]]>";
    public static Query helloQuery = new Query("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<hello xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">" + "<capabilities>"
            + "<capability>urn:ietf:params:netconf:base:1.0</capability>" + "</capabilities>" + "</hello>" + "]]>]]>");

    public static void main(final String[] args) {
        System.out.println(helloQuery.getQuery());
    }

}

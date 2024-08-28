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

package com.ericsson.oss.mediation.util.netconf.manger.matcher;

import org.mockito.ArgumentMatcher;

import com.ericsson.oss.mediation.util.transport.api.TransportData;

/**
 * 
 * @author xdraked
 */
public class TransportCloseRequestMatcher extends ArgumentMatcher<TransportData> {

    private final String cmp;
    private static final String NL = System.lineSeparator();
    private static final String START = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + NL + "<rpc message-id=\"";
    private static final String END = "\txmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">" + NL
            + "\t<close-session/>" + NL + "</rpc>" + NL + "]]>]]>" + NL;

    public TransportCloseRequestMatcher(final String cmp) {
        this.cmp = cmp;
    }

    @Override
    public boolean matches(Object argument) {
        TransportData transportRequest = (TransportData) argument;
        final StringBuilder output = new StringBuilder();
        transportRequest.writeTo(output);
        final String buffer = output.toString();
        //I am ignoring messageID as it get be different (it is incremental) so I will not match it staticly
        //argument is the same if start and end of both params are the same (just ID is ignored)
        return (cmp.startsWith(START) && cmp.endsWith(END) && buffer.startsWith(START) && buffer.endsWith(END));
    }

}

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
package com.ericsson.oss.mediation.adapter.tls.api;

import com.ericsson.oss.mediation.adapter.tls.exception.TlsChannelException;
import com.ericsson.oss.mediation.adapter.tls.exception.TlsException;
import com.ericsson.oss.mediation.transport.tls.TlsConnectionImpl;

public class TLSMainClass {

    private static TlsConnectionRequest reqInfo = new TlsConnectionRequest("192.168.100.243", 6513);

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(final String[] args) throws InterruptedException {
        try {
            TlsResponse res;
            final TlsConnectionImpl tlsConnectionImpl = new TlsConnectionImpl(reqInfo);
            tlsConnectionImpl.connect();
            System.out.println("Tls Connection setup finished");
            System.out.println("------------------------------");
            System.out.println("Tls Write of HELLO Message finished");
            System.out.println("------------------------------");
            TlsRequest tlsRequest = new TlsRequest(HELLO);
            tlsConnectionImpl.sendNonBlocking(tlsRequest);
            res = tlsConnectionImpl.read();
            System.out.println("HELLO from Node " + res.getResponseMessageString());

            System.out.println("Sending GET config request");
            System.out.println("------------------------------");
            tlsRequest = new TlsRequest(GET);
            tlsConnectionImpl.sendNonBlocking(tlsRequest);
            res = tlsConnectionImpl.read();
            System.out.println("Response from Node " + res.getResponseMessageString());

            System.out.println("Sending GET config request");
            System.out.println("------------------------------");
            tlsRequest = new TlsRequest(GET);
            tlsConnectionImpl.sendNonBlocking(tlsRequest);
            res = tlsConnectionImpl.read();
            System.out.println("Response from Node " + res.getResponseMessageString());

            tlsConnectionImpl.disconnect();

        } catch (final TlsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static final String HELLO = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><hello xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">"
            + System.getProperty("line.separator") + "<capabilities>" + System.getProperty("line.separator") + "<capability>"
            + System.getProperty("line.separator") + "urn:ietf:params:netconf:base:1.0" + System.getProperty("line.separator") + "</capability>"
            + System.getProperty("line.separator") + "</capabilities>" + System.getProperty("line.separator") + "</hello>"
            + System.getProperty("line.separator") + "]]>]]>";

    public static final String CLOSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rpc message-id=\"5\" xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">"
            + System.getProperty("line.separator") + "<close-session/>" + System.getProperty("line.separator") + "</rpc>"
            + System.getProperty("line.separator") + "]]>]]>";

    public static final String GET = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rpc message-id=\"6\" xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">"
            + System.getProperty("line.separator") + "<get/>" + System.getProperty("line.separator") + "</rpc>"
            + System.getProperty("line.separator") + "]]>]]>";

}

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

package com.ericsson.oss.mediation.util.netconf.operation;

import com.ericsson.oss.mediation.util.netconf.api.Capability;
import com.ericsson.oss.mediation.util.netconf.capability.NetconfSessionCapabilities;
import com.ericsson.oss.mediation.util.netconf.context.NetconfSession;
import com.ericsson.oss.mediation.util.netconf.manger.NetconfTestConstants;
import static com.ericsson.oss.mediation.util.netconf.operation.Hello.NETCONF_END_TAG;
import static com.ericsson.oss.mediation.util.netconf.operation.Hello.XML_HEADER;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

/**
 * 
 * @author xvaltda
 */
public class HelloTest {

    @Test
    public void testApi() {

    }

    private Hello hello;
    private String sessionId = null;
    private NetconfSession session;
    private NetconfSessionCapabilities netconfSessionCapabilities;
    List<Capability> ecimCapabilities = new ArrayList<Capability>();

    public HelloTest() {

        session = Mockito.mock(NetconfSession.class);
        netconfSessionCapabilities = Mockito.mock(NetconfSessionCapabilities.class);

        Capability capabilityBase = new Capability("urn:ietf:params:ns:netconf:base:1.0", "base",
                "ietf:params:ns:netconf", "1.0");
        Capability capabilityBaseXML = new Capability("urn:ietf:params:xml:ns:netconf:base:1.0", "base",
                "ietf:params:xml:ns:netconf", "1.0");

        ecimCapabilities.add(capabilityBase);
        ecimCapabilities.add(capabilityBaseXML);

        when(session.getNetconfSessionCapabilities()).thenReturn(netconfSessionCapabilities);
        when(netconfSessionCapabilities.getEcimCapabilities()).thenReturn(ecimCapabilities);
    }

    @Test
    public void testCreateHelloOperation() {

        final List<String> capabilities = new ArrayList<String>();
        final List<String> xmlContent = new ArrayList<String>();

        String xml = XML_HEADER + "<hello xmlns=\"" + NetconfTestConstants.XMLNS + "\">\n";
        xml += "\t<capabilities>\n";

        xmlContent.add(xml);

        capabilities.add(Capability.BASE.toString());

        for (final String capability : capabilities) {
            xmlContent.add("\t\t<capability>" + capability + "</capability>\n");
        }

        xmlContent.add("\t</capabilities>\n");

        if (sessionId != null)
            xmlContent.add("\t<session-id>" + sessionId + "</session-id>\n");

        xmlContent.add("</hello>" + NETCONF_END_TAG);

        hello = new Hello();

        Assert.assertTrue((hello.getOperationType() == OperationType.HELLO));

        final Iterator<String> operationIterator = hello.iterator();
        final Iterator expectedIterator = xmlContent.iterator();

        while (operationIterator.hasNext() && expectedIterator.hasNext()) {
            Assert.assertTrue((expectedIterator.next().equals(operationIterator.next())));

        }

    }

    @Test
    public void testCreateHelloOperationWithEcimCapabilities() {

        final List<String> capabilities = new ArrayList<String>();
        final List<String> xmlContent = new ArrayList<String>();

        String xml = XML_HEADER + "<hello xmlns=\"" + NetconfTestConstants.XMLNS + "\">\n";
        xml += "\t<capabilities>\n";

        xmlContent.add(xml);

        capabilities.add(Capability.BASE.toString());

        for (final Capability capability : ecimCapabilities) {
            xmlContent.add("\t\t<capability>" + capability + "</capability>\n");
        }

        xmlContent.add("\t</capabilities>\n");

        if (sessionId != null)
            xmlContent.add("\t<session-id>" + sessionId + "</session-id>\n");

        xmlContent.add("</hello>" + NETCONF_END_TAG);

        hello = new Hello(session.getNetconfSessionCapabilities().getEcimCapabilities());

        System.out.println("hello.getOperationType()  " + hello.getOperationType());
        Assert.assertTrue((hello.getOperationType() == OperationType.HELLO));

        final Iterator<String> operationIterator = hello.iterator();
        final Iterator expectedIterator = xmlContent.iterator();

        while (operationIterator.hasNext() && expectedIterator.hasNext()) {
            Assert.assertTrue((expectedIterator.next().equals(operationIterator.next())));

        }

    }

}

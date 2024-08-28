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

import com.ericsson.oss.mediation.util.netconf.api.Capability;
import com.ericsson.oss.mediation.util.transport.api.TransportData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author xvaltda
 */
public class Hello extends Operation implements Iterable<String> {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Hello.class);

    protected static final String XMLNS = "urn:ietf:params:xml:ns:netconf:base:1.0";
    protected static final String NETCONF_END_TAG = "]]>]]>" + System.lineSeparator();
    protected static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private final List<Capability> capabilities;

    public Hello(final List<Capability> ecimCapabilities) {
        operationType = OperationType.HELLO;
        capabilities = new ArrayList<>();

        for (final Capability capability : ecimCapabilities) {
            if ("base".equals(capability.getName())) {
                logger.debug("Adding capability: " + capability + " in hello message");
                capabilities.add(capability);
            }
        }
    }

    public Hello() {
        capabilities = new ArrayList<Capability>();
        capabilities.add(Capability.BASE);
        operationType = OperationType.HELLO;
    }

    public Iterator<String> iterator() {
        final List<String> xmlContent = new ArrayList<String>();

        String xml = XML_HEADER + "<hello xmlns=\"" + XMLNS + "\">\n";
        xml += "\t<capabilities>\n";

        xmlContent.add(xml);

        for (final Capability capability : capabilities) {
            xmlContent.add("\t\t<capability>" + capability + "</capability>\n");
        }

        xmlContent.add("\t</capabilities>\n");

        xmlContent.add("</hello>" + NETCONF_END_TAG);

        return xmlContent.iterator();
    }

    public String getBody() {
        final String xml = "no apply";

        return xml;
    }

    public TransportData getTransportData() {
        final TransportData transportData = new TransportData();
        transportData.setWritableData(this);
        return transportData;
    }
}
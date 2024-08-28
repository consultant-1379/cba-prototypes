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

package com.ericsson.oss.mediation.util.netconf.parser;

import com.ericsson.oss.mediation.util.netconf.api.Capability;
import com.ericsson.oss.mediation.util.netconf.capability.CapabilityFormatException;
import com.ericsson.oss.mediation.util.netconf.capability.CapabilityParser;
import com.ericsson.oss.mediation.util.netconf.capability.CapabilityParserFactory;
import com.ericsson.oss.mediation.util.netconf.capability.CapabilityProtocolType;
import com.ericsson.oss.mediation.util.netconf.vo.HelloVo;
import com.ericsson.oss.mediation.util.netconf.vo.NetconfVo;
import com.ericsson.oss.mediation.util.transport.api.TransportData;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class HelloParser extends RpcErrorParser {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HelloParser.class);

    private boolean insideCapabilityTag = false;
    private boolean insideSessionIdTag = false;
    private StringBuffer capabilityTagContent = new StringBuffer();
    private StringBuffer sessionIdTagContent = new StringBuffer();
    private List<Capability> capabilities;
    private HelloVo helloVo;

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes)
            throws SAXException {

        super.startElement(uri, localName, qName, attributes);

        if (localName.equalsIgnoreCase("hello")) {
            helloVo = new HelloVo();
            capabilities = new ArrayList<Capability>();
        } else if (localName.equalsIgnoreCase("capability")) {
            insideCapabilityTag = true;
        } else if (localName.equalsIgnoreCase("session-id")) {
            insideSessionIdTag = true;
        }
    }

    @Override
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        super.characters(ch, start, length);

        if (insideCapabilityTag) {
            capabilityTagContent.append(ch, start, length);
        } else if (insideSessionIdTag) {
            sessionIdTagContent.append(ch, start, length);
        }
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        if (localName.equalsIgnoreCase("hello")) {
        } else if (localName.equalsIgnoreCase("capabilities")) {
            helloVo.setCapabilities(capabilities);
        } else if (localName.equalsIgnoreCase("capability")) {
            insideCapabilityTag = false;
            final String stringifiedCapability = capabilityTagContent.toString();
            final String protocol = stringifiedCapability.split(":")[0];

            try {
                final CapabilityProtocolType capabilityType = CapabilityProtocolType.fromProtocol(protocol);
                final CapabilityParser parser = CapabilityParserFactory.getInstance().create(capabilityType);
                capabilities.add(parser.parse(stringifiedCapability));
            } catch (final CapabilityFormatException e) {
                logger.error("Exception in parsing node capability : " + e.getMessage()
                        + ". So, skipping this capability.");
            }
            capabilityTagContent = new StringBuffer();
        } else if (localName.equalsIgnoreCase("session-id")) {
            insideSessionIdTag = false;
            helloVo.setSessionId(sessionIdTagContent.toString());
            sessionIdTagContent = new StringBuffer();
        }
    }

    @Override
    public void error(final SAXParseException e) throws SAXException {
    }

    @Override
    public void fatalError(final SAXParseException e) throws SAXException {
    }

    @Override
    public NetconfVo parse(final TransportData data) throws ParserException {

        XMLReader parser;

        try {
            parser = XMLReaderFactory.createXMLReader();
            parser.setContentHandler(this);
            parser.setErrorHandler(this);
            parser.parse(new InputSource(new StringReader(data.getData().toString())));

        } catch (final SAXException ex) {
            logger.debug("Exception trying to parse the hello received from the node");
            throw new ParserException("Exception trying to parse" + " the hello received from the node");

        } catch (final IOException ex) {
            logger.debug("Exception trying to parse the hello received from the node");
            throw new ParserException("Exception trying to parse" + " the hello received from the node");
        }

        helloVo.setErrorReceived(isError);

        return isError ? rpcErrorVo : helloVo;
    }
}
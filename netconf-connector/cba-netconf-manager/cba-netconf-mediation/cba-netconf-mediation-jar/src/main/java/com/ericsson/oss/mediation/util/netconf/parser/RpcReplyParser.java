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

import com.ericsson.oss.mediation.util.netconf.vo.NetconfVo;
import com.ericsson.oss.mediation.util.netconf.vo.RpcReplyVo;
import com.ericsson.oss.mediation.util.transport.api.TransportData;
import java.io.IOException;
import java.io.StringReader;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * 
 * @author xvaltda
 */
public class RpcReplyParser extends RpcErrorParser {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RpcReplyParser.class);
    private RpcReplyVo rpcReplyVo;

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes)
            throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        if (localName.equalsIgnoreCase("rpc-reply")) {
            rpcReplyVo = new RpcReplyVo();

            final String messageId = attributes.getValue("message-id");
            if (messageId == null) {
                throw new SAXException(new ParserException("Received <rpc-reply> message without a messageId"));
            }
            rpcReplyVo.setMessageId(messageId);
            rpcReplyVo.setOk(false); // defaults to false
        } else if (localName.equalsIgnoreCase("ok")) {
            rpcReplyVo.setOk(true);
        }
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
            ex.printStackTrace();
            logger.debug("Exception trying to parse the rpc-reply received from the node, data received: {} " +ex.getMessage(), data
                    .getData().toString());
            throw new ParserException("Exception trying to parse"
                    + " the rpc-reply  received from the node, data received " + data.getData().toString());

        } catch (final IOException ex) {
            ex.printStackTrace();
            logger.debug("Exception trying to parse the rpc-reply  received from the node, data received {} "+ex.getMessage(), data
                    .getData().toString());
            throw new ParserException("Exception trying to parse"
                    + " the rpc-reply  received from the node, data received: " + data.getData().toString());
        }

        rpcReplyVo.setErrorReceived(isError);
        return isError ? rpcErrorVo : rpcReplyVo;
    }

}
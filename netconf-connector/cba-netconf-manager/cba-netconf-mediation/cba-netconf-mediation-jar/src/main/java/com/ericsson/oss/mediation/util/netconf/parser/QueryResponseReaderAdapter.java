/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ericsson.oss.mediation.util.netconf.parser;

import com.ericsson.oss.mediation.util.netconf.vo.NetconfVo;
import com.ericsson.oss.mediation.util.netconf.vo.RpcReplyVo;
import com.ericsson.oss.mediation.util.transport.api.TransportData;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * 
 * @author xvaltda
 */
public class QueryResponseReaderAdapter extends RpcErrorParser {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RpcReplyParser.class);
    private final RpcReplyVo rpcReplyVo;

    public QueryResponseReaderAdapter() {
        rpcReplyVo = new RpcReplyVo();
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes)
            throws SAXException {

        super.startElement(uri, localName, qName, attributes);

    }

    @Override
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        super.characters(ch, start, length);

    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        super.endElement(uri, localName, qName);
    }

    @Override
    public void error(final SAXParseException e) throws SAXException {
    }

    @Override
    public void fatalError(final SAXParseException e) throws SAXException {
    }

    @Override
    public NetconfVo parse(final TransportData data) throws ParserException {

        logger.debug("Trying to parse rpc-reply");
        final XMLReader parser;
        final String rpcData = data.getData().toString();
        try {
            if (data.getData().indexOf("rpc-error") > -1) {
                
                logger.info("rpc-error received:\n "+rpcData);
                parser = XMLReaderFactory.createXMLReader();
                parser.setContentHandler(this);
                parser.setErrorHandler(this);
                parser.parse(new InputSource(new StringReader(rpcData)));

            } else {
                final XMLInputFactory parserFactory = XMLInputFactory.newInstance();
                final XMLStreamReader reader = parserFactory.createXMLStreamReader(new StringReader(rpcData));
                final QueryResponseReader queryReader = new QueryResponseReader("data", reader);
                rpcReplyVo.setData(queryReader.getData());
            }

        } catch (final SAXException ex) {
            logger.debug("Exception trying to parse the rpc-reply received from the node "+ex.getMessage());
            throw new ParserException("Exception trying to parse" + " the rpc-reply received from the node "+ex.getMessage());

        } catch (final IOException ex) {
            ex.printStackTrace();
            logger.debug("Exception trying to parse the rpc-reply received from the node "+ex.getMessage());
            throw new ParserException("Exception trying to parse" + " the rpc-reply received from the node "+ex.getMessage());
        } catch (XMLStreamException ex) {
            ex.printStackTrace();
            logger.debug("Exception trying to parse the rpc-reply received from the node "+ex.getMessage());
            throw new ParserException("Exception trying to parse" + " the rpc-reply received from the node "+ex.getMessage());
        }

        rpcReplyVo.setErrorReceived(isError);
        return isError ? rpcErrorVo : rpcReplyVo;
    }
}

package com.ericsson.oss.mediation.util.netconf.parser;

import com.ericsson.oss.mediation.util.netconf.vo.NetconfVo;
import com.ericsson.oss.mediation.util.netconf.vo.RpcReplyVo;
import com.ericsson.oss.mediation.util.transport.api.TransportData;
import java.io.StringReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryResponseReader implements NetconfParser {

    private static final Logger logger = LoggerFactory.getLogger(QueryResponseReader.class);

    private final XMLInputFactory parserFactory;

    private final String tagName;
    private final StringBuilder data;
    private int tagCounter;
    private final XMLStreamReader reader;
    private boolean dataReady;

    public QueryResponseReader(final String tagName, final XMLStreamReader reader) {
        this.tagName = tagName;
        this.reader = reader;
        this.data = new StringBuilder();
        this.dataReady = false;
        this.tagCounter = 0;
        this.parserFactory = XMLInputFactory.newInstance();
    }

    public String getData() throws XMLStreamException {
        if (!dataReady) {
            while (reader.hasNext() && !dataReady) {
                final int event = reader.next();
                if (this.tagCounter > 0) {
                    switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        if (reader.getLocalName().equalsIgnoreCase(this.tagName)) {
                            this.tagCounter = this.tagCounter + 1;
                        }
                        data.append("<").append(reader.getLocalName());
                        int count = reader.getAttributeCount();
                        if (count > 0) {
                            for (int i = 0; i < count; i++) {
                                data.append(" ");
                                data.append(reader.getAttributeName(i).toString());
                                data.append("=");
                                data.append("\"");
                                data.append(reader.getAttributeValue(i));
                                data.append("\"");
                            }

                        }
                        count = reader.getNamespaceCount();
                        if (count > 0) {
                            for (int i = 0; i < count; i++) {
                                data.append(" ");
                                data.append("xmlns");
                                if (reader.getNamespacePrefix(i) != null) {
                                    data.append(":").append(reader.getNamespacePrefix(i));
                                }
                                data.append("=");
                                data.append("\"");
                                data.append(reader.getNamespaceURI(i));
                                data.append("\"");
                            }
                        }
                        data.append(">");
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        if (reader.getLocalName().equalsIgnoreCase(this.tagName)) {
                            if (this.tagCounter == 1) {
                                dataReady = true;
                            } else {
                                this.tagCounter = this.tagCounter - 1;
                                data.append("</").append(reader.getLocalName()).append(">");
                            }
                        } else {
                            data.append("</").append(reader.getLocalName()).append(">");
                        }
                        break;
                    case XMLStreamConstants.CDATA:
                        data.append(reader.getTextCharacters(), reader.getTextStart(), reader.getTextLength());
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        data.append(reader.getTextCharacters(), reader.getTextStart(), reader.getTextLength());
                        break;
                    case XMLStreamConstants.COMMENT:
                        data.append("<!--")
                                .append(reader.getTextCharacters(), reader.getTextStart(), reader.getTextLength())
                                .append("-->");
                        break;
                    }
                } else {
                    if (event == XMLStreamConstants.START_ELEMENT
                            && reader.getLocalName().equalsIgnoreCase(this.tagName)) {
                        this.tagCounter = 1;
                    }
                }
            }
        }
        return data.toString();
    }

    @Override
    public NetconfVo parse(final TransportData data) throws ParserException {

        final RpcReplyVo rpcReply = new RpcReplyVo();

        try {
            final XMLStreamReader reader = parserFactory.createXMLStreamReader(new StringReader(data.getData()
                    .toString()));
            final QueryResponseReader qrReader = new QueryResponseReader("data", reader);

            rpcReply.setData(qrReader.getData());
            return rpcReply;

        } catch (final XMLStreamException e) {
            logger.error("I wasn't able to parse <get> request response", e);
            throw new ParserException("I wasn't able to parse <get> request response", e);
        }
    }

}

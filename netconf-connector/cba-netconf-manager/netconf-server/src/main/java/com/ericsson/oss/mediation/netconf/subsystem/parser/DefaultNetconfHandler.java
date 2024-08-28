package com.ericsson.oss.mediation.netconf.subsystem.parser;

import com.ericsson.oss.mediation.netconf.subsystem.CommandListener;
import com.ericsson.oss.mediation.netconf.subsystem.parser.operation.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultNetconfHandler extends DefaultHandler2 {

    private Operation operation;
    private final PrintWriter out;
    private int level;
    private final AtomicBoolean closed;

    CommandListener commandListener;

    public DefaultNetconfHandler(final CommandListener commandListener, final PrintWriter out,
            final AtomicBoolean closed) {
        this.commandListener = commandListener;
        this.out = out;
        this.level = 0;
        this.operation = new Nop();
        this.closed = closed;
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes)
            throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        switch (this.level) {
        case 0:
            if (closed.get()) {
                throw new SAXException("Netconf session was closed. I won't process xml anymore");
            }
            this.operation = this.operation.processTag(localName, attributes, level);
            break;
        case Integer.MAX_VALUE:
            throw new SAXException("Maximum level of nesting is reached. Level " + this.level + ". Tag " + localName);
        default:
            this.operation = this.operation.processTag(localName, attributes, level);
            break;
        }
        this.level += 1;
    }

    @Override
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        super.characters(ch, start, length);
        this.operation = this.operation.characters(ch, start, length);
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        this.level -= 1;
        this.operation = this.operation.processEndTag(localName, level);
        if (level == 0) {
            this.operation = this.operation.invoke(commandListener, out);
        }
    }

    public boolean isNop() {
        return this.operation instanceof Nop;
    }
}

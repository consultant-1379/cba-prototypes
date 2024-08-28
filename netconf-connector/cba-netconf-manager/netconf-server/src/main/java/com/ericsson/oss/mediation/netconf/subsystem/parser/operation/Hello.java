package com.ericsson.oss.mediation.netconf.subsystem.parser.operation;

import com.ericsson.oss.mediation.netconf.subsystem.CommandListener;
import org.xml.sax.Attributes;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Hello extends Request {

    protected List<String> capabilities;
    private StringBuilder buffer;

    protected Hello(final Request request) {
        super(request);
    }

    @Override
    public Hello processTag(final String tag, final Attributes attributes, final int level) {
        switch (tag) {
        case "capabilities":
            if (level == 1) {
                this.capabilities = new LinkedList<>();
            }
            break;
        case "capability":
            if (level == 2) {
                buffer = new StringBuilder();
            }
            break;
        }
        return this;
    }

    @Override
    public Operation characters(final char[] ch, final int start, final int length) {
        if (buffer != null) {
            buffer.append(ch, start, length);
        }
        return this;
    }

    @Override
    public Operation processEndTag(final String tag, final int level) {
        if ("capability".equalsIgnoreCase(tag) && level == 2 && this.capabilities != null) {
            this.capabilities.add(buffer.toString());
            buffer = null;
        }
        return this;
    }

    @Override
    public Operation invoke(final CommandListener commandListener, final PrintWriter out) {
        commandListener.hello(
                this.capabilities == null ? Collections.<String> emptyList() : Collections
                        .unmodifiableList(this.capabilities), -1, out);
        return new Nop();
    }

}

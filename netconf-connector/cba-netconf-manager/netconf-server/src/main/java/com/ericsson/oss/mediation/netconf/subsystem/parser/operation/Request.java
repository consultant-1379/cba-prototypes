package com.ericsson.oss.mediation.netconf.subsystem.parser.operation;

import com.ericsson.oss.mediation.netconf.subsystem.CommandListener;
import org.xml.sax.Attributes;

import java.io.PrintWriter;

public class Request implements Operation {

    protected final String name;

    protected Request(final Request request) {
        this.name = request.name;
    }

    protected Request(final String name) {
        this.name = name;
    }

    public Operation processTag(final String tag, final Attributes attributes, final int level) {
        if (level == 0) {
            switch (tag) {
            case "rpc":
                return new Rpc(this).processTag(tag, attributes, level);
            case "hello":
                return new Hello(this).processTag(tag, attributes, level);
            }
        }
        return this;
    }

    public Operation processEndTag(final String tag, final int level) {
        return this;
    }

    public Operation characters(final char[] ch, final int start, final int length) {
        return this;
    }

    public Operation invoke(final CommandListener commandListener, final PrintWriter out) {
        throw new UnsupportedOperationException("I cannot invoke an unknown operation [" + this.name + "]");
    }

}

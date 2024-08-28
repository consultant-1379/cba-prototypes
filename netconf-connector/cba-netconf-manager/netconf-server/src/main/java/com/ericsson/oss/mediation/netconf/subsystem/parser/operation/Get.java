package com.ericsson.oss.mediation.netconf.subsystem.parser.operation;

import com.ericsson.oss.mediation.netconf.subsystem.CommandListener;
import com.ericsson.oss.mediation.util.netconf.api.Filter;
import org.xml.sax.Attributes;

import java.io.PrintWriter;

public class Get extends Rpc {

    private Filter filter;
    private String filterType;
    private StringBuilder filterBody;

    protected Get(final Rpc rpc) {
        super(rpc);
    }

    @Override
    public Get processTag(final String tag, final Attributes attributes, final int level) {
        switch (level) {
        case 2:
            switch (tag.toLowerCase()) {
            case "filter":
                filterType = attributes.getValue("type");
                filterBody = new StringBuilder();
                break;
            }
            break;
        default:
            if (level > 2 && filterBody != null) {
                filterBody.append("<").append(tag);
                for (int i = 0; i < attributes.getLength(); i++) {
                    filterBody.append(" ").append(attributes.getLocalName(i)).append("=\"")
                            .append(attributes.getValue(i)).append("\"");
                }
                filterBody.append(">");
            }
            break;
        }
        return this;
    }

    @Override
    public Operation characters(final char[] ch, final int start, final int length) {
        if (filterBody != null) {
            filterBody.append(ch, start, length);
        }
        return this;
    }

    @Override
    public Operation processEndTag(final String tag, final int level) {
        switch (level) {
        case 2:
            switch (tag.toLowerCase()) {
            case "filter":
                filter = new Filter() {
                    private String type;
                    private String body;

                    Filter init(final String type, final StringBuilder body) {
                        this.type = type;
                        this.body = body.toString();
                        return this;
                    }

                    @Override
                    public String getType() {
                        return type;
                    }

                    @Override
                    public String asString() {
                        return body;
                    }
                }.init(filterType, filterBody);
                filterType = null;
                filterBody = null;
                break;
            }
        default:
            if (level > 2 && filterBody != null) {
                filterBody.append("</").append(tag).append(">");
            }
            break;
        }
        return this;
    }

    @Override
    public Operation invoke(final CommandListener commandListener, final PrintWriter out) {
        commandListener.get(messageId, filter, out);
        return new Nop();
    }
}

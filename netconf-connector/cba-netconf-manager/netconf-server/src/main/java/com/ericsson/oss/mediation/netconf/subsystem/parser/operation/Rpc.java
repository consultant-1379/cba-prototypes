package com.ericsson.oss.mediation.netconf.subsystem.parser.operation;

import org.xml.sax.Attributes;

public class Rpc extends Request {

    protected String messageId;

    public Rpc(final Rpc rpc) {
        super(rpc);
        this.messageId = rpc.messageId;
    }

    protected Rpc(final Request request) {
        super(request);
    }

    @Override
    public Operation processTag(final String tag, final Attributes attributes, final int level) {
        switch (level) {
        case 0:
            if ("rpc".equalsIgnoreCase(tag)) {
                messageId = attributes.getValue("message-id");
            }
            break;
        case 1:
            switch (tag) {
            case "close-session":
                return new CloseSession(this);
            case "kill-session":
                return new KillSession(this);
            case "get":
                return new Get(this);
            case "get-config":
                return new GetConfig(this);
            }
        }
        return this;
    }

}

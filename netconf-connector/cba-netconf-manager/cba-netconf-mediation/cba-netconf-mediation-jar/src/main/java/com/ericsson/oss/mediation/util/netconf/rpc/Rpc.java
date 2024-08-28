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

package com.ericsson.oss.mediation.util.netconf.rpc;

import com.ericsson.oss.mediation.util.netconf.operation.Operation;
import com.ericsson.oss.mediation.util.netconf.rpc.request.Request;
import com.ericsson.oss.mediation.util.transport.api.TransportData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Rpc extends Request {
    private final long messageId;
    private final String body;
    private final Operation operation;

    private Rpc(final RpcBuilder rpcBuilder) {
        this.messageId = rpcBuilder.messageId;
        this.body = "";
        this.operation = rpcBuilder.operation;
    }

    @Override
    public Iterator<String> iterator() {
        final List<String> data = new ArrayList<>();
        data.add(XML_HEADER);
        data.add(System.lineSeparator());
        data.add("<rpc message-id=\"");
        data.add(String.valueOf(messageId));
        data.add("\"");
        data.add(System.lineSeparator());
        data.add("\txmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">");
        data.add(System.lineSeparator());
        if (this.operation == null) {
            data.add(body);
        } else {
            data.add(this.operation.getBody());
        }

        data.add("</rpc>");
        data.add(System.lineSeparator());
        data.add(NETCONF_END_TAG);
        return data.iterator();
    }

    @Override
    public TransportData getTransportData() {
        final TransportData transportData = new TransportData();
        transportData.setWritableData(this);
        transportData.setMessageId(this.messageId);
        return transportData;
    }

    public long getMessageID() {
        return messageId;
    }

    public static class RpcBuilder {

        private final Operation operation;
        private long messageId;

        public RpcBuilder(final Operation operation) {
            this.operation = operation;
        }

        public Rpc build() {
            messageId = MessageIdManager.generateNextMessageID();
            return new Rpc(this);
        }
    }

}
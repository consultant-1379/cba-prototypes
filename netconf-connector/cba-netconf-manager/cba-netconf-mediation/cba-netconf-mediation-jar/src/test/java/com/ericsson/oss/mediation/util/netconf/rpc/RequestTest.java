/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
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
import com.ericsson.oss.mediation.util.transport.api.TransportData;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RequestTest {

    @Test
    public void testRpc() {
        long messageId = MessageIdManager.generateNextMessageID();
        messageId++;
        final Rpc rpc = new Rpc.RpcBuilder(new OperationTest()).build();
        Assert.assertEquals("Wrong message id", messageId, rpc.getMessageID());
        final TransportData td = rpc.getTransportData();
        final StringBuilder output = new StringBuilder();
        td.writeTo(output);

        Assert.assertEquals("Wrong rpc message", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + System.lineSeparator()
                + "<rpc message-id=\"" + messageId + "\"" + System.lineSeparator()
                + "\txmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">" + System.lineSeparator() + "<test/></rpc>"
                + System.lineSeparator() + "]]>]]>" + System.lineSeparator(), output.toString());
    }

    class OperationTest extends Operation {

        @Override
        public String getBody() {
            return "<test/>";
        }

    }

}

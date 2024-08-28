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

package com.ericsson.oss.mediation.util.netconf.operation;

import com.ericsson.oss.mediation.util.netconf.rpc.MessageIdManager;
import com.ericsson.oss.mediation.util.netconf.rpc.Rpc;
import java.util.Iterator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * 
 * @author xvaltda
 */
@RunWith(MockitoJUnitRunner.class)
public class CloseTest {

    private Rpc rpc;
    private Close close;

    @Test
    public void testCreateCloseOperation() {

        String close_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + System.lineSeparator() + "<rpc message-id=\""
                + (MessageIdManager.generateNextMessageID() + 1) + "\"" + System.lineSeparator()
                + "	xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">" + System.lineSeparator() + "<close-session/>\n"
                + "</rpc>" + System.lineSeparator() + "]]>]]>" + System.lineSeparator();

        close = new Close();

        rpc = new Rpc.RpcBuilder(close).build();
        Assert.assertTrue((close.getOperationType() == OperationType.CLOSE_SESSION));

        final Iterator<String> operationIterator = rpc.iterator();

        String content = "";

        while (operationIterator.hasNext()) {
            content += (String) operationIterator.next();
        }

        Assert.assertTrue(close_xml.equals(content));

    }

}

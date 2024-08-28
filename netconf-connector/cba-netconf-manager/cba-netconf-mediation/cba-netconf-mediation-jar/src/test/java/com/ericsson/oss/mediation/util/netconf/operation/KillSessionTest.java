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
package com.ericsson.oss.mediation.util.netconf.operation;

import com.ericsson.oss.mediation.util.netconf.rpc.MessageIdManager;
import com.ericsson.oss.mediation.util.netconf.rpc.Rpc;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.Assert;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class KillSessionTest {

    private KillSession killSession;

    @Test
    public void testCreateKillSessionOperation() {
        String sessionId = "12";

        List<String> xml_content = new ArrayList<String>();

        String killSession_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + System.lineSeparator()
                + "<rpc message-id=\"" + (MessageIdManager.generateNextMessageID() + 1) + "\"" + System.lineSeparator()
                + "	xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">" + System.lineSeparator() + "<kill-session>\n"
                + "<session-id>" + sessionId + "</session-id>\n" + "</kill-session>\n" + "</rpc>"
                + System.lineSeparator() + "]]>]]>" + System.lineSeparator();

        xml_content.add(killSession_xml);

        killSession = new KillSession(sessionId);
        Rpc rpc = new Rpc.RpcBuilder(killSession).build();

        Assert.assertTrue((killSession.getOperationType() == OperationType.KILL_SESSION));

        final Iterator<String> operationIterator = rpc.iterator();
        final Iterator expectedIterator = xml_content.iterator();

        String content = "";
        while (operationIterator.hasNext()) {
            content += (String) operationIterator.next();
        }
        assertTrue(killSession_xml.equals(content));

    }
}

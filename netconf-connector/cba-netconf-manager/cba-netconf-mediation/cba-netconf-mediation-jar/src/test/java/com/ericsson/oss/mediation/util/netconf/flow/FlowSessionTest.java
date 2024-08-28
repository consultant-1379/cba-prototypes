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

package com.ericsson.oss.mediation.util.netconf.flow;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * 
 * @author xvaltda
 */
public class FlowSessionTest {

    @Test
    public void testAddComponentInTheSessionFlow() {

        final FlowSession session = new FlowSession();
        session.put("key", "value");
        final String value = (String) session.get("key");

        assertTrue("value".equals(value));
    }

    @Test
    public void testCleanSession() {

        final FlowSession session = new FlowSession();
        session.put("key", "value");
        final String value = (String) session.get("key");

        assertTrue("value".equals(value));
        session.clean();
        assertTrue(session.get("key") == null);
    }
}

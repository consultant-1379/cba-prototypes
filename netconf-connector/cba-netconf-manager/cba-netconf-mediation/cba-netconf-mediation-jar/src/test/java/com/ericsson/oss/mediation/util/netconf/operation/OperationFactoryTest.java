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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.when;

import com.ericsson.oss.mediation.util.netconf.api.Capability;
import com.ericsson.oss.mediation.util.netconf.capability.NetconfSessionCapabilities;
import com.ericsson.oss.mediation.util.netconf.context.NetconfSession;
import com.ericsson.oss.mediation.util.netconf.rpc.MessageIdManager;

public class OperationFactoryTest {

    StringBuffer data = null;
    NetconfSession session = null;

    @Test
    public void testCreateHelloFactory() {

        List<Capability> ecimCapabilities = new ArrayList();
        NetconfSessionCapabilities sessionCapabilities = Mockito.mock(NetconfSessionCapabilities.class);

        session = Mockito.mock(NetconfSession.class);
        when(session.getNetconfSessionCapabilities()).thenReturn(sessionCapabilities);
        when(sessionCapabilities.getEcimCapabilities()).thenReturn(ecimCapabilities);

        Operation operation = OperationFactory.create(OperationType.HELLO, data, session);
        assertTrue(operation instanceof Hello);
    }

    @Test
    public void testCreateCloseFactory() {

        Operation operation = OperationFactory.create(OperationType.CLOSE_SESSION, data, session);
        assertTrue(operation instanceof Close);
    }

    @Test
    public void testCreateKillSessionFactory() {

        session = Mockito.mock(NetconfSession.class);
        when(session.getSessionId()).thenReturn("1");
        data = new StringBuffer();
        data.append("12");
        Operation operation = OperationFactory.create(OperationType.KILL_SESSION, data, session);
        assertTrue(operation instanceof KillSession);
    }
}

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

package com.ericsson.oss.mediation.util.netconf.parser;

import com.ericsson.oss.mediation.util.netconf.operation.OperationType;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author xvaltda
 */
public class ParserFactoryTest {

    @Test
    public void testCreateHelloParser() throws ParserException {

        NetconfParser parser = ParserFactory.create(OperationType.HELLO);
        assertTrue(parser instanceof HelloParser);
    }

    @Test
    public void testCreateCloseParser() throws ParserException {

        NetconfParser parser = ParserFactory.create(OperationType.CLOSE_SESSION);
        assertTrue(parser instanceof RpcReplyParser);
    }

    @Test
    public void testCreateKillSessionParser() throws ParserException {

        NetconfParser parser = ParserFactory.create(OperationType.KILL_SESSION);
        assertTrue(parser instanceof RpcReplyParser);
    }

}

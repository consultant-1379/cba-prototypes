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

import com.ericsson.oss.mediation.util.netconf.manger.NetconfTestConstants;
import com.ericsson.oss.mediation.util.netconf.vo.HelloVo;
import com.ericsson.oss.mediation.util.netconf.vo.NetconfVo;
import com.ericsson.oss.mediation.util.transport.api.TransportData;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * 
 * @author xvaltda
 */
public class HelloParserTest {

    private StringBuilder buffer;

    public HelloParserTest() {
        buffer = new StringBuilder();
        buffer.append(NetconfTestConstants.HELLO);
    }

    @Test
    public void testParseOperationThrowException() {
        try {
            buffer = new StringBuilder();
            buffer.append(NetconfTestConstants.BASE_CAPABILITY);

            TransportData transportData = new TransportData();
            transportData.setData(buffer);

            HelloParser helloParser = new HelloParser();
            NetconfVo netconfVo = helloParser.parse(transportData);

        } catch (ParserException ex) {
            assertTrue("Exception trying to parse the hello received from the node".equals(ex.getMessage()));
        }
    }

    @Test
    public void testParseOperationNotError() throws ParserException {

        TransportData transportData = new TransportData();
        transportData.setData(buffer);

        HelloParser helloParser = new HelloParser();
        NetconfVo netconfVo = helloParser.parse(transportData);

        assertFalse(netconfVo.isErrorReceived());
        assertTrue(netconfVo instanceof HelloVo);

        HelloVo helloVo = (HelloVo) netconfVo;

        assertTrue("1".equals(helloVo.getSessionId()));
    }

    @Test
    public void testParseOperationError() throws ParserException {

        buffer = new StringBuilder();
        buffer.append(NetconfTestConstants.HELLO_WITHOUT_SESSION_ID);

        TransportData transportData = new TransportData();
        transportData.setData(buffer);

        HelloParser helloParser = new HelloParser();
        NetconfVo netconfVo = helloParser.parse(transportData);

        assertTrue(netconfVo instanceof HelloVo);

        HelloVo helloVo = (HelloVo) netconfVo;
        assertFalse(helloVo.isErrorReceived());

        assertFalse("1".equals(helloVo.getSessionId()));
    }
}

/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
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
import com.ericsson.oss.mediation.util.netconf.vo.NetconfVo;
import com.ericsson.oss.mediation.util.netconf.vo.error.Error;
import com.ericsson.oss.mediation.util.netconf.vo.error.ErrorSeverity;
import com.ericsson.oss.mediation.util.netconf.vo.error.ErrorTag;
import com.ericsson.oss.mediation.util.netconf.vo.error.ErrorType;
import com.ericsson.oss.mediation.util.netconf.vo.error.RpcErrorVo;
import com.ericsson.oss.mediation.util.transport.api.TransportData;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * @author xdeevas
 * 
 */
public class ErrorParserTest {

    private StringBuilder buffer;
    SAXParser sp;
    RpcErrorParser errorParser;

    @Before
    public void setup() throws ParserConfigurationException, SAXException {
        errorParser = new RpcErrorParser() {

            @Override
            public NetconfVo parse(final TransportData data) throws ParserException {
                XMLReader parser;

                try {
                    parser = XMLReaderFactory.createXMLReader();
                    parser.setContentHandler(this);
                    parser.setErrorHandler(this);
                    parser.parse(new InputSource(new StringReader(data.getData().toString())));
                } catch (IOException | SAXException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                return rpcErrorVo;
            }
        };
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testParseError() throws IOException {
        try {
            buffer = getErrorInput("src/test/resources/netconfError.xml");

            final TransportData transportData = new TransportData();
            transportData.setData(buffer);

            errorParser.parse(transportData);
            final RpcErrorVo errorResponse = errorParser.getErrorResponse();
            final Error error = errorResponse.getErrors().get(0);
            Assert.assertEquals("Error Type is not correct", ErrorType.application, error.getErrortype());
            Assert.assertEquals("Error Tag is not correct", ErrorTag.INVALID_VALUE, error.getErrorTag());
            Assert.assertEquals("Error severity is not correct", ErrorSeverity.error, error.getErrorSeverity());
            Assert.assertEquals("Error path is not correct",
                    "/t:top/t:interface[t:name=\"Ethernet1/0\"]/t:address/t:name", error.getErrorPath().trim());
            Assert.assertEquals("Error message is not correct", "Invalid IP address for interface Ethernet1/0", error
                    .getErrorMessage().trim());
            Assert.assertNull("Error Info is not null", error.getErrorInfo());
            Assert.assertNull("Error App tag is not null", error.getErrorAppTag());
        } catch (final ParserException ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testParseError1() throws IOException {
        try {
            buffer = getErrorInput("src/test/resources/netconfError1.xml");

            final TransportData transportData = new TransportData();
            transportData.setData(buffer);

            errorParser.parse(transportData);
            final RpcErrorVo errorResponse = errorParser.getErrorResponse();
            final Error error = errorResponse.getErrors().get(0);
            Assert.assertEquals("Error Type is not correct", ErrorType.rpc, error.getErrortype());
            Assert.assertEquals("Error Tag is not correct", ErrorTag.MISSING_ATTRIBUTE, error.getErrorTag());
            Assert.assertEquals("Error severity is not correct", ErrorSeverity.error, error.getErrorSeverity());
            Assert.assertNotNull("Error Info is null", error.getErrorInfo());
            Assert.assertEquals("Error Info content is  not correct", "bad-attribute: message-id", error.getErrorInfo()
                    .get(0));
            Assert.assertEquals("Error Info content is  not correct", "bad-element: rpc", error.getErrorInfo().get(1));
            Assert.assertEquals("Error message is not empty","", error.getErrorMessage());
            Assert.assertNull("Error path is not null", error.getErrorPath());
            Assert.assertNull("Error App tag is not null", error.getErrorAppTag());
        } catch (final ParserException ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testParseError2() throws IOException {
        try {
            buffer = getErrorInput("src/test/resources/netconfError2.xml");

            final TransportData transportData = new TransportData();
            transportData.setData(buffer);

            errorParser.parse(transportData);
            final RpcErrorVo errorResponse = errorParser.getErrorResponse();
            final List<Error> errors = errorResponse.getErrors();
            final Error error1 = errors.get(0);
            Assert.assertEquals("Error Type is not correct", ErrorType.application, error1.getErrortype());
            Assert.assertEquals("Error Tag is not correct", ErrorTag.INVALID_VALUE, error1.getErrorTag());
            Assert.assertEquals("Error severity is not correct", ErrorSeverity.error, error1.getErrorSeverity());
            Assert.assertNotNull("Error Info is null", error1.getErrorInfo());
            Assert.assertEquals("Error message is not correct", "MTU value 25000 is not within range 256..9192", error1
                    .getErrorMessage().trim());
            Assert.assertNull("Error path is not null", error1.getErrorPath());
            Assert.assertNull("Error App tag is not null", error1.getErrorAppTag());
            final Error error2 = errors.get(1);
            Assert.assertNotNull("Error 2 is null", error2);
            Assert.assertEquals("Error Type is not correct", ErrorType.application, error2.getErrortype());
            Assert.assertEquals("Error Tag is not correct", ErrorTag.INVALID_VALUE, error2.getErrorTag());
            Assert.assertEquals("Error severity is not correct", ErrorSeverity.error, error2.getErrorSeverity());
            Assert.assertNotNull("Error Info is null", error2.getErrorInfo());
            Assert.assertEquals("Error message is not correct", "Invalid IP address for interface Ethernet1/0", error2
                    .getErrorMessage().trim());
            Assert.assertNull("Error path is not null", error2.getErrorPath());
            Assert.assertNull("Error App tag is not null", error2.getErrorAppTag());
        } catch (final ParserException ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testParseNoError() throws IOException {
        try {
            buffer = new StringBuilder();
            buffer.append(NetconfTestConstants.HELLO);

            final TransportData transportData = new TransportData();
            transportData.setData(buffer);

            final NetconfVo errorResponse = errorParser.parse(transportData);
            Assert.assertEquals("error is not expected", false, errorParser.isError);
            Assert.assertNull("error response is not null", errorResponse);
        } catch (final ParserException ex) {
            ex.printStackTrace();
        }
    }

    public StringBuilder getErrorInput(final String path) throws IOException {
        BufferedReader reader = null;
        final StringBuilder buffer = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(path));

            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } finally {
            reader.close();
        }
        return buffer;
    }
}

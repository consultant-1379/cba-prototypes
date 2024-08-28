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
package com.ericsson.oss.mediation.util.netconf.capability;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import com.ericsson.oss.mediation.util.netconf.api.Capability;

/**
 * 
 * @author xvaltda
 */
public class URLCapabilityParserTest {

    @Test
    public void testParserCorrectFormat() throws CapabilityFormatException {

        final CapabilityParser parser = CapabilityParserFactory.getInstance().create(CapabilityProtocolType.URL);
        final Capability capability = parser.parse("http://www.ericsson.com/gsn/1.0/protocolVersion");

        Assert.assertEquals("protocolVersion", capability.getName());
        Assert.assertEquals("www.ericsson.com/gsn", capability.getNamespace());
        Assert.assertEquals("http://www.ericsson.com/gsn/1.0/protocolVersion", capability.getURN());
        Assert.assertEquals("1.0", capability.getVersion());

    }

    @Test
    public void testParserBadFormat() {
        CapabilityParser parser;
        try {

            parser = CapabilityParserFactory.getInstance().create(CapabilityProtocolType.URL);
            parser.parse("bad-format.");

        } catch (final CapabilityFormatException ex) {
            assertTrue("Error in trying to parse url capability, bad format -1".equals(ex.getMessage()));
        }

    }
}

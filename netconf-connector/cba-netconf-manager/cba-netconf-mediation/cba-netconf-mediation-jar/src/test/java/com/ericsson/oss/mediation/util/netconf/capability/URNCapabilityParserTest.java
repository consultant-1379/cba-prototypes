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
public class URNCapabilityParserTest {

    @Test
    public void testParserWithStandardCapability() throws CapabilityFormatException {

        final CapabilityParser parser = CapabilityParserFactory.getInstance().create(CapabilityProtocolType.URN);
        final Capability capability = parser.parse("urn:ietf:params:xml:ns:netconf:base:1.0");

        Assert.assertEquals("base", capability.getName());
        Assert.assertEquals("ietf:params:xml:ns:netconf", capability.getNamespace());
        Assert.assertEquals("urn:ietf:params:xml:ns:netconf:base:1.0", capability.getURN());
        Assert.assertEquals("1.0", capability.getVersion());

    }

    @Test
    public void testParserWithPropreotoryCapability() throws CapabilityFormatException {

        final CapabilityParser parser = CapabilityParserFactory.getInstance().create(CapabilityProtocolType.URN);
        final Capability capability = parser.parse("urn:com:ericsson:ebase:1.1.0");

        Assert.assertEquals("ebase", capability.getName());
        Assert.assertEquals("com:ericsson", capability.getNamespace());
        Assert.assertEquals("urn:com:ericsson:ebase:1.1.0", capability.getURN());
        Assert.assertEquals("1.1.0", capability.getVersion());

    }

    @Test
    public void testParserBadFormat() {

        CapabilityParser parser;
        try {

            parser = CapabilityParserFactory.getInstance().create(CapabilityProtocolType.URN);
            parser.parse("bad-format.");

        } catch (final CapabilityFormatException ex) {
            assertEquals("Error in trying to parse urn capability, bad format -1", ex.getMessage());
        }

    }
}

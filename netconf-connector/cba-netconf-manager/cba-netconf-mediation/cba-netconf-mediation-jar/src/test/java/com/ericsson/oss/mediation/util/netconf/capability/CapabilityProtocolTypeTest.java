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

import org.junit.Test;

/**
 * 
 * @author xshakku
 * 
 */
public class CapabilityProtocolTypeTest {

    @Test
    public void testBadProtocol() throws CapabilityFormatException {
        try {
            CapabilityProtocolType.fromProtocol("abcd");
            fail("Should through unsupported protocol type exception.");
        } catch (final CapabilityFormatException e) {
            assertEquals("Unsupported protocol type [abcd] for capability.", e.getMessage());
        }
    }

    @Test
    public void testURNProtocol() throws CapabilityFormatException {
        final CapabilityProtocolType protocolType = CapabilityProtocolType.fromProtocol("urn");
        assertEquals(CapabilityProtocolType.URN, protocolType);
    }

    @Test
    public void testHTTPURLProtocol() throws CapabilityFormatException {
        final CapabilityProtocolType protocolType = CapabilityProtocolType.fromProtocol("http");
        assertEquals(CapabilityProtocolType.URL, protocolType);
    }

    @Test
    public void testHTTPsURLProtocol() throws CapabilityFormatException {
        final CapabilityProtocolType protocolType = CapabilityProtocolType.fromProtocol("https");
        assertEquals(CapabilityProtocolType.URL, protocolType);
    }
}

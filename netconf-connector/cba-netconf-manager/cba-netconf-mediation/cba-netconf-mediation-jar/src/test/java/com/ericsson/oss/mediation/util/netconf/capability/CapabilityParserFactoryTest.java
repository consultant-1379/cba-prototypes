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
 * @author xvaltda
 */
public class CapabilityParserFactoryTest {

    @Test
    public void testCreateStandardCapabilityParser() throws CapabilityFormatException {
        final CapabilityParser parser = CapabilityParserFactory.getInstance().create(CapabilityProtocolType.URN);

        assertTrue(parser instanceof URNCapabilityParser);
    }

    @Test
    public void testCreateProperietaryCapabilityParser() throws CapabilityFormatException {
        final CapabilityParser parser = CapabilityParserFactory.getInstance().create(CapabilityProtocolType.URL);

        assertTrue(parser instanceof URLCapabilityParser);
    }
}

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

import com.ericsson.oss.mediation.util.netconf.api.Capability;

/**
 * 
 * @author xshakku
 * 
 */
public class URNCapabilityParser implements CapabilityParser {
    @Override
    public Capability parse(final String stringifiedCapability) throws CapabilityFormatException {
        try {
            final String[] tokens = stringifiedCapability.split(":");
            final int length = tokens.length;
            final String version = tokens[length - 1];
            final String name = tokens[length - 2];
            final String namespace = stringifiedCapability.substring("urn:".length(),
                    stringifiedCapability.indexOf(name) - 1);
            final Capability capability = new Capability(stringifiedCapability, name, namespace, version);
            return capability;
        } catch (final Exception e) {
            throw new CapabilityFormatException("Error in trying to parse urn" + " capability, bad format "
                    + e.getMessage());
        }

    }
}

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

/**
 * 
 * @author xshakku
 * 
 */
public class CapabilityParserFactory {
    private static CapabilityParserFactory instance;

    private final URNCapabilityParser urnCapabilityParser;
    private final URLCapabilityParser urlCapabilityParser;

    public static CapabilityParserFactory getInstance() {
        if (instance == null) {
            instance = new CapabilityParserFactory();
        }
        return instance;
    }

    private CapabilityParserFactory() {
        urnCapabilityParser = new URNCapabilityParser();
        urlCapabilityParser = new URLCapabilityParser();
    }

    public CapabilityParser create(final CapabilityProtocolType capabilityType) throws CapabilityFormatException {
        switch (capabilityType) {
        case URN:
            return urnCapabilityParser;
        case URL:
            return urlCapabilityParser;
        default:
            throw new CapabilityFormatException("Unsupported capability type [" + capabilityType + "]");
        }
    }
}

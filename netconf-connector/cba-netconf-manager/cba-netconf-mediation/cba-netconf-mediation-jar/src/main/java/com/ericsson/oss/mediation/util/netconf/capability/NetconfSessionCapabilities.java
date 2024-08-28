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
package com.ericsson.oss.mediation.util.netconf.capability;

import com.ericsson.oss.mediation.util.netconf.api.Capability;
import com.ericsson.oss.mediation.util.netconf.api.NetconManagerConstants;
import com.ericsson.oss.mediation.util.netconf.api.exception.NetconfManagerErrorMessages;
import com.ericsson.oss.mediation.util.netconf.api.exception.NetconfManagerException;
import com.ericsson.oss.mediation.util.netconf.vo.HelloVo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author xvaltda
 * @author xshakku
 * 
 */
public class NetconfSessionCapabilities {

    private static final Logger logger = LoggerFactory.getLogger(NetconfSessionCapabilities.class);
    private List<Capability> serverCapabilities;
    private final List<Capability> ecimCapabilities;
    private Collection<Capability> activeCapabilities;

    @SuppressWarnings("unchecked")
    public NetconfSessionCapabilities(final Map<String, Object> configProperties) throws NetconfManagerException {
        if ((configProperties == null) || !configProperties.containsKey(NetconManagerConstants.CAPABILITIES_KEY)) {
            throw new NetconfManagerException("No supported capabilities found in configurations.");
        }
        final List<String> supportedCapabilities = (List<String>) configProperties
                .get(NetconManagerConstants.CAPABILITIES_KEY);
        logger.info("supported capabilities received in config properties : " + supportedCapabilities);
        ecimCapabilities = getEcimCapabilities(supportedCapabilities);
    }

    private List<Capability> getEcimCapabilities(final List<String> supportedCapabilities) {
        final List<Capability> capabilities = new ArrayList<Capability>();
        for (final String capability : supportedCapabilities) {
            try {
                final String protocol = capability.split(":")[0];
                CapabilityProtocolType capabilityType;
                capabilityType = CapabilityProtocolType.fromProtocol(protocol);
                final CapabilityParser parser = CapabilityParserFactory.getInstance().create(capabilityType);
                capabilities.add(parser.parse(capability));
            } catch (final Exception e) {
                logger.error("Exception in parsing supported capability : " + e.getMessage()
                        + ". So, skipping this capability.");
            }
        }
        return capabilities;
    }

    public List<Capability> getServerCapabilities() {
        return serverCapabilities;
    }

    public List<Capability> getEcimCapabilities() {
        return ecimCapabilities;
    }

    public Collection<Capability> getActiveCapabilities() {
        if (activeCapabilities == null) {
            return Collections.emptyList();
        }
        return activeCapabilities;
    }

    public List<Capability> getActiveCapabilityByName(final String capabilityName) {
        final List<Capability> capabilities = new ArrayList<>();
        if (activeCapabilities == null) {
            return capabilities;
        }
        for (final Capability capability : activeCapabilities) {
            if (capability.getName().equals(capabilityName)) {
                capabilities.add(capability);
            }
        }
        return capabilities;
    }

    public void processCapabilities(final HelloVo hello) throws NetconfManagerException {
        logger.info("process Capabilities");
        serverCapabilities = hello.getCapabilities();
        if ((serverCapabilities == null) || (serverCapabilities.size() == 0)) {
            throw new NetconfManagerException(NetconfManagerErrorMessages.NO_CAPABILITIES_RECIEVED);
        }
        final Map<String, Capability> activeCapabilitiesMap = new HashMap<String, Capability>();
        for (final Capability serverCapability : serverCapabilities) {
            if (!ecimCapabilities.contains(serverCapability)) {
                continue;
            }

            final Capability existingCapability = activeCapabilitiesMap.get(serverCapability.getName()
                    + serverCapability.getNamespace());
            if (existingCapability != null) {
                final Capability capabilityWithLatestVersion = getCapabilityWithLatestVersion(existingCapability,
                        serverCapability);
                activeCapabilitiesMap.put(
                        capabilityWithLatestVersion.getName() + capabilityWithLatestVersion.getNamespace(),
                        capabilityWithLatestVersion);
            } else {
                activeCapabilitiesMap.put(serverCapability.getName() + serverCapability.getNamespace(),
                        serverCapability);
            }
        }
        activeCapabilities = activeCapabilitiesMap.values();
        logger.info("Node Capabilities: " + serverCapabilities);
        logger.info("ECIM Capabilities: " + ecimCapabilities);
        logger.info("Active Capabilities: " + activeCapabilities);
    }

    Capability getCapabilityWithLatestVersion(final Capability capability1, final Capability capability2) {
        if (compareVersion(capability1.getVersion(), capability2.getVersion()) > 0) {
            return capability1;
        }
        return capability2;
    }

    int compareVersion(final String version1, final String version2) {
        final String[] tokens1 = version1.trim().split("\\.");
        final String[] tokens2 = version2.trim().split("\\.");
        final int min = Math.min(tokens1.length, tokens2.length);
        for (int i = 0; i < min; i++) {
            final int val1 = Integer.parseInt(tokens1[i]);
            final int val2 = Integer.parseInt(tokens2[i]);
            final int result = val1 - val2;
            if (result != 0) {
                return result;
            }
        }
        return tokens1.length - tokens2.length;
    }
}

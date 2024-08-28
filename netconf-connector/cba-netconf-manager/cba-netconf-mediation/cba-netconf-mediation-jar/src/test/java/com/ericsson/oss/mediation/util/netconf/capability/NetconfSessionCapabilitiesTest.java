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
import com.ericsson.oss.mediation.util.netconf.api.NetconManagerConstants;
import com.ericsson.oss.mediation.util.netconf.api.exception.NetconfManagerException;
import com.ericsson.oss.mediation.util.netconf.vo.HelloVo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author xshakku
 * 
 */
public class NetconfSessionCapabilitiesTest {
    private static final String BASE_CAPABILITY_COM = "urn:ietf:params:netconf:base:1.0";
    private static final String BASE_CAPABILITY_SGSN = "urn:ietf:params:xml:ns:netconf:base:1.0";
    public NetconfSessionCapabilities netconfSessionCapabilities;
    private List<Capability> serverCapabilities;

    public NetconfSessionCapabilitiesTest() throws NetconfManagerException {
        final Map<String, Object> configProperties = new HashMap<String, Object>();
        configProperties.put(NetconManagerConstants.CAPABILITIES_KEY, Arrays.asList(BASE_CAPABILITY_COM,
                BASE_CAPABILITY_SGSN, Capability.CANDIDATE + ":1.0", Capability.CONFIRMED_COMMIT + ":2.0",
                Capability.CONFIRMED_COMMIT + ":1.0"));
        netconfSessionCapabilities = new NetconfSessionCapabilities(configProperties);
        serverCapabilities = new ArrayList<Capability>();
        serverCapabilities.add(Capability.BASE);
    }

    @Test
    public void testCompareVersion_100_101() {
        final int result = netconfSessionCapabilities.compareVersion("1.0.0", "1.0.1");
        Assert.assertEquals("1.0.0 should be smaller than 1.0.1", -1, result);
    }

    @Test
    public void testCompareVersion_210_2101() {
        final int result = netconfSessionCapabilities.compareVersion("2.10", "2.10.1");
        Assert.assertEquals("2.10 should be smaller than 2.10.1", -1, result);
    }

    @Test
    public void testCompareVersion_32_321() {
        final int result = netconfSessionCapabilities.compareVersion("3.2", "3.2.1");
        Assert.assertEquals("3.2 should be smaller than 3.2.1", -1, result);
    }

    @Test
    public void testCompareVersion_101_100() {
        final int result = netconfSessionCapabilities.compareVersion("1.0.1", "1.0.0");
        Assert.assertEquals("1.0.1 should be greater than 1.0.0", 1, result);
    }

    @Test
    public void testCompareVersion_2101_210() {
        final int result = netconfSessionCapabilities.compareVersion("2.10.1", "2.10");
        Assert.assertEquals("2.10.1 should be greater than 2.10", 1, result);
    }

    @Test
    public void testCompareVersion_13_12() {
        final int result = netconfSessionCapabilities.compareVersion("13", "12");
        Assert.assertEquals("13 should be greater than 12", 1, result);
    }

    @Test
    public void testCompareVersion_1219_1218() {
        final int result = netconfSessionCapabilities.compareVersion("12.1.9", "12.1.8");
        Assert.assertEquals("12.1.9 should be greater than 12.1.8", 1, result);
    }

    @Test
    public void testCompareVersion_100_100() {
        final int result = netconfSessionCapabilities.compareVersion("1.0.0", "1.0.0");
        Assert.assertEquals("1.0.0 should be equal to 1.0.0", 0, result);
    }

    @Test
    public void testCompareVersion_123_123() {
        final int result = netconfSessionCapabilities.compareVersion("1.2.3", "1.2.3");
        Assert.assertEquals("1.2.3 should be equal to 1.2.3", 0, result);
    }

    @Test
    public void testCompareVersion_1_1() {
        final int result = netconfSessionCapabilities.compareVersion("1", "1");
        Assert.assertEquals("1 should be equal to 1", 0, result);
    }

    @Test
    public void testCompareVersion_12023_12023() {
        final int result = netconfSessionCapabilities.compareVersion("12.0.2.3", "12.0.2.3");
        Assert.assertEquals("12.0.2.3 should be equal to 12.0.2.3", 0, result);
    }

    @Test
    public void testGetCapabilityWithLatestVersion_base10_base11() {
        final Capability capability1 = new Capability("urn:ietf:params:netconf:base:1.0", "base",
                "ietf:params:netconf", "1.0");
        final Capability capability2 = new Capability("urn:ietf:params:netconf:base:1.1", "base",
                "ietf:params:netconf", "1.1");
        final Capability result = netconfSessionCapabilities.getCapabilityWithLatestVersion(capability1, capability2);
        Assert.assertEquals("base:1.0 should not be latest than base:1.1", capability2, result);
    }

    @Test
    public void testGetCapabilityWithLatestVersion_base20_base10() {
        final Capability capability1 = new Capability("urn:ietf:params:netconf:base:2.0", "base",
                "ietf:params:netconf", "2.0");
        final Capability capability2 = new Capability("urn:ietf:params:netconf:base:1.0", "base",
                "ietf:params:netconf", "1.0");
        final Capability result = netconfSessionCapabilities.getCapabilityWithLatestVersion(capability1, capability2);
        Assert.assertEquals("base:2.0 should be latest than base:1.0", capability1, result);
    }

    @Test
    public void testProcessCapabilities_OneMatch() throws NetconfManagerException {
        final HelloVo hello = new HelloVo();
        hello.setCapabilities(serverCapabilities);

        netconfSessionCapabilities.processCapabilities(hello);
        Assert.assertEquals("Active capabilities size does not matched.", 1, netconfSessionCapabilities
                .getActiveCapabilities().size());
        final List<Capability> capabilities = netconfSessionCapabilities.getActiveCapabilityByName("base");
        Assert.assertEquals("More than one base capabiltity found.", 1, capabilities.size());
        Assert.assertEquals("Capability name is not matching.", "base", capabilities.get(0).getName());
        Assert.assertEquals("URN not matching", BASE_CAPABILITY_COM, capabilities.get(0).getURN());
        Assert.assertEquals("Capability version not matching", "1.0", capabilities.get(0).getVersion());
    }

    @Test
    public void testProcessCapabilities_NoMatch() throws NetconfManagerException {
        final HelloVo hello = new HelloVo();
        hello.setCapabilities(serverCapabilities);
        hello.setCapabilities(Arrays.asList(new Capability[] { new Capability(Capability.CANDIDATE + ":2.0",
                "candidate", "ietf:params:xml:ns:netconf:capability", "2.0") }));
        netconfSessionCapabilities.processCapabilities(hello);
        Assert.assertEquals("Active capabilities size does not matched.", 0, netconfSessionCapabilities
                .getActiveCapabilities().size());
    }

    @Test
    public void testProcessCapabilities_TwoMatches() throws NetconfManagerException {
        final HelloVo hello = new HelloVo();
        hello.setCapabilities(serverCapabilities);
        hello.getCapabilities().add(
                new Capability(Capability.CANDIDATE + ":1.0", "candidate", "ietf:params:xml:ns:netconf:capability",
                        "1.0"));

        netconfSessionCapabilities.processCapabilities(hello);
        Assert.assertEquals("Active capabilities size does not matched.", 2, netconfSessionCapabilities
                .getActiveCapabilities().size());
        List<Capability> capabilities = netconfSessionCapabilities.getActiveCapabilityByName("base");
        Assert.assertEquals("More than one base capabiltity found.", 1, capabilities.size());
        Assert.assertEquals("Capability name is not matching.", "base", capabilities.get(0).getName());
        Assert.assertEquals("URN not matching", BASE_CAPABILITY_COM, capabilities.get(0).getURN());
        Assert.assertEquals("Capability version not matching", "1.0", capabilities.get(0).getVersion());
        capabilities = netconfSessionCapabilities.getActiveCapabilityByName("candidate");
        Assert.assertEquals("More than one candidate capabiltity found.", 1, capabilities.size());
        Assert.assertEquals("Capability name is not matching.", "candidate", capabilities.get(0).getName());
        Assert.assertEquals("URN not matching", Capability.CANDIDATE + ":1.0", capabilities.get(0).getURN());
        Assert.assertEquals("Capability version not matching", "1.0", capabilities.get(0).getVersion());
    }

    @Test
    public void testProcessCapabilities_ThreeMatchesWithLatestVersion() throws NetconfManagerException {
        final HelloVo hello = new HelloVo();
        hello.setCapabilities(serverCapabilities);
        hello.getCapabilities().add(
                new Capability(Capability.CANDIDATE + ":1.0", "candidate", "ietf:params:xml:ns:netconf:capability",
                        "1.0"));
        hello.getCapabilities().add(
                new Capability(Capability.CONFIRMED_COMMIT + ":1.0", "confirmed-commit",
                        "ietf:params:xml:ns:netconf:capability", "1.0"));
        hello.getCapabilities().add(
                new Capability(Capability.CONFIRMED_COMMIT + ":2.0", "confirmed-commit",
                        "ietf:params:xml:ns:netconf:capability", "2.0"));

        netconfSessionCapabilities.processCapabilities(hello);
        Assert.assertEquals("Active capabilities size does not matched.", 3, netconfSessionCapabilities
                .getActiveCapabilities().size());
        List<Capability> capabilities = netconfSessionCapabilities.getActiveCapabilityByName("base");
        Assert.assertEquals("More than one base capabiltity found.", 1, capabilities.size());
        Capability capability = capabilities.get(0);
        Assert.assertEquals("Capability name is not matching.", "base", capability.getName());
        Assert.assertEquals("URN not matching", BASE_CAPABILITY_COM, capability.getURN());
        Assert.assertEquals("Capability version not matching", "1.0", capability.getVersion());

        capabilities = netconfSessionCapabilities.getActiveCapabilityByName("candidate");
        Assert.assertEquals("More than one candidate capabiltity found.", 1, capabilities.size());
        capability = capabilities.get(0);
        Assert.assertEquals("Capability name is not matching.", "candidate", capability.getName());
        Assert.assertEquals("URN not matching", Capability.CANDIDATE + ":1.0", capability.getURN());
        Assert.assertEquals("Capability version not matching", "1.0", capability.getVersion());

        capabilities = netconfSessionCapabilities.getActiveCapabilityByName("confirmed-commit");
        Assert.assertEquals("More than one confirmed-commit capabiltity found.", 1, capabilities.size());
        capability = capabilities.get(0);
        Assert.assertEquals("Capability name is not matching.", "confirmed-commit", capability.getName());
        Assert.assertEquals("URN not matching", Capability.CONFIRMED_COMMIT + ":2.0", capability.getURN());
        Assert.assertEquals("Capability version not matching", "2.0", capability.getVersion());
    }

    @Test
    public void testProcessCapabilities_BaseWithDifferentNamespace() throws NetconfManagerException {
        final HelloVo hello = new HelloVo();
        hello.setCapabilities(serverCapabilities);
        hello.getCapabilities().add(new Capability(BASE_CAPABILITY_SGSN, "base", "ietf:params:xml:ns:netconf", "1.0"));

        netconfSessionCapabilities.processCapabilities(hello);
        Assert.assertEquals("Active capabilities size does not matched.", 2, netconfSessionCapabilities
                .getActiveCapabilities().size());
        final List<Capability> capabilities = netconfSessionCapabilities.getActiveCapabilityByName("base");
        Assert.assertEquals("Two base capabilities was expected.", 2, capabilities.size());
        final Capability capability1 = capabilities.get(0);
        final Capability capability2 = capabilities.get(1);

        Assert.assertEquals("Capability name is not matching.", "base", capability1.getName());
        Assert.assertEquals("Capability version not matching", "1.0", capability1.getVersion());

        Assert.assertEquals("Capability name is not matching.", "base", capability2.getName());
        Assert.assertEquals("Capability version not matching", "1.0", capability2.getVersion());
        if (capability1.getNamespace().equals("ietf:params:netconf")) {
            Assert.assertEquals("URN not matching", BASE_CAPABILITY_COM, capability1.getURN());
            Assert.assertEquals("URN not matching", BASE_CAPABILITY_SGSN, capability2.getURN());
            Assert.assertEquals("Capability namespace not matching", "ietf:params:xml:ns:netconf",
                    capability2.getNamespace());
        } else if (capability1.getNamespace().equals("ietf:params:xml:ns:netconf")) {
            Assert.assertEquals("URN not matching", BASE_CAPABILITY_SGSN, capability1.getURN());
            Assert.assertEquals("URN not matching", BASE_CAPABILITY_COM, capability2.getURN());
            Assert.assertEquals("Capability namespace not matching", "ietf:params:netconf", capability2.getNamespace());

        }

    }

    @Test(expected = NetconfManagerException.class)
    public void testProcessCapabilities_NoServerCapability() throws NetconfManagerException {
        final HelloVo hello = new HelloVo();
        hello.setCapabilities(serverCapabilities);
        hello.getCapabilities().clear();
        netconfSessionCapabilities.processCapabilities(hello);
        Assert.fail("Should throw exception as no capability recived from server.");
    }

    @Test
    public void testWithNullConfiguration() {
        try {
            new NetconfSessionCapabilities(null);
            Assert.fail("Should throw exception as no configuration defined.");
        } catch (final NetconfManagerException e) {
            Assert.assertEquals("Exception message not matching", "No supported capabilities found in configurations.",
                    e.getMessage());
        }

    }

    @Test
    public void testWithEmptyConfiguration() {
        try {
            new NetconfSessionCapabilities(new HashMap<String, Object>());
            Assert.fail("Should throw exception as no configuration defined.");
        } catch (final NetconfManagerException e) {
            Assert.assertEquals("Exception message not matching", "No supported capabilities found in configurations.",
                    e.getMessage());
        }

    }
}

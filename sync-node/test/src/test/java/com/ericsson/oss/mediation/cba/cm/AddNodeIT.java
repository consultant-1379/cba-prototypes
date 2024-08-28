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
package com.ericsson.oss.mediation.cba.cm;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.spec.se.manifest.ManifestDescriptor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.cba.cm.deployment.*;
import com.ericsson.oss.mediation.cba.cm.test.util.TestUtil;

@RunWith(Arquillian.class)
public class AddNodeIT {

    public static final Logger LOGGER = LoggerFactory.getLogger(AddNodeIT.class);

    private static final String ID_PIB = "PIB";
    private static final String ID_MOCK = "MockEar";

    @ArquillianResource
    private Deployer deployer;

    @EJB
    private TestUtil testUtil;

    @Deployment(name = ID_PIB, managed = true, testable = false)
    public static EnterpriseArchive createPIBDeployment() {
        LOGGER.debug("******Creating PIB deployment and deploying it to a server******");
        return Deployments.createSimpleEnterpriseArchive(Artifact.COM_ERICSSON_OSS_ITPF_PIB);
    }

    @Deployment(name = "camelEngineEar", testable = false, managed = false)
    public static EnterpriseArchive createCamelEngineEar() {
        return Deployments.createSimpleEnterpriseArchive(Artifact.ORG_JBOSS_AS_CAMEL_EAR);
    }

    @Deployment(name = "mediationCoreEar", testable = false, managed = false)
    public static EnterpriseArchive createMediationCore() {
        return Deployments.createSimpleEnterpriseArchive(Artifact.COM_ERICSSON_OSS_MEDIATION_MEDIATIONCORE_EAR);
    }

    @Deployment(name = "eaiHandlerEar", testable = false, managed = false)
    public static EnterpriseArchive createEAIHandlerEar() {
        return Deployments.createSimpleEnterpriseArchive(Artifact.EAI_HANDLER_EAR);
    }

    @Deployment(name = "ciAssociationHandlerEar", testable = false, managed = false)
    public static EnterpriseArchive createCiAssociationHandlerEar() {
        return Deployments.createSimpleEnterpriseArchive(Artifact.CI_ASSOCIATION_HANDLER_EAR);
    }

    @Deployment(name = "netconfHandlerEar", testable = false, managed = false)
    public static EnterpriseArchive createNetconfHandlerEar() {
        return Deployments.createSimpleEnterpriseArchive(Artifact.NETCONF_HANDLER_EAR);
    }

    @Deployment(name = "netconfConnectHandlerEar", testable = false, managed = false)
    public static EnterpriseArchive createNetconfConnectHandlerEar() {
        return Deployments.createSimpleEnterpriseArchive(Artifact.NETCONF_CONNECT_HANDLER_EAR);
    }

    @Deployment(name = "netconfDisconnectHandlerEar", testable = false, managed = false)
    public static EnterpriseArchive createNetconfDisconnectHandlerEar() {
        return Deployments.createSimpleEnterpriseArchive(Artifact.NETCONF_DISCONNECT_HANDLER_EAR);
    }

    @Deployment(name = "ossPrefixHandlerEar", testable = false, managed = false)
    public static EnterpriseArchive createOssPrefixHandlerEar() {
        return Deployments.createSimpleEnterpriseArchive(Artifact.OSS_PREFIX_HANDLER_EAR);
    }

    @Deployment(name = "inboundDpsHandlerEar", testable = false, managed = false)
    public static EnterpriseArchive createInboundDpsHandlerEar() {
        return Deployments.createSimpleEnterpriseArchive(Artifact.INBOUND_DPS_HANDLER_EAR);
    }

    @Deployment(name = "prototypeEar", testable = false, managed = false)
    public static EnterpriseArchive createPrototypeEar() {
        return Deployments.createSimpleEnterpriseArchive(Artifact.PROTOTYPE_HANDLER_EAR);
    }

    @Deployment(name = "mediationServiceEar", testable = false, managed = false)
    public static EnterpriseArchive createMediationService() {
        return Deployments.createSimpleEnterpriseArchive(Artifact.COM_ERICSSON_NMS_MEDIATION_MEDIATIONSERVICE_EAR);
    }

    @Deployment(name = ID_MOCK, testable = true, managed = false)
    public static EnterpriseArchive createMocksDeployment() throws IOException, InterruptedException {
        final EnterpriseArchive testableEar = Deployments.createCustomEar("Testable", new Class[] { TestUtil.class },
                new Class[] { AddNodeIT.class, TestUtil.class }, new String[] { Artifact.COM_ERICSSON_SFWK_DIST });
        testableEar.setManifest(new StringAsset(Descriptors.create(ManifestDescriptor.class)
                .attribute("Dependencies", "com.ericsson.oss.itpf.datalayer.dps.api export").exportAsString()));
        return testableEar;
    }

    @Test
    @OperateOnDeployment(ID_PIB)
    @RunAsClient
    @InSequence(1)
    public void testDeployPIB(final @ArquillianResource URL bUrl) throws IOException {
        changeConfigParameters(bUrl, "med_service_protocol_info", "CM");
        deployer.deploy("camelEngineEar");
        deployer.deploy("mediationCoreEar");
        deployer.deploy("eaiHandlerEar");
        deployer.deploy("ciAssociationHandlerEar");
        deployer.deploy("netconfHandlerEar");
        deployer.deploy("netconfConnectHandlerEar");
        deployer.deploy("netconfDisconnectHandlerEar");
        deployer.deploy("ossPrefixHandlerEar");
        deployer.deploy("inboundDpsHandlerEar");
        deployer.deploy("prototypeEar");
        deployer.deploy("mediationServiceEar");
        deployer.deploy(ID_MOCK);
    }

    /* ************************************************************
     * TESTS
     * 
     * *************************************************************
     */

    @Test
    @OperateOnDeployment(ID_MOCK)
    @InSequence(2)
    public void testStartSync() {
        testUtil.addMeContext();
        testUtil.addNetworkElement();
        testUtil.addManagedElement();
        testUtil.addConnectivityInfo();
        testUtil.addCmFunction();
        testUtil.sync();
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            LOGGER.warn("Test was interrupted", e);
        }
    }

    private void changeConfigParameters(final URL bUrl, final String paramName, final String newValue)
            throws IOException {
        final URL url = new URL(generateRestURL(bUrl, paramName, newValue));
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        LOGGER.debug("URL for changing service type {}", url);
        conn.setRequestMethod("GET");
        final int code = conn.getResponseCode();
        Assert.assertEquals("Change of mediation service type failed", 200, code);
    }

    private String generateRestURL(final URL bUrl, final String paramName, final String paramValue) {
        return String.valueOf(bUrl) + "configurationService/addConfigParameter?paramName=" + paramName + "&paramValue="
                + paramValue
                + "&serviceIdentifier=mediationservice&paramType=String&paramScopeType=JVM_AND_SERVICE&jvmIdentifier="
                + System.getProperty("cba.node.name");
    }

}

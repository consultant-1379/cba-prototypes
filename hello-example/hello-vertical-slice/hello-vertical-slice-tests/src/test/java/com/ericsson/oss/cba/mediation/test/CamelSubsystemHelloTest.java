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
package com.ericsson.oss.cba.mediation.test;

import java.io.IOException;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.cba.mediation.async.AsyncLatch;
import com.ericsson.oss.cba.mediation.deployment.*;
import com.ericsson.oss.cba.mediation.mocks.service.registration.RegistrationEventSender;
import com.ericsson.oss.cba.mediation.test.util.DpsUtil;
import com.ericsson.oss.cba.mediation.test.util.TestUtil;

@RunWith(Arquillian.class)
public class CamelSubsystemHelloTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(CamelSubsystemHelloTest.class);
    private static String dpsEarBuildDir = System.getProperty("dps.ear.build.dir", "target/dps_work");

    @EJB
    private TestUtil testUtil;

    @Deployment(name = "dpsRuntimeEar", testable = false, managed = true, order = 1)
    public static EnterpriseArchive createDpsRuntimeDeployment() {
        DpsRuntimeEar dpsEarUtil = new DpsRuntimeEar(dpsEarBuildDir);
        return dpsEarUtil.getDpsEnterpriseArchive();
    }

    @Deployment(name = "CamelEngineEar", testable = false, managed = true, order = 2)
    public static EnterpriseArchive createCamelEngineEar() {
        final CamelEngineEar camelEngineEar = new CamelEngineEar();
        return camelEngineEar.createCamelEar();
    }

    @Deployment(name = "mediationServiceEar", testable = false, managed = true, order = 3)
    public static EnterpriseArchive createMediationService() {
        final MediationServiceEar mediationService = new MediationServiceEar();
        return mediationService.getBaseEnterpriseArchive();
    }

    @Deployment(name = "mediationCoreEar", testable = false, managed = true, order = 4)
    public static EnterpriseArchive createMediationCore() {
        final MediationCoreEar mediationCore = new MediationCoreEar();
        return mediationCore.getBaseEnterpriseArchive();
    }

 

    @Deployment(name = "HelloHandlerEar", testable = false, managed = true, order = 6)
    public static EnterpriseArchive createHelloHandlerEar() {
        final HandlersEar helloHandlerEar = new HandlersEar();
        return helloHandlerEar.getHelloEnterpriseArchive();
    }


    @Deployment(name = "mocksEar", testable = true, managed = true, order = 6)
    public static EnterpriseArchive createMocksDeployment() throws IOException, InterruptedException {
        final MocksEar mocks = new MocksEar();
        mocks.addClass(Constants.class).addClass(DpsUtil.class).addClass(TestUtil.class).addClass(AsyncLatch.class).addClass(RegistrationEventSender.class)
                .addTestClass(CamelSubsystemHelloTest.class).addTestClass(MavenEnterpriseArchive.class)
                .addTestClass(IntegrationEnterpriseArchive.class).addEarLibrary(Artifact.COM_ERICSSON_NMS_MEDIATION_SDK_CORE_MEDIATION_ENGINE_API)
                .addEarDependency(Artifact.COM_ERICSSON_NMS_MEDIATION_MEDIATIONCORE_API_JAR)
                .addEarDependency(Artifact.COM_ERICSSON_OSS_MEDIATION_MEDIATION_CORESERVICELOCATOR_API_JAR)
                .addEarDependency(Artifact.COM_ERICSSON_NMS_MEDIATION_SDK_CORE_MODELS_API_JAR)
                .addEarDependency(Artifact.COM_ERICSSON_NMS_MEDIATION_SDK_CORE_MEDIATION_ENGINE_API)
                .addEarDependency(Artifact.COM_ERICSSON_OSS_ITPF_SDK_CONFIG_API_JAR)
                .addEarDependency(Artifact.COM_ERICSSON_OSS_ITPF_SDK_CONFIG_API_JAR_CACHE)
                .addEarDependency(Artifact.COM_ERICSSON_OSS_ITPF_SDK_CONFIG_CACHE).addEarDependency(Artifact.COM_ERICSSON_OSS_ITPF_SDK_CLUSTER_CORE)
                .addEarDependency(Artifact.COM_ERICSSON_OSS_ITPF_SDK_MODELLEDEVENTBUS_API_JAR)
                .addEarDependency(Artifact.COM_ERICSSON_OSS_ITPF_SDK_MODELLEDEVENTBUS_JMS_JAR)
                .addEarDependency(Artifact.COM_ERICSSON_ITPF_DATALAYER_DPS_REMOTE_API);
        mocks.addEJBResource("META-INF/jboss-ejb3.xml", "META-INF/jboss-ejb3.xml");
        final EnterpriseArchive ear = mocks.getEnterpriseArchive();
        ear.setManifest("MANIFEST.MF");
        return ear;
    }

    /* ************************************************************
     * TESTS
     * 
     * *************************************************************
     */

    @Test
    @OperateOnDeployment("mocksEar")
    public void testAddNode() {
        LOGGER.info("{} AddNodeTest:testAddNode", Constants.TEST_CASE);
        try {
            testUtil.addNode(1);
            testUtil.verifyNodeAdded(1);
        } finally {
            testUtil.deleteNode(1);
        }
    }

}

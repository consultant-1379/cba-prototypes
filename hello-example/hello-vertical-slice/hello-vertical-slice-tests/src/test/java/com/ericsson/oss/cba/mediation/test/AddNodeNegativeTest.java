package com.ericsson.oss.cba.mediation.test;
///*------------------------------------------------------------------------------
// *******************************************************************************
// * COPYRIGHT Ericsson 2012
// *
// * The copyright to the computer program(s) herein is the property of
// * Ericsson Inc. The programs may be used and/or copied only with written
// * permission from Ericsson Inc. or in accordance with the terms and
// * conditions stipulated in the agreement/contract under which the
// * program(s) have been supplied.
// *******************************************************************************
// *----------------------------------------------------------------------------*/
//package com.ericsson.oss.mediation.test;
//
//import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.fail;
//
//import javax.ejb.EJB;
//import javax.inject.Inject;
//
//import org.jboss.arquillian.container.test.api.Deployment;
//import org.jboss.arquillian.container.test.api.OperateOnDeployment;
//import org.jboss.arquillian.junit.Arquillian;
//import org.jboss.arquillian.junit.InSequence;
//import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.ericsson.oss.itpf.datalayer.dps.delegate.DataAccessDelegate;
//import com.ericsson.oss.itpf.datalayer.dps.delegate.DataAccessDelegateConfiguration;
//import com.ericsson.oss.itpf.datalayer.dps.delegate.exception.DataAccessDelegateException;
//import com.ericsson.oss.mediation.async.AsyncLatch;
//import com.ericsson.oss.mediation.deployment.Artifact;
//import com.ericsson.oss.mediation.deployment.Constants;
//import com.ericsson.oss.mediation.deployment.DpsRuntimeEar;
//import com.ericsson.oss.mediation.deployment.IntegrationEnterpriseArchive;
//import com.ericsson.oss.mediation.deployment.MavenEnterpriseArchive;
//import com.ericsson.oss.mediation.deployment.MediationCoreEar;
//import com.ericsson.oss.mediation.deployment.MediationServiceEar;
//import com.ericsson.oss.mediation.deployment.MocksEar;
//import com.ericsson.oss.mediation.mocks.logs.MockLogHandler;
//import com.ericsson.oss.mediation.mocks.service.registration.RegistrationEventSender;
//import com.ericsson.oss.mediation.test.util.DpsUtil;
//import com.ericsson.oss.mediation.test.util.TestUtil;
//
//@RunWith(Arquillian.class)
//public class AddNodeNegativeTest {
//
//    public static final Logger LOGGER = LoggerFactory.getLogger(AddNodeNegativeTest.class);
//
//    private static final String MODEL_SERVICE_LOG = "com.ericsson.oss.mediation.service.engine.ModelServiceWrapperImpl";
//    private static final String SEARCH_STRING_MODEL = "There is no Mediation Configuration for " + Constants.NO_MEDIATION_CONFIG + " namespace";
//
//    @EJB(lookup = DataAccessDelegateConfiguration.JNDI_LOOKUP_NAME)
//    private DataAccessDelegate delegate;
//
//    @EJB
//    private TestUtil testUtil;
//
//    @Inject
//    private DpsUtil dpsUtil;
//
//    private static String dpsEarBuildDir = System.getProperty("dps.ear.build.dir", "target/dps_work");
//
//    @Deployment(name = "dpsRuntimeEar", testable = false, managed = true, order = 1)
//    public static EnterpriseArchive createDpsRuntimeDeployment() {
//        final DpsRuntimeEar dpsEarUtil = new DpsRuntimeEar(dpsEarBuildDir);
//        return dpsEarUtil.getDpsEnterpriseArchive();
//    }
//
//    @Deployment(name = "mocksEar", testable = true, managed = true, order = 1)
//    public static EnterpriseArchive createMocksDeployment() {
//        final MocksEar mocks = new MocksEar();
//        mocks.addClass(Constants.class).addClass(RegistrationEventSender.class).addClass(TestUtil.class).addClass(AsyncLatch.class)
//                .addClass(DpsUtil.class).addTestClass(AddNodeNegativeTest.class).addTestClass(MediationCoreEar.class)
//                .addTestClass(MediationServiceEar.class).addTestClass(MavenEnterpriseArchive.class).addTestClass(IntegrationEnterpriseArchive.class)
//                .addEarLibrary(Artifact.COM_ERICSSON_NMS_MEDIATION_SDK_CORE_MEDIATION_ENGINE_API)
//                .addEarLibrary(Artifact.COM_ERICSSON_MODELLING_COMMON_API)
//                .addEarDependency(Artifact.COM_ERICSSON_NMS_MEDIATION_MEDIATIONCORE_API_JAR)
//                .addEarDependency(Artifact.COM_ERICSSON_OSS_MEDIATION_MEDIATION_CORESERVICELOCATOR_API_JAR)
//                .addEarDependency(Artifact.COM_ERICSSON_NMS_MEDIATION_SDK_CORE_MODELS_API_JAR)
//                .addEarDependency(Artifact.COM_ERICSSON_NMS_MEDIATION_SDK_CORE_MEDIATION_ENGINE_API)
//                .addEarDependency(Artifact.COM_ERICSSON_OSS_ITPF_SDK_CONFIG_API_JAR)
//                .addEarDependency(Artifact.COM_ERICSSON_OSS_ITPF_SDK_CONFIG_API_JAR_CACHE)
//                .addEarDependency(Artifact.COM_ERICSSON_OSS_ITPF_SDK_CONFIG_CACHE)
//                .addEarDependency(Artifact.COM_ERICSSON_OSS_ITPF_SDK_MODELLEDEVENTBUS_API_JAR)
//                .addEarDependency(Artifact.COM_ERICSSON_OSS_ITPF_SDK_CLUSTER_CORE)
//                .addEarDependency(Artifact.COM_ERICSSON_OSS_ITPF_SDK_MODELLEDEVENTBUS_JMS_JAR);
//        final EnterpriseArchive ear = mocks.getEnterpriseArchive();
//        ear.setManifest("MANIFEST.MF");
//        return ear;
//    }
//
//    @Deployment(name = "mediationServiceEar", testable = false, managed = true, order = 2)
//    public static EnterpriseArchive createMediationService() {
//        final MediationServiceEar mediationService = new MediationServiceEar();
//        return mediationService.getBaseEnterpriseArchive();
//    }
//
//    @Deployment(name = "mediationCoreEar", testable = false, managed = true, order = 3)
//    public static EnterpriseArchive createMediationCore() {
//        final MediationCoreEar mediationCore = new MediationCoreEar();
//        return mediationCore.getBaseEnterpriseArchive();
//    }
//
//    @Test
//    @InSequence(1)
//    @OperateOnDeployment("mocksEar")
//    public void testCreateManagedObjectWithoutMediationConfiguration() {
//        final MockLogHandler handler = new MockLogHandler();
//        handler.flush();
//        // execute test
//        final String fdn = dpsUtil.createNoMedMo();
//        // verify no mo was created
//        assertTrue(dpsUtil.findMo(fdn) == null);
//        // verify Exception was thrown
//        testUtil.verifyInformationInLogs(handler, MODEL_SERVICE_LOG, SEARCH_STRING_MODEL);
//    }
//
//    @Test
//    @InSequence(2)
//    @OperateOnDeployment("mocksEar")
//    public void testCreatePersistenceObjectWithoutFlow() throws DataAccessDelegateException, InterruptedException {
//        long dummyPoId = 0;
//        try {
//            // execute test
//            dummyPoId = dpsUtil.createDummyPersistenceObject();
//            // verify
//            Assert.assertNotNull(dpsUtil.findPo(dummyPoId));
//        } catch (final Throwable t) {
//            fail("testCreatePersistenceObjectWithoutFlow failed as it got unexpected exception: " + t.getMessage());
//        } finally {
//            if (dummyPoId > 0) {
//                // tidy up
//                LOGGER.info("testCreatePersistenceObjectWithoutFlow tidying up...");
//                dpsUtil.deletePo(dummyPoId);
//            }
//        }
//    }
//
//}

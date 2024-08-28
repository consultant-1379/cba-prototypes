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
//import static org.junit.Assert.fail;
//
//import javax.ejb.EJB;
//
//import org.jboss.arquillian.container.test.api.Deployment;
//import org.jboss.arquillian.container.test.api.OperateOnDeployment;
//import org.jboss.arquillian.junit.Arquillian;
//import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
//import org.jboss.shrinkwrap.api.spec.ResourceAdapterArchive;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.ericsson.oss.mediation.async.AsyncLatch;
//import com.ericsson.oss.mediation.deployment.Artifact;
//import com.ericsson.oss.mediation.deployment.CamelEngineRar;
//import com.ericsson.oss.mediation.deployment.Constants;
//import com.ericsson.oss.mediation.deployment.DpsRuntimeEar;
//import com.ericsson.oss.mediation.deployment.HandlersEar;
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
///**
// * Add-node functional tests
// *
// * @author eshacow
// *
// */
//@RunWith(Arquillian.class)
//public class AddNodeTest {
//
//    public static final Logger LOGGER = LoggerFactory.getLogger(AddNodeTest.class);
//    private static final String ADD_NODE_TEST_CASE = "AddNodeTest:";
//    private static String dpsEarBuildDir = System.getProperty("dps.ear.build.dir", "target/dps_work");
//    public static final String MOCI_HANDLER_LOG = "com.ericsson.oss.models.moci.mediation.handlers.MociHandler";
//    private static final String SEARCH_STRING = "successfully connected with the node ip [192.168.100.";
//    private static final String SEARCH_STRING_EXCEPTION ="Was unable to contact network element with ipAddress=192.168.100.300";
//
//    @EJB
//    private TestUtil testUtil;
//
//    @EJB
//    private AsyncLatch latch;
//
//
//    /**
//     *
//     * @return DPSRuntime ear file
//     */
//    @Deployment(name = "dpsRuntimeEar", testable = false, managed = true, order = 1)
//    public static EnterpriseArchive createDpsRuntimeDeployment() {
//        final DpsRuntimeEar dpsEarUtil = new DpsRuntimeEar(dpsEarBuildDir);
//        return dpsEarUtil.getDpsEnterpriseArchive();
//    }
//
//    /**
//     *
//     * @return mocks ear
//     * @throws Exception
//     *             if the ear cannot be created
//     */
//    @Deployment(name = "mocksEar", testable = true, managed = true, order = 1)
//    public static EnterpriseArchive createMocksDeployment() throws Exception {
//        final MocksEar mocks = new MocksEar();
//        final String jboosEjb3 = "META-INF/jboss-ejb3.xml";
//        mocks.addClass(Constants.class).addClass(DpsUtil.class).addClass(RegistrationEventSender.class).addClass(TestUtil.class)
//                .addClass(AsyncLatch.class).addTestClass(AddNodeTest.class).addTestClass(MediationCoreEar.class)
//                .addTestClass(MediationServiceEar.class).addTestClass(MavenEnterpriseArchive.class).addTestClass(IntegrationEnterpriseArchive.class)
//                .addEarLibrary(Artifact.COM_ERICSSON_NMS_MEDIATION_SDK_CORE_MEDIATION_ENGINE_API)
//                .addEarDependency(Artifact.COM_ERICSSON_NMS_MEDIATION_MEDIATIONCORE_API_JAR)
//                .addEarDependency(Artifact.COM_ERICSSON_OSS_MEDIATION_MEDIATION_CORESERVICELOCATOR_API_JAR)
//                .addEarDependency(Artifact.COM_ERICSSON_NMS_MEDIATION_SDK_CORE_MODELS_API_JAR)
//                .addEarDependency(Artifact.COM_ERICSSON_NMS_MEDIATION_SDK_CORE_MEDIATION_ENGINE_API)
//                .addEarDependency(Artifact.COM_ERICSSON_OSS_ITPF_SDK_CONFIG_API_JAR)
//                .addEarDependency(Artifact.COM_ERICSSON_OSS_ITPF_SDK_CONFIG_API_JAR_CACHE)
//                .addEarDependency(Artifact.COM_ERICSSON_OSS_ITPF_SDK_CONFIG_CACHE).addEarDependency(Artifact.COM_ERICSSON_OSS_ITPF_SDK_CLUSTER_CORE)
//                .addEarDependency(Artifact.COM_ERICSSON_OSS_ITPF_SDK_MODELLEDEVENTBUS_API_JAR)
//                .addEarDependency(Artifact.COM_ERICSSON_OSS_ITPF_SDK_MODELLEDEVENTBUS_JMS_JAR)
//                .addEarDependency(Artifact.COM_ERICSSON_ITPF_DATALAYER_DPS_REMOTE_API);
//        mocks.addEJBResource(jboosEjb3, jboosEjb3);
//        final EnterpriseArchive ear = mocks.getEnterpriseArchive();
//        ear.setManifest("MANIFEST.MF");
//        return ear;
//    }
//
//    /**
//     *
//     * @return the camel-engine rar
//     */
//    @Deployment(name = "camelEngineRar", testable = false, managed = true, order = 2)
//    public static ResourceAdapterArchive createCamelEngine() {
//        final CamelEngineRar camelEngineRar = new CamelEngineRar();
//        return camelEngineRar.getCamelEngineRar();
//    }
//
//    /**
//     *
//     * @return the mediation-service ear
//     */
//    @Deployment(name = "mediationServiceEar", testable = false, managed = true, order = 3)
//    public static EnterpriseArchive createMediationService() {
//        final MediationServiceEar mediationService = new MediationServiceEar();
//        return mediationService.getBaseEnterpriseArchive();
//    }
//
//    /**
//     *
//     * @return the mediation-core ear
//     */
//    @Deployment(name = "mediationCoreEar", testable = false, managed = true, order = 4)
//    public static EnterpriseArchive createMediationCore() {
//        final MediationCoreEar mediationCore = new MediationCoreEar();
//        return mediationCore.getBaseEnterpriseArchive();
//    }
//
//    /**
//     *
//     * @return the eai-creation-handler ear
//     */
//    @Deployment(name = "EaiCreationHandlerEar", testable = false, managed = true, order = 5)
//    public static EnterpriseArchive createEaiCreationHandlerEar() {
//        final HandlersEar eaiCreationHandlerEar = new HandlersEar();
//        return eaiCreationHandlerEar.getEaiEnterpriseArchive();
//    }
//
//    /**
//     *
//     * @return the ci-association-handler ear
//     */
//    @Deployment(name = "CiAssociationHandlerEar", testable = false, managed = true, order = 6)
//    public static EnterpriseArchive createCiAssociationHandlerEar() {
//        final HandlersEar ciAssociationHandlerEar = new HandlersEar();
//        return ciAssociationHandlerEar.getCiEnterpriseArchive();
//    }
//
//    /**
//     * Basic test of add-node functionality. Adds a single node.
//     */
//    @Test
//    @OperateOnDeployment("mocksEar")
//    /*
//     * TODO: Test needs to be updated. Once there is a better way to verify that node is pinged
//     * See: TORF-16376
//     */
//    public void testAddNode() {
//        final MockLogHandler handler = new MockLogHandler();
//        handler.flush();
//        logTestStart("testAddNode");
//        try {
//            testUtil.addNode(1);
//            testUtil.verifyNodeAdded(1);
//            testUtil.verifyInformationInLogs(handler,MOCI_HANDLER_LOG,SEARCH_STRING+"1]");
//        } finally {
//            testUtil.deleteNode(1);
//        }
//    }
//    /**
//     * Basic test of add-node functionality. Adds a single node with non-contactable ipAddress
//     */
//    @Test
//    @OperateOnDeployment("mocksEar")
//    /*
//     * TODO: Test needs to be updated. Once there is a better way to verify that node is pinged or not
//     * See: TORF-16376
//     */
//    public void testAddNodeUnContactable() {
//        final MockLogHandler handler = new MockLogHandler();
//        handler.flush();
//        logTestStart("testAddNodeUnContactable");
//        try {
//            testUtil.addNode(300);
//            testUtil.verifyNodeAdded(300);
//            testUtil.verifyInformationInLogs(handler,MOCI_HANDLER_LOG,SEARCH_STRING_EXCEPTION);
//        } finally {
//            testUtil.deleteNode(300);
//        }
//    }
//
//    /**
//     * Test add 2 nodes sequentially
//     */
//    @Test
//    @OperateOnDeployment("mocksEar")
//    public void testAddTwoNodesSequentially() {
//        logTestStart("testAddTwoNodesSequentially");
//        addMultipleNodesSequentially(2);
//    }
//
//    /**
//     * Test add 10 nodes sequentially
//     */
//    @Test
//    @OperateOnDeployment("mocksEar")
//    public void testAddTenNodesSequentially() {
//        logTestStart("testAddTenNodesSequentially");
//        addMultipleNodesSequentially(10);
//    }
//
//    /**
//     * Test add 2 nodes in parallel
//     *
//     * @throws Exception
//     *             if the Asynchronous bean cannot be invoked
//     */
//    @Test
//    @OperateOnDeployment("mocksEar")
//    public void testAddTwoNodesInParallel() throws Exception {
//        logTestStart("testAddTwoNodesInParallel");
//        addMultipleNodesInParallel(2);
//    }
//
//    /**
//     * Test add 10 nodes in parallel
//     *
//     * @throws Exception
//     *             if the Asynchronous bean cannot be invoked
//     */
//    @Test
//    @OperateOnDeployment("mocksEar")
//    public void testAddTenNodesInParallel() throws Exception {
//        logTestStart("testAddTenNodesInParallel");
//        addMultipleNodesInParallel(10);
//    }
//
//    private void addMultipleNodesSequentially(final int numberOfNodes) {
//        try {
//            for (int i = 1; i < numberOfNodes + 1; i++) {
//                testUtil.addNode(i);
//                testUtil.verifyNodeAdded(i);
//            }
//        } finally {
//            for (int i = 1; i < numberOfNodes + 1; i++) {
//                testUtil.deleteNode(i);
//            }
//        }
//    }
//
//    private void addMultipleNodesInParallel(final int numberOfNodes) throws Exception {
//        boolean failed = false;
//        latch.initialize(numberOfNodes);
//        try {
//            for (int i = 1; i < numberOfNodes + 1; i++) {
//                try {
//                    testUtil.addNodeAsynchronous(i);
//                } catch (final IllegalAccessException e) {
//                    failed = true;
//                    LOGGER.error(e.getMessage());
//                    break;
//                }
//            }
//            waitUntilNodesAdded();
//            for (int i = 1; i < numberOfNodes + 1; i++) {
//                testUtil.verifyNodeAdded(i);
//            }
//        } finally {
//            for (int i = 1; i < numberOfNodes + 1; i++) {
//                testUtil.deleteNode(i);
//            }
//            latch.reset();
//            if (failed) {
//                fail();
//            }
//        }
//    }
//
//    private void waitUntilNodesAdded() throws Exception {
//        while (latch.isOpen()) {
//            Thread.sleep(100);
//        }
//    }
//
//    private void logTestStart(final String testMethod) {
//        LOGGER.info("{} {}{}", Constants.TEST_CASE, ADD_NODE_TEST_CASE, testMethod);
//    }
//
//
//}

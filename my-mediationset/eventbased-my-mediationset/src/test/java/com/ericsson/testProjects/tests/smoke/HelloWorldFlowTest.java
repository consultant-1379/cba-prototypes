package com.ericsson.testProjects.tests.smoke;

import java.io.File;
import java.util.UUID;

import javax.inject.Inject;
import javax.naming.InitialContext;

import org.jboss.arquillian.container.test.api.*;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.itpf.sdk.eventbus.model.EventSender;
import com.ericsson.oss.itpf.sdk.eventbus.model.annotation.Modeled;
import com.ericsson.oss.mediation.core.events.MediationClientType;
import com.ericsson.testProjects.*;
import com.ericsson.testProjects.tests.utils.Artifact;
import com.ericsson.testProjects.util.DataLayerUtility;
import com.ericsson.oss.mediation.sdk.event.MediationTaskRequest;
import com.ericsson.testProjects.SampleSingleStepMediationTaskRequest;

/*
 * HelloWorld Flow Test Case
 * 
 */

@RunWith(Arquillian.class)
public class HelloWorldFlowTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldFlowTest.class);

    @ArquillianResource
    private ContainerController controller;

    @ArquillianResource
    private Deployer deployer;

    private String generalName;

    /**
     * We inject service framework event sender into our test case, it will be used from the test to send event that will trigger Hello World flow
     */
    @Inject
    @Modeled
    private EventSender<MediationTaskRequest> requestSender;

    /**
     * Deploy camel-engine.ear taken from nexus. Version of the engine is managed through main pom file.
     * 
     * @return Enterprise Archive representing camel-engine.ear
     */
    @Deployment(managed = false, testable = false, order = 1, name = "CAMEL_ENGINE")
    public static Archive<?> createCamelEngineDeployment() {
        LOGGER.info("Creating camel engine deployment from maven artifact {}", Artifact.COM_ERICSSON_OSS_MEDIATION__CAMEL_ENGINE);

        return ShrinkWrap.createFromZipFile(EnterpriseArchive.class,
                Artifact.resolveArtifactWithoutDependencies(Artifact.COM_ERICSSON_OSS_MEDIATION__CAMEL_ENGINE));

    }

    /**
     * Deploy mediation-service.ear taken from nexus. Version of mediation-service is managed through main pom file.
     * 
     * @return Enterprise archive representing mediation-service.ear
     */
    @Deployment(managed = false, testable = false, order = 2, name = "MEDIATION_SERVICE")
    public static Archive<?> createMediationServiceDeployment() {

        LOGGER.info("Creating mediation service deployment from maven artifact {}", Artifact.COM_ERICSSON_OSS_MEDIATION__MEDIATION_SERVICE_EAR);
        EnterpriseArchive ear = ShrinkWrap.createFromZipFile(EnterpriseArchive.class,
                Artifact.resolveArtifactWithoutDependencies(Artifact.COM_ERICSSON_OSS_MEDIATION__MEDIATION_SERVICE_EAR));
        ear.addAsLibraries(Artifact
                .resolveArtifactWithoutDependencies(Artifact.COM_ERICSSON_OSS_MEDIATION_TRAINING___SHARED));
        return ear;
    }

    /**
     * Deploy mediation router taken from nexus. Version of mediation router is managed through main pom file.
     * 
     * @return Enterprise archive representing mediation router
     */
    @Deployment(managed = false, testable = false, order = 3, name = "MEDIATION_ROUTER")
    public static Archive<?> createMediationRouterDeployment() {

        LOGGER.info("Creating mediation router deployment from maven artifact {}", Artifact.COM_ERICSSON_OSS_MEDIATION__MEDIATION_ROUTER_EAR);

        EnterpriseArchive ear = ShrinkWrap.createFromZipFile(EnterpriseArchive.class,
                Artifact.resolveArtifactWithoutDependencies(Artifact.COM_ERICSSON_OSS_MEDIATION__MEDIATION_ROUTER_EAR));
        ear.addAsLibraries(Artifact
                .resolveArtifactWithoutDependencies(Artifact.COM_ERICSSON_OSS_MEDIATION_TRAINING___SHARED));

        return ear;
    }

    /**
     * Deploy event client taken from nexus. Version of mediation-core is managed through main pom file.
     * 
     * @return Enterprise archive representing mediation-core.ear
     */
    @Deployment(managed = false, testable = false, order = 4, name = "EVENT_CLIENT")
    public static Archive<?> createEventClientDeployment() {

        LOGGER.info("Creating event client deployment from maven artifact {}", Artifact.COM_ERICSSON_OSS_MEDIATION__EVENT_CLIENT_EAR);

        EnterpriseArchive ear = ShrinkWrap.createFromZipFile(EnterpriseArchive.class,
                Artifact.resolveArtifactWithoutDependencies(Artifact.COM_ERICSSON_OSS_MEDIATION__EVENT_CLIENT_EAR));
        ear.addAsLibraries(Artifact
                .resolveArtifactWithoutDependencies(Artifact.COM_ERICSSON_OSS_MEDIATION_TRAINING___SHARED));

        return ear;
    }

    @Deployment(managed = false, testable = false, order = 5, name = "DPS_CLIENT")
    public static Archive<?> createDPSClientDeployment() {

        LOGGER.info("Creating dps client deployment from maven artifact {}", Artifact.COM_ERICSSON_OSS_MEDIATION__DPS_CLIENT_EAR);

        EnterpriseArchive ear = ShrinkWrap.createFromZipFile(EnterpriseArchive.class,
                Artifact.resolveArtifactWithoutDependencies(Artifact.COM_ERICSSON_OSS_MEDIATION__DPS_CLIENT_EAR));
        ear.addAsLibraries(Artifact
                .resolveArtifactWithoutDependencies(Artifact.COM_ERICSSON_OSS_MEDIATION_TRAINING___SHARED));

        return ear;
    }

    /**
     * Since DPS will be generated based on the army model, and database will be empty, we will need this utility to insert at least one record that
     * will be target for this mediation request. Mediation seems to be tightly coupled with datalayer which makes this example more complicated.
     * 
     * @return Enterprise archive with few utility classes that will allow us to insert or check for needed records inside versant database
     */
    @Deployment(managed = false, testable = false, order = 6, name = "DPS_UTILITY")
    public static Archive<?> createDPSUtilityDeployment() {

        LOGGER.info("Creating dps utility deployment from file {}", "Artifact.COM_ERICSSON_OSS_MEDIATION_TRAINING___DPS_UTILITY");
        return ShrinkWrap.createFromZipFile(EnterpriseArchive.class,
                Artifact.resolveArtifactWithoutDependencies(Artifact.COM_ERICSSON_OSS_MEDIATION_TRAINING___DPS_UTILITY));

    }

    /**
     * Deploy enterprise archive that contains our handler
     * 
     * @return Enterprise archive with our HelloWorld handler
     */
    @Deployment(managed = false, testable = false, order = 7, name = "HELLO_WORLD_HANDLER")
    public static Archive<?> createHelloWorldHandlerDeployment() {

        LOGGER.info("Creating hello world handler deployment from file {}", Artifact.COM_ERICSSON_OSS_MEDIATION___HELLO_WORLD_HANDLER);
        return ShrinkWrap.createFromZipFile(EnterpriseArchive.class,
                Artifact.resolveArtifactWithoutDependencies(Artifact.COM_ERICSSON_OSS_MEDIATION___HELLO_WORLD_HANDLER));
        
        

    }

    /**
     * Deploy war archive with our taste case. Here we are using standard arquillian testing approach, where we deploy war containing our test case(s)
     * 
     * @return Web archive with our test case.
     */
    @Deployment(managed = false, testable = true, order = 8, name = "EVENT_HANDLER_TEST")
    public static Archive<?> createEventHandlerTestDeployment() {
        LOGGER.info("Creating test deployment for EVENT HANDLER TEST.");
        WebArchive testWar = ShrinkWrap.create(WebArchive.class, "event_handler_test.war");
        testWar.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        testWar.addClass(HelloWorldFlowTest.class);
        testWar.addAsWebInfResource("web.xml", "web.xml");
        testWar.addAsLibraries(Artifact.resolveArtifactWithoutDependencies(Artifact.COM_ERICSSON_OSS_ITPF_SDK___SFWK_DIST));
        testWar.addAsLibraries(Artifact.resolveArtifactWithoutDependencies(Artifact.COM_ERICSSON_OSS_MEDIATION___MEDIATION_SDK_CORE_EVENTS));
        testWar.addAsLibraries(Artifact.resolveArtifactWithoutDependencies(Artifact.COM_ERICSSON_OSS_MEDIATION___CORE_MEDIATION_MODELS_API));
        testWar.addAsLibraries(Artifact.resolveArtifactWithoutDependencies(Artifact.COM_ERICSSON_OSS_MEDIATION_TRAINING___DPS_UTILITY_API));
        testWar.addAsLibraries(Artifact
                .resolveArtifactWithoutDependencies(Artifact.COM_ERICSSON_OSS_MEDIATION_TRAINING___SHARED));
        return testWar;
    }

    @BeforeClass
    public static void cleanMarkerFiles() {

        String [] files = new String[5];
           files [0] = "target/HelloWorld.txt";
           files [1] = "target/SingleStepHandlerA.txt";
           files [2] = "target/MultipleStepHandlerA.txt";
           files [3] = "target/MultipleStepHandlerB.txt";
           files [4] = "target/MultipleStepHandlerC.txt";
              for(String targetFile : files){
             final File file = new File(targetFile);
              if (file.exists()){
                  file.delete();
              }
             }
    }

    /**
     * Instruct arquillian to deploy camel engine to the running EAP
     */
    @InSequence(1)
    @Test
    public void deployCamelEngine() {

        LOGGER.info("<------- DEPLOY CAMEL ENGINE------->");
        this.deployer.deploy("CAMEL_ENGINE");

    }

    /**
     * Instruct arquillian to deploy mediation service to the running EAP
     */
    @InSequence(2)
    @Test
    public void deployMediationService() {

        LOGGER.info("<------- DEPLOY MEDIATION SERVICE ENGINE------->");
        this.deployer.deploy("MEDIATION_SERVICE");

    }

    /**
     * Instruct arquillian to deploy mediation core to the running EAP
     */
    @InSequence(3)
    @Test
    public void deployMediationRouter() {

        LOGGER.info("<------- DEPLOY MEDIATION ROUTER------->");
        this.deployer.deploy("MEDIATION_ROUTER");

    }

    /**
     * Instruct arquillian to deploy mediation core to the running EAP
     */
    @InSequence(4)
    @Test
    public void deployEventClient() {

        LOGGER.info("<------- DEPLOY EVENT CLIENT------->");
        this.deployer.deploy("EVENT_CLIENT");

    }

    /**
     * Instruct arquillian to deploy mediation core to the running EAP
     */
    @InSequence(5)
    @Test
    public void deployDPSClient() {

        LOGGER.info("<------- DEPLOY DPS CLIENT------->");
        this.deployer.deploy("DPS_CLIENT");

    }

    /**
     * Instruct arquillian to deploy dps utility, as we might need to insert few records into versant before we invoke mediation with event
     */
    @InSequence(6)
    @Test
    public void deployDPSUtility() {

        LOGGER.info("<------- DEPLOY DPS UTILITY------->");
        this.deployer.deploy("DPS_UTILITY");

    }

    /**
     * Instruct arquillian to deploy ear with our handler(s) to the running EAP
     */
    @InSequence(7)
    @Test
    public void deployHelloWorldHandler() {

        LOGGER.info("<------- DEPLOY Hello World Handler------->");
        this.deployer.deploy("HELLO_WORLD_HANDLER");

    }

    /**
     * Instruct arquillian to deploy our test case to the running EAP
     */
    @OperateOnDeployment("EVENT_HANDLER_TEST")
    @InSequence(8)
    @Test
    public void deployTest() {
        LOGGER.info("<------- DEPLOY EVENT_HANDLER_TEST------->");
        this.deployer.deploy("EVENT_HANDLER_TEST");

    }

    /**
     * Utility method used to insert test record into database
     */
    private long insertTestRecord() {
        LOGGER.info("<------- INSERT TEST RECORD INTO DATABASE------->");
        try {
            InitialContext ctx = new InitialContext();
            /**
             * TODO: Utility needs to bind hardcoded name into jndi, ie name that will not include deployment version, in this case 1.0.1-SNAPSHOT, as
             * this will change
             */
            //            final DataLayerUtility dpsUtil = (DataLayerUtility) ctx
            //                    .lookup("java:global/dps-utility-ear-1.0.1-SNAPSHOT/dps-utility-ejb-1.0.1-SNAPSHOT/DataLayerUtilityImpl!com.ericsson.oss.mediation.training.DataLayerUtility");
            final DataLayerUtility dpsUtil = (DataLayerUtility) ctx.lookup("java:/datalayer/DataLayerUtility");
            this.generalName = UUID.randomUUID().toString();
            return dpsUtil.insertGeneralWithFdn(this.generalName);
        } catch (final Exception e) {
            e.printStackTrace();
            LOGGER.error("Error inserting test record, stacktrace:", e);
        }

        return 0;

    }

    /**
     * Utility method used to check test record from database
     */
    private boolean checkTestRecord(long poID) {
        LOGGER.info("<------- CHECK TEST RECORD FROM DATABASE------->");
        try {
            InitialContext ctx = new InitialContext();
            final DataLayerUtility dpsUtil = (DataLayerUtility) ctx.lookup("java:/datalayer/DataLayerUtility");
            return dpsUtil.poIDExists(poID);
        } catch (final Exception e) {
            e.printStackTrace();
            LOGGER.error("Error checking test record, stacktrace:", e);
        }

        return false;
    }

    /*@OperateOnDeployment("EVENT_HANDLER_TEST")
    @InSequence(9)
    @Test
    public void invokeDPS() {
        LOGGER.info("<------- PERSISTENCE_TEST------->");
        *//**
         * First we insert a test record into the database, then we find the record in the database by using the poID generated when inserting the
         * record.
         *//*
        long poID = insertTestRecord();
        Assert.assertTrue(checkTestRecord(poID));
    }*/

    /**
     * Test where we will send event into mediation and this will trigger HelloWorld flow creation/invocation
     */
    @OperateOnDeployment("EVENT_HANDLER_TEST")
    @InSequence(10)
    @Test
    public void invokeEventHandlerRoute() {
        LOGGER.info("<------- EVENT_HANDLER_TEST HELLO WORLD ------->");
        /**
         * Let's insert test record into database, this record will be the target of this mediation invocation
         */
        insertTestRecord();
        final MediationTaskRequest req = createEvent(new MediationTaskRequest(), "General" + "=" + this.generalName);
        sendEvent(req);
        try {
            /**
             * Now we give it a few seconds for the flow invocation to happen, as this is fire and forget case (event driven)
             */
            Thread.sleep(10000);
            final File file = new File("target/HelloWorld.txt");
            Assert.assertTrue(file.exists());

        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @OperateOnDeployment("EVENT_HANDLER_TEST")
    @InSequence(9)
    @Test
    public void invokeSampleSingleStepRoute() {

        LOGGER.info("<------- invokeSampleSingleStepRoute------->");
        /**
         * Let's insert test record into database, this record will be the
         * target of this mediation invocation
         */
        String type = "General";
        insertTestRecord();

        final MediationTaskRequest req = createEvent(new SampleSingleStepMediationTaskRequest(),
         type + "=" + generalName);
        sendEvent(req);
        try {
            Thread.sleep(30000);
            String fileName = "target/SingleStepHandlerA.txt";
            final File file = new File(fileName);
            Assert.assertTrue(file.exists());
            LOGGER.info("File " + fileName + " found in file system. Test OK...");
        } catch (final InterruptedException e) {

            e.printStackTrace();
        }
    } 
    
    @OperateOnDeployment("EVENT_HANDLER_TEST")
    @InSequence(11)
    @Test
    public void invokeSampleMultipleStepRoute() {

        LOGGER.info("<------- invokeSampleMultipleStepRoute------->");
        /**
         * Let's insert test record into database, this record will be the
         * target of this mediation invocation
         */
        String type = "General";
        insertTestRecord();

        final MediationTaskRequest req = createEvent(new SampleMultipleStepMediationTaskRequest(),
         type + "=" + generalName);
        sendEvent(req);
        try {
            Thread.sleep(40000);
            String [] multiFiles = new String[3];
            multiFiles [0] = "target/MultipleStepHandlerA.txt";
            multiFiles [1] = "target/MultipleStepHandlerB.txt";
            multiFiles [2] = "target/MultipleStepHandlerC.txt";

            for(String targetFile : multiFiles){
              final File file = new File(targetFile);
              Assert.assertTrue(file.exists());
              LOGGER.info("File " + file + " found in file system. Test OK...");
            } 

         }catch (final InterruptedException e) {
            e.printStackTrace();

         }
    } 
    
    @OperateOnDeployment("EVENT_HANDLER_TEST")
    @InSequence(12)
    @Test
    public void invokeOutputFlow() {

        LOGGER.info("<------- invokeOutputFlow------->");
        /**
         * Let's insert test record into database, this record will be the
         * target of this mediation invocation
         */
        String type = "General";
        insertTestRecord();
        
        SampMultiStepOutputMedTaskReq smtr = new SampMultiStepOutputMedTaskReq();
        smtr.setTestField("testingValue");

        final MediationTaskRequest req = createEvent(smtr,
         type + "=" + generalName);
        LOGGER.info("<------- invokeOutputFlow req= ------->" + req);
        sendEvent(req);
        try {
            Thread.sleep(40000);
            String [] multiFiles = new String[2];
            multiFiles [0] = "target/OutputHandlerA.txt";
            multiFiles [1] = "target/OutputHandlerB.txt";

            for(String targetFile : multiFiles){
              final File file = new File(targetFile);
              Assert.assertTrue(file.exists());
              LOGGER.info("File " + file + " found in file system. Test OK...");
            } 

         }catch (final InterruptedException e) {
            e.printStackTrace();

         }
    }

    private MediationTaskRequest createEvent(MediationTaskRequest request, final String genName) {

        request.setClientType(MediationClientType.EVENT_BASED.name());
        /**
         * We set the type of mediation service we are interested in, defaults to PM, so let's use that This is another technical debt, and once
         * router evolves more it should be removed.
         */
        request.setProtocolInfo("PM");
        /**
         * Since this is event based invocation let's assign job id to this event
         */
        request.setJobId(UUID.randomUUID().toString());
        /**
         * Target of this invocation
         */

        request.setNodeAddress(genName);

        return request;
    }

    private void sendEvent(final MediationTaskRequest request) {
        LOGGER.info("<------- Sending event to mediation------------>");
        LOGGER.info("Job id = " +request.getJobId());
        this.requestSender.send(request);
    }

}

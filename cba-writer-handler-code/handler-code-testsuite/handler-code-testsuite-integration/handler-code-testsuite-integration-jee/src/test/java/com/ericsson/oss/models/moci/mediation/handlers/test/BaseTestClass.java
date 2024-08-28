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
package com.ericsson.oss.models.moci.mediation.handlers.test;

import static com.ericsson.oss.models.moci.mediation.handlers.test.deployment.Artifact.CAMEL_EAR;
import static com.ericsson.oss.models.moci.mediation.handlers.test.deployment.Artifact.COM_ERICSSON_OSS_ITPF_SDK_DIST_JAR;
import static com.ericsson.oss.models.moci.mediation.handlers.test.deployment.Artifact.MOCKITO_ALL;
import static com.ericsson.oss.models.moci.mediation.handlers.test.deployment.Artifact.NETWORK_ELEMENT_CONNECTOR_EAR;
import static com.ericsson.oss.models.moci.mediation.handlers.test.util.TestConstants.CAMEL_ENGINE_HANDLER_ROUTE;
import static com.ericsson.oss.models.moci.mediation.handlers.test.util.TestConstants.TEST;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.junit.After;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.dsl.DSLRouteDefinition;
import com.ericsson.oss.mediation.engine.api.MediationEngine;
import com.ericsson.oss.mediation.engine.api.MediationEngineConstants;
import com.ericsson.oss.mediation.flow.FlowAdapter;
import com.ericsson.oss.mediation.flow.FlowPath;
import com.ericsson.oss.mediation.flow.FlowPathElement;
import com.ericsson.oss.mediation.flow.FlowProcessor;
import com.ericsson.oss.models.moci.mediation.handlers.api.NodeConnectionProvider;
import com.ericsson.oss.models.moci.mediation.handlers.test.deployment.Deployments;
import com.ericsson.oss.models.moci.mediation.handlers.test.deployment.MociHandlerEar;
import com.ericsson.oss.models.moci.mediation.handlers.test.deployment.MockEar;
import com.ericsson.oss.models.moci.mediation.handlers.test.mocks.MockMociCMConnectionProvider;
import com.ericsson.oss.models.moci.mediation.handlers.test.mocks.MockNetworkElementConnection;
import com.ericsson.oss.models.moci.mediation.handlers.test.util.ConnectionProviderVerifier;
import com.ericsson.oss.models.moci.mediation.handlers.test.util.MociCmConnectionProviderCounter;
import com.ericsson.oss.models.moci.mediation.handlers.test.util.TestConstants;

public abstract class BaseTestClass {

    protected static final String WRITE_NODE_HANDLER_FQN = "com.ericsson.oss.models.moci.mediation.handlers.WriteNodeControllerHandler";

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseTestClass.class);

    @Rule
    public final TestName NAME = new TestName();

    @EJB(lookup = ConnectionProviderVerifier.CONNECTION_COUNTER_VERIFIER_JNDI)
    protected ConnectionProviderVerifier connectionProviderCounter;

    @EJB(lookup = MediationEngineConstants.CAMEL_MEDIATION_ENGINE_JNDI)
    protected MediationEngine camelMedEngine;

    protected FlowPath singlePath;
    protected FlowAdapter ingress;
    protected FlowProcessor processor;

    protected static EnterpriseArchive createTestDeployment(final Class< ? extends BaseTestClass> clazz) {
        final MockEar mocks = new MockEar(TEST);
        mocks.addClass(ConnectionProviderVerifier.class);
        mocks.addClass(MociCmConnectionProviderCounter.class);
        mocks.addClass(MockNetworkElementConnection.class);
        mocks.addClass(NodeConnectionProvider.class);
        mocks.addClass(MockMociCMConnectionProvider.class);
        mocks.addClass(BaseTestClass.class);
        mocks.addTestClass(clazz);
        mocks.addClassToEarLibrary(TestConstants.class);
        mocks.addEarLibrary(COM_ERICSSON_OSS_ITPF_SDK_DIST_JAR);
        mocks.addEarLibrary(MOCKITO_ALL);
        final EnterpriseArchive ear = mocks.getEnterpriseArchive();
        ear.addAsManifestResource("jboss-deployment-structure.xml");
        return ear;
    }

    /**
     * Creates the real Camel Engine ear.
     *
     * @return
     */
    @Deployment(name = "camelEar", testable = false, managed = true, order = 1)
    public static EnterpriseArchive createCamelEar() {
        return Deployments.createEnterpriseArchiveDeployment(CAMEL_EAR);
    }

    /**
     * Creates the real ne-conn ear.
     *
     * @return
     */
    @Deployment(name = "neConnEar", testable = false, managed = true, order = 2)
    public static EnterpriseArchive createNeConnEar() {
        return Deployments.createEnterpriseArchiveDeployment(NETWORK_ELEMENT_CONNECTOR_EAR);
    }

    /**
     * Creates the real mociHandler ear.
     *
     * @return
     */
    @Deployment(name = "mociHandlerEar", testable = false, managed = true, order = 3)
    public static EnterpriseArchive createMociHandlerDeployment() {
        return MociHandlerEar.createEar();
    }

    protected List<FlowPath> createSimpleTransactionalFlowPath(final String handlerFQN) {
        assertNotNull("Camel engine was not created/deployed.", camelMedEngine);
        LOGGER.info("********* Creating Flow for {} *********", NAME.getMethodName());
        final String uniqueCamelRouteID = CAMEL_ENGINE_HANDLER_ROUTE + handlerFQN;
        final List<FlowPath> pathList = new ArrayList<FlowPath>();
        final List<FlowPathElement> pathElementList = new ArrayList<FlowPathElement>();
        singlePath = mock(FlowPath.class, withSettings().extraInterfaces(DSLRouteDefinition.class).serializable());
        ingress = mock(FlowAdapter.class, withSettings().extraInterfaces(FlowPathElement.class).serializable());
        processor = mock(FlowProcessor.class, withSettings().extraInterfaces(FlowPathElement.class).serializable());
        pathElementList.add(ingress);
        pathElementList.add(processor);
        when(singlePath.getId()).thenReturn(uniqueCamelRouteID);
        when(singlePath.getPathElements()).thenReturn(pathElementList);
        when(singlePath.isTransactional()).thenReturn(true);
        when(ingress.getURI()).thenReturn("direct:" + uniqueCamelRouteID);
        when(processor.getClassName()).thenReturn(handlerFQN);
        pathList.add(singlePath);
        this.camelMedEngine.createFlow(pathList);
        return pathList;
    }

    @After
    public void resetVerifierInvocationsLists() {
        connectionProviderCounter.resetAll();
    }

}

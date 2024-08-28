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

import static com.ericsson.oss.mediation.engine.api.MediationEngineConstants.*;
import static com.ericsson.oss.models.moci.mediation.handlers.test.util.TestConstants.*;

import java.util.*;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.flow.FlowPath;
import com.ericsson.oss.mediation.modeling.modelservice.typed.configuration.PersistenceOperationType;

@RunWith(Arquillian.class)
public class ActionTest extends BaseTestClass {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionTest.class);
    private static final String SGSN_CLASS_NAME_OF_ACTION_ATTRIBUTE = "NE";
    private static final String SGSN_ACTION_NAME_NO_ARG_NO_RETURN = "deleteRas";
    private static final String SIMULATION_IP_ADDRESS_TO_CONNECT = "127.0.0.1";
    private static final String SGSN_FDN = "NE=1";
    private static final String SGSN_NODE_NAME = "NE01";
    private static final String NODE_NAME = "nodeName";

    /**
     * Creates a test ear that will contain this test class, a mock remote DPS implementation and a singleton for verifying the DPS calls on the
     * remote api.
     * 
     * @return
     */
    @Deployment(name = "testEar", testable = true, managed = true, order = 4)
    public static EnterpriseArchive getTestEar() {
        return createTestDeployment(ActionTest.class);
    }

    @Test
    @OperateOnDeployment("testEar")
    public void testEmptyArgumentsWhichReturnsVoid() throws Exception {
        final List<FlowPath> flowPath = createSimpleTransactionalFlowPath(WRITE_NODE_HANDLER_FQN);
        LOGGER.info("********* Invoking Flow for {} *********", NAME.getMethodName());
        final Map<String, Object> headers = new HashMap<>();
        headers.put(
                WRITE_NODE_HANDLER_FQN,
                createActionHeader(SIMULATION_IP_ADDRESS_TO_CONNECT, null, SGSN_ACTION_NAME_NO_ARG_NO_RETURN, SGSN_CLASS_NAME_OF_ACTION_ATTRIBUTE,
                        SGSN_FDN, "Sgsn_Mme", "1.27.0"));
        final Object result = this.camelMedEngine.invokeFlowWithResults(flowPath.get(0).getId(), headers);
        LOGGER.info("******* Verifying flow for {} ******** ", NAME.getMethodName());
        LOGGER.info("******* The result from handler is {} ******** ", result);

    }

    private Map<String, Object> createActionHeader(final String ipAddress, final Map<String, Object> actionAttrs, final String actionName,
                                                   final String poType, final String fdn, final String nameSpace, final String version) {
        final Map<String, Object> header = new HashMap<>();
        header.put(ADDRESS, ipAddress);
        header.put(NODE_NAME, SGSN_NODE_NAME);
        header.put(PTR_ACTION_ATTRIBUTES, actionAttrs);
        header.put(PTR_ACTION_NAME, actionName);
        header.put(PTR_PO_NAMESPACE, nameSpace);
        header.put(PTR_PO_TYPE, poType);
        header.put(PTR_PO_VERSION, version);
        header.put(PTR_TIMEOUT, TIMEOUT_VALUE);
        header.put(PTR_FDN, fdn);
        header.put(PTR_CLIENT_OPERATION, PersistenceOperationType.ACTION.name());
        return header;
    }
}

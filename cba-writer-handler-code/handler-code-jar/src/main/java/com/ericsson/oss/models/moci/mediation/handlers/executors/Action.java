/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2015
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.models.moci.mediation.handlers.executors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.itpf.datalayer.dps.modeling.modelservice.typed.persistence.primarytype.PrimaryTypeActionSpecification;
import com.ericsson.oss.itpf.datalayer.dps.modeling.modelservice.typed.persistence.primarytype.PrimaryTypeSpecification;
import com.ericsson.oss.itpf.modeling.modelservice.typed.core.EModelAttributeSpecification;
import com.jcraft.jsch.JSchException;

/**
 * Action is called by the controller handler to invoke a single action on the node.
 * 
 * @author ecapati
 */
public class Action extends CbaExecutorImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(Action.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.oss.models.moci.mediation.handlers.executors.WriteExecutor#execute()
     */
    @Override
    public Object execute() {
        buildXMLToSend();
        String result = "";
        final SgsnMMECredentialHolder sgsnMMECredentialHolder = new SgsnMMECredentialHolder();
        sgsnMMECredentialHolder.setIPAddress(requestData.getIpAddress());
        try {
            final String actionName = requestData.getActionName();

            final NetconfHelper netconfHelper = new NetconfHelper();
            LOGGER.info("********* opening the session *************");
            netconfHelper.openSession(sgsnMMECredentialHolder);

            final String query = Queries.performMOAction("302", requestData.getNodeName());

            LOGGER.info("********* Running the action *************");
            LOGGER.info("ACTION NAME = {}", actionName);
            LOGGER.info(query);
            result = netconfHelper.runQuery(Queries.performMOAction("302", requestData.getNodeName()));
            LOGGER.info("********* returning the result to the flow *************");
            netconfHelper.closeSession();
        } catch (final JSchException | InterruptedException | IOException e) {
            LOGGER.error(e.getMessage());
        }
        LOGGER.debug("Action executed successfully");
        return result;
    }

    public Object buildXMLToSend() {
        final Object convertedActionResult = null;
        final String fdn = requestData.getFdn();
        final String actionName = requestData.getActionName();
        LOGGER.info(">>>>>>>>> Action name >>>>>>>>>" + actionName);
        final PrimaryTypeSpecification specification = modelServiceFacade.getEModelSpecification(createModelInfo(), PrimaryTypeSpecification.class);
        LOGGER.info(specification.getDescription());
        LOGGER.info(specification.getKeyAttributeName());
        LOGGER.info(specification.toString());
        final PrimaryTypeActionSpecification actionSpecification = specification.getActionSpecification(actionName);
        LOGGER.info(actionSpecification.toString());
        LOGGER.info(actionSpecification.getName());
        LOGGER.info(actionSpecification.getDescription());

        final List<EModelAttributeSpecification> modeledActionParameters = new ArrayList<EModelAttributeSpecification>();
        for (final EModelAttributeSpecification actionParam : actionSpecification.getParameters()) {
            modeledActionParameters.add(actionParam);
            LOGGER.info(">>>>>> Adding >>>>>>>" + actionParam.getName());
        }
        LOGGER.debug("Action executed successfully on [{}] and returning value [{}]", fdn, convertedActionResult);
        return convertedActionResult;
    }

}
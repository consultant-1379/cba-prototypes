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

package com.ericsson.oss.mediation.util.netconf.validator;

import com.ericsson.oss.mediation.util.netconf.api.exception.NetconfManagerErrorMessages;
import com.ericsson.oss.mediation.util.netconf.api.exception.NetconfManagerException;
import com.ericsson.oss.mediation.util.netconf.capability.NetconfSessionCapabilities;
import com.ericsson.oss.mediation.util.netconf.flow.FlowInput;
import com.ericsson.oss.mediation.util.netconf.flow.FlowOutput;
import com.ericsson.oss.mediation.util.netconf.flow.component.FlowComponent;
import com.ericsson.oss.mediation.util.netconf.vo.HelloVo;
import com.ericsson.oss.mediation.util.netconf.vo.NetconfVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author xvaltda
 */
public class HelloVoValidator implements NetconfVoValidator, FlowComponent {

    private static final String EMPTY_STRING = "";
    private static final Logger logger = LoggerFactory.getLogger(HelloVoValidator.class);

    private HelloVo helloVo = null;
    private NetconfSessionCapabilities netconfSessionCapabilities = null;

    @Override
    public ValidatorOutput validate() {

        final ValidatorOutput validatorOutput = new ValidatorOutput();

        if (helloVo.isErrorReceived()) {

        } else {

            if ((helloVo.getSessionId() == null) || EMPTY_STRING.equals(helloVo.getSessionId())) {
                logger.info(NetconfManagerErrorMessages.SESSIONID_WAS_NOT_RECEIVED);
                validatorOutput.setErrorMessage(NetconfManagerErrorMessages.SESSIONID_WAS_NOT_RECEIVED);
                validatorOutput.setValid(false);
            } else if (!ValidatorUtil.isOnlyDigits(helloVo.getSessionId())) {
                logger.info(NetconfManagerErrorMessages.SESSIONID_BAD_FORMAT);
                validatorOutput.setErrorMessage(NetconfManagerErrorMessages.SESSIONID_BAD_FORMAT);
                validatorOutput.setValid(false);
            } else {
                try {
                    netconfSessionCapabilities.processCapabilities(helloVo);

                    if (netconfSessionCapabilities.getActiveCapabilities().isEmpty()) {
                        logger.info(NetconfManagerErrorMessages.CAPABILITIES_MISSMATCH
                                + " check the ECIM's capabilities.");
                        validatorOutput.setValid(false);
                        validatorOutput.setErrorMessage(NetconfManagerErrorMessages.CAPABILITIES_MISSMATCH);
                    }
                } catch (NetconfManagerException ex) {
                    logger.error("Error validating the capabilities, " + ex.getMessage());
                    validatorOutput.setValid(false);
                    validatorOutput.setErrorMessage(ex.getMessage());
                }

            }

        }

        return validatorOutput;

    }

    @Override
    public FlowOutput execute(final FlowInput input) {

        final FlowOutput output = new FlowOutput();
        output.setError(false);

        final NetconfVo netconfVo = (NetconfVo) input.getFlowSession().get("NETCONF_VO");
        if (!netconfVo.isErrorReceived()) {
            helloVo = (HelloVo) netconfVo;
            netconfSessionCapabilities = input.getNetconfSession().getNetconfSessionCapabilities();

            input.getNetconfSession().setSessionId(helloVo.getSessionId());
            logger.info("sessionId received:  " + helloVo.getSessionId());
            final ValidatorOutput validatorOutput = validate();

            if (!validatorOutput.isValid()) {
                output.setError(true);
                output.setErrorMessage(validatorOutput.getErrorMessage());
            }

        }
        return output;
    }
}

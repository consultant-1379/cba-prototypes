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
package com.ericsson.oss.mediation.util.netconf.operation;

import com.ericsson.oss.mediation.util.netconf.flow.FlowInput;
import com.ericsson.oss.mediation.util.netconf.flow.FlowOutput;
import com.ericsson.oss.mediation.util.netconf.flow.component.FlowComponent;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author xvaltda
 */
public class OperationComponent implements FlowComponent {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(OperationComponent.class);
    private final FlowOutput output;
    private final Object data;

    public OperationComponent() {
        output = new FlowOutput();
        data = null;
    }

    public OperationComponent(final Object data) {
        this.data = data;
        output = new FlowOutput();
    }

    @Override
    public FlowOutput execute(final FlowInput input) {

        final Operation operation = OperationFactory.create(input.getOperationType(), data, input.getNetconfSession());

        if (operation == null) {
            logger.error("Operation type " + input.getOperationType() + " is not supported by Netconf Manager");
            output.setErrorMessage("operation type " + input.getOperationType()
                    + " is not supported by NetconfManager.");
            output.setError(true);
        } else {
            input.getFlowSession().put("OPERATION", operation);
            output.setError(false);
        }

        return output;
    }
}

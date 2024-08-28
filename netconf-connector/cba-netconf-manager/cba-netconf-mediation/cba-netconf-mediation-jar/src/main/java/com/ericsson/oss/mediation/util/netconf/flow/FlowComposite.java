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

package com.ericsson.oss.mediation.util.netconf.flow;

import com.ericsson.oss.mediation.util.netconf.context.NetconfSession;
import com.ericsson.oss.mediation.util.netconf.flow.component.FlowComponent;
import com.ericsson.oss.mediation.util.netconf.operation.OperationType;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author xvaltda
 */
public class FlowComposite {

    private final List<FlowComponent> components;
    private FlowOutput output;
    private final FlowInput input;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(FlowComposite.class);

    public FlowComposite(final NetconfSession netconfSession, final OperationType operationType) {
        components = new ArrayList<FlowComponent>();
        input = new FlowInput(netconfSession, operationType);
    }

    public List<FlowComponent> getComponents() {
        return components;
    }

    public void addComponent(final FlowComponent component) {
        components.add(component);
    }

    public FlowOutput execute() {

        for (final FlowComponent component : components) {
            output = component.execute(input);

            if (output.isError()) {
                logger.debug("Error executing the flow for operation " + input.getOperationType() + ""
                        + " The flow will stop its execution");
                return output;
            }
        }
        return output;

    }

}

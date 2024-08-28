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

package com.ericsson.oss.mediation.util.netconf.io;

import com.ericsson.oss.mediation.util.netconf.flow.FlowInput;
import com.ericsson.oss.mediation.util.netconf.flow.FlowOutput;
import com.ericsson.oss.mediation.util.netconf.flow.component.FlowComponent;
import com.ericsson.oss.mediation.util.netconf.operation.Hello;
import com.ericsson.oss.mediation.util.netconf.operation.Operation;
import com.ericsson.oss.mediation.util.transport.api.exception.TransportException;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author xvaltda
 */
public class HelloWriterComponent implements FlowComponent {

    private FlowOutput output;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HelloWriterComponent.class);

    @Override
    public FlowOutput execute(final FlowInput input) {
        try {
            output = new FlowOutput();
            final Operation operation = (Operation) input.getFlowSession().get("OPERATION");
            final Hello hello = (Hello) operation;
            input.getNetconfSession().getTransportManager().sendData(hello.getTransportData());

            output.setError(false);
        } catch (TransportException ex) {
            logger.error("Error sending the Hello to the server");
            output.setError(true);
            output.setErrorMessage(ex.getMessage());
        }
        return output;
    }

}

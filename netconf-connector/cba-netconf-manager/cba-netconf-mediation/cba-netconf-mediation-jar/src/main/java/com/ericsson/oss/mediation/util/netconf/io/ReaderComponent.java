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

import com.ericsson.oss.mediation.util.netconf.flow.FlowErrorSeverity;
import com.ericsson.oss.mediation.util.netconf.flow.FlowInput;
import com.ericsson.oss.mediation.util.netconf.flow.FlowOutput;
import com.ericsson.oss.mediation.util.netconf.flow.component.FlowComponent;
import com.ericsson.oss.mediation.util.transport.api.TransportData;
import com.ericsson.oss.mediation.util.transport.api.exception.TransportException;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author xvaltda
 */
public class ReaderComponent implements FlowComponent {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ReaderComponent.class);
    final FlowOutput output;
    final boolean readAndClose;

    public ReaderComponent() {
        this(false);
    }

    public ReaderComponent(final boolean readAndClose) {
        this.output = new FlowOutput();
        this.readAndClose = readAndClose;
    }

    @Override
    public FlowOutput execute(final FlowInput input) {
        final TransportData transportData = new TransportData();
        transportData.setEndData(']');
        try {
            input.getNetconfSession().getTransportManager().readData(transportData, readAndClose);
            input.getFlowSession().put("TRANSPORT_DATA", transportData);
            output.setError(false);
        } catch (TransportException ex) {
            logger.debug("Error reading Transport Data {}", ex.getMessage());

            output.setError(true);
            output.setErrorMessage(ex.getMessage());

            if (ex.getSeverity() == FlowErrorSeverity.FATAL.getValue()) {
                output.setErrorSeverity(FlowErrorSeverity.FATAL);
            } else {
                output.setErrorSeverity(FlowErrorSeverity.FAILURE);
            }

        }
        return output;
    }
}

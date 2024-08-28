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

package com.ericsson.oss.mediation.util.netconf.parser;

import com.ericsson.oss.mediation.util.netconf.flow.FlowInput;
import com.ericsson.oss.mediation.util.netconf.flow.FlowOutput;
import com.ericsson.oss.mediation.util.netconf.flow.component.FlowComponent;
import com.ericsson.oss.mediation.util.netconf.vo.NetconfVo;
import com.ericsson.oss.mediation.util.transport.api.TransportData;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author xvaltda
 */
public class ParserComponent implements FlowComponent {

    private final FlowOutput output = new FlowOutput();
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ParserComponent.class);

    @Override
    public FlowOutput execute(final FlowInput input) {
        try {

            final TransportData data = (TransportData) input.getFlowSession().get("TRANSPORT_DATA");

            final NetconfParser parser = ParserFactory.create(input.getOperationType());

            final NetconfVo netconfVo = parser.parse(data);
            input.getFlowSession().put("NETCONF_VO", netconfVo);
            output.setError(false);
            output.setNetconfVo(netconfVo);

        } catch (ParserException ex) {
            logger.error("Error parsing the Operation: {}", input.getOperationType());
            logger.error(ex.getMessage());
            output.setError(true);
            output.setErrorMessage(ex.getMessage());
        }
        return output;
    }

}

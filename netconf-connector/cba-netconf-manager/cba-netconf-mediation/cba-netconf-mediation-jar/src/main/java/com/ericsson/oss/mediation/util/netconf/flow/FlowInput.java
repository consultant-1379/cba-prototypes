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
import com.ericsson.oss.mediation.util.netconf.operation.OperationType;

/**
 * 
 * @author xvaltda
 */
public class FlowInput {
    private OperationType operationType;
    private final NetconfSession netconfSession;
    private FlowSession flowSession;
    private Object Data;

    public FlowInput(final NetconfSession netconfSession, final OperationType operationType) {
        flowSession = new FlowSession();
        this.operationType = operationType;
        this.netconfSession = netconfSession;
    }

    public NetconfSession getNetconfSession() {
        return netconfSession;
    }

    public FlowSession getFlowSession() {
        return flowSession;
    }

    public void setFlowSession(final FlowSession flowSession) {
        this.flowSession = flowSession;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(final OperationType operationType) {
        this.operationType = operationType;
    }

    public Object getData() {
        return Data;
    }

    public void setData(final Object Data) {
        this.Data = Data;
    }

}

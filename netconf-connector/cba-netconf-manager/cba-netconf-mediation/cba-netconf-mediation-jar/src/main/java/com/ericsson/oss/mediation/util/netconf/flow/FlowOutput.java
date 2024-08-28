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

import com.ericsson.oss.mediation.util.netconf.api.NetconfResponse;
import com.ericsson.oss.mediation.util.netconf.vo.NetconfVo;
import com.ericsson.oss.mediation.util.netconf.vo.RpcReplyVo;
import com.ericsson.oss.mediation.util.netconf.vo.error.RpcErrorVo;

/**
 * 
 * @author xvaltda
 */
public class FlowOutput {

    private boolean error = false;
    private String errorMessage = "";
    private FlowErrorSeverity errorSeverity;
    private String componentId = "";
    private NetconfVo netconfVo = new NetconfVo();

    public void setNetconfVo(final NetconfVo netconfVo) {
        this.netconfVo = netconfVo;
    }

    public FlowErrorSeverity getErrorSeverity() {
        return errorSeverity;
    }

    public void setErrorSeverity(final FlowErrorSeverity errorSeverity) {
        this.errorSeverity = errorSeverity;
    }

    public boolean isError() {
        return error;
    }

    public void setError(final boolean error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(final String componentId) {
        this.componentId = componentId;
    }

    public NetconfResponse toNetconfResponse() {

        final NetconfResponse netconfResponse = new NetconfResponse();

        netconfResponse.setError(error);
        netconfResponse.setErrorMessage(errorMessage);

        if (netconfVo instanceof RpcReplyVo) {

            final RpcReplyVo rpcVo = (RpcReplyVo) netconfVo;
            netconfResponse.setData(rpcVo.getData());

        } else if (netconfVo instanceof RpcErrorVo) {
            String errorResponseInfo = "";
            netconfResponse.setError(true);

            final RpcErrorVo rpcError = (RpcErrorVo) netconfVo;
            for (final com.ericsson.oss.mediation.util.netconf.vo.error.Error error : rpcError.getErrors()) {
                errorResponseInfo+= error.toString();
            }

            
            netconfResponse.setErrorMessage(errorResponseInfo);
        }

        return netconfResponse;
    }
}

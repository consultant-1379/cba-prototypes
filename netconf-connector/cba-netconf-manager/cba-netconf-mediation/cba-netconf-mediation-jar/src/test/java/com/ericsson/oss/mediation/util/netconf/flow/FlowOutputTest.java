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
import com.ericsson.oss.mediation.util.netconf.vo.HelloVo;
import com.ericsson.oss.mediation.util.netconf.vo.RpcReplyVo;
import com.ericsson.oss.mediation.util.netconf.vo.error.Error;
import com.ericsson.oss.mediation.util.netconf.vo.error.RpcErrorVo;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * 
 * @author xvaltda
 */
public class FlowOutputTest {

    final String errorOutput = 
            "Error Message: error message\n" +
            "Error Info:\n" +
            "    bad format\n";
    final String BAD_FORMAT_ERROR = "bad format";
    final String RESPONSE_DATA = "rpc-reply data";

    @Test
    public void testBuildNetconfResponseRpcError() {

        final List<Error> errors = new ArrayList();
        final List<String> errorInfo = new ArrayList();
        final Error error = new Error();

        errorInfo.add(BAD_FORMAT_ERROR);

        error.setErrorInfo(errorInfo);
        error.setErrorMessage("error message");
        errors.add(error);

        final RpcErrorVo rpcErrorVo = new RpcErrorVo();
        rpcErrorVo.setErrors(errors);
        final FlowOutput output = new FlowOutput();

        rpcErrorVo.setErrorReceived(true);
        output.setError(false);
        output.setErrorMessage("");

        output.setNetconfVo(rpcErrorVo);

        NetconfResponse response = output.toNetconfResponse();

        System.out.println (response.getErrorMessage());
        assertTrue((errorOutput).equals(response.getErrorMessage()));
        assertTrue(response.isError());
    }

    @Test
    public void testBuildNetconfResponseRpcReply() {

        final RpcReplyVo rpcReplyVo = new RpcReplyVo();
        rpcReplyVo.setOk(true);
        rpcReplyVo.setData(RESPONSE_DATA);

        final FlowOutput output = new FlowOutput();

        output.setError(false);
        output.setErrorMessage("");

        output.setNetconfVo(rpcReplyVo);

        final NetconfResponse response = output.toNetconfResponse();

        assertFalse(response.isError());
        assertTrue(RESPONSE_DATA.equals(response.getData()));
        assertTrue("".equals(response.getErrorMessage()));
    }

    @Test
    public void testBuildNetconfResponseHelloScenario() {

        final HelloVo hello = new HelloVo();
        hello.setErrorReceived(false);
        hello.setSessionId("12");

        final FlowOutput output = new FlowOutput();

        output.setError(false);
        output.setErrorMessage("");

        output.setNetconfVo(hello);

        final NetconfResponse response = output.toNetconfResponse();

        assertFalse(response.isError());
        assertTrue("".equals(response.getData()));
        assertTrue("".equals(response.getErrorMessage()));
    }

    @Test
    public void testBuildNetconfResponseHelloScenarioWithError() {

        final HelloVo hello = new HelloVo();
        hello.setErrorReceived(true);
        hello.setSessionId("12");

        final FlowOutput output = new FlowOutput();

        output.setError(true);
        output.setErrorMessage("Error in Hello");

        output.setNetconfVo(hello);

        final NetconfResponse response = output.toNetconfResponse();

        assertTrue(response.isError());
        assertTrue("".equals(response.getData()));
        assertTrue("Error in Hello".equals(response.getErrorMessage()));
    }

}

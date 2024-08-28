/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.mediation.util.netconf.parser;

import com.ericsson.oss.mediation.util.netconf.vo.NetconfVo;
import com.ericsson.oss.mediation.util.netconf.vo.error.Error;
import com.ericsson.oss.mediation.util.netconf.vo.error.ErrorSeverity;
import com.ericsson.oss.mediation.util.netconf.vo.error.ErrorTag;
import com.ericsson.oss.mediation.util.netconf.vo.error.ErrorType;
import com.ericsson.oss.mediation.util.netconf.vo.error.RpcErrorVo;
import com.ericsson.oss.mediation.util.transport.api.TransportData;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author xdeevas
 * 
 */
public class RpcErrorParser extends DefaultHandler implements NetconfParser {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RpcErrorParser.class);
    /**
	 * 
	 */
    private static final String RPC_REPLY = "rpc-reply";
    /**
	 * 
	 */
    private static final String ERROR_INFO = "error-info";
    /**
	 * 
	 */
    private static final String RPC_ERROR = "rpc-error";
    boolean isError;
    RpcErrorVo rpcErrorVo;
    List<Error> errorList = new ArrayList<Error>();
    Error currentError;
    String currentErrorNode;
    boolean insideErrorInfo;
    List<String> errorInfoContent;

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes)
            throws SAXException {
        if (qName.equalsIgnoreCase(RPC_ERROR)) {
            logger.info("rpc error received, trying to parse");
            isError = true;
            rpcErrorVo = new RpcErrorVo();
            currentError = new Error();
        } else {
            currentErrorNode = qName;
            if (currentErrorNode.equals(ERROR_INFO)) {
                insideErrorInfo = true;
                errorInfoContent = new ArrayList<String>();
            }
        }
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        if (qName.equals(ERROR_INFO)) {
            insideErrorInfo = false;
            currentError.setErrorInfo(errorInfoContent);
        } else if (qName.equalsIgnoreCase(RPC_ERROR)) {
            final Error error = copyError();
            errorList.add(error);
        } else if (qName.equals(RPC_REPLY)) {
            if (rpcErrorVo != null) {
                rpcErrorVo.setErrors(errorList);
            }
        }
        currentErrorNode = "";
    }

    private Error copyError() {
        final Error error = new Error();
        error.setErrorAppTag(currentError.getErrorAppTag());
        error.setErrorInfo(currentError.getErrorInfo());
        error.setErrorMessage(currentError.getErrorMessage());
        error.setErrorPath(currentError.getErrorPath());
        error.setErrorSeverity(currentError.getErrorSeverity());
        error.setErrorTag(currentError.getErrorTag());
        error.setErrortype(currentError.getErrortype());
        return error;
    }

    @Override
    public void characters(final char ch[], final int start, final int length) throws SAXException {
        final String errorDetail = new String(ch, start, length);
        switch (currentErrorNode) {
        case "error-type":
            if (notNullorEmpty(errorDetail)) {
                currentError.setErrortype(ErrorType.valueOf(errorDetail));
                break;
            }
        case "error-tag":
            if (notNullorEmpty(errorDetail)) {
                final ErrorTag errorTagbyValue = ErrorTag.getErrorTagbyValue(errorDetail);
                currentError.setErrorTag(errorTagbyValue);
                break;
            }
        case "error-severity":
            if (notNullorEmpty(errorDetail)) {
                currentError.setErrorSeverity(ErrorSeverity.valueOf(errorDetail));
                break;
            }
        case ERROR_INFO:
            //			errorResponse.setErrorInfo(errorDetail);
            break;
        case "error-path":
            currentError.setErrorPath(errorDetail);
            break;
        case "error-app-tag":
            currentError.setErrorAppTag(errorDetail);
            break;
        case "error-message":
            currentError.setErrorMessage(errorDetail);
            break;
        default:
            if (insideErrorInfo && notNullorEmpty(currentErrorNode) && notNullorEmpty(errorDetail)) {
                errorInfoContent.add(currentErrorNode + ": " + errorDetail);
            }
            break;
        }
    }

    private boolean notNullorEmpty(final String errorDetail) {
        return (errorDetail != null) && !errorDetail.trim().isEmpty();
    }

    /**
     * @return the errorResponse
     */
    public RpcErrorVo getErrorResponse() {
        return rpcErrorVo;
    }
    
    @Override
    public NetconfVo parse(final TransportData data) throws ParserException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

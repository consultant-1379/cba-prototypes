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
package com.ericsson.oss.mediation.util.netconf.vo.error;

import java.util.List;

/**
 * @author xdeevas
 * 
 */
public class Error {

    ErrorType errortype;
    ErrorTag errorTag;
    ErrorSeverity errorSeverity;
    String errorAppTag;
    String errorPath;
    String errorMessage ="";
    List<String> errorInfoContent;

    /**
     * @return the errortype
     */
    public ErrorType getErrortype() {
        return errortype;
    }

    /**
     * @return the errorTag
     */
    public ErrorTag getErrorTag() {
        return errorTag;
    }

    /**
     * @return the errorSeverity
     */
    public ErrorSeverity getErrorSeverity() {
        return errorSeverity;
    }

    /**
     * @return the errorAppTag
     */
    public String getErrorAppTag() {
        return errorAppTag;
    }

    /**
     * @return the errorPath
     */
    public String getErrorPath() {
        return errorPath;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @return the errorInfo
     */
    public List<String> getErrorInfo() {
        return errorInfoContent;
    }

    /**
     * @param errortype
     *            the errortype to set
     */
    public void setErrortype(final ErrorType errortype) {
        this.errortype = errortype;
    }

    /**
     * @param errorTag
     *            the errorTag to set
     */
    public void setErrorTag(final ErrorTag errorTag) {
        this.errorTag = errorTag;
    }

    /**
     * @param errorSeverity
     *            the errorSeverity to set
     */
    public void setErrorSeverity(final ErrorSeverity errorSeverity) {
        this.errorSeverity = errorSeverity;
    }

    /**
     * @param errorAppTag
     *            the errorAppTag to set
     */
    public void setErrorAppTag(final String errorAppTag) {
        this.errorAppTag = errorAppTag;
    }

    /**
     * @param errorPath
     *            the errorPath to set
     */
    public void setErrorPath(final String errorPath) {
        this.errorPath = errorPath;
    }

    /**
     * @param errorMessage
     *            the errorMessage to set
     */
    public void setErrorMessage(final String errorMessage) {
        if (errorMessage != null && !("".equals(errorMessage.trim()))) {
            this.errorMessage = this.errorMessage+errorMessage;
        }
    }

    /**
     * @param errorInfo
     *            the errorInfo to set
     */
    public void setErrorInfo(final List<String> errorInfo) {
        this.errorInfoContent = errorInfo;
    }
    
    @Override
     public String toString (){
         String errorToString = "";
         if (errortype != null){
             errorToString+="Error Type: "+errortype +"\n";
         }
         
         if (errorTag != null){
             errorToString += "Error Tag: "+errorTag+"\n";
         }
         if (errorSeverity != null){
             errorToString+= "Error Severity: "+errorSeverity+"\n";
         }
         if (errorAppTag != null){
             errorToString+= "Error App Tag: "+errorAppTag+"\n";
         }
         if (errorPath != null){
             errorToString+="Error Path: "+errorPath+"\n";
         }
         if (errorMessage != null){
             errorToString+= "Error Message: "+errorMessage+"\n";
         }

         
         
         
         if (errorInfoContent != null){
             
         errorToString+="Error Info:\n";
         
         for (String info :errorInfoContent){
             errorToString+="    "+info+"\n";
         }
         }
         
         return errorToString.isEmpty()?"no- info":errorToString;
     }

}

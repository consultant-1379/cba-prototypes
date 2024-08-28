/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.testProjects;

import com.ericsson.oss.itpf.modeling.annotation.EModel;
import com.ericsson.oss.itpf.modeling.annotation.EModelAttribute;
import com.ericsson.oss.itpf.modeling.annotation.eventtype.EventAttribute;
import com.ericsson.oss.itpf.modeling.annotation.eventtype.EventTypeDefinition;
import com.ericsson.oss.mediation.sdk.event.MediationTaskRequest;

/**   
 * @author echhrah
 * 
 */
@EModel(namespace = "global", name = "SampMultiStepOutputMedTaskReq", version = "1.0.0", 
description = "Mediation task request for executing flows with multiple steps and passing value")
@EventTypeDefinition(channelUrn = "//global/ClusteredEventBasedMediationClient")
public class SampMultiStepOutputMedTaskReq extends MediationTaskRequest {

    private static final long serialVersionUID = 1L;
    
    @EModelAttribute(description = "test field")
    @EventAttribute
    private String testField;

     /**
	 * @return the testField
	 */
    public String getTestField() {
        return testField;
    }

	/**
	 * @param testField the testField to set
	 */
    public void setTestField(String testField) {
        this.testField = testField;
    }

	/**
     * 
     */
    public SampMultiStepOutputMedTaskReq() {

    }

    @Override
    public String toString() {
        return "SampMultiStepOutputMedTaskReq [testField=" + testField + "[nodeAddress=" + getNodeAddress() + ", "
				+ "jobId=" + getJobId() + ", protocolInfo=" + getProtocolInfo() + ", clientType="
                + getClientType() + "]";
    }

}  
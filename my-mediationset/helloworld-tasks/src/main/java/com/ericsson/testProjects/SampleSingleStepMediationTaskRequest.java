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
import com.ericsson.oss.itpf.modeling.annotation.eventtype.EventTypeDefinition;
import com.ericsson.oss.mediation.sdk.event.MediationTaskRequest;

/**
 * @author ecaoodo  
 *
 */
@EModel(namespace = "global", name = "SampleSingleStepMediationTaskRequest", version = "1.0.0", 
description = "Mediation task request for executing flow with a single step")
@EventTypeDefinition(channelUrn = "//global/ClusteredEventBasedMediationClient")
public class SampleSingleStepMediationTaskRequest extends MediationTaskRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public SampleSingleStepMediationTaskRequest() {

    }

} 

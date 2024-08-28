/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2015
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.models.moci.mediation.handlers.executors;

import com.ericsson.oss.models.moci.mediation.handlers.util.RequestData;

/**
 * The Interface MociExecutor.
 */
public interface CbaExecutor {

    /**
     * Execute.
     *
     * @return the object
     */
    Object execute();


    /**
     * Provides header data received to the executors
     *
     * @param requestData
     *          Data from request
     *
     */
    void init(RequestData requestData);

    /**
     * Meant to clear all instance variables if any.
     */
    void destroy();

}

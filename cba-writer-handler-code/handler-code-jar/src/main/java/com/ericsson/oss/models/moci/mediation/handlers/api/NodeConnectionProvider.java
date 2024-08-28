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
package com.ericsson.oss.models.moci.mediation.handlers.api;

import com.ericsson.oss.mediation.network.api.MociCMConnectionProvider;
import com.ericsson.oss.mediation.network.api.exception.MociConnectionProviderException;

/**
 * Interface definition to get a node connection.
 */
public interface NodeConnectionProvider {

    /**
     * Getting MociCMConnectionProvider.
     *
     * @return MociCMConnectionProvider with access to the node.
     * @throws MociConnectionProviderException
     *             when the creation of the connection failed
     */
    MociCMConnectionProvider getConnection() throws MociConnectionProviderException;

}

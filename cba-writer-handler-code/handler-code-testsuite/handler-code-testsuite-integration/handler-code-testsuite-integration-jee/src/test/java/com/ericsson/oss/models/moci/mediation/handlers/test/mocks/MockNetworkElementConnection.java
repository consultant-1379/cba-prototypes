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
package com.ericsson.oss.models.moci.mediation.handlers.test.mocks;

import javax.ejb.EJB;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import com.ericsson.oss.mediation.network.api.MociCMConnectionProvider;
import com.ericsson.oss.mediation.network.api.exception.MociConnectionProviderException;
import com.ericsson.oss.models.moci.mediation.handlers.api.NodeConnectionProvider;
import com.ericsson.oss.models.moci.mediation.handlers.test.util.ConnectionProviderVerifier;

/**
 * @author ecasdia
 */
@Alternative
public class MockNetworkElementConnection implements NodeConnectionProvider {

    @EJB(lookup = ConnectionProviderVerifier.CONNECTION_COUNTER_VERIFIER_JNDI)
    private ConnectionProviderVerifier connectionCounterVerifier;

    @Inject
    MockMociCMConnectionProvider mockMociCMConnectionProvider;

    @Override
    public MociCMConnectionProvider getConnection() throws MociConnectionProviderException {
        connectionCounterVerifier.setGetConnectionInvoked();
        return mockMociCMConnectionProvider;
    }

}

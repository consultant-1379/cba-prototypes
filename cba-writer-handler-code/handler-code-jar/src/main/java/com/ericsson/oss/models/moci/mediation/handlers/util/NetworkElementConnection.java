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
package com.ericsson.oss.models.moci.mediation.handlers.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.ericsson.oss.mediation.network.api.MociCMConnectionProvider;
import com.ericsson.oss.mediation.network.api.exception.MociConnectionProviderException;
import com.ericsson.oss.models.moci.mediation.handlers.api.NodeConnectionProvider;

/**
 * Provides Node connection using MociCMConnectionProvider.
 *
 * @author ecasdia
 */
public class NetworkElementConnection implements NodeConnectionProvider {

    /*
     * (non-Javadoc)
     *
     * @see
     * com.ericsson.oss.models.moci.mediation.handlers.api.NodeConnectionProvider
     * #getConnection()
     */
    @Override
    public MociCMConnectionProvider getConnection() throws MociConnectionProviderException {
        Context ctx;
        MociCMConnectionProvider connection;
        try {
            ctx = new InitialContext();
            connection = (MociCMConnectionProvider) ctx.lookup(MociCMConnectionProvider.VERSION_INDEPENDENT_JNDI_NAME);
        } catch (final NamingException e) {
            throw new MociConnectionProviderException(e);
        }
        return connection;
    }

}
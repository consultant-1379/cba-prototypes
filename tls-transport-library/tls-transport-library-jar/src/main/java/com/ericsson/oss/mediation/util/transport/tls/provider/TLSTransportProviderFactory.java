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
package com.ericsson.oss.mediation.util.transport.tls.provider;

import com.ericsson.oss.mediation.transport.api.TransportManagerCI;
import com.ericsson.oss.mediation.transport.api.provider.TransportProvider;
import com.ericsson.oss.mediation.transport.api.provider.factory.TransportProviderFactory;

public class TLSTransportProviderFactory implements TransportProviderFactory {

    // TODO : remove this field the end pattern should be provided by TransporCI
    public static final String NETCONF_END_PATTERN = "]]>]]>";

    private TransportProvider sessionProvider = null;

    @Override
    public TransportProvider createTransportProvider(final TransportManagerCI transportManagerCI) {

        sessionProvider = new TLSTransportProviderImpl(transportManagerCI.getHostname(), transportManagerCI.getPort(), NETCONF_END_PATTERN);

        return sessionProvider;
    }
}

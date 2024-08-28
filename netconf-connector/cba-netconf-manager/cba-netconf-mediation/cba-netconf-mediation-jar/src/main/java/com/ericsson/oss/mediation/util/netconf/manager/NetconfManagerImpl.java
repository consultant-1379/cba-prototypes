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
package com.ericsson.oss.mediation.util.netconf.manager;

import com.ericsson.oss.mediation.util.netconf.api.Capability;
import com.ericsson.oss.mediation.util.netconf.api.Datastore;
import com.ericsson.oss.mediation.util.netconf.api.Filter;
import com.ericsson.oss.mediation.util.netconf.api.NetconfConnectionStatus;
import com.ericsson.oss.mediation.util.netconf.api.NetconfManager;
import com.ericsson.oss.mediation.util.netconf.api.NetconfResponse;
import com.ericsson.oss.mediation.util.netconf.api.SessionState;
import com.ericsson.oss.mediation.util.netconf.api.exception.NetconfManagerException;
import com.ericsson.oss.mediation.util.netconf.context.NetconfSession;
import com.ericsson.oss.mediation.util.transport.api.TransportManager;
import java.util.Collection;
import java.util.Map;

public class NetconfManagerImpl implements NetconfManager {

    private final NetconfSession netconfSession;

    @Override
    public NetconfResponse connect() throws NetconfManagerException {
        netconfSession.connect();
        return netconfSession.getNetconfResponse();
    }

    @Override
    public NetconfResponse disconnect() throws NetconfManagerException {
        netconfSession.disconnect();
        return netconfSession.getNetconfResponse();

    }

    @Override
    public NetconfConnectionStatus getStatus() {
        return netconfSession.getStatus();
    }

    @Override
    public NetconfResponse get() throws NetconfManagerException {
        return get(null);
    }

    @Override
    public NetconfResponse get(final Filter filter) throws NetconfManagerException {
        // add logs
        netconfSession.get(filter);
        return netconfSession.getNetconfResponse();
    }

    @Override
    public NetconfResponse getConfig() throws NetconfManagerException {
        return getConfig(Datastore.RUNNING, null);
    }

    @Override
    public NetconfResponse getConfig(final Datastore source) throws NetconfManagerException {
        return getConfig(source, null);
    }

    @Override
    public NetconfResponse getConfig(final Datastore source, final Filter filter) throws NetconfManagerException {
        netconfSession.getConfig(source, filter);
        return netconfSession.getNetconfResponse();
    }

    NetconfManagerImpl(final TransportManager transportManager, final Map<String, Object> configProperties)
            throws NetconfManagerException {
        netconfSession = new NetconfSession(transportManager, configProperties);
    }

    @Override
    public NetconfResponse killSession(final String sessionId) throws NetconfManagerException {
        netconfSession.killSession(sessionId);
        return netconfSession.getNetconfResponse();
    }

    @Override
    public Collection<Capability> getAllActiveCapabilities() {
        return netconfSession.getNetconfSessionCapabilities().getActiveCapabilities();
    }

    @Override
    public String getSessionId() {
        return netconfSession.getSessionId();
    }

    @Override
    public SessionState getSessionState() {
        return netconfSession.getSessionState();
    }

}

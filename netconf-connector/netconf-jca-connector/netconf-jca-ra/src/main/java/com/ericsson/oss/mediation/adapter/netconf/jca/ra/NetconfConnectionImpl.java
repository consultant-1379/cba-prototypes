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

package com.ericsson.oss.mediation.adapter.netconf.jca.ra;
       
import com.ericsson.oss.mediation.adapter.netconf.jca.api.NetconfConnection;
import com.ericsson.oss.mediation.util.netconf.api.Capability;
import com.ericsson.oss.mediation.util.netconf.api.Datastore;
import com.ericsson.oss.mediation.util.netconf.api.Filter;
import com.ericsson.oss.mediation.util.netconf.api.NetconfConnectionStatus;
import com.ericsson.oss.mediation.util.netconf.api.NetconfResponse;
import com.ericsson.oss.mediation.util.netconf.api.SessionState;
import com.ericsson.oss.mediation.util.netconf.api.exception.NetconfManagerException;
import java.util.Collection;

/**
 * 
 * @author xvaltda
 */
public class NetconfConnectionImpl implements NetconfConnection {

    
    private NetconfManagedConnection nmc;
    
    public NetconfConnectionImpl (final NetconfManagedConnection nmc) {
        this.nmc = nmc;
    }
    
    public NetconfResponse connect() throws NetconfManagerException {
        return nmc.connect();
    }

    public NetconfResponse disconnect() throws NetconfManagerException {
        return nmc.disconnect();
    }

    public NetconfConnectionStatus getStatus() {
        return nmc.getStatus();
    }

    public NetconfResponse get() throws NetconfManagerException {
        return nmc.get();
    }

    public NetconfResponse get(Filter filter) throws NetconfManagerException {
        return nmc.get(filter);
    }

    public NetconfResponse getConfig() throws NetconfManagerException {
        return nmc.getConfig();
    }

    public NetconfResponse getConfig(Datastore dtstr)
            throws NetconfManagerException {
        return nmc.getConfig( dtstr);
    }

    public NetconfResponse getConfig(Datastore dtstr, Filter filter)
            throws NetconfManagerException {
        return nmc.getConfig(dtstr, filter);
    }

    public NetconfResponse killSession(String sessionId)
            throws NetconfManagerException {
        return nmc.killSession(sessionId);
    }

    public Collection<Capability> getAllActiveCapabilities() {
        return nmc.getAllActiveCapabilities();
    }

    public String getSessionId() {
        return nmc.getSessionId();
    }

    @Override
    public SessionState getSessionState() {
        return nmc.getSessionState();
    }

  

}

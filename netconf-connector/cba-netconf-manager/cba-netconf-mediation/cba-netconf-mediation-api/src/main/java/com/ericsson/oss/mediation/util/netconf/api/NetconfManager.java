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

package com.ericsson.oss.mediation.util.netconf.api;

import com.ericsson.oss.mediation.util.netconf.api.exception.NetconfManagerException;
import java.util.Collection;

public interface NetconfManager {

    NetconfResponse connect() throws NetconfManagerException;

    NetconfResponse disconnect() throws NetconfManagerException;

    NetconfConnectionStatus getStatus();

    NetconfResponse get() throws NetconfManagerException;

    NetconfResponse get(Filter filter) throws NetconfManagerException;

    NetconfResponse getConfig() throws NetconfManagerException;

    NetconfResponse getConfig(Datastore source) throws NetconfManagerException;

    NetconfResponse getConfig(Datastore source, Filter filter) throws NetconfManagerException;

    NetconfResponse killSession(String sessionId) throws NetconfManagerException;

    Collection<Capability> getAllActiveCapabilities();

    String getSessionId();
    
    SessionState getSessionState ();
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ericsson.oss.mediation.util.netconf.manager;

import java.util.Map;

import com.ericsson.oss.mediation.util.netconf.api.NetconfManager;
import com.ericsson.oss.mediation.util.netconf.api.exception.NetconfManagerException;
import com.ericsson.oss.mediation.util.transport.api.TransportManager;

/**
 * 
 * @author xvaltda
 */
public abstract class NetconfManagerFactory {

    public static NetconfManager createNetconfManager(final TransportManager transportManager,
            final Map<String, Object> configProperties) throws NetconfManagerException {
        if (configProperties == null) {
            throw new NetconfManagerException("Configuration properties are not defined.");
        }
        return new NetconfManagerImpl(transportManager, configProperties);

    }
}

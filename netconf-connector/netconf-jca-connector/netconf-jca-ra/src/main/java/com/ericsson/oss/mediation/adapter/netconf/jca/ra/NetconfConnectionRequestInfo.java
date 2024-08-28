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

import com.ericsson.oss.mediation.util.transport.api.TransportManager;
import java.util.Map;
import javax.resource.spi.ConnectionRequestInfo;

/**
 * 
 * @author xvaltda
 */
public class NetconfConnectionRequestInfo implements ConnectionRequestInfo{

    private final TransportManager transportManager;
    private final Map<String, Object> configProperties;
    
    public NetconfConnectionRequestInfo (final TransportManager transportManager, final Map<String, Object> configProperties){
        this.transportManager = transportManager; 
        this.configProperties = configProperties;
    }
    
    public Map<String, Object> getConfigProperties (){
        return configProperties;
    }
    
    public TransportManager getTransportManager (){
        return transportManager;
    }
    
    @Override
    public boolean equals (final Object object) {
        
        if (this == object) {
            return true;
        }
        
        if (object == null || !(object instanceof TransportManager)){
            return false;  
        }
        // implement getProtocolDescripcion
        
        // implement to configProperties
        
        return true;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((transportManager == null) ? 0 : transportManager.hashCode());
        return result;
    }
    
}

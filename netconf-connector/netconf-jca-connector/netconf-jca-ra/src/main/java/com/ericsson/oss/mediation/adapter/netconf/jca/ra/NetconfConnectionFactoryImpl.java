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
import com.ericsson.oss.mediation.adapter.netconf.jca.api.NetconfConnectionFactory;
import com.ericsson.oss.mediation.util.transport.api.TransportManager;
import java.util.Map;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author xvaltda
 */
public class NetconfConnectionFactoryImpl implements NetconfConnectionFactory {

    private static final Logger LOG = LoggerFactory.getLogger(NetconfConnectionFactoryImpl.class);
    private Reference reference;
    
    private NetconfManagedConnectionFactory cmcf;
    private ConnectionManager connectionManager;
    
    public NetconfConnectionFactoryImpl(final NetconfManagedConnectionFactory cmcf, final ConnectionManager connectionManager) {
        this.cmcf = cmcf;
        this.connectionManager = connectionManager;
    }
    
    @Override
    public NetconfConnection createNetconfConnection(TransportManager transportManager, Map<String, Object> configProperties) throws ResourceException {
        LOG.debug("creating the NetconfConnection");
        NetconfConnectionRequestInfo requestInfo = new NetconfConnectionRequestInfo(transportManager,configProperties);
        return (NetconfConnection) this.connectionManager.allocateConnection(this.cmcf,requestInfo);
    }

    @Override
    public void setReference(Reference reference) {
        this.reference = reference;
    }

    @Override
    public Reference getReference() throws NamingException {
        return reference;
    }

}

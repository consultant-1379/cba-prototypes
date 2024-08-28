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
import com.ericsson.oss.mediation.util.netconf.api.SessionState;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionDefinition;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.security.auth.Subject;

/**
 * 
 * @author xvaltda
 */
@ConnectionDefinition(
        connectionFactory = NetconfConnectionFactory.class, connectionFactoryImpl = NetconfConnectionFactoryImpl.class,
        connection = NetconfConnection.class, connectionImpl = NetconfConnectionImpl.class)
public class NetconfManagedConnectionFactory implements
        ManagedConnectionFactory {
    
    private PrintWriter logWriter;

    public Object createConnectionFactory(ConnectionManager cxManager)
            throws ResourceException {
        return new NetconfConnectionFactoryImpl(this, cxManager);
    }

    public Object createConnectionFactory() throws ResourceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
 
    public ManagedConnection createManagedConnection(Subject subject,
            ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        return new NetconfManagedConnection(this, cxRequestInfo);
    }

    public ManagedConnection matchManagedConnections(final Set connectionSet,
            final Subject subject, final ConnectionRequestInfo cxRequestInfo)
            throws ResourceException {
        final NetconfConnectionRequestInfo netconfRequestInfo = (NetconfConnectionRequestInfo)cxRequestInfo;
        
        final Iterator iterator = connectionSet.iterator();
        
        
        while (iterator.hasNext()){
                final ManagedConnection managedConnection = (ManagedConnection)iterator.next();
                
        if (managedConnection instanceof NetconfManagedConnection) {
            final NetconfManagedConnection netconfManagedConnection = (NetconfManagedConnection) managedConnection;
            boolean isConnectionStateNotBusy = false;
            if (netconfManagedConnection.getNetconfManager().getSessionState() == SessionState.READY ||
                netconfManagedConnection.getNetconfManager().getSessionState() == SessionState.IDLE) {
                isConnectionStateNotBusy = true;
            }
            
            if (isConnectionStateNotBusy && (netconfManagedConnection.getNetconfConnectionRequestInfo().getTransportManager().getProtocolType().equals(
                netconfRequestInfo.getTransportManager().getProtocolType()) &&
                netconfManagedConnection.getNetconfConnectionRequestInfo().getTransportManager().getTransportManagerCI().getHostname().equals(
                netconfRequestInfo.getTransportManager().getTransportManagerCI().getHostname()) &&
                netconfManagedConnection.getNetconfConnectionRequestInfo().getTransportManager().getTransportManagerCI().getPort()==
                netconfRequestInfo.getTransportManager().getTransportManagerCI().getPort())) {
               
                return managedConnection;
            
              }
        }        

        }
        
        return null;

        
    }

    public void setLogWriter(PrintWriter out) throws ResourceException {
        this.logWriter = out; 
    }

    public PrintWriter getLogWriter() throws ResourceException {
        return logWriter;
    }

    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 12;//prime * result + ((resourceAdapter == null) ? 0 : resourceAdapter.hashCode());
        return result;
    }
    
    @Override
    public boolean equals (Object obj){
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        return true;
    }
}

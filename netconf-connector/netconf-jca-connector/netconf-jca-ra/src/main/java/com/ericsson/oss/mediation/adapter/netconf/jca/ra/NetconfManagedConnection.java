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
import com.ericsson.oss.mediation.util.netconf.api.NetconfManager;
import com.ericsson.oss.mediation.util.netconf.api.NetconfResponse;
import com.ericsson.oss.mediation.util.netconf.api.SessionState;
import com.ericsson.oss.mediation.util.netconf.api.exception.NetconfManagerException;
import com.ericsson.oss.mediation.util.netconf.manager.NetconfManagerFactory;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionEventListener;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.LocalTransaction;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionMetaData;
import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author xvaltda
 */
public class NetconfManagedConnection implements ManagedConnection {

    
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(NetconfManagedConnection.class);
    
    private PrintWriter logWriter;
    private Object connection;
    private NetconfConnectionRequestInfo requestInfo;
    private NetconfManager netconfManager;
    private NetconfManagedConnectionFactory managedConnectionFactory;
    private NetconfManagedConnectionMetaData netconfManagedConnectionMetaData;
    private List<ConnectionEventListener> listeners;
    
    public NetconfManagedConnection(final NetconfManagedConnectionFactory mcf, final ConnectionRequestInfo requestInfo) {
        log.debug("NetconfManagedConnection constructor called...");
        this.managedConnectionFactory = mcf;
        this.requestInfo = (NetconfConnectionRequestInfo) requestInfo;
        this.netconfManagedConnectionMetaData = new NetconfManagedConnectionMetaData();
        this.listeners = Collections.synchronizedList(new ArrayList<ConnectionEventListener>(1));
        
    }
    
    public NetconfManager getNetconfManager (){
        return netconfManager;
    }
    
    public Object getConnection(Subject subject,
            ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        
        try {
        
            final NetconfConnection netconfConnection = new NetconfConnectionImpl(this);
        
            this.requestInfo = (NetconfConnectionRequestInfo) cxRequestInfo;
            this.netconfManager = NetconfManagerFactory.createNetconfManager(requestInfo.getTransportManager(), requestInfo.getConfigProperties());
            this.connection = netconfConnection;
        
        } catch (NetconfManagerException ex) {
            Logger.getLogger(NetconfManagedConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return this.connection;
    }

    public NetconfConnectionRequestInfo getNetconfConnectionRequestInfo(){
        
        return this.requestInfo;
    }
    
    public void destroy() throws ResourceException {
        log.debug("destroy() method called");
        this.requestInfo = null;
        this.netconfManagedConnectionMetaData = null;
        this.connection = null;    
    }

    public void cleanup() throws ResourceException {
        log.debug("cleanup() method called");
        this.requestInfo = null;
        this.netconfManagedConnectionMetaData = null;
        this.connection = null;
    }

    public void associateConnection(Object connection) throws ResourceException {
         log.debug("associateConnection()");
        if (connection == null) {
            throw new ResourceException("Attempting to associate null connection handle to managed connection");
        }
        if (!(connection instanceof NetconfConnection)) {
            throw new ResourceException("Wrong connection handle type, connection handle is not instance of NetconfConnection");
        }
        this.connection = connection;
    }

    public void addConnectionEventListener(ConnectionEventListener listener) {
        log.debug("addConnectionEventListener()");
        if (listener == null) {
            throw new IllegalArgumentException("Connection Listener is Null");
        }
        this.listeners.add(listener);
    }

    public void removeConnectionEventListener(ConnectionEventListener listener) {
        log.debug("removeConnectionEventListener()");
        if (listener == null) {
            throw new IllegalArgumentException("Connection Listener is Null");
        }
        this.listeners.remove(listener);
    
    }

    public XAResource getXAResource() throws ResourceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public LocalTransaction getLocalTransaction() throws ResourceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ManagedConnectionMetaData getMetaData() throws ResourceException {
        return netconfManagedConnectionMetaData;
    }

    public void setLogWriter(PrintWriter out) throws ResourceException {
        this.logWriter = out;
    }

    public PrintWriter getLogWriter() throws ResourceException {
        return logWriter;
    }
    
    
    
    
    
    public NetconfResponse connect() throws NetconfManagerException {
        return netconfManager.connect();
    }

    public NetconfResponse disconnect() throws NetconfManagerException {
        return netconfManager.disconnect();
    }

    public NetconfConnectionStatus getStatus() {
       return netconfManager.getStatus();
    }

    public NetconfResponse get() throws NetconfManagerException {
        return netconfManager.get();
    }

    public NetconfResponse get(Filter filter) throws NetconfManagerException {
        return netconfManager.get(filter);
    }

    public NetconfResponse getConfig() throws NetconfManagerException {
        return netconfManager.getConfig();
    }

    public NetconfResponse getConfig(Datastore dtstr)
            throws NetconfManagerException {
        return netconfManager.getConfig(dtstr);
    }

    public NetconfResponse getConfig(Datastore dtstr, Filter filter)
            throws NetconfManagerException {
       return netconfManager.getConfig(dtstr, filter);
    }

    public NetconfResponse killSession(String sessionId)
            throws NetconfManagerException {
        return netconfManager.killSession(sessionId);
    }

    public Collection<Capability> getAllActiveCapabilities() {
        return netconfManager.getAllActiveCapabilities();
    }

    public String getSessionId() {
        return netconfManager.getSessionId();
    }
    
    public SessionState getSessionState() {
        return netconfManager.getSessionState();
    }


}

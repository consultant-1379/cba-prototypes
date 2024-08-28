/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.mediation.adapter.ssh;

import java.io.PrintWriter;
import java.util.Set;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionDefinition;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterAssociation;
import javax.security.auth.Subject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.adapter.ssh.api.SSHConnection;
import com.ericsson.oss.mediation.adapter.ssh.api.SSHConnectionFactory;
import com.ericsson.oss.mediation.adapter.ssh.api.SSHSessionRequest;

/**
 * SSHManagedConnectionFactory
 * 
 * @author eilanag
 */
@ConnectionDefinition(connectionFactory = SSHConnectionFactory.class, connectionFactoryImpl = SSHConnectionFactoryImpl.class, connection = SSHConnection.class, connectionImpl = SSHConnectionImpl.class)
public class SSHManagedConnectionFactory implements ManagedConnectionFactory, ResourceAdapterAssociation {

    /** The serial version UID */
    private static final long serialVersionUID = 1L;

    /** The logger */
    private static Logger log = LoggerFactory.getLogger(SSHManagedConnectionFactory.class);

    /** The resource adapter */
    private ResourceAdapter resourceAdapter;

    /** The logwriter */
    private PrintWriter logwriter;

    /**
     * Creates a Connection Factory instance.
     * 
     * @param cxManager
     *            ConnectionManager to be associated with created EIS connection factory instance
     * @return EIS-specific Connection Factory instance or javax.resource.cci.ConnectionFactory instance
     * @throws ResourceException
     *             Generic exception
     */
    @Override
    public Object createConnectionFactory(final ConnectionManager cxManager) throws ResourceException {
        log.info("createConnectionFactory()");
        return new SSHConnectionFactoryImpl(this, cxManager);
    }

    /**
     * Creates a Connection Factory instance.
     * 
     * @return EIS-specific Connection Factory instance or javax.resource.cci.ConnectionFactory instance
     * @throws ResourceException
     *             Generic exception
     */
    @Override
    public Object createConnectionFactory() throws ResourceException {
        throw new ResourceException("This resource adapter doesn't support non-managed environments");
    }

    /**
     * Creates a new physical connection to the underlying EIS resource manager.
     * 
     * @param subject
     *            Caller's security information
     * @param cxRequestInfo
     *            Additional resource adapter specific connection request information
     * @throws ResourceException
     *             generic exception
     * @return ManagedConnection instance
     */
    @Override
    public ManagedConnection createManagedConnection(final Subject subject, final ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        log.info("createManagedConnection()");
        return new SSHManagedConnection(this, ((SSHSessionRequest)cxRequestInfo).getIpAddress());
    }

    /**
     * Returns a matched connection from the candidate set of connections.
     * 
     * @param connectionSet
     *            Candidate connection set
     * @param subject
     *            Caller's security information
     * @param cxRequestInfo
     *            Additional resource adapter specific connection request information
     * @throws ResourceException
     *             generic exception
     * @return ManagedConnection if resource adapter finds an acceptable match otherwise null
     */
    @Override
    @SuppressWarnings("rawtypes")
    public ManagedConnection matchManagedConnections(final Set connectionSet, final Subject subject, final ConnectionRequestInfo cxRequestInfo)
            throws ResourceException {
        log.debug("matchManagedConnections()");
        ManagedConnection result = null;

        for(final Object mgdConnection : connectionSet) {
            final ManagedConnection managedConnection = (ManagedConnection) mgdConnection;
            if (managedConnection instanceof SSHManagedConnection) {
                final SSHConnectionImpl sshConx = ((SSHManagedConnection) managedConnection).getConnection();
                if(sshConx != null) {
                    if(sshConx.getReqInfo().equals( ((SSHSessionRequest) cxRequestInfo))) {
                        log.debug("matchManagedConnections() match found.");
                        result = managedConnection;
                        break;
                    }
                }
                
            }

        }
        return result;
    }

    /**
     * Get the log writer for this ManagedConnectionFactory instance.
     * 
     * @return PrintWriter
     * @throws ResourceException
     *             generic exception
     */
    @Override
    public PrintWriter getLogWriter() throws ResourceException {
        log.debug("getLogWriter()");
        return logwriter;
    }

    /**
     * Set the log writer for this ManagedConnectionFactory instance.
     * 
     * @param out
     *            PrintWriter - an out stream for error logging and tracing
     * @throws ResourceException
     *             generic exception
     */
    @Override
    public void setLogWriter(final PrintWriter out) throws ResourceException {
        log.debug("setLogWriter()");
        logwriter = out;
    }

    /**
     * Get the resource adapter
     * 
     * @return The handle
     */
    @Override
    public ResourceAdapter getResourceAdapter() {
        log.debug("getResourceAdapter()");
        return resourceAdapter;
    }

    /**
     * Set the resource adapter
     * 
     * @param resourceAdapter
     *            The handle
     */
    @Override
    public void setResourceAdapter(final ResourceAdapter resourceAdapter) {
        log.debug("setResourceAdapter()");
        this.resourceAdapter = resourceAdapter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((resourceAdapter == null) ? 0 : resourceAdapter.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SSHManagedConnectionFactory other = (SSHManagedConnectionFactory) obj;
        if (resourceAdapter == null) {
            if (other.resourceAdapter != null) {
                return false;
            }
        } else if (!resourceAdapter.equals(other.resourceAdapter)) {
            return false;
        }
        return true;
    }

}

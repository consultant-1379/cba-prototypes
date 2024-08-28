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


import javax.naming.NamingException;
import javax.naming.Reference;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.adapter.ssh.api.*;

/**
 * SSHConnectionFactoryImpl
 * 
 * @author eilanag
 */
public class SSHConnectionFactoryImpl implements SSHConnectionFactory {
    /** The serial version UID */
    private static final long serialVersionUID = 1L;

    /** The logger */
    private static Logger log =  LoggerFactory.getLogger(SSHConnectionFactoryImpl.class.getName());

    /** Reference */
    private Reference reference;

    /** ManagedConnectionFactory */
    private SSHManagedConnectionFactory mcf;

    /** ConnectionManager */
    private ConnectionManager connectionManager;

    /**
     * Default constructor
     */
    public SSHConnectionFactoryImpl() {

    }

    /**
     * Default constructor
     * 
     * @param mcf
     *            ManagedConnectionFactory
     * @param cxManager
     *            ConnectionManager
     */
    public SSHConnectionFactoryImpl(final SSHManagedConnectionFactory mcf, final ConnectionManager cxManager) {
        this.mcf = mcf;
        this.connectionManager = cxManager;
    }

    /**
     * Get connection from factory
     * 
     * @return SSHConnection instance
     * @exception ResourceException
     *                Thrown if a connection can't be obtained
     */
    @Override
    public SSHConnection getConnection(final SSHSessionRequest cxRequestInfo) throws ResourceException {
        log.info("getConnection()" + cxRequestInfo);
        return (SSHConnection) connectionManager.allocateConnection(mcf, cxRequestInfo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.Referenceable#setReference(javax.naming.Reference)
     */
    @Override
    public void setReference(final Reference reference) {
        this.reference = reference;

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Referenceable#getReference()
     */
    @Override
    public Reference getReference() throws NamingException {
        return this.reference;
    }
}

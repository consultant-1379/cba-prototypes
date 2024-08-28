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

import javax.resource.ResourceException;
import javax.resource.spi.ManagedConnectionMetaData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SSHManagedConnectionMetaData
 * 
 * @author eilanag
 */
public class SSHManagedConnectionMetaData implements ManagedConnectionMetaData {
    /** The logger */
    private static Logger log = LoggerFactory.getLogger(SSHManagedConnectionMetaData.class);
    
    private final SSHManagedConnection managedConnection;
    
    private static final String EIS_PRODUCT_NAME = "SSH Rar";
    private static final String EIS_PRODUCT_VERSION = "1.0.1";
    private static final int MAX_CONNECTIONS = 20;
    
    public SSHManagedConnectionMetaData(final SSHManagedConnection managedConnection) {
        this.managedConnection = managedConnection;
    }

    /**
     * Returns Product name of the underlying EIS instance connected through the ManagedConnection.
     * 
     * @return Product name of the EIS instance
     * @throws ResourceException
     *             Thrown if an error occurs
     */
    @Override
    public String getEISProductName() throws ResourceException {
        log.trace("getEISProductName()");
        return EIS_PRODUCT_NAME;
    }

    /**
     * Returns Product version of the underlying EIS instance connected through the ManagedConnection.
     * 
     * @return Product version of the EIS instance
     * @throws ResourceException
     *             Thrown if an error occurs
     */
    @Override
    public String getEISProductVersion() throws ResourceException {
        log.trace("getEISProductVersion()");
        return EIS_PRODUCT_VERSION;
    }

    /**
     * Returns maximum limit on number of active concurrent connections
     * 
     * @return Maximum limit for number of active concurrent connections
     * @throws ResourceException
     *             Thrown if an error occurs
     */
    @Override
    public int getMaxConnections() throws ResourceException {
        log.trace("getMaxConnections()");
        return MAX_CONNECTIONS;
    }

    /**
     * Returns name of the user associated with the ManagedConnection instance
     * 
     * @return Name of the user
     * @throws ResourceException
     *             Thrown if an error occurs
     */
    @Override
    public String getUserName() throws ResourceException {
        log.trace("getUserName()");
        return managedConnection.getUser();
    }

}

/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.mediation.util.transport.jca;


import javax.naming.NamingException;

import javax.naming.Reference;

import javax.resource.ResourceException;

import javax.resource.spi.ConnectionManager;

/**
 * RAConnectionFactoryImpl
 * @version $Revision: $
 */

public class RAConnectionFactoryImpl implements RAConnectionFactory {

	private static final long serialVersionUID = 1L;
	private Reference reference;
	private RAManagedConnectionFactory mcf;
	private ConnectionManager connectionManager;

	/**
	 * Default constructor
	 * @param mcf
	 *            ManagedConnectionFactory
	 * @param cxManager
	 *            ConnectionManager
	 */

	public RAConnectionFactoryImpl(RAManagedConnectionFactory mcf,
			ConnectionManager cxManager) {
		this.mcf = mcf;
		this.connectionManager = cxManager;
	}

	/**
	 * Get connection from factory
	 * @return RAConnection instance
	 * @exception ResourceException
	 *                Thrown if a connection can't be obtained
	 */

	@Override
	public RAConnection getConnection() throws ResourceException {
		return (RAConnection) connectionManager.allocateConnection(mcf, null);
	}

	/**
	 * Get the Reference instance.
	 * @return Reference instance
	 * @exception NamingException
	 *                Thrown if a reference can't be obtained
	 */

	@Override
	public Reference getReference() throws NamingException{
		return reference;
	}

	/**
	 * Set the Reference instance.
	 * @param reference
	 *            A Reference instance
	 */

	@Override
	public void setReference(Reference reference){
		this.reference = reference;
	}

}
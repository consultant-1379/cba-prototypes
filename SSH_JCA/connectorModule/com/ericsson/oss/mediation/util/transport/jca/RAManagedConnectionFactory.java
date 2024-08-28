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

import java.io.PrintWriter;

import java.util.Iterator;

import java.util.Set;

import java.util.logging.Logger;

import javax.resource.ResourceException;

import javax.resource.spi.ConnectionDefinition;

import javax.resource.spi.ConnectionManager;

import javax.resource.spi.ConnectionRequestInfo;

import javax.resource.spi.ManagedConnection;

import javax.resource.spi.ManagedConnectionFactory;

import javax.resource.spi.ResourceAdapter;

import javax.resource.spi.ResourceAdapterAssociation;

import javax.security.auth.Subject;

/**
 * RAManagedConnectionFactory
 * 
 * @version $Revision: $
 */

@ConnectionDefinition(connectionFactory = RAConnectionFactory.class, connectionFactoryImpl = RAConnectionFactoryImpl.class, connection = RAConnection.class, connectionImpl = RAConnectionImpl.class)
public class RAManagedConnectionFactory implements ManagedConnectionFactory,
		ResourceAdapterAssociation {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger("RAManagedConnectionFactory");
	private ResourceAdapter ra;

	private PrintWriter logwriter;

	public RAManagedConnectionFactory() {
		this.ra = null;
		this.logwriter = null;
	}

	/**
	 * Creates a Connection Factory instance.
	 * 
	 * @return EIS-specific Connection Factory instance or
	 *         javax.resource.cci.ConnectionFactory instance
	 * @throws ResourceException
	 *             Generic exception
	 */

	public Object createConnectionFactory() throws ResourceException {
		throw new ResourceException(
				"This resource adapter doesn't support non-managed environments");
	}

	/**
	 * Creates a Connection Factory instance.
	 * 
	 * @param cxManager
	 *            ConnectionManager to be associated with created EIS connection
	 *            factory instance
	 * @return EIS-specific Connection Factory instance or
	 *         javax.resource.cci.ConnectionFactory instance
	 * @throws ResourceException
	 *             Generic exception
	 */

	public Object createConnectionFactory(ConnectionManager cxManager)
			throws ResourceException {
		return new RAConnectionFactoryImpl(this, cxManager);
	}

	/**
	 * Creates a new physical connection to the underlying EIS resource manager.
	 * 
	 * @param subject
	 *            Caller's security information
	 * @param cxRequestInfo
	 *            Additional resource adapter specific connection request
	 *            information
	 * @throws ResourceException
	 *             generic exception
	 * @return ManagedConnection instance
	 */

	public ManagedConnection createManagedConnection(Subject subject, ConnectionRequestInfo cxRequestInfo) throws ResourceException	{
		log.info("RAManagedConnectionFactory created managed connection ");
		return new RAManagedConnection(this);
	}

	/**
	 * 
	 * Returns a matched connection from the candidate set of connections.
	 * @param connectionSet
	 *            Candidate connection set
	 * @param subject
	 *            Caller's security information
	 * @param cxRequestInfo
	 *            Additional resource adapter specific connection request
	 *            information
	 * @throws ResourceException
	 *             generic exception
	 * @return ManagedConnection if resource adapter finds an acceptable match
	 *         otherwise null
	 */

	public ManagedConnection matchManagedConnections(Set connectionSet,
			Subject subject, ConnectionRequestInfo cxRequestInfo)
			throws ResourceException {
		ManagedConnection result = null;

		Iterator it = connectionSet.iterator();
		while (result == null && it.hasNext()) {
			ManagedConnection mc = (ManagedConnection) it.next();

			if (mc instanceof RAManagedConnection) {
				RAManagedConnection hwmc = (RAManagedConnection) mc;

				result = hwmc;
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

	public PrintWriter getLogWriter() throws ResourceException {
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

	public void setLogWriter(PrintWriter out) throws ResourceException {
		logwriter = out;
	}

	public ResourceAdapter getResourceAdapter() {
		return ra;
	}

	public void setResourceAdapter(ResourceAdapter ra) {
		this.ra = ra;
	}

	@Override
	public int hashCode() {
		int result = 17;
		return result;
	}

	/**
	 * Indicates whether some other object is equal to this one.
	 * 
	 * @param other
	 *            The reference object with which to compare.
	 * @return true If this object is the same as the obj argument, false
	 *         otherwise.
	 */

	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof RAManagedConnectionFactory))
			return false;
		RAManagedConnectionFactory obj = (RAManagedConnectionFactory) other;
		boolean result = true;
		return result;
	}

}
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

import java.util.logging.Logger;

import javax.resource.ResourceException;

import javax.resource.spi.ActivationSpec;

import javax.resource.spi.BootstrapContext;

import javax.resource.spi.ConfigProperty;

import javax.resource.spi.Connector;

import javax.resource.spi.ResourceAdapter;

import javax.resource.spi.ResourceAdapterInternalException;

import javax.resource.spi.TransactionSupport;

import javax.resource.spi.endpoint.MessageEndpointFactory;

import javax.transaction.xa.XAResource;

@Connector(

reauthenticationSupport = false,

transactionSupport = TransactionSupport.TransactionSupportLevel.NoTransaction)
public class SocketResourceAdapter implements ResourceAdapter {

	private static Logger log = Logger.getLogger("SocketResourceAdapter");

	@ConfigProperty(defaultValue = "DefaultMessage", supportsDynamicUpdates = true)
	private String name;

	public SocketResourceAdapter() {

	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void endpointActivation(MessageEndpointFactory endpointFactory,
			ActivationSpec spec) throws ResourceException {
	}

	public void endpointDeactivation(MessageEndpointFactory endpointFactory,
			ActivationSpec spec) {
	}

	public void start(BootstrapContext ctx)
			throws ResourceAdapterInternalException {
		log.info("Resource Adapter bootstrap!");
	}

	/**
	 * This is called when a resource adapter instance is undeployed or during
	 * application server shutdown.
	 */

	public void stop() {
		log.info("Resource adapter shutdown!");
	}

	/**
	 * 
	 * This method is called by the application server during crash recovery.
	 * 
	 * @param specs
	 *            an array of ActivationSpec JavaBeans
	 * @throws ResourceException
	 *             generic exception
	 * @return an array of XAResource objects
	 */

	public XAResource[] getXAResources(ActivationSpec[] specs)
			throws ResourceException {
		return null;
	}

	@Override
	public int hashCode() {

		int result = 17;

		if (name != null)
			result += 31 * result + 7 * name.hashCode();
		else
			result += 31 * result + 7;
		return result;
	}

	/**
	 * 
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
		if (!(other instanceof SocketResourceAdapter))
			return false;
		SocketResourceAdapter obj = (SocketResourceAdapter) other;
		boolean result = true;
		if (result) {
			if (name == null)
				result = obj.getName() == null;
			else
				result = name.equals(obj.getName());
		}
		return result;
	}

}

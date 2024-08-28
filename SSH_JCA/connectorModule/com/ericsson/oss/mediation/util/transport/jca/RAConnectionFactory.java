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


import java.io.Serializable;

import javax.resource.Referenceable;

import javax.resource.ResourceException;

/**
 * RAConnectionFactory
 * 
 * @version $Revision: $
 */

public interface RAConnectionFactory extends Serializable, Referenceable {
	/**
	 * Get connection from factory
	 * 
	 * @return RAConnection instance
	 * @exception ResourceException
	 *                Thrown if a connection can't be obtained
	 */

	public RAConnection getConnection() throws ResourceException;

}

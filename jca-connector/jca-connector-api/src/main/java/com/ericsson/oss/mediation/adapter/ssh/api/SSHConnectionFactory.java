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
package com.ericsson.oss.mediation.adapter.ssh.api;

import java.io.Serializable;

import javax.resource.Referenceable;
import javax.resource.ResourceException;

/**
 * 
 */
public interface SSHConnectionFactory extends Serializable, Referenceable {

    /**
     * Get connection from factory
     * 
     * @return SSHConnection instance
     * @exception ResourceException
     *                Thrown if a connection can't be obtained
     */
    SSHConnection getConnection(final SSHSessionRequest req) throws ResourceException;
}

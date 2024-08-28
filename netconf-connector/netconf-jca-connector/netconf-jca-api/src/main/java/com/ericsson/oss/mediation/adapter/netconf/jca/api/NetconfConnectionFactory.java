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

package com.ericsson.oss.mediation.adapter.netconf.jca.api;

import com.ericsson.oss.mediation.util.transport.api.TransportManager;
import java.io.Serializable;
import java.util.Map;
import javax.resource.Referenceable;
import javax.resource.ResourceException;


/**
 * 
 * @author xvaltda
 */
public interface NetconfConnectionFactory extends Serializable, Referenceable {

    NetconfConnection createNetconfConnection(final TransportManager transportManager, final Map<String, Object> configProperties)
            throws ResourceException;

}

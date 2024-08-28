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

package com.ericsson.oss.mediation.util.transport.ssh.manager;

import com.ericsson.oss.mediation.util.transport.api.TransportManager;
import com.ericsson.oss.mediation.util.transport.api.TransportManagerCI;
import com.ericsson.oss.mediation.util.transport.api.factory.TransportFactory;

/**
 * 
 * @author xvaltda
 */
public class SSHTransportFactory implements TransportFactory {

    @Override
    public TransportManager createTransportManager(final TransportManagerCI transportManagerCI) {

        return new SshTransportManagerImpl(transportManagerCI);
    }

}

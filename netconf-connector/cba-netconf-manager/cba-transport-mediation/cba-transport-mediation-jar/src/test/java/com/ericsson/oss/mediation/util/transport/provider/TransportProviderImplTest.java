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

package com.ericsson.oss.mediation.util.transport.provider;

import com.ericsson.oss.mediation.util.transport.api.factory.TransportFactory;
import com.ericsson.oss.mediation.util.transport.ssh.manager.SSHTransportFactory;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

/**
 * 
 * @author xvaltda
 */
public class TransportProviderImplTest {

    public TransportProviderImplTest() {
    }

    @Test
    public void testSshTransportManagerCreation() {
        TransportFactory transportManager = TransportProviderImpl.getFactory("ssh");

        assertTrue(transportManager instanceof SSHTransportFactory);
    }

    @Test
    public void testTransportManagerCreationExceptoin() {
        TransportFactory transportManager = TransportProviderImpl.getFactory("tls");

        assertTrue(transportManager == null);
    }
}

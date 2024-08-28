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

import com.ericsson.oss.mediation.util.transport.ssh.manager.SshTransportManagerImpl;
import com.ericsson.oss.mediation.util.transport.ssh.manager.SSHTransportFactory;
import com.ericsson.oss.mediation.util.transport.api.TransportManager;
import com.ericsson.oss.mediation.util.transport.api.TransportManagerCI;
import com.ericsson.oss.mediation.util.transport.api.TransportSessionType;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

/**
 * 
 * @author xvaltda
 */
public class SSHTransportFactoryTest {

    TransportManagerCI transportManagerCI;

    public SSHTransportFactoryTest() {
        transportManagerCI = new TransportManagerCI();
        transportManagerCI.setHostname("192.168.100.208");
        transportManagerCI.setPassword("sgsn123");
        transportManagerCI.setUsername("borusgsn");
        transportManagerCI.setPort(22);
        transportManagerCI.setSocketConnectionTimeoutInMillis(1000);
        transportManagerCI.setSessionType(TransportSessionType.SUBSYSTEM);
        transportManagerCI.setSessionTypeValue("netconf");
    }

    @Test
    public void testCreateTransportManager() {

        SSHTransportFactory sshTransportFactory = new SSHTransportFactory();
        TransportManager transportManager = sshTransportFactory.createTransportManager(transportManagerCI);

        assertTrue(transportManager instanceof SshTransportManagerImpl);

    }

}

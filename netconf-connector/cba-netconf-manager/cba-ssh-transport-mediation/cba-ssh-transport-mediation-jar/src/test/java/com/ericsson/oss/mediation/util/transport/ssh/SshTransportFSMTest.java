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

package com.ericsson.oss.mediation.util.transport.ssh;

import com.ericsson.oss.mediation.util.transport.api.exception.TransportException;
import com.ericsson.oss.mediation.util.transport.ssh.exception.SshException;
import com.ericsson.oss.mediation.util.transport.ssh.exception.SshIllegalStateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * 
 * @author xvaltda
 */
@RunWith(MockitoJUnitRunner.class)
public class SshTransportFSMTest {

    private SshTransportFSM sshTransportFSM;

    @Mock
    SshContext sshContext;

    @Test
    public void test_from_IDLE_to_OPEN_CONNECTION() {

        try {

            sshTransportFSM = SshTransportFSM.IDLE;
            sshTransportFSM.connect(sshContext);

            verify(sshContext, times(1)).doCreateSshConnection();
            verify(sshContext, times(1)).setState(SshTransportFSM.OPEN_CONNECTION);

        } catch (SshIllegalStateException ex) {
            Logger.getLogger(SshTransportFSMTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SshException ex) {
            Logger.getLogger(SshTransportFSMTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void test_from_OPEN_CONNECTION_to_CONNECTION_ESTABLISHED() throws SshIllegalStateException {

        sshTransportFSM = SshTransportFSM.OPEN_CONNECTION;
        sshTransportFSM.onSuccess(sshContext);

        verify(sshContext, times(1)).setState(SshTransportFSM.CONNECTION_ESTABLISHED);
        verifyNoMoreInteractions(sshContext);
    }

    @Test
    public void test_from_CONNECTION_ESTABLISHED_to_OPEN_SESSION() throws SshIllegalStateException, SshException {

        sshTransportFSM = SshTransportFSM.CONNECTION_ESTABLISHED;
        sshTransportFSM.openchannel(sshContext);

        verify(sshContext, times(1)).doCreateSshSession();
        verify(sshContext, times(1)).setState(SshTransportFSM.OPEN_SESSION);
        verifyNoMoreInteractions(sshContext);
    }

    @Test
    public void test_from_OPEN_SESSION_to_READY() throws SshIllegalStateException {

        sshTransportFSM = SshTransportFSM.OPEN_SESSION;
        sshTransportFSM.onSuccess(sshContext);

        verify(sshContext, times(1)).setState(SshTransportFSM.READY);
        verifyNoMoreInteractions(sshContext);
    }

    @Test
    public void test_from_READY_to_WRITE() throws SshIllegalStateException, SshException {

        sshTransportFSM = SshTransportFSM.READY;
        sshTransportFSM.write(sshContext);

        verify(sshContext, times(1)).setState(SshTransportFSM.WRITING);
        verify(sshContext, times(1)).doWrite();
        verifyNoMoreInteractions(sshContext);
    }

    @Test
    public void test_from_READY_to_READ() throws SshIllegalStateException, SshException, TransportException {

        sshTransportFSM = SshTransportFSM.READY;
        sshTransportFSM.read(sshContext, false);

        verify(sshContext, times(1)).setState(SshTransportFSM.READING);
        verify(sshContext, times(1)).doRead(false);
        verifyNoMoreInteractions(sshContext);
    }

    @Test
    public void test_from_READY_to_DISCONNECT() throws SshIllegalStateException, SshException {

        sshTransportFSM = SshTransportFSM.READY;
        sshTransportFSM.disconnect(sshContext);

        verify(sshContext, times(1)).setState(SshTransportFSM.DISCONNECT);
        verify(sshContext, times(1)).doDisconnect();
        verifyNoMoreInteractions(sshContext);
    }
}

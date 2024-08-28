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

/**
 * 
 * @author xvaltda
 */
public enum SshTransportFSM {

    IDLE {
        @Override
        public void connect(final SshContext context) throws SshIllegalStateException, SshException {
            this.exit(context);
            OPEN_CONNECTION.enter(context);
            context.doCreateSshConnection();

        }
    },
    OPEN_CONNECTION {

        @Override
        void onSuccess(final SshContext context) {
            this.exit(context);
            CONNECTION_ESTABLISHED.enter(context);
        }

        @Override
        void onFailure(final SshContext context) {
            this.exit(context);
            IDLE.enter(context);
        }

    },
    CONNECTION_ESTABLISHED {

        @Override
        public void openchannel(final SshContext context) throws SshException {
            this.exit(context);
            OPEN_SESSION.enter(context);
            context.doCreateSshSession();
        }
    },
    OPEN_SESSION {

        @Override
        void onSuccess(final SshContext context) {
            this.exit(context);
            READY.enter(context);
        }

        @Override
        void onFailure(final SshContext context) {
            this.exit(context);
            ERROR.enter(context);
        }
    },
    READY {

        @Override
        void disconnect(final SshContext context) throws SshIllegalStateException, SshException {
            this.exit(context);
            DISCONNECT.enter(context);
            context.doDisconnect();
        }

        @Override
        void write(final SshContext context) throws SshIllegalStateException {
            this.exit(context);
            WRITING.enter(context);
            context.doWrite();

        }

        @Override
        void read(final SshContext context, final boolean close) throws SshIllegalStateException, SshException,
                TransportException {
            this.exit(context);
            READING.enter(context);
            context.doRead(close);

        }

    },
    WRITING {
        @Override
        void onSuccess(final SshContext context) {
            this.exit(context);
            READY.enter(context);
        }

        @Override
        void onFailure(final SshContext context) {
            this.exit(context);
            ERROR.enter(context);
        }

    },
    READING {
        @Override
        void onSuccess(final SshContext context) {
            this.exit(context);
            READY.enter(context);
        }

        @Override
        void onFailure(final SshContext context) {
            this.exit(context);
            try {
                DISCONNECT.enter(context);
                context.doDisconnect();
            } catch (SshException ex) {

                IDLE.enter(context);
            }
            ;

        }

    },
    DISCONNECT {
        void onSuccess(final SshContext context) {
            this.exit(context);
            IDLE.enter(context);
        }

        void onFailure(final SshContext context) {
            this.exit(context);
            IDLE.enter(context);
        }

    },
    ERROR {
        @Override
        void disconnect(final SshContext context) throws SshIllegalStateException, SshException {
            this.exit(context);
            DISCONNECT.enter(context);
            context.doDisconnect();
        }
    };

    void connect(final SshContext context) throws SshIllegalStateException, SshException {
        throw new SshIllegalStateException();
    }

    void disconnect(final SshContext context) throws SshIllegalStateException, SshException {
        throw new SshIllegalStateException();
    }

    void openchannel(final SshContext context) throws SshIllegalStateException, SshException {
        throw new SshIllegalStateException();
    }

    void read(final SshContext context, final boolean close) throws SshIllegalStateException, SshException,
            TransportException {
        throw new SshIllegalStateException();
    }

    void write(final SshContext context) throws SshIllegalStateException, SshException {
        throw new SshIllegalStateException();
    }

    void enter(final SshContext context) {
        context.setState(this);
        onEntry();
    }

    void exit(final SshContext context) {
        onExit();
    }

    void onEntry() {
    }

    void onExit() {
    }

    void onSuccess(final SshContext context) {
        throw illegalState();
    }

    void onFailure(final SshContext context) {
        throw illegalState();
    }

    private IllegalStateException illegalState() {
        final String myMethod = Thread.currentThread().getStackTrace()[2].getMethodName();
        final String myMessage = "Method: " + myMethod + ", State: " + name();
        return new IllegalStateException(myMessage);
    }

}
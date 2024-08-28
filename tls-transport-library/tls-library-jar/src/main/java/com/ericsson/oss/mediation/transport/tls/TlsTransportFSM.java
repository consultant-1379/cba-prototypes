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

package com.ericsson.oss.mediation.transport.tls;

import java.io.IOException;

import com.ericsson.oss.mediation.adapter.tls.api.TlsRequest;
import com.ericsson.oss.mediation.adapter.tls.exception.*;

public enum TlsTransportFSM {

    IDLE {
        @Override
        public void connect(final TlsConnectionImpl context) throws TlsException, IOException {
            READY.enter(context);
            context.doConnect();
        }

    },

    READY {
        @Override
        public void read(final TlsConnectionImpl context) throws TlsException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void sendBlocking(final TlsConnectionImpl context) throws TlsException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void sendNonBlocking(final TlsConnectionImpl context, final TlsRequest tlsRequest) throws TlsException {
            WRITING.enter(context);
            context.doNonBlockingWrite(tlsRequest);
        }

        @Override
        public void disconnect(final TlsConnectionImpl context) throws TlsException {
            IDLE.enter(context);
            context.doDisconnect();
        }

    },

    WRITING {

        @Override
        void onNonBlockingWriteSuccess(final TlsConnectionImpl context) {
            READY.enter(context);
        }

        @Override
        void onBlockingWriteSuccess(final TlsConnectionImpl context) {
            BLOCKING_READ.enter(context);
        }

        @Override
        void onFailure(final TlsConnectionImpl context) {
            ERROR.enter(context);
        }

    },

    //TODO Need to implement this
    BLOCKING_READ {
    },

    ERROR {
        @Override
        public void connect(final TlsConnectionImpl context) throws TlsException {
            throw new TlsException(ExceptionReason.ERROR_STATE);
        }

        @Override
        void handshake(final TlsConnectionImpl context) throws TlsException {
            throw new TlsException(ExceptionReason.ERROR_STATE);
        }

        @Override
        void beginHandshake(final TlsConnectionImpl context) throws TlsException {
            throw new TlsException(ExceptionReason.ERROR_STATE);
        }

        @Override
        void disconnect(final TlsConnectionImpl context) throws TlsIllegalStateException, TlsException {
            throw new TlsException(ExceptionReason.ERROR_STATE);
        }

        @Override
        void read(final TlsConnectionImpl context) throws TlsIllegalStateException, TlsException {
            throw new TlsException(ExceptionReason.ERROR_STATE);
        }

        @Override
        void write(final TlsConnectionImpl context) throws TlsIllegalStateException, TlsException {
            throw new TlsException(ExceptionReason.ERROR_STATE);
        }

    };

    void connect(final TlsConnectionImpl context) throws TlsIllegalStateException, TlsException, IOException {
        illegalState();
    }

    void onFailure(final TlsConnectionImpl context) {
        ERROR.enter(context);
    }

    void onNonBlockingWriteSuccess(final TlsConnectionImpl context) throws TlsIllegalStateException {
        illegalState();
    }

    void onBlockingWriteSuccess(final TlsConnectionImpl context) throws TlsIllegalStateException {
        illegalState();
    }

    public void sendBlocking(final TlsConnectionImpl context) throws TlsException, TlsIllegalStateException {
        illegalState();
    }

    public void sendNonBlocking(final TlsConnectionImpl context, final TlsRequest tlsRequest) throws TlsException, TlsIllegalStateException {
        illegalState();
    }

    void handshake(final TlsConnectionImpl context) throws TlsIllegalStateException, TlsException {
        illegalState();
    }

    void beginHandshake(final TlsConnectionImpl context) throws TlsIllegalStateException, TlsException {
        illegalState();
    }

    void disconnect(final TlsConnectionImpl context) throws TlsIllegalStateException, TlsException {
        illegalState();
    }

    void read(final TlsConnectionImpl context) throws TlsIllegalStateException, TlsException {
        illegalState();
    }

    void write(final TlsConnectionImpl context) throws TlsIllegalStateException, TlsException {
        illegalState();
    }

    void enter(final TlsConnectionImpl context) {
        context.setState(this);
    }

    private TlsIllegalStateException illegalState() throws TlsIllegalStateException {
        final String method = Thread.currentThread().getStackTrace()[2].getMethodName();
        final String message = "Method: " + method + ", State: " + name();
        throw new TlsIllegalStateException(message);
    }
}
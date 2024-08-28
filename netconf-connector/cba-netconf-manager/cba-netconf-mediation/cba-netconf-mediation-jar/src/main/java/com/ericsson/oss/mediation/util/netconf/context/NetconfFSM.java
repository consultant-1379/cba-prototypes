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

package com.ericsson.oss.mediation.util.netconf.context;

import com.ericsson.oss.mediation.util.netconf.api.Datastore;
import com.ericsson.oss.mediation.util.netconf.api.Filter;
import com.ericsson.oss.mediation.util.netconf.api.NetconfConnectionStatus;
import com.ericsson.oss.mediation.util.netconf.api.SessionState;
import com.ericsson.oss.mediation.util.netconf.api.exception.NetconfManagerException;

public enum NetconfFSM {

    IDLE {
        @Override
        void connect(final NetconfSession context) throws NetconfManagerException {
            exit(context);
            CONNECT.enter(context);
            context.doConnect();
        }
    },
    CONNECT {
        @Override
        void onSuccess(final NetconfSession context) throws NetconfManagerException {
            exit(context);
            HAND_SHAKE.enter(context);
            context.doHandshake();
        }

        @Override
        void onFailure(final NetconfSession context) {
            exit(context);
            IDLE.enter(context);
        }
    },
    HAND_SHAKE {
        @Override
        void onSuccess(final NetconfSession context) {
            exit(context);
            READY.enter(context);
            context.setConnectionStatus(NetconfConnectionStatus.CONNECTED);
            context.setSessionState(SessionState.READY);

        }

        @Override
        void onFailure(final NetconfSession context) throws NetconfManagerException {
            exit(context);
            IDLE.enter(context);
            context.setConnectionStatus(NetconfConnectionStatus.NOT_CONNECTED);
            throw new NetconfManagerException();
        }
    },
    READY {
        @Override
        void disconnect(final NetconfSession context) throws NetconfManagerException {
            exit(context);
            CLOSE_SESSION.enter(context);
            context.doCloseSession();
        }

        void killSession(final NetconfSession context, final String sessionId) throws NetconfManagerException {
            exit(context);
            KILL_SESSION.enter(context);
            context.doKillSession(sessionId);
        }

        void getConfig(final NetconfSession context, final Datastore datastore, final Filter filter)
                throws NetconfManagerException {
            exit(context);
            QUERY.enter(context);
            context.doGetConfig(datastore, filter);

        }

        void get(final NetconfSession context, final Filter filter) throws NetconfManagerException {
            exit(context);
            QUERY.enter(context);
            context.doGet(filter);
        }

    },
    QUERY {
        void onSuccess(final NetconfSession context) throws NetconfManagerException {
            exit(context);
            READY.enter(context);
        }

        void onFailure(final NetconfSession context) throws NetconfManagerException {
            exit(context);
            READY.enter(context);
        }
    },
    KILL_SESSION {

        void onSuccess(final NetconfSession context) throws NetconfManagerException {
            exit(context);
            READY.enter(context);
        }

        void onFailure(final NetconfSession context) throws NetconfManagerException {
            exit(context);
            READY.enter(context);
        }
    },
    CLOSE_SESSION {
        @Override
        void onSuccess(final NetconfSession context) throws NetconfManagerException {
            exit(context);

            DISCONNECT.enter(context);
            context.doDisconnect();
        }

        @Override
        void onFailure(final NetconfSession context) throws NetconfManagerException {
            exit(context);
            DISCONNECT.enter(context);
            context.doDisconnect();
        }

    },
    DISCONNECT {
        @Override
        void onSuccess(final NetconfSession context) {
            exit(context);
            context.setConnectionStatus(NetconfConnectionStatus.NOT_CONNECTED);
            IDLE.enter(context);
            context.setSessionState(SessionState.IDLE);
        }

        @Override
        void onFailure(final NetconfSession context) throws NetconfManagerException {
            exit(context);
            context.setConnectionStatus(NetconfConnectionStatus.NOT_CONNECTED);
            IDLE.enter(context);
            context.setSessionState(SessionState.IDLE);
        }
    },
    ERROR {
        @Override
        void onSuccess(final NetconfSession context) {
            exit(context);
            context.setConnectionStatus(NetconfConnectionStatus.NOT_CONNECTED);
            IDLE.enter(context);
            context.setSessionState(SessionState.IDLE);
        }
    };

    void exit(final NetconfSession context) {

    }

    void enter(final NetconfSession context) {

        context.setState(this);
        onEntry(context);
    }

    void onSuccess(final NetconfSession context) throws NetconfManagerException {
        throw illegalState();
    }

    void onFailure(final NetconfSession context) throws NetconfManagerException {
        throw illegalState();
    }

    void onFatalError(final NetconfSession context) throws NetconfManagerException {
        exit(context);
        context.setConnectionStatus(NetconfConnectionStatus.NOT_CONNECTED);
        ERROR.enter(context);
        context.doCleanUp();
    }

    void connect(final NetconfSession context) throws NetconfManagerException {
        throw illegalState();
    }

    void disconnect(final NetconfSession context) throws NetconfManagerException {
        throw illegalState();
    }

    void handshake(final NetconfSession context) throws NetconfManagerException {
        throw illegalState();
    }

    void killSession(final NetconfSession context, final String sessionId) throws NetconfManagerException {
        throw illegalState();
    }

    void getConfig(final NetconfSession context, final Datastore datastore, final Filter filter)
            throws NetconfManagerException {
        throw illegalState();
    }

    void get(final NetconfSession context, final Filter filter) throws NetconfManagerException {
        throw illegalState();
    }

    void onEntry(final NetconfSession context) {

    }

    void onExit(final NetconfSession context) {

    }

    private IllegalStateException illegalState() {
        final String myMethod = Thread.currentThread().getStackTrace()[2].getMethodName();
        final String myMessage = "Method: " + myMethod + ", State: " + name();
        return new IllegalStateException(myMessage);
    }
}

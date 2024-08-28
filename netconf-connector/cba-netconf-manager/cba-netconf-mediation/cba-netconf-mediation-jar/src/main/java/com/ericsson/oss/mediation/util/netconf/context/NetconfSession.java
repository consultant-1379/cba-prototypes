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
import com.ericsson.oss.mediation.util.netconf.api.NetconfResponse;
import com.ericsson.oss.mediation.util.netconf.api.SessionState;
import com.ericsson.oss.mediation.util.netconf.api.exception.NetconfManagerException;
import com.ericsson.oss.mediation.util.netconf.capability.NetconfSessionCapabilities;
import com.ericsson.oss.mediation.util.netconf.flow.FlowErrorSeverity;
import com.ericsson.oss.mediation.util.netconf.flow.FlowFactory;
import com.ericsson.oss.mediation.util.netconf.flow.FlowOutput;
import com.ericsson.oss.mediation.util.transport.api.TransportManager;
import com.ericsson.oss.mediation.util.transport.api.exception.TransportConnectionRefusedException;
import com.ericsson.oss.mediation.util.transport.api.exception.TransportException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetconfSession {

    private final Map<String, Object> configProperties;

    private static final Logger logger = LoggerFactory.getLogger(NetconfSession.class);

    private final TransportManager transportManager;
    private NetconfConnectionStatus netconfStatus;
    private NetconfFSM netconfFSM;
    private NetconfSessionCapabilities netconfSessionCapabilities;
    private NetconfResponse netconfResponse;
    private String sessionId = null;
    private SessionState sessionState;

    public NetconfSession(final TransportManager tranportManager, final Map<String, Object> configProperties)
            throws NetconfManagerException {

        this.configProperties = configProperties;
        this.transportManager = tranportManager;
        this.netconfFSM = NetconfFSM.IDLE;
        this.sessionState = SessionState.IDLE;
        this.netconfStatus = NetconfConnectionStatus.NEVER_CONNECTED;
        this.netconfSessionCapabilities = new NetconfSessionCapabilities(this.configProperties);
        this.netconfResponse = new NetconfResponse();
    }
    
    public void setSessionState (SessionState sessionState) {
        this.sessionState = sessionState;
    }

    public SessionState getSessionState (){
        return sessionState;
    }
    
    public void setSessionId(final String sessionId) {
        this.sessionId = sessionId;
    }

    public TransportManager getTransportManager() {
        return transportManager;
    }

    public String getSessionId() {
        return sessionId;
    }

    public NetconfResponse getNetconfResponse() {
        return netconfResponse;
    }

    public NetconfSessionCapabilities getNetconfSessionCapabilities() {
        return netconfSessionCapabilities;
    }

    public void setNetconfSessionCapabilities(final NetconfSessionCapabilities netconfSessionCapabilities) {
        this.netconfSessionCapabilities = netconfSessionCapabilities;
    }

    public Map<String, Object> getConfigProperties() {
        return configProperties;
    }

    public void connect() throws NetconfManagerException {
        netconfFSM.connect(this);
    }

    public void disconnect() throws NetconfManagerException {
        netconfFSM.disconnect(this);
    }

    public void getConfig(final Datastore datastore, final Filter filter) throws NetconfManagerException {
        netconfFSM.getConfig(this, datastore, filter);
    }

    public void get(final Filter filter) throws NetconfManagerException {
        netconfFSM.get(this, filter);
    }

    public void killSession(final String sessionId) throws NetconfManagerException {
        netconfFSM.killSession(this, sessionId);
    }

    public void setState(final NetconfFSM state) {
        netconfFSM = state;
    }

    public void setConnectionStatus(final NetconfConnectionStatus status) {
        netconfStatus = status;
    }

    public NetconfConnectionStatus getStatus() {
        return netconfStatus;
    }

    public void doConnect() throws NetconfManagerException {
        try {
            logger.info("doConnect, trying to open a connection in " + "the transport layer ");
            transportManager.openConnection();
            netconfFSM.onSuccess(this);

        } catch (TransportException | TransportConnectionRefusedException ex) {

            netconfFSM.onFailure(this);
            throw new NetconfManagerException(ex);

        } catch (final NetconfManagerException e) {
            //dont call onFailure, this exception can happen in other state during the session that will call onFailure
            throw e;
        } catch (final Exception ex) {

            logger.error("doConnect, Exception " + ex.getMessage());
            netconfFSM.onFailure(this);
            throw new NetconfManagerException(ex);
        }
    }

    public void doHandshake() throws NetconfManagerException {
        logger.info("doHandshake, sending Hello message");
        final FlowOutput output = FlowFactory.createHandshakeFlow(this).execute();
        checkForError(output);
        netconfResponse = output.toNetconfResponse();

    }

    public void doGetConfig(final Datastore datastore, final Filter filter) throws NetconfManagerException {
        logger.info("doGetConfig, sending get-config message");
        final FlowOutput output = FlowFactory.createGetConfigFlow(this, datastore, filter).execute();
        checkForError(output);
        netconfResponse = output.toNetconfResponse();
    }

    public void doGet(final Filter filter) throws NetconfManagerException {
        logger.info("doGet, sending get message");
        final FlowOutput output = FlowFactory.createGetFlow(this, filter).execute();
        checkForError(output);
        netconfResponse = output.toNetconfResponse();
    }

    public void doKillSession(final String sessionId) throws NetconfManagerException {
        logger.info("doKillSession, sending kill session message");
        final FlowOutput output = FlowFactory.createKillSessionFlow(sessionId, this).execute();
        checkForError(output);
        netconfResponse = output.toNetconfResponse();

    }

    public void doCloseSession() throws NetconfManagerException {

        logger.info("doCloseSession, sending close-session");
        final FlowOutput output = FlowFactory.createCloseFlow(this).execute();
        checkForError(output);
        netconfResponse = output.toNetconfResponse();

    }

    public void doDisconnect() throws NetconfManagerException {
        try {
            logger.info("doDisconnect, trying to close the socket in the transport layer," + " close Connection ");
            transportManager.closeConnection();
            netconfFSM.onSuccess(this);
        } catch (final TransportException ex) {

            logger.error("doDisconnect, Error on closing Transport connection " + ex.getMessage());
            netconfFSM.onFailure(this);
            throw new NetconfManagerException(ex);
        }
    }

    public void doCleanUp() throws NetconfManagerException {
        netconfFSM.onSuccess(this);
    }

    private void checkForError(final FlowOutput output) throws NetconfManagerException {

        try {
            if (output.isError()) {
                if (output.getErrorSeverity() == FlowErrorSeverity.FATAL) {
                    netconfFSM.onFatalError(this);
                    logger.error("Fatal error on NetconfSession, netconf's session will be closed:\n "
                            + output.getErrorMessage());
                    throw new NetconfManagerException(
                            "Fatal error on NetconfSession, netconf's session will be closed:\n "
                                    + output.getErrorMessage());
                } else {
                    netconfFSM.onFailure(this);
                }
            } else {
                netconfFSM.onSuccess(this);
            }
        } catch (final NetconfManagerException ex) {
            throw new NetconfManagerException(output.getErrorMessage());
        }
    }
}
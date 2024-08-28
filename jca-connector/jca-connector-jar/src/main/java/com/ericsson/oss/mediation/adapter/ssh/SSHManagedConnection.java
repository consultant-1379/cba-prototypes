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
package com.ericsson.oss.mediation.adapter.ssh;

import java.io.PrintWriter;
import java.util.*;

import javax.resource.ResourceException;
import javax.resource.spi.*;
import javax.security.auth.Subject;
import javax.transaction.xa.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.adapter.ssh.api.SSHConnection;
import com.ericsson.oss.mediation.adapter.ssh.provider.CLISessionException;

/**
 * SSHManagedConnection
 * 
 * JCA Generated class, represents managed connection holding a handle for physical connection to EIS
 * 
 * @author eilanag
 */
public class SSHManagedConnection implements ManagedConnection, XAResource, LocalTransaction {

    /** The logger */
    private static final Logger log = LoggerFactory.getLogger(SSHManagedConnection.class);

    private static final String CONN_LISTENER_IS_NULL = "Connection listener is null";

    /** The logwriter */
    private PrintWriter logwriter;

    /** ManagedConnectionFactory */
    private final SSHManagedConnectionFactory mcf;

    /** Listeners */
    private final List<ConnectionEventListener> listeners;

    /** Connection */
    private SSHConnectionImpl connection;

    /** MetaData information */
    private SSHManagedConnectionMetaData metaData;

    /** Transaction timeout */
    private int txTimeout;

    private final String user;
    /**
     * 
     * @param mcf
     *            mcf
     */
    public SSHManagedConnection(final SSHManagedConnectionFactory mcf, final String user) {
        this.mcf = mcf;
        this.logwriter = null;
        this.listeners = Collections.synchronizedList(new ArrayList<ConnectionEventListener>(1));
        this.connection = null;
        this.user = user;
    }

    /**
     * Creates a new connection handle for the underlying physical connection represented by the ManagedConnection instance.
     * 
     * @param subject
     *            Security context as JAAS subject
     * @param cxRequestInfo
     *            ConnectionRequestInfo instance
     * @return generic Object instance representing the connection handle.
     * @throws ResourceException
     *             generic exception if operation fails
     */
    @Override
    public Object getConnection(final Subject subject, final ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        log.debug("getConnection()" + connection);
        if (connection == null) {
            connection = new SSHConnectionImpl(this, mcf, cxRequestInfo);
        } else if(!connection.isAlive()) {
            connection.closeSSHSession();
        }
        try {
            connection.createSSHSession();
        } catch(final CLISessionException e) {
            throw new ResourceException(e.getMessage(), e);
        }
        log.debug("getConnection()" + connection);
        return connection;
    }

    public SSHConnectionImpl getConnection() {
        return connection;
    }

    /**
     * Used by the container to change the association of an application-level connection handle with a ManagedConneciton instance.
     * 
     * @param connection
     *            Application-level connection handle
     * @throws ResourceException
     *             generic exception if operation fails
     */
    @Override
    public void associateConnection(final Object connection) throws ResourceException {
        log.debug("associateConnection()");

        if (connection == null) {
            throw new ResourceException("Null connection handle");
        }

        if (!(connection instanceof SSHConnectionImpl)) {
            throw new ResourceException("Wrong connection handle");
        }

        this.connection = (SSHConnectionImpl) connection;
    }

    /**
     * Application server calls this method to force any cleanup on the ManagedConnection instance.
     * 
     * @throws ResourceException
     *             generic exception if operation fails
     */
    @Override
    public void cleanup() throws ResourceException {
        log.debug("cleanup() method called");
        final String endCmd = this.connection.getReqInfo().getEndCommand();
        final String[] commands = { endCmd };
        this.connection.executeCommandBytes(commands);

    }

    /**
     * Destroys the physical connection to the underlying resource manager.
     * 
     * @throws ResourceException
     *             generic exception if operation fails
     */
    @Override
    public void destroy() throws ResourceException {
        log.debug("destroy() method called");
        if(connection != null) {
            connection.closeSSHSession();
        }
        this.connection = null;
        this.metaData = null;

    }

    /**
     * Close handle
     * 
     * @param handle
     *            The handle
     */
    void closeHandle(final SSHConnection handle) {
        final ConnectionEvent event = new ConnectionEvent(this, ConnectionEvent.CONNECTION_CLOSED);
        log.debug("closeHandle has been called...");
        event.setConnectionHandle(handle);
        for (final ConnectionEventListener cel : listeners) {
            log.debug("cel.connectionClosed(event)");
            cel.connectionClosed(event);
        }

        log.debug("closeHandle method complete");
    }



    public String getUser() {
        return user;
    }

    /**
     * Gets the log writer for this ManagedConnection instance.
     * 
     * @return Character output stream associated with this Managed-Connection instance
     * @throws ResourceException
     *             generic exception if operation fails
     */
    @Override
    public PrintWriter getLogWriter() throws ResourceException {
        log.trace("getLogWriter()");
        return logwriter;
    }

    /**
     * Sets the log writer for this ManagedConnection instance.
     * 
     * @param out
     *            Character Output stream to be associated
     * @throws ResourceException
     *             generic exception if operation fails
     */
    @Override
    public void setLogWriter(final PrintWriter out) throws ResourceException {
        log.trace("setLogWriter()");
        logwriter = out;
    }

    /**
     * Returns an <code>javax.resource.spi.LocalTransaction</code> instance.
     * 
     * @return LocalTransaction instance
     * @throws ResourceException
     *             generic exception if operation fails
     */
    @Override
    public LocalTransaction getLocalTransaction() throws ResourceException {
        log.trace("getLocalTransaction()");
        return this;
    }

    /**
     * Returns an <code>javax.transaction.xa.XAresource</code> instance.
     * 
     * @return XAResource instance
     * @throws ResourceException
     *             generic exception if operation fails
     */
    @Override
    public XAResource getXAResource() throws ResourceException {
        log.trace("getXAResource()");
        return this;
    }

    /**
     * Gets the metadata information for this connection's underlying EIS resource manager instance.
     * 
     * @return ManagedConnectionMetaData instance
     * @throws ResourceException
     *             generic exception if operation fails
     */
    @Override
    public ManagedConnectionMetaData getMetaData() throws ResourceException {
        log.trace("getMetaData()");
        this.metaData = new SSHManagedConnectionMetaData(this);
        return this.metaData;
    }


    @Override
    public void addConnectionEventListener(final javax.resource.spi.ConnectionEventListener listener) {
        log.debug("addConnectionEventListener()");
        if (listener == null) {
            throw new IllegalArgumentException(CONN_LISTENER_IS_NULL);
        }
        this.listeners.add(listener);
    }

    @Override
    public void removeConnectionEventListener(final javax.resource.spi.ConnectionEventListener listener) {
        log.debug("removeConnectionEventListener()");
        if (listener == null) {
            throw new IllegalArgumentException(CONN_LISTENER_IS_NULL);
        }
        this.listeners.remove(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.LocalTransaction#begin()
     */
    @Override
    public void begin() throws ResourceException {
        log.trace("begin method called...");

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.LocalTransaction#commit()
     */
    @Override
    public void commit() throws ResourceException {
        log.trace("commit method called...");

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.LocalTransaction#rollback()
     */
    @Override
    public void rollback() throws ResourceException {
        log.trace("rollback method called...");

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.transaction.xa.XAResource#commit(javax.transaction.xa.Xid, boolean)
     */
    @Override
    public void commit(final Xid xid, final boolean onePhase) throws XAException {
        log.trace("commit called with xid=[{}] and onePhase=[{}]", new Object[] { xid, onePhase });

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.transaction.xa.XAResource#end(javax.transaction.xa.Xid, int)
     */
    @Override
    public void end(final Xid xid, final int flags) throws XAException {
        log.trace("End called with xid=[{}] and flag[{}]", new Object[] { xid, flags });
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.transaction.xa.XAResource#forget(javax.transaction.xa.Xid)
     */
    @Override
    public void forget(final Xid xid) throws XAException {
        log.trace("Forget called with xid=[{}]", xid);

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.transaction.xa.XAResource#getTransactionTimeout()
     */
    @Override
    public int getTransactionTimeout() throws XAException {
        log.trace("getTransactionTimeout called and returning {}", this.txTimeout);
        return this.txTimeout;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.transaction.xa.XAResource#isSameRM(javax.transaction.xa.XAResource)
     */
    @Override
    public boolean isSameRM(final XAResource xares) throws XAException {
        final boolean result = this.equals(xares);
        log.trace("isSameRM called with xares=[{}] and is returning {}", new Object[] { xares, result });
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.transaction.xa.XAResource#prepare(javax.transaction.xa.Xid)
     */
    @Override
    public int prepare(final Xid xid) throws XAException {
        log.trace("prepare called with xid=[{}]", xid);
        return XA_OK;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.transaction.xa.XAResource#recover(int)
     */
    @Override
    public Xid[] recover(final int flag) throws XAException {
        log.trace("recover called with flag=[{}] and returning null", flag);
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.transaction.xa.XAResource#rollback(javax.transaction.xa.Xid)
     */
    @Override
    public void rollback(final Xid xid) throws XAException {
        log.trace("rollback called for xid=[{}]", xid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.transaction.xa.XAResource#setTransactionTimeout(int)
     */
    @Override
    public boolean setTransactionTimeout(final int seconds) throws XAException {
        log.trace("setTransactionTimeout called with seconds=[{}]", seconds);
        this.txTimeout = seconds;
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.transaction.xa.XAResource#start(javax.transaction.xa.Xid, int)
     */
    @Override
    public void start(final Xid xid, final int flags) throws XAException {
        log.trace("start called for xid=[{}] and flags=[{}]", new Object[] { xid, flags });
    }

}

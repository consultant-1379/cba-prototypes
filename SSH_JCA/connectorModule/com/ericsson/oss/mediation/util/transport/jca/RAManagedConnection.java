/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.mediation.util.transport.jca;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.spi.*;
import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;

import com.ericsson.oss.mediation.util.transport.api.TransportData;
import com.ericsson.oss.mediation.util.transport.api.TransportManager;
import com.ericsson.oss.mediation.util.transport.api.exception.TransportException;

/**
 * 
 * RAManagedConnection
 * 
 *
 * 
 * @version $Revision: $
 */

public class RAManagedConnection implements ManagedConnection {
	
    public static String HELLO_QUERY = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<hello xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">"
			+ "<capabilities><capability>urn:ietf:params:netconf:base:1.0</capability></capabilities></hello>]]>]]>";

	private static Logger log = Logger.getLogger("RAManagedConnection");
	private RAManagedConnectionFactory mcf;
	private PrintWriter logWriter;
	private List<ConnectionEventListener> listeners;
	private Object connection;

	public RAManagedConnection(RAManagedConnectionFactory mcf) {
		this.mcf = mcf;
		this.logWriter = null;
		this.listeners = new ArrayList<ConnectionEventListener>(1);
		this.connection = null;
	}

	/**
	 * Creates a new connection handle for the underlying physical connection
	 * represented by the ManagedConnection instance.
	 * @param subject
	 *            Security context as JAAS subject
	 * 
	 * @param cxRequestInfo
	 *            ConnectionRequestInfo instance
	 * 
	 * @return generic Object instance representing the connection handle.
	 * 
	 * @throws ResourceException
	 *             generic exception if operation fails
	 */

	public Object getConnection(Subject subject, ConnectionRequestInfo cxRequestInfo) throws ResourceException {
		connection = new RAConnectionImpl(this, mcf);
		return connection;
	}

	/**
	 * Used by the container to change the association of an
	 * application-level connection handle with a ManagedConneciton instance.
	 * @param connection
	 *            Application-level connection handle
	 * 
	 * @throws ResourceException
	 *             generic exception if operation fails
	 */

	public void associateConnection(Object connection) throws ResourceException {
		this.connection = connection;
	}

	/**
	 * Application server calls this method to force any cleanup on
	 * the ManagedConnection instance.
	 * @throws ResourceException
	 *             generic exception if operation fails
	 */

	public void cleanup() throws ResourceException {
	}

	/**
	 * Destroys the physical connection to the underlying resource manager.
	 * @throws ResourceException
	 *             generic exception if operation fails
	 */

	public void destroy() throws ResourceException	{
		this.connection = null;
	}

	/**
	 * Adds a connection event listener to the ManagedConnection instance.
	 * @param listener
	 *            A new ConnectionEventListener to be registered
	 */

	public void addConnectionEventListener(ConnectionEventListener listener)	{
		if (listener == null)
			throw new IllegalArgumentException("Listener is null");
		listeners.add(listener);
	}

	/**
	 * 
	 * Removes an already registered connection event listener
	 * from the ManagedConnection instance.
	 * @param listener
	 *            Already registered connection event listener to be removed
	 */

	public void removeConnectionEventListener(ConnectionEventListener listener)	{
		if (listener == null)
			throw new IllegalArgumentException("Listener is null");
		listeners.remove(listener);
	}

	/**
	 * 
	 * Gets the log writer for this ManagedConnection instance.
	 * 
	 * @return Character ourput stream associated with this Managed-Connection
	 *         instance
	 * @throws ResourceException
	 *             generic exception if operation fails
	 */

	public PrintWriter getLogWriter() throws ResourceException {
		return logWriter;
	}

	/**
	 * Sets the log writer for this ManagedConnection instance.
	 * 
	 * @param out
	 *            Character Output stream to be associated
	 * @throws ResourceException
	 *             generic exception if operation fails
	 */

	public void setLogWriter(PrintWriter out) throws ResourceException {
		this.logWriter = out;
	}

	/**
	 * Returns an <code>javax.resource.spi.LocalTransaction</code> instance.
	 * 
	 * @return LocalTransaction instance
	 * @throws ResourceException
	 *             generic exception if operation fails
	 */

	public LocalTransaction getLocalTransaction() throws ResourceException {
		throw new NotSupportedException("LocalTransaction not supported");
	}

	/**
	 * Returns an <code>javax.transaction.xa.XAresource</code> instance.
	 * 
	 * @return XAResource instance
	 * @throws ResourceException
	 *             generic exception if operation fails
	 */

	public XAResource getXAResource() throws ResourceException {
		throw new NotSupportedException("GetXAResource not supported");
	}

	/**
	 * Gets the metadata information for this connection's underlying EIS
	 * resource manager instance.
	 * 
	 * @return ManagedConnectionMetaData instance
	 * 
	 * @throws ResourceException
	 *             generic exception if operation fails
	 */

	public ManagedConnectionMetaData getMetaData() throws ResourceException {
		return new RAManagedConnectionMetaData();
	}

	/**
	 * Call write
	 * 
	 * @param name
	 *            String text
	 * @return returnVal
	 */

	String write(String text) {
		RAConnectionImpl conn = (RAConnectionImpl) connection;
//		Socket socket = conn.getSocket();
		TransportManager tm = conn.getTransportManager();
		TransportData request = new TransportData();
		request.setData(new StringBuffer(HELLO_QUERY));
		String responseToReturn = "This is not expected respone ;-)";
		try {
			tm.sendData(request);
			System.out.println("data sent...");
			
			TransportData response = new TransportData();
			response.setEndData("]".charAt(0));
			tm.readData(response);
			System.out.println("odgovor...:");
			responseToReturn = response.getData().toString();
			System.out.println(responseToReturn);
		} catch (TransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseToReturn;
	}

	/**
	 * Close handle
	 * 
	 * @param handle
	 *            The handle
	 */

	void closeHandle(RAConnection handle) {
		ConnectionEvent event = new ConnectionEvent(this,
				ConnectionEvent.CONNECTION_CLOSED);

		event.setConnectionHandle(handle);

		for (ConnectionEventListener cel : listeners) {
			cel.connectionClosed(event);
		}
	}

}


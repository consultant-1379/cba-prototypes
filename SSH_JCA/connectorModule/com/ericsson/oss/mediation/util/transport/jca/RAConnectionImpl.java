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

import java.util.logging.Logger;

import com.ericsson.oss.mediation.util.transport.api.*;
import com.ericsson.oss.mediation.util.transport.ssh.manager.SSHTransportFactory;

public class RAConnectionImpl implements RAConnection {
	private static Logger log = Logger.getLogger("RAConnectionImpl");
	private RAManagedConnection mc;
	private RAManagedConnectionFactory mcf;

//	Socket socket = null;
	SSHTransportFactory sshTransportFactory;
	TransportManager transportManager;

	public RAConnectionImpl(RAManagedConnection mc, RAManagedConnectionFactory mcf) {
		this.mc = mc;
		this.mcf = mcf;
		try {
//			socket = new Socket("127.0.0.1", 6789);
			sshTransportFactory = new SSHTransportFactory();
			
			TransportManagerCI transportManagerCI = new TransportManagerCI();
			transportManagerCI.setHostname("192.168.100.240");
			transportManagerCI.setPassword("tcu123");
			transportManagerCI.setUsername("tcuuser");
			transportManagerCI.setPort(22);
			transportManagerCI.setSocketConnectionTimeoutInMillis(20000);
			transportManagerCI.setSessionType(TransportSessionType.SUBSYSTEM);
			transportManagerCI.setSessionTypeValue("netconf");
			
			transportManager = sshTransportFactory.createTransportManager(transportManagerCI);
			transportManager.openConnection();
			System.out.println("drazen conn opened.");
			log.info("drazen otvorio");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String write() {
		return write(((SocketResourceAdapter) mcf.getResourceAdapter()).getName());
	}

	public String write(String name) {
		return mc.write(name);
	}
	
	/**
	 * @return the transportManager
	 */
	public TransportManager getTransportManager() {
		return transportManager;
	}

	public void close() {
		mc.closeHandle(this);
		try {
			this.transportManager.closeConnection();
			log.info("Socket closed!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

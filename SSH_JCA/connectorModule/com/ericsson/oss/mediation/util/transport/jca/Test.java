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

import com.ericsson.oss.mediation.util.transport.api.*;
import com.ericsson.oss.mediation.util.transport.api.exception.TransportConnectionRefusedException;
import com.ericsson.oss.mediation.util.transport.api.exception.TransportException;
import com.ericsson.oss.mediation.util.transport.ssh.SshContext;
import com.ericsson.oss.mediation.util.transport.ssh.exception.SshException;
import com.ericsson.oss.mediation.util.transport.ssh.manager.SSHTransportFactory;

public class Test {

	public static void main(String[] args) {
		SSHTransportFactory sshTransportFactory = new SSHTransportFactory();

		TransportManagerCI transportManagerCI = new TransportManagerCI();
		transportManagerCI.setHostname("192.168.100.240");
		transportManagerCI.setPassword("tcu123");
		transportManagerCI.setUsername("tcuuser");
		transportManagerCI.setPort(22);
		transportManagerCI.setSocketConnectionTimeoutInMillis(20000);
		transportManagerCI.setSessionType(TransportSessionType.SUBSYSTEM);
		transportManagerCI.setSessionTypeValue("netconf");
		int count = 2;
		TransportManager[] transportManagerArr = new TransportManager[count];
		
		
		for (int i = 0; i < count; i++) {
			TransportManager transportManager = sshTransportFactory.createTransportManager(transportManagerCI);
			try {
				transportManager.openConnection();
				transportManagerArr[i] = transportManager;
				
				System.out.println("Drazen oc sleep...");
				Thread.sleep(1000 * 10);
				System.out.println("Drazen oc nastavljam");
				
				TransportData data = new TransportData();
				
				data.setData(new StringBuffer("ajde"));
				data.setEndData("]".toCharArray()[0]);
				transportManagerArr[i].sendData(data);
				transportManagerArr[i].readData(data);
				System.out.println(data.getData().toString());
				System.out.println("connected.. " + i);

				System.out.println("probudjen...");
				data.setData(new StringBuffer("ajde"));
				data.setEndData("]".toCharArray()[0]);
				transportManagerArr[i].sendData(data);
				transportManagerArr[i].readData(data);
				System.out.println(data.getData().toString());
				System.out.println("connected aa.. " + i);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	

		System.out.println("evo me");
		for (int i = 0; i < count; i++) {
			try {
				transportManagerArr[i].closeConnection();
				System.out.println("dicconnected " + i);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}

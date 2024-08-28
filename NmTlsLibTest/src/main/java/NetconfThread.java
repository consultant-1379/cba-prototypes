import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.AsyncContext;

import com.ericsson.oss.mediation.adapter.tls.exception.TlsException;
import com.ericsson.oss.mediation.util.netconf.api.NetconManagerConstants;
import com.ericsson.oss.mediation.util.netconf.api.NetconfManager;
import com.ericsson.oss.mediation.util.netconf.api.exception.NetconfManagerException;
import com.ericsson.oss.mediation.util.netconf.manager.NetconfManagerFactory;
import com.ericsson.oss.mediation.transport.api.TransportManager;
import com.ericsson.oss.mediation.transport.api.TransportManagerCI;
import com.ericsson.oss.mediation.transport.api.TransportSessionType;
import com.ericsson.oss.mediation.transport.manager.TransportManagerFactory;

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

public class NetconfThread implements Runnable {

	private AsyncContext acontext;
	private String operation;
	private String ipAddress;
	private PrintWriter out2;
	private NetconfManager netconfManager;

	public NetconfThread(AsyncContext acontext, String operation, String ipAddress)
			throws IOException {
		this.acontext = acontext;
		this.operation = operation;
		this.ipAddress = ipAddress;

		out2 = acontext.getResponse().getWriter();
		try {
			setupNetconf(this.ipAddress);
		} catch (NetconfManagerException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			testNetConf();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void performNetconfDisconnect() throws TlsException {
		out2.println("Attempting disconnect of Netconf over TLS Connection");
		out2.println("...................");
		try {
			netconfManager.disconnect();
		} catch (NetconfManagerException e) {
			e.printStackTrace();
		}
		out2.println("disconnect is complete");
		out2.println("...................");
		out2.flush();
	}

	private void performNetconfConnect() throws TlsException {
		out2.println("Starting Netconf over TLS Connection....");
		out2.println("...................");
		try {
			netconfManager.connect();
		} catch (NetconfManagerException e) {
			e.printStackTrace();
		}
		out2.println("Handshake is complete");
		out2.println("...................");
		out2.println("Netconf over TLS Connection Successful");
		out2.flush();
	}

	public void testNetConf() throws Exception {

		out2.println("start...");
		out2.flush();

		if (this.operation.equalsIgnoreCase("connect")) {
			performNetconfConnect();
		} else if (this.operation.equalsIgnoreCase("disconnect")) {
			performNetconfDisconnect();
		} else if (this.operation.equalsIgnoreCase("write")) {
			//
		} else if (this.operation.equalsIgnoreCase("read")) {
			//
		} else {
			out2.println("No operation selected");
			out2.println("...................");
		}

		this.acontext.complete();
	}

	private void setupNetconf(final String ip) throws IOException, NetconfManagerException {

		TransportManagerCI transportManagerCI = new TransportManagerCI();
		transportManagerCI.setHostname(ip);
		//transportManagerCI.setUsername("borusgsn");
		//transportManagerCI.setPassword("sgsn123");
		transportManagerCI.setPort(6513);
		transportManagerCI.setSocketConnectionTimeoutInMillis(2000);
		transportManagerCI.setSessionType(TransportSessionType.SUBSYSTEM);
		transportManagerCI.setSessionTypeValue("netconf");

//		// This will give us an instance of TLStransportFactory
//		TransportFactory transportFactory = TransportProviderImpl
//				.getFactory("tls");
//
//		// This will give us an instance of TlsTransportManagerImpl
//		TransportManager transportManager = transportFactory
//				.createTransportManager(transportManagerCI);

		TransportManager transportManager = TransportManagerFactory.createTransportManager("tls", transportManagerCI);

		Map<String, Object> configProperties = new HashMap<>();
		String[] capabilities = new String[] {
				"urn:ietf:params:ns:netconf:base:1.0",
				"urn:ietf:params:xml:ns:netconf:base:1.0",
				"urn:ietf:params:netconf:capability:writable-running:1.0",
				"urn:ietf:params:netconf:capability:notification:1.0",
				"urn:ietf:params:netconf:capability:rollback-on-error:1.0" };
		configProperties.put(NetconManagerConstants.CAPABILITIES_KEY,
				Arrays.asList(capabilities));

		netconfManager = NetconfManagerFactory.createNetconfManager(
				transportManager, configProperties);
	}
}

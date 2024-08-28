import com.ericsson.oss.mediation.adapter.tls.exception.TlsChannelException;
import com.ericsson.oss.mediation.adapter.tls.exception.TlsException;
import com.ericsson.oss.mediation.transport.api.TransportManagerCI;
import com.ericsson.oss.mediation.transport.api.TransportSessionMode;
import com.ericsson.oss.mediation.transport.api.exception.TransportConnectionRefusedException;
import com.ericsson.oss.mediation.transport.api.exception.TransportException;

import javax.servlet.AsyncContext;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

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

public class ThreadReal implements Runnable {

	private AsyncContext acontext;
	private String operation;
	private String message;
	private int bytesToRead;
	private PrintWriter out2;
	private TransportManagerCI transportManagerCI =  new TransportManagerCI();

	public ThreadReal(AsyncContext acontext, final RequestInformation requestInformation) throws IOException {
		this.acontext = acontext;
		this.operation = requestInformation.operation;

		transportManagerCI.setHostname("192.168.100.243");
		transportManagerCI.setPort(6513);
		transportManagerCI.setEndDataPattern(Pattern.compile("]]>]]>"));

		if(requestInformation.message != null){
			this.message = this.convertMessage(requestInformation.message);
		}

		if(requestInformation.bytesToRead != -1){
			this.bytesToRead = requestInformation.bytesToRead;
		}

		System.out.println("ThreadReal.testTLS()");
		out2 = acontext.getResponse().getWriter();
	}

	public void run() {
		try {
			testTLS();
		} catch (TransportException | TransportConnectionRefusedException | UnsupportedEncodingException e) {
			this.acontext.complete();
			out2.println("TlsException thrown..." + e.getMessage());
			out2.flush();
		}

	}

	public void testTLS() throws TransportException, TransportConnectionRefusedException, UnsupportedEncodingException {
		out2.println("start...");
		out2.flush();

		if(this.operation.equalsIgnoreCase("connectShort")){
			performTlsConnect();
		} else if(this.operation.equalsIgnoreCase("connectLong")){
			performTlsConnectLong();
		} else if(this.operation.equalsIgnoreCase("disconnect")){
			performTlsDisconnect();
		} else if(this.operation.equalsIgnoreCase("executeAsync")){
			performExecuteAsync();
		} else if(this.operation.equalsIgnoreCase("executeCommand")){
			performExecuteCommand();
		} else if(this.operation.equalsIgnoreCase("write")){
			performWrite();
		} else if(this.operation.equalsIgnoreCase("readint")){
			performReadInt();
		} else if(this.operation.equalsIgnoreCase("read")){
			performRead();
		} else if(this.operation.equalsIgnoreCase("status")){
            performCheckConnectionStatus();
        } else if(this.operation.equalsIgnoreCase("getPattern")){
			performGetPattern();
		} else {
			out2.println("No operation selected");
			out2.println("...................");
		}

		this.acontext.complete();
	}


	private void performReadInt() throws TransportException {
		out2.println("Checking for number of bytes available....");
		out2.println("...................");
		out2.println("Bytes available : " + TlsProviderHolder.getTlsProvider(transportManagerCI).read());
		out2.println("...................");
		out2.println("TLS Read successful");
		out2.flush();
	}

	private void performRead() throws TransportException, UnsupportedEncodingException {
		out2.println("Reading from TlsLibrary....");
		out2.println("...................");
		out2.println("Number of bytes to read : " + bytesToRead);
		byte[] bytesRead = new byte[bytesToRead];
		out2.println(TlsProviderHolder.getTlsProvider(transportManagerCI).read(bytesRead, 0, bytesToRead));
		out2.println("Number of bytes read was : " + bytesRead.length);
		out2.println("...................");
		out2.println("Data read is : " + new String(bytesRead, "UTF-8"));
		out2.println("...................");
		out2.println("TLS Read successful");
		out2.flush();
	}

	private void performTlsDisconnect() throws TransportException {
		out2.println("Attempting disconnect of TLS Connection");
		out2.println("...................");
		TlsProviderHolder.getTlsProvider(transportManagerCI).closeSession();
		out2.println("disconnect is complete");
		out2.println("...................");
		out2.flush();
	}

	private void performTlsConnect() throws TransportConnectionRefusedException, TransportException {
		out2.println("Starting TLS Connection....");
		out2.println("...................");
		TlsProviderHolder.getTlsProvider(transportManagerCI).setSessionTimeout(10000, TimeUnit.SECONDS);
		TlsProviderHolder.getTlsProvider(transportManagerCI).openSession();
		out2.println("Handshake is complete");
		out2.println("...................");
		out2.println("TLS Connection Successful");
		out2.flush();
	}

	private void performTlsConnectLong() throws TransportConnectionRefusedException, TransportException {
		out2.println("Starting TLS Connection....");
		out2.println("...................");
		TlsProviderHolder.getTlsProvider(transportManagerCI).openSession(TransportSessionMode.LONG_LIFE);
		out2.println("Handshake is complete");
		out2.println("...................");
		out2.println("TLS Connection Successful");
		out2.flush();
	}

	private void performExecuteAsync() throws TransportException {
        out2.println("ExecuteCommandAsync....");
        out2.println("...................");
        out2.println("Message to send : ");
		out2.println(this.message);
        out2.println("...................");
        TlsProviderHolder.getTlsProvider(transportManagerCI).executeCommandAsync(this.message);
        out2.println("Tls library has sent message successfully");
        out2.println("...................");
        out2.println("ExecuteCommandAsync Successful");
        out2.println("...................");
        out2.flush();
	}

    private void performExecuteCommand() throws TransportException {
        out2.println("Execute Command....");
        out2.println("...................");
		out2.println("Message to send : ");
		out2.println(this.message);
        out2.println("...................");
		out2.println("RESPONSE IS: ");
		out2.println(TlsProviderHolder.getTlsProvider(transportManagerCI).executeCommand(this.message));
        out2.println("...................");
        out2.println("ExecuteCommand successful");
        out2.println("...................");
        out2.flush();
    }

    private void performWrite() throws TransportException {
        out2.println("Sending bytes to TlsLibrary....");
        out2.println("...................");
        out2.println("Bytes to send : " + this.message.getBytes());
        out2.println("...................");
        TlsProviderHolder.getTlsProvider(transportManagerCI).write(this.message.getBytes());
        out2.println("Tls library has sent the bytes successfully");
        out2.println("...................");
        out2.println("Write successful");
        out2.println("...................");
        out2.flush();
    }

    private void performCheckConnectionStatus(){
        out2.println("Check connection status");
        out2.println("...................");
        out2.println("Connection Status is : " + TlsProviderHolder.getTlsProvider(transportManagerCI).isConnectionAlive());
        out2.println("...................");
        out2.println("Check connection status complete.");
		out2.println("...................");
		out2.flush();
    }

	private void performGetPattern(){
		out2.println("Get end data pattern");
		out2.println("...................");
		out2.println("End data pattern is : " + TlsProviderHolder.getTlsProvider(transportManagerCI).getEndDataPattern());
		out2.println("...................");
		out2.println("Get end data pattern complete.");
		out2.println("...................");
		out2.flush();
	}

	private String convertMessage(final String message){
		if(message.equalsIgnoreCase("HELLO")){
			return HELLO;
		} else if(message.equalsIgnoreCase("GET")){
			return GET;
		} else if(message.equalsIgnoreCase("CLOSE")){
			return CLOSE;
		} else {
			return GET;
		}
	}

	public static String HELLO = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><hello xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">"
			+ System.getProperty("line.separator")
			+ "<capabilities>"
			+ System.getProperty("line.separator")
			+ "<capability>"
			+ System.getProperty("line.separator")
			+ "urn:ietf:params:netconf:base:1.0"
			+ System.getProperty("line.separator")
			+ "</capability>"
			+ System.getProperty("line.separator")
			+ "</capabilities>"
			+ System.getProperty("line.separator")
			+ "</hello>"
			+ System.getProperty("line.separator") + "]]>]]>";

	public static String CLOSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rpc message-id=\"5\" xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">"
			+ System.getProperty("line.separator")
			+ "<close-session/>"
			+ System.getProperty("line.separator")
			+ "</rpc>"
			+ System.getProperty("line.separator") + "]]>]]>";

	public static String GET = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rpc message-id=\"6\" xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">"
			+ System.getProperty("line.separator")
			+ "<get/>"
			+ System.getProperty("line.separator")
			+ "</rpc>"
			+ System.getProperty("line.separator") + "]]>]]>";
}

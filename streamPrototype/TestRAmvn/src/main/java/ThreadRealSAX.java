import java.io.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.AsyncContext;
import javax.xml.stream.XMLInputFactory;

import org.xml.sax.SAXException;

import com.ericsson.oss.mediation.adapter.ssh.api.*;

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

public class ThreadRealSAX extends Thread {
    private final DelayedInputStream delayedInputStream;
    private static final String JNDI_NAME= "java:/eis/SSHNetConfConnectionFactory";
    private static final String END_PATTERN= "]]>]]>";
    final XMLInputFactory factory = XMLInputFactory.newInstance();

    AsyncContext acontext;
    int counter;
    int timeI;
    PipedOutputStream pipedOutputStream = null;

    public ThreadRealSAX(final AsyncContext acontext, final int counter, final int timeI, final DelayedInputStream delayedInputStream) throws IOException {
        this.acontext = acontext;
        this.counter = counter;
        this.timeI = timeI;
        this.delayedInputStream = delayedInputStream;
        final PipedInputStream inputStream = (PipedInputStream)delayedInputStream.getInputStream();
        pipedOutputStream = new PipedOutputStream(inputStream);
    }

    @Override
    public void run() {
        try {
            testViaServlet();
            //            testNetConf();
            acontext.complete();
        } catch (final Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @throws IOException 
     * @throws SAXException 
     * 
     */
    private void testViaServlet() throws IOException, SAXException {
        SSHConnection[] connArray = new SSHConnection[counter];

        try {

            final Context ctx = new InitialContext();
            final SSHConnectionFactory connectionFactory = (SSHConnectionFactory) ctx.lookup(JNDI_NAME);
            final String ipAddress = "127.0.0.1";//"192.168.100.209"
            final String username = "borusgsn";
            final String password = "sgsn123";
            final int waitTimeForResponse = 3000;
            final String endCommand = "";
            final SSHSessionRequest.SSHSessionRequestBuilder builder = new SSHSessionRequest.SSHSessionRequestBuilder(
                    ipAddress, username, password, waitTimeForResponse, END_PATTERN, endCommand);
            builder.patternPrefixAndSuffix("", "");
            builder.subsystem("netconf");
            builder.port(22);

            final SSHSessionRequest req = builder.buildSshSessionRequest();

            final PrintWriter out2 = acontext.getResponse().getWriter();

            for (int i = 0; i < connArray.length; i++) {
                try {
                    final SSHConnection connection = connectionFactory.getConnection(req);
                    connArray[i] = connection;
                    System.out.println("sending HELLO" );
                    SSHSessionBytesResponse resp = connection.executeCommandBytes(HELLO);
                    out2.println("Hello");
                    readAndParseStream(out2, connection, resp, null);

                    //sleep for some time or not?
                    if ((timeI != 0) && (timeI > 0)) {
                        Thread.sleep(1000 * 60 * timeI);
                    }

                    final long start = System.currentTimeMillis();
                    System.out.println("sending GET" );
                    resp = connection.executeCommandBytes(GET);
                    delayedInputStream.setDataArived(true);
                    final long end = System.currentTimeMillis();
                    final double result = (end - start) / 1000d;
                    out2.println("time: " + result + "seconds");

                    out2.println(" GET respons: ");
                    readAndParseStream(out2, connection, resp, pipedOutputStream);
                    if ((timeI != 0) && (timeI > 0)) {
                        Thread.sleep(1000 * 60 * timeI);
                    }
                    System.out.println("setting data finished");
                    delayedInputStream.setDataFinished(true);
                    resp = connection.executeCommandBytes(CLOSE);
                    out2.println(resp.getResponseBytes().toString());
                    out2.flush();
                    if ((timeI != 0) && (timeI > 0)) {
                        Thread.sleep(1000 * 60 * timeI);
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                    out2.println(e.getMessage());
                    out2.flush();
                }
            }
            for (int i = 0; i < connArray.length; i++) {
                try {
                    if (null != connArray[i]) {
                        System.out.println("Servlet calling closeAndTerminateSSH. " + i);
                        connArray[i].closeAndTerminateSSH();
                    }
                } catch (final Exception e) {
                    out2.println(e.getMessage());
                    out2.flush();
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            connArray = null;
            delayedInputStream.close();
            pipedOutputStream.close();

        }

    }


    /**
     * @param out2
     * @param connection
     * @param resp
     * @param pipedInputStream
     * @param pipedOutputStream
     * @param parser
     * @param source 
     * @throws IOException 
     */
    private void readAndParseStream(final PrintWriter out2, final SSHConnection connection,  SSHSessionBytesResponse resp,
            final PipedOutputStream pipedOutputStream)  {
        System.out.println("reading and parsing stream");
        int executionCount = 0;
        byte[] responseBytes = resp.getResponseBytes();
        while (responseBytes != null){
            final int length = responseBytes.length;
            System.out.println("Response byte size " + length);
            out2.println("Response byte size " + length);
            out2.flush();
            final String msg = new String(responseBytes).trim();
            final int length2 = msg.getBytes().length;
            if(pipedOutputStream!= null){
                synchronized (delayedInputStream) {
                    try {
                        final int availableBytes = delayedInputStream.getInputStream().available();
                        if(availableBytes > 0){
                            System.out.println("WRITE--AVAILABLE BYTES - " + availableBytes);
                            delayedInputStream.wait();
                        }
                        pipedOutputStream.write(msg.getBytes(), 0,length2 );
                        System.out.println(msg + ", Bytes WRITE-" + length2);
                        pipedOutputStream.flush();
                        delayedInputStream.notify();
                    } catch (final IOException | InterruptedException e) {
                        try {
                            delayedInputStream.wait();
                        } catch (final InterruptedException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
            //executing commands again
            resp= connection.readResponseBytes();
            responseBytes = resp.getResponseBytes();
            System.out.println("executionCount - " + executionCount++);
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
            + "</rpc>" + System.getProperty("line.separator") + "]]>]]>";

    public static String GET = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rpc message-id=\"6\" xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">"
            + System.getProperty("line.separator")
            + "<get/>"
            + System.getProperty("line.separator")
            + "</rpc>"
            + System.getProperty("line.separator") + "]]>]]>";
}

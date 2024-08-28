import java.io.*;
import java.util.Deque;
import java.util.LinkedList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.AsyncContext;
import javax.xml.stream.*;

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

public class ThreadReal implements Runnable {

    private static final String JNDI_NAME= "java:/eis/SSHNetConfConnectionFactory";
    private static final String END_PATTERN= "]]>]]>";
    final XMLInputFactory factory = XMLInputFactory.newInstance();

    AsyncContext acontext;
    int counter;
    int timeI;

    public ThreadReal(final AsyncContext acontext, final int counter, final int timeI) {
        this.acontext = acontext;
        this.counter = counter;
        this.timeI = timeI;
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
     * 
     */
    private void testViaServlet() throws IOException {
        SSHConnection[] connArray = new SSHConnection[counter];
        final int dataSize = 1024 * 10;
        final PipedInputStream pipedInputStream = new PipedInputStream(dataSize);
        final PipedOutputStream pipedOutputStream = new PipedOutputStream(pipedInputStream);
        final PipedInputStream pipedInputStream1 = new PipedInputStream(dataSize);
        final PipedOutputStream pipedOutputStream1 = new PipedOutputStream(pipedInputStream1);
        final QueueReader queueReader = new QueueReader();
        final Deque<String> dequeA = queueReader.getDequeA();
        final QueueParser parser = new QueueParser(dequeA);
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
                    SSHSessionBytesResponse resp = connection.executeCommandBytes(HELLO);
                    out2.println("Hello");
                    readAndParseStream(out2, connection, resp,  queueReader, parser);
                    //                    printStream(pipedOutputStream);

                    //sleep for some time or not?
                    if ((timeI != 0) && (timeI > 0)) {
                        Thread.sleep(1000 * 60 * timeI);
                    }

                    final long start = System.currentTimeMillis();

                    resp = connection.executeCommandBytes(GET);

                    final long end = System.currentTimeMillis();
                    final double result = (end - start) / 1000d;
                    out2.println("time: " + result + "seconds");

                    out2.println(" GET respons: ");
                    readAndParseStream(out2, connection, resp,  queueReader, parser);
                    //                    printStream(pipedOutputStream1);
                    if ((timeI != 0) && (timeI > 0)) {
                        Thread.sleep(1000 * 60 * timeI);
                    }

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
            try {
                pipedOutputStream.close();
                pipedOutputStream1.close();
                pipedInputStream.close();
                pipedInputStream1.close();
            } catch (final IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private void readResponse(final PrintWriter out2, final SSHConnection connection, SSHSessionBytesResponse resp, final PipedOutputStream pipedOutputStream, final PipedInputStream pipedInputStream)  {
        byte[] responseBytes = resp.getResponseBytes();
        int executionCount = 0;
        try{
            pipedOutputStream.write(responseBytes);
            while (responseBytes != null){
                final int length = responseBytes.length;
                System.out.println("Response byte size " + length);
                out2.println("Response byte size " + length);
                final String msg = new String(responseBytes).trim();
                out2.println(msg);
                System.out.println("writing in stream " + msg + "-END");
                if((msg != null) && (msg.getBytes().length > 0)){
                    pipedOutputStream.write(msg.getBytes(), 0, msg.getBytes().length);
                    if(executionCount ==0 ){
                        //                        parseXmlStream(pipedInputStream);
                    }
                }
                out2.flush();

                //executing commands again
                resp= connection.readResponseBytes();
                responseBytes = resp.getResponseBytes();
                System.out.println("executionCount - " + executionCount++);
            }}catch (final Throwable e) {
                e.printStackTrace();
            } 

    }

    private void readAndParseStream(final PrintWriter out2, final SSHConnection connection, 
            SSHSessionBytesResponse resp,final QueueReader queueReader, final QueueParser parser ) throws IOException, XMLStreamException, InterruptedException
            {
        int executionCount = 0;
        byte[] responseBytes = resp.getResponseBytes();
        //        final XMLStreamReader reader = factory.createXMLStreamReader(pipedInputStream);
        //        final InputStreamReader reader = new InputStreamReader(pipedInputStream);
        //        final BufferedReader bufferedReader = new BufferedReader(reader);
        while (responseBytes != null){
            final int length = responseBytes.length;
            System.out.println("Response byte size " + length);
            out2.println("Response byte size " + length);
            //            if((msg != null) && (msg.getBytes().length > 0)){
            //                pipedOutputStream.write(msg.getBytes(), 0, msg.getBytes().length);
            //                //                parseXmlStream(reader);
            //                printStream(bufferedReader);
            //            }
            out2.flush();
            queueReader.read(responseBytes);
            parser.parse();
            //executing commands again
            resp= connection.readResponseBytes();
            responseBytes = resp.getResponseBytes();
            System.out.println("executionCount - " + executionCount++);
        }
            }

    private void printStream(final BufferedReader bufferedReader) throws IOException{
        System.out.println("reading stream");
        System.out.println(bufferedReader.readLine());
    }

    private void parseXmlStream(final XMLStreamReader reader) throws XMLStreamException, InterruptedException{
        System.out.println("parsing stream xml");
        String text = null;
        if(reader.hasNext()){
            final int Event = reader.next();
            switch (Event) {
            case XMLStreamConstants.START_ELEMENT: {
                System.out.println("start element");

                break;
            }
            case XMLStreamConstants.CHARACTERS: {
                text = reader.getText().trim();
                System.out.println(text);
                break;
            }
            case XMLStreamConstants.END_ELEMENT: {

                break;
            }
            }
        }
    }

    public static void main(final String[] args) {
        final Deque<String> dequeA = new LinkedList<String>();

        dequeA.add("1");
        dequeA.add("2");
        dequeA.add("3");
        dequeA.add("4");
        dequeA.add("5");

        System.out.println(dequeA.poll());
        System.out.println(dequeA.pop());
        System.out.println(dequeA.pop());
        System.out.println(dequeA.pop());
        System.out.println(dequeA.pop());
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

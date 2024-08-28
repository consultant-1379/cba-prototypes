import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.AsyncContext;
import javax.xml.stream.XMLStreamException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXParseException;

import com.ericsson.oss.mediation.adapter.ssh.api.*;
import com.ericsson.oss.mediation.util.netconf.api.*;
import com.ericsson.oss.mediation.util.netconf.manager.NetconfManagerFactory;
import com.ericsson.oss.mediation.util.transport.api.*;
import com.ericsson.oss.mediation.util.transport.api.factory.TransportFactory;
import com.ericsson.oss.mediation.util.transport.provider.TransportProviderImpl;

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

    AsyncContext acontext;
    int counter;
    int timeI;

    public ThreadReal(AsyncContext acontext, int counter, int timeI) {
        this.acontext = acontext;
        this.counter = counter;
        this.timeI = timeI;
    }

    public void run() {
        try {
//            testViaServlet();
//            testViaServletAsync();
            testNetConf();
            acontext.complete();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public void testNetConf() throws Exception {
        System.out.println("ThreadReal.testNetConf()");
        PrintWriter out2 = acontext.getResponse().getWriter();
        out2.println("start...");
        out2.flush();

        try {
            
            NetconfManager[] connArray = new NetconfManager[counter];
            for (int i = 0; i < connArray.length; i++) {
                try {
                    TransportManagerCI transportManagerCI = new TransportManagerCI();
                    transportManagerCI.setHostname("127.0.0.1"); //127.0.0.1  192.168.100.209
                    transportManagerCI.setUsername("borusgsn"); //borusgsn
                    transportManagerCI.setPassword("sgsn123"); //sgsn123
                    transportManagerCI.setPort(22);
                    transportManagerCI.setSocketConnectionTimeoutInMillis(3000);
                    transportManagerCI.setSessionType(TransportSessionType.SUBSYSTEM);
                    transportManagerCI.setSessionTypeValue("netconf");

                    TransportFactory transportFactory = TransportProviderImpl.getFactory("ssh");

                    TransportManager transportManager = transportFactory.createTransportManager(transportManagerCI);

                    HashMap<String, Object> configProperties = new HashMap<>();
                    String[] capabilities = new String[] { "urn:ietf:params:xml:ns:netconf:base:1.0",
                            "urn:ietf:params:netconf:capability:startup:1.0" };
                    configProperties.put(NetconManagerConstants.CAPABILITIES_KEY, Arrays.asList(capabilities));
                    
                    connArray[i] = NetconfManagerFactory.createNetconfManager(transportManager, configProperties);
                    NetconfResponse netconfResp = connArray[i].connect();
                    if (netconfResp.isError()){
                        out2.println("Connect has error, code: " + netconfResp.getErrorCode() + ", message:"  + netconfResp.getErrorMessage());
                        out2.flush();
                    }

                    //sleep for some time or not?
                    if (timeI != 0 && timeI > 0) {
                        System.out.println("ThreadReal.testNetConf() - going to sleep" );
                        Thread.sleep(1000 * 60 * timeI);
                    }
                    
                    
                    NetconfResponseListener listener = new NetconfResponseListener() {
                        
                        @Override
                        public void warning(SAXParseException e) {
                            
                        }
                        
                        @Override
                        public void startElement(String uri, String localName, String qName, Attributes attributes) {
                           System.out.println("startElementuri " + uri);
                           System.out.println("startElementlocalName " + localName);
                           System.out.println("startElementattributes " + attributes);
                            
                        }
                        
                        @Override
                        public void netconfReaderError(String errorMessage) {
                            // TODO Auto-generated method stub
                            
                        }
                        
                        @Override
                        public void fatalError(SAXParseException e) {
                            // TODO Auto-generated method stub
                            
                        }
                        
                        @Override
                        public void error(SAXParseException e) {
                            // TODO Auto-generated method stub
                            
                        }
                        
                        @Override
                        public void endElement(String uri, String localName, String qName) {
                            System.out.println("endElementuri " + uri);
                            System.out.println("endElementlocalName " + localName);
                            System.out.println("endElementqName " + qName);
                            
                        }
                        
                        @Override
                        public void characters(char[] ch, int start, int length) {
                            System.out.println("characters-" + new String(ch, start, length));
                            
                        }
                    };
                    NetconfResponse resp = connArray[i].get(listener, null, null);
                    
                    
//                    NetconfResponse resp = connArray[i].get();
                    out2.print(resp.getData().length());
                    out2.flush();
                    if (netconfResp.isError()){
                        out2.println("Error is is: " + resp.getErrorMessage());
                    }
                    out2.println("Response size is: " + resp.getData().length());
                    out2.flush();
                } catch (Exception e) {
                    out2.println("Exception: " + e.getMessage());
                    out2.flush();
                }

            }
            
            for (int i = 0; i < connArray.length; i++) {
                NetconfManager manager = connArray[i];
                try {
                    if (null != manager) {
                        if (manager.getStatus().equals(NetconfConnectionStatus.CONNECTED)){
                            System.out.println("Servlet calling testNetConf()..disconnect. " + i);
                            manager.disconnect();
                        } else {
                            System.out.println("status je> " + manager.getStatus());
                        }
                    }
                } catch (Exception e) {
                    out2.println(e.getMessage());
                    out2.flush();
                }
            }
            
            
        } catch (Exception e) {
            out2.println(e.getMessage());
            out2.flush();
        }
        
        out2.println("end");
        out2.flush();
    }

    /**
     * 
     */
    private void testViaServlet() {
        SSHConnection[] connArray = new SSHConnection[counter];
        try {
            Context ctx = new InitialContext();
            SSHConnectionFactory connectionFactory = (SSHConnectionFactory) ctx.lookup(JNDI_NAME);
            String ipAddress = "127.0.0.1";//"192.168.100.209"
            String username = "borusgsn";
            String password = "sgsn123";
            int waitTimeForResponse = 3000;
            String endCommand = "";
            SSHSessionRequest.SSHSessionRequestBuilder builder = new SSHSessionRequest.SSHSessionRequestBuilder(
                    ipAddress, username, password, waitTimeForResponse, END_PATTERN, endCommand);
            builder.patternPrefixAndSuffix("", "");
            builder.subsystem("netconf");

            SSHSessionRequest req = builder.buildSshSessionRequest();

            PrintWriter out2 = acontext.getResponse().getWriter();

            for (int i = 0; i < connArray.length; i++) {
                try {
                    SSHConnection connection = connectionFactory.getConnection(req);
                    connArray[i] = connection;
                    SSHSessionResponse resp = connection.executeCommand(HELLO);
                    String msg = resp.getResponseMessage().toString();
                    if (msg != null && !msg.isEmpty()){
                        out2.println(msg.substring(0, msg.indexOf(END_PATTERN)));
                    } else {
                        out2.println("Response is null or empty");
                    }
                    out2.flush();
                    
                    //sleep for some time or not?
                    if (timeI != 0 && timeI > 0) {
                        Thread.sleep(1000 * 60 * timeI);
                    }
                    
                    long start = System.currentTimeMillis();
                    
                    resp = connection.executeCommand(GET);
                    
                    long end = System.currentTimeMillis();
                    double result = (end - start) / 1000d;
                    out2.println("time: " + result + "seconds");
                    
                    out2.println("Velicina GET responsa: ");
                    out2.println(resp.getResponseMessage().length());
                    out2.flush();
                    if (timeI != 0 && timeI > 0) {
                        Thread.sleep(1000 * 60 * timeI);
                    }
                    
                    resp = connection.executeCommand(CLOSE);
                    out2.println(resp.getResponseMessage());
                    out2.flush();
                    if (timeI != 0 && timeI > 0) {
                        Thread.sleep(1000 * 60 * timeI);
                    }
                } catch (Exception e) {
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
                } catch (Exception e) {
                    out2.println(e.getMessage());
                    out2.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connArray = null;
        }

    }
    
    private void testViaServletAsync() throws IOException {
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
                    SSHSessionResponse resp = connection.executeCommandBytes(HELLO);
                    if (!resp.isSuccess())
                        throw new Exception(resp.getErrorMessage());

                    out2.println("Hello");
                    readAndParseStream(out2, connection);

                    //sleep for some time or not?
                    if ((timeI != 0) && (timeI > 0)) {
                        Thread.sleep(1000 * 60 * timeI);
                    }

                    final long start = System.currentTimeMillis();
                    connection.executeCommandBytes(GET);
                    readAndParseStream(out2, connection);

                    //                    
                    //                    System.out.println("creating stream");
                    //                    inputStream = new InputStreamImpl(connection, GET);
                    //                    System.out.println("Stream Created");
                    //                    System.out.println("creating STAXPArser");
                    //                    final SAXPArser parser = new SAXPArser(inputStream);
                    //                    System.out.println("parser created");
                    //                    parser.parse();

                    final long end = System.currentTimeMillis();
                    final double result = (end - start) / 1000d;
                    out2.println("time: " + result + "seconds");

                    if ((timeI != 0) && (timeI > 0)) {
                        Thread.sleep(1000 * 60 * timeI);
                    }

                    connection.executeCommandBytes(CLOSE);
                    readAndParseStream(out2, connection);
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
        }

    }

    private void readAndParseStream(final PrintWriter out2, final SSHConnection connection) throws IOException,
            XMLStreamException, InterruptedException {
        try {
            boolean endFound = false;
            while (!endFound) {
                byte[] responseBytes = new byte[1024];
                int offset = 0;
                int length = 1024;
                int lengthRead = connection.read(responseBytes, offset, length);
//                out2.println("Response lengthRead " + lengthRead);

                String responseTemp = new String(responseBytes, StandardCharsets.UTF_8).trim();
//                String responseTemp = new String(responseBytes, 0, lengthRead-1, StandardCharsets.UTF_8);

                System.out.println("Response byte  " + responseTemp);
//                out2.println("Response byte " + responseTemp);
//                out2.flush();

//                System.out.println("executionCount - " + executionCount++);

                if (responseTemp.endsWith(END_PATTERN)) {
                    endFound = true;
                    System.out.println("endfound");
                }
            }
        } catch (Exception e) {
            connection.closeAndTerminateSSH();
            e.printStackTrace();
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

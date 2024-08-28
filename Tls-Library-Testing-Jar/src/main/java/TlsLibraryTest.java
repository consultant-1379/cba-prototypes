import com.ericsson.oss.mediation.transport.api.exception.TransportConnectionRefusedException;
import com.ericsson.oss.mediation.transport.api.exception.TransportException;
import com.ericsson.oss.mediation.util.transport.tls.provider.TLSTransportProviderImpl;

/**
 * Created by ecaseal on 19/01/2015.
 */
public class TlsLibraryTest {

    public static void main(String args[]) throws Exception {
        String command, ipAddress;
        int port;

        command = args[0];
        ipAddress = args[1];
        port = Integer.parseInt(args[2]);

        TLSTransportProviderImpl transportProvider = new TLSTransportProviderImpl(ipAddress, port, "]]>]]>");
        performCommand(command, transportProvider);
    }

    private static void performCommand(String command, TLSTransportProviderImpl transportProvider) {
        if (command.equalsIgnoreCase("connect")) {
            performConnect(transportProvider);
            performDisconnect(transportProvider);
        } else if (command.equalsIgnoreCase("executeAsync")) {
            performExecuteAsync(transportProvider);
        } else if (command.equalsIgnoreCase("executeCommand")) {
            performExecuteCommand(transportProvider);
        } else if (command.equalsIgnoreCase("write")) {
            performWrite(transportProvider);
        } else if (command.equalsIgnoreCase("read")) {
            //performRead(transportProvider);
        } else if (command.equalsIgnoreCase("get")) {
            //performGet(transportProvider);
        }
    }

    private static void performConnect(TLSTransportProviderImpl transportProvider) {
        try {
            transportProvider.openSession();
            System.out.println("Connection Status Alive, true or false : " + transportProvider.isConnectionAlive());
        } catch (TransportConnectionRefusedException e) {
            e.printStackTrace();
        }
    }

    private static void performDisconnect(TLSTransportProviderImpl transportProvider) {
        try {
            transportProvider.closeSession();
            System.out.println("Connection Status Alive, true or false : " + transportProvider.isConnectionAlive());
        } catch (TransportException e) {
            e.printStackTrace();
        }
    }

    private static void performExecuteAsync(TLSTransportProviderImpl transportProvider) {
        try {
            performConnect(transportProvider);
            transportProvider.executeCommandAsync(HELLO);
            //transportProvider.write(HELLO.getBytes());
            performDisconnect(transportProvider);
        } catch (TransportException e) {
            e.printStackTrace();
        }
    }

    private static void performExecuteCommand(TLSTransportProviderImpl transportProvider) {
//        try {
//            performConnect(transportProvider);
//            //transportProvider.executeCommandAsync(HELLO);
//            performDisconnect(transportProvider);
//        } catch (TransportException e) {
//            e.printStackTrace();
//        }
    }

    private static void performWrite(TLSTransportProviderImpl transportProvider) {
        try {
            performConnect(transportProvider);
            transportProvider.write(HELLO.getBytes());
            performDisconnect(transportProvider);
        } catch (TransportException e) {
            e.printStackTrace();
        }
    }

    private static void performRead(TLSTransportProviderImpl transportProvider) {
        try {
            performConnect(transportProvider);
            transportProvider.executeCommandAsync(HELLO);
            //TlsResponse response = transportProvider.read();
            //System.out.println(response.getResponseMessageString());
            performDisconnect(transportProvider);
        } catch (TransportException e) {
            e.printStackTrace();
        }
    }

    private static void performGet(TLSTransportProviderImpl transportProvider) {
        try {
            transportProvider.openSession();
            transportProvider.executeCommandAsync(HELLO);
            // TODO add a read here

            transportProvider.executeCommandAsync(GET);
            // TODO add a read here.
            transportProvider.closeSession();
        } catch (TransportConnectionRefusedException e) {
            e.printStackTrace();
        } catch (TransportException e) {
            e.printStackTrace();
        }
    }

    public static final String HELLO = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><hello xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">"
            + System.getProperty("line.separator") + "<capabilities>" + System.getProperty("line.separator") + "<capability>"
            + System.getProperty("line.separator") + "urn:ietf:params:netconf:base:1.0" + System.getProperty("line.separator") + "</capability>"
            + System.getProperty("line.separator") + "</capabilities>" + System.getProperty("line.separator") + "</hello>"
            + System.getProperty("line.separator") + "]]>]]>";

    public static final String CLOSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rpc message-id=\"5\" xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">"
            + System.getProperty("line.separator") + "<close-session/>" + System.getProperty("line.separator") + "</rpc>"
            + System.getProperty("line.separator") + "]]>]]>";

    public static final String GET = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rpc message-id=\"6\" xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">"
            + System.getProperty("line.separator") + "<get/>" + System.getProperty("line.separator") + "</rpc>"
            + System.getProperty("line.separator") + "]]>]]>";
}

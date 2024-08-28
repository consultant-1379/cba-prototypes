package com.ericsson.oss.mediation.netconf;

import com.ericsson.oss.mediation.netconf.rule.SSHServerResource;
import com.ericsson.oss.mediation.netconf.subsystem.DefaultCommandListener;
import com.ericsson.oss.mediation.util.netconf.api.*;
import com.ericsson.oss.mediation.util.netconf.api.exception.NetconfManagerException;
import com.ericsson.oss.mediation.util.netconf.filter.SubTreeFilter;
import com.ericsson.oss.mediation.util.netconf.manager.NetconfManagerFactory;
import com.ericsson.oss.mediation.util.transport.api.TransportManager;
import com.ericsson.oss.mediation.util.transport.api.TransportManagerCI;
import com.ericsson.oss.mediation.util.transport.api.TransportSessionType;
import com.ericsson.oss.mediation.util.transport.api.factory.TransportFactory;
import com.ericsson.oss.mediation.util.transport.provider.TransportProviderImpl;
import com.rexsl.test.XhtmlMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4.class)
public class GetConfigIT {

    @ClassRule
    public static final SSHServerResource sshd = new SSHServerResource(new NestedDataCommandListener());

    private static TransportManagerCI transportManagerCI;

    @BeforeClass
    public static void before() {
        transportManagerCI = new TransportManagerCI();
        transportManagerCI.setHostname(System.getProperty("sshd.host"));
        transportManagerCI.setPort(Integer.parseInt(System.getProperty("sshd.port")));
        transportManagerCI.setUsername(System.getProperty("sshd.username"));
        transportManagerCI.setPassword(System.getProperty("sshd.password"));
        transportManagerCI.setSocketConnectionTimeoutInMillis(20000);
        transportManagerCI.setSessionType(TransportSessionType.SUBSYSTEM);
        transportManagerCI.setSessionTypeValue("netconf");
    }

    @Test
    public void testGetConfig() throws NetconfManagerException {
        final Map<String, Object> hashMap = new HashMap<>();
        hashMap.put(NetconManagerConstants.CAPABILITIES_KEY, Arrays.asList("urn:ietf:params:ns:netconf:base:1.0",
                "urn:ietf:params:xml:ns:netconf:base:1.0", "urn:ietf:params:netconf:base:1.1"));
        final TransportFactory transportFactory = TransportProviderImpl.getFactory("SSH");
        final TransportManager transportManager = transportFactory.createTransportManager(transportManagerCI);
        Assert.assertNotNull("Transport Manager was not built properly.", transportManager);
        final NetconfManager netconfManager = NetconfManagerFactory.createNetconfManager(transportManager, hashMap);
        Assert.assertNotNull("Netconf Manager was not built properly.", netconfManager);
        netconfManager.connect();
        final NetconfResponse getConfigData = netconfManager.getConfig(Datastore.RUNNING, new SubTreeFilter(
                "<MeContext id=\"5\"/>"));
        Assert.assertFalse("<get-config> request returned error", getConfigData.isError());
        Assert.assertNotNull("<get-config> request returned null response", getConfigData.getData());
        final String wrappedGetConfigData = "<root>" + getConfigData.getData() + "</root>";
        MatcherAssert.assertThat(wrappedGetConfigData, XhtmlMatchers.hasXPath("/root/MeContext[@id='5']"));
        MatcherAssert.assertThat(wrappedGetConfigData,
                XhtmlMatchers.hasXPath("/root/filter[@type='subtree' and @source='running']"));
        netconfManager.disconnect();
    }

    private static class NestedDataCommandListener extends DefaultCommandListener {

        @Override
        public void getConfig(final String messageId, Datastore source, Filter filter, final PrintWriter out) {
            out.print("<rpc-reply message-id=\"");
            out.print(messageId);
            out.println("\"");
            out.println("\txmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">");
            out.println("\t<data>");
            out.print("\t<filter type=\"");
            out.print(filter.getType());
            out.print("\" source=\"");
            out.print(source.asParameter());
            out.println("\"/>");
            out.println(filter.asString());
            out.println("\t</data>");
            out.println("</rpc-reply>");
            out.println("]]>]]>");
        }
    }

}

package com.ericsson.oss.mediation.netconf;

import com.ericsson.oss.mediation.netconf.rule.SSHServerResource;
import com.ericsson.oss.mediation.util.netconf.api.NetconManagerConstants;
import com.ericsson.oss.mediation.util.netconf.api.NetconfManager;
import com.ericsson.oss.mediation.util.netconf.api.NetconfResponse;
import com.ericsson.oss.mediation.util.netconf.api.exception.NetconfManagerException;
import com.ericsson.oss.mediation.util.netconf.manager.NetconfManagerFactory;
import com.ericsson.oss.mediation.util.transport.api.TransportManager;
import com.ericsson.oss.mediation.util.transport.api.TransportManagerCI;
import com.ericsson.oss.mediation.util.transport.api.TransportSessionType;
import com.ericsson.oss.mediation.util.transport.api.factory.TransportFactory;
import com.ericsson.oss.mediation.util.transport.provider.TransportProviderImpl;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(JUnit4.class)
public class NetconfManagerIT {

    @ClassRule
    public static final SSHServerResource sshd = new SSHServerResource();

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
    public void testConnectAndDisconnet() throws NetconfManagerException {
        final Map<String, Object> hashMap = new HashMap<>();
        hashMap.put(NetconManagerConstants.CAPABILITIES_KEY, Arrays.asList("urn:ietf:params:ns:netconf:base:1.0",
                "urn:ietf:params:xml:ns:netconf:base:1.0", "urn:ietf:params:netconf:base:1.1"));
        final TransportFactory transportFactory = TransportProviderImpl.getFactory("SSH");
        final TransportManager transportManager = transportFactory.createTransportManager(transportManagerCI);
        Assert.assertNotNull("Transport Manager was not built properly.", transportManager);
        final NetconfManager netconfManager = NetconfManagerFactory.createNetconfManager(transportManager, hashMap);
        Assert.assertNotNull("Netconf Manager was not built properly.", netconfManager);
        netconfManager.connect();
        Assert.assertNotNull("Session id is not defined but it should be", netconfManager.getSessionId());
        netconfManager.disconnect();
    }
}

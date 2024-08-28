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
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class KillSessionIT {

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
    public void testKillSessionOfNeighbor() throws NetconfManagerException {
        final Map<String, Object> hashMap = new HashMap<>();
        hashMap.put(NetconManagerConstants.CAPABILITIES_KEY, Arrays.asList("urn:ietf:params:ns:netconf:base:1.0",
                "urn:ietf:params:xml:ns:netconf:base:1.0", "urn:ietf:params:netconf:base:1.1"));
        final TransportFactory transportFactory = TransportProviderImpl.getFactory("SSH");
        final TransportManager transportManager1 = transportFactory.createTransportManager(transportManagerCI);
        final NetconfManager netconfManager1 = NetconfManagerFactory.createNetconfManager(transportManager1, hashMap);
        netconfManager1.connect();
        final TransportManager transportManager2 = transportFactory.createTransportManager(transportManagerCI);
        final NetconfManager netconfManager2 = NetconfManagerFactory.createNetconfManager(transportManager2, hashMap);
        netconfManager2.connect();
        final NetconfResponse response = netconfManager1.killSession(netconfManager2.getSessionId());
        Assert.assertFalse("kill-session returned error response", response.isError());
        netconfManager1.disconnect();
        final NetconfResponse getResponse = netconfManager2.get();
        Assert.assertTrue("<get> didn't return an error", getResponse.isError());
    }
}

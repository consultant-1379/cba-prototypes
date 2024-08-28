/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ericsson.oss.mediation.util.transport.ssh.manager;
//
//import com.ericsson.oss.mediation.util.netconf.api.NetconManagerConstants;
//import com.ericsson.oss.mediation.util.netconf.api.NetconfConnectionStatus;
//import com.ericsson.oss.mediation.util.netconf.api.NetconfManager;
//import com.ericsson.oss.mediation.util.netconf.api.NetconfResponse;
//import com.ericsson.oss.mediation.util.netconf.manager.NetconfManagerFactory;
//import com.ericsson.oss.mediation.util.transport.api.TransportManager;
//import com.ericsson.oss.mediation.util.transport.api.TransportManagerCI;
//import com.ericsson.oss.mediation.util.transport.api.TransportSessionType;
//import com.ericsson.oss.mediation.util.transport.api.factory.TransportFactory;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;

/**
 * 
 * @author xvaltda
 */

public class Hello2 {

//    private static TransportManagerCI transportManagerCI;
//
//    public Hello2() {
//        //borusgsn/sgsn123 , IP Address: 192.168.100.208
//        transportManagerCI = new TransportManagerCI();
//        transportManagerCI.setHostname("192.168.100.240");
//        transportManagerCI.setPassword("sgsn123");
//        transportManagerCI.setUsername("borusgsn");
//        transportManagerCI.setPort(22);
//        transportManagerCI.setSocketConnectionTimeoutInMillis(1000);
//        transportManagerCI.setSessionType(TransportSessionType.SUBSYSTEM);
//        transportManagerCI.setSessionTypeValue("netconf");
//
//    }
//
//    public static void main(String[] args) {
//
//        try {
//
//            /*
//             * transportManagerCI = new TransportManagerCI();
//             * transportManagerCI.setHostname("192.168.100.240");
//             * transportManagerCI.setPassword("tcu123");
//             * transportManagerCI.setUsername("tcuuser");
//             * transportManagerCI.setPort(22);
//             * transportManagerCI.setSocketConnectionTimeoutInMillis(20000);
//             * transportManagerCI
//             * .setSessionType(TransportSessionType.SUBSYSTEM);
//             * transportManagerCI.setSessionTypeValue("netconf");
//             */
//
//            transportManagerCI = new TransportManagerCI();
//            transportManagerCI.setHostname("192.168.100.208");
//            transportManagerCI.setPassword("sgsn123");
//            transportManagerCI.setUsername("borusgsn");
//            transportManagerCI.setPort(22);
//            transportManagerCI.setSocketConnectionTimeoutInMillis(20000);
//            transportManagerCI.setSessionType(TransportSessionType.SUBSYSTEM);
//            transportManagerCI.setSessionTypeValue("netconf");
//
//            Map<String, Object> configProperties = new HashMap<String, Object>();
//
//            List<String> list = new ArrayList<String>();
//            //list.add("urn:ietf:params:netconf:base:1.0");
//            list.add("urn:ietf:params:xml:ns:netconf:base:1.1");
//            configProperties.put(NetconManagerConstants.CAPABILITIES_KEY, list);
//
//            TransportFactory sshTransportManaager = new SSHTransportFactory();
//            TransportManager sshTransportManager = sshTransportManaager
//                    .createTransportManager(transportManagerCI);
//            NetconfManager netconf;
//            netconf = NetconfManagerFactory.createNetconfManager(
//                    sshTransportManager, configProperties);
//
//            NetconfResponse response = netconf.connect();
//            System.out.println("Connect: " + response.isError());
//
//            System.out.println("SessionId waiting: " + netconf.getSessionId());
//            response = netconf.get();
//            System.out.println("NetconfReponse Data: " + response.isError() + " error data: "+response.getErrorMessage());
//            //netconf.get();
//
//            //Thread.sleep(1000);
//            if (netconf.getStatus() == NetconfConnectionStatus.CONNECTED)
//            netconf.disconnect();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
}
///*------------------------------------------------------------------------------
// *******************************************************************************
// * COPYRIGHT Ericsson 2013
// *
// * The copyright to the computer program(s) herein is the property of
// * Ericsson Inc. The programs may be used and/or copied only with written
// * permission from Ericsson Inc. or in accordance with the terms and
// * conditions stipulated in the agreement/contract under which the
// * program(s) have been supplied.
// *******************************************************************************
// *----------------------------------------------------------------------------*/
//package com.ericsson.cba.prototype.protocol.sshclient.handler;
//
//import static com.ericsson.cba.prototype.protocol.sshclient.handler.StaticValues.getChassisQuery;
//
//import java.util.List;
//
//import javax.security.auth.login.Configuration;
//
//import com.ericsson.cba.prototype.protocol.sshclient.exception.SshTransportException;
//import com.ericsson.cba.prototype.protocol.sshclient.model.*;
//import com.ericsson.cba.prototype.protocol.sshclient.ssh.*;
//import com.ericsson.cba.prototype.protocol.sshclient.util.Configurator;
//import com.ericsson.oss.itpf.common.event.handler.EventHandlerContext;
//
//public class BulkDeviceTest {
//
//    List<Device> deviceList = null;
//    private EventHandlerContext unDecoratorContext = null;
//
//    private SshConnection connectDevice(final Device device, final SshClient sshClient) {
//        SshConnection sshConnection = null;
//        try {
//            sshConnection = sshClient.createSshConnection(device.getIp(), device.getPort(), device.getUserName(), device.getPassword());
//        } catch (final Exception e) {
//            System.out.println("Connection cannot able to established to " + device.toString());
//        }
//        return sshConnection;
//    }
//
//    private SshSession createSession(final SshConnection sshConnection, final Device device, final SshClient sshClient) {
//        SshSession sshSession = null;
//        try {
//            sshSession = sshClient.createSessionToSubsystem(sshConnection, SessionType.SUBSYSTEM, "netconf");
//        } catch (final Exception e) {
//            System.out.println("Connection not able to established to " + device.toString());
//        }
//        return sshSession;
//
//    }
//
//    private Configuration getMockedConfiguration(final boolean isDecorator) {
//        final Configuration mockedConfiguration = Mockito.mock(Configuration.class);
//        if (isDecorator) {
//            when(mockedConfiguration.getStringProperty("enrichmentType")).thenReturn("undecorator");
//        }
//        return mockedConfiguration;
//    }
//
//    public String handleDevice(final Device device) throws SshTransportException {
//        final SshClient sshClient = new JschSshHandler();
//        SshConnection sshConnection = null;
//        SshSession sshSession = null;
//        System.out.println("Attempt to connect  " + device.toString());
//        long started = System.currentTimeMillis();
//        sshConnection = connectDevice(device, sshClient);
//        System.out.println("Connected in " + (System.currentTimeMillis() - started) + " ms.");
//        System.out.println("Attempt to create session @" + device.toString());
//        started = System.currentTimeMillis();
//        sshSession = createSession(sshConnection, device, sshClient);
//        System.out.println("Session created in " + (System.currentTimeMillis() - started) + " ms.");
//        System.out.println("Attempt to run " + getChassisQuery.getQuery() + " @ " + device.toString());
//        started = System.currentTimeMillis();
//        final String queryResponse = runQuery(sshSession, device, sshClient);
//        System.out.println(device.toString() + " <-- response --> " + queryResponse);
//        System.out.println("Query run in " + (System.currentTimeMillis() - started) + " ms.");
//        sshClient.removeConnection(sshConnection);
//        sshClient.disconnectSession(sshSession);
//        return queryResponse;
//    }
//
//    @Before
//    public void init() {
//        mockEventHandlerUnDecoratorContext();
//        deviceList = new Configurator().getAvailableDevices();
//        Device.print(deviceList);
//    }
//
//    private void mockEventHandlerUnDecoratorContext() {
//        unDecoratorContext = Mockito.mock(EventHandlerContext.class);
//        final Configuration mockedConfiguration = getMockedConfiguration(false);
//        when(unDecoratorContext.getEventHandlerConfiguration()).thenReturn(mockedConfiguration);
//    }
//
//    private String runQuery(final SshSession sshSession, final Device device, final SshClient sshClient) {
//        String queryResponse = null;
//        try {
//            queryResponse = sshClient.runQuery(sshSession, getChassisQuery);
//        } catch (final Exception e) {
//            System.out.println("Query : " + getChassisQuery + " not able to run " + " device: " + device.toString());
//        }
//        return queryResponse;
//    }
//
//    // @Test
//    public void shouldRunChassisQuery() throws SshTransportException {
//        final String queryResponse = handleDevice(new Device("root", "root", "155.53.174.21", "155.53.174.21", 5566));
//        final RpcXmlHandler rpcXmlHandler = new RpcXmlHandler();
//        rpcXmlHandler.init(unDecoratorContext);
//        final Exchange exchange = new Exchange();
//        exchange.setInput(queryResponse);
//        rpcXmlHandler.onEvent(exchange);
//        System.out.println(exchange.getOutput().toString());
//
//    }
//
//    // @Test
//    public void shouldRunPortQueryOnEachDevice() throws SshTransportException {
//        if (deviceList.isEmpty()) {
//            return;
//        }
//        for (final Device device : deviceList) {
//            handleDevice(device);
//        }
//
//    }
//
//}

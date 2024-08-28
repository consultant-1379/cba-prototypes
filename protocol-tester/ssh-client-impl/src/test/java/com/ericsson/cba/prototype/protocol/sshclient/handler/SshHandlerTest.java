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
//import java.util.ArrayList;
//import java.util.Collection;
//
//import javax.security.auth.login.Configuration;
//
//import com.ericsson.cba.prototype.protocol.sshclient.handler.SshHandler;
//import com.ericsson.cba.prototype.protocol.sshclient.model.Exchange;
////import org.junit.Test;
//import com.ericsson.oss.itpf.common.event.handler.EventSubscriber;
//
//public class SshHandlerTest {
//
//    private class MockEventSubscriber implements EventSubscriber {
//
//        private String deviceResponse = null;
//
//        public String getEventReceived() {
//            return deviceResponse;
//        }
//
//        public String getIdentifier() {
//            return "mockSubscriber";
//        }
//
//        public void sendEvent(final Object event) {
//            deviceResponse = (String) event;
//        }
//
//    }
//
//    private SshHandler sshHandler = null;
//
//    private EventHandlerContext context = null;
//
//    private final Collection<EventSubscriber> eventSubscribers = new ArrayList<EventSubscriber>();
//
//    private final MockEventSubscriber mockedEventSubscriber = new MockEventSubscriber();
//
//    private Configuration getMockedConfiguration() {
//        final Configuration mockedConfiguration = Mockito.mock(Configuration.class);
//        when(mockedConfiguration.getStringProperty("hostname")).thenReturn("lena-2.lab.redback.com");
//        when(mockedConfiguration.getIntProperty("port")).thenReturn(55118);
//        when(mockedConfiguration.getIntProperty("socketTimeoutValueInMilli")).thenReturn(-1);
//        when(mockedConfiguration.getStringProperty("username")).thenReturn("root");
//        when(mockedConfiguration.getStringProperty("password")).thenReturn("root");
//        when(mockedConfiguration.getStringProperty("sessionType")).thenReturn("subsystem");
//        when(mockedConfiguration.getStringProperty("sessionTypeValue")).thenReturn("netconf");
//        return mockedConfiguration;
//    }
//
//    //@Before
//    public void init() {
//        sshHandler = new SshHandler();
//        eventSubscribers.add(mockedEventSubscriber);
//        mockEventHandlerContext();
//        sshHandler.init(context);
//
//    }
//
//    private EventHandlerContext mockEventHandlerContext() {
//        context = Mockito.mock(EventHandlerContext.class);
//        final Configuration mockedConfiguration = getMockedConfiguration();
//        when(context.getEventHandlerConfiguration()).thenReturn(mockedConfiguration);
//        when(context.getEventSubscribers()).thenReturn(eventSubscribers);
//        return context;
//    }
//
//    //@Test
//    public void shouldHandleQueryEvent() {
//        final Exchange exchange = new Exchange();
//        exchange.setInput(getChassisQuery.getQuery());
//        sshHandler.onEvent(exchange);
//        assertNotNull(exchange.getOutput());
//    }
//
//}
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
package com.ericsson.oss.cba.mediation.mocks.service.registration;

import java.util.ArrayList;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.itpf.sdk.eventbus.model.EventSender;
import com.ericsson.oss.itpf.sdk.eventbus.model.annotation.Modeled;
import com.ericsson.oss.mediation.core.api.event.MediationServiceRegisteredEvent;
import com.ericsson.oss.mediation.service.MediationServiceClient;

@Singleton
@Startup
public class RegistrationEventSender {
    
    @Inject
    @Modeled
    private EventSender<MediationServiceRegisteredEvent> registeredEventSender;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationEventSender.class);
    
    private final ArrayList<MediationServiceRegisteredEvent> eventList = new ArrayList<>();
    
    private static final String CM_CHANNEL_ID = "unused_channel";
       
    private static final String APP_ID = "APP";
    
    private static final String CM = "CM";
    
    private static final String NODE_ID = System.getProperty("com.ericsson.oss.sdk.node.identifier");
    
    private static final String PORT = System.getProperty("jacorb.port", "3528");
    
    private static final String ACTUAL_PORT = String.valueOf(Integer.parseInt(PORT));
    
    private static final String IP_ADDRESS = System.getProperty("jboss.bind.address.unsecure", "localhost");

    private static final String JNDI_ID = "corbaname:iiop:" 
                                           + IP_ADDRESS + ":"
                                           + ACTUAL_PORT + "#" 
                                           + MediationServiceClient.VERSION_INDEPENDENT_JNDI_NAME;
    
    private static final String DEBUG_MSG = "Registration attempt being sent for protocol: {}, nodeId: {}  " +
                                "appId: {}, channelId: {}, msJndi: {}";
    
    @PostConstruct
    private void sendRegistrationEvents(){
        createRegistrationEvents();
        for (MediationServiceRegisteredEvent event : eventList){
            sendEvent(event);
        }
    }
    
    private void sendEvent(MediationServiceRegisteredEvent event){
        registeredEventSender.send(event);
    }
    
    private void createRegistrationEvents(){
        eventList.add(createRegistrationEvent(APP_ID, 
                                              CM_CHANNEL_ID, 
                                              CM, 
                                              NODE_ID, 
                                              JNDI_ID)
                );
    }
    
   private MediationServiceRegisteredEvent createRegistrationEvent(final String appId,
                                                                   final String channelId,
                                                                   final String protocol,
                                                                   final String nodeId,
                                                                   final String msJndi){
       
       LOGGER.info(DEBUG_MSG, protocol, nodeId, appId, channelId, msJndi);
       return new MediationServiceRegisteredEvent(
               appId, 
               channelId, 
               UUID.randomUUID().toString(),
               protocol, 
               nodeId,
               msJndi);
   }

}

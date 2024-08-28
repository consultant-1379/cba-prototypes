package com.ericsson.nms.mediation.component.dps.listeners;

import com.ericsson.oss.itpf.datalayer.dps.notification.DpsNotificationConfiguration;
import com.ericsson.oss.itpf.datalayer.dps.notification.event.DpsObjectCreatedEvent;
import com.ericsson.oss.itpf.sdk.eventbus.annotation.Consumes;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;


@ApplicationScoped
public class DpsListener {

    @Inject
    private Logger logger;

    public void receiveMessage(
            @Observes @Consumes(endpoint = DpsNotificationConfiguration.DPS_EVENT_NOTIFICATION_CHANNEL_URI) final DpsObjectCreatedEvent event) {
        logger.debug("receiveMessage {}", event);
    }

}

package com.ericsson.nms.mediation.component.dps.handler;

import com.ericsson.oss.itpf.common.event.ComponentEvent;
import com.ericsson.oss.itpf.common.event.handler.EventHandlerContext;
import com.ericsson.oss.itpf.common.event.handler.TypedEventInputHandler;
import com.ericsson.oss.itpf.common.event.handler.annotation.EventHandler;
import com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService;
import com.ericsson.oss.itpf.datalayer.dps.persistence.ManagedObject;
import com.ericsson.oss.itpf.sdk.core.annotation.EServiceRef;
import com.ericsson.oss.mediation.cba.handlers.netconf.AbstractNetconfHandler;
import com.ericsson.oss.mediation.util.netconf.api.NetconfResponse;
import com.ericsson.oss.mediation.util.netconf.api.exception.NetconfManagerException;
import org.slf4j.Logger;

import javax.inject.Inject;

@EventHandler(contextName = "")
public class SyncHandler extends AbstractNetconfHandler {

    @Inject
    protected Logger logger;

    @EServiceRef
    private DataPersistenceService dps;

    @Override
    public ComponentEvent onEvent(final ComponentEvent componentEvent) {
        final ComponentEvent result = super.onEvent(componentEvent);
        logger.debug("onEvent {}", componentEvent);
        final String moFdn = (String) componentEvent.getHeaders().get("moFdn");
        logger.debug("moFdn {}", moFdn);
        final ManagedObject managedElement = dps.getLiveBucket().findMoByFdn(moFdn);
        logger.debug("ManagedElement {}", managedElement);
        try {
            final NetconfResponse response = netconfManager.get();
            logger.debug("ManagedElement {}", response);
        } catch (NetconfManagerException e) {
            logger.error("I wasn't able to do a get request to a netconf server", e);
        }
        return result;
    }

    @Override
    public void init(final EventHandlerContext eventHandlerContext) {
        logger.debug("init {}", eventHandlerContext);
    }

    @Override
    public void destroy() {
        logger.debug("destroy");
    }
}

package com.ericsson.nms.mediation.component.dps.handler;

import com.ericsson.oss.itpf.common.event.ComponentEvent;
import com.ericsson.oss.itpf.common.event.handler.EventHandlerContext;
import com.ericsson.oss.itpf.common.event.handler.TypedEventInputHandler;
import com.ericsson.oss.itpf.common.event.handler.annotation.EventHandler;
import com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService;
import com.ericsson.oss.itpf.datalayer.dps.persistence.ManagedObject;
import com.ericsson.oss.itpf.sdk.core.annotation.EServiceRef;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Map;

@EventHandler(contextName = "")
public class ManualSyncNodeHandler implements TypedEventInputHandler {

    private static final String ADDRESS_PARAM = "ipAddress";
    private static final String PORT = "port";
    private static final String FDN_PARAM = "fdn";

    @Inject
    protected Logger logger;

    @EServiceRef
    private DataPersistenceService dps;

    private final ThreadLocal<Map<String, Object>> contextHeaders = new ThreadLocal<>();

    @Override
    public ComponentEvent onEvent(final ComponentEvent componentEvent) {
        logger.debug("onEvent");
        final Map<String, Object> inputEventHeaders = componentEvent.getHeaders();
        logger.debug("Input Event Headers retrieved: '{}'", inputEventHeaders);
        componentEvent.getHeaders().put("moFdn", contextHeaders.get().get("moFdn"));
        contextHeaders.remove();
        return componentEvent;
    }

    @Override
    public void init(final EventHandlerContext ctx) {
        logger.debug("init");
        final Map<String, Object> properties = ctx.getEventHandlerConfiguration().getAllProperties();
        final String address = (String) properties.get(ADDRESS_PARAM);
        logger.debug("Address retrieved: {}", address);
        final String nodeFdn = (String) properties.get(FDN_PARAM);
        logger.debug("FDN retrieved: {}", nodeFdn);
        final Integer port = (Integer) properties.get(PORT);
        logger.debug("Port retrieved: {}", port);
        final ManagedObject cmFunction = dps.getLiveBucket().findMoByFdn(nodeFdn);
        final String neName = cmFunction.getParent().getName();
        contextHeaders.set(properties);
        contextHeaders.get().put("moFdn", String.format("MeContext=%s,ManagedElement=%s", neName, cmFunction.getName()));
    }

    @Override
    public void destroy() {
        logger.debug("destroy");
    }
}

/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.mediation.pm.handlers;

import static com.ericsson.oss.mediation.pm.handlers.constants.HandlerConstants.EVENT_TYPE_PROPERTY_NAME;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.itpf.common.event.ComponentEvent;
import com.ericsson.oss.itpf.common.event.handler.EventHandlerContext;
import com.ericsson.oss.itpf.common.event.handler.TypedEventInputHandler;
import com.ericsson.oss.itpf.common.event.handler.annotation.EventHandler;
import com.ericsson.oss.mediation.pm.handlers.exceptions.NetconfManagerNotFoundException;
import com.ericsson.oss.mediation.pm.handlers.strategies.PMOpsFacade;
import com.ericsson.oss.mediation.pm.handlers.util.MediationTaskRequestAttributeExtractor;
import com.ericsson.oss.mediation.util.netconf.api.NetconManagerConstants;
import com.ericsson.oss.mediation.util.netconf.api.NetconfManager;
import com.ericsson.oss.services.pm.initiation.cpp.jobs.constants.PMEventConstants;


/**
 * @author sunrise
 */
@EventHandler(contextName = "")
public class InitiationHandler implements TypedEventInputHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    
    @Inject
    private PMOpsFacade pmOpsFacade;
    
    private final Map<String, Object> eventHandlerContextAttributes = new HashMap<String, Object>();

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.oss.itpf.common.event.handler.TypedEventInputHandler#onEvent(java.lang.Object)
     * 
     * @param inputEvent the event to be handled.
     * 
     * @return the handled event.
     */
    @Override
    public ComponentEvent onEvent(final ComponentEvent inputEvent) {

        LOGGER.debug("<------ {} --- event received ---->", getClass().getSimpleName());

        if (inputEvent == null) {
        	LOGGER.error("Scanner operation cannot be performed as event received from upstream system is null...");
        	throw new IllegalArgumentException("ERROR: The inputEvent is null");
        }

        /* Picking tags */
        final Map<String, Object> eventHeaders = inputEvent.getHeaders();       
        
        validateEventHeader(eventHeaders);
        
        eventHeaders.putAll(eventHandlerContextAttributes);
        LOGGER.debug("EventHandler headers are {}", eventHeaders);
        
        MediationTaskRequestAttributeExtractor.putMediationTaskRequestAttributesInHandlerSpecificHeaders(eventHeaders);

        validateNetconfManager(eventHeaders);
        
        handleEventType(eventHeaders);
        
        return inputEvent;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.oss.itpf.common.event.handler.EventHandler#init(com.ericsson.oss.itpf.common.event.handler.EventHandlerContext)
     */
    @Override
    public void init(final EventHandlerContext ctx) {
        LOGGER.debug("<------ {} --- init method called ---->", getClass().getSimpleName());
        eventHandlerContextAttributes.putAll(ctx.getEventHandlerConfiguration().getAllProperties());
        LOGGER.debug("Init eventHandlerContextAttributes: {}", eventHandlerContextAttributes);
    }

    @Override
    public void destroy() {
    }
   
    /**
     * This method checks if the event headers being passed in are null or
     * empty.
     * 
     * @param handlerEventHeaders
     *            event headers of type Map<String, Object>
     * @throws IllegalArgumentException
     *             IllegalArgumentException is thrown if the event headers are
     *             null or are empty
     * 
     */
    private final void validateEventHeader(final Map<String, Object> eventHeaders) throws IllegalArgumentException {
        if (eventHeaders == null || eventHeaders.isEmpty()) {
            LOGGER.error("Scanner operation cannot be performed as event headers received from upstream system are null...");
            throw new IllegalArgumentException("Event headers cannot be null or emtpy...");
        }
    }
    
   /**
     * This method verify the presence of NetconfManager in the event headers.
     * 
     * @param eventHeaders
     *            event headers of type Map<String, Object>
     * 
     * @throws NetconfManagerNotFoundException
     *            NetconfManagerNotFoundException is thrown if the NetconfManager is null
     * 
     */
    protected final void validateNetconfManager(final Map<String, Object> eventHeaders) throws NetconfManagerNotFoundException {
    	final NetconfManager netconfManager = (NetconfManager)eventHeaders.get(NetconManagerConstants.NETCONF_MANAGER_ATTR);

        LOGGER.debug("Retrieved attributes for handler, netconfManager: {}", netconfManager);

        if (netconfManager == null) {
            LOGGER.error("Error: Netconf Manager NULL");
            throw new NetconfManagerNotFoundException("Netconf Manager is null and not supplied with event headers.");
        }
    }
    
    /**
     * This method extracts and returns the pmEventType from the event headers.
     * 
     * @param eventHeaders
     *            event headers of type Map<String, Object>
     * 
     * @return EventType 
     * 
     * @throws IllegalArgumentException
     *             IllegalArgumentException is thrown if the event type is null
     *             or is empty
     */
    private final String getEventType(final Map<String, Object> eventHeaders) {
    	final String eventType = (String)eventHeaders.get(EVENT_TYPE_PROPERTY_NAME);
    	
    	LOGGER.debug("Retrieved attributes for handler, eventType: {}", eventType);

        if (eventType == null || eventType.isEmpty()) {
        	LOGGER.error("Error: pm event type NULL or EMPTY");
            throw new IllegalArgumentException("Event type is null or empty in event headers...");
        }
        
        return eventType;
    }
    
    /**
     * This method decides what operations are required based on the event type
     * attribute in the header and then handles them by either creating and using objects
     * of type PerformanceMonitoringActivator or
     * PerformanceMonitoringDeacitvator.
     * 
     * @param eventHeaders
     *            event headers of type Map<String, Object>
     * 
     * @return componentEvent 
     *            ComponentEvent used in the next handler
     * 
     */
    private void handleEventType(final Map<String, Object> eventHeaders) {
        final String eventType = getEventType(eventHeaders);

        switch (eventType) {
        case PMEventConstants.EVENT_TYPE_ACTIVATION:
            LOGGER.debug("Activation required...");

            this.pmOpsFacade.activatePerformanceMonitoring(eventHeaders);
            break;

        case PMEventConstants.EVENT_TYPE_DEACTIVATION:
            LOGGER.debug("Deactivation required...");

            this.pmOpsFacade.deactivatePerformanceMonitoring(eventHeaders);
            break;
            
        case PMEventConstants.EVENT_TYPE_SCANNER_RESUMPTION:
            LOGGER.debug("Resume required...");

            this.pmOpsFacade.resumePerformanceMonitoring(eventHeaders);
            break;
            
        case PMEventConstants.EVENT_TYPE_SCANNER_SUSPENSION:
            LOGGER.debug("Suspend required...");

            this.pmOpsFacade.suspendPerformanceMonitoring(eventHeaders);
            break;
            
        default:
        	LOGGER.error("Error: pm event type unknown");
        	throw new IllegalArgumentException("Event type is unknown in event headers...");
        }
    }

}

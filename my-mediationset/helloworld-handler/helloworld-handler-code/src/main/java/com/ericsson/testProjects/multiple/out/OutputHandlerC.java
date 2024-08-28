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
package com.ericsson.testProjects.multiple.out;


import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.itpf.common.event.ComponentEvent;
import com.ericsson.oss.itpf.common.event.handler.*;
import com.ericsson.oss.itpf.common.event.handler.annotation.EventHandler;

/**
 * @author ecaoodo
 *
 */
@EventHandler
public class OutputHandlerC implements TypedEventInputHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(OutputHandlerC.class);
    
    
    /* (non-Javadoc)
     * @see com.ericsson.oss.itpf.common.event.handler.EventHandler#init(com.ericsson.oss.itpf.common.event.handler.EventHandlerContext)
     */
    @Override
    public void init(EventHandlerContext paramEventHandlerContext) {
        LOGGER.info("<------ init method called ---->");
        LOGGER.info("<------ EventHandlerContext->EventHandlerConfiguration: {} ---->", paramEventHandlerContext.getEventHandlerConfiguration()
                .getAllProperties());
    }

    /* (non-Javadoc)
     * @see com.ericsson.oss.itpf.common.Destroyable#destroy()
     */
    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        
    }



	/* (non-Javadoc)
	 * @see com.ericsson.oss.itpf.common.event.handler.TypedEventInputHandler#onEvent(com.ericsson.oss.itpf.common.event.ComponentEvent)
	 */
    @Override

    public ComponentEvent onEvent(final ComponentEvent componentEvent) {
    	LOGGER.debug("onEvent is called with headers before: {}", componentEvent.getHeaders());
    	
    	List<String> handlerTraverse = new ArrayList<String>();
    	handlerTraverse.add("OutputHandlerA");
    	
    	
    	componentEvent.getHeaders().put("HandlerTraverseList", handlerTraverse);
        
        LOGGER.debug("onEvent is called with headers after: {}", componentEvent.getHeaders());
        
        LOGGER.info("[SAMPLE] Executing Handler's onEventWithResults method...");
        final File file = new File("target/OutputHandlerA.txt");
        try {
            file.createNewFile();
            final FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("OutputHandlerA File is created");
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.info("[SAMPLE] Created file ");

        return componentEvent;
    }

}

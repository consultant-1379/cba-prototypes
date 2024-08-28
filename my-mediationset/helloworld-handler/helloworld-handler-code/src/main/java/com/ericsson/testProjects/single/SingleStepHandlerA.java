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
package com.ericsson.testProjects.single;


import java.io.File;
import java.io.FileWriter;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.itpf.common.event.handler.EventHandlerContext;
import com.ericsson.oss.itpf.common.event.handler.EventInputHandler;
import com.ericsson.oss.itpf.common.event.handler.annotation.EventHandler;
import com.ericsson.testProjects.handler.util.GreeterImpl;

/**
 * @author ecaoodo
 *
 */
@EventHandler
public class SingleStepHandlerA implements EventInputHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(SingleStepHandlerA.class);
    @Inject
    private GreeterImpl greeter;
    
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
     * @see com.ericsson.oss.itpf.common.event.handler.EventInputHandler#onEvent(java.lang.Object)
     */
    @Override
    public void onEvent(Object inputEvent) {
        LOGGER.info("[SAMPLE] Executing Handler's onEvent method...");

    }  

    /* (non-Javadoc)
     * @see com.ericsson.oss.itpf.common.event.handler.EventInputHandler#onEventWithResults(java.lang.Object)
     */
    @Override
    public Object onEventWithResults(Object object) {
        LOGGER.info("[SAMPLE] Executing Handler's onEventWithResults method...");
        final File file = new File("target/SingleStepHandlerA.txt");
        try {
            file.createNewFile();
            final FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("SingleStepHandlerA File is created");
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.info("[SAMPLE] Created file ");
        return null;
    }

}

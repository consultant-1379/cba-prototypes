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
package com.ericsson.testProjects.handler;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.itpf.common.event.handler.EventHandlerContext;
import com.ericsson.oss.itpf.common.event.handler.EventInputHandler;
import com.ericsson.oss.itpf.common.event.handler.annotation.EventHandler;
import com.ericsson.testProjects.handler.util.GreeterImpl;

/**
 * Hello World handler example using EventInputHandler interface This handler also demonstrates use of CDI injection within handlers
 * 
 * @author edejket
 * 
 */
@EventHandler
public class HelloWorldHandler implements EventInputHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldHandler.class);

    @Inject
    private GreeterImpl greeter;

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.oss.itpf.common.event.handler.EventInputHandler#onEvent(java.lang.Object)
     */
    @Override
    public void onEvent(final Object inputEvent) {
        LOGGER.debug("Handler recieved on event with no result: {} ", inputEvent);
        //greeter.sayHello("Hello World!");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.oss.itpf.common.event.handler.EventHandler#init(com.ericsson.oss.itpf.common.event.handler.EventHandlerContext)
     */
    @Override
    public void init(EventHandlerContext paramEventHandlerContext) {
        LOGGER.debug("<------ init method called ---->");
        LOGGER.debug("<------ EventHandlerContext->EventHandlerConfiguration: {} ---->", paramEventHandlerContext.getEventHandlerConfiguration()
                .getAllProperties());
    }

    @Override
    public void destroy() {
    }

    @Override
    public Object onEventWithResults(Object inputEvent) {
        LOGGER.debug("Handler recieved on event with result: {} ", inputEvent);
        greeter.sayHello("Hello World!");
        return null;
    }
}

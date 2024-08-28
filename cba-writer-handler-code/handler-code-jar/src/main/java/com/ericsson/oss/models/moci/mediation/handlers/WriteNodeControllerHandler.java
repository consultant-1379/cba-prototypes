/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2015
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.models.moci.mediation.handlers;

import static com.ericsson.oss.models.moci.mediation.handlers.api.Constants.*;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.itpf.common.event.handler.EventHandlerContext;
import com.ericsson.oss.itpf.common.event.handler.ResultEventInputHandler;
import com.ericsson.oss.itpf.common.event.handler.annotation.EventHandler;
import com.ericsson.oss.itpf.common.event.handler.exception.EventHandlerException;
import com.ericsson.oss.mediation.modeling.modelservice.typed.configuration.PersistenceOperationType;
import com.ericsson.oss.models.moci.mediation.handlers.executors.Action;
import com.ericsson.oss.models.moci.mediation.handlers.executors.CbaExecutor;
import com.ericsson.oss.models.moci.mediation.handlers.instrumentation.WriteNodeInstrumentationBean;
import com.ericsson.oss.models.moci.mediation.handlers.util.RequestData;

/**
 * The Class WriteNodeControllerHandler is used as a main entry point for all the write operations (CUDA).
 */
@EventHandler
public class WriteNodeControllerHandler implements ResultEventInputHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WriteNodeControllerHandler.class);
    private Map<String, CbaExecutor> executors;
    private CbaExecutor executor;

    protected Map<String, Object> headers;
    @Inject
    private WriteNodeInstrumentationBean instrumentationBean;
    @Inject
    private Action actionExecutor;

    @Inject
    protected RequestData requestData;

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.oss.itpf.common.event.handler.EventHandler#init(com.ericsson .oss.itpf.common.event.handler.EventHandlerContext).
     */
    @Override
    public void init(final EventHandlerContext ctx) {
        parseHeaders(ctx);
        final String clientOperation = requestData.getClientOperation();
        instrumentationBean.recordWriteNodeEntryPoint(requestData.getFdn(), clientOperation, requestData.getActionName());
        executors = createExecutors();
        executor = executors.get(clientOperation);
        if (executor == null) {
            instrumentationBean.recordError(requestData.getFdn(), clientOperation, INVALID_OPERATION);
            throw new EventHandlerException(EXCEPTION_MSG + clientOperation + RIGHT_SQUARE_BRAC);
        }
        executor.init(requestData);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.oss.itpf.common.event.handler.EventInputHandler#onEvent( java.lang.Object)
     */
    @Override
    public void onEvent(final Object inputEvent) {
        onEventWithResult(inputEvent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.oss.itpf.common.Destroyable#destroy()
     */
    @Override
    public void destroy() {
        this.executor = null;
        headers = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.oss.itpf.common.event.handler.EventInputHandler#onEventWithResults(java.lang.Object)
     */
    @Override
    public Object onEventWithResult(final Object inputEvent) {
        final Object result = executor.execute();
        executor.destroy();
        instrumentationBean.recordWriteNodeExitPoint(requestData.getFdn(), requestData.getClientOperation(), requestData.getActionName());
        return result;
    }

    private void parseHeaders(final EventHandlerContext ctx) {
        headers = ctx.getEventHandlerConfiguration().getAllProperties();
        LOGGER.debug("Extracted headers with data: [{}]", headers);
        requestData.setHeaders(headers);
    }

    private Map<String, CbaExecutor> createExecutors() {
        executors = new HashMap<String, CbaExecutor>();
        executors.put(PersistenceOperationType.ACTION.name(), actionExecutor);
        return executors;
    }
}
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
package com.ericsson.oss.mediation.pm.handlers.strategies;

//EPICROB: TO DO handling of SystemRecorder. Code lines are commented 

//import static com.ericsson.oss.mediation.pm.handlers.util.ApplicationMessages.ALLOWED_OPERATOR;
//import static com.ericsson.oss.mediation.pm.handlers.util.PmicLogCommands.*;

//import com.ericsson.oss.mediation.pm.handlers.qualifiers.StrategyQualifier;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.pm.handlers.error.HandlerError;
import com.ericsson.oss.mediation.pm.handlers.helpers.PerformanceMonitoringFactoryImpl;
import com.ericsson.oss.mediation.pm.scanneroperations.exception.PerformanceMonitoringException;
//import com.ericsson.oss.itpf.sdk.recording.*;

/**
 * Base class for handling PM operations. Common calls and exception handling
 * is performed in this class and the concrete strategies are responsible for
 * executing the appropriate PM actions on a node.
 * 
 * 
 */
@ApplicationScoped
public abstract class BaseOperationStrategyImpl implements OperationStrategy {
	
    private final static Logger log = LoggerFactory.getLogger(BaseOperationStrategyImpl.class);

//    @Inject
//    protected SystemRecorder systemRecorder;

    @Inject
    protected PerformanceMonitoringFactoryImpl pmFactory;

    protected abstract void executeOperation(final Map<String, Object> eventHeaders) throws PerformanceMonitoringException;
    protected abstract void createExceptionEvent(final Map<String, Object> headers, final short errorCode);

    @Override
    public void execute(final Map<String, Object> eventHeaders) {
//        String eventName = this.getClass().getAnnotation(StrategyQualifier.class).type().toString();

        try {

            this.executeOperation(eventHeaders);

//            systemRecorder.recordEvent(PMIC_SUCCESSFUL_OUTPUT_EVENT.getDescription(), EventLevel.COARSE, PMIC_SUCCESSFUL_OUTPUT_EVENT
//                    .getDescription(), "PERFORMANCE MONITORING " + eventName + " EVENT", String.format("PMIC, SUCCESSFUL " + eventName
//                    + " EVENT, operator: %s  handler: %s", ALLOWED_OPERATOR, this.getClass().getSimpleName()));

        } catch (final PerformanceMonitoringException e) {
            log.error("PerformanceMonitoringException thrown for performance monitoring: {} ", e.getMessage());

//            systemRecorder.recordError(PMIC_PERFORMANCE_EXCEPTION_EVENT.getDescription(), ErrorSeverity.ERROR,
//                    PMIC_PERFORMANCE_EVENT_ACTIVATION_ERROR.getDescription(), "PERFORMANCE MONITORING " + eventName + " EVENT",
//                    String.format("PMIC, PERFORMANCE EXCEPTION EVENT, operator: %s  handler: %s", ALLOWED_OPERATOR, this.getClass().getSimpleName()));
              this.createExceptionEvent(eventHeaders, e.getErrorCode());

        } catch (final Exception e) {
            log.error("Exception thrown for performance monitoring", e);

//            systemRecorder.recordError(PMIC_EXCEPTION_EVENT.getDescription(), ErrorSeverity.ERROR, PMIC_EXCEPTION_EVENT.getDescription(),
//                    "PERFORMANCE MONITORING " + eventName + " EVENT",
//                    String.format("PMIC, EXCEPTION EVENT, operator: %s  handler: %s", ALLOWED_OPERATOR, this.getClass().getSimpleName()));
            this.createExceptionEvent(eventHeaders, HandlerError.GENERIC_EXCEPTION.getCode());
        }
    }
}
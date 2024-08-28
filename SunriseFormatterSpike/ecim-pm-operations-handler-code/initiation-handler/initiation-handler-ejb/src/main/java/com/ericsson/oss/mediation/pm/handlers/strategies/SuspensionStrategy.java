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

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.pm.handlers.qualifiers.StrategyQualifier;
import com.ericsson.oss.mediation.pm.handlers.qualifiers.StrategyType;
import com.ericsson.oss.mediation.pm.handlers.util.HandlerUtil;
import com.ericsson.oss.mediation.pm.handlers.helpers.PerformanceMonitoringDeactivator;
import com.ericsson.oss.mediation.pm.scanneroperations.exception.PerformanceMonitoringException;
import static com.ericsson.oss.mediation.pm.handlers.constants.HandlerConstants.*;

/**
 * Class responsible for providing the call to suspend a
 * previously created scanner or predefined scanner on a node.
 * 
 * 
 */
@StrategyQualifier(type = StrategyType.SUSPENSION)
public class SuspensionStrategy extends BaseOperationStrategyImpl {
    private final static Logger log = LoggerFactory.getLogger(SuspensionStrategy.class);

    @Override
    public void executeOperation(final Map<String, Object> eventHeaders) throws PerformanceMonitoringException {
        final PerformanceMonitoringDeactivator deactivator = this.pmFactory.getPerformanceMonitoringDeactivator();

        deactivator.suspendPerformanceMonitoring(eventHeaders);

        log.info("Executed suspension strategy...");
    }
    
    @Override
    protected void createExceptionEvent(final Map<String, Object> headers, final short errorCode) {
    	final String scannerId = (String)headers.get(SCANNER_ID_HEADER_PARAM_KEY);
    	HandlerUtil.prepareEventForNextHandler(headers, scannerId, errorCode, STATUS_ERROR);
    }

}

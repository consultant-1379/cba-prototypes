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
package com.ericsson.oss.mediation.pm.handlers.helpers;

import static com.ericsson.oss.mediation.pm.handlers.constants.HandlerConstants.*;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.pm.handlers.util.HandlerUtil;
import com.ericsson.oss.mediation.pm.scanneroperations.ScannerOperationResult;
import com.ericsson.oss.mediation.pm.scanneroperations.exception.PerformanceMonitoringException;

/**
 * Performance monitoring activator to perform create and resume scanner
 * operations on the node via scanner operations executor.
 */
public class PerformanceMonitoringActivator extends AbstractPerformanceMonitoringHandler {

    private static final Logger log = LoggerFactory.getLogger(PerformanceMonitoringActivator.class);

    /**
     * Method to process the incoming activation event. This event includes
     * creating the scanner. <br/>
     * 
     * @return mediationComponentEvent MediationComponentEvent
     * @throws PerformanceMonitoringException
     */
    public void activatePerformanceMonitoring(final Map<String, Object> headers) throws PerformanceMonitoringException {

        final String nodeAddress = (String) headers.get(NODE_ADDRESS);

        log.debug("Handling scanner activation event for node {}", nodeAddress);
        
        final ScannerOperationResult result = createPerformanceMonitoring(headers);
        
        final String scannerId = result.getScannerID();
        final String scannerStatus = result.getScannerStatus();
        final short errorCode = result.getErrorCode();
        
        if (scannerStatus == STATUS_ERROR) {
            log.info("Performance monitoring cannot be created on the node {} with ID {} ", nodeAddress, scannerId);
        } else {
        	log.debug("Performance monitoring created on the node {} with ID {} and status {} ", nodeAddress, scannerId, scannerStatus);
        }        
        
        HandlerUtil.prepareEventForNextHandler(headers, scannerId, errorCode, scannerStatus);

    }

    /**
     * Method to process the incoming resumption event. This method only resumes
     * an existing scanner <br/>
     * 
     * @return mediationComponentEvent MediationComponentEvent
     * @throws PerformanceMonitoringException
     */
    public void resumePerformanceMonitoring(final Map<String, Object> headers) throws PerformanceMonitoringException {
        final String nodeAddress = (String) headers.get(NODE_ADDRESS);
        final String scannerName = (String) headers.get(SCANNER_NAME);

        log.debug("Resuming performance monitoring on the node {} and scannerName {} ", nodeAddress, scannerName);
        
        ScannerOperationResult result = null;
        
        if (scannerOperationsExecutor != null) {
        	result = scannerOperationsExecutor.resumePerformanceMonitoring(headers);
            
            log.debug("Result after performing resume performance monitoring: scannerId={}, scannerStatus={}, errorCode={} ", 
            		result.getScannerID(), result.getScannerStatus(), result.getErrorCode());
        }
        else {
        	result = new ScannerOperationResult(scannerName, "INACTIVE", (short) 0);
            log.debug("Unable to resume performance monitoring due to scannerOperation NULL");       	
        }

        log.debug("Result is {}", result);
        
        HandlerUtil.prepareEventForNextHandler(headers, result.getScannerID(), result.getErrorCode(), result.getScannerStatus());        
    }

    /**
     * Method to create the scanner on the specified node.
     * 
     * @return scannerOperationResult scanner operation result returned from
     *         scanner operations executor
     */
    protected ScannerOperationResult createPerformanceMonitoring(final Map<String, Object> eventHeader) throws PerformanceMonitoringException {
        log.debug("Creating performance monitoring with attributes {} ", eventHeader);        

        ScannerOperationResult result = null;
        
        if (scannerOperationsExecutor != null) {
        	result = scannerOperationsExecutor.createPerformanceMonitoring(eventHeader); 
        	
            log.debug("Result after performing create performance monitoring: scannerId={}, scannerStatus={}, errorCode={} ", 
            		result.getScannerID(), result.getScannerStatus(), result.getErrorCode());
        } else {
        	final String scannerName = (String)eventHeader.get(SCANNER_NAME);
        	result = new ScannerOperationResult(scannerName, "INACTIVE", (short) 0);
        	log.debug("Unable to create performance monitoring due to scannerOperation NULL");       	
        }

        log.debug("Result is {}", result);
        
        return result;
    }
}

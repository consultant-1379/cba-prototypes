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
 * Performance monitoring deactivator to perform suspend and deactivation
 * scanner operations on the node via scanner operations executor.
 */
public class PerformanceMonitoringDeactivator extends AbstractPerformanceMonitoringHandler {

    private static final Logger log = LoggerFactory.getLogger(PerformanceMonitoringDeactivator.class);

    /**
     * Method to process the incoming deactivation event.
     * 
     * @return mediationComponentEvent MediationComponentEvent
     * @throws PerformanceMonitoringException
     */    
    public void deactivatePerformanceMonitoring(final Map<String, Object> headers)
            throws PerformanceMonitoringException {
    	
        final String nodeAddress = (String)headers.get(NODE_ADDRESS);

        log.debug("Handling scanner deactivation event for node {}", nodeAddress);
        
        //EPICROB: it is necessary to verify if SGSN-MME needs subscription to be suspended before delete it.
        //Check also if Capabilities jobStartStopSupport has to be considered

        final ScannerOperationResult result = deletePerformanceMonitoring(headers);
        
        final String scannerId = result.getScannerID();
        final String scannerStatus = result.getScannerStatus();
        final short errorCode = result.getErrorCode();
        
        log.debug("successfully performed the deletePerformanceMonitoring for scannerId {} ", scannerId);      
        
        if (scannerStatus == STATUS_ERROR) {
            log.info("Performance monitoring cannot be deleted on the node {} with ID {} ", nodeAddress, scannerId);
        } else {
        	log.debug("Performance monitoring deleted on the node {} with ID {} ", nodeAddress, scannerId);
        } 

        HandlerUtil.prepareEventForNextHandler(headers, scannerId, errorCode, scannerStatus);
    }

    /**
     * Method to process the incoming scanner suspension event. It assumes the
     * scanner is already created
     * 
     * @return mediationComponentEvent MediationComponentEvent
     * @throws PerformanceMonitoringException
     */
    public void suspendPerformanceMonitoring(final Map<String, Object> headers)
            throws PerformanceMonitoringException {
        final String nodeAddress = (String) headers.get(NODE_ADDRESS);
        final String scannerId = (String) headers.get(SCANNER_ID_HEADER_PARAM_KEY);

        log.debug("Suspending performance monitoring on the node {} and scannerId {} ", nodeAddress, scannerId);
        
        ScannerOperationResult result = null;
        
        if (scannerOperationsExecutor != null) {
        	result = scannerOperationsExecutor.suspendPerformanceMonitoring(headers);
            
            log.debug("Result after performing suspend performance monitoring: scannerId={}, scannerStatus={}, errorCode={} ", 
            		result.getScannerID(), result.getScannerStatus(), result.getErrorCode());
        }
        else {
        	result = new ScannerOperationResult(scannerId, "INACTIVE", (short) 0);
            log.debug("Unable to suspend performance monitoring due to scannerOperation NULL"); 
        }

        log.debug("Result is {}", result);
        
        HandlerUtil.prepareEventForNextHandler(headers, result.getScannerID(), result.getErrorCode(), result.getScannerStatus()); 
    }


    /**
     * Method to delete the scanner on the specified node.
     * 
     * @return scannerOperationResult scanner operation result returned from
     *         scanner operations executor
     */
    public ScannerOperationResult deletePerformanceMonitoring(final Map<String, Object> headers) throws PerformanceMonitoringException {
        log.debug("Delete performance monitoring with attributes {} ", headers);        

        ScannerOperationResult result = null;
        
        if (scannerOperationsExecutor != null) {
        	result = scannerOperationsExecutor.deletePerformanceMonitoring(headers); 
        	
            log.debug("Result after performing delete performance monitoring: scannerId={}, scannerStatus={}, errorCode={} ", 
            		result.getScannerID(), result.getScannerStatus(), result.getErrorCode());
        } else {
        	final String scannerId = (String)headers.get(SCANNER_ID_HEADER_PARAM_KEY);
        	result = new ScannerOperationResult(scannerId, "INACTIVE", (short) 0);
        	log.debug("Unable to delete performance monitoring due to scannerOperation NULL");        	
        }

        log.debug("Result is {}", result);
        
        return result;
    }

}

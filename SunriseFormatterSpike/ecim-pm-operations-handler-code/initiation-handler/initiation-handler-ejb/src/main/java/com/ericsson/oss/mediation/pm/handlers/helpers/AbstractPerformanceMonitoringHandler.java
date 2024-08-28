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

import com.ericsson.oss.mediation.pm.scanneroperations.ScannerOperations;
import com.ericsson.oss.mediation.pm.scanneroperations.ScannerOperationsImpl;

public abstract class AbstractPerformanceMonitoringHandler {

	//EPICROB: TO DO understand if it is necessary to implement a proxy so as to 
	//perform scanner operations on the base of ECIM version
    protected ScannerOperations scannerOperationsExecutor;

    public AbstractPerformanceMonitoringHandler() {
        scannerOperationsExecutor = createScannerOperation();
    }


    private ScannerOperations createScannerOperation() {
        final ScannerOperations scannerOperationObj = new ScannerOperationsImpl();

        return scannerOperationObj;
    }

}

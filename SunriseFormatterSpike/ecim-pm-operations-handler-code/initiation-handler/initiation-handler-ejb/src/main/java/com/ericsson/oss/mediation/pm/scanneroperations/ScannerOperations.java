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
package com.ericsson.oss.mediation.pm.scanneroperations;

import java.util.List;
import java.util.Map;

import com.ericsson.oss.mediation.pm.scanneroperations.exception.PerformanceMonitoringException;
import com.ericsson.oss.services.pm.initiation.cpp.events.NodeScannerInfo;

public interface ScannerOperations {

    ScannerOperationResult createPerformanceMonitoring(final Map<String, Object> eventAttributes) throws PerformanceMonitoringException;

    ScannerOperationResult resumePerformanceMonitoring(final Map<String, Object> eventAttributes) throws PerformanceMonitoringException;

    ScannerOperationResult suspendPerformanceMonitoring(final Map<String, Object> eventAttributes) throws PerformanceMonitoringException;

    ScannerOperationResult deletePerformanceMonitoring(final Map<String, Object> eventAttributes) throws PerformanceMonitoringException;

    List<NodeScannerInfo> listPerformanceMonitoring(final String nodeFdn, final String nodeIpAddress) throws PerformanceMonitoringException;
}

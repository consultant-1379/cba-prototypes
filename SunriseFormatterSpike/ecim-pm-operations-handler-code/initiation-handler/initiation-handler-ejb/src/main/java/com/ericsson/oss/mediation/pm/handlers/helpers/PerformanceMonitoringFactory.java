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

import com.ericsson.oss.mediation.pm.scanneroperations.exception.PerformanceMonitoringException;

/**
 * Interface for factory class that will produce implementation classes (Activator/Deactivator) to handle
 * different PM operations 
 * 
 * 
 */
public interface PerformanceMonitoringFactory {

    /**
     * @return
     * @throws PerformanceMonitoringException
     */
    PerformanceMonitoringActivator getPerformanceMonitoringActivator() throws PerformanceMonitoringException;


    /**
     * @return
     * @throws PerformanceMonitoringException
     */
    PerformanceMonitoringDeactivator getPerformanceMonitoringDeactivator() throws PerformanceMonitoringException;
}

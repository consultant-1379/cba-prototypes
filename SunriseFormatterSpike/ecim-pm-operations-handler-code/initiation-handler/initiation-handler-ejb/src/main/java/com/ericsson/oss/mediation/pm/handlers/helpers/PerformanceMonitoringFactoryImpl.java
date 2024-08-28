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

import javax.enterprise.context.ApplicationScoped;

import com.ericsson.oss.mediation.pm.scanneroperations.exception.PerformanceMonitoringException;


/**
 * Factory for producing access to Activator/Deactivator classes to handle different PM operations
 * 
 * 
 */
@ApplicationScoped
public class PerformanceMonitoringFactoryImpl implements PerformanceMonitoringFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public PerformanceMonitoringActivator getPerformanceMonitoringActivator() throws PerformanceMonitoringException {
        return new PerformanceMonitoringActivator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PerformanceMonitoringDeactivator getPerformanceMonitoringDeactivator() throws PerformanceMonitoringException {
        return new PerformanceMonitoringDeactivator();
    }
}

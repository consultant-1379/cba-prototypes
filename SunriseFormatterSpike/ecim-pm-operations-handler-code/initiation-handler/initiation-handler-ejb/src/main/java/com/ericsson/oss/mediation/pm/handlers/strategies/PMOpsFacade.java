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

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.ericsson.oss.mediation.pm.handlers.qualifiers.StrategyQualifier;
import com.ericsson.oss.mediation.pm.handlers.qualifiers.StrategyType;

/**
 * This class provides a service responsible for acting as a facade for the
 * different calls to be done on a node.
 * 
 * 
 */
@ApplicationScoped
public class PMOpsFacade {

    @Inject
    @StrategyQualifier(type = StrategyType.ACTIVATION)
    private OperationStrategy activationStrategy;

    @Inject
    @StrategyQualifier(type = StrategyType.DEACTIVATION)
    private OperationStrategy deactivationStrategy;

    @Inject
    @StrategyQualifier(type = StrategyType.RESUMPTION)
    private OperationStrategy resumptionStrategy;

    @Inject
    @StrategyQualifier(type = StrategyType.SUSPENSION)
    private OperationStrategy suspensionStrategy;

    /**
     * Delegates execution of the activation of a scanner on a node.
     * 
     * @param eventHeaders
     *            The mediation event headers
     * @return The MediationComponentEvent used in the next handler
     */
    public void activatePerformanceMonitoring(final Map<String, Object> eventHeaders) {
        this.activationStrategy.execute(eventHeaders);
    }

    /**
     * Delegates execution of the deactivation of a scanner on a node.
     * 
     * @param eventHeaders
     *            The mediation event headers
     * @return The MediationComponentEvent used in the next handler
     */
    public void deactivatePerformanceMonitoring(final Map<String, Object> eventHeaders) {
        this.deactivationStrategy.execute(eventHeaders);
    }

    /**
     * Delegates execution of the suspension of a scanner on a node.
     * 
     * @param eventHeaders
     *            The mediation event headers
     * @return The MediationComponentEvent used in the next handler
     */
    public void suspendPerformanceMonitoring(final Map<String, Object> eventHeaders) {
        this.suspensionStrategy.execute(eventHeaders);
    }

    /**
     * Delegates execution of the resumption of a scanner on a node.
     * 
     * @param eventHeaders
     *            The mediation event headers
     * @return The MediationComponentEvent used in the next handler
     */
    public void resumePerformanceMonitoring(final Map<String, Object> eventHeaders) {
        this.resumptionStrategy.execute(eventHeaders);
    }
}

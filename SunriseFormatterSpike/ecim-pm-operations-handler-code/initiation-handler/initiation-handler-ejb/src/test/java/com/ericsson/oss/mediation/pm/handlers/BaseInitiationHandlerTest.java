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
package com.ericsson.oss.mediation.pm.handlers;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;

import com.ericsson.oss.itpf.common.config.Configuration;
import com.ericsson.oss.itpf.common.event.handler.EventHandlerContext;
import com.ericsson.oss.mediation.pm.handlers.helpers.PerformanceMonitoringFactoryImpl;
import com.ericsson.oss.mediation.pm.handlers.strategies.ActivationStrategy;
import com.ericsson.oss.mediation.pm.handlers.strategies.DeactivationStrategy;
import com.ericsson.oss.mediation.pm.handlers.strategies.PMOpsFacade;
import com.ericsson.oss.mediation.pm.handlers.strategies.ResumptionStrategy;
import com.ericsson.oss.mediation.pm.handlers.strategies.SuspensionStrategy;
import com.ericsson.oss.mediation.util.netconf.api.NetconfManager;
import com.ericsson.oss.mediation.util.netconf.api.NetconfResponse;

public abstract class BaseInitiationHandlerTest {
	
	protected InitiationHandler objectUnderTest;

    protected Map<String, Object> eventHeader;

    @Mock
    protected EventHandlerContext ctx;

    @Mock
    protected Configuration mockHandlerConfig;
    
    @Mock
    protected NetconfManager netconfManager;
    
    @Mock
    protected NetconfResponse netconfResponse;

    protected static PerformanceMonitoringFactoryImpl pmFactory;    
    protected static PMOpsFacade pmOpsFacade;
    protected static ActivationStrategy activationStrategy;
    protected static DeactivationStrategy deactivationStrategy;
    protected static ResumptionStrategy resumptionStrategy;
    protected static SuspensionStrategy suspensionStrategy;
    
    @BeforeClass
    public static void setUpClass() {
        pmOpsFacade = new PMOpsFacade();
        activationStrategy = new ActivationStrategy();
        deactivationStrategy = new DeactivationStrategy();
        resumptionStrategy = new ResumptionStrategy();
        suspensionStrategy = new SuspensionStrategy();   
        pmFactory = new PerformanceMonitoringFactoryImpl();

        Whitebox.setInternalState(pmOpsFacade, "activationStrategy", activationStrategy);
        Whitebox.setInternalState(pmOpsFacade, "deactivationStrategy", deactivationStrategy);
        Whitebox.setInternalState(pmOpsFacade, "resumptionStrategy", resumptionStrategy);
        Whitebox.setInternalState(pmOpsFacade, "suspensionStrategy", suspensionStrategy);
        
        Whitebox.setInternalState(activationStrategy, "pmFactory", pmFactory);
        Whitebox.setInternalState(deactivationStrategy, "pmFactory", pmFactory);
        Whitebox.setInternalState(resumptionStrategy, "pmFactory", pmFactory);
        Whitebox.setInternalState(suspensionStrategy, "pmFactory", pmFactory); 
    }
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        objectUnderTest = new InitiationHandler();
        eventHeader = new HashMap<String, Object>();
        
        Whitebox.setInternalState(this.objectUnderTest, "pmOpsFacade", pmOpsFacade);
        
        Mockito.when(ctx.getEventHandlerConfiguration()).thenReturn(mockHandlerConfig);
        Mockito.when(mockHandlerConfig.getAllProperties()).thenReturn(eventHeader);  
    }   
    
    @After
    public void tearDown() {
    }
}

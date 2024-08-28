/* ------------- PACKAGE -------------*/
package com.ericsson.oss.mediation.pm.handlers;

/* ------------- IMPORT -------------*/
import org.junit.Test;
import org.mockito.Mockito;

import com.ericsson.oss.itpf.common.event.ComponentEvent;
import com.ericsson.oss.mediation.flow.events.MediationComponentEvent;
import com.ericsson.oss.mediation.pm.handlers.exceptions.NetconfManagerNotFoundException;
import com.ericsson.oss.mediation.sdk.event.MediationTaskRequest;
import com.ericsson.oss.mediation.util.netconf.api.NetconManagerConstants;
import com.ericsson.oss.mediation.util.netconf.api.NetconfManager;
import com.ericsson.oss.services.pm.initiation.cpp.jobs.StatisticsPMActivationJob;

/* ------------- CODE -------------*/

public class InitiationHandlerTest extends BaseInitiationHandlerTest {

    /*
     * inputEvent = null
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEventErrorWhenInputEventIsNull() {
        objectUnderTest.init(ctx);
        objectUnderTest.onEvent(null);
    }

    /*
     * inputEvent.headers = null
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEventErrorWhenInputEventHeaderIsNull() {
        objectUnderTest.init(ctx);
        objectUnderTest.onEvent(new MediationComponentEvent(null, ""));
    }

    /*
     * inputEvent.headers = Map<String, Object>()
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEventErrorWhenInputEventHasEmptyHeader() {
        objectUnderTest.init(ctx);
        objectUnderTest.onEvent(new MediationComponentEvent(eventHeader, ""));
    }

    /*
     * inputEvent.headers = {{NETCONF_MANAGER_ATTR, null}}
     */
    @Test(expected = NetconfManagerNotFoundException.class)
    public void testEventErrorWhenInputEventHasNetconfManagerNull() {
        eventHeader.put(NetconManagerConstants.NETCONF_MANAGER_ATTR, null);

        final ComponentEvent inputEvent = new MediationComponentEvent(eventHeader, "");
        objectUnderTest.init(ctx);
        objectUnderTest.onEvent(inputEvent);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEventErrorWhenPmEventTypeNull() {
    	NetconfManager netconfManager = Mockito.mock(NetconfManager.class);        
        
        final MediationTaskRequest activationRequest = new StatisticsPMActivationJob(null, "NetworkElement=node", "jobId", "scannerName", 900, null, "1234",
                "parentMoFdn", "scannerModelNameSpace", "scannerModelType", "scannerModelVersion", null);

        eventHeader.put(NetconManagerConstants.NETCONF_MANAGER_ATTR, netconfManager);
        eventHeader.put("mediationTaskRequest", activationRequest);

        final ComponentEvent inputEvent = new MediationComponentEvent(eventHeader, "");
        objectUnderTest.init(ctx);
        objectUnderTest.onEvent(inputEvent);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEventErrorWhenPmEventTypeEmpty() {
    	NetconfManager netconfManager = Mockito.mock(NetconfManager.class);        
        
        final MediationTaskRequest activationRequest = new StatisticsPMActivationJob("", "NetworkElement=node", "jobId", "scannerName", 900, null, "1234",
                "parentMoFdn", "scannerModelNameSpace", "scannerModelType", "scannerModelVersion", null);

        eventHeader.put(NetconManagerConstants.NETCONF_MANAGER_ATTR, netconfManager);
        eventHeader.put("mediationTaskRequest", activationRequest);

        final ComponentEvent inputEvent = new MediationComponentEvent(eventHeader, "");
        objectUnderTest.init(ctx);
        objectUnderTest.onEvent(inputEvent);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEventErrorWhenPmEventTypeUnknown() {
    	NetconfManager netconfManager = Mockito.mock(NetconfManager.class);        
        
        final MediationTaskRequest activationRequest = new StatisticsPMActivationJob("unknown", "NetworkElement=node", "jobId", "scannerName", 900, null, "1234",
                "parentMoFdn", "scannerModelNameSpace", "scannerModelType", "scannerModelVersion", null);

        eventHeader.put(NetconManagerConstants.NETCONF_MANAGER_ATTR, netconfManager);
        eventHeader.put("mediationTaskRequest", activationRequest);

        final ComponentEvent inputEvent = new MediationComponentEvent(eventHeader, "");
        objectUnderTest.init(ctx);
        objectUnderTest.onEvent(inputEvent);
    }
}

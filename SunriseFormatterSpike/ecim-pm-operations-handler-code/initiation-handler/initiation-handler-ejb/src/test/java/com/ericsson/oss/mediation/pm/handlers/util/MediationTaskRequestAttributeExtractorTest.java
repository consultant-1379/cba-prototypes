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
package com.ericsson.oss.mediation.pm.handlers.util;

import static org.junit.Assert.assertEquals;

import java.util.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.ericsson.oss.mediation.sdk.event.MediationTaskRequest;
import com.ericsson.oss.services.pm.initiation.cpp.jobs.StatisticsPMActivationJob;
import com.ericsson.oss.services.pm.initiation.cpp.jobs.StatisticsPMDeactivationJob;
import com.ericsson.oss.services.pm.initiation.cpp.jobs.constants.PMEventConstants;

public class MediationTaskRequestAttributeExtractorTest {

    public static final String INITIATION_HANDLER_CLASS_NAME = "com.ericsson.oss.mediation.pm.handlers.InitiationHandler";
    
    private static String nodeAddress = "address";
    private static String jobId = "1234";
    private static final String protocolInfo = "PM";
    private String clientType;
    
    private String connectivityInfoPoId;
    private static String pmEventTypeActivation = PMEventConstants.EVENT_TYPE_ACTIVATION;
    private static String pmEventTypeDeactivation = PMEventConstants.EVENT_TYPE_DEACTIVATION;
    private static List<String> counterDetails = new ArrayList<String>();
    private static String parentMoFdn = "MeContext=1";
    private static int ropPeriod = 15;
    private static String scannerModelNameSpace = "DefaultNameSpace";
    private static String scannerModelType = "DefaultScannerType";
    private static String scannerModelVersion = "1.0.0";
    private static String scannerName = "DefaultScannerName";
    private static String subscriptionPoId = "2222";
    private static String scannerFdn = "DefaultScannerFdn";
    private static String scannerId = "DefaultScannerId";

    private static Map<String, Object> activationHeaders;
    private static Map<String, Object> deactivationHeaders;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUp() throws Exception {
        counterDetails.add("Counter1");
        counterDetails.add("Counter2");
        counterDetails.add("Counter3");
        
        //prepare headers for Activation Job
        final MediationTaskRequest activationJob = new StatisticsPMActivationJob(pmEventTypeActivation, nodeAddress, jobId, scannerName, ropPeriod, counterDetails,
                subscriptionPoId, parentMoFdn, scannerModelNameSpace, scannerModelType, scannerModelVersion, scannerId);
        
        activationHeaders = new HashMap<String, Object>();        
        final Map<String, Object> activationAttributesMap = new HashMap<String, Object>();
        activationAttributesMap.put("mediationTaskRequest", activationJob);
        activationHeaders.put(INITIATION_HANDLER_CLASS_NAME, activationAttributesMap);
        
        //prepare Header for Deactivation Job
        final MediationTaskRequest deactivationJob = new StatisticsPMDeactivationJob(pmEventTypeDeactivation, nodeAddress, jobId, scannerName, scannerId, scannerFdn);        
        
        deactivationHeaders = new HashMap<String, Object>();        
        final Map<String, Object> deactivationAttributesMap = new HashMap<String, Object>();        
        deactivationAttributesMap.put("mediationTaskRequest", deactivationJob);
        
        deactivationHeaders.put(INITIATION_HANDLER_CLASS_NAME, deactivationAttributesMap);
    }

    @Test
    public void testActivationJobAttributeExtraction() {
        MediationTaskRequestAttributeExtractor.addMediationTaskRequestAttributesToHeader(activationHeaders);
        for (final String key : activationHeaders.keySet()) {
            final Object value = activationHeaders.get(key);
            if (value instanceof Map) {
                @SuppressWarnings("unchecked")
                final Map<String, Object> handlerAttributesMap = (Map<String, Object>) value;
                assertEquals(nodeAddress, handlerAttributesMap.get("nodeAddress"));
                assertEquals(jobId, handlerAttributesMap.get("jobId"));
                assertEquals(scannerName, handlerAttributesMap.get("scannerName"));
                assertEquals(ropPeriod, handlerAttributesMap.get("ropPeriod"));
                assertEquals(counterDetails, handlerAttributesMap.get("counterDetails"));
                assertEquals(subscriptionPoId, handlerAttributesMap.get("subscriptionPoId"));
                assertEquals(parentMoFdn, handlerAttributesMap.get("parentMoFdn"));
                assertEquals(scannerModelNameSpace, handlerAttributesMap.get("scannerModelNameSpace"));
                assertEquals(scannerModelType, handlerAttributesMap.get("scannerModelType"));
                assertEquals(scannerModelVersion, handlerAttributesMap.get("scannerModelVersion"));
                assertEquals(connectivityInfoPoId, handlerAttributesMap.get("connectivityInfoPoId"));
                assertEquals(protocolInfo, handlerAttributesMap.get("protocolInfo"));
                assertEquals(clientType, handlerAttributesMap.get("clientType"));
                assertEquals(pmEventTypeActivation, handlerAttributesMap.get("pmEventType"));
            }
        }
    }

    @Test
    public void testPutMediationTaskRequestAttributes() {
        @SuppressWarnings("unchecked")
        final Map<String, Object> handlerHeadersForActivation = (Map<String, Object>) activationHeaders.get(INITIATION_HANDLER_CLASS_NAME);
        MediationTaskRequestAttributeExtractor.putMediationTaskRequestAttributesInHandlerSpecificHeaders(handlerHeadersForActivation);
        assertEquals(nodeAddress, handlerHeadersForActivation.get("nodeAddress"));
        assertEquals(jobId, handlerHeadersForActivation.get("jobId"));
        assertEquals(scannerName, handlerHeadersForActivation.get("scannerName"));
        assertEquals(ropPeriod, handlerHeadersForActivation.get("ropPeriod"));
        assertEquals(counterDetails, handlerHeadersForActivation.get("counterDetails"));
        assertEquals(subscriptionPoId, handlerHeadersForActivation.get("subscriptionPoId"));
        assertEquals(parentMoFdn, handlerHeadersForActivation.get("parentMoFdn"));
        assertEquals(scannerModelNameSpace, handlerHeadersForActivation.get("scannerModelNameSpace"));
        assertEquals(scannerModelType, handlerHeadersForActivation.get("scannerModelType"));
        assertEquals(scannerModelVersion, handlerHeadersForActivation.get("scannerModelVersion"));
        assertEquals(connectivityInfoPoId, handlerHeadersForActivation.get("connectivityInfoPoId"));
        assertEquals(protocolInfo, handlerHeadersForActivation.get("protocolInfo"));
        assertEquals(clientType, handlerHeadersForActivation.get("clientType"));
        assertEquals(pmEventTypeActivation, handlerHeadersForActivation.get("pmEventType"));

        @SuppressWarnings("unchecked")
        final Map<String, Object> handlerHeadersForDeactivation = (Map<String, Object>) deactivationHeaders.get(INITIATION_HANDLER_CLASS_NAME);
        MediationTaskRequestAttributeExtractor.putMediationTaskRequestAttributesInHandlerSpecificHeaders(handlerHeadersForDeactivation);
        assertEquals(nodeAddress, handlerHeadersForDeactivation.get("nodeAddress"));
        assertEquals(jobId, handlerHeadersForDeactivation.get("jobId"));
        assertEquals(scannerId, handlerHeadersForDeactivation.get("scannerId"));
        assertEquals(scannerFdn, handlerHeadersForDeactivation.get("scannerFdn"));
        assertEquals(pmEventTypeDeactivation, handlerHeadersForDeactivation.get("pmEventType"));
        assertEquals(protocolInfo, handlerHeadersForDeactivation.get("protocolInfo"));
        assertEquals(clientType, handlerHeadersForDeactivation.get("clientType"));
    }

    @Test
    public void testDeactivationJobAttributeExtraction() {
        MediationTaskRequestAttributeExtractor.addMediationTaskRequestAttributesToHeader(deactivationHeaders);
        for (final String key : deactivationHeaders.keySet()) {
            final Object value = deactivationHeaders.get(key);
            if (value instanceof Map) {
                @SuppressWarnings("unchecked")
                final Map<String, Object> handlerAttributesMap = (Map<String, Object>) value;
                assertEquals(nodeAddress, handlerAttributesMap.get("nodeAddress"));
                assertEquals(jobId, handlerAttributesMap.get("jobId"));
                assertEquals(scannerId, handlerAttributesMap.get("scannerId"));
                assertEquals(scannerFdn, handlerAttributesMap.get("scannerFdn"));
                assertEquals(pmEventTypeDeactivation, handlerAttributesMap.get("pmEventType"));
                assertEquals(protocolInfo, handlerAttributesMap.get("protocolInfo"));
                assertEquals(clientType, handlerAttributesMap.get("clientType"));
            }
        }
    }

}

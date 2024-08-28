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

import static com.ericsson.oss.mediation.pm.handlers.constants.HandlerConstants.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

import java.io.Writer;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.xml.sax.SAXException;

import com.ericsson.oss.itpf.common.event.ComponentEvent;
import com.ericsson.oss.mediation.flow.events.MediationComponentEvent;
import com.ericsson.oss.mediation.pm.handlers.error.HandlerError;
import com.ericsson.oss.mediation.pm.handlers.helpers.PerformanceMonitoringDeactivator;
import com.ericsson.oss.mediation.pm.handlers.helpers.PerformanceMonitoringFactoryImpl;
import com.ericsson.oss.mediation.pm.netconf.serialization.FormatterImpl;
import com.ericsson.oss.mediation.pm.scanneroperations.ScannerOperationsImpl;
import com.ericsson.oss.mediation.pm.scanneroperations.exception.PerformanceMonitoringException;
import com.ericsson.oss.mediation.sdk.event.MediationTaskRequest;
import com.ericsson.oss.mediation.util.netconf.api.Datastore;
import com.ericsson.oss.mediation.util.netconf.api.NetconManagerConstants;
import com.ericsson.oss.mediation.util.netconf.api.NetconfConnectionStatus;
import com.ericsson.oss.mediation.util.netconf.api.NetconfResponse;
import com.ericsson.oss.mediation.util.netconf.api.exception.NetconfManagerException;
import com.ericsson.oss.services.pm.initiation.cpp.jobs.StatisticsPMDeactivationJob;
import com.ericsson.oss.services.pm.initiation.cpp.jobs.constants.PMEventConstants;

public class InitiationHandlerDeactivationTest extends BaseInitiationHandlerTest implements InitiationHandlerTestInterface {

	/* (non-Javadoc)
	 * @see com.ericsson.oss.mediation.pm.handlers.InitiationHandlerTestInterface#prepareEvent()
	 */
	@Override
	public void prepareEvent() {
    	final MediationTaskRequest deactivationRequest = new StatisticsPMDeactivationJob(PMEventConstants.EVENT_TYPE_DEACTIVATION, "NetworkElement=node", "jobId", "scannerName", "scannerName", "scannerFdn");

        eventHeader.put("mediationTaskRequest", deactivationRequest);
        eventHeader.put(NetconManagerConstants.NETCONF_MANAGER_ATTR, netconfManager);
	}
	
    private void testDeactivationWithNetconfOperationFailed() {
    	prepareEvent();
        
        objectUnderTest.init(ctx);

        final ComponentEvent mediationEvent = new MediationComponentEvent(eventHeader, "");
        final ComponentEvent output = objectUnderTest.onEvent(mediationEvent);
        
        final Map<String, Object> outputHeaders = output.getHeaders();

        assertEquals(eventHeader.get(SCANNER_ID_HEADER_PARAM_KEY), outputHeaders.get(NODE_SCANNER_ID));
        assertEquals(STATUS_ERROR, outputHeaders.get(SCANNER_STATUS));
        assertEquals(HandlerError.NETCONF_OPERATION_FAILED.getCode(), outputHeaders.get(ERROR_CODE));
    } 
	
    @Override
	@Test
    public void testonEvent() {
    	Mockito.when(this.netconfManager.getStatus()).thenReturn(NetconfConnectionStatus.CONNECTED);		
    	NetconfResponse response = new NetconfResponse();
    	try {
			Mockito.when(this.netconfManager.lock(any(Datastore.class))).thenReturn(response);
			Mockito.when(this.netconfManager.editConfig(any(Datastore.class), any(String.class))).thenReturn(response);
			Mockito.when(this.netconfManager.validate(any(Datastore.class))).thenReturn(response);
			Mockito.when(this.netconfManager.commit()).thenReturn(response);
			Mockito.when(this.netconfManager.unlock(any(Datastore.class))).thenReturn(response);
		} catch (NetconfManagerException e) {
			e.printStackTrace();
		}    	

    	prepareEvent();

        objectUnderTest.init(ctx);

        final ComponentEvent mediationEvent = new MediationComponentEvent(eventHeader, "");
        final ComponentEvent output = objectUnderTest.onEvent(mediationEvent);
        
        final Map<String, Object> outputHeaders = output.getHeaders();
        
        assertEquals(eventHeader.get(SCANNER_ID_HEADER_PARAM_KEY), outputHeaders.get(NODE_SCANNER_ID));
        assertEquals(STATUS_DELETED, outputHeaders.get(SCANNER_STATUS));
        assertEquals(netconfManager, outputHeaders.get(NetconManagerConstants.NETCONF_MANAGER_ATTR));
    }
    
    @Override
	@Test
    public void testonEventWhenNetconfNotConnected() {
    	Mockito.when(this.netconfManager.getStatus()).thenReturn(NetconfConnectionStatus.NOT_CONNECTED);	
    	
    	prepareEvent();
        
        objectUnderTest.init(ctx);

        final ComponentEvent mediationEvent = new MediationComponentEvent(eventHeader, "");
        final ComponentEvent output = objectUnderTest.onEvent(mediationEvent);
        
        final Map<String, Object> outputHeaders = output.getHeaders();

        assertEquals(eventHeader.get(SCANNER_ID_HEADER_PARAM_KEY), outputHeaders.get(NODE_SCANNER_ID));
        assertEquals(STATUS_ERROR, outputHeaders.get(SCANNER_STATUS));
        assertEquals(HandlerError.NETCONFMANAGER_NOT_CONNECTED.getCode(), outputHeaders.get(ERROR_CODE));
    }
    
    @Override
	@SuppressWarnings("unchecked")
	@Test
    public void testonEventWhenNetconfException() {
    	Mockito.when(this.netconfManager.getStatus()).thenReturn(NetconfConnectionStatus.CONNECTED);		

    	try {
			Mockito.when(this.netconfManager.lock(any(Datastore.class))).thenThrow(NetconfManagerException.class);
			Mockito.when(this.netconfManager.editConfig(any(Datastore.class), any(String.class))).thenThrow(NetconfManagerException.class);
			Mockito.when(this.netconfManager.validate(any(Datastore.class))).thenThrow(NetconfManagerException.class);
			Mockito.when(this.netconfManager.commit()).thenThrow(NetconfManagerException.class);
			Mockito.when(this.netconfManager.unlock(any(Datastore.class))).thenThrow(NetconfManagerException.class);
		} catch (NetconfManagerException e) {
			e.printStackTrace();
		}    	
        
    	prepareEvent();
    	
        objectUnderTest.init(ctx);

        final ComponentEvent mediationEvent = new MediationComponentEvent(eventHeader, "");
        final ComponentEvent output = objectUnderTest.onEvent(mediationEvent);
        
        final Map<String, Object> outputHeaders = output.getHeaders();

        assertEquals(eventHeader.get(SCANNER_ID_HEADER_PARAM_KEY), outputHeaders.get(NODE_SCANNER_ID));
        assertEquals(STATUS_ERROR, outputHeaders.get(SCANNER_STATUS));
        assertEquals(HandlerError.NETCONFMANAGER_EXCEPTION.getCode(), outputHeaders.get(ERROR_CODE));
    }
    
    @Override
	@Test
    public void testonEventWhenLockFail() {
    	Mockito.when(this.netconfManager.getStatus()).thenReturn(NetconfConnectionStatus.CONNECTED);	
		Mockito.when(this.netconfResponse.isError()).thenReturn(Boolean.TRUE);
		Mockito.when(this.netconfResponse.getErrorMessage()).thenReturn("lock operation failed");
    	try {
			Mockito.when(this.netconfManager.lock(any(Datastore.class))).thenReturn(this.netconfResponse);
		} catch (NetconfManagerException e) {
			e.printStackTrace();
		}    	
    	
    	testDeactivationWithNetconfOperationFailed();
    }
    
    @Override
	@Test
    public void testonEventWhenEditConfigFail() {
    	Mockito.when(this.netconfManager.getStatus()).thenReturn(NetconfConnectionStatus.CONNECTED);	
		Mockito.when(this.netconfResponse.isError()).thenReturn(Boolean.TRUE);
		Mockito.when(this.netconfResponse.getErrorMessage()).thenReturn("editConfig operation failed");
		NetconfResponse responseOk = new NetconfResponse();
    	try {
    		Mockito.when(this.netconfManager.lock(any(Datastore.class))).thenReturn(responseOk);
			Mockito.when(this.netconfManager.editConfig(any(Datastore.class), any(String.class))).thenReturn(this.netconfResponse);
			Mockito.when(this.netconfManager.unlock(any(Datastore.class))).thenReturn(responseOk);
		} catch (NetconfManagerException e) {
			e.printStackTrace();
		}     	
    	
    	testDeactivationWithNetconfOperationFailed();
    }
  
    @Override
	@Test
    public void testonEventWhenValidateFail() {
    	Mockito.when(this.netconfManager.getStatus()).thenReturn(NetconfConnectionStatus.CONNECTED);	
		Mockito.when(this.netconfResponse.isError()).thenReturn(Boolean.TRUE);
		Mockito.when(this.netconfResponse.getErrorMessage()).thenReturn("validate operation failed");
		NetconfResponse responseOk = new NetconfResponse();
    	try {
			Mockito.when(this.netconfManager.lock(any(Datastore.class))).thenReturn(responseOk);
			Mockito.when(this.netconfManager.editConfig(any(Datastore.class), any(String.class))).thenReturn(responseOk);
			Mockito.when(this.netconfManager.validate(any(Datastore.class))).thenReturn(this.netconfResponse);
			Mockito.when(this.netconfManager.unlock(any(Datastore.class))).thenReturn(responseOk);
		} catch (NetconfManagerException e) {
			e.printStackTrace();
		}    	
    	
    	testDeactivationWithNetconfOperationFailed();
    }
    
    @Override
	@Test
    public void testonEventWhenCommitFail() {
    	Mockito.when(this.netconfManager.getStatus()).thenReturn(NetconfConnectionStatus.CONNECTED);	
		Mockito.when(this.netconfResponse.isError()).thenReturn(Boolean.TRUE);
		Mockito.when(this.netconfResponse.getErrorMessage()).thenReturn("commit operation failed");
		NetconfResponse responseOk = new NetconfResponse();
    	try {
			Mockito.when(this.netconfManager.lock(any(Datastore.class))).thenReturn(responseOk);
			Mockito.when(this.netconfManager.editConfig(any(Datastore.class), any(String.class))).thenReturn(responseOk);
			Mockito.when(this.netconfManager.validate(any(Datastore.class))).thenReturn(responseOk);
			Mockito.when(this.netconfManager.commit()).thenReturn(this.netconfResponse);
			Mockito.when(this.netconfManager.unlock(any(Datastore.class))).thenReturn(responseOk);
		} catch (NetconfManagerException e) {
			e.printStackTrace();
		}    
    	
    	testDeactivationWithNetconfOperationFailed();
    }
    
    @Override
	@Test
    public void testonEventWhenUnlockFail() {
    	Mockito.when(this.netconfManager.getStatus()).thenReturn(NetconfConnectionStatus.CONNECTED);	
		Mockito.when(this.netconfResponse.isError()).thenReturn(Boolean.TRUE);
		Mockito.when(this.netconfResponse.getErrorMessage()).thenReturn("unlock operation failed");
		NetconfResponse responseOk = new NetconfResponse();
    	try {
			Mockito.when(this.netconfManager.lock(any(Datastore.class))).thenReturn(responseOk);
			Mockito.when(this.netconfManager.editConfig(any(Datastore.class), any(String.class))).thenReturn(responseOk);
			Mockito.when(this.netconfManager.validate(any(Datastore.class))).thenReturn(responseOk);
			Mockito.when(this.netconfManager.commit()).thenReturn(responseOk);
			Mockito.when(this.netconfManager.unlock(any(Datastore.class))).thenReturn(this.netconfResponse);
		} catch (NetconfManagerException e) {
			e.printStackTrace();
		}    
    	
    	testDeactivationWithNetconfOperationFailed();
    }
    
    @Override
    @Test
    public void testonEventWhenJAXBException() {
    	Mockito.when(this.netconfManager.getStatus()).thenReturn(NetconfConnectionStatus.CONNECTED);
    	FormatterImpl formatter = Mockito.mock(FormatterImpl.class);
    	ScannerOperationsImpl scannerExecutor = new ScannerOperationsImpl();
    	PerformanceMonitoringDeactivator deactivator = new PerformanceMonitoringDeactivator();
    	PerformanceMonitoringFactoryImpl factory = Mockito.mock(PerformanceMonitoringFactoryImpl.class);    	

    	try {
    		Mockito.when(factory.getPerformanceMonitoringDeactivator()).thenReturn(deactivator);
		    Mockito.when(formatter.format(any(Object.class), any(Writer.class))).thenThrow(new JAXBException("JAXB Exception thrown"));
		} catch (PerformanceMonitoringException | JAXBException | SAXException e) {
			e.printStackTrace();
		}
    	
    	Whitebox.setInternalState(scannerExecutor, "formatter", formatter);
    	Whitebox.setInternalState(deactivator, "scannerOperationsExecutor", scannerExecutor);
    	Whitebox.setInternalState(deactivationStrategy, "pmFactory", factory);
    	
    	prepareEvent();
    	
        objectUnderTest.init(ctx);

        final ComponentEvent mediationEvent = new MediationComponentEvent(eventHeader, "");
        final ComponentEvent output = objectUnderTest.onEvent(mediationEvent);
        
        final Map<String, Object> outputHeaders = output.getHeaders();
        
        assertEquals(eventHeader.get(SCANNER_ID_HEADER_PARAM_KEY), outputHeaders.get(NODE_SCANNER_ID));
        assertEquals(STATUS_ERROR, outputHeaders.get(SCANNER_STATUS));
        assertEquals(HandlerError.NETCONF_OPERATION_FORMAT_ERROR.getCode(), outputHeaders.get(ERROR_CODE));
        
        Whitebox.setInternalState(deactivationStrategy, "pmFactory", pmFactory);
    }
    
    @Override
    @Test
    public void testonEventWhenSAXException() {
    	Mockito.when(this.netconfManager.getStatus()).thenReturn(NetconfConnectionStatus.CONNECTED);
    	FormatterImpl formatter = Mockito.mock(FormatterImpl.class);
    	ScannerOperationsImpl scannerExecutor = new ScannerOperationsImpl();
    	PerformanceMonitoringDeactivator deactivator = new PerformanceMonitoringDeactivator();
    	PerformanceMonitoringFactoryImpl factory = Mockito.mock(PerformanceMonitoringFactoryImpl.class);

    	try {
    		Mockito.when(factory.getPerformanceMonitoringDeactivator()).thenReturn(deactivator);
		    Mockito.when(formatter.format(any(Object.class), any(Writer.class))).thenThrow(new SAXException("SAX Exception thrown"));
		} catch (PerformanceMonitoringException | JAXBException | SAXException e) {
			e.printStackTrace();
		}
    	
    	Whitebox.setInternalState(scannerExecutor, "formatter", formatter);
    	Whitebox.setInternalState(deactivator, "scannerOperationsExecutor", scannerExecutor);
    	Whitebox.setInternalState(deactivationStrategy, "pmFactory", factory);
    	
    	prepareEvent();
    	
        objectUnderTest.init(ctx);

        final ComponentEvent mediationEvent = new MediationComponentEvent(eventHeader, "");
        final ComponentEvent output = objectUnderTest.onEvent(mediationEvent);
        
        final Map<String, Object> outputHeaders = output.getHeaders();
        
        assertEquals(eventHeader.get(SCANNER_ID_HEADER_PARAM_KEY), outputHeaders.get(NODE_SCANNER_ID));
        assertEquals(STATUS_ERROR, outputHeaders.get(SCANNER_STATUS));
        assertEquals(HandlerError.NETCONF_OPERATION_FORMAT_ERROR.getCode(), outputHeaders.get(ERROR_CODE));
        
        Whitebox.setInternalState(deactivationStrategy, "pmFactory", pmFactory);
    }
    
    @Override
    @Test
    public void testonEventWhenScannerOperationExecutorNull() {
    	PerformanceMonitoringDeactivator deactivator = new PerformanceMonitoringDeactivator();
    	PerformanceMonitoringFactoryImpl factory = Mockito.mock(PerformanceMonitoringFactoryImpl.class);
    	
    	try {
    		Mockito.when(factory.getPerformanceMonitoringDeactivator()).thenReturn(deactivator);
		} catch (PerformanceMonitoringException e) {
			e.printStackTrace();
		}
    	
    	Whitebox.setInternalState(deactivator, "scannerOperationsExecutor", null);
    	Whitebox.setInternalState(deactivationStrategy, "pmFactory", factory);
    	
    	prepareEvent();
    	
        objectUnderTest.init(ctx);

        final ComponentEvent mediationEvent = new MediationComponentEvent(eventHeader, "");
        final ComponentEvent output = objectUnderTest.onEvent(mediationEvent);
        
        final Map<String, Object> outputHeaders = output.getHeaders();
        
        assertEquals(eventHeader.get(SCANNER_ID_HEADER_PARAM_KEY), outputHeaders.get(NODE_SCANNER_ID));
        assertEquals("INACTIVE", outputHeaders.get(SCANNER_STATUS));
        assertEquals((short)0, outputHeaders.get(ERROR_CODE));

        Whitebox.setInternalState(deactivationStrategy, "pmFactory", pmFactory);
    }

}

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

import static com.ericsson.oss.mediation.pm.handlers.constants.HandlerConstants.*;

import java.io.*;
import java.util.*;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.eclipse.persistence.dynamic.DynamicEntity;
import org.eclipse.persistence.jaxb.dynamic.DynamicJAXBContext;
import org.eclipse.persistence.jaxb.dynamic.DynamicJAXBContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.ericsson.oss.mediation.pm.handlers.error.HandlerError;
import com.ericsson.oss.mediation.pm.netconf.classes.ObjectFactory;
import com.ericsson.oss.mediation.pm.netconf.classes.pm.*;
import com.ericsson.oss.mediation.pm.netconf.classes.top.ManagedElement;
import com.ericsson.oss.mediation.pm.netconf.classes.top.SystemFunctions;
import com.ericsson.oss.mediation.pm.netconf.contentHandler.NamespaceDeclarationHandler;
import com.ericsson.oss.mediation.pm.netconf.lineFormat.*;
import com.ericsson.oss.mediation.pm.netconf.lineFormat.constants.NetconfOperation;
import com.ericsson.oss.mediation.pm.netconf.serialization.Formatter;
import com.ericsson.oss.mediation.pm.netconf.serialization.FormatterImpl;
import com.ericsson.oss.mediation.pm.observationdetails.ObservationClass;
import com.ericsson.oss.mediation.pm.observationdetails.ObservationUtil;
import com.ericsson.oss.mediation.pm.scanneroperations.exception.PerformanceMonitoringException;
import com.ericsson.oss.mediation.util.netconf.api.*;
import com.ericsson.oss.mediation.util.netconf.api.exception.NetconfManagerException;
import com.ericsson.oss.services.pm.initiation.cpp.events.NodeScannerInfo;

public class ScannerOperationsImpl implements ScannerOperations {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(ScannerOperationsImpl.class);
	
	private final Formatter formatter;

	public ScannerOperationsImpl() {
		super();
		this.formatter = new FormatterImpl();
	}

	/* (non-Javadoc)
	 * @see com.ericsson.oss.mediation.pm.scanneroperations.ScannerOperations#createPerformanceMonitoring(java.util.Map)
	 */
	@Override
	public ScannerOperationResult createPerformanceMonitoring(final Map<String, Object> eventAttributes) 
			throws PerformanceMonitoringException {
		
		final NetconfManager netconfManager = (NetconfManager)eventAttributes.get(NetconManagerConstants.NETCONF_MANAGER_ATTR);
		final String scannerName = (String)eventAttributes.get(SCANNER_NAME);
		
		if (netconfManager.getStatus() != NetconfConnectionStatus.CONNECTED) {
			LOGGER.error("Netconf manager non connected");
			return new ScannerOperationResult(scannerName, STATUS_ERROR, HandlerError.NETCONFMANAGER_NOT_CONNECTED.getCode());
		}			

		final String managedElementId = getManagedElementId((String)eventAttributes.get(NODE_ADDRESS));	
		
		LOGGER.info("*************** START TEST WITH LINE FORMAT AND STAX **************");
		
		LineFormat lf = new LineFormat();
		LineFormatElement lfe = new LineFormatElement().namespace("urn:com:ericsson:ecim:SgsnMmeTop").name("ManagedElement");
		LineFormatElement lfePmJob = new LineFormatElement().name("PmJob").operation(NetconfOperation.CREATE)
				.setAttribute("pmJobId", new StringFormat(scannerName))
				.setAttribute("requestedJobState", new EnumFormat("ACTIVE"))
				.setAttribute("granularityPeriod", new EnumFormat(convertToEcimTimePeriod(eventAttributes.get(SCANNER_ROP_PERIOD).toString())))
				.setAttribute("reportingPeriod", new EnumFormat(convertToEcimTimePeriod(eventAttributes.get(SCANNER_ROP_PERIOD).toString())))
				.setAttribute("jobType", new EnumFormat("MEASUREMENTJOB"));
				
		lfe.setAttribute("managedElementId", new StringFormat(managedElementId))
			.add(
				new LineFormatElement().name("SystemFunctions")
					.setAttribute("systemFunctionsId", new StringFormat("1"))
					.add(
							new LineFormatElement().namespace("urn:com:ericsson:ecim:SgsnMmePM").name("Pm")
								.setAttribute("pmId", new StringFormat("1"))
								.add(lfePmJob)
					)
			);
		
	    final ObservationClass[] counterInfo2 = ObservationUtil.getObservationClasses(eventAttributes);
		for (ObservationClass observationClass2 : counterInfo2) {
			for(String counterName : observationClass2.moAttributesList) {
				lfePmJob.add(new LineFormatElement().name("MeasurementReader").operation(NetconfOperation.CREATE)
				   .setAttribute("measurementReaderId", new StringFormat(observationClass2.moClassName + "_" + counterName + "_Id")));
			}
		}    
		lf.add(lfe);
		
		final StringWriter writer2 = lf.format(new StringWriter());
		LOGGER.info("Output: " +  writer2.toString());
		LOGGER.info("*************** END TEST WITH LINE FORMAT AND STAX **************");
		
		LOGGER.info("*************** START TEST WITH DYNAMIC JAXB **************");
		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/schema/SgsnMmeTop.xsd");

			DynamicJAXBContext dContext = DynamicJAXBContextFactory.createContextFromXSD(inputStream, new SchemaEntityResolver(), null, null);
			 
			DynamicEntity me = dContext.newDynamicEntity("ManagedElement");
			me.set("managedElementId", managedElementId);
			
			DynamicEntity sys = dContext.newDynamicEntity("SystemFunctionsType");
			sys.set("systemFunctionsId", "1");			
			
			DynamicEntity pm = dContext.newDynamicEntity("Pm");
			pm.set("pmId", "1");			
			
			DynamicEntity pmJob = dContext.newDynamicEntity("PmJobType");
			pmJob.set("operation", "create");
			pmJob.set("pmJobId", scannerName);
			pmJob.set("requestedJobState", "ACTIVE");
			pmJob.set("granularityPeriod", convertToEcimTimePeriod(eventAttributes.get(SCANNER_ROP_PERIOD).toString()));
			pmJob.set("reportingPeriod", convertToEcimTimePeriod(eventAttributes.get(SCANNER_ROP_PERIOD).toString()));
			pmJob.set("jobType", "MEASUREMENTJOB");
			
			ArrayList<Object> readers = new ArrayList<Object>();
		
		    final ObservationClass[] counterInfo3 = ObservationUtil.getObservationClasses(eventAttributes);
			for (ObservationClass observationClass3 : counterInfo3) {
				for(String counterName : observationClass3.moAttributesList) {
					DynamicEntity reader = dContext.newDynamicEntity("MeasurementReaderType");
					reader.set("operation", "create");
					reader.set("measurementReaderId", observationClass3.moClassName + "_" + counterName + "_Id");
					
					final String measTypeFdn = ManagedElement.class.getSimpleName() + "=" + managedElementId + "," 
							  + SystemFunctions.class.getSimpleName() + "=1," 
							  + Pm.class.getSimpleName() + "=1,"
							  + "PmGroup=" + observationClass3.moClassName + ","
							  + "MeasurementType=" + counterName;
					
					DynamicEntity spec = dContext.newDynamicEntity("MeasurementSpecificationType");
					spec.set("struct", "MeasurementSpecification");
					spec.set("measurementTypeRef", measTypeFdn);
					reader.set("measurementSpecification", spec);
					
					readers.add(reader);
				}
			}  
			
			if(!readers.isEmpty()) {
				pmJob.set("measurementReader", readers);				
			}
			
			pm.set("pmJob", pmJob);
			sys.set("pm", pm);
			me.set("systemFunctions", sys);
			
			StringWriter writer = new StringWriter();
			final Marshaller m = dContext.createMarshaller();			
			m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			m.marshal(me, writer);			
			LOGGER.info("Output: " +  writer.toString());
			
			StringWriter  writerContentHandler = new StringWriter();
			final NamespaceDeclarationHandler contentHandler = new NamespaceDeclarationHandler(writerContentHandler);
			contentHandler.init();
			m.marshal(me, contentHandler);
			LOGGER.info("Output2: " +  writerContentHandler.toString());

			inputStream.close();

		} catch (IOException | JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		LOGGER.info("*************** END TEST WITH DYNAMIC JAXB **************");
		
		
		LOGGER.info("*************** START TEST WITH STATIC JAXB **************");
		final ObjectFactory factory = new ObjectFactory();
		
		final PmJob pmJob = factory.createPmJob();		
		pmJob.setPmJobId(scannerName);
		pmJob.setRequestedJobState("ACTIVE");
		pmJob.setGranularityPeriod(convertToEcimTimePeriod(eventAttributes.get(SCANNER_ROP_PERIOD).toString()));
		pmJob.setReportingPeriod(convertToEcimTimePeriod(eventAttributes.get(SCANNER_ROP_PERIOD).toString()));
	    pmJob.setJobType("MEASUREMENTJOB");
	    pmJob.setOperation("create");
		
	    final ObservationClass[] counterInfo = ObservationUtil.getObservationClasses(eventAttributes);
		for (ObservationClass observationClass : counterInfo) {
			for(String counterName : observationClass.moAttributesList) {
				
				final MeasurementReader reader = factory.createMeasurementReader();
				
				reader.setMeasurementReaderId(observationClass.moClassName + "_" + counterName + "_Id");
				reader.setOperation("create");
				
				final MeasurementSpecification specification = factory.createMeasurementSpecification();
				
				final String measTypeFdn = ManagedElement.class.getSimpleName() + "=" + managedElementId + "," 
				  + SystemFunctions.class.getSimpleName() + "=1," 
				  + Pm.class.getSimpleName() + "=1,"
				  + "PmGroup=" + observationClass.moClassName + ","
				  + "MeasurementType=" + counterName;
				
				specification.setMeasurementTypeRef(measTypeFdn);
				reader.setMeasurementSpecification(specification);
				pmJob.getMeasurementReader().add(reader);
			}
		}		
		
		final Pm pm = factory.createPm();
		pm.setPmId("1");
		pm.setPmJob(pmJob);
		
		final SystemFunctions sysFunction = factory.createSystemFunctions();
		sysFunction.setSystemFunctionsId("1");
		sysFunction.setPm(pm);
		
		final ManagedElement me = factory.createManagedElement();		
		me.setManagedElementId(managedElementId);
		me.setSystemFunctions(sysFunction);
		
		try {
			final StringWriter writer = formatter.format(me, new StringWriter());			
			LOGGER.info("createPerformanceMonitoring xmlString: " +  writer.toString());	
			LOGGER.info("*************** END TEST WITH STATIC JAXB **************");
			
			//node scannerID of the result is put equal to scannerName
			LOGGER.info("Executing performance monitoring create operation");
			return sendOperation(netconfManager, writer.toString(), scannerName, STATUS_ACTIVE);			
		} catch (JAXBException | SAXException e) {
			throw new PerformanceMonitoringException(String.format("ERROR: unable to format Netconf body command: %s", e.getMessage()), 
					HandlerError.NETCONF_OPERATION_FORMAT_ERROR);
		}
	}

	/* (non-Javadoc)
	 * @see com.ericsson.oss.mediation.pm.scanneroperations.ScannerOperations#resumePerformanceMonitoring(Map<String, Object>)
	 */
	@Override
	public ScannerOperationResult resumePerformanceMonitoring(final Map<String, Object> eventAttributes)
			throws PerformanceMonitoringException {
		final NetconfManager netconfManager = (NetconfManager)eventAttributes.get(NetconManagerConstants.NETCONF_MANAGER_ATTR);
		final String scannerName = (String)eventAttributes.get(SCANNER_NAME);
		
		if (netconfManager.getStatus() != NetconfConnectionStatus.CONNECTED) {
			LOGGER.error("Netconf manager non connected");
			return new ScannerOperationResult(scannerName, STATUS_ERROR, HandlerError.NETCONFMANAGER_NOT_CONNECTED.getCode());
		}			
		
		final String managedElementId = getManagedElementId((String)eventAttributes.get(NODE_ADDRESS));
		
		final ObjectFactory factory = new ObjectFactory();
		
		final PmJob pmJob = factory.createPmJob();		
		pmJob.setPmJobId(scannerName);
		pmJob.setRequestedJobState("ACTIVE");
	    pmJob.setOperation("merge");
		
	    final Pm pm = factory.createPm();
		pm.setPmId("1");
		pm.setPmJob(pmJob);
		
		final SystemFunctions sysFunction = factory.createSystemFunctions();
		sysFunction.setSystemFunctionsId("1");
		sysFunction.setPm(pm);
		
		final ManagedElement me = factory.createManagedElement();		
		me.setManagedElementId(managedElementId);
		me.setSystemFunctions(sysFunction);
		
		try {
			final StringWriter writer = formatter.format(me, new StringWriter());
			LOGGER.info("resumePerformanceMonitoring xmlString: " + writer.toString());
			
			//node scannerID of the result is put equal to scannerName
			LOGGER.info("Executing performance monitoring resume operation");
			return sendOperation(netconfManager, writer.toString(), scannerName, STATUS_ACTIVE);			
		} catch (JAXBException | SAXException e) {
			throw new PerformanceMonitoringException(String.format("ERROR: unable to format Netconf body command: %s", e.getMessage()), 
					HandlerError.NETCONF_OPERATION_FORMAT_ERROR);
		}
	}

	/* (non-Javadoc)
	 * @see com.ericsson.oss.mediation.pm.scanneroperations.ScannerOperations#suspendPerformanceMonitoring(Map<String, Object>)
	 */
	@Override
	public ScannerOperationResult suspendPerformanceMonitoring(final Map<String, Object> eventAttributes)
			throws PerformanceMonitoringException {
		final NetconfManager netconfManager = (NetconfManager)eventAttributes.get(NetconManagerConstants.NETCONF_MANAGER_ATTR);
		final String scannerId = (String)eventAttributes.get(SCANNER_ID_HEADER_PARAM_KEY);

		if (netconfManager.getStatus() != NetconfConnectionStatus.CONNECTED) {
			LOGGER.error("Netconf manager non connected");
			return new ScannerOperationResult(scannerId, STATUS_ERROR, HandlerError.NETCONFMANAGER_NOT_CONNECTED.getCode());
		}			
		
		final String managedElementId = getManagedElementId((String)eventAttributes.get(NODE_ADDRESS));
		
		final ObjectFactory factory = new ObjectFactory();
		
		final PmJob pmJob = factory.createPmJob();		
		pmJob.setPmJobId(scannerId);
		pmJob.setRequestedJobState("STOPPED");
	    pmJob.setOperation("merge");
		
	    final Pm pm = factory.createPm();
		pm.setPmId("1");
		pm.setPmJob(pmJob);
		
		final SystemFunctions sysFunction = factory.createSystemFunctions();
		sysFunction.setSystemFunctionsId("1");
		sysFunction.setPm(pm);
		
		final ManagedElement me = factory.createManagedElement();		
		me.setManagedElementId(managedElementId);
		me.setSystemFunctions(sysFunction);
		
		try {
			final StringWriter writer = formatter.format(me, new StringWriter());
			LOGGER.info("suspendPerformanceMonitoring xmlString: " + writer.toString());
			
			LOGGER.info("Executing performance monitoring suspend operation");
			return sendOperation(netconfManager, writer.toString(), scannerId, STATUS_INACTIVE);			
		} catch (JAXBException | SAXException e) {
			throw new PerformanceMonitoringException(String.format("ERROR: unable to format Netconf body command: %s", e.getMessage()), 
					HandlerError.NETCONF_OPERATION_FORMAT_ERROR);
		}
	}

	/* (non-Javadoc)
	 * @see com.ericsson.oss.mediation.pm.scanneroperations.ScannerOperations#deletePerformanceMonitoring(int[])
	 */
	@Override
	public ScannerOperationResult deletePerformanceMonitoring(final Map<String, Object> eventAttributes)
			throws PerformanceMonitoringException {
		ScannerOperationResult result =null;

		final NetconfManager netconfManager = (NetconfManager)eventAttributes.get(NetconManagerConstants.NETCONF_MANAGER_ATTR);
		//scannerId should be equal to scannerName
		final String scannerId = (String)eventAttributes.get(SCANNER_ID_HEADER_PARAM_KEY);
		
		if (netconfManager.getStatus() != NetconfConnectionStatus.CONNECTED) {
			LOGGER.error("Netconf manager non connected");
			result = new ScannerOperationResult(scannerId, STATUS_ERROR, HandlerError.NETCONFMANAGER_NOT_CONNECTED.getCode());
			return result;
		}			

		final String managedElementId = getManagedElementId((String)eventAttributes.get(NODE_ADDRESS));
		
		final ObjectFactory factory = new ObjectFactory();
		
		final PmJob pmJob = factory.createPmJob();		
		pmJob.setPmJobId(scannerId);
	    pmJob.setOperation("delete");
		
	    final Pm pm = factory.createPm();
		pm.setPmId("1");
		pm.setPmJob(pmJob);
		
		final SystemFunctions sysFunction = factory.createSystemFunctions();
		sysFunction.setSystemFunctionsId("1");
		sysFunction.setPm(pm);
		
		final ManagedElement me = factory.createManagedElement();		
		me.setManagedElementId(managedElementId);
		me.setSystemFunctions(sysFunction);
		
		try {
			final StringWriter writer = formatter.format(me, new StringWriter());
			LOGGER.info("deletePerformanceMonitoring xmlString: " + writer.toString());
			
			LOGGER.info("Executing performance monitoring delete operation");
			return sendOperation(netconfManager, writer.toString(), scannerId, STATUS_DELETED);
		} catch (JAXBException | SAXException e) {
			throw new PerformanceMonitoringException(String.format("ERROR: unable to format Netconf body command: %s", e.getMessage()), 
					HandlerError.NETCONF_OPERATION_FORMAT_ERROR);
		}		
	}

	/* (non-Javadoc)
	 * @see com.ericsson.oss.mediation.pm.scanneroperations.ScannerOperations#listPerformanceMonitoring(java.lang.String, java.lang.String)
	 */
	@Override
	public List<NodeScannerInfo> listPerformanceMonitoring(final String nodeFdn, final String nodeIpAddress) 
			throws PerformanceMonitoringException {
		// TODO Auto-generated method stub
		return null;
	}
	
	private String getManagedElementId(final String nodeFdn) {
		final int startIndex = NODE_PREFIX.length();
		final int endIndex = nodeFdn.length(); 
		return nodeFdn.substring(startIndex, endIndex);		
	}
	
	protected String convertToEcimTimePeriod(final String periodInSeconds) {
		
		switch(periodInSeconds.trim()) {
		  case "60":
		  return "ONE_MIN";
		  
		  case "300":
		  return "FIVE_MIN";
		  
		  case "900":
		  return "FIFTEEN_MIN";
		  
		  case "1800":
		  return "THIRTY_MIN";
		  
		  case "3600":
		  return "ONE_HOUR";
		  
		  case "720":
		  return "TWELVE_HOUR";
		  
		  case "86400":
		  return "ONE_DAY";		  
		  
		  case "10":
		  return "TEN_SECONDS";
		  
		  case "30":
		  return "THIRTY_SECONDS";

		}
		
		return null;
	}	
	
	private ScannerOperationResult sendOperation(final NetconfManager netconfManager, final String body, 
			final String scannerId, final String scannerStatus) {
		try {
			NetconfResponse response = netconfManager.lock(Datastore.CANDIDATE);
			if (response.isError()) {
				LOGGER.error("Netconf lock operation failed with response: " + response.getErrorMessage());
				return new ScannerOperationResult(scannerId, STATUS_ERROR, HandlerError.NETCONF_OPERATION_FAILED.getCode());
			}

			response = netconfManager.editConfig(Datastore.CANDIDATE, body);
			if (response.isError()) {
				LOGGER.error("Netconf editConfig operation failed with response: " + response.getErrorMessage());
				final NetconfResponse resp = netconfManager.unlock(Datastore.CANDIDATE);
				if (resp.isError()) {
					LOGGER.error("Netconf editConfig-unlock operation failed with response: " + resp.getErrorMessage());
				}
				return new ScannerOperationResult(scannerId, STATUS_ERROR, HandlerError.NETCONF_OPERATION_FAILED.getCode());
			}

			response = netconfManager.validate(Datastore.CANDIDATE);
			if (response.isError()) {
				LOGGER.error("Netconf validate operation failed with response: " + response.getErrorMessage());
				final NetconfResponse resp = netconfManager.unlock(Datastore.CANDIDATE);
				if (resp.isError()) {
					LOGGER.error("Netconf validate-unlock operation failed with response: " + resp.getErrorMessage());
				}
				return new ScannerOperationResult(scannerId, STATUS_ERROR, HandlerError.NETCONF_OPERATION_FAILED.getCode());
			}

			response = netconfManager.commit();
			if (response.isError()) {
				LOGGER.error("Netconf commit operation failed with response: " + response.getErrorMessage());
				final NetconfResponse resp = netconfManager.unlock(Datastore.CANDIDATE);
				if (resp.isError()) {
					LOGGER.error("Netconf commit-unlock operation failed with response: " + resp.getErrorMessage());
				}
				return new ScannerOperationResult(scannerId, STATUS_ERROR, HandlerError.NETCONF_OPERATION_FAILED.getCode());
			}

			response = netconfManager.unlock(Datastore.CANDIDATE);
			if (response.isError()) {
				LOGGER.error("Netconf unlock operation failed with response: " + response.getErrorMessage());
				return new ScannerOperationResult(scannerId, STATUS_ERROR, HandlerError.NETCONF_OPERATION_FAILED.getCode());
			}

			LOGGER.info("Performance monitoring operation completed successfully with ID {} ", scannerId);
			return new ScannerOperationResult(scannerId, scannerStatus);
			
		} catch (NetconfManagerException e) {
			LOGGER.error(e.getMessage(), e);
			return new ScannerOperationResult(scannerId, STATUS_ERROR, HandlerError.NETCONFMANAGER_EXCEPTION.getCode());
		}
	}
	
}

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
package com.ericsson.oss.mediation.pm.netconf;

import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.ericsson.oss.mediation.pm.netconf.classes.ObjectFactory;
import com.ericsson.oss.mediation.pm.netconf.classes.pm.MeasurementReader;
import com.ericsson.oss.mediation.pm.netconf.classes.pm.MeasurementSpecification;
import com.ericsson.oss.mediation.pm.netconf.classes.pm.Pm;
import com.ericsson.oss.mediation.pm.netconf.classes.pm.PmJob;
import com.ericsson.oss.mediation.pm.netconf.classes.top.ManagedElement;
import com.ericsson.oss.mediation.pm.netconf.classes.top.SystemFunctions;
import com.ericsson.oss.mediation.pm.netconf.serialization.Formatter;
import com.ericsson.oss.mediation.pm.netconf.serialization.FormatterImpl;

public class FormatterTest {
	private static ObjectFactory factory;
	private static Unmarshaller unmarshaller;
	
	private void assertJaxbEquals(Object obj1, Object obj2) {
		if(obj1 == null || obj2 == null) {
			throw new AssertionError();
		}
		
		if(!obj1.getClass().equals(obj2.getClass())) {
			throw new AssertionError();
		}
		
		final Field[] objDeclaredFields = obj1.getClass().getDeclaredFields();
		
		if(objDeclaredFields == null) {
			throw new AssertionError();
		}
		
        for (Field objField : objDeclaredFields ) {
        	if(objField.getAnnotation(XmlElement.class) == null &&
        	   objField.getAnnotation(XmlAttribute.class) == null) {
        		continue;
        	}
        		
        	objField.setAccessible(true);
        	try {
        		Object obj1FieldValue = objField.get(obj1);
        		Object obj2FieldValue = objField.get(obj2);
        		if(obj1FieldValue == null && obj2FieldValue != null) {
        			throw new AssertionError();
        		}
        		if(obj1FieldValue != null && obj2FieldValue == null) {
        			throw new AssertionError();
        		}
        		if(obj1FieldValue != null) {
        			if(obj2FieldValue instanceof Collection) {
        				assertEqualsCollection((Collection<?>)obj1FieldValue, (Collection<?>)obj2FieldValue);
        			}
        			else if(obj2FieldValue instanceof Map) {
        				assertEqualsMap((Map<?,?>)obj1FieldValue, (Map<?,?>)obj2FieldValue);
        			}
        			else if(objField.getType().getAnnotation(XmlRootElement.class) != null) {
        				assertJaxbEquals(obj1FieldValue, obj2FieldValue);
        			}
        			else if(!obj1FieldValue.equals(obj2FieldValue)) {
        				throw new AssertionError();
        			}
        		}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
        }
	}
	
	private void assertEqualsCollection(Collection<?> collection1, Collection<?> collection2) {
		if( collection1.size() != collection2.size()) {
			throw new AssertionError();
		}
	    for(Object item1 : collection1) {
	    	boolean found = false;
	    	for(Object item2 : collection2) {
	    		try {
	    			assertJaxbEquals(item1, item2);
	    			found = true;
	    			break;
	    		}
	    		catch (AssertionError e) {
	    		}
	        }
	    	if (!found) {
	    		throw new AssertionError();
	    	}
	    }		
	}
	
	private void assertEqualsMap(Map<?,?> map1, Map<?,?> map2) {
		if( map1.size() != map2.size()) {
			throw new AssertionError();
		}
	    for(Object item1Key : map1.keySet()) {
	    	Object item2Value = map2.get(item1Key);
	    	if ( item2Value == null ) {
	    		throw new AssertionError();
	    	}
	    	Object item1Value = map1.get(item1Key);
	    	assertJaxbEquals(item1Value, item2Value);
	    }
	}
	
    @BeforeClass
    public static void setUpClass() {
    	factory = new ObjectFactory(); 
    	
    	final StringBuilder contextPath = new StringBuilder();
    	contextPath.append("com.ericsson.oss.mediation.pm.netconf.classes");
    	
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(contextPath.toString());
			unmarshaller = jaxbContext.createUnmarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
    }
	
	@Test
	public void testFormatPm() {	
		Pm pm = factory.createPm();
		pm.setPmId("1");
		
		SystemFunctions sysFunction = factory.createSystemFunctions();
		sysFunction.setSystemFunctionsId("1");
		sysFunction.setPm(pm);
		
		ManagedElement me = factory.createManagedElement();		
		me.setManagedElementId("nodeId");
		me.setSystemFunctions(sysFunction);
		
		Object unmarshalObj = null;
		
		try {
			Formatter formatter = new FormatterImpl();
			StringWriter writer = formatter.format(me, new StringWriter());			

			unmarshalObj = unmarshaller.unmarshal(new StringReader(writer.toString()));
		} catch (JAXBException | SAXException e) {
			e.printStackTrace();
		}   
		
		assertJaxbEquals(me, unmarshalObj);
	}
	
	@Test
	public void testFormatPmJob() {	
		PmJob pmJob = factory.createPmJob();		
		pmJob.setPmJobId("scannerName");
		pmJob.setRequestedJobState("ACTIVE");
		pmJob.setGranularityPeriod("FIFTEEN_MIN");
		pmJob.setReportingPeriod("FIFTEEN_MIN");
	    pmJob.setJobType("MEASUREMENTJOB");
	    pmJob.setJobPriority("MEDIUM");
	    pmJob.setCompressionType("compressionTypeValue");
	    pmJob.setJobGroup("jobGroupvalue");

	    for(int i = 0; i < 2; i++) {
	    	MeasurementReader reader = factory.createMeasurementReader();
	    	reader.setMeasurementReaderId("readerId" + i);

	    	MeasurementSpecification specification = factory.createMeasurementSpecification();
	    	specification.setMeasurementTypeRef("measurementTypeFdn" + i);
	    	reader.setMeasurementSpecification(specification);
	    	
	    	pmJob.getMeasurementReader().add(reader);
	    }

		Pm pm = factory.createPm();
		pm.setPmId("1");
		pm.setPmJob(pmJob);
		
		SystemFunctions sysFunction = factory.createSystemFunctions();
		sysFunction.setSystemFunctionsId("1");
		sysFunction.setPm(pm);
		
		ManagedElement me = factory.createManagedElement();		
		me.setManagedElementId("nodeId");
		me.setSystemFunctions(sysFunction);
		
		Object unmarshalObj = null;
		
		try {
			Formatter formatter = new FormatterImpl();
			StringWriter writer = formatter.format(me, new StringWriter());			

			unmarshalObj = unmarshaller.unmarshal(new StringReader(writer.toString()));
		} catch (JAXBException | SAXException e) {
			e.printStackTrace();
		}   

		assertJaxbEquals(me, unmarshalObj);
	}
	
    @After
    public void tearDown() {
    }

}

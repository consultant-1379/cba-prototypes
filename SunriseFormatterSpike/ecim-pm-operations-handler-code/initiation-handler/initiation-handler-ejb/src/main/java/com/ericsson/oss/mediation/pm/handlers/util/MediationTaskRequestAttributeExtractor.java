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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ericsson.oss.itpf.modeling.annotation.eventtype.EventAttribute;
import com.ericsson.oss.mediation.sdk.event.MediationTaskRequest;
import com.ericsson.oss.services.pm.initiation.cpp.jobs.StatisticsPMActivationJob;
import com.ericsson.oss.services.pm.initiation.cpp.jobs.StatisticsPMDeactivationJob;

public class MediationTaskRequestAttributeExtractor {
    private MediationTaskRequestAttributeExtractor() {
    }

    /**
     * @param header map potentially containing a Map with an instance of StatisticsPMActivationJob or StatisticsPMDeactivationJob
     * @return the header map passed as argument added with all attributes of StatisticsPMActivationJob or StatisticsPMDeactivationJob
     */
    public static Map<String, Object> addMediationTaskRequestAttributesToHeader(final Map<String, Object> header) {
        final Map<String, Object> mediationTaskRequestAttributes = getAttributesFromMediationTaskRequestPassedInHeaders(header);
        for (final String key : header.keySet()) {
            if (header.get(key) instanceof Map && key.startsWith("com.ericsson")) {
                @SuppressWarnings("unchecked")
                final Map<String, Object> handlerAttributesMap = (Map<String, Object>) header.get(key);
                handlerAttributesMap.putAll(mediationTaskRequestAttributes);
                header.put(key, handlerAttributesMap);
            }
        }
        return header;
    }

    /**
     * @param EModel object
     * @return a map containing all EModelAttributes of object passed as argument, including ones of its super classes
     */
    private static Map<String, Object> getAttributesFromModelObj(final Object obj) {
        final Map<String, Object> attributes = new HashMap<String, Object>();
        final List<Field> attrList = getEModelEventFields(obj.getClass());
        for (Field field : attrList) {
        	field.setAccessible(true);
        	try {
				attributes.put(field.getName(), field.get(obj));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}

        return attributes;
    }

    /**
     * @param a class 
     * @return a list of EModelEvent fields of the input class, including ones of its super classes
     */
    private static List<Field> getEModelEventFields(final Class<?> cls) {
        final List<Field> fields = new LinkedList<Field>();
        if(cls == null) {
        	return fields;
        }
        
        final Field[] objFields = cls.getDeclaredFields();
        for (Field field : objFields ) {
        	if(field.getAnnotation(EventAttribute.class) != null) {
               fields.add(field);
        	}
        }
        
        final Class<?> superCls = cls.getSuperclass();
        if (superCls == null || superCls.equals(Object.class) ) {
        	return fields;
        }
        else {
        	fields.addAll(getEModelEventFields(superCls));
            return fields;
        }
    }

    /**
     * @param header map potentially containing a Map with an instance of StatisticsPMActivationJob or StatisticsPMDeactivationJob 
     * @return a map containing all attributes of StatisticsPMActivationJob or StatisticsPMDeactivationJob
     */
    private static Map<String, Object> getAttributesFromMediationTaskRequestPassedInHeaders(final Map<String, Object> header) {
        final Map<String, Object> mediationTaskRequestAttributes = new HashMap<String, Object>();
        for (final String key : header.keySet()) {
            if (header.get(key) instanceof Map && key.startsWith("com.ericsson")) {
                @SuppressWarnings("unchecked")
                final Map<String, Object> handlerAttributesMap = (Map<String, Object>) header.get(key);
                if (handlerAttributesMap.containsKey("mediationTaskRequest")) {
                    final MediationTaskRequest job = (MediationTaskRequest) handlerAttributesMap.get("mediationTaskRequest");
                    if (job instanceof StatisticsPMActivationJob || 
                    	job instanceof StatisticsPMDeactivationJob) {
                        mediationTaskRequestAttributes.putAll(getAttributesFromModelObj(job));  
                    }
                }
            }
        }
        return mediationTaskRequestAttributes;
    }

    public static void putMediationTaskRequestAttributesInHandlerSpecificHeaders(final Map<String, Object> handlerHeaders) {
            if (handlerHeaders.get("mediationTaskRequest") instanceof StatisticsPMActivationJob ||
            	handlerHeaders.get("mediationTaskRequest") instanceof StatisticsPMDeactivationJob) {
                handlerHeaders.putAll(getAttributesFromModelObj(handlerHeaders.get("mediationTaskRequest")));
            }
    }	

}

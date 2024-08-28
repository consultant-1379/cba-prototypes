/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2015
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.models.moci.mediation.handlers.api;

import java.util.Collection;
import java.util.Map;

import com.ericsson.oss.itpf.datalayer.dps.modeling.modelservice.typed.persistence.primarytype.PrimaryTypeAttributeSpecification;
import com.ericsson.oss.itpf.modeling.modelservice.typed.core.DataTypeSpecification;
import com.ericsson.oss.itpf.modeling.modelservice.typed.core.EModelAttributeSpecification;
import com.ericsson.oss.mediation.network.api.util.NodeAttributeModelDTO;

/**
 * Define what methods a converter utility class should have.
 */
public interface ConverterUtil {

    /**
     * Convert attribute to ENM type.
     *
     * @param attributeSpecification
     *            ENM attribute specification
     * @param toConvert
     *            <code>Object</code> to be converted by attribute specification.
     * @return object converted to ENM type
     */
    Object convertAttributeToEnmType(DataTypeSpecification attributeSpecification, Object toConvert);

    /**
     * Convert attributes to node type.
     *
     * @param eModelAttributeSpecifications
     *            the e model attribute specifications
     * @param attributes
     *            to be converted
     * @return the map with converted attributes
     */
    Map<String, NodeAttributeModelDTO> convertAttributesToNodeType(
        Collection<EModelAttributeSpecification> eModelAttributeSpecifications, Map<String, Object> attributes);

    /**
     * Convert attributes to ENM type.
     *
     * @param attributes
     *            to be converted
     * @param eModelAttributeSpecifications
     *            the e model attribute specifications
     * @return the map with converted attributes
     */
    Map<String, Object> convertAttributesToEnmType(
        Map<String, Object> attributes, Map<String, PrimaryTypeAttributeSpecification> eModelAttributeSpecifications);
}

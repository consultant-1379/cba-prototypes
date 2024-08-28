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
package com.ericsson.oss.models.moci.mediation.handlers.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.ericsson.enm.mediation.handler.common.AttributeConverter;
import com.ericsson.enm.mediation.handler.common.impl.converter.outbound.OutboundConverter;
import com.ericsson.enm.mediation.handler.common.impl.factory.OutboundConverterFactory;
import com.ericsson.oss.itpf.datalayer.dps.modeling.modelservice.typed.persistence.primarytype.PrimaryTypeAttributeSpecification;
import com.ericsson.oss.itpf.modeling.modelservice.typed.core.DataTypeSpecification;
import com.ericsson.oss.itpf.modeling.modelservice.typed.core.EModelAttributeSpecification;
import com.ericsson.oss.mediation.network.api.util.NodeAttributeModelDTO;
import com.ericsson.oss.models.moci.mediation.handlers.api.ConverterUtil;

/**
 * Provides helper methods to interacting with the node, converting attributes between node definition and Ericsson data types provide by Model
 * Service.
 */
public class ConverterUtilImpl implements ConverterUtil {

    @Inject
    private OutboundConverterFactory outboundConverterFactory;
    @Inject
    private AttributeConverter attributeConverter;

    @Override
    public Map<String, NodeAttributeModelDTO> convertAttributesToNodeType(
        final Collection<EModelAttributeSpecification> eModelAttributeSpecifications, final Map<String, Object> attributes) {

        final Map<String, NodeAttributeModelDTO> nodeAttributes = new HashMap<>();
        for (final EModelAttributeSpecification attrSpec : eModelAttributeSpecifications) {
            final String attributeName = attrSpec.getName();
            if (attributes.containsKey(attributeName)) {
                final DataTypeSpecification dataTypeSpecification = attrSpec.getDataTypeSpecification();
                final Object attributeValue = attributes.get(attributeName);
                final OutboundConverter converter = outboundConverterFactory.getAttributeConverter(dataTypeSpecification.getDataType());
                final Map<String, NodeAttributeModelDTO> convertedAttributeValue = converter.convert(attributeName, attributeValue,
                        dataTypeSpecification);
                nodeAttributes.put(attributeName, convertedAttributeValue.get(attributeName));
            }
        }
        return nodeAttributes;
    }

    @Override
    public Map<String, Object> convertAttributesToEnmType(
        final Map<String, Object> attrsToConvert, final Map<String, PrimaryTypeAttributeSpecification> attrSpec) {

        final Map<String, Object> convertedAttributes = new HashMap<>();
        for (final Map.Entry<String, Object> attrEntry : attrsToConvert.entrySet()) {
            final EModelAttributeSpecification eModelAttrSpec = attrSpec.get(attrEntry.getKey());
            final Object convertedValue = attributeConverter.convertToENM(eModelAttrSpec, attrEntry.getValue());
            convertedAttributes.put(attrEntry.getKey(), convertedValue);
        }
        return convertedAttributes;
    }

    @Override
    public Object convertAttributeToEnmType(final DataTypeSpecification attributeSpecification, final Object toConvert) {
        return attributeConverter.convertToENM(attributeSpecification, toConvert);
    }
}

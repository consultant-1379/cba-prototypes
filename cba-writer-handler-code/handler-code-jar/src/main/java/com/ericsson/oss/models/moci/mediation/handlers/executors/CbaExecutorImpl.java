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
package com.ericsson.oss.models.moci.mediation.handlers.executors;

import static com.ericsson.oss.models.moci.mediation.handlers.api.Constants.*;

import java.util.*;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.enm.mediation.handler.common.ModelServiceFacade;
import com.ericsson.oss.itpf.common.event.handler.exception.EventHandlerException;
import com.ericsson.oss.itpf.datalayer.dps.modeling.modelservice.typed.persistence.primarytype.PrimaryTypeAttributeSpecification;
import com.ericsson.oss.itpf.datalayer.dps.modeling.modelservice.typed.persistence.primarytype.PrimaryTypeSpecification;
import com.ericsson.oss.itpf.modeling.common.info.ModelInfo;
import com.ericsson.oss.itpf.modeling.modelservice.typed.core.EModelAttributeSpecification;
import com.ericsson.oss.itpf.modeling.schema.util.SchemaConstants;
import com.ericsson.oss.mediation.network.api.exception.MociConnectionProviderException;
import com.ericsson.oss.models.moci.mediation.handlers.api.ConverterUtil;
import com.ericsson.oss.models.moci.mediation.handlers.api.NodeConnectionProvider;
import com.ericsson.oss.models.moci.mediation.handlers.instrumentation.WriteNodeInstrumentationBean;
import com.ericsson.oss.models.moci.mediation.handlers.util.RequestData;

/**
 * MociExecutorImpl contains common functionality that will be used by each executor while trying to perform their operation against the node.
 */
public abstract class CbaExecutorImpl implements CbaExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CbaExecutorImpl.class);

    protected RequestData requestData;

    @Inject
    protected ConverterUtil converterUtil;

    @Inject
    protected ModelServiceFacade modelServiceFacade;

    @Inject
    protected NodeConnectionProvider nodeConnectionProvider;

    @Inject
    protected WriteNodeInstrumentationBean instrumentationBean;

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.oss.itpf.common.event.handler.EventHandler#init(com.ericsson .oss.itpf.common.event.handler.EventHandlerContext)
     */
    @Override
    public void init(final RequestData requestData) {
        this.requestData = requestData;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.oss.models.moci.mediation.handlers.WriteExecutor#destroy()
     */
    @Override
    public void destroy() {
        requestData = null;
    }

    /**
     * Creates ModelInfo
     * 
     * @return ModelInfo
     */
    protected ModelInfo createModelInfo() {
        final ModelInfo modelInfo = new ModelInfo(SchemaConstants.DPS_PRIMARYTYPE, requestData.getPoNamespace(), requestData.getPoType(),
                requestData.getPoVersion());
        LOGGER.debug("Creating modelInfo: [{}]", modelInfo);
        return modelInfo;
    }

    /**
     * Gets the attributes specification from the model.
     * 
     * @return the attributes specification
     */
    protected Collection<EModelAttributeSpecification> getAttributesSpecification() {
        final PrimaryTypeSpecification moSpecification = modelServiceFacade.getEModelSpecification(createModelInfo(), PrimaryTypeSpecification.class);
        final List<EModelAttributeSpecification> attributesFromSpecification = new ArrayList<EModelAttributeSpecification>();
        final Collection<PrimaryTypeAttributeSpecification> specs = moSpecification.getAttributeSpecifications();
        for (final EModelAttributeSpecification attribute : specs) {
            attributesFromSpecification.add(attribute);
        }
        return attributesFromSpecification;
    }

    /**
     * This method reports an error while executing any operation via Executors.
     * 
     * @param fdn
     *            of the MO being operated
     * @param e
     *            exception occurred
     */
    protected void reportError(final String fdn, final MociConnectionProviderException e) {
        final String operation = requestData.getClientOperation();
        final StringBuilder error = new StringBuilder(FAILURE_MESSAGE);
        error.append(operation);
        error.append(ON);
        error.append(MO_FDN_EQUALS);
        error.append(fdn);
        instrumentationBean.recordError(fdn, operation, e.getMessage());
        throw new EventHandlerException(error.toString(), e);
    }

}
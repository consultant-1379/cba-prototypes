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
import java.util.Map;

import com.ericsson.oss.itpf.common.event.handler.exception.EventHandlerException;
import com.ericsson.oss.mediation.engine.api.MediationEngineConstants;

/**
 * Data holder class that wraps the header data required by the handlers to prevent polluting the handler classes with lots of field variables.
 * Handlers simply use this class to get what they want through getters and this class uses the mediation-engine constants API to retrieve the data
 * from the headers map. This class will also prevent the leak of MediationEngineConstants into the handler classes. This class should be removed when
 * the DPS DTOHandler implementation work is done. <br>
 * <br>
 * http://jira-oss.lmera.ericsson.se/browse/TORF-9782
 * 
 * @author eeikkah
 * 
 */
public class RequestData implements MediationEngineConstants {

    static final int DEFAULT_TIMEOUT = 60;
    static final String MTR_COULD_NOT_PARSE_HEADER_DATA = "Unable to extract all the PersistenceTaskRequest specific data from header";
    static final String MTR_MODIFED_ATTRIBUTES_EMPTY_OR_NULL = "ModifiedAttributes map was empty or null";
    static final String MTR_ACTION_ATTRIBUTES_EMPTY_OR_NULL = "ActionAttributes map was empty or null";
    static final String MTR_INVALID_FDN = "Fdn was null, empty or did not contain at least one '=' character";
    static final String MTR_INVALID_PO_COORDINATES = "At least one of PO attributes[namespace, name(type),version] was null or empty";

    static final String ADDRESS = "ipAddress";
    static final String NODE_NAME = "nodeName";

    private Map<String, Object> actionAttributes;
    private Map<String, Object> modifiedAttributes;
    private Collection<String> readAttributes;
    private String clientOperation;
    private String poNamespace;
    private String poType;
    private String poVersion;
    private Integer timeout;
    private String fdn;
    private String actionName;
    private String ipAddress;
    private String nodeName;

    /**
     * Sets the headers for the RequestData object. Allows for CDI injection.
     * 
     * @param headers
     *            the headers to be set.
     */
    @SuppressWarnings("unchecked")
    public void setHeaders(final Map<String, Object> headers) {
        try {
            actionAttributes = (Map<String, Object>) headers.get(PTR_ACTION_ATTRIBUTES);
            actionName = (String) headers.get(PTR_ACTION_NAME);
            modifiedAttributes = (Map<String, Object>) headers.get(PTR_MODIFIED_ATTRIBUTES);
            readAttributes = (Collection<String>) headers.get(PTR_READ_ATTRIBUTES);
            clientOperation = (String) headers.get(PTR_CLIENT_OPERATION);
            poNamespace = (String) headers.get(PTR_PO_NAMESPACE);
            poType = (String) headers.get(PTR_PO_TYPE);
            poVersion = (String) headers.get(PTR_PO_VERSION);
            verifyPoCoordinates();
            timeout = Integer.getInteger((String) headers.get(PTR_TIMEOUT), DEFAULT_TIMEOUT);
            fdn = (String) headers.get(PTR_FDN);
            ipAddress = (String) headers.get(ADDRESS);
            nodeName = (String) headers.get(NODE_NAME);
            verifyFdn();
        } catch (final ClassCastException cce) {
            throw new EventHandlerException(MTR_COULD_NOT_PARSE_HEADER_DATA, cce.getCause());
        }
    }

    /**
     * @return actionName
     */
    public String getActionName() {
        return actionName;
    }

    /**
     * @return actionAttributes
     */
    public Map<String, Object> getActionAttributes() {
        return actionAttributes;
    }

    /**
     * @return modifiedAttributes
     */
    public Map<String, Object> getModifiedAttributes() {
        verifyModifiedAttributes();
        return modifiedAttributes;
    }

    /**
     * @return readAttributes
     */
    public Collection<String> getReadAttributes() {
        return readAttributes;
    }

    /**
     * @return po_namespace
     */
    public String getPoNamespace() {
        return poNamespace;
    }

    /**
     * @return po_type
     */
    public String getPoType() {
        return poType;
    }

    /**
     * @return po_version
     */
    public String getPoVersion() {
        return poVersion;
    }

    /**
     * @return timeout
     */
    public Integer getTimeout() {
        return timeout;
    }

    /**
     * @return clientOperation
     */
    public String getClientOperation() {
        return clientOperation;
    }

    /**
     * @return fdn
     */
    public String getFdn() {
        return fdn;
    }

    /**
     * @return ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * @return nodeName
     */
    public String getNodeName() {
        return nodeName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "RequestData [actionAttributes=" + actionAttributes + ", modifiedAttributes=" + modifiedAttributes + ", clientOperation="
                + clientOperation + ", po_namespace=" + poNamespace + ", po_type=" + poType + ", po_version=" + poVersion + ", timeout=" + timeout
                + ", fdn=" + fdn + ", actionName=" + actionName + "]";
    }

    /*
     * Verifies that modified attributes is not null or empty.
     */
    private void verifyModifiedAttributes() {
        if (modifiedAttributes == null || modifiedAttributes.isEmpty()) {
            throw new EventHandlerException(MTR_MODIFED_ATTRIBUTES_EMPTY_OR_NULL);
        }
    }

    /*
     * Verifies that fdn is not null, empty and contains at least one '=' character.
     */
    private void verifyFdn() {
        if (fdn == null || fdn.isEmpty() || !fdn.contains("=")) {
            throw new EventHandlerException(MTR_INVALID_FDN);
        }
    }

    /*
     * Verifies that neither the namespace, name(type) or version of the PO is null or empty.
     */
    private void verifyPoCoordinates() {
        if (poNamespace == null || poNamespace.isEmpty() || poVersion == null || poVersion.isEmpty() || poType == null || poType.isEmpty()) {
            throw new EventHandlerException(MTR_INVALID_PO_COORDINATES);
        }
    }
}
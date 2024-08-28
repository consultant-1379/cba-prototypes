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
package com.ericsson.oss.models.moci.mediation.handlers.test.mocks;

import static com.ericsson.oss.models.moci.mediation.handlers.test.util.TestConstants.IP_ADDRESS_VALUE_UN_REACHABLE;
import static com.ericsson.oss.models.moci.mediation.handlers.test.util.TestConstants.SET_STARTABLE;
import static com.ericsson.oss.models.moci.mediation.handlers.test.util.TestConstants.IP_ROUTING_TABLE_FDN;
import static com.ericsson.oss.models.moci.mediation.handlers.test.util.TestConstants.IP_ROUTING_TABLE_NOT_EXIST_FDN;


import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;

import com.ericsson.oss.mediation.network.api.MociCMConnectionProvider;
import com.ericsson.oss.mediation.network.api.exception.MociConnectionProviderException;
import com.ericsson.oss.mediation.network.api.util.ConnectionConfig;
import com.ericsson.oss.mediation.network.api.util.ManagedObjectMetaInfo;
import com.ericsson.oss.mediation.network.api.util.MibChangeInfo;
import com.ericsson.oss.mediation.network.api.util.NodeAttributeModelDTO;
import com.ericsson.oss.mediation.network.api.util.NodeMetaInfo;
import com.ericsson.oss.models.moci.mediation.handlers.test.util.ConnectionProviderVerifier;

/**
 * Mocking MociCMConnectionProvider for testing proposal.
 * 
 * Generated by alternative bean MockNetworkElementConnection
 * (/test/resources/META-INF/beans.xml).
 * 
 * @author ecasdia
 */
@ApplicationScoped
public class MockMociCMConnectionProvider implements MociCMConnectionProvider {

    private static final String OUT_OF_USE_CASE = "This is a mock class testing modify and action operation towards the network element";

    @EJB(lookup = ConnectionProviderVerifier.CONNECTION_COUNTER_VERIFIER_JNDI)
    private ConnectionProviderVerifier connectionProviderVerifier;

    @Override
    public void modify(final ConnectionConfig connectionData, final String fdn, final Map<String, NodeAttributeModelDTO> attributes)
            throws MociConnectionProviderException {
        mockUnreachableIP(connectionData);
        connectionProviderVerifier.setModifyInvoked();
    }

    @Override
    public Object action(final ConnectionConfig connectionData, final String fdn, final String actionName,
            final Map<String, NodeAttributeModelDTO> actionParameters)
            throws MociConnectionProviderException {
        mockActionNotAllowed(actionName);
        mockUnreachableIP(connectionData);
        connectionProviderVerifier.setActionInvoked();;
        return Integer.valueOf(1);
    }

    @Override
    public Collection<ManagedObjectMetaInfo> getFdns(final ConnectionConfig arg0) throws MociConnectionProviderException {
        throw new MociConnectionProviderException(OUT_OF_USE_CASE);
    }

    @Override
    public long getGenerationCounter(final ConnectionConfig arg0) throws MociConnectionProviderException {
        throw new MociConnectionProviderException(OUT_OF_USE_CASE);
    }

    @Override
    public Map<String, Map<String, Object>> getMOAttributes(final ConnectionConfig arg0, final Map<String, String[]> arg1)
            throws MociConnectionProviderException {
        throw new MociConnectionProviderException(OUT_OF_USE_CASE);
    }

    @Override
    public Collection<MibChangeInfo> getMibChanges(final ConnectionConfig arg0, final int arg1) throws MociConnectionProviderException {
        throw new MociConnectionProviderException(OUT_OF_USE_CASE);
    }

    @Override
    public NodeMetaInfo getNodeInfo(final ConnectionConfig arg0) throws MociConnectionProviderException {
        throw new MociConnectionProviderException(OUT_OF_USE_CASE);
    }

    @Override
    public Date getRestartNodeDate(final ConnectionConfig arg0) throws MociConnectionProviderException {
        throw new MociConnectionProviderException(OUT_OF_USE_CASE);
    }


    /* (non-Javadoc)
     * @see com.ericsson.oss.mediation.network.api.MociCMConnectionProvider#createMO(com.ericsson.oss.mediation.network.api.util.ConnectionConfig, java.lang.String, java.lang.String, java.util.Map)
     */
    @Override
    public void createMO(ConnectionConfig connectionData, String fdn, String moType,
            Map<String, NodeAttributeModelDTO> attributes)
            throws MociConnectionProviderException {
        mockUnreachableIP(connectionData);
        connectionProviderVerifier.setCreateInvoked();
    }

    /* (non-Javadoc)
     * @see com.ericsson.oss.mediation.network.api.MociCMConnectionProvider#deleteMO(com.ericsson.oss.mediation.network.api.util.ConnectionConfig, java.lang.String)
     */
    @Override
    public void deleteMO(ConnectionConfig connectionData, String fdn)
            throws MociConnectionProviderException {
        mockUnreachableIP(connectionData);
        connectionProviderVerifier.setDeleteInvoked();
    }

    /* (non-Javadoc)
     * @see com.ericsson.oss.mediation.network.api.MociCMConnectionProvider#setDnPrefix(com.ericsson.oss.mediation.network.api.util.ConnectionConfig, java.lang.String)
     */
    @Override
    public boolean setDnPrefix(ConnectionConfig connectionData, String newPrefix) throws MociConnectionProviderException {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.ericsson.oss.mediation.network.api.MociCMConnectionProvider#getMOAttributes(com.ericsson.oss.mediation.network.api.util.ConnectionConfig, java.lang.String, java.util.Collection)
     */
    @Override
    public Map<String, Object> getMOAttributes(ConnectionConfig connectionData, String fdn, Collection<String> moAttributes)
            throws MociConnectionProviderException {
        connectionProviderVerifier.setReadInvoked();
        Map<String, Object> fromNodeAtts = new HashMap<>();
        if (!moAttributes.isEmpty()){
            if (fdn.equals(IP_ROUTING_TABLE_FDN)){
                fromNodeAtts.put(moAttributes.iterator().next(), "userIdValueFromNode");
            }else if(fdn.equals(IP_ROUTING_TABLE_NOT_EXIST_FDN)){
                throw new MociConnectionProviderException("Mo does not exist!");
            }
        }
        return fromNodeAtts;
    }
    
    /**
     * throws exception simulating when client executes a unreachable IP (see
     * TestConstants.IP_ADDRESS_VALUE_UN_REACHABLE).
     * 
     * @param connectionData
     * @throws MociConnectionProviderException
     */
    private void mockUnreachableIP(final ConnectionConfig connectionData) throws MociConnectionProviderException {
        if (connectionData.getIpAddress().equals(IP_ADDRESS_VALUE_UN_REACHABLE)) {
            throw new MociConnectionProviderException("Mock connection timeout in the Node.");
        }
    }

    /**
     * throws exception simulating when client executes a not allowed action
     * (setStartable node action).
     * 
     * @param actionName
     * @throws MociConnectionProviderException
     */
    private void mockActionNotAllowed(final String actionName) throws MociConnectionProviderException {
        if (actionName.equals(SET_STARTABLE)) {
            throw new MociConnectionProviderException("Mock action not allowed");
        }
    }

}
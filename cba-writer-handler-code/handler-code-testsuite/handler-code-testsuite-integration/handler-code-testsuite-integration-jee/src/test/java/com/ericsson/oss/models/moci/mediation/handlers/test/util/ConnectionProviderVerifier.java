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
package com.ericsson.oss.models.moci.mediation.handlers.test.util;

/**
 * Interface definition for test proposal to verify if the mocked node connector
 * was contacted.
 * 
 * @author ecasdia
 */
public interface ConnectionProviderVerifier {

    String CONNECTION_COUNTER_VERIFIER_JNDI = "java:/connectionProviderVerifier";

    void setGetConnectionInvoked();

    void setModifyInvoked();

    void setActionInvoked();
    
    void setCreateInvoked();
    
    void setDeleteInvoked();
    
    void setReadInvoked();

    boolean isModifyInvoked();

    boolean isActionInvoked();

    boolean isGetConnectionInvoked();

    boolean isCreateInvoked();
    
    boolean isDeleteInvoked();
    
    boolean isReadInvoked();

    void resetAll();

}

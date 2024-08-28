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

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Singleton;

@Singleton
@EJB(name = ConnectionProviderVerifier.CONNECTION_COUNTER_VERIFIER_JNDI, beanInterface = ConnectionProviderVerifier.class)
@Remote(ConnectionProviderVerifier.class)
public class MociCmConnectionProviderCounter implements ConnectionProviderVerifier {

    private boolean getConnectionInvoked = false;
    private boolean actionInvoked = false;
    private boolean modifyInvoked = false;
    private boolean createInvoked = false;
    private boolean deleteInvoked = false;
    private boolean readInvoked = false;

    @Override
    public void setGetConnectionInvoked() {
        getConnectionInvoked = true;
    }

    @Override
    public void setModifyInvoked() {
        modifyInvoked = true;
    }

    @Override
    public void setActionInvoked() {
        actionInvoked = true;
    }
    
    @Override
    public void setCreateInvoked() {
        createInvoked = true;
    }
    
    @Override
    public void setDeleteInvoked() {
        deleteInvoked = true;
    }
    
    @Override
    public void setReadInvoked() {
        readInvoked= true;
    }

    @Override
    public boolean isCreateInvoked() {
        return createInvoked;
    }
    
    @Override
    public boolean isDeleteInvoked() {
        return deleteInvoked;
    }
    
    @Override
    public boolean isModifyInvoked() {
        return modifyInvoked;
    }

    @Override
    public boolean isActionInvoked() {
        return actionInvoked;
    }
    
    @Override
    public boolean isReadInvoked(){
        return readInvoked;
    }
    
    @Override
    public boolean isGetConnectionInvoked() {
        return getConnectionInvoked;
    }

    @Override
    public void resetAll() {
        getConnectionInvoked = false;
        actionInvoked = false;
        modifyInvoked = false;
        createInvoked = false;
        deleteInvoked = false;
        readInvoked = false;
    }



}

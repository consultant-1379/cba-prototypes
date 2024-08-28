/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2013
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.oss.mediation.util.transport.ssh;


public class SshConnection {

    private Object connection = null;

    /**
     * @return
     */
    public Object getConnection() {
        return connection;
    }

    /**
     * @param connection
     */
    public void setConnection(final Object connection) {
        this.connection = connection;
    }

}


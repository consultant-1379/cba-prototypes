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

package com.ericsson.oss.mediation.util.netconf.vo;

import java.io.Serializable;

/**
 * 
 * @author xvaltda
 */
public class NetconfVo implements Serializable {
    private boolean isErrorReceived;

    public boolean isErrorReceived() {
        return isErrorReceived;
    }

    public void setErrorReceived(final boolean isErrorReceived) {
        this.isErrorReceived = isErrorReceived;
    }

}

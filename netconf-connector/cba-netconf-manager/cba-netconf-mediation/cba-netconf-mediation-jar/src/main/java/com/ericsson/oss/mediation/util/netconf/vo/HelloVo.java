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

import com.ericsson.oss.mediation.util.netconf.api.Capability;
import java.util.List;

/**
 * 
 * @author xvaltda
 */
public class HelloVo extends NetconfVo {

    private List<Capability> capabilities;
    private String sessionId;

    public List<Capability> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(final List<Capability> capabilities) {
        this.capabilities = capabilities;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(final String sessionId) {
        this.sessionId = sessionId;
    }

}

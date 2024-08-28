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

package com.ericsson.oss.mediation.util.netconf.flow;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author xvaltda
 */
public class FlowSession {
    private final Map<String, Object> session;

    public FlowSession() {
        session = new HashMap<String, Object>();
    }

    public Object get(final String key) {
        return (key != null) ? session.get(key) : null;

    }

    public void clean() {
        session.clear();
    }

    public void put(final String key, final Object object) {
        session.put(key, object);
    }
}

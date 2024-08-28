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

package com.ericsson.oss.mediation.util.netconf.operation;

import com.ericsson.oss.mediation.util.netconf.api.Filter;

/**
 * 
 * @author xvaltda
 */
public class Get extends Operation {

    private final Filter filter;

    public Get(final Object filter) {
        this.filter = (Filter) filter;
    }

    @Override
    public String getBody() {
        return (filter == null ? "\t<get/>\n" : String.format(
                "\t<get>\t\t<filter type=\"%s\">%s\t\t</filter>\t</get>\n", filter.getType(), filter.asString()));
    }

}

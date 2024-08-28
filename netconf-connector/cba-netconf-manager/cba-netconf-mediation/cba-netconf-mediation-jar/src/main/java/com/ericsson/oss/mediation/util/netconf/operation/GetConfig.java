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

import com.ericsson.oss.mediation.util.netconf.api.Datastore;
import com.ericsson.oss.mediation.util.netconf.api.Filter;

/**
 * 
 * @author xvaltda
 */
public class GetConfig extends Operation {

    private final Datastore source;
    private final Filter filter;

    public GetConfig() {
        this.source = Datastore.RUNNING;
        filter = null;
    }

    public GetConfig(final Datastore source, final Filter filter) {
        this.source = source == null ? Datastore.RUNNING : source;
        this.filter = filter;
    }

    @Override
    public String getBody() {
        return String.format(
                "\t<get-config>\n\t\t<source>\n\t\t\t<%s/>\n\t\t</source>%s\t</get-config>\n",
                source.asParameter(),
                filter == null ? "" : String.format("\t<filter type=\"%s\">\n%s\n</filter>\n", filter.getType(),
                        filter.asString()));
    }

}

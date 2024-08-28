package com.ericsson.oss.mediation.util.netconf.filter;

import com.ericsson.oss.mediation.util.netconf.api.Filter;

public class SubTreeFilter implements Filter {

    protected final String filter;
    protected final static String TYPE = "subtree";

    public SubTreeFilter(final String filter) {
        this.filter = filter;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String asString() {
        return filter;
    }
}

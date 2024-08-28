package com.ericsson.oss.mediation.util.netconf.rpc.request;

import com.ericsson.oss.mediation.util.transport.api.TransportData;

public abstract class Request implements Iterable<String> {

    protected static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    protected static final String NETCONF_END_TAG = "]]>]]>" + System.lineSeparator();

    public abstract TransportData getTransportData();

}

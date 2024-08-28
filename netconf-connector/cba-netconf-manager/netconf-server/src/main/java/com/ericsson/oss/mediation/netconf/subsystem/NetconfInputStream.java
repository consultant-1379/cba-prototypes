package com.ericsson.oss.mediation.netconf.subsystem;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class NetconfInputStream extends FilterInputStream {

    public NetconfInputStream(final InputStream in) {
        super(in);
    }

    @Override
    public void close() throws IOException {

    }

    public void forceClose() throws IOException {
        super.close();
    }

}

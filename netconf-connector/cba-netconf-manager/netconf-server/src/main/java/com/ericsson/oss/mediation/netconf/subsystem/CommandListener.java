package com.ericsson.oss.mediation.netconf.subsystem;

import com.ericsson.oss.mediation.util.netconf.api.Datastore;
import com.ericsson.oss.mediation.util.netconf.api.Filter;
import java.io.PrintWriter;
import java.util.List;

public interface CommandListener {

    void closeSession(String messageId, PrintWriter out);

    void hello(List<String> capabilities, int sessionId, PrintWriter out);

    void get(String messageId, Filter filter, PrintWriter out);

    void getConfig(String messageId, Datastore source, Filter filter, PrintWriter out);

    void killSession(String messageId, int sessionId, PrintWriter out);
}

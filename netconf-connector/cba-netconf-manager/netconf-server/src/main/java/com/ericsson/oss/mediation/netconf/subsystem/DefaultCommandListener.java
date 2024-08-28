package com.ericsson.oss.mediation.netconf.subsystem;

import com.ericsson.oss.mediation.util.netconf.api.Datastore;
import com.ericsson.oss.mediation.util.netconf.api.Filter;
import java.io.PrintWriter;
import java.util.List;

public class DefaultCommandListener implements CommandListener {

    @Override
    public void closeSession(final String messageId, final PrintWriter out) {
        out.print("<rpc-reply message-id=\"");
        out.print(messageId);
        out.println("\"");
        out.println("\txmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">");
        out.println("\t<ok/>");
        out.println("</rpc-reply>");
        out.println("]]>]]>");
    }

    @Override
    public void hello(final List<String> capabilities, final int sessionId, final PrintWriter out) {
        out.println("<hello xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">");
        out.println("\t<capabilities>");
        out.print("\t\t<capability>");
        out.print("urn:ietf:params:netconf:base:1.1");
        out.println("</capability>");
        out.print("\t\t<capability>");
        out.print("urn:ietf:params:netconf:capability:startup:1.0");
        out.println("</capability>");
        out.print("\t\t<capability>");
        out.print("http://example.net/router/2.3/myfeature");
        out.println("</capability>");
        out.println("\t</capabilities>");
        out.print("\t<session-id>");
        out.print(sessionId);
        out.println("</session-id>");
        out.println("</hello>");
        out.println("]]>]]>");
    }

    @Override
    public void get(final String messageId, final Filter filter, final PrintWriter out) {
        out.print("<rpc-reply message-id=\"");
        out.print(messageId);
        out.println("\"");
        out.println("\txmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">");
        out.println("\t<data>");
        out.println("\t\t<top xmlns=\"http://example.com/schema/1.2/stats\">");
        out.println("\t\t\t<interfaces>");
        out.println("\t\t\t\t<interface>");
        out.println("\t\t\t\t\t<ifName>eth0</ifName>");
        out.println("\t\t\t\t\t<ifInOctets>45621</ifInOctets>");
        out.println("\t\t\t\t\t<ifOutOctets>774344</ifOutOctets>");
        out.println("\t\t\t\t</interface>");
        out.println("\t\t\t</interfaces>");
        out.println("\t\t</top>");
        out.println("\t</data>");
        out.println("</rpc-reply>");
        out.println("]]>]]>");
    }

    @Override
    public void getConfig(final String messageId, final Datastore source, final Filter filter, final PrintWriter out) {
        out.print("<rpc-reply message-id=\"");
        out.print(messageId);
        out.println("\"");
        out.println("\txmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">");
        out.println("\t<data>");
        out.println("\t\t<top xmlns=\"http://example.com/schema/1.2/config\">");
        out.println("\t\t\t<users>");
        out.println("\t\t\t\t<user>");
        out.println("\t\t\t\t\t<name>root</name>");
        out.println("\t\t\t\t\t<type>superuser</type>");
        out.println("\t\t\t\t\t<full-name>Charlie Root</full-name>");
        out.println("\t\t\t\t\t<company-info>");
        out.println("\t\t\t\t\t\t<dept>1</dept>");
        out.println("\t\t\t\t\t\t<id>1</id>");
        out.println("\t\t\t\t\t</company-info>");
        out.println("\t\t\t\t</user>");
        out.println("\t\t\t\t<!-- additional <user> elements appear here... -->");
        out.println("\t\t\t</users>");
        out.println("\t\t</top>");
        out.println("\t</data>");
        out.println("</rpc-reply>");
        out.println("]]>]]>");
    }

    @Override
    public void killSession(final String messageId, final int sessionId, final PrintWriter out) {
        out.print("<rpc-reply message-id=\"");
        out.print(messageId);
        out.println("\"");
        out.println("\txmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">");
        out.println("\t<ok/>");
        out.println("</rpc-reply>");
        out.println("]]>]]>");
    }
}
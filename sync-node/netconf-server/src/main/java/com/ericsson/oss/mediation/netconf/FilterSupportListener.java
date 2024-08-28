package com.ericsson.oss.mediation.netconf;

import com.ericsson.oss.mediation.netconf.subsystem.DefaultCommandListener;
import com.ericsson.oss.mediation.util.netconf.api.Filter;

import java.io.PrintWriter;
import java.util.List;

public class FilterSupportListener extends DefaultCommandListener {

    @Override public void hello(List<String> capabilities, int sessionId, PrintWriter out) {
        out.println("<hello xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">");
        out.println("\t<capabilities>");
        out.print("\t\t<capability>");
        out.print("urn:ietf:params:netconf:base:1.0");
        out.println("</capability>");
        out.print("\t\t<capability>");
        out.print("urn:ietf:params:xml:ns:netconf:base:1.0");
        out.println("</capability>");
        out.println("\t</capabilities>");
        out.print("\t<session-id>");
        out.print(sessionId);
        out.println("</session-id>");
        out.println("</hello>");
        out.println("]]>]]>");
    }

    @Override
    public void get(String messageId, Filter filter, PrintWriter out) {
        out.print("<rpc-reply message-id=\"");
        out.print(messageId);
        out.println("\"");
        out.println("\txmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">");
        out.println("\t<data>");
        out.println("\t\t<ManagedElement xmlns=\"urn:com:ericsson:ecim:SgsnMmeTop\">");
        out.println("\t\t\t<managedElementId>selnpcnsgsnmme043</managedElementId>");
        out.println("\t\t\t<localDateTime>2014-09-16T15:30:03</localDateTime>");
        out.println("\t\t\t<timeZone>CET</timeZone>");
        out.println("\t\t\t<dateTimeOffset>+02:00</dateTimeOffset>");
        out.println("\t\t\t<managedElementType>SGSN-MME</managedElementType>");
        out.println("\t\t\t<release>14B-00-02</release>");
        out.println("\t\t</ManagedElement>");
        out.println("\t</data>");
        out.println("</rpc-reply>");
        out.println("]]>]]>");
    }

}

package com.ericsson.oss.mediation.netconf.subsystem.parser.operation;

import com.ericsson.oss.mediation.netconf.subsystem.CommandListener;
import org.xml.sax.Attributes;

import java.io.PrintWriter;

public interface Operation {

    Operation processTag(String tag, Attributes attributes, int level);

    Operation processEndTag(String tag, int level);

    Operation characters(char[] ch, int start, int length);

    Operation invoke(CommandListener commandListener, PrintWriter out);

}

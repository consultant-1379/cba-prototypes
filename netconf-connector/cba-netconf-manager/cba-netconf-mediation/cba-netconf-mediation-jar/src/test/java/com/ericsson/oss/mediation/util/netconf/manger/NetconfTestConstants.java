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

package com.ericsson.oss.mediation.util.netconf.manger;

//import static com.ericsson.oss.mediation.util.netconf.operation.Operation.XML_HEADER;

/**
 * @author xvaltda
 */
public final class NetconfTestConstants {
    public static final String XMLNS = "urn:ietf:params:xml:ns:netconf:base:1.0";
    public static String CLOSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + System.lineSeparator()
            + "<rpc message-id=\"1\"" + System.lineSeparator() + "\txmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">"
            + System.lineSeparator() + "\t<close-session/>" + System.lineSeparator() + "</rpc>"
            + System.lineSeparator() + "]]>]]>" + System.lineSeparator();

    public static final String HELLO = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<hello xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">" + "	<capabilities>"
            + "		<capability>urn:ietf:params:xml:ns:netconf:base:1.0</capability>" + "	</capabilities>"
            + "	<session-id>1</session-id>" + "</hello>";

    public static final String HELLO_WITHOUT_SESSION_ID = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<hello xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">" + "	<capabilities>"
            + "		<capability>urn:ietf:params:xml:ns:netconf:base:1.0</capability>" + "	</capabilities>" + "</hello>";

    public static final String RPC_OK = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<rpc-reply xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\" message-id=\"4\">\n" + "<ok/>\n"
            + "</rpc-reply>";

    public static final String CLOSE_SESSION = " <?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<rpc message-id=\"	xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">\n" + "	<close-session/>\n"
            + "</rpc>\n" + "]]>]]>";

    public static final String BASE_CAPABILITY = "urn:ietf:params:netconf:base:1.0";
    public static final String HELLO_CAPABILITY = "urn:ietf:params:xml:ns:netconf:base:1.0";

    public static final String KILL_SESSION = " <?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<rpc message-id=\"1\" xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">\n" + "<kill-session>\n"
            + "<session-id>1</session-id>\n" + "</kill-session>\n" + "</rpc>\n";

    public static final String RPC_ERROR = "<rpc-reply xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">\n"
            + "<rpc-error>\n" + "  <error-type>rpc</error-type>\n" + "  <error-tag>missing-attribute</error-tag>\n"
            + "  <error-severity>error</error-severity>\n" + "  <error-info>\n"
            + "    <bad-attribute>message-id</bad-attribute>\n" + "    <bad-element>rpc</bad-element>\n"
            + "  </error-info>\n" + "</rpc-error>\n" + "</rpc-reply>";
    
    public static final String RPC_ERROR_2 ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
"<rpc-reply xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\" message-id=\"1\">\n" +
"<rpc-error>\n" +
"    <error-type>application</error-type>\n" +
"    <error-tag>operation-failed</error-tag>\n" +
"    <error-severity>error</error-severity>\n" +
"    <error-message xml:lang=\"en\">Error info:\n "
            + "{unknown_attribute,\"sgsnMmeTop:ManagedElement\",'SysM'}\n" +
"    </error-message>\n" +
"</rpc-error>\n" +
"</rpc-reply>";
    
public static final String RPC_ERROR_3 = 
"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
"<rpc-reply xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\" >\n" +
"  <rpc-error>\n" +
"    <error-type>application</error-type>\n" +
"    <error-tag>operation-failed</error-tag>\n" +
"    <error-severity>error</error-severity>\n" +
"    <error-message>\n" +
"      Unexpected error: {failed_to_parse_xml,\n" +
"    {fatal,\n" +
"        {{endtag_does_not_match,{was,ggSysM,should_have_been,'SysM'}},\n" +
"         {file,file_name_unknown},\n" +
"         {line,4},\n" +
"         {col,266}}}}\n" +
"    </error-message>\n" +
"  </rpc-error>\n" +
"</rpc-reply>";

public final static String RPC_ERROR_MISSING_ELEMENT = 
"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
"<rpc-reply xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\" message-id=\"3\">\n" +
"<rpc-error>\n" +
"    <error-type>application</error-type>\n" +
"    <error-tag>missing-element</error-tag>\n" +
"    <error-severity>error</error-severity>\n" +
"    <error-info>\n" +
"        <missing-element>systemFunctionsId</missing-element>\n" +
"    </error-info>\n" +
"    <error-message xml:lang=\"en\">MO key attribute missing</error-message>\n" +
"</rpc-error>\n" +
"</rpc-reply>";

}
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.management.MXBean;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

public class DelayedInput
extends DefaultHandler {
   private List<String> elementNameList = new ArrayList<String>();
   private long MAX_MEM_USED = 0l;

    //
    // Constants
    //

    // feature ids

    /** Namespaces feature id (http://xml.org/sax/features/namespaces). */
//    protected static final String NAMESPACES_FEATURE_ID = "http://xml.org/sax/features/namespaces";

    /** Validation feature id (http://xml.org/sax/features/validation). */
//    protected static final String VALIDATION_FEATURE_ID = "http://xml.org/sax/features/validation";

    /** Schema validation feature id (http://apache.org/xml/features/validation/schema). */
//    protected static final String SCHEMA_VALIDATION_FEATURE_ID = "http://apache.org/xml/features/validation/schema";

    /** Schema full checking feature id (http://apache.org/xml/features/validation/schema-full-checking). */
//    protected static final String SCHEMA_FULL_CHECKING_FEATURE_ID = "http://apache.org/xml/features/validation/schema-full-checking";

    // default settings

    /** Default parser name. */
//    protected static final String DEFAULT_PARSER_NAME = "org.apache.xerces.parsers.SAXParser";

    /** Default namespaces support (true). */
//    protected static final boolean DEFAULT_NAMESPACES = true;

    /** Default validation support (false). */
//    protected static final boolean DEFAULT_VALIDATION = false;

    /** Default Schema validation support (false). */
//    protected static final boolean DEFAULT_SCHEMA_VALIDATION = false;

    /** Default Schema full checking support (false). */
//    protected static final boolean DEFAULT_SCHEMA_FULL_CHECKING = false;

    //
    // Data
    //

    /** Print writer. */
    protected PrintWriter fOut;

    //
    // Constructors
    //

    /** Default constructor. */
    public DelayedInput() {
    } // <init>()

    //
    // ContentHandler methods
    //

    /** Start element. */
    @Override
    public void startElement(final String uri, final String localpart, final String rawname,
            final Attributes attrs) throws SAXException {

//        System.out.println("("+rawname);
        elementNameList.add(rawname);
//        final int length = attrs != null ? attrs.getLength() : 0;
//        for (int i = 0; i < length; i++) {
//            System.out.println("A"+attrs.getQName(i)+' '+attrs.getValue(i));
//        }

    } // startElement(String,String,String,Attributes)

    /** End element. */
    @Override
    public void endElement(final String uri, final String localpart, final String rawname)
            throws SAXException {
//        System.out.println(")"+rawname);
	long memoryUsage = getMemoryUsage();
	if(memoryUsage > MAX_MEM_USED)
	{
	    MAX_MEM_USED = memoryUsage;
	}
    } // endElement(String,String,String)

    //
    // ErrorHandler methods
    //

    /** Warning. */
    @Override
    public void warning(final SAXParseException ex) throws SAXException {
        printError("Warning", ex);
    } // warning(SAXParseException)

    /** Error. */
    @Override
    public void error(final SAXParseException ex) throws SAXException {
        printError("Error", ex);
    } // error(SAXParseException)

    /** Fatal error. */
    @Override
    public void fatalError(final SAXParseException ex) throws SAXException {
        printError("Fatal Error", ex);
        throw ex;
    } // fatalError(SAXParseException)

    //
    // Protected methods
    //

    /** Prints the error message. */
    protected void printError(final String type, final SAXParseException ex) {

        System.err.print("[");
        System.err.print(type);
        System.err.print("] ");
        String systemId = ex.getSystemId();
        if (systemId != null) {
            final int index = systemId.lastIndexOf('/');
            if (index != -1) {
                systemId = systemId.substring(index + 1);
            }
            System.err.print(systemId);
        }
        System.err.print(':');
        System.err.print(ex.getLineNumber());
        System.err.print(':');
        System.err.print(ex.getColumnNumber());
        System.err.print(": ");
        System.err.print(ex.getMessage());
        System.err.println();
        System.err.flush();

    } // printError(String,SAXParseException)

    public List<String> getElementNameList() {
        return elementNameList;
    }
    
    public static long getMemoryUsage() {
   	Runtime	runtime = Runtime.getRuntime();
   	long totalMemory = runtime.totalMemory();
   	long freeMemory = runtime.freeMemory();
   	long usedMemory = totalMemory - freeMemory;
   	return usedMemory;
       }

    public long getMAX_MEM_USED() {
        return MAX_MEM_USED;
    }

    public void setElementNameList(List<String> elementNameList) {
        this.elementNameList = elementNameList;
    }


}
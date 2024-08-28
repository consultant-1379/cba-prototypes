import java.io.IOException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

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

public class SAXPArser {
    private final InputStreamImpl in ;
    protected static final String DEFAULT_PARSER_NAME = "org.apache.xerces.parsers.SAXParser";

    /**
     * @param in
     */
    public SAXPArser(final InputStreamImpl in) {
        super();
        System.out.println("STAXPArser Constructor");
        this.in = in;
    }


    public void parse() throws  IOException, SAXException
    {
	long memoryUsage = DelayedInput.getMemoryUsage();
	float memoryUsageMB = memoryUsage/(1024 * 1024);
	System.out.println("start memeory usage - " + memoryUsageMB);
	System.out.println("creating handler");
	final DelayedInput handler = new DelayedInput();
	System.out.println("creating parser");
	long startTime = System.currentTimeMillis();
	final XMLReader parser = XMLReaderFactory.createXMLReader(DEFAULT_PARSER_NAME);
	System.out.println("setting handler");
	parser.setContentHandler(handler);
	parser.setErrorHandler(handler);
	System.out.println("creating source");
	final InputSource source = new InputSource(in);
	System.out.println("parsing");
	parser.parse(source);
	System.out.println("parsed");
	printStats(handler, startTime);
	handler.setElementNameList(null);
    }


    private void printStats(final DelayedInput handler, long startTime) {
	long memoryUsage;
	float memoryUsageMB;
	long endTime = System.currentTimeMillis();
	float timetaken = (endTime - startTime)/1000;
	memoryUsage = DelayedInput.getMemoryUsage();
	memoryUsageMB = memoryUsage/(1024 * 1024);
	System.out.println("end memeory usage - " + memoryUsageMB);
	long maxMemUsageParsing = handler.getMAX_MEM_USED()/(1024*1024);
	System.out.println("max memory usage while parsing -" + maxMemUsageParsing);
	System.out.println("timetaken="+timetaken);
	System.out.println("element parsed-" + handler.getElementNameList().size());
    }

   public void staxParser()
   {
//	final XMLInputFactory factory = XMLInputFactory.newInstance();
	//	factory.setProperty("javax.xml.stream.isValidating", new Boolean(false));
	//	System.out.println("creating stream reader");
	//	final XMLStreamReader reader = factory.createXMLStreamReader(in);
	//	System.out.println("created stream reader");
	//
	//	while (reader.hasNext()) {
	//	    System.out.println("inside while loop");
	//	    final int Event = reader.next();
	//	    System.out.println("Event--" + Event);
	//	    switch (Event) {
	//	    case XMLStreamConstants.START_ELEMENT: {
	//		System.out.println("START_ELEMENT");
	//		break;
	//	    }
	//	    case XMLStreamConstants.CHARACTERS: {
	//		final String text = reader.getText().trim();
	//		System.out.println(text);
	//		break;
	//	    }
	//	    case XMLStreamConstants.END_ELEMENT: {
	//		System.out.println("END_ELEMENT");
	//		break;
	//	    }
	//	    }
	//	}
   }
}

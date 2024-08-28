import java.io.IOException;

import org.xml.sax.*;
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

public class ThreadParser extends Thread
{
    private final DelayedInputStream delayedInputStream;
    private final  DefaultHandler handler = new DelayedInput();
    private XMLReader parser = null;
    /** Default parser name. */
    protected static final String DEFAULT_PARSER_NAME = "org.apache.xerces.parsers.SAXParser";
    private InputSource source =null;

    /**
     * @param delayedInputStream
     * @param delayedInput
     * @throws SAXException 
     */
    public ThreadParser(final DelayedInputStream inputStream) throws SAXException {
        super();
        this.delayedInputStream =inputStream;
        source= new InputSource(delayedInputStream);
        parser = XMLReaderFactory.createXMLReader(DEFAULT_PARSER_NAME);
        parser.setContentHandler(handler);
        parser.setErrorHandler(handler);
    }
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        System.out.println("running parser");
        final byte[] buffer = new byte[1024];
        boolean dataArived = delayedInputStream.isDataArived();

        int count = 0;
        while(!(dataArived ^ (count>0))){
            System.out.println("in the loop");
            synchronized (delayedInputStream) {

                //wait for input-stream to fill
                try {
                    dataArived = delayedInputStream.isDataArived();
                    if(dataArived){
                        System.out.println("dataarived");
                        final boolean dataFinished = delayedInputStream.isDataFinished();
                        if(dataFinished)
                        {
                            break;
                        }
                        final int availableBytes = delayedInputStream.getInputStream().available();
                        if(availableBytes <= 0){
                            System.out.println("read--AVAILABLE BYTES - " + availableBytes);
                            delayedInputStream.wait();
                        }
                        count = delayedInputStream.read(buffer, 0, 1024);

                        delayedInputStream.notify();
                    }
                    else
                    {
                        System.out.println("waiting for the data");
                        delayedInputStream.wait();
                        //                        Thread.sleep(100);
                    }
                } catch (final IOException |  InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    break;
                }
            }
        }
        System.out.println("pRSER FINISH ITS JOB");
    }

    /**
     * @param args
     */
    public static void main(final String[] args) {
        // TODO Auto-generated method stub

    }

}

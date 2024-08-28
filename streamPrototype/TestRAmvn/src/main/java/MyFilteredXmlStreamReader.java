
import javax.xml.stream.StreamFilter;
import javax.xml.stream.XMLStreamReader;

import com.ctc.wstx.stax.FilteredStreamReader;
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

public class MyFilteredXmlStreamReader extends  FilteredStreamReader{
    private InputStreamImpl impl ;

    /**
     * @param r
     * @param f
     */
    public MyFilteredXmlStreamReader(final XMLStreamReader r, final StreamFilter f) {
        super(r, f);
    }


    @Override
    public boolean hasNext()
    {
        System.out.println("HASHNEXT");
        final int count = 0;
        boolean isNext = false;
        //        try {
        //            count = impl.read();
        //        } catch (final IOException e) {
        //            // TODO Auto-generated catch block
        //            e.printStackTrace();
        //        }
        if(count >=0)
        {
            isNext = true;
        }
        System.out.println("return- " + isNext);
        return isNext;
    }



    /**
     * @return the impl
     */
    public InputStreamImpl getImpl() {
        return impl;
    }


    /**
     * @param impl the impl to set
     */
    public void setImpl(final InputStreamImpl impl) {
        this.impl = impl;
    }



}

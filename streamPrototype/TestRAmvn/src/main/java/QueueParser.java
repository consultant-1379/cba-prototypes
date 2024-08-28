import java.util.*;


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

public class QueueParser {

    private  Deque<String> dequeA =null;
    private final Map<String, Object> objectMaps = new HashMap<String, Object>();




    /**
     * @param dequeA
     */
    public QueueParser(final Deque<String> dequeA) {
        super();
        this.dequeA = dequeA;
    }

    public void parse()
    {
        final String element = dequeA.poll();
        System.out.println(element);
        if(element.contains("<ManagedElement "))
        {
            objectMaps.put("ManagedElement", element);
        }
    }

    public int getQueueSize()
    {
        return dequeA.size();
    }

    /**
     * @param args
     */
    public static void main(final String[] args) {
    }
}

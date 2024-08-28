import java.util.Deque;
import java.util.LinkedList;


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

public class QueueReader {

    private  Deque<String> dequeA =null;




    /**
     * @param dequeA
     */
    public QueueReader() {
        super();
        this.dequeA = new LinkedList<String>();
    }

    public void read(final byte[] bytes)
    {
        final String msg = new String(bytes).trim();
        dequeA.add(msg);
    }

    public int getQueueSize()
    {
        return dequeA.size();
    }

    /**
     * @return the dequeA
     */
    public Deque<String> getDequeA() {
        return dequeA;
    }

    /**
     * @param args
     */
    public static void main(final String[] args) {
    }
}

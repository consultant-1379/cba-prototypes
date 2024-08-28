import java.io.*;

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

public class DelayedInputStream extends FilterInputStream {
    boolean dataArived = false;
    boolean dataFinished = false;
    //
    // Data
    //

    /** Random number generator. */

    //
    // Constructors
    //

    /** Constructs a delayed input stream from the specified input stream. */
    public DelayedInputStream(final InputStream in) {
        super(in);
    } // <init>(InputStream)

    //
    // InputStream methods
    //

    /** Performs a delayed block read. */
    @Override
    public int read(final byte[] buffer, final int offset, int length) throws IOException {
        System.out.println("inside read");

        // keep read small enough for display
        if (length > 48) {
            length = 48;
        }
        int count = 0;

        // read bytes and pause
        final long before = System.currentTimeMillis();
        count = in.read(buffer, offset, length);
        //            Thread.sleep(Math.abs(fRandom.nextInt()) % 2000);

        final long after = System.currentTimeMillis();

        // print output
        System.out.print("read "+count+" bytes in "+(after-before)+" ms: ");
        printBuffer(buffer, offset, count);
        System.out.println();

        // return number of characters read
        return count;

    } // read(byte[],int,int):int

    //
    // Private methods
    //

    /** Prints the specified buffer. */
    private void printBuffer(final byte[] buffer, final int offset, final int length) {

        // is there anything to do?
        if (length <= 0) {
            System.out.print("no data read");
            return;
        }

        // print buffer
        System.out.print('[');
        for (int i = 0; i < length; i++) {
            switch ((char)buffer[offset + i]) {
            case '\r': {
                System.out.print("\\r");
                break;
            }
            case '\n': {
                System.out.print("\\n");
                break;
            }
            default: {
                System.out.print((char)buffer[offset + i]);
            }
            }
        }
        System.out.print(']');

    } // printBuffer(byte[],int,int)

    public InputStream getInputStream()
    {
        return super.in;
    }

    /**
     * @return the dataArived
     */
    public boolean isDataArived() {
        return dataArived;
    }

    /**
     * @param dataArived the dataArived to set
     */
    public void setDataArived(final boolean dataArived) {
        this.dataArived = dataArived;
    }

    /**
     * @return the dataFinished
     */
    public boolean isDataFinished() {
        return dataFinished;
    }

    /**
     * @param dataFinished the dataFinished to set
     */
    public void setDataFinished(final boolean dataFinished) {
        this.dataFinished = dataFinished;
    }



}

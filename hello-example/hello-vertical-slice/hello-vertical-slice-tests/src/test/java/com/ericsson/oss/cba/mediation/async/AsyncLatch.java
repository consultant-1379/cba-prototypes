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
package com.ericsson.oss.cba.mediation.async;

import javax.ejb.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides a way of blocking tests from proceeding until all Asynchronous calls have executed. It is a more favourable solution to using
 * timers.
 * 
 * @author eshacow
 * 
 */
@Singleton
@LocalBean
@Lock(LockType.WRITE)
public class AsyncLatch {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncLatch.class);
    private boolean initialized;
    private int count;

    /**
     * Initializes the latch
     * 
     * @param count
     *            the number of threads to execute
     * @throws IllegalAccessException
     *             if the latch is already initialized or the count is less than 1
     */
    public void initialize(int count) throws IllegalAccessException {
        if (this.initialized == true || count <= 0) {
            throw new IllegalAccessException("Unable to initialize the AsyncLatch.");
        }
        this.count = count;
        this.initialized = true;
    }

    /**
     * Returns the status of the latch. An open latch still has threads executing
     * 
     * @return <code>true</code> if the latch is open<br>
     *         <code>false</code> if the latch is closed
     */
    public boolean isOpen() {
        return this.count != 0;
    }

    /**
     * Returns the number of threads still executing
     * 
     * @return the number of threads still executing
     */
    public int getCount() {
        return this.count;
    }

    /**
     * Decrements the count upon completion of a thread
     * 
     * @throws IllegalAccessException
     *             if the latch has not been intialized
     */
    public void decrementCount() throws IllegalAccessException {
        checkIfInitialized();
        this.count -= 1;
        LOGGER.info("Have decremented latch count to {}", this.count);
        if (this.count == 0) {
            this.initialized = false;
        }
    }

    /**
     * Resets the stauts of the latch
     */
    public void reset() {
        this.count = 0;
        this.initialized = false;
    }

    private void checkIfInitialized() throws IllegalAccessException {
        if (this.initialized == false) {
            throw new IllegalAccessException("The AsyncLatch must be initialized before use");
        }
    }

}

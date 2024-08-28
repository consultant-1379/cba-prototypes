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
package com.ericsson.cba.prototype.protocol.sshclient.ssh;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * This class is used to carry io stream objects and the session(channel) created between the application and the device
 * 
 */
public class SshSession {

    private Object session = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;

    /**
     * @return
     */
    public InputStream getInputStream() {
        return this.inputStream;
    }

    /**
     * @return
     */
    public OutputStream getOutputStream() {
        return this.outputStream;
    }

    /**
     * @return
     */
    public Object getSession() {
        return session;
    }

    /**
     * @param inputStream
     */
    public void setInputStream(final InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * @param outputStream
     */
    public void setOutputStream(final OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * @param session
     */
    public void setSession(final Object session) {
        this.session = session;
    }

}

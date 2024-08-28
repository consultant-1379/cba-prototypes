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
package com.ericsson.oss.mediation.util.transport.ssh;

import java.io.InputStream;
import java.io.OutputStream;

public class SshSession {

    private final Object session;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    public SshSession(final Object session, final InputStream inputStream, final OutputStream outputStream) {
        this.session = session;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

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

}

/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.oss.mediation.util.transport.tls.provider;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import com.ericsson.oss.mediation.adapter.tls.api.TlsConnectionRequest;
import com.ericsson.oss.mediation.adapter.tls.api.TlsRequest;
import com.ericsson.oss.mediation.adapter.tls.exception.TlsException;
import com.ericsson.oss.mediation.transport.api.exception.TransportConnectionRefusedException;
import com.ericsson.oss.mediation.transport.api.exception.TransportException;
import com.ericsson.oss.mediation.transport.api.provider.TransportProvider;
import com.ericsson.oss.mediation.transport.api.provider.TransportProviderListener;
import com.ericsson.oss.mediation.transport.tls.TlsConnectionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TLSTransportProviderImpl implements TransportProvider {

    TlsConnectionImpl tlsConnectionImpl;
    private static final Logger logger = LoggerFactory.getLogger(TLSTransportProviderImpl.class);

    public TLSTransportProviderImpl(final String hostname, final int port, final String netconfEndPattern) {
        final TlsConnectionRequest reqInfo = new TlsConnectionRequest(hostname, port);
        tlsConnectionImpl = new TlsConnectionImpl(reqInfo);
    }

    @Override
    public void openSession() throws TransportConnectionRefusedException {
        try {
            tlsConnectionImpl.connect();
        } catch (final TlsException e) {
            throw new TransportConnectionRefusedException(e);
        }
    }

    @Override
    public void openSession(final boolean buffered) throws TransportConnectionRefusedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void closeSession() throws TransportException {
        try {
            tlsConnectionImpl.disconnect();
        } catch (final TlsException e) {
            throw new TransportException(e);
        }
    }

    @Override
    public void executeAsyncCommand(final String command) throws TransportException {
        TlsRequest tlsRequest = new TlsRequest(command);
        try {
            tlsConnectionImpl.sendNonBlocking(tlsRequest);
        } catch (TlsException e) {
            throw new TransportException(e);
        }
    }

    @Override
    public String executeCommand(final String command) throws TransportException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void sendBytes(final byte... bytes) throws TransportException {
        // TODO Auto-generated method stub

    }

    @Override
    public int read() throws TransportException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int read(final byte[] bytes, final int offset, final int length) throws TransportException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isConnectionAlive() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public Pattern getCommandPrompt() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setWaitTimeForResponse(final long timeout, final TimeUnit timeUnit) {
        // TODO Auto-generated method stub

    }

    @Override
    public long getWaitTimeForResponse(final TimeUnit timeUnit) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setSocketTimeout(final long timeout, final TimeUnit timeUnit) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public long getSocketTimeout(final TimeUnit timeUnit) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Boolean isTransportProviderListenerSupported() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void registerTransportProviderListener(final TransportProviderListener listener) throws TransportException {
        // TODO Auto-generated method stub

    }

    @Override
    public void deregisterTransportProviderListener(final TransportProviderListener listener) throws TransportException {
        // TODO Auto-generated method stub

    }

}

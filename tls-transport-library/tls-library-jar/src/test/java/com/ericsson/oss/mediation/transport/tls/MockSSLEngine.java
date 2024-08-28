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
package com.ericsson.oss.mediation.transport.tls;

import sun.beans.editors.ByteEditor;

import java.nio.ByteBuffer;
import java.security.Principal;
import java.security.cert.Certificate;

import javax.net.ssl.*;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.security.cert.X509Certificate;

public class MockSSLEngine extends SSLEngine {

    public SSLEngineResult sslEngineResult;

    @Override
    public void beginHandshake() throws SSLException {
    }

    @Override
    public void closeInbound() throws SSLException {
    }

    @Override
    public void closeOutbound() {
    }

    @Override
    public Runnable getDelegatedTask() {
        return null;
    }

    @Override
    public boolean getEnableSessionCreation() {
        return false;
    }

    @Override
    public String[] getEnabledCipherSuites() {
        return null;
    }

    @Override
    public String[] getEnabledProtocols() {
        return null;
    }

    @Override
    public HandshakeStatus getHandshakeStatus() {
        return null;
    }

    @Override
    public boolean getNeedClientAuth() {
        return false;
    }

    @Override
    public SSLSession getSession() {
        return new SSLSession() {

            @Override
            public void removeValue(final String name) {
                // TODO Auto-generated method stub

            }

            @Override
            public void putValue(final String name, final Object value) {
                // TODO Auto-generated method stub

            }

            @Override
            public boolean isValid() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void invalidate() {
                // TODO Auto-generated method stub

            }

            @Override
            public String[] getValueNames() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Object getValue(final String name) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public SSLSessionContext getSessionContext() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getProtocol() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Principal getPeerPrincipal() throws SSLPeerUnverifiedException {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public int getPeerPort() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public String getPeerHost() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Certificate[] getPeerCertificates() throws SSLPeerUnverifiedException {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public X509Certificate[] getPeerCertificateChain() throws SSLPeerUnverifiedException {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public int getPacketBufferSize() {
                // TODO Auto-generated method stub
                return 100;
            }

            @Override
            public Principal getLocalPrincipal() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Certificate[] getLocalCertificates() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public long getLastAccessedTime() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public byte[] getId() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public long getCreationTime() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public String getCipherSuite() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public int getApplicationBufferSize() {
                return 100;
            }
        };
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return null;
    }

    @Override
    public String[] getSupportedProtocols() {
        return null;
    }

    @Override
    public boolean getUseClientMode() {
        return false;
    }

    @Override
    public boolean getWantClientAuth() {
        return false;
    }

    @Override
    public boolean isInboundDone() {
        return false;
    }

    @Override
    public boolean isOutboundDone() {
        return false;
    }

    @Override
    public void setEnableSessionCreation(final boolean arg0) {
    }

    @Override
    public void setEnabledCipherSuites(final String[] arg0) {
    }

    @Override
    public void setEnabledProtocols(final String[] arg0) {
    }

    @Override
    public void setNeedClientAuth(final boolean arg0) {
    }

    @Override
    public void setUseClientMode(final boolean arg0) {
    }

    @Override
    public void setWantClientAuth(final boolean arg0) {
    }

    @Override
    public SSLEngineResult unwrap(final ByteBuffer arg0, final ByteBuffer[] arg1, final int arg2, final int arg3) throws SSLException {
        return null;
    }

    @Override
    public SSLEngineResult wrap(final ByteBuffer[] srcs, final int offset, final int length, final ByteBuffer dst) throws SSLException {
        return null;
    }

    @Override
    public SSLEngineResult wrap(final ByteBuffer src, final ByteBuffer dst){
        while(src.hasRemaining()){
            dst.put(src.get());
        }

        SSLEngineResult result = new SSLEngineResult(SSLEngineResult.Status.OK, HandshakeStatus.NOT_HANDSHAKING, src.limit(), dst.position());
        return result;
    }

    @Override
    public SSLEngineResult unwrap(final ByteBuffer src, final ByteBuffer dst) throws SSLException {
        while(src.hasRemaining()){
            dst.put(src.get());
        }

        SSLEngineResult result = new SSLEngineResult(SSLEngineResult.Status.OK, HandshakeStatus.NOT_HANDSHAKING, src.limit(), dst.position());
        return result;
    }

}

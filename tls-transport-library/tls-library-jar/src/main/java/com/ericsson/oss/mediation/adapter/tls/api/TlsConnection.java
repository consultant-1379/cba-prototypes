/*
 * ------------------------------------------------------------------------------
 *  *******************************************************************************
 *  * COPYRIGHT Ericsson 2014
 *  *
 *  * The copyright to the computer program(s) herein is the property of
 *  * Ericsson Inc. The programs may be used and/or copied only with written
 *  * permission from Ericsson Inc. or in accordance with the terms and
 *  * conditions stipulated in the agreement/contract under which the
 *  * program(s) have been supplied.
 *  *******************************************************************************
 *  *----------------------------------------------------------------------------
 */
package com.ericsson.oss.mediation.adapter.tls.api;

import com.ericsson.oss.mediation.adapter.tls.exception.TlsException;
import com.ericsson.oss.mediation.transport.tls.TlsChannel;

public interface TlsConnection {

    /*
     * Sets up the SSL engine, opens up the socket channel and perform TLS handshake
     */
    void connect() throws TlsException;

    /*
     * Closes Inbound and Outbound network data to SSL engine. Signals the server to close the TLS connection.
     */
    void disconnect() throws TlsException;

    /*
     * Sends the request over TLS channel. This is a non-blocking call.
     */
    void sendNonBlocking(TlsRequest tlsRequest) throws TlsException;
    
    
    /*
     * Reads data sent back from the Node and stored in the Buffer.
     */
    TlsResponse read() throws TlsException;

	/**
	 * @return
	 */

	TlsChannel getTlsChannel();

}

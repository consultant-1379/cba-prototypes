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
package com.ericsson.oss.mediation.util.transport.api;

import com.ericsson.oss.mediation.util.transport.api.exception.TransportConnectionRefusedException;
import com.ericsson.oss.mediation.util.transport.api.exception.TransportException;

public interface TransportManager {
    void openConnection() throws TransportConnectionRefusedException, TransportException;

    void closeConnection() throws TransportException;

    void sendData(TransportData request) throws TransportException;

    void readData(TransportData response) throws TransportException;

    void readData(TransportData response, boolean close) throws TransportException;
    
    String getProtocolType ();
    TransportManagerCI getTransportManagerCI ();
}

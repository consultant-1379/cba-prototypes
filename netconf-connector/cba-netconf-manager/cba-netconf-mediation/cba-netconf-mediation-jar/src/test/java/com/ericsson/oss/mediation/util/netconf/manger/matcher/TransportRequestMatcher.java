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

package com.ericsson.oss.mediation.util.netconf.manger.matcher;

import com.ericsson.oss.mediation.util.transport.api.TransportData;
import com.ericsson.oss.mediation.util.transport.api.TransportSessionType;
import org.mockito.ArgumentMatcher;

/**
 * 
 * @author xvaltda
 */
public class TransportRequestMatcher extends ArgumentMatcher<Object> {

    private String hostname;
    private int port;
    private String username;
    private String password;
    private int socketConnectionTimeoutInMillis;
    private TransportSessionType sessionType;
    private String sessionTypeValue;
    private StringBuffer dataBuffer = new StringBuffer();

    public TransportRequestMatcher(StringBuffer dataBuffer) {
        this.dataBuffer = dataBuffer;
    }

    @Override
    public boolean matches(Object argument) {

        TransportData transportRequest = (TransportData) argument;

        return (transportRequest.getData().toString().trim().equals(dataBuffer.toString().trim()));
    }

}

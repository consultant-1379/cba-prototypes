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

/**
 *
 * @author xvaltda
 */
public class TransportManagerCI 
{   
    private String hostname;
    private int port;
    private String username;
    private String password;
    private int socketConnectionTimeoutInMillis;
    private TransportSessionType sessionType;
    private String sessionTypeValue;

    public String getHostname()
    {
        return hostname;
    }

    public void setHostname(final String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(final int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public int getSocketConnectionTimeoutInMillis() {
        return socketConnectionTimeoutInMillis;
    }

    public void setSocketConnectionTimeoutInMillis(final int socketConnectionTimeoutInMillis) {
        this.socketConnectionTimeoutInMillis = socketConnectionTimeoutInMillis;
    }

    public TransportSessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(final TransportSessionType sessionType) {
        this.sessionType = sessionType;
    }

    public String getSessionTypeValue() {
        return sessionTypeValue;
    }

    public void setSessionTypeValue(final String sessionTypeValue) {
        this.sessionTypeValue = sessionTypeValue;
    }

}
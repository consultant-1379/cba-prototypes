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
public enum TransportSessionType 
{
    SESSION("session"), SHELL("shell"), EXEC("exec"), X11("x11"), DIRECT_TCPIP("direct-tcpip"), SFTP("sftp"), SUBSYSTEM("subsystem");

    private String sessionTypeValue;

    /**
     * @param sessionTypeValue
     */
    private TransportSessionType(final String sessionTypeValue) 
    {
        this.sessionTypeValue = sessionTypeValue;
    }

    /**
     * @return
     */
    public String getValue() 
    {
        return this.sessionTypeValue;
    }    
}

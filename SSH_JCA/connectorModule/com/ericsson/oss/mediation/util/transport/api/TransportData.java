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
public class TransportData {
    
    private StringBuffer transportData;
    private  Character endData = '0';

    public byte[] getBytes () {
        return transportData.toString().getBytes();
    }
    
    public void setEndData (final Character endData) {
        this.endData = endData;
    }
    
    public Character getEndData () {
        return endData;
    }
    public StringBuffer getData() {
        return transportData;
    }

    public void setData(final StringBuffer response) {
        this.transportData = response;
    }
    
    public boolean isEndOfData (final Character data) {
    
        boolean endOfData = false;
        
        return endOfData;
    }
    
    
}

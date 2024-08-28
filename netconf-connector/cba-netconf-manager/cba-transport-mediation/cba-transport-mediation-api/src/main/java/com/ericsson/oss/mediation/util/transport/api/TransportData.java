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

import java.io.*;

/**
 * 
 * @author xvaltda
 */
public class TransportData {

    private StringBuilder transportData;
    private Character endData = '0';
    private long messageId;
    private Iterable<String> dataToWrite;

    public void setEndData(final Character endData) {
        this.endData = endData;
    }

    public Character getEndData() {
        return endData;
    }

    public StringBuilder getData() {
        return transportData;
    }

    public void setData(final StringBuilder response) {
        this.transportData = response;
    }

    public void setMessageId(final long messageId) {
        this.messageId = messageId;
    }

    public long getMessageId() {
        return messageId;
    }

    public void writeTo(final OutputStream outputStream) throws IOException {
        final OutputStreamWriter osw = new OutputStreamWriter(outputStream, "UTF-8");
        for (final String line : this.dataToWrite) {
            osw.write(line);
        }
        osw.flush();
    }

    public void writeTo(final StringBuilder output) {
        for (final String line : this.dataToWrite) {
            output.append(line);
        }
    }

    public void setWritableData(final Iterable<String> dataToWrite) {
        this.dataToWrite = dataToWrite;
    }
}

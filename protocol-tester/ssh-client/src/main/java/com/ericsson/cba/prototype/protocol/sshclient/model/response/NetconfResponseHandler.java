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
package com.ericsson.cba.prototype.protocol.sshclient.model.response;

import static com.ericsson.cba.prototype.protocol.sshclient.model.StaticValues.NETCONF_MESSAGE_END;
import static com.ericsson.cba.prototype.protocol.sshclient.util.Util.*;
import java.io.*;

import com.ericsson.cba.prototype.protocol.sshclient.model.Callback;


public class NetconfResponseHandler implements ResponseHandler {

    private final Callback callback;
    private final BufferedReader reader;

    /**
     * if subsystem type value is Netconf, this class is used for handling response.
     * 
     * @param inputStream
     * @param callback
     */
    public NetconfResponseHandler(final InputStream inputStream, final Callback callback) {
        this.callback = callback;
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
    }

    public String call() {
        String callbackMessage = "";
        try {
            callbackMessage = readResponseFromStream();
            callback(callbackMessage);
            if (isHelloHeader(callbackMessage)) {//this means netconf agent will provide one more message.
                callbackMessage = readResponseFromStream();
                callback(callbackMessage);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return callbackMessage;
    }

    private void callback(final String callbackMessage) {
        if (callback != null) {
            callback.onResponse(callbackMessage);
        }
    }

    private String readResponseFromStream() {
        String callbackMessage = "";
        try {
            String response = reader.readLine();
            while ((response != null) && !response.equalsIgnoreCase(NETCONF_MESSAGE_END)) {
                response = reader.readLine();
                callbackMessage += response;
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return callbackMessage;
    }

}

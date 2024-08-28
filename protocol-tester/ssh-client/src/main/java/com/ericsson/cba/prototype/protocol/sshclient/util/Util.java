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
package com.ericsson.cba.prototype.protocol.sshclient.util;

import static com.ericsson.cba.prototype.protocol.sshclient.model.StaticValues.NETCONF_HELLO_HEADER;

import com.ericsson.cba.prototype.protocol.sshclient.model.query.Query;

public class Util {

    public static final String NULL_EMPTY_PASSWORD = "null/empty password";
    public static final String UTF_8 = "UTF-8";

    /**
     * hides password from the log
     * 
     * @param password
     * @return
     */
    public static String hidePassword(final String password) {

        if ((password == null) || "".equalsIgnoreCase(password.trim())) {
            return NULL_EMPTY_PASSWORD;
        }

        String hiddenPassword = "";
        for (int i = 0; i < password.length(); i++) {
            hiddenPassword += "*";
        }

        return hiddenPassword;
    };

    /**
     * @param query
     * @return
     */
    public static boolean isHelloHeader(final String query) {
        return (query != null) && query.contains(NETCONF_HELLO_HEADER);
    }

    /**
     * @param query
     * @return
     */
    public static boolean isHelloQuery(final Query query) {
        return isHelloHeader(query.getQuery());
    }

}

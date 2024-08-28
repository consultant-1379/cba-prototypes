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

package com.ericsson.oss.mediation.util.netconf.rpc;

/**
 * 
 * @author xvaltda
 */
public class MessageIdManager {

    // This is deliberately static to ensure message Id is unique for this
    // instance of Netconf Manager.
    private static long messageId = 0;

    private MessageIdManager() {
        // Constructor added to silence PMD warning.
    }

    public synchronized static long generateNextMessageID() {
        return ++messageId;
    }

}

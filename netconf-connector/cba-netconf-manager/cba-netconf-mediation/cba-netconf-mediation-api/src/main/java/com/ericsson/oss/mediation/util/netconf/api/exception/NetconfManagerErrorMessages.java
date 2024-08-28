/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ericsson.oss.mediation.util.netconf.api.exception;

/**
 * 
 * @author xvaltda
 */
public class NetconfManagerErrorMessages {
    public static final String SESSIONID_WAS_NOT_RECEIVED = "SessionId not received. The Node doesn't send the session Id, Netconf Manager cannot continue the NetconfSession";
    public static final String SESSIONID_BAD_FORMAT = "Bad format of the SessionId, it should be only digits.";

    public static final String NO_CAPABILITIES_RECIEVED = "No capability received from node";
    public static final String CAPABILITIES_MISSMATCH = "The node's capabilities are not compatible, "
            + "the netconf session cannot continue.";
}

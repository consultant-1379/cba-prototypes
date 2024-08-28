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
package com.ericsson.cba.prototype.protocol.netconf;

import com.ericsson.cba.prototype.protocol.sshclient.handler.HandlerContext;
import com.ericsson.cba.prototype.protocol.sshclient.handler.SshHandler;
import com.ericsson.cba.prototype.protocol.sshclient.model.Exchange;

public class Main {

	public static void main(String[] args) {
		try {
			SshHandler handler = new SshHandler();
			HandlerContext context = new HandlerContext();
			context.setHostName("192.168.100.208");
			context.setUsername("borusgsn");
			context.setPassword("sgsn123");
			context.setPort(22);
			context.setSessionType("subsystem");
			context.setSessionTypeValue("netconf");
			context.setSocketTimeoutValueInMilli(20000);
			handler.init(context);
			Exchange event = new Exchange();
			event.setInput(Queries.getOperation("101"));
			handler.onEvent(event);
			System.err.println(event.getInput());
			handler.destroy();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}

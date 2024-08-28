package com.ericsson.cba.prototype.protocol.netconf;


/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/**
 * This program will demonstrate how to use the Subsystem channel.
 * 
 */

public class Subsystem {

	public static void main(String[] arg) {
		try {

			NetconfHelper netconfHelper = new NetconfHelper();
			netconfHelper.openSession(new SgsnMMECredentialHolder());
			String runQuery = netconfHelper.runQuery(Queries.getConfigQuery(
					"20", "running"));
			System.err.println(runQuery);
			netconfHelper.closeSession();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

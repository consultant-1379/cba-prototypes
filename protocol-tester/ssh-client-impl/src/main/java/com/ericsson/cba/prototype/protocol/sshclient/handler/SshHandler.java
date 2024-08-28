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
package com.ericsson.cba.prototype.protocol.sshclient.handler;

import com.ericsson.cba.prototype.protocol.sshclient.exception.SshTransportException;
import com.ericsson.cba.prototype.protocol.sshclient.model.*;
import com.ericsson.cba.prototype.protocol.sshclient.model.query.Query;
import com.ericsson.cba.prototype.protocol.sshclient.ssh.*;
import com.ericsson.oss.itpf.common.event.handler.EventHandlerContext;
import com.ericsson.oss.itpf.common.event.handler.EventInputHandler;

/**
 * creates connection and session during the initialization time based on the configuration parameters. based on the connection and created session,
 * on event method writes stream to the devices OutputStream
 * 
 */
public class SshHandler implements EventInputHandler {

    private SshHandlerInitializer sshHandlerInitializer = null;
    private SshConnection sshConnection = null;
    private SshSession sshSession = null;
    private SshClient sshClient = null;

    public void destroy() {
        try {
            sshClient.removeConnection(sshConnection);
            sshClient.disconnectSession(sshSession);
        } catch (final Exception e) {
            e.printStackTrace();
            throw new SshClientInputException(e);
        }

    }

    public void init(final HandlerContext context) {
        this.sshHandlerInitializer = new SshHandlerInitializer(context);
        this.sshConnection = sshHandlerInitializer.getSshConnection();
        this.sshSession = sshHandlerInitializer.getSshSession();
        this.sshClient = sshHandlerInitializer.getSshClient();
    }

    public void onEvent(final Object event) {
        if (!(event instanceof Exchange)) {
            throw new SshClientInputException("SshSynchronousQueryHandler expects instance of Exchange");
        }
        final Exchange exchange = (Exchange) event;

        if (!(exchange.getInput() instanceof String)) {
            throw new SshClientInputException("SshSynchronousQueryHandler expects instance of String at Exchange Input");
        }
        final String queryString = (String) exchange.getInput();
        final Query query = new Query(queryString);
        String deviceResponse = null;
        try {
            deviceResponse = sshClient.runQuery(sshSession, query);
        } catch (final SshTransportException e) {
            throw new QueryProcessingException("Exception while processing event in SshSynchronousQueryHandler " + e.getMessage(), e);
        }

        exchange.setOutput(deviceResponse);
    }

	/* (non-Javadoc)
	 * @see com.ericsson.oss.itpf.common.event.handler.EventHandler#init(com.ericsson.oss.itpf.common.event.handler.EventHandlerContext)
	 */
	@Override
	public void init(EventHandlerContext arg0) {
		// TODO Auto-generated method stub
		
	}

}

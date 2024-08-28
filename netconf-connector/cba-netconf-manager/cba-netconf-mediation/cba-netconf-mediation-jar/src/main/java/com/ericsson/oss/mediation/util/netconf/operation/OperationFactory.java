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
package com.ericsson.oss.mediation.util.netconf.operation;

import com.ericsson.oss.mediation.util.netconf.api.Datastore;
import com.ericsson.oss.mediation.util.netconf.api.Filter;
import com.ericsson.oss.mediation.util.netconf.context.NetconfSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author xvaltda
 */
public abstract class OperationFactory {
    private static final Logger logger = LoggerFactory.getLogger(OperationFactory.class);

    public static Operation create(final OperationType requestType, final Object data, final NetconfSession session) {

        Operation operation = null;

        switch (requestType) {

        case HELLO:
            logger.debug("Creating Hello message: ");
            operation = new Hello(session.getNetconfSessionCapabilities().getEcimCapabilities());
            break;
        case CLOSE_SESSION:
            logger.debug("Creating Close operation: ");
            operation = new Close();
            break;
        case KILL_SESSION:
            logger.debug("Creating kil-session operation: ");
            operation = new KillSession(data.toString());
            break;
        case GET_CONFIG:
            logger.debug("Creating Get-Config operation: ");
            final Object[] getConfigParameters = (Object[]) data;
            if (getConfigParameters != null) {
                operation = new GetConfig((Datastore) getConfigParameters[0], (Filter) getConfigParameters[1]);
            } else {
                operation = new GetConfig();
            }
            break;
        case GET:
            logger.debug("Creating Get operation: ");
            operation = new Get(data);
            break;
        }
        return operation;
    }
}

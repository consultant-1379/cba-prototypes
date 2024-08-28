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
package com.ericsson.oss.mediation.cba.handlers.netconf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.itpf.common.event.ComponentEvent;
import com.ericsson.oss.itpf.common.event.handler.EventHandlerContext;
import com.ericsson.oss.itpf.common.event.handler.TypedEventInputHandler;
import com.ericsson.oss.mediation.util.netconf.api.*;
import com.ericsson.oss.mediation.util.netconf.api.exception.NetconfManagerException;

public abstract class AbstractNetconfHandler implements TypedEventInputHandler {

    // no static and no final for test purpose
    // anyway in future it should be injected
    protected Logger logger = LoggerFactory.getLogger(AbstractNetconfHandler.class);

    protected NetconfManager netconfManager;

    @Override
    public void init(final EventHandlerContext ctx) {
        try {
            logger.debug("init called on " + this.getClass().getName());
            logger.debug("The config attributes are: " + ctx.getEventHandlerConfiguration().getAllProperties());
        } catch (final Exception e) {
            manageException(e);
            throw e;
        } finally {
            logger.debug("init finished on " + this.getClass().getName());
        }
    }

    @Override
    public ComponentEvent onEvent(final ComponentEvent inputEvent) {
        try {
            logger.debug("onEvent called on " + this.getClass().getName());
            netconfManager = (NetconfManager) inputEvent.getHeaders().get(NetconManagerConstants.NETCONF_MANAGER_ATTR);
            logger.debug("Retrieved attributes for handler, netconfManager: " + netconfManager);
            if (netconfManager == null) {
                throw new IllegalArgumentException("Input attribute '" + NetconManagerConstants.NETCONF_MANAGER_ATTR
                        + "' was not supplied.");
            }
            return inputEvent;
        } catch (final Exception e) {
            manageException(e);
            throw e;
        } finally {
            logger.debug("onEvent finished on " + this.getClass().getName());
        }
    }

    @Override
    public void destroy() {
        try {
            logger.debug("destroy called on " + this.getClass().getName());
        } catch (final Exception e) {
            manageException(e);
            throw e;
        } finally {
            logger.debug("destroy finished on " + this.getClass().getName());
        }
    }

    private void manageException(final Exception e) {
        logger.error("Error in handler " + this.getClass().getName() + " caused by: " + e.getMessage());
        if ((netconfManager != null) && netconfManager.getStatus().equals(NetconfConnectionStatus.CONNECTED)) {
            try {
                netconfManager.disconnect();
            } catch (final NetconfManagerException nme) {
                logger.warn("Disconnection throws an exception: " + nme.getMessage());
            }
        }
    }

}

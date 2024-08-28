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
package com.ericsson.oss.cba.mediation.flow.handler.hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.itpf.common.config.Configuration;
import com.ericsson.oss.itpf.common.event.handler.EventHandlerContext;
import com.ericsson.oss.itpf.common.event.handler.EventInputHandler;
import com.ericsson.oss.itpf.common.event.handler.annotation.EventHandler;

@EventHandler(contextName = "")
public class HelloHandler implements EventInputHandler {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(HelloHandler.class);

	private String fdn;

	private static final String FDN_ATTR = "fdn";

	@Override
	public void init(EventHandlerContext ctx) {
		LOGGER.debug("init called on HelloHandlerBean.....");
		extractParameters(ctx);
		LOGGER.debug("init finished on HelloHandlerBean.....");
	}

	@Override
	public void onEvent(Object inputEvent) {
		LOGGER.debug("onEvent called on HelloHandlerBean.....");
		LOGGER.info("Hello!!! I am hello handler...Fdn : " + fdn);
		LOGGER.debug("onEvent finished on HelloHandlerBean.....");
	}

	private void extractParameters(final EventHandlerContext context) {
		Configuration eventHandlerConfiguration = context
				.getEventHandlerConfiguration();

		LOGGER.info("The config attributes are: "
				+ eventHandlerConfiguration.getAllProperties());

		extractFdnId(eventHandlerConfiguration);

		LOGGER.info("Retrieved attributes for handler, " + FDN_ATTR + "=" + fdn);
	}

	/**
	 * @param eventHandlerConfiguration
	 */
	private void extractFdnId(Configuration eventHandlerConfiguration) {
		fdn = eventHandlerConfiguration.getStringProperty(FDN_ATTR);
		if (fdn == null) {
			throw new IllegalArgumentException("Config attribute '" + FDN_ATTR
					+ "' was not supplied.");
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

}

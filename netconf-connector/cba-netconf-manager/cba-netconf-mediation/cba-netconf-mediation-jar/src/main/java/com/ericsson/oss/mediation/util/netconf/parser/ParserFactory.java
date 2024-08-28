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
package com.ericsson.oss.mediation.util.netconf.parser;

import com.ericsson.oss.mediation.util.netconf.operation.OperationType;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author xvaltda
 */
public abstract class ParserFactory {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ParserFactory.class);

    public static NetconfParser create(final OperationType requestType) throws ParserException {

        NetconfParser parser = null;

        switch (requestType) {

        case HELLO:
            logger.debug("creating HELLO Parser ");
            parser = new HelloParser();
            break;

        case CLOSE_SESSION:
            logger.debug("creating CLOSE SESSION Parser ");
            parser = new RpcReplyParser();
            break;

        case KILL_SESSION:
            logger.debug("creating KILL_SESSION Parser ");
            parser = new RpcReplyParser();
            break;
        case GET_CONFIG:
            logger.debug("creating GET_CONFIG Parser ");
            parser = new QueryResponseReaderAdapter();
            break;
        case GET:
            logger.debug("creating GET Parser ");
            parser = new QueryResponseReaderAdapter();
            break;
        }
        return parser;
    }
}

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

/**
 * 
 * @author xvaltda
 */
public class ParserException extends Exception {
    public ParserException() {
        super();
    }

    public ParserException(final String msg) {
        super(msg);
    }

    public ParserException(final String msg, final Exception e) {
        super(msg, e);
    }

    public ParserException(final Exception e) {

        super(e);
    }
}

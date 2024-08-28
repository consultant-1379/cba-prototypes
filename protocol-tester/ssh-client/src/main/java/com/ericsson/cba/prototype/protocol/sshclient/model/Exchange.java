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
package com.ericsson.cba.prototype.protocol.sshclient.model;

/**
 * Temprory Exchange object mimics, YMER Mediation Camel Exchange behaviour.
 * 
 */
public class Exchange {

    private Object input;
    private Object output;

    /**
     * @return
     */
    public Object getInput() {
        return input;
    }

    /**
     * @return
     */
    public Object getOutput() {
        return output;
    }

    /**
     * @param input
     */
    public void setInput(final Object input) {
        this.input = input;
    }

    /**
     * @param output
     */
    public void setOutput(final Object output) {
        this.output = output;
    }

}

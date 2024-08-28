/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ericsson.oss.mediation.util.netconf.flow;

/**
 * 
 * @author xvaltda
 */
public enum FlowErrorSeverity {

    FAILURE(1), FATAL(10);

    private int severity = 0;

    private FlowErrorSeverity(final int severity) {
        this.severity = severity;
    }

    public int getValue() {
        return severity;
    }

}

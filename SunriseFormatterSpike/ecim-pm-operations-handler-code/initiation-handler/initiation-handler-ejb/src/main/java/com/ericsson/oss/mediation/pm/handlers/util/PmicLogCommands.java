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
package com.ericsson.oss.mediation.pm.handlers.util;

//EPICROB: TO DO to be used for SystemRecoder
public enum PmicLogCommands {
    
    PMIC_INPUT_EVENT_RECEIVED("PMIC.INPUT_EVENT_RECEIVED"),
    PMIC_PERFORMANCE_EVENT_ACTIVATION_ERROR("PMIC.PERFORMANCE_EVENT_ACTIVATION_ERROR"),
    PMIC_PERFORMANCE_EVENT_DEACTIVATION_ERROR("PMIC.PERFORMANCE_EVENT_DEACTIVATION_ERROR"),
    PMIC_PERFORMANCE_EXCEPTION_EVENT("PMIC.PERFORMANCE_EXCEPTION_EVENT"),
    PMIC_NON_SGSN_EXCEPTION_EVENT("PMIC.NON_SGSN_EXCEPTION_EVENT"),
    PMIC_EXCEPTION_EVENT("PMIC.EXCEPTION_EVENT"),
    PMIC_SUCCESSFUL_OUTPUT_EVENT("PMIC.SUCCESSFUL_OUTPUT_EVENT");
   

    private final String description;

    private PmicLogCommands(final String description) {
        this.description = description;
    }

    public String getDescription() {
         return description;
    }
}

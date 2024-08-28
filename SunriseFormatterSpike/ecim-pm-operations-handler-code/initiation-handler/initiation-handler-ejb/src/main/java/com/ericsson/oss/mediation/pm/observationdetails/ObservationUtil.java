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
package com.ericsson.oss.mediation.pm.observationdetails;

import java.util.List;
import java.util.Map;

import static com.ericsson.oss.mediation.pm.handlers.constants.HandlerConstants.*;

public class ObservationUtil {

    private ObservationUtil() {
    }

    public static ObservationClass[] getObservationClasses(final Map<String, Object> eventAttributes) {
        @SuppressWarnings("unchecked")
        final List<String> counterList = (List<String>) eventAttributes.get(COUNTER_DETAILS);

        ObservationClass[] observationClasses = null;
        if (counterList != null) {
            observationClasses = new ObservationClass[counterList.size()];
            for (int i = 0; i < counterList.size(); i++) {
                final String countersInfo = counterList.get(i);
                final String[] splitMoAndCounters = countersInfo.split(":");
                final String moName = splitMoAndCounters[0];
                final String[] counters = splitMoAndCounters[1].split(",");
                final ObservationClass observationClass = new ObservationClass(moName, counters);
                observationClasses[i] = observationClass;
            }
        }
        return observationClasses;
    }

}

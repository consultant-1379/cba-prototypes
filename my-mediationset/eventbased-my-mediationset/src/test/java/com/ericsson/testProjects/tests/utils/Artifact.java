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

package com.ericsson.testProjects.tests.utils;

import java.io.File;
import java.util.*;

import org.jboss.shrinkwrap.resolver.api.maven.Maven;

/**
 * Utility class
 * 
 * This class contains all coordinates for maven artifacts used in the test Since we will be using pre-built mediation from nexus, we need to maven
 * coordinates
 * 
 * @author edejket
 * 
 */
public class Artifact {
    /**
     * SLF4J coordinates
     */
    public static final String ORG_SLF4J___SLF4J_API_JAR = "org.slf4j:slf4j-api:jar";
    /**
     * Service framework coordinates
     */
    public static final String COM_ERICSSON_OSS_ITPF_SDK___SERVICES_CORE_JAR = "com.ericsson.oss.itpf.sdk:sdk-services-core:jar";
    public static final String COM_ERICSSON_OSS_ITPF_SDK___CONFIG_API_JAR = "com.ericsson.oss.itpf.sdk:sdk-config-api:jar";
    public static final String COM_ERICSSON_OSS_ITPF_SDK___CONFIG_CORE_JAR = "com.ericsson.oss.itpf.sdk:sdk-config-core:jar";
    public static final String COM_ERICSSON_OSS_ITPF_SDK___CONFIG_CACHE_NON_CDI_JAR = "com.ericsson.oss.itpf.sdk:sdk-config-cache-non-cdi:jar";
    public static final String COM_ERICSSON_OSS_ITPF_SDK___SFWK_DIST = "com.ericsson.oss.itpf.sdk:service-framework-dist";
    public static final String COM_ERICSSON_OSS_ITPF_SDK___SDK_MODELED_EVENTBUS_API = "com.ericsson.oss.itpf.sdk:sdk-modeled-eventbus-api";
    public static final String COM_ERICSSON_OSS_ITPF_SDK___SDK_MODELED_EVENTBUS = "com.ericsson.oss.itpf.sdk:sdk-modeled-eventbus";

    /**
     * Core mediation events
     */
    public static final String COM_ERICSSON_OSS_MEDIATION___MEDIATION_SDK_CORE_EVENTS = "com.ericsson.oss.mediation:mediation-sdk-event-models-jar:jar:?";
    public static final String COM_ERICSSON_OSS_MEDIATION___CORE_MEDIATION_MODELS_API = "com.ericsson.nms.mediation:core-mediation-models-api:jar:?";

    /**
     * DPS Utility
     * 
     */
    public static final String COM_ERICSSON_OSS_MEDIATION_TRAINING___DPS_UTILITY = "com.ericsson.testProjects:dps-utility-ear:ear:?";
    public static final String COM_ERICSSON_OSS_MEDIATION_TRAINING___DPS_UTILITY_API = "com.ericsson.testProjects:dps-utility-api:jar:?";

    /**
     * Camel Engine coordinates
     * 
     */
    public static final String COM_ERICSSON_OSS_MEDIATION__CAMEL_ENGINE = "org.jboss.as.camel:camel-engine-ear:ear:?";

    /**
     * Mediation Service coordinates
     */

    public static final String COM_ERICSSON_OSS_MEDIATION__MEDIATION_SERVICE_EAR = "com.ericsson.nms.mediation:mediation-service-ear:ear:?";

    /**
     * Mediation Router coordinates
     */
    public static final String COM_ERICSSON_OSS_MEDIATION__MEDIATION_ROUTER_EAR = "com.ericsson.oss.mediation:mediation-router-ear:ear:?";

    /**
     * Mediation Router coordinates
     */
    public static final String COM_ERICSSON_OSS_MEDIATION__EVENT_CLIENT_EAR = "com.ericsson.oss.mediation:event-based-client-ear:ear:?";

    /**
     * Mediation Router coordinates
     */
    public static final String COM_ERICSSON_OSS_MEDIATION__DPS_CLIENT_EAR = "com.ericsson.oss.mediation:dps-based-client-ear:ear:?";

    /**
     * Hello World handler deployment
     */
    public static final String COM_ERICSSON_OSS_MEDIATION___HELLO_WORLD_HANDLER = "com.ericsson.testProjects:helloworld-handler-ear:ear:?";
    
    
    public static final String COM_ERICSSON_OSS_MEDIATION_TRAINING___SHARED ="com.ericsson.testProjects:helloworld-tasks:jar:?"; 

    /* ------------------------Utility methods ----------------------------- */

    /**
     * Resolve artifact with given coordinates without any dependencies, this method should be used to resolve just the artifact with given name, and
     * it can be used for adding artifacts as modules into EAR
     *
     * If artifact can not be resolved, or the artifact was resolved into more then one file then the IllegalStateException will be thrown
     *
     *
     * @param artifactCoordinates
     *            in usual maven format
     *
     *            <pre>
     * {@code<groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>}
     * </pre>
     * @return File representing resolved artifact
     */
    public static File resolveArtifactWithoutDependencies(final String artifactCoordinates) {
        final File artifact = Maven.resolver().loadPomFromFile("pom.xml").resolve(artifactCoordinates).withoutTransitivity().asSingleFile();
        if (artifact == null) {
            throw new IllegalStateException("Artifact with coordinates " + artifactCoordinates + " was not resolved");
        }
        return artifact;
    }

    /**
     * Resolve dependencies for artifact with given coordinates, if artifact can not be resolved IllegalState exception will be thrown
     *
     * @param artifactCoordinates
     *            in usual maven format
     *
     *            <pre>
     * {@code<groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>}
     * </pre>
     *
     * @return resolved dependencies
     */
    public static File[] resolveArtifactDependencies(final String artifactCoordinates) {
        File[] artifacts = Maven.resolver().loadPomFromFile("pom.xml").resolve(artifactCoordinates).withTransitivity().asFile();
        if (artifacts == null) {
            throw new IllegalStateException("No dependencies resolved for artifact with coordinates " + artifactCoordinates);
        }

        if (artifacts.length > 0) {
            // find artifact that has given coordinates
            final File artifact = resolveArtifactWithoutDependencies(artifactCoordinates);
            final List<File> filteredDeps = new ArrayList<File>(Arrays.asList(artifacts));
            // remove it from the list
            filteredDeps.remove(artifact);
            artifacts = new File[0];
            // return the resolved list with only dependencies
            return filteredDeps.toArray(artifacts);
        } else {
            return artifacts;
        }

    }

}

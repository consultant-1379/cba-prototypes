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
package com.ericsson.oss.models.moci.mediation.handlers.test.deployment;

import java.io.File;

import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

/**
 * @author eobrste
 * 
 */
public class Artifact {

    /* Moci Handler Artifacts */
    public static final String CBA_WRITER_HANDLER_JAR = "com.ericsson.nms.mediation.component:handler-code-jar:jar";

    public static final String CBA_WRITER_HANDLER_EAR = "com.ericsson.nms.mediation.component:handler-code-ear:ear";

    /* Platform Integration Bridge Artifacts */
    public static final String COM_ERICSSON_OSS_ITPF_PIB = "com.ericsson.oss.itpf.common:PlatformIntegrationBridge-ear:ear";

    public static final String COM_ERICSSON_OSS_ITPF_SDK_DIST_JAR = "com.ericsson.oss.itpf.sdk:service-framework-dist:jar";

    public static final String NETWORK_ELEMENT_CONNECTOR_EAR = "com.ericsson.oss.mediation:network-element-connector-receiver-ear:ear";

    /* Camel Ear */
    public static final String CAMEL_EAR = "org.jboss.as.camel:camel-engine-ear:ear";

    /* DPS Remote Api Jar */
    public static final String DPS_REMOTE_API_JAR = "com.ericsson.oss.itpf.datalayer.dps:dps-remote-api:jar";

    /* Mockito jar */
    public static final String MOCKITO_ALL = "org.mockito:mockito-all";

    /**
     * Maven resolver that will try to resolve dependencies using pom.xml of the project where this class is located.
     * 
     * @return MavenDependencyResolver
     */
    public static MavenDependencyResolver getMavenResolver() {
        return DependencyResolvers.use(MavenDependencyResolver.class).loadMetadataFromPom("pom.xml");

    }

    /**
     * Resolve artifacts without dependencies
     * 
     * @param artifactCoordinates
     * @return
     */
    public static File resolveArtifactWithoutDependencies(final String artifactCoordinates) {
        final File[] artifacts = getMavenResolver().artifact(artifactCoordinates).exclusion("*").resolveAsFiles();
        if (artifacts == null) {
            throw new IllegalStateException("Artifact with coordinates " + artifactCoordinates + " was not resolved");
        }
        if (artifacts.length != 1) {
            throw new IllegalStateException("Resolved more then one artifact with coordinates " + artifactCoordinates);
        }
        return artifacts[0];
    }

    public static File[] resolveArtifactWithDependencies(final String artifactCoordinates) {
        final File[] artifacts = getMavenResolver().artifact(artifactCoordinates).resolveAsFiles();

        if (artifacts == null) {
            throw new IllegalStateException("Artifact with coordinates " + artifactCoordinates + " was not resolved");
        }

        return artifacts;
    }

}
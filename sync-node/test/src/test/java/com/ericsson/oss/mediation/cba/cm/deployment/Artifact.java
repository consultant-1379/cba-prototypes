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

package com.ericsson.oss.mediation.cba.cm.deployment;

import java.io.File;

import org.jboss.shrinkwrap.resolver.api.Resolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;

public class Artifact {

    public static final String COM_ERICSSON_SFWK_DIST = "com.ericsson.oss.itpf.sdk:service-framework-dist:?";

    public static final String COM_ERICSSON_OSS_MEDIATION_MEDIATIONCORE_EAR = "com.ericsson.nms.mediation:mediation-core-ear:ear:?";
    public static final String COM_ERICSSON_NMS_MEDIATION_MEDIATIONSERVICE_EAR = "com.ericsson.nms.mediation:mediation-service-ear:ear:?";
    public static final String ORG_JBOSS_AS_CAMEL_EAR = "org.jboss.as.camel:camel-engine-ear:ear:?";

    public static final String COM_ERICSSON_OSS_ITPF_PIB = "com.ericsson.oss.itpf.common:PlatformIntegrationBridge-ear:ear:?";

    public static final String EAI_HANDLER_EAR = "com.ericsson.nms.mediation.component:eai-creation-handler-code-ear:ear:?";
    public static final String CI_ASSOCIATION_HANDLER_EAR = "com.ericsson.nms.mediation.component:ci-association-handler-code-ear:ear:?";
    public static final String NETCONF_HANDLER_EAR = "com.ericsson.oss.mediation.cba.cm.handlers.netconf:cba-cm-netconf-handler-ear:ear:?";
    public static final String NETCONF_CONNECT_HANDLER_EAR = "com.ericsson.oss.mediation.cba.handlers.netconf:cba-netconf-connect-handler-ear:ear:?";
    public static final String NETCONF_DISCONNECT_HANDLER_EAR = "com.ericsson.oss.mediation.cba.handlers.netconf:cba-netconf-disconnect-handler-ear:ear:?";
    public static final String OSS_PREFIX_HANDLER_EAR = "com.ericsson.oss.mediation.shared.generichandlers:oss-prefix-handler-code-ear:ear:?";
    public static final String INBOUND_DPS_HANDLER_EAR = "com.ericsson.nms.mediation.component:inbound-dps-handler-code-ear:ear:?";
    public static final String PROTOTYPE_HANDLER_EAR = "com.ericsson.oss.mediation.cba:sync-node-handler-ear:ear:?";

    public static PomEquippedResolveStage getMavenResolver() {
        return Resolvers.use(MavenResolverSystem.class).loadPomFromFile("pom.xml");
    }

    public static File resolveArtifactWithoutDependencies(final String artifactCoordinates) {
        final File artifact = getMavenResolver().resolve(artifactCoordinates).withoutTransitivity().asSingleFile();
        if (artifact == null) {
            throw new IllegalStateException("Artifact with coordinates " + artifactCoordinates + " was not resolved");
        }
        return artifact;
    }

}

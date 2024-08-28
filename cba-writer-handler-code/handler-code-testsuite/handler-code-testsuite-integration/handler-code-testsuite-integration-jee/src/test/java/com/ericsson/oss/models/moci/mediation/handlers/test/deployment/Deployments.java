/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.models.moci.mediation.handlers.test.deployment;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class used to create ears for tests. It will use ear created by maven either from nexus or local maven repository
 */
public class Deployments {

    private static final Logger LOGGER = LoggerFactory.getLogger(Deployments.class);

    public static EnterpriseArchive createEnterpriseArchiveDeployment(final String artifactName) {
        LOGGER.info("Creating EAR deployment from artifact: {} ", artifactName);
        final EnterpriseArchive ear = ShrinkWrap
                .createFromZipFile(EnterpriseArchive.class, Artifact.resolveArtifactWithoutDependencies(artifactName));
        return ear;
    }
    
    public static JavaArchive createJavaArchive(final String artifactName) {
        LOGGER.info("Creating JAR from artifact: {} ", artifactName);
        final JavaArchive jar = ShrinkWrap
                .createFromZipFile(JavaArchive.class, Artifact.resolveArtifactWithoutDependencies(artifactName));
        return jar;
    }
    
    
}

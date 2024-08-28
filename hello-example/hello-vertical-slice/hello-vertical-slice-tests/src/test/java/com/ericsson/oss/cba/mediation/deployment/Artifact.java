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

package com.ericsson.oss.cba.mediation.deployment;

import java.io.*;
import java.util.*;

import org.jboss.arquillian.protocol.servlet.arq514hack.descriptors.api.application.ApplicationDescriptor;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

/**
 * This class contains all dependencies used in this test listed as constants
 * for easier use. Dependencies should be added in sections, for service
 * framework, this project artifacts etc... When adding dependencies here, make
 * sure same are added into pom file as well. It is forbidden to put versions of
 * dependencies here all versions need to be defined in pom files, this list
 * here should never contain dependency with version.
 *
 * In order to be able to maintain this file easily, please add only required
 * dependencies, and make sure they are used. If any dependency is not used any
 * more, make sure it is removed from this file and pom file.
 *
 * @author eleejhn
 * @author edejket
 *
 */
public class Artifact {

    /**
     * MediationCore artifacts needed for this test
     */
    public static final String COM_ERICSSON_OSS_MEDIATION_MEDIATIONCORE_EJB = "com.ericsson.nms.mediation:mediation-core-ejb";
    public static final String COM_ERICSSON_OSS_MEDIATION_MEDIATIONCORE_EAR = "com.ericsson.nms.mediation:mediation-core-ear:ear";
    public static final String COM_ERICSSON_NMS_MEDIATION_SDK_CORE_MEDIATION_ENGINE_API = "com.ericsson.nms.mediation:core-mediation-engine-api:jar";

    /**
     * MediationCore artifacts needed for sending the registration event
     */
    public static final String COM_ERICSSON_NMS_MEDIATION_MEDIATIONCORE_API_JAR = "com.ericsson.nms.mediation:mediation-core-api:jar";
    public static final String COM_ERICSSON_OSS_MEDIATION_MEDIATION_CORESERVICELOCATOR_API_JAR = "com.ericsson.oss.mediation:mediation-core-service-locator-api:jar";
    public static final String COM_ERICSSON_NMS_MEDIATION_SDK_CORE_MODELS_API_JAR = "com.ericsson.nms.mediation:core-mediation-models-api:jar";


    /**
     * Service Framework artifacts and needed for sending the registration event
     */
    public static final String COM_ERICSSON_OSS_ITPF_SDK_MODELLEDEVENTBUS_API_JAR = "com.ericsson.oss.itpf.sdk:sdk-modeled-eventbus-api:jar";
    public static final String COM_ERICSSON_OSS_ITPF_SDK_MODELLEDEVENTBUS_JMS_JAR = "com.ericsson.oss.itpf.sdk:sdk-modeled-eventbus:jar";
    public static final String COM_ERICSSON_OSS_ITPF_SDK_CONFIG_API_JAR = "com.ericsson.oss.itpf.sdk:sdk-config-api:jar";
    public static final String COM_ERICSSON_OSS_ITPF_SDK_CONFIG_API_JAR_CACHE = "com.ericsson.oss.itpf.sdk:sdk-cache-infinispan:jar";
    public static final String COM_ERICSSON_OSS_ITPF_SDK_CONFIG_CACHE = "com.ericsson.oss.itpf.sdk:sdk-config-cache:jar";
    public static final String COM_ERICSSON_OSS_ITPF_SDK_CLUSTER_CORE = "com.ericsson.oss.itpf.sdk:sdk-cluster-core:jar";


    /**
     * Mediation Service
     */
    public static final String COM_ERICSSON_NMS_MEDIATION_MEDIATIONSERVICE_EJB = "com.ericsson.nms.mediation:mediation-service-engine:jar";
    public static final String COM_ERICSSON_NMS_MEDIATION_MEDIATIONSERVICE_EAR = "com.ericsson.nms.mediation:mediation-service-ear:ear";

    /**
     * Data Persistence service
     */
    public static final String COM_ERICSSON_ITPF_DATALAYER_DPS_API = "com.ericsson.oss.itpf.datalayer.dps:dps-api:jar";
    public static final String COM_ERICSSON_ITPF_DATALAYER_DATA_ACCESS_DELEGATE_API = "com.ericsson.oss.itpf.datalayer.dps:data-access-delegate-api:jar";
    public static final String COM_ERICSSON_MODELLING_COMMON_API = "com.ericsson.oss.itpf.datalayer.modeling:modeling-common-jar:jar";


    public static final String COM_ERICSSON_NMS_MEDIATION_CAMEL_ENGINE = "com.ericsson.nms.mediation:camel-engine-jca-flow-rar:rar";
    public static final String COM_ERICSSON_ITPF_DATALAYER_DPS_REMOTE_API = "com.ericsson.oss.itpf.datalayer.dps:dps-remote-api:jar";

    /**
     * Camel Engine
     */
    public static final String ORG_JBOSS_AS_CAMEL_EAR = "org.jboss.as.camel:camel-engine-ear:ear";

    
    /* ------------------------Utility methods ----------------------------- */

    /**
     * Resolve artifact with given coordinates without any dependencies, this
     * method should be used to resolve just the artifact with given name, and
     * it can be used for adding artifacts as modules into EAR
     *
     * If artifact can not be resolved, or the artifact was resolved into more
     * then one file then the IllegalStateException will be thrown
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
    public static File resolveArtifactWithoutDependencies(
            final String artifactCoordinates) {
        final File[] artifacts = getMavenResolver()
                .artifact(artifactCoordinates).exclusion("*").resolveAsFiles();
        if (artifacts == null) {
            throw new IllegalStateException("Artifact with coordinates "
                    + artifactCoordinates + " was not resolved");
        }

        if (artifacts.length != 1) {
            throw new IllegalStateException(
                    "Resolved more then one artifact with coordinates "
                            + artifactCoordinates);
        }
        return artifacts[0];
    }

    /**
     * Resolve dependencies for artifact with given coordinates, if artifact can
     * not be resolved IllegalState exception will be thrown
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
    public static File[] resolveArtifactDependencies(
            final String artifactCoordinates) {
        File[] artifacts = getMavenResolver().artifact(artifactCoordinates)
                .scope("compile").exclusion(artifactCoordinates)
                .resolveAsFiles();

        if (artifacts == null) {
            throw new IllegalStateException(
                    "No dependencies resolved for artifact with coordinates "
                            + artifactCoordinates);
        }

        if (artifacts.length > 0) {
            // find artifact that has given coordinates
            final File artifact = resolveArtifactWithoutDependencies(artifactCoordinates);
            final List<File> filteredDeps = new ArrayList<File>(
                    Arrays.asList(artifacts));
            // remove it from the list
            filteredDeps.remove(artifact);
            artifacts = new File[0];
            // return the resolved list with only dependencies
            return filteredDeps.toArray(artifacts);
        } else {
            return artifacts;
        }

    }

    /**
     * Utility method giving access to maven dependency resolver, allowing
     * dependency resolving with transitive dependency taken into account
     *
     * @return MavenDependencyResolver built on local pom file
     */
    public static MavenDependencyResolver getMavenResolver() {
        return DependencyResolvers.use(MavenDependencyResolver.class)
                .loadMetadataFromPom("pom.xml").goOffline();
    }

    public static void createCustomApplicationXmlFile(
            final EnterpriseArchive serviceEar, final String webModuleName) {

        final Node node = serviceEar.get("META-INF/application.xml");
        ApplicationDescriptor desc = Descriptors.importAs(
                ApplicationDescriptor.class).fromStream(
                node.getAsset().openStream());

        desc.webModule(webModuleName + ".war", webModuleName);
        final String descriptorAsString = desc.exportAsString();

        serviceEar.delete(node.getPath());
        desc = Descriptors.importAs(ApplicationDescriptor.class).fromString(
                descriptorAsString);

        final Asset asset = new Asset() {
            @Override
            public InputStream openStream() {
                final ByteArrayInputStream bi = new ByteArrayInputStream(
                        descriptorAsString.getBytes());
                return bi;
            }
        };
        serviceEar.addAsManifestResource(asset, "application.xml");
    }
}

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
package com.ericsson.oss.mediation.cba.cm.deployment;

import java.io.File;

import org.jboss.arquillian.container.test.api.Testable;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class Deployments {

    public static EnterpriseArchive createSimpleEnterpriseArchive(final String artifactName) {
        final File archiveFile = Artifact.resolveArtifactWithoutDependencies(artifactName);
        if (archiveFile == null) {
            throw new IllegalStateException("Unable to resolve artifact " + artifactName);
        }
        return ShrinkWrap.createFromZipFile(EnterpriseArchive.class, archiveFile);
    }

    public static EnterpriseArchive createCustomEar(final String name, final Class[] ejbClasses,
            final Class[] jarClasses, final String[] artifacts) {
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, name + ".ear");
        final WebArchive testableWar = ShrinkWrap.create(WebArchive.class, name + ".war");
        final JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "jar.jar").addAsManifestResource(
                EmptyAsset.INSTANCE, "beans.xml");
        jar.addClasses(jarClasses);
        testableWar.addAsLibraries(jar);
        ear.addAsModule(Testable.archiveToTest(testableWar));
        JavaArchive ejbJar = ShrinkWrap.create(JavaArchive.class, "ejb.jar").addAsManifestResource(EmptyAsset.INSTANCE,
                "beans.xml");
        ejbJar.addClasses(ejbClasses);
        ear.addAsModule(ejbJar);
        for (final String artifact : artifacts) {
            ear.addAsLibraries(Artifact.resolveArtifactWithoutDependencies(artifact));
        }
        return ear;
    }
}

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jboss.arquillian.container.test.api.Testable;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public abstract class IntegrationEnterpriseArchive {

    private final List<Class<?>> testClasses = new ArrayList<Class<?>>();

    private final List<Class<?>> ejbClasses = new ArrayList<Class<?>>();

    private final List<String> earLibraries = new ArrayList<String>();

    private final List<Class<?>> earLibClasses = new ArrayList<Class<?>>();

    private final List<String> earDependencies = new ArrayList<String>();

    private final Map<String, String> ejbResources = new HashMap<String, String>();

    public IntegrationEnterpriseArchive() {
        super();
        addTestClass(IntegrationEnterpriseArchive.class);
    }

    public EnterpriseArchive getEnterpriseArchive() {
        final WebArchive testWar = ShrinkWrap.create(WebArchive.class, this.getWarName());
        final JavaArchive testLibrary = createTestLibrary();
        testWar.addAsLibrary(testLibrary);
        testWar.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        final EnterpriseArchive ear = getBaseEnterpriseArchive();
        ear.delete("META-INF/application.xml");
        final JavaArchive ejbJar = createEJBJar();
        ear.delete(ejbJar.getName());
        ear.addAsModule(ejbJar);
        ear.addAsModule(Testable.archiveToTest(testWar));
        addEarLibraries(ear);
        addEarDependencies(ear);
        return ear;

    }

    private void addEarDependencies(final EnterpriseArchive ear) {
        final Iterator<String> iterator = earDependencies.iterator();
        while (iterator.hasNext()) {
            ear.addAsLibraries(Artifact.getMavenResolver().artifact(iterator.next()).resolveAsFiles());
        }
    }

    private JavaArchive createEJBJar() {
        final JavaArchive ejbArchive = getBaseEjbArchive();
        for (final Class<?> element : ejbClasses) {
            ejbArchive.addClass(element);
        }
        final Iterator<String> keys = ejbResources.keySet().iterator();
        while (keys.hasNext()) {
            final String key = keys.next();
            ejbArchive.addAsResource(key, ejbResources.get(key));
        }
        return ejbArchive;
    }

    private JavaArchive createTestLibrary() {
        final JavaArchive library = ShrinkWrap.create(JavaArchive.class).addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        for (final Class<?> element : testClasses) {
            library.addClass(element);
        }
        return library;
    }

    private void addEarLibraries(final EnterpriseArchive ear) {
        final Iterator<String> iterator = earLibraries.iterator();
        while (iterator.hasNext()) {
            ear.addAsLibrary(Artifact.resolveArtifactWithoutDependencies(iterator.next()));
        }
        final Iterator<Class<?>> libClassIterator = earLibClasses.iterator();
        final JavaArchive libJar = ShrinkWrap.create(JavaArchive.class);
        while (libClassIterator.hasNext()) {
            libJar.addClass(libClassIterator.next());
        }
        ear.addAsLibrary(libJar);
    }

    protected abstract String getWarName();

    public abstract EnterpriseArchive getBaseEnterpriseArchive();

    protected abstract JavaArchive getBaseEjbArchive();

    public IntegrationEnterpriseArchive addTestClass(final Class<?> clazz) {
        testClasses.add(clazz);
        return this;
    }

    public IntegrationEnterpriseArchive addClass(final Class<?> clazz) {
        ejbClasses.add(clazz);
        return this;
    }

    public IntegrationEnterpriseArchive addEarLibrary(final String artifact) {
        earLibraries.add(artifact);
        return this;
    }

    public IntegrationEnterpriseArchive addEarDependency(final String artifact) {
        earDependencies.add(artifact);
        return this;
    }

    public void addEJBResource(final String source, final String target) {
        ejbResources.put(source, target);
    }

    public void addClassToEarLibrary(final Class libClass) {
        earLibClasses.add(libClass);
    }

}
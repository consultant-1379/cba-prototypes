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

/**
 * @author ejuaolm
 * 
 */
public abstract class IntegrationEnterpriseArchive {

	private List<Class<?>> testClasses = new ArrayList<Class<?>>();

	private List<Class<?>> ejbClasses = new ArrayList<Class<?>>();

	private List<String> earLibraries = new ArrayList<String>();
	
	private List<String> earDependencies = new ArrayList<String>();
	
	private Map<String,String> ejbResources = new HashMap<String,String>();

	/**
	 * 
	 */
	public IntegrationEnterpriseArchive() {
		super();
		addTestClass(IntegrationEnterpriseArchive.class);
	}

	public EnterpriseArchive getEnterpriseArchive() {
		WebArchive testWar = ShrinkWrap.create(WebArchive.class,
				this.getWarName());
		JavaArchive testLibrary = createTestLibrary();
		testWar.addAsLibrary(testLibrary);

		final EnterpriseArchive ear = getBaseEnterpriseArchive();
		ear.delete("META-INF/application.xml");
		JavaArchive ejbJar = createEJBJar();
		ear.delete(ejbJar.getName());
		ear.addAsModule(ejbJar);
		ear.addAsModule(Testable.archiveToTest(testWar));
		addEarLibraries(ear);
		addEarDependencies(ear);
		return ear;

	}


        private void addEarDependencies(EnterpriseArchive ear) {
            Iterator<String> iterator = earDependencies.iterator();
            while (iterator.hasNext()) {
                ear.addAsLibraries(Artifact.getMavenResolver().artifact(iterator.next()).resolveAsFiles());
            }
        }

        private JavaArchive createEJBJar() {
		JavaArchive ejbArchive = getBaseEjbArchive();
		for (Class<?> element : ejbClasses) {
			ejbArchive.addClass(element);
		}
		Iterator<String> keys = ejbResources.keySet().iterator();
		while (keys.hasNext()) {
		    String key = keys.next();
		    ejbArchive.addAsResource(key, ejbResources.get(key));
		}
		return ejbArchive;
	}

	private JavaArchive createTestLibrary() {
		JavaArchive library = ShrinkWrap.create(JavaArchive.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		for (Class<?> element : testClasses) {
			library.addClass(element);
		}
		return library;
	}

	private void addEarLibraries(EnterpriseArchive ear) {
	    Iterator<String> iterator = earLibraries.iterator();
            while (iterator.hasNext()) {
                    ear.addAsLibrary(Artifact
                                    .resolveArtifactWithoutDependencies(iterator.next()));
            }
	}

	protected abstract String getWarName();

	public abstract EnterpriseArchive getBaseEnterpriseArchive();

	protected abstract JavaArchive getBaseEjbArchive();

	public IntegrationEnterpriseArchive addTestClass(Class<?> clazz) {
		testClasses.add(clazz);
		return this;
	}

	public IntegrationEnterpriseArchive addClass(Class<?> clazz) {
		ejbClasses.add(clazz);
		return this;
	}

	public IntegrationEnterpriseArchive addEarLibrary(String artifact) {
		earLibraries.add(artifact);
		return this;
	}
	
	public IntegrationEnterpriseArchive addEarDependency(String artifact) {
            earDependencies.add(artifact);
            return this;
        }
	
	public void addEJBResource(String source, String target) {
	    ejbResources.put(source, target);
	}

}
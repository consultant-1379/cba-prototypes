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

import java.io.File;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

/**
 * @author ejuaolm
 *
 */
public abstract class MavenEnterpriseArchive extends
		IntegrationEnterpriseArchive {

	/**
	 * 
	 */
	public MavenEnterpriseArchive() {
		super();
		addTestClass(MavenEnterpriseArchive.class);
	}

	public EnterpriseArchive getBaseEnterpriseArchive() {
		final File archiveFile = Artifact
				.resolveArtifactWithoutDependencies(this.getEARArtifact());
		if (archiveFile == null) {
			throw new IllegalStateException("Unable to resolve artifact "
					+ this.getEARArtifact());
		}
		
		return ShrinkWrap
				.createFromZipFile(EnterpriseArchive.class, archiveFile);
	}
	
	protected JavaArchive getBaseEjbArchive() {
		final File ejbArchiveFile = Artifact
				.resolveArtifactWithoutDependencies(this.getEjbArtifact());
		if (ejbArchiveFile == null) {
			throw new IllegalStateException(
					"Unable to resolve artifact "
							+ this.getEjbArtifact());
		}
		JavaArchive ejbArchive = ShrinkWrap.createFromZipFile(
				JavaArchive.class, ejbArchiveFile);
		return ejbArchive;
	}
	

	protected abstract String getEARArtifact() ;

	protected abstract String getEjbArtifact();
	
}
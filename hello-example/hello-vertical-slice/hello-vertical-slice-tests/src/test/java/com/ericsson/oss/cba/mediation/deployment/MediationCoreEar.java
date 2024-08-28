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




/**
 * Generic Mediation EAR provider for different tests, so that each one can
 * add mocked objects, such as EJB's or classes, representing other services
 * or it also can be used to replace some of the components in Mediation Core
 * for mocks for specific tests
 * @author ejuaolm
 *
 */
public class MediationCoreEar extends MavenEnterpriseArchive {

	/**
	 * 
	 */
	public MediationCoreEar() {
		super();
		addTestClass(MediationCoreEar.class);
	}

	private static final String WAR_NAME = "MediationCore.war";
	
	/* (non-Javadoc)
	 * @see com.ericsson.oss.mediation.deployment.MediationEnterpriseArchive#getEJBArtifactName()
	 */
	@Override
	protected String getEjbArtifact() {
		return Artifact.COM_ERICSSON_OSS_MEDIATION_MEDIATIONCORE_EJB;
	}

	/* (non-Javadoc)
	 * @see com.ericsson.oss.mediation.deployment.MediationEnterpriseArchive#getEARArtifactName()
	 */
	@Override
	protected String getEARArtifact() {
		return Artifact.COM_ERICSSON_OSS_MEDIATION_MEDIATIONCORE_EAR;
	}

	/* (non-Javadoc)
	 * @see com.ericsson.oss.mediation.deployment.IntegrationEnterpriseArchive#getWarName()
	 */
	@Override
	protected String getWarName() {
		return WAR_NAME;
	}	
}

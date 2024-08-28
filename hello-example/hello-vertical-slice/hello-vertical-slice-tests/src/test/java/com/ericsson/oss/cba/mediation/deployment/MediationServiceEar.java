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


public class MediationServiceEar extends MavenEnterpriseArchive {

	/**
	 * 
	 */
	public MediationServiceEar() {
		super();
		addTestClass(MediationServiceEar.class);
	}

	private static final String WAR_NAME = "MediationService.war";

	/* (non-Javadoc)
	 * @see com.ericsson.oss.mediation.deployment.IntegrationEnterpriseArchive#getEjbArtifact()
	 */
	@Override
	protected String getEjbArtifact() {
		return Artifact.COM_ERICSSON_NMS_MEDIATION_MEDIATIONSERVICE_EJB;
	}

	/* (non-Javadoc)
	 * @see com.ericsson.oss.mediation.deployment.IntegrationEnterpriseArchive#getEARArtifact()
	 */
	@Override
	protected String getEARArtifact() {
		return Artifact.COM_ERICSSON_NMS_MEDIATION_MEDIATIONSERVICE_EAR;
	}

	/* (non-Javadoc)
	 * @see com.ericsson.oss.mediation.deployment.IntegrationEnterpriseArchive#getWarName()
	 */
	@Override
	protected String getWarName() {
		return WAR_NAME;
	}
}

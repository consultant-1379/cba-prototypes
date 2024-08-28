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
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public class MocksEar extends IntegrationEnterpriseArchive {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ericsson.oss.mediation.deployment.IntegrationEnterpriseArchive#getWarName
	 * ()
	 */
	@Override
	protected String getWarName() {
		return "Mocks.war";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ericsson.oss.mediation.deployment.IntegrationEnterpriseArchive#
	 * getBaseEnterpriseArchive()
	 */
	@Override
	public EnterpriseArchive getBaseEnterpriseArchive() {
		return ShrinkWrap.create(EnterpriseArchive.class, "Mocks.ear");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ericsson.oss.mediation.deployment.IntegrationEnterpriseArchive#
	 * getBaseEjbArchive()
	 */
	@Override
	protected JavaArchive getBaseEjbArchive() {
	    JavaArchive ejbJar = ShrinkWrap.create(JavaArchive.class, "ejb.jar").addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	    ejbJar.addAsResource(new File("src/test/resources/ServiceFrameworkConfiguration.properties"));
	    return ejbJar;
	}
	
}

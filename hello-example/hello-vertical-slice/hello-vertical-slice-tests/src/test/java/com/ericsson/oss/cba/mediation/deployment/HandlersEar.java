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

public class HandlersEar {
	private static final String HELLO_HANDLER_EAR = "com.ericsson.oss.cba.mediation.flow.handler.hello:hello-handler-ear:ear:1.0.0-SNAPSHOT";

	private File getHelloHandlerEarFile() {
		File ciEar = Artifact
				.resolveArtifactWithoutDependencies(HELLO_HANDLER_EAR);
		return ciEar;
	}

	/**
	 * Returns the HelloHandler*.ear as an EnterpriseArchive
	 * 
	 * @return EAR file of HelloHandler
	 */
	public EnterpriseArchive getHelloEnterpriseArchive() {
		return ShrinkWrap.createFromZipFile(EnterpriseArchive.class,
				getHelloHandlerEarFile());
	}

}

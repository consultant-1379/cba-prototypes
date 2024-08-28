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

public class CamelEngineEar {

    /**
     * 
     * @return Camel Engine Ear
     */
    public EnterpriseArchive createCamelEar() {
        File archive = Artifact.resolveArtifactWithoutDependencies(Artifact.ORG_JBOSS_AS_CAMEL_EAR);
        if (archive == null) {
            throw new IllegalStateException("Unable to resolve artifact " + Artifact.ORG_JBOSS_AS_CAMEL_EAR);
        }
        return ShrinkWrap.createFromZipFile(EnterpriseArchive.class, archive);
    }
}

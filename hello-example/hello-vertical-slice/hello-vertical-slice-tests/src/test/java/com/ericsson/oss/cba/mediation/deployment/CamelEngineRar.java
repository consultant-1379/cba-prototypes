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
import org.jboss.shrinkwrap.api.spec.ResourceAdapterArchive;


public class CamelEngineRar {

    
    public ResourceAdapterArchive getCamelEngineRar(){
        File archiveFile = Artifact.resolveArtifactWithoutDependencies(Artifact.COM_ERICSSON_NMS_MEDIATION_CAMEL_ENGINE);
        if (archiveFile == null) {
            throw new IllegalStateException("Unable to resolve artifact " + Artifact.COM_ERICSSON_NMS_MEDIATION_CAMEL_ENGINE);
        }
        return ShrinkWrap.createFromZipFile(ResourceAdapterArchive.class, archiveFile);
    }

}

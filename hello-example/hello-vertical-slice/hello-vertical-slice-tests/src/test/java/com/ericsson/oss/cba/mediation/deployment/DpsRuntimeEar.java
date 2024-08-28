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
import java.util.Collection;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;


public class DpsRuntimeEar {
    
    private String dpsEarBuildLocation;
        
    private static final String ERROR_FILE_NOT_EXIST = "[ERROR] There should exist a 'dps-ear-runtime*.ear' file.";
    
    private static IOFileFilter fileFilter = new WildcardFileFilter("dps-ear-runtime*.ear");
    

    public DpsRuntimeEar(String dpsEarBuildLocation){
        this.dpsEarBuildLocation = dpsEarBuildLocation;
    }
  
    private File getEarFile(){
        File buildDirectory = new File(dpsEarBuildLocation);
        Collection<File> fileList = FileUtils.listFiles(buildDirectory, fileFilter, null);
        Assert.assertEquals(ERROR_FILE_NOT_EXIST, 1, fileList.size());
        File dpsEarFile = fileList.iterator().next();
        return dpsEarFile;
    }
    
    /**
     * Returns the dps-ear-runtime*.ear as an EnterpriseArchive
     * @return
     */
    public EnterpriseArchive getDpsEnterpriseArchive(){
       return ShrinkWrap.createFromZipFile(EnterpriseArchive.class, getEarFile());
    }
}

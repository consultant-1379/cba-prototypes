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

import java.io.File;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public class MockEar extends IntegrationEnterpriseArchive {

    private final String earName;
    private final String warName;
    
    public MockEar(final String earName) {
        this.earName = earName + ".ear";
        this.warName = earName + ".war";
    }
    
    @Override
    protected String getWarName() {
        return warName;
    }

    @Override
    public EnterpriseArchive getBaseEnterpriseArchive() {
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, earName);
        return ear;
    }

    @Override
    protected JavaArchive getBaseEjbArchive() {
        final JavaArchive ejbJar = ShrinkWrap.create(JavaArchive.class, "ejb.jar").addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        ejbJar.addAsResource(new File("src/test/resources/ServiceFrameworkConfiguration.properties"));
        return ejbJar;
    }

}

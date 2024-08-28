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
package com.ericsson.oss.mediation.cba.cm.test.util;

import javax.ejb.*;
import javax.inject.Inject;

import com.ericsson.oss.itpf.datalayer.dps.DataBucket;
import com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService;
import com.ericsson.oss.itpf.sdk.core.annotation.EServiceRef;
import org.slf4j.Logger;

import com.ericsson.oss.itpf.datalayer.dps.persistence.ManagedObject;

import java.util.Collections;
import java.util.Map;

@Singleton
@LocalBean
public class TestUtil {

    @Inject
    private Logger logger;

    @EServiceRef
    private DataPersistenceService dataPersistenceService;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void addMeContext() {
        final DataBucket liveBucket = dataPersistenceService.getLiveBucket();
        final ManagedObject mo = liveBucket.getMibRootBuilder().namespace("OSS_TOP").type("MeContext").version("2.0.0")
                .name("NEMSRBS_V1").addAttribute("MeContextId", "NEMSRBS_V1").addAttribute("neType", "SGSN-MME")
                .addAttribute("platformType", "CBA").create();
        logger.debug("MeContext created {}", mo);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void addNetworkElement() {
        final DataBucket liveBucket = dataPersistenceService.getLiveBucket();
        final ManagedObject mo = liveBucket.getMibRootBuilder().namespace("OSS_NE_DEF").type("NetworkElement")
                .version("1.0.0").name("NEMSRBS_V1").addAttribute("networkElementId", "NEMSRBS_V1")
                .addAttribute("neType", "SGSN-MME").addAttribute("platformType", "CBA").addAttribute("neVersion", "1")
                .addAttribute("ossPrefix", "").create();
        logger.debug("NetworkElement created {}", mo);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void addConnectivityInfo() {
        Integer port = 22;
        try {
            port = Integer.valueOf(System.getProperty("sshd.port"));
        } catch (NumberFormatException e) {
            logger.warn("I wasn't able to find sshd.port system property. Default port will be used {}", port);
        }
        final DataBucket liveBucket = dataPersistenceService.getLiveBucket();
        final ManagedObject parent = liveBucket.findMoByFdn("NetworkElement=NEMSRBS_V1");
        final ManagedObject mo = liveBucket.getMibRootBuilder().parent(parent).namespace("COM_MED")
                .type("ComConnectivityInformation").name("1").version("1.0.0").addAttribute("ipAddress", "localhost")
                .addAttribute("port", port).create();
        logger.debug("ComConnectivityInformation created {}", mo);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void addManagedElement() {
        final DataBucket liveBucket = dataPersistenceService.getLiveBucket();
        final ManagedObject parent = liveBucket.findMoByFdn("MeContext=NEMSRBS_V1");
        final ManagedObject mo = liveBucket.getMibRootBuilder().parent(parent).namespace("SgsnMmeTop")
                .type("ManagedElement").name("1").version("1.2.0").addAttribute("neType", "SGSN-MME")
                .addAttribute("platformType", "CBA").addAttribute("managedElementId", "1")
                .addAttribute("userLabel", "Ireland").create();
        logger.debug("ManagedElement created {}", mo);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void addCmFunction() {
        final DataBucket liveBucket = dataPersistenceService.getLiveBucket();
        final ManagedObject parent = liveBucket.findMoByFdn("NetworkElement=NEMSRBS_V1");
        final ManagedObject mo = liveBucket.getMibRootBuilder().parent(parent).namespace("OSS_NE_CM_DEF")
                .type("CmFunction").name("1").version("1.0.0").create();
        logger.debug("CmFunction created {}", mo);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sync() {
        final DataBucket liveBucket = dataPersistenceService.getLiveBucket();
        final ManagedObject parent = liveBucket.findMoByFdn("NetworkElement=NEMSRBS_V1,CmFunction=1");
        final Object result = parent.performAction("sync", Collections.<String, Object> emptyMap());
        logger.debug("CmFunction sync command was sent with output {}", result);
    }
}

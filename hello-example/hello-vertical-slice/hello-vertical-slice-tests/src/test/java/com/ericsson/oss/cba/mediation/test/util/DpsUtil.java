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
package com.ericsson.oss.cba.mediation.test.util;

import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.Objects;

import javax.ejb.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.cba.mediation.deployment.Constants;
import com.ericsson.oss.itpf.datalayer.dps.DataBucket;
import com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService;
import com.ericsson.oss.itpf.datalayer.dps.persistence.ManagedObject;
import com.ericsson.oss.itpf.datalayer.dps.persistence.PersistenceObject;
import com.ericsson.oss.itpf.sdk.core.annotation.EServiceRef;

@Stateless
public class DpsUtil {

    @EServiceRef
    private DataPersistenceService dataPersistenceService;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DpsUtil.class);

    /**
     * Creates an MeContext root in the live bucket with the FDN MeContext=LTE02ERBS0000<b>id</b>
     *
     * @param id
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void createMeContext(final int id) {
        final DataBucket liveBucket = getMediatedLiveBucket();
        final String name = Constants.getMeContextName(id);
        LOGGER.info("Creating MeContext with FDN MeContext={}", name);
        final ManagedObject mo = liveBucket.getMibRootBuilder().namespace(Constants.OSS_TOP).type(Constants.ME_CONTEXT)
                .version(Constants.ME_CONTEXT_VERSION).name(name).addAttribute(Constants.ME_CONTEXT_ID_ATTR, name).addAttribute("neType", "ENODEB")
                .create();
        logPostCreation(mo.getFdn());
    }

    /**
     * Creates a ManagedElement under the MeContext in live bucket.<br>
     * MeContext=LTE01ERBS0000<b>id</b>,ManagedElement=1
     *
     * @param id
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void createManagedElement(final int id) {
        final DataBucket liveBucket = getMediatedLiveBucket();
        final String parentFdn = Constants.getMeContextFdn(id);
        LOGGER.info("Creating ManagedElement with FDN {},ManagedElement=1", parentFdn);
        final ManagedObject parent = liveBucket.findMoByFdn(parentFdn);
        verifyParentExists(parentFdn, parent);
        final ManagedObject mo = liveBucket.getMibRootBuilder().type(Constants.MANAGED_ELEMENT).namespace(Constants.CPP_NODE_MODEL)
                .name(Constants.NON_ROOT_MO_ID).version(Constants.MANAGED_ELEMENT_VERSION).parent(parent)
                .addAttribute(Constants.MANAGED_ELEMENT_ID_ATTR, Constants.NON_ROOT_MO_ID).create();
        logPostCreation(mo.getFdn());
    }

    /**
     * Creates an ENodeBFunction under the ManagedElement in the live bucket.<br>
     * MeContext=LTE01ERBS0000<b>id</b>,ManagedElement=1,ENodeBFunction=1
     *
     * @param id
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void createENodeBFunction(final int id) {
        final DataBucket liveBucket = getMediatedLiveBucket();
        final String parentFdn = Constants.getManagedElementFdn(id);
        final ManagedObject parent = liveBucket.findMoByFdn(parentFdn);
        verifyParentExists(parentFdn, parent);
        final ManagedObject mo = liveBucket.getMibRootBuilder().type(Constants.E_NODE_B_FUNCTION).namespace(Constants.ERBS_NODE_MODEL)
                .version(Constants.E_NODE_B_FUNCTION_VERSION).parent(parent).name(Constants.NON_ROOT_MO_ID)
                .addAttribute(Constants.E_NODE_B_FUNCTION_ID_ATTR, Constants.NON_ROOT_MO_ID).create();
        logPostCreation(mo.getFdn());
    }

    /**
     * Creates an ERBSConnectivityInfo MO in the live bucket.<br>
     * MeContext=LTE01ERBS0000<b>id</b>,ManagedElement=1,ENodeBFunction=1, ERBSConnectivityInfo=1
     *
     * TODO: The default gateway is atvts70.vpn with simulation having nodeIp = 192.100.168.0+id
     *
     * see http://jira-oss.lmera.ericsson.se/browse/TORF-13030 for task to connect to NetSim
     *
     * @param id
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void createErbsConnectivityInfo(final int id) {
        final DataBucket liveBucket = getMediatedLiveBucket();
        final String parentFdn = Constants.getENodeBFunctionFdn(id);
        final ManagedObject parent = liveBucket.findMoByFdn(parentFdn);
        verifyParentExists(parentFdn, parent);
        final String ipAddress = Constants.NODE_IP+id;
        LOGGER.debug("ipAddress : {}",ipAddress);
        final ManagedObject mo = liveBucket.getMibRootBuilder().name(Constants.NON_ROOT_MO_ID).namespace(Constants.ERBS_NODE_MODEL).parent(parent)
                .type(Constants.CONNECTIVITY_INFO).version(Constants.CONNECTIVITY_INFO_VERSION).addAttribute("ipAddress", ipAddress).create();
        logPostCreation(mo.getFdn());
    }

    private void verifyParentExists(final String parentFdn, final ManagedObject parent) {
        assertNotNull("Could not get parent with FDN " + parentFdn + " from DPS", parent);
    }

    private void logPostCreation(final String fdn) {
        LOGGER.info("Created ManagedObject with FDN {}", fdn);
    }

    /**
     * Creates a NoMedMo MO in the live bucket.<br>
     * NoMedMo=1
     */
    public String createNoMedMo() {
        final DataBucket liveBucket = getNonMediatedLiveBucket();
        LOGGER.info("######## Creating NoMedMo ##########");
        final ManagedObject mo = liveBucket.getMibRootBuilder().namespace(Constants.NO_MEDIATION_CONFIG).name(Constants.NON_ROOT_MO_ID)
                .version(Constants.NO_MED_MO_VERSION).type(Constants.NO_MED_MO).addAttribute(Constants.NO_MED_MO_ID_ATTR, Constants.NON_ROOT_MO_ID)
                .create();
        logPostCreation(mo.getFdn());
        return mo.getFdn();
    }

    /**
     * Creates a DummyPersistenceObject in the live bucket.
     *
     * @return the PoId
     */
    public long createDummyPersistenceObject() {
        final DataBucket liveBucket = getNonMediatedLiveBucket();
        LOGGER.info("Creating DummyPersistenceObject");
        final PersistenceObject po = liveBucket.getPersistenceObjectBuilder().namespace(Constants.ERBS_NODE_MODEL)
                .version(Constants.DUMMY_PERSISTENCE_OBJECT_VERSION).type(Constants.DUMMY_PERSISTENCE_OBJECT).create();
        LOGGER.info("Created DummyPersistenceObject with PoId {}", po.getPoId());
        return po.getPoId();
    }

    /**
     * Deletes any ManagedObject from live bucket
     *
     * @param fdn
     */
    public void deleteManagedObject(final String fdn) {
        LOGGER.debug("Deleting ManagedObject with FDN {}", fdn);
        final DataBucket liveBucket = getMediatedLiveBucket();
        final ManagedObject mo = liveBucket.findMoByFdn(fdn);
        if (mo != null) {
            liveBucket.deletePo(mo);
            LOGGER.debug("Deleted ManagedObject with FDN {}", fdn);
        } else {
            LOGGER.warn("Could not find ManagedObject with FDN {} to delete", fdn);
        }
    }

    /**
     * Deletes a PO from the live bucket. Can be used to delete MO's also.
     *
     * @param id
     */
    public void deletePo(final long id) {
        final DataBucket liveBucket = getMediatedLiveBucket();
        LOGGER.debug("Deleting PersistenceObject with PoId {}", id);
        final PersistenceObject po = liveBucket.findPoById(id);
        liveBucket.deletePo(po);
        LOGGER.debug("Deleted PersistenceObject with PoId {}", id);
    }

    /**
     * Finds an MO from the live bucket.
     *
     * @param fdn
     * @return the ManagedObject
     */
    public ManagedObject findMo(final String fdn) {
        final DataBucket liveBucket = getMediatedLiveBucket();
        LOGGER.debug("Retrieving ManagedObject with FDN {}", fdn);
        final ManagedObject mo = liveBucket.findMoByFdn(fdn);
        if (mo == null) {
            LOGGER.warn("Could not find ManagedObject with FDN {}", fdn);
        }

        return mo;
    }

    /**
     * Finds a PO using its ID from the live bucket.
     *
     * @param id
     * @return the PersistenceObject
     */
    public PersistenceObject findPo(final long id) {
        final DataBucket liveBucket = getMediatedLiveBucket();
        LOGGER.debug("Retrieving PersistenceObject with PoId {}", id);
        final PersistenceObject po = liveBucket.findPoById(id);
        if (po == null) {
            LOGGER.warn("Could not find PersistenceObject with PoId {}", id);
        }
        return po;
    }

    /**
     * Gets the EntityAddressInfo PoId for the ENodeBFunction with FDN MeContext=LTE01ERBS00001,ManagedElement=1,ENodeBFunction=1
     *
     * @return
     */
    public long getEntityAddressInfoId() {
        return getEaiId(1);
    }

    /**
     * Gets the EntityAddressInfo PoId for the ENodeBFunction with FDN MeContext=LTE01ERBS0000<b>id</b>,ManagedElement=1,ENodeBFunction=1
     *
     * @param id
     * @return
     */
    public long getEntityAddressInfoId(final int id) {
        return getEaiId(id);
    }

    private long getEaiId(final int id) {
        final DataBucket liveBucket = getMediatedLiveBucket();
        long eaiId = -1;
        final ManagedObject mo = liveBucket.findMoByFdn(Constants.getENodeBFunctionFdn(id));
        if (mo != null) {
            final PersistenceObject eai = mo.getEntityAddressInfo();
            if (eai != null) {
                eaiId = mo.getEntityAddressInfo().getPoId();
            }
        }
        return eaiId;
    }

    /**
     * Checks that an association exists form one PO to another with a given endPoint
     *
     * @param poId
     *            The ID of the PO which contains the association
     * @param associatedPoId
     *            The ID of the PO which should be associated
     * @param endPoint
     *            The endPoint name
     * @return <code>true</code> if the association exists<br>
     *         <code>false</code> otherwise
     */
    public boolean checkAssociation(final long poId, final long associatedPoId, final String endPoint) {
        final DataBucket liveBucket = getMediatedLiveBucket();
        boolean associationExists = false;
        final PersistenceObject po = liveBucket.findPoById(poId);
        final Collection<PersistenceObject> associations = po.getAssociations(endPoint);
        for (final PersistenceObject association : associations) {
            associationExists = Objects.equals(association.getPoId(), associatedPoId);
            if (associationExists) {
                break;
            }
        }
        return associationExists;
    }

    private DataBucket getMediatedLiveBucket() {
        return dataPersistenceService.getLiveBucket();
    }

    private DataBucket getNonMediatedLiveBucket() {
        return dataPersistenceService.getDataBucket("live", "SUPRESS_MEDIATION");
    }

}

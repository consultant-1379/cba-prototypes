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
import static org.junit.Assert.assertNull;

import javax.ejb.*;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.cba.mediation.async.AsyncLatch;
import com.ericsson.oss.cba.mediation.deployment.Constants;

/**
 * Utility class for adding and deleting nodes
 *
 * @author eshacow
 *
 */
@Stateless
@LocalBean
public class TestUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestUtil.class);

    @Inject
    private DpsUtil dpsUtil;

    @EJB
    private AsyncLatch latch;

    /**
     * Adds a single node with a given id
     *
     * @param id
     *            the id of the node to be added
     */
    public void addNode(final int id) {
        dpsUtil.createMeContext(id);
        dpsUtil.createManagedElement(id);
//        dpsUtil.createENodeBFunction(id);
//        dpsUtil.createErbsConnectivityInfo(id);
    }

    /**
     * Asynchronously adds a single node with a given id
     *
     * @param id
     *            the id of the node to be added
     * @throws IllegalAccessException
     *             if the <code>AsyncLatch</code> has not been initialized
     */
    @Asynchronous
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void addNodeAsynchronous(final int id) throws IllegalAccessException {
        try {
            dpsUtil.createMeContext(id);
//            dpsUtil.createManagedElement(id);
//            dpsUtil.createENodeBFunction(id);
//            dpsUtil.createErbsConnectivityInfo(id);
        } catch (final Throwable t) {
            LOGGER.error("Was unable to create node with id {}", id);
            t.printStackTrace();
        } finally {
            latch.decrementCount();
            LOGGER.info("Have completed asynchronous invocation for id {}", id);
        }
    }

    /**
     * Test verification that a node has been added for a node with a given id
     *
     * @param id
     *            the id of the node
     */
    public void verifyNodeAdded(final int id) {
        assertNotNull("MeC is null for fdn, " + Constants.getMeContextFdn(id), dpsUtil.findMo(Constants.getMeContextFdn(id)));
//        assertNotNull("ManagedElement is null for fdn, " + Constants.getManagedElementFdn(id), dpsUtil.findMo(Constants.getManagedElementFdn(id)));
//        assertNotNull("ENodeBFunction is null for fdn, " + Constants.getENodeBFunctionFdn(id), dpsUtil.findMo(Constants.getENodeBFunctionFdn(id)));
//        assertNotNull("ERBSConnectivityInfo is null for fdn, " + Constants.getErbsConnectivityInfoFdn(id),
//                dpsUtil.findMo(Constants.getErbsConnectivityInfoFdn(id)));
//        verifyCiAssociation(dpsUtil.getEntityAddressInfoId(id), dpsUtil.findMo(Constants.getErbsConnectivityInfoFdn(id)).getPoId());
    }

//    private void verifyCiAssociation(final long eaiId, final long connInfoId) {
//        assertTrue("Association does not exist", dpsUtil.checkAssociation(eaiId, connInfoId, Constants.CI_ASSOCIATION));
//    }

    /**
     * Deletes a node with the given id. Deletes EAI object also
     *
     * @param id
     *            the id of the node to be deleted
     */
    public void deleteNode(final int id) {
//        final long eaiId = deleteEai(id);
        dpsUtil.deleteManagedObject(Constants.getMeContextFdn(id));
        verifyNodeDeleted(id);
    }

//    public void verifyInformationInLogs(final MockLogHandler handler, final String loggerName, final String searchLog){
//        final List<LogRecord> logs = handler.getLogs(loggerName);
//        boolean found = false;
//        final Iterator<LogRecord> iterator = logs.iterator();
//        while (!found & iterator.hasNext()) {
//            final LogRecord record = iterator.next();
//            LOGGER.info("***************************************** record {}", record);
//            found = record.getMessage().equals(searchLog);
//        }
//        assertTrue(found);
//    }

    private long deleteEai(final int id) {
        final long eaiId = dpsUtil.getEntityAddressInfoId(id);
        if (eaiId != -1) {
            dpsUtil.deletePo(eaiId);
        } else {
            LOGGER.debug("Was unable to get EAI for node with id {}", id);
        }
        return eaiId;
    }

    private void verifyNodeDeleted(final int id) {
        assertNull("MeContext has not been deleted with fdn, " + Constants.getMeContextFdn(id), dpsUtil.findMo(Constants.getMeContextFdn(id)));
//        assertNull("ManagedElement has not been deleted with fdn, " + Constants.getManagedElementFdn(id),
//                dpsUtil.findMo(Constants.getManagedElementFdn(id)));
//        assertNull("ENodeBFunction has not been deleted with fdn, " + Constants.getENodeBFunctionFdn(id),
//                dpsUtil.findMo(Constants.getENodeBFunctionFdn(id)));
//        assertNull("ERBSConnectivityInfo has not been deleted with fdn, " + Constants.getErbsConnectivityInfoFdn(id),
//                dpsUtil.findMo(Constants.getErbsConnectivityInfoFdn(id)));
//        assertNull("EntityAddressInfo has not been deleted for ENodeBFunction with fdn " + Constants.getENodeBFunctionFdn(id), dpsUtil.findPo(eaiId));
    }

}

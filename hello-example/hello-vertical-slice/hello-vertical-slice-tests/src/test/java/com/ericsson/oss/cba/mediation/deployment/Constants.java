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

public class Constants {

    public static final String NODE_IP ="192.168.100.";

    // Test case log
    public static final String TEST_CASE = "TEST_CASE";

    // Associations
    public static final String CI_ASSOCIATION = "ciAssociation";

    // Base constants
    private static final String ME_CONTEXT_FDN_BASE = "MeContext=LTE01ERBS0000";
    private static final String ME_CONTEXT_NAME_BASE = "LTE01ERBS0000";
    private static final String MANAGED_ELEMENT_FDN_BASE = ",ManagedElement=1";
    private static final String E_NODE_B_FUNCTION_FDN_BASE = ",ENodeBFunction=1";
    private static final String ERBS_CONNECTIVITY_INFO_FDN_BASE = ",ERBSConnectivityInfo=1";

    // IDs
    public static final String NON_ROOT_MO_ID = "1";

    // Object types
    public static final String ME_CONTEXT = "MeContext";
    public static final String MANAGED_ELEMENT = "ManagedElement";
    public static final String MANAGED_FUNCTION = "ManagedFunction";
    public static final String CONNECTIVITY_INFO = "ERBSConnectivityInfo";
    public static final String E_NODE_B_FUNCTION = "ENodeBFunction";
    public static final String ENTITY_ADDRESS_INFO = "EntityAddressInfo";
    public static final String NO_MED_MO = "NoMedMo";
    public static final String DUMMY_PERSISTENCE_OBJECT = "DummyPersistenceObject";

    // FDNs
    public static final String NO_MED_MO_FDN = "NoMedMo=" + NON_ROOT_MO_ID;

    // Namespace
    public static final String ERBS_NODE_MODEL = "ERBS_NODE_MODEL";
    public static final String OSS_TOP = "OSS_TOP";
    public static final String OSS_MED = "OSS_MED";
    public static final String CPP_NODE_MODEL = "CPP_NODE_MODEL";
    public static final String NO_MEDIATION_CONFIG = "NO_MEDIATION_CONFIG";

    // Version
    public static final String E_NODE_B_FUNCTION_VERSION = "3.1.72";
    public static final String ME_CONTEXT_VERSION = "1.1.0";
    public static final String CONNECTIVITY_INFO_VERSION = "1.0.0";
    public static final String ENTITY_ADDRESS_INFO_VERSION = "1.0.0";
    public static final String MANAGED_ELEMENT_VERSION = "3.12.0";
    public static final String DUMMY_PERSISTENCE_OBJECT_VERSION = "1.0.0";
    public static final String NO_MED_MO_VERSION = "1.0.1";

    // Attributes
    public static final String ME_CONTEXT_ID_ATTR = "MeContextId";
    public static final String MANAGED_ELEMENT_ID_ATTR = "ManagedElementId";
    public static final String E_NODE_B_FUNCTION_ID_ATTR = "ENodeBFunctionId";
    public static final String NO_MED_MO_ID_ATTR = "NoMedMoId";

    public static String getMeContextFdn(final int id) {
        return ME_CONTEXT_FDN_BASE + id;
    }

    public static String getMeContextName(final int id) {
        return ME_CONTEXT_NAME_BASE + id;
    }

    public static String getManagedElementFdn(final int id) {
        return getMeContextFdn(id) + MANAGED_ELEMENT_FDN_BASE;
    }

    public static String getENodeBFunctionFdn(final int id) {
        return getManagedElementFdn(id) + E_NODE_B_FUNCTION_FDN_BASE;
    }

    public static String getErbsConnectivityInfoFdn(final int id) {
        return getENodeBFunctionFdn(id) + ERBS_CONNECTIVITY_INFO_FDN_BASE;
    }

}

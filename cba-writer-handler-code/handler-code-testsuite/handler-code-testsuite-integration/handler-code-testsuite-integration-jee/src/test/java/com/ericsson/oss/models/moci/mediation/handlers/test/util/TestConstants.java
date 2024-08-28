/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.models.moci.mediation.handlers.test.util;


public class TestConstants {

    public static final String TEST = "test";
    public static final String ADDRESS = "ipAddress";
    public static final String SECURED = "secured";
    public static final String TIMEOUT_VALUE = "1";
    public static final String CAMEL_ENGINE_HANDLER_ROUTE = "CAMEL_ENGINE_HANDLER_ROUTE";
    public static final String SESSION_VERIFIER_JNDI = "java:/sessionVerifier";

    //IP-addresses used in testing
    public static final String IP_ADDRESS_VALUE = "192.168.100.1";
    public static final String IP_ADDRESS_VALUE_UN_REACHABLE = "192.168.1.111";

    //FDNs used in testing
    public static final String CONFIGURATION_VERSION_FDN = "MeContext=ERBS0050,ManagedElement=1,SwManagement=1,ConfigurationVersion=1";
    public static final String IP_ROUTING_TABLE_FDN = "MeContext=1,ManagedElement=1,IpOam=1,Ip=1,IpRoutingTable=1";
    public static final String IP_ROUTING_TABLE_NOT_EXIST_FDN = "MeContext=1,ManagedElement=1,IpOam=1,Ip=1,IpRoutingTable=NotExist";
    public static final String ENODE_B_FUNC_FDN = "MeContext=1,ManagedElement=1,ENodeBFunction=1";
    public static final String SECURITY_FDN = "MeContext=1,ManagedElement=1,SystemFunctions=1,Security=1";

    //Node actions used in testing
    public static final String GET_FROM_FTP_SERVER = "getFromFtpServer";
    public static final String LIST_ROUTES = "listRoutes";
    public static final String SET_STARTABLE = "setStartable";
    public static final String GET_ROUTING_TABLE_ENTRY = "getRoutingTableEntry";
    public static final String ACTIVATE_USER_DEF_PROFILES = "activateUserDefProfiles";

    //Node attributes used in testing
    public static final String USER_LABEL = "userLabel";
    public static final String STORED_CONFIG_VERSIONS = "storedConfigurationVersions";
    public static final String CONFIGURATION_VERSION_ID = "ConfigurationVersionId";
    public static final String CONFIG_OP_COUNT_DOWN = "configOpCountdown";
    public static final String X2_BLACK_LIST = "x2BlackList";

    //MO properties
    public static final String CPP_NODE_MODEL = "CPP_NODE_MODEL";
    public static final String ERBS_NODE_MODEL = "ERBS_NODE_MODEL";
    public static final String CONFIGURATION_VERSION = "ConfigurationVersion";
    public static final String CURRENT_UPGRADE_PCK = "currentUpgradePackage";
    public static final String IP_ROUTING_TABLE = "IpRoutingTable";
    public static final String ENODE_B_FUNC = "ENodeBFunction";
    public static final String SECURITY = "Security";
    public static final String MO_VERSION = "14.130.5";
    public static final String ENODE_VERSION = "5.1.120";
    
  //CREATE MO properties
    public static final String UPGRADE_PACKAGE_ID = "CXP102051_1_R4D74"; 
    public static final String FTP_SERVER_IPADDRESS = "192.168.100.132";
    public static final String UPFILE_PATH_ON_FTPSERVER  = "CXP102051_1_R4D74.xml";
    public static final String USER = "anonymous";
    public static final String PASSWORD = "anonymous";
    
    public static final String ATTR_UPGRADE_PACKAGE_ID = "UpgradePackageId"; 
    public static final String ATTR_FTP_SERVER_IPADDRESS = "ftpServerIpAddress";
    public static final String ATTR_UPFILE_PATH_ON_FTPSERVER  = "upFilePathOnFtpServer";
    public static final String ATTR_USER = "user";
    public static final String ATTR_PASSWORD = "password";
    public static final String ATTR_USER_LABEL = "userLabel";
}

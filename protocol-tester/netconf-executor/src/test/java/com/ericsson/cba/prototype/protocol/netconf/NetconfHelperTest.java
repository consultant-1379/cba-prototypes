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
package com.ericsson.cba.prototype.protocol.netconf;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import com.jcraft.jsch.JSchException;

/**
 * 
 * @author xshakku
 * 
 */
public class NetconfHelperTest {

	/****** Testing TCU node *******/
	@Test
	public void testPerformGetConfigOnTCUNode() throws JSchException,
			InterruptedException, IOException {
		NetconfHelper netconfHelper = new NetconfHelper();
		netconfHelper.openSession(new TCUCredentialHolder());
		String result = netconfHelper.runQuery(Queries.getConfigQuery("200",
				"running"));
		System.err.println(result);
		netconfHelper.closeSession();
	}

	@Test
	public void testPerformGetSchemaWithFilterOnTCUNode() throws JSchException,
			InterruptedException, IOException {
		NetconfHelper netconfHelper = new NetconfHelper();
		netconfHelper.openSession(new TCUCredentialHolder());
		String schemas = "<ManagedElement xmlns=\"urn:com:ericsson:ecim:ComTop\"><managedElementId>TCU</managedElementId><SystemFunctions><systemFunctionsId>1</systemFunctionsId><SysM xmlns=\"urn:com:ericsson:ecim:ComSysM\"><sysMId>1</sysMId><Schema></Schema></SysM></SystemFunctions></ManagedElement>";
		String result = netconfHelper.runQuery(Queries.getWithSubtreeFilter(
				"204", "running", schemas));
		System.err.println(result);
		netconfHelper.closeSession();
	}

	@Test
	public void testPerformGetUserLabelWithFilterOnTCUNode()
			throws JSchException, InterruptedException, IOException {
		NetconfHelper netconfHelper = new NetconfHelper();
		netconfHelper.openSession(new TCUCredentialHolder());
		String batteryBackupUserLabel = "<ManagedElement xmlns=\"urn:com:ericsson:ecim:ComTop\"><managedElementId>TCU</managedElementId><EquipmentSupportFunction xmlns=\"urn:com:ericsson:ecim:ResEquipmentSupportFunction\"> <equipmentSupportFunctionId>1</equipmentSupportFunctionId><BatteryBackup xmlns=\"urn:com:ericsson:ecim:ResBatteryBackup\"><batteryBackupId>1</batteryBackupId><userLabel/></BatteryBackup></EquipmentSupportFunction></ManagedElement>";
		String result = netconfHelper.runQuery(Queries.getWithSubtreeFilter(
				"201", "running", batteryBackupUserLabel));
		System.err.println(result);
		netconfHelper.closeSession();
	}

	@Test
	public void testPerformWriteOnTCUNode() throws JSchException,
			InterruptedException, IOException {
		NetconfHelper netconfHelper = new NetconfHelper();
		netconfHelper.openSession(new TCUCredentialHolder());
		String batteryBackupUserLabel = "<ManagedElement xmlns=\"urn:com:ericsson:ecim:ComTop\"><managedElementId>TCU</managedElementId><EquipmentSupportFunction xmlns=\"urn:com:ericsson:ecim:ResEquipmentSupportFunction\"> <equipmentSupportFunctionId>1</equipmentSupportFunctionId><BatteryBackup xmlns=\"urn:com:ericsson:ecim:ResBatteryBackup\"><batteryBackupId>1</batteryBackupId> <userLabel>"
				+ "Shakti"
				+ "</userLabel></BatteryBackup></EquipmentSupportFunction></ManagedElement>";
		String result = netconfHelper.runQuery(Queries.getEditConfig("202",
				"running", batteryBackupUserLabel));
		System.err.println(result);
		netconfHelper.closeSession();
	}

	/****** Testing Sgsn-MME node *******/
	@Test
	public void testPerformGetWithFilterOnSgsnMMENode() throws JSchException,
			InterruptedException, IOException {
		NetconfHelper netconfHelper = new NetconfHelper();
		netconfHelper.openSession(new SgsnMMECredentialHolder());
		String mMEAttachThrottlingEnabled = "<ManagedElement xmlns=\"urn:com:ericsson:ecim:SgsnMmeTop\"><managedElementId>SGSN13B01</managedElementId><SgsnMme xmlns=\"urn:com:ericsson:ecim:Sgsn_Mme\"><sgsnMmeId>1</sgsnMmeId><MME><mMEName>1</mMEName><mMEAttachThrottlingEnabled/></MME></SgsnMme></ManagedElement>";
		String result = netconfHelper.runQuery(Queries.getWithSubtreeFilter(
				"300", "running", mMEAttachThrottlingEnabled));
		System.err.println(result);
		netconfHelper.closeSession();
	}

	@Test
	public void testPerformGetConfigOnSgsnMMENode() throws JSchException,
			InterruptedException, IOException {
		NetconfHelper netconfHelper = new NetconfHelper();
		netconfHelper.openSession(new SgsnMMECredentialHolder());
		String result = netconfHelper.runQuery(Queries.getConfigQuery("301",
				"running"));
		System.err.println(result);
		netconfHelper.closeSession();
	}

	@Ignore
	@Test
	public void testPerformGetCandidateConfigOnSgsnMMENode()
			throws JSchException, InterruptedException, IOException {
		NetconfHelper netconfHelper = new NetconfHelper();
		netconfHelper.openSession(new SgsnMMECredentialHolder());
		String result = netconfHelper.runQuery(Queries.getConfigQuery("302",
				"candidate"));
		System.err.println(result);
		netconfHelper.closeSession();
	}

	@Test
	public void testPerformValidateCandidateConfigOnSgsnMMENode()
			throws JSchException, InterruptedException, IOException {
		NetconfHelper netconfHelper = new NetconfHelper();
		netconfHelper.openSession(new SgsnMMECredentialHolder());
		String result = netconfHelper.runQuery(Queries.validate("303",
				"candidate"));
		System.err.println(result);
		netconfHelper.closeSession();
	}

	@Test
	public void testPerformWriteRunningConfigOnSgsnMMENode()
			throws JSchException, InterruptedException, IOException {
		NetconfHelper netconfHelper = new NetconfHelper();
		netconfHelper.openSession(new SgsnMMECredentialHolder());
		String mMEAttachThrottlingEnabled = "<ManagedElement xmlns=\"urn:com:ericsson:ecim:SgsnMmeTop\"><managedElementId>SGSN13B01</managedElementId><SgsnMme xmlns=\"urn:com:ericsson:ecim:Sgsn_Mme\"><sgsnMmeId>1</sgsnMmeId><MME><mMEName>1</mMEName><mMEAttachThrottlingEnabled>"
				+ "true"
				+ "</mMEAttachThrottlingEnabled></MME></SgsnMme></ManagedElement>";

		String result = netconfHelper.runQuery(Queries.getEditConfig("304",
				"running", mMEAttachThrottlingEnabled));
		System.err.println(result);
		netconfHelper.closeSession();
	}

	//@Ignore
	@Test
	public void testPerformWriteCandidateConfigOnSgsnMMENode()
			throws JSchException, InterruptedException, IOException {
		NetconfHelper netconfHelper = new NetconfHelper();
		netconfHelper.openSession(new SgsnMMECredentialHolder());
		String mMEAttachThrottlingEnabled = "<ManagedElement xmlns=\"urn:com:ericsson:ecim:SgsnMmeTop\"><managedElementId>SGSN13B01</managedElementId><SgsnMme xmlns=\"urn:com:ericsson:ecim:Sgsn_Mme\"><sgsnMmeId>1</sgsnMmeId><MME xc:operation=\"merge\"><mMEName>1</mMEName><mMEAttachThrottlingEnabled>"
				+ "true"
				+ "</mMEAttachThrottlingEnabled></MME></SgsnMme></ManagedElement>";

		String result = netconfHelper.runQuery(Queries.getEditConfig("305",
				"candidate", mMEAttachThrottlingEnabled));
		System.err.println(result);
		netconfHelper.closeSession();
	}

	@Ignore
	@Test
	public void testPerformCommitSgsnMMENode() throws JSchException,
			InterruptedException, IOException {
		NetconfHelper netconfHelper = new NetconfHelper();
		netconfHelper.openSession(new SgsnMMECredentialHolder());
		String result = netconfHelper.runQuery(Queries.commit("306"));
		System.err.println(result);
		netconfHelper.closeSession();
	}

}

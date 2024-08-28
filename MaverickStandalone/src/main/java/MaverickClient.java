import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import com.maverick.ssh.*;
import com.maverick.ssh2.Ssh2Session;
import com.sshtools.net.SocketTransport;

public class MaverickClient {

    private String ipAddress = "192.168.100.1";
    private int port = 22;

    private SshClient sshClient;
    private SshSession sshSession;
    private SocketTransport socketTransport;
    private String username = "netsim";

    protected final String password = "netsim";

    private Pattern commandPrompt = Pattern.compile("]]>]]>", Pattern.DOTALL);
    protected final String subsystem = "netconf";
    private InputStream stdOut;
    private OutputStream stdIn;

    private enum actions {
        ACTION_loadLicense, ACTION_deleteRas, ACTION_dss7SysName, ACTION_sctpSys, ACTION_removeUpgradePackage, ACTION_createUpgradePackage, ACTION_createBackup
    }

    private enum netconf_operations {
        LOCK, EDIT_CONFIG, VALIDATE, COMMIT, UNLOCK, EDIT_CONFIG_CREATE, EDIT_CONFIG_DELETE, EDIT_CONFIG_UPDATE, HELLO, CLOSE, GET, EDIT_CONFIG_COMPLEX_CREATE, EDIT_CONFIG_UPDATE_SEQUENCE, EDIT_CONFIG_UPDATE_SEQENCE_OF_STRUCT
    }

    static {
        try {

            final String fileName = "/license/maverick_license.txt";
            final BufferedReader bufferReader = new BufferedReader(new InputStreamReader(MaverickClient.class.getResourceAsStream(fileName)));
            final StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\r\n");
            }
            System.out.println("license String" + stringBuilder.toString());
            com.maverick.ssh.LicenseManager.addLicense(stringBuilder.toString());
        } catch (final Exception exc) {
            exc.printStackTrace();
        }
    }

    static String readFile(final String path, final Charset encoding) throws IOException {
        final byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static void main(final String[] args) {
        final MaverickClient mc = new MaverickClient();
        try {
            mc.openSession();

            //            String content = readFile("sgsnBrmBackUpAction.xml", StandardCharsets.UTF_8);
            String content = readFile("radioBrmBackUpAction.xml", StandardCharsets.UTF_8);
            content = content + "]]>]]>";

            System.out.println(mc.executeCommand(getNetconfCommands(netconf_operations.HELLO, "LTE08dg2ERBS00001")));

            System.out.println(mc.executeCommand(content));
            //            System.out.println(mc.executeCommand(getNetconfCommands(netconf_operations.EDIT_CONFIG_UPDATE_SEQUENCE, "NE01_DUSGEN")));
            //            System.out.println(mc.executeCommand(getAction(actions.ACTION_deleteRas, "SGSN-15A-WPP-V501")));
            //            final String rpc = content;
            //            System.out.println("RPC to send:" + rpc);
            //
            //            System.out.println(mc.executeCommand(rpc));
            //            System.out.println(mc.executeCommand(getNetconfCommands(netconf_operations.GET, "NE02")));
            //            final String getResponse = mc.executeCommand(getCustomFilter("NE51"));

            //SGSN Action MO
            //            testSGSNActions(mc);

            //SGSN Delete MO
            //            testSGSNDeleteSingleMO(mc);

            //SGSN Create MO
            //testSGSNCreateSingleMO(mc);

            //            testSGSNCreateComplexMO(mc);

            //SGSN Update MO
            //            testSGSNUpdateSingleMO(mc);

            mc.closeSession();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param mc
     * @throws Exception
     */
    private static void testSGSNCreateComplexMO(final MaverickClient mc) throws Exception {
        System.out.println(mc.executeCommand(getNetconfCommands(netconf_operations.LOCK, "NE01")));
        System.out.println(getNetconfCommands(netconf_operations.EDIT_CONFIG_COMPLEX_CREATE, "NE01"));
        System.out.println(mc.executeCommand(getNetconfCommands(netconf_operations.EDIT_CONFIG_COMPLEX_CREATE, "NE01")));
        System.out.println(mc.executeCommand(getNetconfCommands(netconf_operations.VALIDATE, "NE01")));
        System.out.println(mc.executeCommand(getNetconfCommands(netconf_operations.COMMIT, "NE01")));
        System.out.println(mc.executeCommand(getNetconfCommands(netconf_operations.UNLOCK, "NE01")));
    }

    // SGSN Action commands - Works only when patch - P04229 is installed.
    private static void testSGSNActions(final MaverickClient mc) throws Exception {
        //        System.out.println(mc.executeCommand(getAction(actions.ACTION_loadLicense, "NE02")));
        //        System.out.println(mc.executeCommand(getAction(actions.ACTION_deleteRas, "NE02")));
        //        System.out.println(mc.executeCommand(getAction(actions.ACTION_dss7SysName, "NE02")));
        System.out.println(getAction(actions.ACTION_sctpSys, "NE01"));
        System.out.println(mc.executeCommand(getAction(actions.ACTION_sctpSys, "NE01")));
        //        System.out.println(mc.executeCommand(getAction(actions.ACTION_removeUpgradePackage, "NE02")));
        //        System.out.println(mc.executeCommand(getAction(actions.ACTION_createUpgradePackage, "NE02")));
        //        System.out.println(mc.executeCommand(getAction(actions.ACTION_createBackup, "NE02")));
    }

    // SGSN Delete MO commands 
    // This test will delete ManagedElement=NE02,SgsnMme=1,Dt=1
    private static void testSGSNDeleteSingleMO(final MaverickClient mc) throws Exception {
        System.out.println(mc.executeCommand(getNetconfCommands(netconf_operations.LOCK, "NE02")));
        System.out.println(mc.executeCommand(getNetconfCommands(netconf_operations.EDIT_CONFIG_DELETE, "NE02")));
        System.out.println(mc.executeCommand(getNetconfCommands(netconf_operations.VALIDATE, "NE02")));
        System.out.println(mc.executeCommand(getNetconfCommands(netconf_operations.COMMIT, "NE02")));
        System.out.println(mc.executeCommand(getNetconfCommands(netconf_operations.UNLOCK, "NE02")));
    }

    // SGSN Create MO commands 
    // This test will create ManagedElement=NE02,SgsnMme=1,Dt=1
    private static void testSGSNCreateSingleMO(final MaverickClient mc) throws Exception {
        System.out.println(mc.executeCommand(getNetconfCommands(netconf_operations.LOCK, "NE02")));
        System.out.println(mc.executeCommand(getNetconfCommands(netconf_operations.EDIT_CONFIG_CREATE, "NE02")));
        System.out.println(mc.executeCommand(getNetconfCommands(netconf_operations.VALIDATE, "NE02")));
        System.out.println(mc.executeCommand(getNetconfCommands(netconf_operations.COMMIT, "NE02")));
        System.out.println(mc.executeCommand(getNetconfCommands(netconf_operations.UNLOCK, "NE02")));
    }

    // SGSN Update MO commands 
    // This test will update the parameter qoSIpDscpGtpCValue value of ManagedElement=NE02,SgsnMme=1,Qos=1. 
    // this value is hardcoded in the function with variable name ATTR_VALUE. Change it see effect in NETSim.
    private static void testSGSNUpdateSingleMO(final MaverickClient mc) throws Exception {
        System.out.println(mc.executeCommand(getNetconfCommands(netconf_operations.LOCK, "NE02")));
        System.out.println(mc.executeCommand(getNetconfCommands(netconf_operations.EDIT_CONFIG_UPDATE, "NE02")));
        System.out.println(mc.executeCommand(getNetconfCommands(netconf_operations.VALIDATE, "NE02")));
        System.out.println(mc.executeCommand(getNetconfCommands(netconf_operations.COMMIT, "NE02")));
        System.out.println(mc.executeCommand(getNetconfCommands(netconf_operations.UNLOCK, "NE02")));
    }

    public void openSession() throws Exception {
        this.socketTransport = new SocketTransport(this.ipAddress, this.port);
        this.socketTransport.setSoTimeout(2000);

        this.sshClient = SshConnector.getInstance().connect(this.socketTransport, this.username, false);

        int authenticationState = -1;
        final PasswordAuthentication pwd = new PasswordAuthentication();
        pwd.setPassword(this.password);
        authenticationState = this.sshClient.authenticate(pwd); // User authentication

        switch (authenticationState) {
            case SshAuthentication.COMPLETE: {
                this.sshSession = this.sshClient.openSessionChannel();
                ((Ssh2Session) this.sshSession).startSubsystem(this.subsystem);
                stdIn = this.sshSession.getOutputStream();
                stdOut = this.sshSession.getInputStream();
                break;
            }
            default: {
                throw new Exception("Authentication failed - Error code: " + authenticationState);
            }
        }
    }

    public final void closeSession() {
        try {
            if (this.sshSession != null) {
                this.sshSession.close();
            }
            if (this.sshClient != null) {
                this.sshClient.disconnect();
            }
            if (this.socketTransport != null) {
                this.socketTransport.close();
            }
        } catch (final IOException exc) {
            exc.printStackTrace();
        }
    }

    public final String executeCommand(final String command) throws Exception {
        sendCommand(command);
        final String outputCmd = readOutputCommand(this.commandPrompt);
        return outputCmd;
    }

    protected final void sendCommand(final String command) throws Exception {
        this.stdIn.write(command.concat("\n").getBytes());
        this.stdIn.flush();
    }

    protected final String readOutputCommand(final Pattern expectedPattern) throws Exception {
        final StringBuilder output = new StringBuilder();
        final byte[] buffer = new byte[1024];
        int readBytesNum = 0;
        final StringBuilder matchString = new StringBuilder();
        while (true) {
            readBytesNum = this.stdOut.read(buffer);
            if (readBytesNum == -1) {
                throw new Exception("The stdout stream is closed - output: \"" + output + "\"" + output.toString());
            }
            final String currentChunk = new String(buffer, 0, readBytesNum, StandardCharsets.UTF_8);
            output.append(currentChunk);

            matchString.append(currentChunk);

            /* If the expected string is found in stdout buffer */
            if (findPattern(matchString, expectedPattern)) {
                return output.toString();
            }
            matchString.setLength(0);
            matchString.append(currentChunk);
        }

    }

    protected final boolean findPattern(final StringBuilder commadOutput, final Pattern pattern) {
        return pattern.matcher(commadOutput).find();
    }

    public static String getFilter(final String id) {
        final String filter = "<rpc message-id=\"1\" xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">"
                + "<get>           <filter type=\"subtree\"><ManagedElement xmlns=\"urn:com:ericsson:ecim:SgsnMmeTop\"><managedElementId>"
                + id
                + "</managedElementId><SystemFunctions xmlns=\"urn:com:ericsson:ecim:SgsnMmeTop\"><systemFunctionsId>1</systemFunctionsId><SwInventory xmlns=\"urn:com:ericsson:ecim:SgsnMmeSwIM\"><swInventoryId>1</swInventoryId></SwInventory></SystemFunctions></ManagedElement></filter> </get>"
                + "</rpc>" + "]]>]]>";
        return filter;
    }

    public static String getCustomFiltver(final String id) {
        final String filter = "<rpc message-id=\"1\" xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">" + "<get>" + "<filter type=\"subtree\">"
                + "<ManagedElement xmlns=\"urn:com:ericsson:ecim:ComTop\"><managedElementId>" + id + "</managedElementId>"
                + "<SystemFunctions xmlns=\"urn:com:ericsson:ecim:ComTop\"><systemFunctionsId>1</systemFunctionsId>"
                + "<Fm xmlns=\"urn:com:ericsson:ecim:ComFm\"> <fmId>1</fmId>" + "<FmAlarmModel><fmAlarmModelId>1</fmAlarmModelId>"
                + "<FmAlarmType><fmAlarmTypeId>CriticalTemperatureTakenOutofService</fmAlarmTypeId>" + "<majorType/>" + "<minorType/>"
                + "</FmAlarmType></FmAlarmModel></Fm></SystemFunctions></ManagedElement></filter> </get>" + "</rpc>" + "]]>]]>";

        return filter;
    }

    public static String getAction(final actions action, final String id) {
        final String head = "<rpc message-id=\"039\">";
        final String start = "<action xmlns=\"urn:com:ericsson:ecim:1.0\"><data><ManagedElement xmlns=\"urn:com:ericsson:ecim:SgsnMmeTop\"><managedElementId>"
                + id + "</managedElementId>";
        final String end = "</ManagedElement></data></action></rpc>]]>]]>";

        switch (action) {
            case ACTION_loadLicense:
                return head + start + "<SgsnMme xmlns=\"urn:com:ericsson:ecim:Sgsn_Mme\"><sgsnMmeId>1</sgsnMmeId>"
                        + "<NE><neId>1</neId><loadLicense><lkfName>sgsnmmelicence</lkfName></loadLicense></NE></SgsnMme>" + end;

            case ACTION_deleteRas:
                return head + start + "<SgsnMme xmlns=\"urn:com:ericsson:ecim:Sgsn_Mme\"><sgsnMmeId>1</sgsnMmeId>"
                        + "<NE><neId>1</neId><deleteRas></deleteRas></NE></SgsnMme>" + end;

            case ACTION_dss7SysName:
                return head + start + "<SgsnMme xmlns=\"urn:com:ericsson:ecim:Sgsn_Mme\"><sgsnMmeId>1</sgsnMmeId>"
                        + "<Ss7Sys><ss7SysName>1</ss7SysName><restart_febe>"
                        + "<ss7Process>all</ss7Process><equipmentPositionOptional>2.5</equipmentPositionOptional><ss7RestartId>123</ss7RestartId>"
                        + "</restart_febe></Ss7Sys></SgsnMme>" + end;

            case ACTION_sctpSys:
                return head
                        + start
                        + "<SgsnMme xmlns=\"urn:com:ericsson:ecim:Sgsn_Mme\"><sgsnMmeId>1</sgsnMmeId>"
                        + "<SctpSys><sctpSysName>1</sctpSysName><ton><sctpModule>sctp</sctpModule><equipmentPositionOptional>2.5</equipmentPositionOptional><timerTrace>true</timerTrace><traceLevel>info</traceLevel><sctpId>all</sctpId>"
                        + "<messageTrace>true</messageTrace></ton></SctpSys></SgsnMme>" + end;

                // After this action is run, SgsnMmeSwM:UpgradePackage=1 MO will be removed. To re test create this MO again.  
            case ACTION_removeUpgradePackage:
                return head + start
                        + "<SystemFunctions><systemFunctionsId>1</systemFunctionsId><SwM xmlns=\"urn:com:ericsson:ecim:SgsnMmeSwM\"><swMId>1</swMId>"
                        + "<removeUpgradePackage><upgradePackage>ManagedElement=" + id + ",SystemFunctions=1,SwM=1,UpgradePackage=1</upgradePackage>"
                        + "</removeUpgradePackage></SwM></SystemFunctions>" + end;

            case ACTION_createUpgradePackage:
                return head + start + "<SystemFunctions><systemFunctionsId>1</systemFunctionsId><SwM xmlns=\"urn:com:ericsson:ecim:SgsnMmeSwM\">"
                        + "<swMId>1</swMId><createUpgradePackage><uri>ftp://users@14BCP04</uri><password>password</password></createUpgradePackage>"
                        + "</SwM></SystemFunctions>" + end;

            case ACTION_createBackup:
                return head
                        + start
                        + "<SystemFunctions><systemFunctionsId>1</systemFunctionsId><BrM xmlns=\"urn:com:ericsson:ecim:SgsnMmeBRM\">"
                        + "<brMId>1</brMId><BrmBackupManager><brmBackupManagerId>1</brmBackupManagerId><createBackup><name>filename</name></createBackup></BrmBackupManager>"
                        + "</BrM></SystemFunctions>" + end;

            default:
                break;
        }
        return "";
    }

    public static String getNetconfCommandStruct() {
        return "ipAddress";

    }

    public static String getNetconfCommand(final String body) {
        final String start = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rpc message-id=\"039\" xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">";
        final String end = "</rpc>]]>]]>";
        final String target_candidate = "<target><candidate/></target>";
        return start + "<edit-config>" + target_candidate + "<config xmlns:xc=\"urn:ietf:params:xml:ns:netconf:base:1.0\">" + body
                + "</config></edit-config>" + end;

    }

    public static String getNetconfCommands(final netconf_operations operation, final String id) {
        final String start = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rpc message-id=\"039\" xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">";
        final String end = "</rpc>]]>]]>";
        final String target_candidate = "<target><candidate/></target>";
        final String source_candidate = "<source><candidate/></source>";
        final String ATTR_VALUE = "4";

        switch (operation) {
            case LOCK:
                return start + "<lock>" + target_candidate + "</lock>" + end;

            case EDIT_CONFIG_DELETE:
                return start + "<edit-config>" + target_candidate
                        + "<config xmlns:xc=\"urn:ietf:params:xml:ns:netconf:base:1.0\"><ManagedElement xmlns=\"urn:com:ericsson:ecim:SgsnMmeTop\">"
                        + "<managedElementId>" + id
                        + "</managedElementId><SgsnMme xmlns=\"urn:com:ericsson:ecim:Sgsn_Mme\"><sgsnMmeId>1</sgsnMmeId><Dt xc:operation=\"delete\">"
                        + "<dtName>1</dtName></Dt></SgsnMme></ManagedElement></config></edit-config>" + end;

            case EDIT_CONFIG_CREATE:
                return start + "<edit-config>" + target_candidate
                        + "<config xmlns:xc=\"urn:ietf:params:xml:ns:netconf:base:1.0\"><ManagedElement xmlns=\"urn:com:ericsson:ecim:SgsnMmeTop\">"
                        + "<managedElementId>" + id
                        + "</managedElementId><SgsnMme xmlns=\"urn:com:ericsson:ecim:Sgsn_Mme\"><sgsnMmeId>1</sgsnMmeId><Dt xc:operation=\"create\">"
                        + "<dtName>1</dtName></Dt></SgsnMme></ManagedElement></config></edit-config>" + end;

            case EDIT_CONFIG_UPDATE:
                return start + "<edit-config>" + target_candidate
                        + "<config xmlns:xc=\"urn:ietf:params:xml:ns:netconf:base:1.0\"><ManagedElement xmlns=\"urn:com:ericsson:ecim:SgsnMmeTop\">"
                        + "<managedElementId>" + id + "</managedElementId><SgsnMme xmlns=\"urn:com:ericsson:ecim:Sgsn_Mme\"><sgsnMmeId>1</sgsnMmeId>"
                        + "<Qos xc:operation=\"merge\"><qoSName>1</qoSName><qoSIpDscpGtpCValue>" + ATTR_VALUE + "</qoSIpDscpGtpCValue></Qos>"
                        + "</SgsnMme></ManagedElement></config></edit-config>" + end;

            case EDIT_CONFIG_UPDATE_SEQUENCE:
                return start + "<edit-config>" + target_candidate + "<config xmlns:xc=\"urn:ietf:params:xml:ns:netconf:base:1.0\">"
                        + "<ManagedElement xmlns=\"urn:com:ericsson:ecim:ComTop\"><managedElementId>" + id + "</managedElementId>"
                        + "<SystemFunctions><systemFunctionsId>1</systemFunctionsId>"
                        + "<SecM xmlns=\"urn:com:ericsson:ecim:ComSecM\"><secMId>1</secMId>"
                        + "<UserManagement xc:operation=\"merge\"><userManagementId>1</userManagementId><targetType>z</targetType>"
                        + "</UserManagement>" + "</SecM>" + "</SystemFunctions></ManagedElement>" + "</config></edit-config>" + end;

            case EDIT_CONFIG_UPDATE_SEQENCE_OF_STRUCT:
                return start + "<edit-config>" + target_candidate + "<config xmlns:xc=\"urn:ietf:params:xml:ns:netconf:base:1.0\">"
                        + "<ManagedElement xmlns=\"urn:com:ericsson:ecim:ComTop\">" + "<managedElementId>" + id + "</managedElementId>"
                        + "<SystemFunctions>" + "<systemFunctionsId>1</systemFunctionsId>" + "<SysM xmlns=\"urn:com:ericsson:ecim:ComSysM\">"
                        + "<sysMId>1</sysMId>" + "<Snmp xmlns=\"urn:com:ericsson:ecim:ComSnmp\" xc:operation=\"merge\">" + "<snmpId>1</snmpId>"
                        + "<agentAddress><port>0</port><host>abc</host></agentAddress>"
                        + "<agentAddress><port>1</port><host>xyz</host></agentAddress>" + "</Snmp></SysM></SystemFunctions></ManagedElement>"
                        + "</config></edit-config>" + end;

            case EDIT_CONFIG_COMPLEX_CREATE:
                //                return start
                //                        + "<edit-config>"
                //                        + target_candidate
                //                        + "<config xmlns:xc=\"urn:ietf:params:xml:ns:netconf:base:1.0\">"
                //                        + "<ManagedElement xmlns=\"urn:com:ericsson:ecim:SgsnMmeTop\"><managedElementId>"
                //                        + id
                //                        + "</managedElementId>"
                //                        + "<SystemFunctions><systemFunctionsId>1</systemFunctionsId>"
                //                        + "<BrM xmlns=\"urn:com:ericsson:ecim:SgsnMmeBRM\"><brMId>1</brMId>"
                //                        + "<BrmBackupManager><brmBackupManagerId>1</brmBackupManagerId><BrmBackupScheduler xc:operation=\"merge\">"
                //                        + "<scheduledBackupName>BACKUPER</scheduledBackupName><progressReport><actionName>testing</actionName><additionalInfo/><progressInfo/>"
                //                        + "<progressPercentage/><result>1</result><resultInfo/><state>1</state><actionId/><timeActionStarted/><timeActionCompleted/><timeOfLastStatusUpdate/>"
                //                        + "</progressReport>" + "<brmBackupSchedulerId>1</brmBackupSchedulerId></BrmBackupScheduler></BrmBackupManager></BrM>"
                //                        + "</SystemFunctions></ManagedElement>" + "</config></edit-config>" + end;

                return start
                        + "<edit-config>"
                        + target_candidate
                        + "<config xmlns:xc=\"urn:ietf:params:xml:ns:netconf:base:1.0\">"
                        + "<ManagedElement xmlns=\"urn:com:ericsson:ecim:SgsnMmeTop\"><managedElementId>"
                        + id
                        + "</managedElementId>"
                        + "<SystemFunctions><systemFunctionsId>1</systemFunctionsId>"
                        + "<BrM xmlns=\"urn:com:ericsson:ecim:SgsnMmeBRM\"><brMId>1</brMId>"
                        + "<BrmBackupManager><brmBackupManagerId>1</brmBackupManagerId><BrmBackupScheduler xc:operation=\"merge\">"
                        + "<scheduledBackupName>BACKUPER</scheduledBackupName><progressReport><actionName>testing</actionName><additionalInfo/><progressInfo/>"
                        + "<progressPercentage/><result>1</result><resultInfo/><state>1</state><actionId/><timeActionCompleted/><timeOfLastStatusUpdate/>"
                        + "</progressReport>" + "<brmBackupSchedulerId>1</brmBackupSchedulerId></BrmBackupScheduler></BrmBackupManager></BrM>"
                        + "</SystemFunctions></ManagedElement>" + "</config></edit-config>" + end;

            case VALIDATE:
                return start + "<validate>" + source_candidate + "</validate>" + end;

            case COMMIT:
                return start + "<commit/>" + end;

            case UNLOCK:
                return start + "<unlock>" + target_candidate + "</unlock>" + end;

            case HELLO:
                return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><hello xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">"
                        + "<capabilities><capability>urn:ietf:params:netconf:base:1.0</capability></capabilities></hello>]]>]]>";

            case CLOSE:
                return start + "<close-session/>" + end;

            case GET:
                return start + "<get/>" + end;

            default:
                break;
        }
        return "";
    }
}

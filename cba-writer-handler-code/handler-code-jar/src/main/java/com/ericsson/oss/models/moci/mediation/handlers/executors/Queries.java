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
package com.ericsson.oss.models.moci.mediation.handlers.executors;

public class Queries {

    public static String HELLO_QUERY = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<hello xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">"
            + "<capabilities><capability>urn:ietf:params:netconf:base:1.0</capability></capabilities></hello>]]>]]>";

    public static String getConfigQuery(final String messageId, final String targatDataBase) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<rpc message-id=\"" + messageId
                + "\" xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">" + "<get-config><source><" + targatDataBase + "/></source></get-config>"
                + "</rpc>]]>]]>";
    }

    public static String performMOAction(final String messageId, final String managedElementId) {

        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<rpc message-id=\""
                + messageId
                + "\" xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">"
                + "<action><data><ManagedElement xmlns=\"urn:com:ericsson:ecim:SgsnMmeTop\"><managedElementId>"
                + managedElementId
                + "</managedElementId><SgsnMme xmlns=\"urn:com:ericsson:ecim:Sgsn_Mme\"><sgsnMmeId>1</sgsnMmeId><NE><neId>1</neId><deleteRas></deleteRas></NE></SgsnMme></ManagedElement></data></action></rpc>]]>]]>";
    }

    public static String getReplyQuery(final String messageId) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<rpc-reply xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\" message-id=\""
                + messageId + "\"></data></rpc-reply>]]>]]>";
    }

    public static String noRpcReply(final String messageId) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<rpc-reply  xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\" message-id=\""
                + messageId + "\"></data></rpc-reply>]]>]]>";
    }

    public static final String getWithSubtreeFilter(final String messageId, final String targetDataBase, final String config) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<rpc message-id=\"" + messageId
                + "\" xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">" + "<get>" + "<filter type=\"subtree\">" + config
                + "</filter></get></rpc>]]>]]>";
    }

    public static final String getEditConfig(final String messageId, final String targetDataBase, final String config) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<rpc message-id=\"" + messageId
                + "\" xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">" + "<edit-config><target><" + targetDataBase
                + "/></target><config xmlns:xc=\"urn:ietf:params:xml:ns:netconf:base:1.0\">" + config + "</config></edit-config></rpc>]]>]]>";
    }

    public static String getEditConfigQueryWithRollbackOnError(final String messageId, final String targetDataBase, final String config) {
        return "<rpc message-id=\"" + messageId + "\" xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">" + "<edit-config><target><" + targetDataBase
                + "/></target><error-option>rollback-on-error</error-option><config>" + config + "</config>" + "</edit-config>" + "</rpc>]]>]]>";
    }

    public static String getEditConfigQuery(final String messageId, final String targetDataBase, final String config) {
        return "<rpc message-id=\"" + messageId + "\" xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">" + "<edit-config><target><" + targetDataBase
                + "/></target><config>" + config + "</config>" + "</edit-config>" + "</rpc>]]>]]>";
    }

    public static String copyConfigQuery(final String messageId, final String url) {
        // https://user:password@example.com/cfg/new.txt
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rpc message-id=\"" + messageId
                + "\" xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\"> <copy-config> <target> <running/> </target> <source> <url>" + url
                + "</url> </source> </copy-config> </rpc>]]>]]>";
    }

    public static String deleteConfigOperation(final String messageId, final String targetDataBase) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rpc message-id=\"" + messageId
                + "\" xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\"> <delete-config> <target> <" + targetDataBase
                + "/> </target> </delete-config> </rpc>]]>]]>";
    }

    public static String unlock(final String messageId, final String targetDataBase) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rpc message-id=\"" + messageId
                + "\" xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\"> <unlock> <target> <" + targetDataBase
                + "/> </target> </unlock> </rpc>]]>]]>";
    }

    public static String lockOperation(final String messageId, final String targetDataBase) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rpc message-id=\"" + messageId
                + "\" xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\"> <lock> <target> <" + targetDataBase + "/> </target> </lock> </rpc>]]>]]>";
    }

    public static String getOperation(final String messageId) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rpc message-id=\"" + messageId
                + "\" xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\"><get/></rpc>]]>]]>";
    }

    public static String closeSession(final String messageId) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rpc message-id=\"" + messageId
                + "\" xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\"><close-session/></rpc>]]>]]>";
    }

    public static String validate(final String messageId, final String sourceDataBase) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rpc message-id=\"" + messageId
                + "\" xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\"><validate><source><" + sourceDataBase + "/></source></validate></rpc>]]>]]>";
    }

    public static String killSession(final String sessionId) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rpc message-id=\"101\" xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\"><kill-session><session-id>"
                + sessionId + "</session-id></kill-session></rpc>]]>]]>";
    }

    public static String commit(final String messageId) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<rpc xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\" message-id=\"" + messageId
                + "\"><commit/></rpc-reply>]]>]]>";
    }

    public static String confirmCommit(final String messageId) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<rpc-reply xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\" message-id=\""
                + messageId + "\"><commit><confirmed/></commit></rpc-reply>]]>]]>";
    }

    public static String cancelCommit(final String messageId) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<rpc-reply xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\" message-id=\""
                + messageId + "\"><cancel-commit/></rpc-reply>]]>]]>";
    }

}

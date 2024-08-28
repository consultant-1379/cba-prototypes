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
package com.ericsson.oss.mediation.adapter.ssh.api;

import javax.resource.spi.ConnectionRequestInfo;

/**
 * SSHSessionRequest
 * 
 * 
 */
public class SSHSessionRequest implements ConnectionRequestInfo {

    private final String ipAddress;
    private final int port;
    private final String username;
    private final String password;
    private final int waitTimeForResponse;
    private final String commandPromptPattern;
    private final String endCommand;
    private final String maxSessionAuthReachedMsgPattern;
    private final String patternPrefix;
    private final String patternSuffix;
    private final String subsystem;

    private SSHSessionRequest(final SSHSessionRequestBuilder builder) {
        this.ipAddress = builder.ipAddress;
        this.port = builder.port;
        this.username = builder.username;
        this.password = builder.password;
        this.waitTimeForResponse = builder.waitTimeForResponse;
        this.commandPromptPattern = builder.commandPromptPattern;
        this.endCommand = builder.endCommand;
        this.maxSessionAuthReachedMsgPattern = builder.maxSessionAuthReachedMsgPattern;
        this.patternPrefix = builder.patternPrefix;
        this.patternSuffix = builder.patternSuffix;
        this.subsystem = builder.subsystem;
    }

    /**
     * @return ipAddress
     */
    public String getIpAddress() {
        return this.ipAddress;
    }

    /**
     * @return port number
     */
    public int getPort() {
        return this.port;
    }

    /**
     * @return username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * @return responsetimeout
     */
    public int getWaitTimeForResponse() {
        return this.waitTimeForResponse;
    }

    /**
     * @return commandPromptPattern
     */
    public String getCommandPromptPattern() {
        return this.commandPromptPattern;
    }

    public String getEndCommand() {
        return endCommand;
    }

    /**
     * @return maxSessionAuthReachedMsgPattern
     */
    public String getMaxSessionAuthReachedMsgPattern() {
        return this.maxSessionAuthReachedMsgPattern;
    }

    public String getPatternPrefix() {
        return patternPrefix;
    }

    public String getPatternSuffix() {
        return patternSuffix;
    }

    public String getSubsystem() {
        return subsystem;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return super.toString() + "\n\tIp Address: " + this.ipAddress + "\n\tPort: " + this.port + "\n\tUsername: " + this.username
                + "\n\tWait Time For Response: " + this.waitTimeForResponse + "\n\tCommand Prompt Pattern: " + this.commandPromptPattern;
    }

    /**
     * Returns a hash code value for the object.
     * 
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        int result = 17;

        result += 31 * result + (this.ipAddress != null ? this.ipAddress.hashCode() : 0);
        result += 31 * result + String.valueOf(this.port).hashCode();

        return result;
    }

    /**
     * Indicates whether some other object is equal to this one.
     * 
     * @param other
     *            The reference object with which to compare.
     * @return true if this object is the same as the obj argument, false otherwise.
     */
    @Override
    public boolean equals(final Object other) {
        if (other == null || (!(other instanceof SSHSessionRequest))) {
            return false;
        }

        final SSHSessionRequest obj = (SSHSessionRequest) other;

        if (checkCredentials(this, obj)) {
            return checkSubsystem(this, obj);
        }
        return false;

    }

    /**
     * Checks for destination address and credentials
     * 
     * @param request1
     * @param request2
     * @return
     */
    private boolean checkCredentials(final SSHSessionRequest request1, final SSHSessionRequest request2) {
        if ((request1.ipAddress != null && request1.ipAddress.equalsIgnoreCase(request2.ipAddress)) && request1.port == request2.port
                && (request1.username != null && request1.username.equals(request2.username))
                && (request1.password != null && request1.password.equals(request2.password))) {

            return true;
        }
        return false;
    }

    /**
     * Check the subsystem when available
     * 
     * @param request1
     * @param request2
     * @return
     */
    private boolean checkSubsystem(final SSHSessionRequest request1, final SSHSessionRequest request2) {
        if (request1.subsystem != null && !request1.subsystem.isEmpty() && !request1.subsystem.equals(request2.subsystem)) {
            return false;
        }
        return true;
    }

    public static class SSHSessionRequestBuilder {

        private final String ipAddress;
        private int port = 22;
        private final String username;
        private final String password;
        private final int waitTimeForResponse;
        private final String commandPromptPattern;
        private final String endCommand;
        private String maxSessionAuthReachedMsgPattern;
        private String patternPrefix;
        private String patternSuffix;
        private String subsystem;

        public SSHSessionRequestBuilder(final String ipAddress, final String username, final String password, final int waitTimeForResponse,
                                        final String commandPromptPattern, final String endCommand) {
            this.ipAddress = ipAddress;
            this.username = username;
            this.password = password;
            this.waitTimeForResponse = waitTimeForResponse;
            this.commandPromptPattern = commandPromptPattern;
            this.endCommand = endCommand;

        }

        public SSHSessionRequestBuilder port(final int port) {
            this.port = port;
            return this;
        }

        public SSHSessionRequestBuilder maxSessionAuthReachedMsgPattern(final String maxSessionAuthReachedMsgPattern) {
            this.maxSessionAuthReachedMsgPattern = maxSessionAuthReachedMsgPattern;
            return this;
        }

        public SSHSessionRequestBuilder subsystem(final String subsystem) {
            this.subsystem = subsystem;
            return this;
        }

        public SSHSessionRequestBuilder patternPrefixAndSuffix(final String patternPrefix, final String patternSuffix) {
            this.patternPrefix = patternPrefix;
            this.patternSuffix = patternSuffix;
            return this;
        }

        public SSHSessionRequest buildSshSessionRequest() {
            return new SSHSessionRequest(this);
        }

    }

}

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

public class SgsnMMECredentialHolder implements CredentialHolder {

    String ipAddress = "";

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.cba.prototype.protocol.netconf.CredentialHolder#getIPAddress ()
     */
    @Override
    public String getIPAddress() {
        return ipAddress;
    }

    public void setIPAddress(final String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.cba.prototype.protocol.netconf.CredentialHolder#getUserName ()
     */
    @Override
    public String getUserName() {
        return "sovereign";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.cba.prototype.protocol.netconf.CredentialHolder#getPassword ()
     */
    @Override
    public String getPassword() {
        return "sovereign";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.cba.prototype.protocol.netconf.CredentialHolder#getPort()
     */
    @Override
    public int getPort() {
        return 22;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.cba.prototype.protocol.netconf.CredentialHolder#getChannelType ()
     */
    @Override
    public String getChannelType() {
        return "subsystem";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.cba.prototype.protocol.netconf.CredentialHolder#getSessionType ()
     */
    @Override
    public String getChannelName() {
        return "netconf";
    }

}

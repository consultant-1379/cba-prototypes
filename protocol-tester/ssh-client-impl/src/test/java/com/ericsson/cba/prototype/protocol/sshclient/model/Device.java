/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2013
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.cba.prototype.protocol.sshclient.model;

import java.util.List;

public class Device {

    public static void print(final List<Device> deviceList) {
        System.out.println("Available device are : ");
        int index = 1;
        for (final Device device : deviceList) {
            System.out.println(index + ")." + device.toString());
            index++;
        }
    }

    private final String userName;
    private final String password;
    private final String hostName;
    private final String ipName;

    private final int port;

    public Device(final String userName, final String password, final String hostName, final String ipName, final int port) {
        super();
        this.userName = userName;
        this.password = password;
        this.hostName = hostName;
        this.ipName = ipName;
        this.port = port;
    }

    public String getHostName() {
        return hostName;
    }

    public String getIp() {
        return ipName;
    }

    public String getPassword() {
        return password;
    }

    public int getPort() {
        return port;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "Device [userName=" + userName + ", password=" + password + ", hostName=" + hostName + ", ip=" + ipName + ", port=" + port + "]";
    }

}

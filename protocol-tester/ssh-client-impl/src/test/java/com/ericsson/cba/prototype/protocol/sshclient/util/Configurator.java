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
package com.ericsson.cba.prototype.protocol.sshclient.util;

import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;

import com.ericsson.cba.prototype.protocol.sshclient.model.Device;

public class Configurator {
    public static void main(final String[] args) {
        new Configurator().getAvailableDevices();
    }

    private final Properties props = new Properties();

    public Configurator() {
        loadParams();
    }

    private void addToDeviceList(final Entry<Object, Object> entry, final List<Device> deviceList) {
        final String hostName = entry.getValue().toString();
        final String ipName = props.getProperty(hostName + ".ip");
        final String port = props.getProperty(hostName + ".port");
        if (!isNumber(port)) {
            System.out.println("port should be number");
            return;
        }
        final String userName = props.getProperty(hostName + ".username");
        final String password = props.getProperty(hostName + ".password");
        if (isValid(userName, password, hostName, port, ipName)) {
            deviceList.add(new Device(userName, password, hostName, ipName, Integer.parseInt(port)));
        }

    }

    public List<Device> getAvailableDevices() {
        final List<Device> deviceList = new ArrayList<Device>();
        for (final Entry<Object, Object> entry : props.entrySet()) {
            if (entry.getKey().toString().contains("device")) {
                addToDeviceList(entry, deviceList);
            }
        }
        return deviceList;
    }

    public String getProperty(final String key) {
        return props.getProperty(key);
    }

    private boolean isNumber(final String port) {
        try {
            Integer.parseInt(port);
        } catch (final Exception e) {
            return false;
        }
        return true;
    }

    private boolean isValid(final String userName, final String password, final String hostName, final String port, final String ipName) {
        return (hostName != null) && (port != null) && !port.equalsIgnoreCase("") && (ipName != null) && !ipName.equalsIgnoreCase("")
                && (password != null) && !password.equalsIgnoreCase("") && (userName != null) && !userName.equalsIgnoreCase("");
    }

    private void loadParams() {
        InputStream inputStream = null;
        try {
            if (inputStream == null) {
                inputStream = Configurator.class.getClassLoader().getResourceAsStream("available.device.list");
            }
            props.load(inputStream);
        } catch (final Exception e) {
        }
    }

}

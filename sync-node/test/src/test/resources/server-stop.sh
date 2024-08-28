#!/bin/sh

kill -9 `ps -ef | grep -i "${netconf.server.jar.name}" | awk '{print $2}'`
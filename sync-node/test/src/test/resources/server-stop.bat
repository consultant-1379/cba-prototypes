@ECHO OFF

wmic process where (commandline like "%%${netconf.server.jar.name}%%" and not name="wmic.exe") delete
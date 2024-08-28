#!/bin/sh
# ********************************************************************
# Ericsson Radio Systems AB                                     SCRIPT
# ********************************************************************
#
#
# (c) Ericsson Radio Systems AB 2012 - All rights reserved.
#
# The copyright to the computer program(s) herein is the property
# of Ericsson Radio Systems AB, Sweden. The programs may be used 
# and/or copied only with the written permission from Ericsson Radio 
# Systems AB or in accordance with the terms and conditions stipulated 
# in the agreement/contract under which the program(s) have been 
# supplied.
#
# ********************************************************************
# Name    : tunne_doozers.bsh
# Date    : 09/04/2014
# Author  : ERANAGG
# Purpose : This script will Setup/Exit a tunnel against cloud gateway based on user request.
# Note    : Please make sure you have Shrew soft VPN client installed to run this script. 
#			Also, config_file provided for the gateway is correct.
#
# Usage   : tunne_doozers.bsh
#
# ********************************************************************
#

#   Configuration Section
#
# ********************************************************************
#

OS=`uname -s`
vpn_path_windows="C:\Program Files\ShrewSoft\VPN Client\ipsecc"

#
# ********************************************************************
#
#   Utility Functions
#
# ********************************************************************
#

### Function: abort_script ###
#
#   This will is called if the script is aborted through an error
#   error signal sent by the kernel such as CTRL-C or if an
#   error is encountered during runtime
#
# Arguments:
#       $1 - Error message from part of program
# Return Values:
#       none

abort_script()
{
if [ "$1" ]; then
    echo "$1"
    exit 1
else
    echo "Script aborted.......\n"
    exit 1
fi
}

### Function: usage_msg ###
#
#   Print out the usage message
#
# Arguments:
#   none
# Return Values:
#   none

usage_msg() 
{
$CLEAR
echo "
Usage: $0 -a <setUp|exit> -g <gatewayConfigFileName>
                      
options:

-a	: Action. Must be one of the following:
              setUp --> Setup the tunnel connection towards cloud gateway
              exit  --> Exit the connection setUp earlier if any.
-g	: Gateway. Represents the config_file required to set up tunnel. Format:<gateway_name.vpn>
              
"
}

#
# ********************************************************************
#
# 	Main body of program
#
# ********************************************************************
#

#
#Get user input
#
while getopts ":a:g:" arg; do
  case $arg in
    a) ACTION="$OPTARG"
       ;;
    g) GATEWAY="$OPTARG"
       ;;
   \?) usage_msg
       exit 1
       ;;
  esac
done
shift `expr $OPTIND - 1`

#
# Check Input Params
#

if [ ! "$ACTION" -o ! "$GATEWAY" ]; then
	usage_msg
	exit 1
fi

#
#Execute action
#
if [ "$ACTION" = 'setUp' ]; then
	if [ "$OS" = 'MINGW32_NT-6.1' ]; then
		echo "It's a windows env. Setting up tunnel to Cloud Gateway"
		"$vpn_path_windows" -r "$GATEWAY" -a
	else
		echo "It's  a Linux env. Setting up tunnel to Cloud Gateway"
		ikec -r "$GATEWAY" -a
	fi
elif [ "$ACTION" = 'exit' ]; then
	echo "Exiting tunnel"
	if [ "$OS" = 'MINGW32_NT-6.1' ]; then
		echo "Exiting Tunnel in windows"
		kill -9 `ps -ef | grep -i ipsecc | awk '{print $2}'`
		if [ $? -ne 0 ]; then
			_err_msg_="Not able to exit the tunnel. Please make sure there is tunnel setup. If yes, exit manually."
	    	abort_script "$_err_msg_"
	    fi
	else
		echo "Exiting Tunnel in Linux"
		kill -9 `ps -ef | grep -i ikec | grep -i "$GATEWAY" | awk '{print $2}'`
		if [ $? -ne 0 ]; then
			_err_msg_="Not able to exit the tunnel. Please make sure there is tunnel setup. If yes, exit manually."
	    	abort_script "$_err_msg_"
	    fi
	fi
else
	_err_msg_="Action Not supported. Valid actions are setUp or exit. Check Usage for more information."
	abort_script "$_err_msg_"
fi
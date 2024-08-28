package com.ericsson.oss.mediation.pm.netconf.contentHandler;

public interface NsPrefixGenerator {
	String newPrefix();
	void reset();
}

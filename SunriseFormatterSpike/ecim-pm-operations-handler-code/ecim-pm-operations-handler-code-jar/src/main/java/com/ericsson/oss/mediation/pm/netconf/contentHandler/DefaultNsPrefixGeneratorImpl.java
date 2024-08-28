package com.ericsson.oss.mediation.pm.netconf.contentHandler;

public class DefaultNsPrefixGeneratorImpl implements NsPrefixGenerator {

	private int number = 0;
	private final String basePrefix;
	
	public DefaultNsPrefixGeneratorImpl(final String basePrefix) {
		super();
		this.number = 0;
		this.basePrefix = basePrefix;
	}

	@Override
	public String newPrefix() {
		return String.format("%s%d", basePrefix, number++);
	}

	@Override
	public void reset() {
		this.number = 0;
	}

}

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
package com.ericsson.oss.mediation.util.transport.jca;

import javax.resource.ResourceException;

import javax.resource.spi.ManagedConnectionMetaData;

public class RAManagedConnectionMetaData implements ManagedConnectionMetaData {

	public RAManagedConnectionMetaData() {
	}

	@Override
	public String getEISProductName() throws ResourceException {
		return "HelloWorld Resource Adapter";
	}

	@Override
	public String getEISProductVersion() throws ResourceException {
		return "1.0";
	}

	@Override
	public int getMaxConnections() throws ResourceException {
		return 50;
	}

	@Override
	public String getUserName() throws ResourceException {
		return null;
	}

}

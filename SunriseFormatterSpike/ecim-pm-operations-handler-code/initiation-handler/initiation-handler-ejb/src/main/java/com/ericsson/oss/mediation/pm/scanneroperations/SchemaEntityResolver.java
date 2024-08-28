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
package com.ericsson.oss.mediation.pm.scanneroperations;

import java.io.File;
import java.io.IOException;

import org.xml.sax.*;

public class SchemaEntityResolver implements EntityResolver {
	
	@Override
	public InputSource resolveEntity(String publicId, String systemId)
			throws SAXException, IOException {
		// Imported schemas are located in src/main/resources

		// Grab only the filename part from the full path
		String filename = new File(systemId).getName();

		// Now prepend the correct path
		String correctedId = "src/main/resources/schema/" + filename;

		InputSource is = new InputSource(this.getClass().getResourceAsStream(filename));
		is.setSystemId(correctedId);

		return is;
	}

}

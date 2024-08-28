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
package com.ericsson.oss.mediation.pm.netconf.lineFormat;

import java.io.Writer;

import javax.xml.stream.*;

public class StringFormat extends AttributeTypeFormat {
	private final String value;

	/**
	 * @param value
	 */
	public StringFormat(String value) {
		super();
		this.value = value;
	}

	@Override
	public <T extends Writer> T format(T writer) {
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		try {
			XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(writer);
			XMLEventFactory eventFactory = XMLEventFactory.newInstance();

		    eventWriter.add(eventFactory.createCharacters(value));
			
			eventWriter.close();
		} catch (XMLStreamException e) {
			//TODO 
			e.printStackTrace();
		}
		
		return writer;
	}
	
	
}

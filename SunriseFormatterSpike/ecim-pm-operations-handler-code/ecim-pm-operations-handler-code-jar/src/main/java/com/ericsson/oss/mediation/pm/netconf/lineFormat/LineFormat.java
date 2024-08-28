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
import java.util.*;

import javax.xml.stream.*;

public class LineFormat extends BaseLineFormat {
	
	private final List<LineFormatElement> elements;

	public LineFormat() {
		super();
		elements = new LinkedList<>();
	}
	
	public LineFormat add(final LineFormatElement element) {
		if(element != null) {
			elements.add(element);
		}
		return this;
	}
	
	public LineFormat remove(final LineFormatElement element) {
		if(element != null) {
			elements.remove(element);
		}
		return this;
	}

	@Override
	public <T extends Writer> T format(final T writer) {
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		try {
			XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(writer);

			ListIterator<LineFormatElement> iterator = elements.listIterator();
			while (iterator.hasNext()) {
				LineFormatElement lfe = (LineFormatElement)iterator.next();
				if(lfe != null) {
					lfe.format(writer); 
				}
			}

			eventWriter.close();
		} catch (XMLStreamException e) {
			//TODO 
			e.printStackTrace();
		}
		
		return writer;
	}
}

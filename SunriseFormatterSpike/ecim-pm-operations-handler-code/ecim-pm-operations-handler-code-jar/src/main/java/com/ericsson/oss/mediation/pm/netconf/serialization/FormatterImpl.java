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
package com.ericsson.oss.mediation.pm.netconf.serialization;

import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.xml.sax.SAXException;

import com.ericsson.oss.mediation.pm.netconf.contentHandler.NamespaceDeclarationHandler;

public class FormatterImpl implements Formatter {
	
	@Override
	public <T extends Writer> T format(final Object obj, final T writer) throws JAXBException, SAXException {
		final JAXBContext context = JAXBContext.newInstance(obj.getClass());
		final Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

		final NamespaceDeclarationHandler contentHandler = new NamespaceDeclarationHandler(writer);
		contentHandler.init();

		m.marshal(obj, contentHandler);
		return writer;
	}

}

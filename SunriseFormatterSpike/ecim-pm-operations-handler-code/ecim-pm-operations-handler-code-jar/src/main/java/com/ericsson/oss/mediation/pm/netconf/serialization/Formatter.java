package com.ericsson.oss.mediation.pm.netconf.serialization;

import java.io.Writer;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

public interface Formatter {
	<T extends Writer> T format(Object obj, T writer) throws JAXBException, SAXException;
}

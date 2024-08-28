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

import com.ericsson.oss.mediation.pm.netconf.lineFormat.constants.NetconfOperation;
import com.ericsson.oss.mediation.pm.netconf.serialization.Formattable;

/**
 *     <elementA xmlns="uriX" xmlns:ns0="uriY"> *
 *       <elementB>contentB</elementB>
 *     </elementA>
 *     
 *     There will be:
 *     one LineFormatElement A with:
 *       namespace = "uriY"
 *       name = elementA
 *       operation = NOT_APPLICABLE
 *       elements contains LineFormatElement B
 *       value = null
 *     one LineFormatElement B with:
 *       namespace = "uriY"
 *       name = elementB
 *       operation = NOT_APPLICABLE
 *       elements empty
 *       value = contentB
 */     
public class LineFormatElement extends BaseLineFormat {
	
	private String namespace;
	private String name;
	private NetconfOperation operation;
	private List<LineFormatElement> elements;
	private Formattable value;

	
	public LineFormatElement() {
		super();
		this.operation = NetconfOperation.NOT_APPLICABLE;
		this.elements = new LinkedList<LineFormatElement>();
	}

	/**
	 * @param string
	 * @return
	 */
	public LineFormatElement namespace(String namespace) {
		this.namespace = namespace;
		return this;
	}	
	
	/**
	 * @param string
	 * @return
	 */
	public LineFormatElement name(String name) {
		this.name = name;
		return this;
	}	
	
	/**
	 * @param string
	 * @return
	 */
	public LineFormatElement operation(NetconfOperation operation) {
		this.operation = operation;
		return this;
	}	
	
	/**
	 * @param string
	 * @return
	 */
	public LineFormatElement value(Formattable value) {
		this.value = value;
		return this;
	}	
	
	/**
	 * @return the namespace
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the operation
	 */
	public NetconfOperation getOperation() {
		return operation;
	}

	/**
	 * @return the elements
	 */
	public List<LineFormatElement> getElements() {
		return elements;
	}
	
	/**
	 * @return the value
	 */
	public Formattable getValue() {
		return value;
	}

	public LineFormatElement add(final LineFormatElement element) {
		if(element != null) {
			elements.add(element);
		}
		return this;
	}
	
	public LineFormatElement remove(final LineFormatElement element) {
		if(element != null) {
			elements.remove(element);
		}
		return this;
	}
	
	public LineFormatElement setAttribute(final String namespace, final String name, final Formattable value) {
		add(new LineFormatElement().namespace(namespace).name(name).value(value));
		return this;
	}
	
	public LineFormatElement setAttribute(final String name, final Formattable value) {
		return setAttribute("", name, value);
	}

	@Override
	public <T extends Writer> T format(final T writer) {
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		try {
			XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(writer);
			XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		    
		    //TODO prefix handling
		    eventWriter.add(eventFactory.createStartElement("", namespace, name));
		    
		    if(namespace != null && !namespace.isEmpty()) {
		        eventWriter.add(eventFactory.createNamespace(namespace));
		    }
		    
		    if(operation != NetconfOperation.NOT_APPLICABLE) {
		    	eventWriter.add(eventFactory.createAttribute("xc", "", "operation", operation.toString()));
		    }
		    
		    eventWriter.add(eventFactory.createCharacters(""));
		    
		    if(value != null) {
		    	value.format(writer);		    	
		    }

		    ListIterator<LineFormatElement> iterator = elements.listIterator();
		    while (iterator.hasNext()) {
		    	LineFormatElement lfe = (LineFormatElement)iterator.next();
		    	if(lfe != null) {
		    		lfe.format(writer); 
		    	}
		    }
			
			eventWriter.add(eventFactory.createEndElement("", namespace, name));
			
			eventWriter.close();
		} catch (XMLStreamException e) {
			//TODO 
			e.printStackTrace();
		}
		
		return writer;
	}

}

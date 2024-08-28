package com.ericsson.oss.mediation.pm.netconf.contentHandler;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

/**
 * NamespaceDeclarationHandler is an implementation of a SAX Content Handler
 * so as to customize declaration of namespaces during parsing of a document.
 * The output is written in a Writer.
 * 
 * The customization consists in distributing declarations of namespaces through the document,
 * adding them in the first element which need a specific namespace; this will remove usage of
 * namespace prefixes unless it becomes necessary. For instance a document as:
 *     <elementA xmlns="uriX" xmlns:ns0="uriY">
 *       <elementB>contentB</elementB>
 *       <elementC>
 *         <elementD>contentD</elementD>
 *         <ns0:elementE>
 *           <ns0:elementF>contentF</ns0:elementF>
 *           <ns0:elementG>
 *               <ns0:elementH>contentH</ns0:elementH>
 *           </ns0:elementG>
 *         </ns0:elementE>
 *       </elementC>
 *     </elementA>
 * will become:
 *     <elementA xmlns="uriX">
 *       <elementB>contentB</elementB>
 *       <elementC>
 *         <elementD>contentD</elementD>
 *         <elementE xmlns="uriY" xmlns:ns0="uriX">
 *           <elementF>contentF</elementF>
 *           <elementG>
 *               <elementH>contentH</elementH>
 *           </elementG>
 *         </elementE>
 *       </elementC>
 *     </elementA>
 *     
 * In case usage of prefix can't be avoided, a prefix will be generated as "ns0", "ns1", etc. incrementing 
 * the index during parsing.
 *
 */
public class NamespaceDeclarationHandler extends DefaultHandler2 {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(NamespaceDeclarationHandler.class);
	
	private NsPrefixGenerator nsPrefixGenerator = null;
	private Writer writer;
	private final Stack<String> namespaces;
	private final Map<String, String> prefixMap; //key = uri, value = prefix
	
	/**
	 * 
	 */
	public NamespaceDeclarationHandler() {
		this(new StringWriter());
	}
	
		
	/**Writer
	 * @param writer
	 */
	public NamespaceDeclarationHandler(final Writer writer) {
		super();
		this.writer = writer;
		namespaces = new Stack<String>();
		prefixMap = new HashMap<>(); 
		nsPrefixGenerator = new DefaultNsPrefixGeneratorImpl("ns");
	}

	/**
	 * @return the writer
	 */
	public Writer getWriter() {
		return writer;
	}
	
		
	/**
	 * @param writer the writer to set
	 */
	public void setWriter(final Writer writer) {
		this.writer = writer;
	}


	/**
	 * @param generator the nsPrefixGenerator to set
	 */
	public void setNsPrefixGenerator(final NsPrefixGenerator generator) {
		nsPrefixGenerator = generator;
	}
	
	public void init() {
		nsPrefixGenerator.reset();
		namespaces.clear();
		prefixMap.clear();
		// TODO it would be better to pass as argument namespaces with fixed prefix
		prefixMap.put("urn:ietf:params:xml:ns:netconf:base:1.0", "xc");
		
//		LOGGER.debug(getStatus("RESET"));	
	}

	@Override
	public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) 
			throws SAXException {
//		LOGGER.info("START {} {} {}", localName, qName, uri);
//		for (int i = 0; i < attributes.getLength(); i++) {
//			LOGGER.info("START ATTR {} {} {}", attributes.getQName(i), attributes.getValue(i), attributes.getURI(i));
//		}
		final StringWriter item = new StringWriter();
		
		if(!namespaces.isEmpty() && namespaces.peek().equals(uri)) {
			item.append(String.format("<%s%s", getElementPrefix(uri), localName));
		}
		else {
			final String previousDefaultNs = setDefaultNamespace(uri);

			item.append(String.format("<%s xmlns=\"%s\"", localName, uri));
			
			if(previousDefaultNs != null) {
				item.append(String.format(" xmlns:%s=\"%s\"", getNsDeclarationPrefix(previousDefaultNs),  previousDefaultNs));
			}
		}		
		
		for (int i = 0; i < attributes.getLength(); i++) {
			//skip attribute which is a namespaces itself
			if(isNamespaceAttr(attributes.getQName(i))) {
				continue;
			}
			if(prefixMap.get(attributes.getURI(i)) == null) {
				prefixMap.put(attributes.getURI(i), nsPrefixGenerator.newPrefix());
				item.append(String.format(" xmlns:%s=\"%s\"", getElementPrefix(attributes.getURI(i)),  attributes.getURI(i)));
			}
			item.append(String.format(" %s%s=\"%s\"", getElementPrefix(attributes.getURI(i)), attributes.getLocalName(i), attributes.getValue(i)));
		}
		item.append(">");

		try {
			writer.append(item.toString());
		} catch (IOException e) {
			throw new SAXException(e);
		}
	}

	@Override
	public void characters(final char[] ch, final int start, final int length)
			throws SAXException {
		try {
			writer.append(new String(ch, start, length));
		} catch (IOException e) {
			throw new SAXException(e);
		}
	}

	@Override
	public void endElement(final String uri, final String localName, final String qName)
			throws SAXException {
		//LOGGER.info("END {} {}", localName, uri);
		final StringWriter item = new StringWriter();
		
		if(!namespaces.peek().equals(uri)) {
			if(resetDefaultNamespace() == null) {
				throw new SAXException(String.format("Failed to format element, localName %s uri %s ", localName, uri));
			}
		}
		
		item.append(String.format("</%s%s>", getElementPrefix(uri), localName));		
	
		try {
			writer.append(item.toString());
		} catch (IOException e) {
			throw new SAXException(e);
		}
	}	

	/**
	 * @param qName
	 * @return
	 */
	private boolean isNamespaceAttr(String qName) {
		boolean isNamespace = false;
		
		if(qName != null) {
			final String[] splitted = qName.split(":");
			if(splitted[0].equals("xmlns")) {
				isNamespace = true;
			}
		}
		
		return isNamespace;
	}
	
	/**
	 * @param uri: the namespace uri
	 * @return the namespace prefix followed by colon 
	 * 		   or an empty String in case no prefix is associated to the namespace uri
	 * @throws SAXException in case of null prefix
	 */
	private String getElementPrefix(final String uri) throws SAXException {
		return getPrefix(uri, ":");
	}
	
	/**
	 * @param uri: the namespace uri
	 * @return the namespace prefix 
	 * 		   or an empty String in case no prefix is associated to the namespace uri
	 * @throws SAXException
	 */
	private String getNsDeclarationPrefix(final String uri) throws SAXException {
		return getPrefix(uri, "");
	}	
	
	/**
	 * @param uri: the namespace uri
	 * @param suffix: the suffix to be appended after the returned prefix
	 * @return the namespace prefix followed by a suffix  
	 * 		   or an empty String in case no prefix is associated to the namespace uri
	 * @throws SAXException in case of null prefix
	 */
	private String getPrefix(final String uri, final String suffix) throws SAXException {
		final String prefix = prefixMap.get(uri);
		
		if(prefix == null) {
			throw new SAXException(String.format("Illegal null prefix for uri %s", uri));
		}
		if(prefix.isEmpty()) {
			return prefix;
		}
		else {
			return prefix + suffix;
		}
	}
	
	/**
	 * @param uri: the namespace uri which becomes the new default namespace.
	 * @return the namespace uri used as default till now and for which an endElement
	 * 		   notification has not been received.
	 * 		    
	 */
	private String setDefaultNamespace(final String uri) {
		String previousDefaultNs = null;
		
		if(!namespaces.isEmpty()) {
			//the stack is not empty in case an endElement notification of uri at the top of the stack
			//has not been received. Since this uri stops to be the default namespace, it is necessary
			//to assign a prefix to it.
			previousDefaultNs = namespaces.peek();
			prefixMap.put(previousDefaultNs, nsPrefixGenerator.newPrefix());	
		}

		//uri is the new default namespace: its prefix is an emptyString
		prefixMap.put(uri, "");
		//uri is the new default namespace: it is inserted at the top of the stack
		namespaces.push(uri);

//		LOGGER.debug(getStatus("SET"));
		
		return previousDefaultNs;
	}
	
	private String resetDefaultNamespace() {
		if(namespaces.isEmpty()) {
			return null;
		}
		
		final String previousDefaultNs = namespaces.peek();
		prefixMap.remove(previousDefaultNs);
		namespaces.pop();
		prefixMap.put(namespaces.peek(), "");

//		LOGGER.debug(getStatus("RESET"));
		
		return previousDefaultNs;
		
	}

	private String getStatus(final String msg) {
		final StringBuilder sb = new StringBuilder(msg);
		sb.append("{");
		sb.append("Namespaces Stack : {");
		for (String stackItem : this.namespaces) {
			sb.append(String.format("%s ",stackItem));
		}
		sb.append("}");
		sb.append("prefixMap : {");
		for (Map.Entry<String,String> prefixMapItem : this.prefixMap.entrySet()) {
			sb.append(String.format("[%s = %s] ",prefixMapItem.getKey(),prefixMapItem.getValue()));
		}
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}

}

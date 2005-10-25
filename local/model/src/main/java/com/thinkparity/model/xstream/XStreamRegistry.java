/*
 * Oct 11, 2005
 */
package com.thinkparity.model.xstream;

import com.thinkparity.model.parity.api.document.xml.DocumentVersionXmlTranslator;
import com.thinkparity.model.parity.api.document.xml.DocumentXmlTranslator;
import com.thinkparity.model.parity.api.project.xml.ProjectXmlTranslator;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.project.Project;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.extended.EncodedByteArrayConverter;

/**
 * XStreamRegistry
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class XStreamRegistry {

	/**
	 * Singleton instance of the registry class.
	 */
	private static final XStreamRegistry singleton;

	/**
	 * Synchronization lock for singleton access.
	 */
	private static final Object singletonLock;

	static {
		singleton = new XStreamRegistry();
		singletonLock = new Object();
	}

	/**
	 * Create the converter registry for the xstream instance.
	 * 
	 * @param xStream
	 *            The XStream instance within which to create the registry.
	 * @see XStream#registerConverter(com.thoughtworks.xstream.converters.Converter)
	 */
	public static void createRegistry(final XStream xStream) {
		synchronized(singletonLock) { singleton.registerConverters(xStream); }
	}

	/**
	 * Create an XStreamRegistry [Singleton]
	 */
	private XStreamRegistry() { super(); }

	/**
	 * Register the list of converters required for the parity model library.
	 * <ol>
	 * <li>EncodedByteArrayConverter
	 * <li>ProjectXmlTranslator
	 * <li>DocumentXmlTranslator
	 * <li>DocumentVersionXmlTranslator
	 * 
	 * @param xStream
	 *            The xStream instance to register.
	 */
	private void registerConverters(final XStream xStream) {
		xStream.registerConverter(new EncodedByteArrayConverter());

		xStream.registerConverter(new ProjectXmlTranslator());
		xStream.alias("project", Project.class);

		xStream.registerConverter(new DocumentXmlTranslator());
		xStream.alias("document", Document.class);

		xStream.registerConverter(new DocumentVersionXmlTranslator());
		xStream.alias("documentversion", DocumentVersion.class);
	}
}

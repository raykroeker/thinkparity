/*
 * Oct 11, 2005
 */
package com.thinkparity.model.xstream;

import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentContent;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.io.xml.document.DocumentContentConverter;
import com.thinkparity.model.parity.model.io.xml.document.DocumentConverter;
import com.thinkparity.model.parity.model.io.xml.document.DocumentVersionConverter;
import com.thinkparity.model.parity.model.io.xml.index.Index;
import com.thinkparity.model.parity.model.io.xml.index.IndexConverter;
import com.thinkparity.model.parity.model.io.xml.project.ProjectConverter;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.xmpp.document.XMPPDocument;
import com.thinkparity.model.xmpp.document.XMPPDocumentConverter;

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
	 * <li>ProjectConverter
	 * <li>DocumentConverter
	 * <li>DocumentVersionConverter
	 * 
	 * @param xStream
	 *            The xStream instance to register.
	 */
	private void registerConverters(final XStream xStream) {
		xStream.registerConverter(new EncodedByteArrayConverter());

		xStream.registerConverter(new ProjectConverter());
		xStream.alias("project", Project.class);

		xStream.registerConverter(new DocumentConverter());
		xStream.alias("document", Document.class);

		xStream.registerConverter(new DocumentContentConverter());
		xStream.alias("documentcontent", DocumentContent.class);

		xStream.registerConverter(new DocumentVersionConverter());
		xStream.alias("documentversion", DocumentVersion.class);

		xStream.registerConverter(new IndexConverter());
		xStream.alias("index", Index.class);

		xStream.registerConverter(new XMPPDocumentConverter());
		xStream.alias("xmppdocument", XMPPDocument.class);
	}
}

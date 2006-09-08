/*
 * Dec 6, 2005
 */
package com.thinkparity.desdemona.util.dom4j;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

/**
 * The singleton wrapper around the dom4j xml reader was implemented because the
 * dom4j SAXReader is not thread-safe.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class XmlReader {

	/**
	 * A singleton instance.
	 */
	private static final XmlReader singleton;

	/**
	 * The synchronization lock for the singleton.
	 */
	private static final Object singletonLock;

	static {
		singleton = new XmlReader();
		singletonLock = new Object();
	}

	/**
	 * Read an xml string into a dom4j document.
	 * 
	 * @param xml
	 *            The xml.
	 * @return The xml document.
	 * @throws DocumentException
	 */
	public static Document read(final String xml) throws DocumentException {
		synchronized(singletonLock) { return singleton.readImpl(xml); }
	}

	/**
	 * A dom4j sax reader.
	 */
	private final SAXReader saxReader;

	/**
	 * Create a XmlReader [Singleton]
	 */
	private XmlReader() {
		super();
		this.saxReader = new SAXReader();
	}

	/**
	 * Read an xml string into a dom4j document.
	 * 
	 * @param xml
	 *            The xml.
	 * @return The xml document.
	 * @throws DocumentException
	 */
	private Document readImpl(final String xml) throws DocumentException {
		return saxReader.read(xml);
	}

}

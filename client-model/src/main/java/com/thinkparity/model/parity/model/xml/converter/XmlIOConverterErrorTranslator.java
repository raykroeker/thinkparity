/*
 * Nov 2, 2005
 */
package com.thinkparity.model.parity.model.xml.converter;

import java.io.IOException;
import java.util.zip.DataFormatException;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class XmlIOConverterErrorTranslator {

	/**
	 * Singleton implementation.
	 * @see XmlIOConverterErrorTranslator#singletonLock
	 */
	private static final XmlIOConverterErrorTranslator singleton;

	/**
	 * Singleton synchronization lock.
	 * @see XmlIOConverterErrorTranslator#singleton
	 */
	private static final Object singletonLock;

	static {
		singleton = new XmlIOConverterErrorTranslator();
		singletonLock = new Object();
	}

	/**
	 * Convert a data format error into an xml io interface error.
	 * 
	 * @param dfx
	 *            The data format error.
	 * @return The xml io interface error.
	 */
	public static XmlIOConverterException translate(
			final DataFormatException dfx) {
		synchronized(singletonLock) { return singleton.translateImpl(dfx); }
	}

	/**
	 * Convert a java io error to an xml io interface error.
	 * 
	 * @param iox
	 *            The java io error.
	 * @return The xml io interface error.
	 */
	public static XmlIOConverterException translate(final IOException iox) {
		synchronized(singletonLock) { return singleton.translateImpl(iox); }
	}

	/**
	 * Create a XmlIOConverterErrorTranslator [Singleton]
	 */
	private XmlIOConverterErrorTranslator() { super(); }

	/**
	 * Convert a data format error into an xml io interface error.
	 * 
	 * @param dfx
	 *            The data format error.
	 * @return The xml io interface error.
	 */
	private XmlIOConverterException translateImpl(final DataFormatException dfx) {
		return new XmlIOConverterException(dfx);
	}

	/**
	 * Convert a java io error to an xml io interface error.
	 * 
	 * @param iox
	 *            The java io error.
	 * @return The xml io interface error.
	 */
	private XmlIOConverterException translateImpl(final IOException iox) {
		return new XmlIOConverterException(iox);
	}
}

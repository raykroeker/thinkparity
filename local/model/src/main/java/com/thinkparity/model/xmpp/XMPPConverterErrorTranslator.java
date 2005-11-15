/*
 * Nov 15, 2005
 */
package com.thinkparity.model.xmpp;

import java.io.IOException;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class XMPPConverterErrorTranslator {

	private static final XMPPConverterErrorTranslator singleton;

	private static final Object singletonLock;
	
	static { 
		singleton = new XMPPConverterErrorTranslator();
		singletonLock = new Object();
	}

	public static XMPPConverterException translate(final IOException iox) {
		synchronized(singletonLock) { return singleton.translateImpl(iox); }
	}

	/**
	 * Create a XMPPConverterErrorTranslator.
	 */
	private XMPPConverterErrorTranslator() { super(); }

	/**
	 * Translate an io exception into a converter interface exception.
	 * 
	 * @param iox
	 *            The io error.
	 * @return The xmpp converter error.
	 */
	private XMPPConverterException translateImpl(final IOException iox) {
		return new XMPPConverterException(iox);
	}
}

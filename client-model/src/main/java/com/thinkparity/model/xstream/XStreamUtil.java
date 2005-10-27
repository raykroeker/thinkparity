/*
 * Feb 21, 2005
 */
package com.thinkparity.model.xstream;

import com.thoughtworks.xstream.XStream;

/**
 * XStreamUtil
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class XStreamUtil {

	/**
	 * Handle to the main XStream class for the client application.
	 */
	private static final XStream xStream;

	static {
		xStream = new XStream();
		XStreamRegistry.createRegistry(xStream);
	}

	public static Object fromXML(final String xml) {
		return xStream.fromXML(xml);
	}

	public static String toXML(final Object object) {
		return xStream.toXML(object);
	}

	/**
	 * Create an XStreamUtil [Singleton]
	 */
	private XStreamUtil() { super(); }
}

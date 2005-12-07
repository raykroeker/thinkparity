/*
 * Dec 7, 2005
 */
package com.thinkparity.server.org.dom4j;

import org.dom4j.Element;

import com.thinkparity.server.handler.ElementName;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ElementBuilder {

	/**
	 * Add an enumeration-named element to the element.
	 * 
	 * @param element
	 *            The dom4j element.
	 * @param elementName
	 *            The element name.
	 * @return The new element.
	 */
	public static Element addElement(final Element element,
			final ElementName elementName) {
		return element.addElement(elementName.getElementName());
	}

	/**
	 * Create a ElementBuilder [Builder,Singleton]
	 */
	private ElementBuilder() { super(); }
}

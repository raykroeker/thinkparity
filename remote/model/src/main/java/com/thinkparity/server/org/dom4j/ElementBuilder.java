/*
 * Dec 7, 2005
 */
package com.thinkparity.server.org.dom4j;

import org.dom4j.Element;


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
	 * @param elementText
	 *            The element data.
	 * @return The new element.
	 */
	public static Element addElement(final Element element,
			final ElementName elementName, final String elementText) {
		final Element newElement = addElement(element, elementName);
		newElement.setText(elementText);
		return newElement;
	}

	public static Element addElement(final Element element,
			final ElementName elementName) {
		final Element newElement = element.addElement(elementName.getName());
		return newElement;
	}

	/**
	 * Create a ElementBuilder [Builder,Singleton]
	 */
	private ElementBuilder() { super(); }
}

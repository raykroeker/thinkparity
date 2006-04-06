/*
 * Dec 7, 2005
 */
package com.thinkparity.server.org.dom4j;

import java.io.IOException;
import java.util.UUID;

import org.dom4j.Element;

import com.thinkparity.codebase.CompressionUtil;
import com.thinkparity.codebase.CompressionUtil.Level;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ElementBuilder {

	public static Element addElement(final Element element,
			final ElementName elementName) {
		final Element newElement = element.addElement(elementName.getName());
		return newElement;
	}

    public static Element addElement(final Element parentElement,
            final ElementName elementName, final byte[] elementValue) {
        final Element e2 = parentElement.addElement(elementName.getName());
        try { e2.setText(Base64.encode(CompressionUtil.compress(elementValue, Level.Nine))); }
        catch(final IOException iox) { throw new RuntimeException(iox); }
        return e2;
    }

    public static Element addElement(final Element parentElement,
            final ElementName elementName, final Long elementValue) {
        return addElement(parentElement, elementName, elementValue.toString());
    }

    /**
	 * Add an enumeration-named element to the element.
	 * 
	 * @param parentElement
	 *            The dom4j element.
	 * @param elementName
	 *            The element name.
	 * @param string
	 *            The element data.
	 * @return The new element.
	 */
	public static Element addElement(final Element parentElement,
            final ElementName elementName, final String string) {
		final Element newElement = addElement(parentElement, elementName);
		newElement.setText(string);
		return newElement;
	}

	public static Element addElement(final Element parentElement, final ElementName name,
            final UUID uniqueId) {
        final Element e = parentElement.addElement(name.getName());
        e.setText(uniqueId.toString());
        return e;
    }

	/**
	 * Create a ElementBuilder [Builder,Singleton]
	 */
	private ElementBuilder() { super(); }
}

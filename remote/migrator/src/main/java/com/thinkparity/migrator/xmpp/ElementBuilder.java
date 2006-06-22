/*
 * Dec 7, 2005
 */
package com.thinkparity.migrator.xmpp;

import java.util.List;

import org.dom4j.Element;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Constants.Xml;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class ElementBuilder extends com.thinkparity.codebase.xmpp.ElementBuilder {

    /**
     * Add a library type element.
     * 
     * @param parent
     *            The parent element.
     * @param name
     *            The element name.
     * @param value
     *            The element value.
     * @return The element.
     */
    static Element addElement(final Element parent, final String name,
            final Library.Type value) {
        return addElement(parent, name, Library.Type.class, value.toString());
    }

    /**
     * Add a list of library values.
     * 
     * @param parent
     *            The parent element.
     * @param name
     *            The element name.
     * @param value
     *            A long.
     * @return The root element added.
     */
    static Element addLibraryElements(final Element parent,
            final String parentName, final String name, final List<Library> values) {
        final Element element = addElement(parent, parentName, List.class);

        Element libraryElement;
        for(final Library value : values) {
            libraryElement = addElement(element, Xml.Library.LIBRARY, Library.class);

            addElement(libraryElement, Xml.Library.ARTIFACT_ID, value.getArtifactId());
            addElement(libraryElement, Xml.Library.CREATED_ON, value.getCreatedOn());
            addElement(libraryElement, Xml.Library.GROUP_ID, value.getGroupId());
            addElement(libraryElement, Xml.Library.ID, value.getId());
            addElement(libraryElement, Xml.Library.PATH, value.getPath());
            addElement(libraryElement, Xml.Library.TYPE, value.getType());
            addElement(libraryElement, Xml.Library.VERSION, value.getVersion());
        }

        return element;
    }

	/** Create ElementBuilder */
	protected ElementBuilder() { super(); }
}

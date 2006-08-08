/*
 * Created On: Aug 7, 2006 2:49:35 PM
 */
package com.thinkparity.model.xmpp;

import org.dom4j.Element;

import com.thinkparity.model.artifact.ArtifactType;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ElementBuilder extends
        com.thinkparity.codebase.xmpp.ElementBuilder {

    /**
     * Add an artifact type value.
     * 
     * @param parent
     *            The parent element.
     * @param name
     *            The element name.
     * @param value
     *            The element value.
     * @return The element.
     */
    public static final Element addElement(final Element parent,
            final String name, final ArtifactType value) {
        return addElement(parent, name, ArtifactType.class, value.toString());
    }

    /** Create ElementBuilder. */
    private ElementBuilder() { super(); }
}

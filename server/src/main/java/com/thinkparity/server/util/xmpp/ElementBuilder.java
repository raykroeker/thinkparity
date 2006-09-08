/*
 * Created On: Aug 7, 2006 2:49:35 PM
 */
package com.thinkparity.desdemona.util.xmpp;

import java.util.List;

import org.dom4j.Element;

import com.thinkparity.model.artifact.ArtifactType;
import com.thinkparity.model.profile.ProfileEMail;


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

    public static final Element addProfileEMailElements(final Element parent,
            final String parentName, final String name,
            final List<ProfileEMail> values) {
        final Element element = addElement(parent, parentName, List.class);
        for(final ProfileEMail value : values) { addElement(element, name, value); }
        return element;
    }

    public static final Element addElement(final Element parent,
            final String name, final ProfileEMail value) {
        final String valueString = value.toString();
        return addElement(parent, name, ProfileEMail.class, valueString);
    }

    /** Create ElementBuilder. */
    private ElementBuilder() { super(); }
}

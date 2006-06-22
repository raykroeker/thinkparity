/*
 * Created On: Thu May 11 2006 11:41 PDT
 * $Id$
 */
package com.thinkparity.migrator.xmpp;

import java.util.List;

import org.xmpp.packet.IQ;

import com.thinkparity.migrator.Library;

/**
 * <b>Title:</b>thinkParity Migrator IQ Writer <br>
 * <b>Description:</b>A concrete implementation of an iq writer for the
 * migrator.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class IQWriter extends com.thinkparity.codebase.xmpp.IQWriter {

    /**
     * Create IQWriter.
     * 
     * @param iq
     *            The backing internet query.
     */
    public IQWriter(final IQ iq) { super(iq); }

    /**
     * Write a list of library values.
     * 
     * @param parentName
     *            The parent element name.
     * @param name
     *            The element name.
     * @param libraries
     *            The element values.
     */
    public void writeLibraries(final String parentName, final String name,
            final List<Library> libraries) {
        ElementBuilder.addLibraryElements(iq.getChildElement(), parentName, name, libraries);
    }

    /**
     * Write a library type value.
     * 
     * @param name
     *            The element name.
     * @param value
     *            The element value.
     */
    public void writeLibraryType(final String name,
            final Library.Type value) {
        ElementBuilder.addElement(iq.getChildElement(), name, value);
    }
}

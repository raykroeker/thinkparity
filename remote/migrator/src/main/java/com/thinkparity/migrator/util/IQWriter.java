/*
 * Created On: Thu May 11 2006 11:41 PDT
 * $Id$
 */
package com.thinkparity.migrator.util;

import java.util.Calendar;
import java.util.List;

import org.xmpp.packet.IQ;

import com.thinkparity.migrator.Library;

public class IQWriter {

    /** The query this data writer is backing. */
    private final IQ iq;

    /** Create IQDataWriter. */
    public IQWriter(final IQ iq) {
        super();
        this.iq = iq;
    }

    /**
     * Obtain the internet query.
     *
     * @return The internet query.
     */
    public IQ getIQ() { return iq; }

    /**
     * Write a byte array value.
     * 
     * @param name
     *            The element name.
     * @param bytes
     *            The element value.
     */
    public void writeBytes(final String name, final byte[] bytes) {
        ElementBuilder.addElement(iq.getChildElement(), name, bytes);
    }

    /**
     * Write a calendar value.
     *
     * @param name
     *      The element name.
     * @param value
     *      The element value.
     */
    public void writeCalendar(final String name, final Calendar value) {
        ElementBuilder.addElement(iq.getChildElement(), name, value);
    }

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

    /**
     * Write a long value.
     * 
     * @param name
     *      The element name.
     * @param value
     *      The element value.
     */
    public void writeLong(final String name, final Long value) {
        ElementBuilder.addElement(iq.getChildElement(), name, value);
    }

    /**
     * Write a list of long values.
     * 
     * @param name
     *            The element name.
     * @param value
     *            The element values.
     */
    public void writeLongs(final String parentName, final String name,
            final List<Long> values) {
        ElementBuilder.addElements(iq.getChildElement(), parentName, name, values);
    }

    /**
     * Write a string value.
     *
     * @param name
     *      The element name.
     * @param value
     *      The element value.
     */
    public void writeString(final String name, final String value) {
        ElementBuilder.addElement(iq.getChildElement(), name, value);
    }
}

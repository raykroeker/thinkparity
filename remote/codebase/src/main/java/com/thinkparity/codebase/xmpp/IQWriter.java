/*
 * Created On: Thu May 11 2006 11:41 PDT
 * $Id$
 */
package com.thinkparity.codebase.xmpp;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.xmpp.packet.IQ;

import com.thinkparity.codebase.jabber.JabberId;


/**
 * <b>Title:</b>thinkParity IQ Writer<br>
 * <b>Description:</b>A jive server iq writer.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public abstract class IQWriter {

    /** The xmpp internet query this data writer is backing. */
    protected final IQ iq;

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
    public final IQ getIQ() { return iq; }

    /**
     * Write a byte array value.
     * 
     * @param name
     *            The element name.
     * @param bytes
     *            The element value.
     */
    public final void writeBytes(final String name, final byte[] bytes) {
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
    public final void writeCalendar(final String name, final Calendar value) {
        ElementBuilder.addElement(iq.getChildElement(), name, value);
    }

    /**
     * Write an integer value.
     * 
     * @param name
     *            The element name.
     * @param value
     *            The element value.
     */
    public final void writeInteger(final String name, final Integer value) {
        ElementBuilder.addElement(iq.getChildElement(), name, value);
    }

    /**
     * Write a jabber id value.
     * 
     * @param name
     *            The element name.
     * @param value
     *            The element value.
     */
    public final void writeJabberId(final String name, final JabberId value) {
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
    public final void writeLong(final String name, final Long value) {
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
    public final void writeLongs(final String parentName, final String name,
            final List<Long> values) {
        ElementBuilder.addLongElements(iq.getChildElement(), parentName, name, values);
    }

    /**
     * Write a string value.
     *
     * @param name
     *      The element name.
     * @param value
     *      The element value.
     */
    public final void writeString(final String name, final String value) {
        ElementBuilder.addElement(iq.getChildElement(), name, value);
    }

    /**
     * Write string values.
     * 
     * @param parentName
     *            The parent element name.
     * @param name
     *            The element name.
     * @param values
     *            The element values.
     */
    public final void writeStrings(final String parentName, final String name,
            final List<String> values) {
        ElementBuilder.addStringElements(iq.getChildElement(), parentName, name, values);
    }

    /**
     * Write unique id value.
     * 
     * @param name
     *            The element name.
     * @param value
     *            The element value.
     */
    public final void writeUniqueId(final String name, final UUID value) {
        ElementBuilder.addElement(iq.getChildElement(), name, value);
    }
}

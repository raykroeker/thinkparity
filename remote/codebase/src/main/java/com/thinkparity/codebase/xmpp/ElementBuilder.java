/*
 * Created On: Dec 7, 2005
 * $Id$
 */
package com.thinkparity.codebase.xmpp;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.UUID;

import org.dom4j.Element;

import com.thinkparity.codebase.CompressionUtil;
import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.util.Base64;

/**
 * <b>Title:</b>thinkParity Remote Element Builder <br>
 * <b>Description:</b>A dom element builder for the jive server plugins.
 * 
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class ElementBuilder {

    /**
     * Add a byte array value.
     *
     * @param parent
     *      The parent element.
     * @param name
     *      The element name.
     * @param
     *      The element byte array value.
     * @return The element.
     */
    public static final Element addElement(final Element parent, final String name,
            final byte[] value) {
        try {
            return addElement(parent, name, byte[].class, encode(compress(value)));
        }
        catch(final IOException iox) { throw new RuntimeException(iox); }
    }

    /**
     * Add a calendar value.
     *
     * @param parent
     *      The parent element.
     * @param name
     *      The element name.
     * @param value
     *      The element value.
     * @return The element.
     */
    public static final Element addElement(final Element parent, final String name,
            final Calendar value) {
        final Calendar valueGMT = DateUtil.getInstance(
                value.getTime(), new SimpleTimeZone(0, "GMT"));
        final String valueString = DateUtil.format(
                valueGMT, DateUtil.DateImage.ISO);
        return addElement(parent, name, Calendar.class, valueString);
    }

    /**
     * Add a jabber id value.
     * 
     * @param parent
     *            The parent element.
     * @param name
     *            The element name.
     * @param value
     *            A jabber id.
     * @return The element.
     */
    public static final Element addElement(final Element parent, final String name,
            final JabberId value) {
        return addElement(parent, name, JabberId.class, value.getQualifiedJabberId());
    }

    /**
     * Add a long value.
     * 
     * @param parent
     *            The parent element.
     * @param name
     *            The element name.
     * @param value
     *            A long.
     * @return The element.
     */
    public static final Element addElement(final Element parent,
            final String name, final Long value) {
        return addElement(parent, name, Long.class, value.toString());
    }

    /**
     * Add a string value.
     * 
     * @param parent
     *            The parent element.
     * @param name
     *            The element name.
     * @param value
     *            A string.
     * @return The element.
     */
	public static final Element addElement(final Element parent, final String name,
            final String value) {
        return addElement(parent, name, String.class, value);
    }

    /**
     * Add a uuid value.
     * 
     * @param parent
     *            The parent element.
     * @param name
     *            The element name.
     * @param value
     *            A uuid.
     * @return The element.
     */
    public static final Element addElement(final Element parent, final String name,
            final UUID value) {
        return addElement(parent, name, UUID.class, value.toString());
    }

    /**
     * Add a list of long values.
     * 
     * @param parent
     *            The parent element.
     * @param name
     *            The element name.
     * @param value
     *            A long.
     * @return The root element added.
     */
    public static final Element addElements(final Element parent,
            final String parentName, final String name, final List<Long> values) {
        final Element element = addElement(parent, parentName, List.class);

        for(final Long value : values) { addElement(element, name, value); }

        return element;
    }

    /**
     * Add a typed element to the parent.
     * 
     * @param parent
     *            The parent element.
     * @param name
     *            The element name.
     * @return The element.
     */
    protected static final Element addElement(final Element parent, final String name,
            final Class type) {
        final Element element = parent.addElement(name);
        element.addAttribute("javaType", type.getName());
        return element;
    }

    /**
     * Add a typed element to the parent.
     * 
     * @param parent
     *            The parent element.
     * @param name
     *            The element name.
     * @param type
     *            The element type.
     * @param value
     *            The element value.
     * @return The element.
     */
    protected static final Element addElement(final Element parent, final String name,
            final Class type, final String value) {
        final Element element = addElement(parent, name, type);
        element.setText(value);
        return element;
    }

    /**
     * Compress a byte array.
     *
     * @param bytes
     *      The bytes to compress.
     * @return The compressed bytes.
     */
    protected static final byte[] compress(final byte[] bytes) throws IOException {
        return CompressionUtil.compress(bytes, CompressionUtil.Level.Nine);
    }

    /**
     * Encode a byte array.
     *
     * @param bytes.
     *      The bytes to encode.
     * @return A Base64 encoding of the bytes.
     */
    protected static final String encode(final byte[] bytes) {
        return Base64.encode(bytes);
    }

	/** Create ElementBuilder */
	protected ElementBuilder() { super(); }
}

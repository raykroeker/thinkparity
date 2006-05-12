/*
 * Dec 7, 2005
 */
package com.thinkparity.migrator.util;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.dom4j.Element;

import com.thinkparity.codebase.CompressionUtil;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
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
    public static Element addElement(final Element parent, final String name,
            final Byte[] value) {
        final Element element = parent.addElement(name);
        try { element.setText(encode(compress(value))); }
        catch(final IOException iox) { throw new RuntimeException(iox); }
        return element;
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
    public static Element addElement(final Element parent, final String name,
            final JabberId value) {
        return addElement(parent, name, value.getQualifiedJabberId());
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
    public static Element addElement(final Element parentElement,
            final String name, final Long value) {
        return addElement(parentElement, name, value.toString());
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
	public static Element addElement(final Element parent, final String name,
            final String value) {
        final Element element = parent.addElement(name);
        element.setText(value);
        return element;
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
    public static Element addElement(final Element parent, final String name,
            final UUID value) {
        return addElement(parent, name, value.toString());
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
    public static Element addElements(final Element parentElement,
            final String parentName, final String name, final List<Long> values) {
        final Element element = parentElement.addElement(parentName);
        for(final Long value : values) {
            ElementBuilder.addElement(element, name, value.toString());
        }
        return element;
    }

    /**
     * Compress a byte array.
     *
     * @param bytes
     *      The bytes to compress.
     * @return The compressed bytes.
     */
    private static Byte[] compress(final Byte[] bytes) throws IOException {
        final byte[] boxed = new byte[bytes.length];
        for(int i = 0; i < bytes.length; i++) { boxed[i] = bytes[i]; }
        final byte[] compressed =
                CompressionUtil.compress(boxed, CompressionUtil.Level.Nine);
        final Byte[] unboxed = new Byte[compressed.length];
        for(int i = 0; i < unboxed.length; i++) { unboxed[i] = compressed[i]; }
        return unboxed;
    }

    /**
     * Encode a byte array.
     *
     * @param bytes.
     *      The bytes to encode.
     * @return A Base64 encoding of the bytes.
     */
    private static String encode(final Byte[] bytes) {
        final byte[] boxed = new byte[bytes.length];
        for(int i = 0; i < bytes.length; i++) { boxed[i] = bytes[i]; }
        return Base64.encode(boxed);
    }

	/** Create ElementBuilder */
	protected ElementBuilder() { super(); }
}

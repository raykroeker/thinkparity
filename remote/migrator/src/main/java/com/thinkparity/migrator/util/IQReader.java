/*
 * Created On: Thu May 11 2006 11:41 PDT
 * $Id$
 */
package com.thinkparity.migrator.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.DataFormatException;

import org.dom4j.Element;
import org.xmpp.packet.IQ;

import com.thinkparity.codebase.CompressionUtil;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Constants.Xml;

public class IQReader {

    /** The query backing this reader. */
    private final IQ iq;

    /**
     * Create IQReader.
     *
     * @param iq
     *      The internet query.
     */
    public IQReader(final IQ iq) {
        super();
        this.iq = iq;
    }

    public Byte[] readByteArray(final String name) {
        final String sData = readString(name);
        if(null == sData) { return null; }
        else {
            try { return decompress(decode(sData)); }
            catch(final DataFormatException dfx) { throw new RuntimeException(dfx); }
            catch(final IOException iox) { throw new RuntimeException(iox); }
        }
    }

    public List<Library> readLibraries(final String parentName, final String name) {
        final Element element = iq.getChildElement().element(parentName);
        if(null == element) { return null; }

        final Iterator iChildren = element.elementIterator(name);
        final List<Library> libraries = new LinkedList<Library>();
        Element libraryElement;
        Library library;
        while(iChildren.hasNext()) {
            libraryElement = (Element) iChildren.next();

            library = new Library();
            library.setArtifactId((String) libraryElement.element(Xml.Library.ARTIFACT_ID).getData());
            library.setGroupId((String) libraryElement.element(Xml.Library.GROUP_ID).getData());
            library.setId(Long.valueOf((String) libraryElement.element(Xml.Library.ID).getData()));
            library.setType(Library.Type.valueOf((String) libraryElement.element(Xml.Library.TYPE).getData()));
            library.setVersion((String) libraryElement.element(Xml.Library.VERSION).getData());
 
            libraries.add(library);
        }
        return libraries;
    }

    /**
     * Read library type data.
     *
     * @param name
     *      The element name.
     * @return The data; or null if the data does not exist.
     */
    public Library.Type readLibraryType(final String name) {
        final String sData = readString(name);
        if(null == sData) { return null; }
        else { return Library.Type.valueOf(sData); }
    }

    /**
     * Read long data.
     *
     * @param name
     *      The element name.
     * @return The data; or null if the data does not exist.
     */
    public Long readLong(final String name) {
        final String sData = readString(name);
        if(null == sData) { return null; }
        else { return Long.valueOf(sData); }
    }

    /**
     * Read long data.
     *
     * @param parentName
     *      The parent element name.
     * @param name
     *      The element name.
     * @return The data; or null if the data does not exist.
     */
    public List<Long> readLongs(final String parentName, final String name) {
        final Element element = iq.getChildElement().element(parentName);
        final Iterator iChildren = element.elementIterator(name);
        final List<Long> longs = new LinkedList<Long>();
        while(iChildren.hasNext()) {
            longs.add(Long.valueOf((String) ((Element) iChildren.next()).getData()));
        }
        return longs;
    }

    /**
     * Read string data.
     *
     * @param name
     *      The element name.
     * @return The data; or null if the data does not exist.
     */
    public String readString(final String name) {
        return (String) readObject(name);
    }

    /**
     * Decode a Base64 encoded string into an array of bytes.
     *
     * @param s
     *      A Base64 encoded string.
     * @return An array of bytes.
     */
    private byte[] decode(final String s) { return Base64.decode(s); }

    /**
     * Decompress an array of bytes into its object array.
     *
     * @param bytes
     *      An array of bytes.
     * @return An array of byte objects.
     */
    private Byte[] decompress(final byte[] bytes)
            throws DataFormatException, IOException {
        final byte[] decompressed = CompressionUtil.decompress(bytes);
        final Byte[] unboxed = new Byte[decompressed.length];
        for(int i = 0; i < decompressed.length; i++) {
            unboxed[i] = decompressed[i];
        }
        return unboxed;
    }

    /**
     * Read object data.
     *
     * @param name
     *      The element name.
     * @return The element data; or null if the element does not exist.
     */
    private Object readObject(final String name) {
        final Element e = iq.getChildElement().element(name);
        if(null == e) { return null; }
        else { return e.getData(); }
    }
}

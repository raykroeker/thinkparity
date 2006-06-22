/*
 * Created On: Thu May 11 2006 11:41 PDT
 * $Id$
 */
package com.thinkparity.migrator.xmpp;

import java.text.ParseException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SimpleTimeZone;

import org.dom4j.Element;
import org.xmpp.packet.IQ;

import com.thinkparity.codebase.DateUtil;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Constants.Xml;

/**
 * <b>Title:</b>thinkParity Migrator IQ Reader <br>
 * <b>Description:</b>A concrete migrator implementation of an xmpp iq reader.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class IQReader extends com.thinkparity.codebase.xmpp.IQReader {

    /**
     * Create IQReader.
     *
     * @param iq
     *      The internet query.
     */
    public IQReader(final IQ iq) { super(iq); }

    /**
     * Read a list of libraries from the backing query.
     * 
     * @param parentName
     *            The parent element name.
     * @param name
     *            The element name.
     * @return A list of libraries.
     */
    public final List<Library> readLibraries(final String parentName, final String name) {
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
            try { library.setCreatedOn(DateUtil.parse((String) libraryElement.element(Xml.Library.CREATED_ON).getData(), DateUtil.DateImage.ISO, new SimpleTimeZone(0, "GMT"))); }
            catch(final ParseException px) { throw new RuntimeException(px); }
            library.setGroupId((String) libraryElement.element(Xml.Library.GROUP_ID).getData());
            library.setId(Long.valueOf((String) libraryElement.element(Xml.Library.ID).getData()));
            library.setPath((String) libraryElement.element(Xml.Library.PATH).getData());
            library.setType(Library.Type.valueOf((String) libraryElement.element(Xml.Library.TYPE).getData()));
            library.setVersion((String) libraryElement.element(Xml.Library.VERSION).getData());
 
            libraries.add(library);
        }
        return libraries;
    }

    /**
     * Read a library type from the backing query.
     *
     * @param name
     *      The element name.
     * @return The data; or null if the data does not exist.
     */
    public final Library.Type readLibraryType(final String name) {
        final String sData = readString(name);
        if(null == sData) { return null; }
        else { return Library.Type.valueOf(sData); }
    }
}

/*
 * Created On: Thu May 11 2006 08:34 PDT
 * $Id$
 */
package com.thinkparity.migrator.controller;

import java.util.List;

import org.xmpp.packet.IQ;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.util.IQWriter;

/**
 * A mock object for an xmpp iq.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class MockIQ extends IQ {

    /** Create a mock internet query of type "GET" */
    public static MockIQ createGet() { return new MockIQ(IQ.Type.get); }

    /** A query writer. */
    private final IQWriter writer;

    /**
     * Create MockIQ.
     *
     * @param type
     *      The type of query.
     */
    private MockIQ(final IQ.Type type) {
        super(type);
        this.writer = new IQWriter(this);
        setChildElement("query", "jabber:iq:parity");
    }

    public void writeByteArray(final String name, final Byte[] value) {
        writer.writeByteArray(name, value);
    }

    public void writeLibraryType(final String name,
            final Library.Type libraryType) {
        writer.writeLibraryType(name, libraryType);
    }

    public void writeLong(final String name, final Long value) {
        writer.writeLong(name, value);
    }

    public void writeLongs(final String parentName, final String name,
            final List<Long> values) {
        writer.writeLongs(parentName, name, values);
    }

    public void writeString(final String name, final String value) {
        writer.writeString(name, value);
    }
}
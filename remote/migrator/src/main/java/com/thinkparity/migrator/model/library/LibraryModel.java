/*
 * May 9, 2006 3:17:27 PM
 * $Id$
 */
package com.thinkparity.migrator.model.library;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.model.AbstractModel;
import com.thinkparity.migrator.model.Context;

/**
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public class LibraryModel extends AbstractModel {

    /**
     * Obtain the remote internal parity library interface.
     * 
     * @param context
     *            The parity internal context.
     * @return The remote internal parity library interface.
     */
    public static InternalLibraryModel getInternalModel(final Context context) {
        return new InternalLibraryModel(context);
    }

    /**
     * Obtain the remote parity library interface.
     * 
     * @return The remote parity library interface.
     */
    public static LibraryModel getModel() {
        return new LibraryModel();
    }

    /** The library implementation. */
    private final LibraryModelImpl impl;

    /** The library implementation synchronization lock. */
    private final Object implLock;

    /** Create LibraryModel. */
    private LibraryModel() {
        this.impl = new LibraryModelImpl();
        this.implLock = new Object();
    }

    /**
     * Create a library.
     * 
     * @param artifactId
     *            A artifact id.
     * @param groupId
     *            A group id.
     * @param type
     *            A library type.
     * @param version
     *            A version.
     * @return A library.
     */
    public Library create(final String artifactId, final String groupId,
            final Library.Type type, final String version) {
        synchronized(implLock) {
            return impl.create(artifactId, groupId, type, version);
        }
    }

    /**
     * Create the bytes for a library.
     *
     * @param libraryId
     *      A library id.
     * @param bytes
     *      A library bytes.
     *      
     * @xmpp.model.api
     */
    public void createBytes(final Long libraryId, final Byte[] bytes) {
        synchronized(implLock) { impl.createBytes(libraryId, bytes); }
    }

    /**
     * Read a library.
     * 
     * @param libraryId
     *            A library id.
     * @return A library.
     */
    public Library read(final Long libraryId) {
        synchronized(implLock) { return impl.read(libraryId); }
    }

    /**
     * Read a library.
     * 
     * @param artifactId
     *            An artifact id.
     * @param groupId
     *            A group id.
     * @param type
     *            A type.
     * @param version
     *            A version.
     * @return A library.
     */
    public Library read(final String artifactId, final String groupId,
            final Library.Type type, final String version) {
        synchronized(implLock) {
            return impl.read(artifactId, groupId, type, version);
        }
    }

    /**
     * Download the library bytes.
     *
     * @param libraryId
     *      A library id.
     */
    public Byte[] readBytes(final Long libraryId) {
        synchronized(implLock) { return impl.readBytes(libraryId); }
    }

}

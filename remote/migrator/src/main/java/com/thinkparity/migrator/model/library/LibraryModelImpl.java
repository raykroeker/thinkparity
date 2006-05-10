/*
 * May 9, 2006 3:17:36 PM
 * $Id$
 */
package com.thinkparity.migrator.model.library;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.io.IOFactory;
import com.thinkparity.migrator.io.handler.LibraryIOHandler;
import com.thinkparity.migrator.model.AbstractModelImpl;

/**
 * @author raymond@thinkparity.com
 * @version 1.1
 */
class LibraryModelImpl extends AbstractModelImpl {

    /** The library io interface. */
    private final LibraryIOHandler libraryIO;

    /** Create LibraryModelImpl. */
    LibraryModelImpl() {
        super();
        this.libraryIO = IOFactory.getHypersonic().createLibraryHandler();
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
    Library create(final String artifactId, final String groupId,
            final Library.Type type, final String version) {
        logger.info("[RMIGRATOR] [LIBRARY] [CREATE]");
        logger.debug(artifactId);
        logger.debug(groupId);
        logger.debug(type);
        logger.debug(version);
        final Long libraryId = libraryIO.create(artifactId, groupId, type, version);
        return read(libraryId);
    }

    /**
     * Create the bytes for a library.
     *
     * @param libraryId
     *      A library id.
     * @param bytes
     *      A library bytes.
     */
    void createBytes(final Long libraryId, final Byte[] bytes) {
        logger.info("[RMIGRATOR] [LIBRARY] [CREATE BYTES]");
        logger.debug(libraryId);
        logger.debug(bytes);
        libraryIO.createBytes(libraryId, bytes);
    }

    /**
     * Read a library.
     * 
     * @param libraryId
     *            A library id.
     * @return A library.
     */
    Library read(final Long libraryId) {
        logger.info("[RMIGRATOR] [LIBRARY] [READ]");
        logger.debug(libraryId);
        return libraryIO.read(libraryId);
    }

    /**
     * Download the library bytes.
     *
     * @param libraryId
     *      A library id.
     */
    Byte[] readBytes(final Long libraryId) {
        logger.info("[RMIGRATOR] [LIBRARY] [READ  BYTES]");
        logger.debug(libraryId);
        return libraryIO.readBytes(libraryId);
    }
}

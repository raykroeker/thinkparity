/*
 * May 9, 2006 3:17:36 PM
 * $Id$
 */
package com.thinkparity.migrator.model.library;

import java.text.MessageFormat;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.LibraryBytes;
import com.thinkparity.migrator.io.IOFactory;
import com.thinkparity.migrator.io.handler.LibraryIOHandler;
import com.thinkparity.migrator.model.AbstractModelImpl;
import com.thinkparity.migrator.util.ChecksumUtil;

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
     * @param path
     *            A library path.
     * @param type
     *            A library type.
     * @param version
     *            A version.
     * @return A library.
     */
    Library create(final String artifactId, final String groupId,
            final String path, final Library.Type type, final String version) {
        logger.info("[RMIGRATOR] [MODEL] [LIBRARY] [CREATE]");
        logger.debug(artifactId);
        logger.debug(groupId);
        logger.debug(type);
        logger.debug(path);
        logger.debug(version);
        final Long libraryId = libraryIO.create(artifactId, groupId, path, type, version);
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
    void createBytes(final Long libraryId, final byte[] bytes, final String checksum) {
        logger.info("[RMIGRATOR] [MODEL] [LIBRARY] [CREATE BYTES]");
        logger.debug(libraryId);
        logger.debug(bytes);
        logger.debug(checksum);
        final String calculatedChecksum = ChecksumUtil.md5Hex(bytes);
        Assert.assertTrue(MessageFormat.format(
                "[RMIGRATOR [MODEL] [LIBRARY] [CREATE BYTES] [CHECKSUM DOES NOT MATCH CONTENT] [{0}] [{1}]",
                new Object[] {checksum, calculatedChecksum}),
                checksum.equals(calculatedChecksum));
        libraryIO.createBytes(libraryId, bytes, checksum);
    }

    /**
     * Delete a library.
     *
     * @param libraryId
     *      A library id.
     */
    void delete(final Long libraryId) {
        logger.info("[RMIGRATOR] [MODEL] [LIBRARY] [DELETE]");
        logger.debug(libraryId);
        libraryIO.delete(libraryId);
    }

    /**
     * Read a library.
     * 
     * @param libraryId
     *            A library id.
     * @return A library.
     */
    Library read(final Long libraryId) {
        logger.info("[RMIGRATOR] [MODEL] [LIBRARY] [READ]");
        logger.debug(libraryId);
        return libraryIO.read(libraryId);
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
    Library read(final String artifactId, final String groupId,
            final Library.Type type, final String version) {
        logger.info("[RMIGRATOR] [MODEL] [LIBRARY] [READ]");
        logger.debug(artifactId);
        logger.debug(groupId);
        logger.debug(type);
        logger.debug(version);
        return libraryIO.read(artifactId, groupId, type, version);
    }

    /**
     * Download the library bytes.
     *
     * @param libraryId
     *      A library id.
     */
    LibraryBytes readBytes(final Long libraryId) {
        logger.info("[RMIGRATOR] [MODEL] [LIBRARY] [READ  BYTES]");
        logger.debug(libraryId);
        return libraryIO.readBytes(libraryId);
    }
}

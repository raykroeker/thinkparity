/*
 * Created On: 
 * $Id$
 */
package com.thinkparity.model.parity.model.library;

import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.io.IOFactory;
import com.thinkparity.model.parity.model.io.handler.LibraryIOHandler;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.util.MD5Util;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.LibraryBytes;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
class LibraryModelImpl extends AbstractModelImpl {

    /** The library xmpp io. */
    private final LibraryIOHandler libraryIO;

    /** Create LibraryModelImpl. */
    LibraryModelImpl(final Workspace workspace) {
        super(workspace);
        this.libraryIO = IOFactory.getXMPP().createLibraryHandler();
    }

    /**
     * Create a library.
     * 
     * @param artifactId
     *            An artifact id.
     * @param groupId
     *            A group id.
     * @param path
     *            A library path.
     * @param type
     *            A type.
     * @param version
     *            A version.
     * @param bytes
     *            A byte array.
     * @return A library.
     */
    Library create(final String artifactId, final String groupId,
            final String path, final Library.Type type, final String version,
            final byte[] bytes) {
        logger.info("[LMODEL] [MODEL] [LIBRARY] [CREATE]");
        logger.debug(artifactId);
        logger.debug(groupId);
        logger.debug(path);
        logger.debug(type);
        logger.debug(version);
        logger.debug(null == bytes ? null : bytes.length);
        final Long libraryId = libraryIO.create(artifactId, groupId, path, type, version);
        final String checksum = MD5Util.md5Hex(bytes);
        libraryIO.createBytes(libraryId, bytes, checksum);
        return read(libraryId);
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
        logger.info("[LMODEL] [MODEL] [LIBRARY] [READ]");
        logger.debug(artifactId);
        logger.debug(groupId);
        logger.debug(type);
        logger.debug(version);
        return libraryIO.read(artifactId, groupId, type, version);
    }

    /**
     * Read a library's bytes.
     * 
     * @param libraryId
     *            A library id.
     * @return A library's bytes.
     */
    LibraryBytes readBytes(final Long libraryId) {
        return libraryIO.readBytes(libraryId);
    }

    /**
     * Read a library.
     * 
     * @param libraryId
     *            A library id.
     * @return A library.
     */
    private Library read(final Long libraryId) {
        return libraryIO.read(libraryId);
    }
}

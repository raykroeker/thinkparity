/*
 * Created On: 
 * $Id$
 */
package com.thinkparity.ophelia.model.migrator;


import com.thinkparity.codebase.model.migrator.Library;
import com.thinkparity.codebase.model.migrator.LibraryBytes;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.AbstractModelImpl;
import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.LibraryIOHandler;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
class LibraryModelImpl extends AbstractModelImpl {

    /** The library xmpp io. */
    private final LibraryIOHandler libraryIO;

    /** Create LibraryModelImpl. */
    LibraryModelImpl(final Environment environment, final Workspace workspace) {
        super(environment, workspace);
        this.libraryIO = IOFactory.getXMPP(workspace).createLibraryHandler();
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
        logger.logApiId();
        logger.logVariable("variable", artifactId);
        logger.logVariable("variable", groupId);
        logger.logVariable("variable", path);
        logger.logVariable("variable", type);
        logger.logVariable("variable", version);
        logger.logVariable("variable", null == bytes ? null : bytes.length);
        final Long libraryId = libraryIO.create(artifactId, groupId, path, type, version);
        // final String checksum = MD5Util.md5Hex(bytes);
        // TODO Refactor to use streams.
        libraryIO.createBytes(libraryId, bytes, null);
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
        logger.logApiId();
        logger.logVariable("variable", artifactId);
        logger.logVariable("variable", groupId);
        logger.logVariable("variable", type);
        logger.logVariable("variable", version);
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

/*
 * Created On: Fri May 12 2006 11:32 PDT
 * $Id$
 */
package com.thinkparity.model.parity.model.io.handler;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.LibraryBytes;

/**
 * The implementation of the xmpp library io interface.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public interface LibraryIOHandler {

    /**
     * Create a library.
     *
     * @return A library id.
     */
    public Long create(final String artifactId, final String groupId,
            final String path, final Library.Type type, final String version);

    /**
     * Create the library bytes.
     * 
     * @param libraryId
     *            A library id.
     * @param bytes
     *            A library's bytes.
     * @param checksum
     *            A library's checksum.
     */
    public void createBytes(final Long libraryId, final byte[] bytes,
            final String checksum);

    /**
     * Delete a library.
     *
     * @param libraryId.
     */
    public void delete(final Long libraryId);

    /**
     * Read a library.
     *
     * @param libraryId
     *      A library id.
     * @return A library.
     */
    public Library read(final Long libraryId);

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
            final Library.Type type, final String version);

    /**
     * Read the library's bytes.
     * 
     * @param libraryId
     *            A library id.
     * @return A byte array of the library's content.
     */
    public LibraryBytes readBytes(final Long libraryId);
}

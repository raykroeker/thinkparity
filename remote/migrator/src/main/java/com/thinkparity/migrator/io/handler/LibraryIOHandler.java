/*
 * Created On: May 9, 2006 3:26:42 PM
 * $Id$
 */
package com.thinkparity.migrator.io.handler;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.LibraryBytes;
import com.thinkparity.migrator.io.hsqldb.HypersonicException;

/**
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public interface LibraryIOHandler {

    /**
     * Create a library.
     * 
     * @param artifactId
     *            The artifact id.
     * @param groupId
     *            The group id.
     * @param type
     *            The type.
     * @param path
     *            The path.
     * @param version
     *            The version.
     * @return The library id.
     * @throws HypersonicException
     */
    public Long create(final String artifactId, final String groupId,
            final String path, final Library.Type type, final String version)
            throws HypersonicException;
    /**
     * Create a library's bytes.
     * 
     * @param libraryId
     *            A library id.
     * @param bytes
     *            A library's bytes.
     * @param checksum
     *            The byte checksum.
     * @throws HypersonicException
     */
    public void createBytes(final Long libraryId, final byte[] bytes,
            final String checksum) throws HypersonicException;

    /**
     * Delete a library.
     *
     * @param libraryId
     *      A library id.
     */
    public void delete(final Long libraryId) throws HypersonicException;

    /**
     * Read a library by its id.
     * 
     * @param libraryId
     *            The library id.
     * @return The library.
     * @throws HypersonicException
     */
    public Library read(final Long libraryId) throws HypersonicException;

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
     * @throws HypersonicException
     */
    public Library read(final String artifactId, final String groupId,
            final Library.Type type, final String version)
            throws HypersonicException;

    /**
     * Read a library's bytes.
     *
     * @param libraryId
     *      A library id.
     * @return A byte array.
     */
    public LibraryBytes readBytes(final Long libraryId)
            throws HypersonicException;
}

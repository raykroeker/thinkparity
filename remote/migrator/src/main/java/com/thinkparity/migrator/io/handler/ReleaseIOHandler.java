/*
 * May 9, 2006 11:53:05 AM
 * $Id$
 */
package com.thinkparity.migrator.io.handler;

import java.util.List;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Release;
import com.thinkparity.migrator.io.hsqldb.HypersonicException;

/**
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public interface ReleaseIOHandler {

    /**
     * Create a release.
     * 
     * @param artifactId
     *            The artifact id.
     * @param groupId
     *            The group id.
     * @param version
     *            The version.
     * @return The release id.
     * @throws HypersonicException
     */
    public Long create(final String artifactId, final String groupId,
            final String version) throws HypersonicException;

    /**
     * Create a release library relationship.
     * 
     * @param releaseId
     *            The release id.
     * @param libraryId
     *            The library id.
     * @throws HypersonicException
     */
    public void createLibraryRel(final Long releaseId, final Long libraryId)
            throws HypersonicException;


    /**
     * Delete a release.
     *
     * @param releaseId
     *      A release id.
     */
    public void delete(final Long releaseId) throws HypersonicException;

    /**
     * Delete all release library relationships.
     *
     * @param releaseId
     *      A release id.
     */
    public void deleteLibraryRel(final Long releaseId)
            throws HypersonicException;

    /**
     * Read a release.
     * 
     * @param releaseId
     *            A release id.
     * @return A release.
     * @throws HypersonicException
     */
    public Release read(final Long releaseId) throws HypersonicException;

    /**
     * Read a release.
     * 
     * @param artifactId
     *      An artifact id.
     * @param groupid
     *      A group id.
     * @param version.
     *      A version.
     * @return A release.
     * @throws HypersonicException
     */
    public Release read(final String artifactId, final String groupId,
            final String version) throws HypersonicException;

   /**
     * Read the latest release.
     * 
     * @param artifactId
     *            An artifact id.
     * @param groupId
     *            A group id.
     * @return A release.
     * @throws HypersonicException
     */
    public Release readLatest(final String artifactId, final String groupId)
            throws HypersonicException;

    /**
     * Read a list of libraries.
     *
     * @param releaseId
     *      A release id.
     * @return A list of libraries.
     */
    public List<Library> readLibraryRel(final Long releaseId)
        throws HypersonicException;
}

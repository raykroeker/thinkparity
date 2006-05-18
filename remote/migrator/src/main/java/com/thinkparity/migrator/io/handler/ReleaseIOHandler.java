/*
 * May 9, 2006 11:53:05 AM
 * $Id$
 */
package com.thinkparity.migrator.io.handler;

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
     * @param name
     *            The name.
     * @param version
     *            The version.
     * @return The release id.
     * @throws HypersonicException
     */
    public Long create(final String artifactId, final String groupId,
            final String name, final String version) throws HypersonicException;

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
     * Read a release by its name.
     * 
     * @param releaseName
     *            The release name.
     * @return The release.
     * @throws HypersonicException
     */
    public Release read(final String releaseName) throws HypersonicException;

   /**
     * Read the latest release.
     * 
     * @return The release.
     * @throws HypersonicException
     */
    public Release readLatest() throws HypersonicException;
}

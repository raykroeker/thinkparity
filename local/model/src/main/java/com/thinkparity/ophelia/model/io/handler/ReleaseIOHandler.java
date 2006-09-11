/*
 * Created On: Fri May 12 2006 11:32 PDT
 * $Id$
 */
package com.thinkparity.ophelia.model.io.handler;

import java.util.List;

import com.thinkparity.codebase.model.migrator.Library;
import com.thinkparity.codebase.model.migrator.Release;


/**
 * The implementation of the xmpp release io interface.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public interface ReleaseIOHandler  {

    /**
     * Create a release.
     * 
     * @param libraries
     *            A list of libraries.
     * @return A release id.
     */
    public Long create(final String artifactId, final String groupId,
            final String version, final List<Long> libraryIds);

    /**
     * Delete a release.
     *
     * @param releaseId
     *      A release id.
     */
    public void delete(final Long releaseId);

    /**
     * Read a release.
     * 
     * @param name
     *            A release id.
     * @return A release.
     */
    public Release read(final Long releaseId);

    /**
     * Read a release.
     * 
     * @param name
     *            A release id.
     * @return A release.
     */
    public Release read(final String artifactId, final String groupId,
            final String version);

    /**
     * Read all releases.
     * 
     * @return A list of releases.
     */
    public List<Release> readAll();

    /**
     * Read the latest release.
     * 
     * @param artifactId
     *            An artifact id.
     * @param groupid
     *            A group id.
     * @return A release.
     */
    public Release readLatest(final String artifactId, final String groupId);

    /**
     * Read the libraries.
     *
     * @param releaseId
     * @return A list of libraries.
     */
    public List<Library> readLibraries(final Long releaseId);
}

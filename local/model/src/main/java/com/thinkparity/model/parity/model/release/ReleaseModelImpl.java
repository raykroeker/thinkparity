/*
 * Created On: Fri May 12 2006 09:11 PDT
 * $Id$
 */
package com.thinkparity.model.parity.model.release;

import java.util.LinkedList;
import java.util.List;

import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.io.IOFactory;
import com.thinkparity.model.parity.model.io.handler.ReleaseIOHandler;
import com.thinkparity.model.parity.model.workspace.Workspace;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Release;

/**
 * The parity bootstrap's release model implementation.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
class ReleaseModelImpl extends AbstractModelImpl {

    /** The release xmpp io interface. */
    private final ReleaseIOHandler releaseIO;

    /** Create ReleaseModelImpl. */
    ReleaseModelImpl(final Workspace workspace) {
        super(workspace);
        this.releaseIO = IOFactory.getXMPP().createReleaseHandler();
    }
    /**
     * Create a release.
     * 
     * @param artifactId
     *            An artifact id.
     * @param groupId
     *            A group id.
     * @param version
     *            A version.
     * @param libraries
     *            A list of libraries.
     * @return A release.
     */
    Release create(final String artifactId, final String groupId,
            final String version, final List<Library> libraries) {
        logger.info("");
        logger.debug(artifactId);
        logger.debug(groupId);
        logger.debug(version);
        logger.debug(libraries);
        final Long releaseId = releaseIO.create(
                artifactId, groupId, version, extractLibraryIds(libraries));
        return read(releaseId);
    }

    /**
     * Read a release.
     *
     * @param artifactId
     *      An artifact id.
     * @param groupId
     *      A group id.
     * @param version
     *      A version.
     * @return A release.
     */
    Release read(final String artifactId, final String groupId,
            final String version) {
        return releaseIO.read(artifactId, groupId, version);
    }

    /**
     * Read the latest release.
     * 
     * @param artifactId
     *            An artifact id.
     * @param groupId
     *            A group id.
     * @return A release.
     */
    Release readLatest(final String artifactId, final String groupId) {
        return releaseIO.readLatest(artifactId, groupId);
    }

    /**
     * Read the libraries for the release.
     * 
     * @param releaseId
     *            A release id.
     * @return A list of libraries belonging to the release.
     */
    List<Library> readLibraries(final Long releaseId) {
        return releaseIO.readLibraries(releaseId);
    }

    /**
     * Extract a list of library ids from a list of libraries.
     * 
     * @param libraries
     *            A list of libraries.
     * @return A list of library ids.
     */
    private List<Long> extractLibraryIds(final List<Library> libraries) {
        final List<Long> libraryIds = new LinkedList<Long>();
        for(final Library library : libraries) { libraryIds.add(library.getId()); }
        return libraryIds;
    }

    /**
     * Read a release.
     * 
     * @param releaseId
     *            A release id.
     * @return A release.
     */
    private Release read(final Long releaseId) {
        return releaseIO.read(releaseId);
    }
}

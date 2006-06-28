/*
 * Created On:  May 9, 2006
 * $Id$
 */
package com.thinkparity.migrator.model.release;

import java.util.List;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Release;
import com.thinkparity.migrator.io.IOFactory;
import com.thinkparity.migrator.io.handler.ReleaseIOHandler;
import com.thinkparity.migrator.model.AbstractModelImpl;

/**
 * The release model implementation.  Contains the business logic for creating
 * and reading the releases available.
 *
 * @author raymond@thinkparity.com
 * @version 1.1
 * @tpc.model framework="remote"
 */
class ReleaseModelImpl extends AbstractModelImpl {

    /** The release io interface. */
    private final ReleaseIOHandler releaseIO;

    /** Create ReleaseModelImpl. */
    ReleaseModelImpl() {
        super();
        this.releaseIO = IOFactory.getHypersonic().createReleaseHandler();
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
     *            The libraries associated with the release.
     * @return The release.
     */
    Release create(final String artifactId, final String groupId,
            final String version, final List<Library> libraries) {
        logger.info("[RMIGRATOR] [MODEL] [RELEASE] [CREATE]");
        logger.debug(artifactId);
        logger.debug(groupId);
        logger.debug(version);
        logger.debug(libraries);
        final Long releaseId = releaseIO.create(artifactId, groupId, version);
        for(final Library library : libraries)
            releaseIO.createLibraryRel(releaseId, library.getId());
        return read(releaseId);
    }

    /**
     * Delete a release.
     *
     * @param releaseId
     *      A release id.
     */
    void delete(final Long releaseId) {
        logger.info("[RMIGRATOR] [MODEL] [RELEASE] [DELETE]");
        logger.debug(releaseId);
        releaseIO.deleteLibraryRel(releaseId);
        releaseIO.delete(releaseId);
    }

    /**
     * Read a release.
     * 
     * @param releaseId
     *            A release id.
     * @return A release.
     * @tpc.model.api visiblity="public"
     */
    Release read(final Long releaseId) {
        logger.info("[RMIGRATOR] [MODEL] [RELEASE] [READ]");
        logger.debug(releaseId);
        return releaseIO.read(releaseId);
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
        logger.info("[RMIGRATOR] [MODEL] [RELEASE] [READ]");
        logger.debug(artifactId);
        logger.debug(groupId);
        logger.debug(version);
        return releaseIO.read(artifactId, groupId, version);
    }

    /**
     * Read all releases.
     * 
     * @return A list of all releases.
     */
    List<Release> readAll() {
        logger.info("[RMIGRATOR] [MODEL] [RELEASE] [READ ALL]");
        return releaseIO.readAll();
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
        logger.info("[RMIGRATOR] [MODEL] [RELEASE] [READ LATEST]");
        logger.debug(artifactId);
        logger.debug(groupId);
        return releaseIO.readLatest(artifactId, groupId);
    }

    /**
     * Read the release libraries.
     *
     * @param releaseId
     *      A release id.
     * @return A list of libraries.
     */
    List<Library> readLibraries(final Long releaseId) {
        logger.info("[RMIGRATOR] [MODEL] [RELEASE] [READ LIBRARIES]");
        logger.debug(releaseId);
        return releaseIO.readLibraryRel(releaseId);
    }
}

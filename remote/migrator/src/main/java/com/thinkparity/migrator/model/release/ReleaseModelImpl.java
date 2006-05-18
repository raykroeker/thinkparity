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
     * Read a release.
     * 
     * @param releaseName
     *            The release name.
     * @return The release.
     * @tpc.model.api visiblity="public"
     */
    Release read(final String name) {
        logger.info("[RMIGRATOR] [MODEL] [RELEASE] [READ]");
        logger.debug(name);
        return releaseIO.read(name);
    }

    /**
     * Read the latest release.
     *
     * @return A release.
     */
    Release readLatest() {
        logger.info("[RMIGRATOR] [MODEL] [RELEASE] [READ LATEST]");
        return releaseIO.readLatest();
    }

    /**
     * Create a release.
     * 
     * @param artifactId
     *            An artifact id.
     * @param groupId
     *            A group id.
     * @param name
     *            A name.
     * @param version
     *            A version.
     * @param libraries
     *            The libraries associated with the release.
     * @return The release.
     */
    Release create(final String artifactId, final String groupId,
            final String name, final String version,
            final List<Library> libraries) {
        logger.info("[RMIGRATOR] [MODEL] [RELEASE] [CREATE RELEASE]");
        logger.debug(artifactId);
        logger.debug(groupId);
        logger.debug(name);
        logger.debug(version);
        logger.debug(libraries);
        final Long releaseId = releaseIO.create(artifactId, groupId, name, version);
        for(final Library library : libraries)
            releaseIO.createLibraryRel(releaseId, library.getId());
        return read(name);
    }
}

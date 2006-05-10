/*
 * May 9, 2006
 */
package com.thinkparity.migrator.model.release;

import java.util.List;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Release;
import com.thinkparity.migrator.io.IOFactory;
import com.thinkparity.migrator.io.handler.ReleaseIOHandler;
import com.thinkparity.migrator.model.AbstractModelImpl;

/**
 * @author raymond@thinkparity.com
 * @version 1.1
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
     */
    Release read(final String releaseName) {
        logger.info("[RMIGRATOR] [RELEASE] [READ]");
        logger.debug(releaseName);
        return releaseIO.read(releaseName);
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
        logger.info("[RMIGRATOR] [RELEASE] [CREATE RELEASE]");
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

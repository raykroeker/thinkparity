/*
 * Created On: Fri May 12 2006 09:11 PDT
 * $Id$
 */
package com.thinkparity.model.parity.model.release;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.ParityErrorTranslator;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.io.IOFactory;
import com.thinkparity.model.parity.model.io.handler.ReleaseIOHandler;
import com.thinkparity.model.parity.model.workspace.Workspace;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Migrator;
import com.thinkparity.migrator.Release;
import com.thinkparity.migrator.ReleaseDateComparator;

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
     * @param name
     *            A name.
     * @param version
     *            A version.
     * @param libraries
     *            A list of libraries.
     * @return A release.
     */
    Release create(final String artifactId, final String groupId,
            final String version, final List<Library> libraries) {
        logger.info("[LMODEL] [RELEASE] [CREATE]");
        logger.debug(artifactId);
        logger.debug(groupId);
        logger.debug(version);
        logger.debug(libraries);

        final Long releaseId = releaseIO.create(
                artifactId, groupId, version, extractIds(libraries));

        return releaseIO.read(releaseId);
    }

    /**
     * Delete a release.
     *
     * @param releaseId
     *      A release id.
     */
    void delete(final Long releaseId) {
        logger.info("[LMODEL] [RELEASE] [DELETE]");
        logger.debug(releaseId);
        releaseIO.delete(releaseId);
    }

    /**
     * Download the latest release.
     *
     * @param artifactId
     *      An artifact id.
     * @param groupId
     *      A group id.
     * @param version
     *      A version.
     */
    void download(final String artifactId, final String groupId)
            throws ParityException {
        final Release latestRelease = readLatest(artifactId, groupId);
        try { download(latestRelease); }
        catch(final IOException iox) {
            logger.error("[RMODEL] [RELEASE] [DOWNLOAD]", iox);
            logger.error(artifactId);
            logger.error(groupId);
            throw ParityErrorTranslator.translate(iox);
        }
    }

    /**
     * Determine whether a release is available.
     *
     * @param artifactId
     *      An artifact id.
     * @param groupId
     *      A group id.
     * @param version
     *      A version
     * @return True if a release is available; false otherwise.
     */
    Boolean isAvailable(final String artifactId, final String groupId,
            final String version) {
        logger.info("[LMODEL] [RELEASE] [IS AVAILABLE]");
        logger.debug(artifactId);
        logger.debug(groupId);
        logger.debug(version);
        final Release release = read(artifactId, groupId, version);
        return isAvailable(release);
    }

    /**
     * Migrate to the latest release.
     * 
     * @param artifactId
     *            An artifact id.
     * @param groupId
     *            A group id.
     * @param version
     *            A version.
     */
    void migrate(final String artifactId, final String groupId,
            final String version) {
        logger.info("[LMODEL] [RELEASE] [MIGRATE]");
        logger.debug(artifactId);
        logger.debug(groupId);
        logger.debug(version);
        // ensure there exists a new release for the given release
        final Release current = read(artifactId, groupId, version);
        assertIsAvailable("[MIGRATE]", current);

        final Release latest = readLatest(artifactId, groupId);
        final List<Library> latestLibraries = readLibraries(latest.getId());

        // ensure the latest relase has been downloaded
        assertIsDownloaded("[MIGRATE]", latest, latestLibraries);

        final MetaInfoHelper metaInfoHelper =
            new MetaInfoHelper(context, latest, latestLibraries);
        MetaInfo metaInfo;
        Migrator libraryMigrator;
        for(final Library library : latestLibraries) {
            if(library.isCore()) {
                try {
                    metaInfo = metaInfoHelper.getMetaInfo(library);

                    libraryMigrator = metaInfo.getMigrator();
                    if(null != libraryMigrator) {
                        libraryMigrator.upgrade(current, latest);
                    }
                }
                catch(final ClassNotFoundException cnfx) {
                    logger.error("", cnfx);
                }
                catch(final IOException iox) {
                    logger.error("", iox);
                }
                catch(final IllegalAccessException iax) {
                    logger.error("", iax);
                }
                catch(final InstantiationException ix) {
                    logger.error("", ix);
                }
                catch(final IntrospectionException ix) {
                    logger.error("", ix);
                }
            }
        }
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

    private void assertIsAvailable(final String assertion, final Release release) {
        Assert.assertTrue(
                new StringBuffer(assertion).insert(0, "[LMODEL] [RELEASE] ")
                .append(" [NO RELEASE AVAILABLE]").toString(),
                isAvailable(release));
    }

    /**
     * Assert that the release has been downloaded.
     * 
     * @param assertion
     *            An assertion message.
     * @param release
     *            A release.
     * @param libraries
     *            The release libraries.
     */
    private void assertIsDownloaded(final String assertion,
            final Release release, final List<Library> libraries) {
        final DownloadHelper helper = new DownloadHelper(getContext(), release);
        for(final Library library : libraries) {
            Assert.assertTrue(
                    "[LMODEL] [RELEASE] [MIGRATE] [DOWNLOAD CORRUPT]",
                    helper.isDownloaded(library));
        }
    }

    /**
     * Download a given release.
     * 
     * @param release
     *            A release.
     * @throws IOException
     */
    private void download(final Release release) throws IOException {
        final List<Library> libraries = readLibraries(release.getId());

        final DownloadHelper helper = new DownloadHelper(getContext(), release);
        for(final Library library : libraries) {
            if(!helper.isDownloaded(library)) { helper.download(library); }
        }
    }

    /**
     * Extract a list of library ids from a list of libraries.
     * 
     * @param libraries
     *            A list of libraries.
     * @return A list of library ids.
     */
    private List<Long> extractIds(final List<Library> libraries) {
        final List<Long> ids = new LinkedList<Long>();
        for(final Library library : libraries) ids.add(library.getId());
        return ids;
    }

    /**
     * Determine whether a release is available.
     *
     * @param release
     *      A release.
     * @return True if a release is available; false otherwise.
     */    
    private Boolean isAvailable(final Release release) {
        final Release latestRelease = readLatest(
                release.getArtifactId(), release.getGroupId());
        return new ReleaseDateComparator(latestRelease).isAfter(release);
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
    private Release readLatest(final String artifactId, final String groupId) {
        return releaseIO.readLatest(artifactId, groupId);
    }

    /**
     * Read the libraries for the release.
     * 
     * @param releaseId
     *            A release id.
     * @return A list of libraries.
     */
    private List<Library> readLibraries(final Long releaseId) {
        return releaseIO.readLibraries(releaseId);
    }
}

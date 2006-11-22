/*
 * Created On: 
 * $Id$
 */
package com.thinkparity.ophelia.model.download;

import java.util.List;

import com.thinkparity.codebase.model.migrator.Library;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.AbstractModelImpl;
import com.thinkparity.ophelia.model.Constants;
import com.thinkparity.ophelia.model.ParityException;
import com.thinkparity.ophelia.model.download.helper.DownloadHelper;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * The parity bootstrap download interface implementation.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
class DownloadModelImpl extends AbstractModelImpl {

    /** Create DownloadModelImpl. */
    DownloadModelImpl(final Environment environment, final Workspace workspace) {
        super(environment, workspace);
    }

    /**
     * Download a release.
     * 
     * @param release
     *            A release.
     * @throws ParityException
     */
    void download(final Release release) {
        logger.logApiId();
        logger.logVariable("variable", release);
        try {
            final List<Library> libraries = readLibraries(release.getId());
            final DownloadHelper helper = new DownloadHelper(internalModelFactory, release);
            if (!helper.isStarted()) {
                helper.start(libraries);
            }
            helper.resume();
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Determine if the download for a release is complete.
     * 
     * @param release
     *            A release.
     * @return True the download for the latest version is complete.
     */
    Boolean isComplete(final Release release) {
        logger.logApiId();
        logger.logVariable("variable", release);
        try {
            // check to see if the latest release has already been downloaded
            final DownloadHelper download = new DownloadHelper(
                    internalModelFactory, release);
            return download.isComplete();
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the latest release that has been downloaded.
     * 
     * @return A release.
     */
    Release read() {
        logger.logApiId();
        try {
            final Release latest = readLatestRelease();
            final DownloadHelper helper = new DownloadHelper(internalModelFactory, latest);
            if (helper.isComplete()) {
                return latest;
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    private Release readLatestRelease() {
        return getInternalReleaseModel().readLatest(
                Constants.Release.ARTIFACT_ID, Constants.Release.GROUP_ID);
    }

    private List<Library> readLibraries(final Long releaseId) {
        return getInternalReleaseModel().readLibraries(releaseId);
    }
}

/*
 * Created On: 
 * $Id$
 */
package com.thinkparity.ophelia.model.download;

import java.util.List;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.migrator.Library;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.ParityException;
import com.thinkparity.ophelia.model.download.helper.DownloadHelper;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * The parity bootstrap download interface implementation.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public final class DownloadModelImpl extends Model implements
        DownloadModel, InternalDownloadModel {

    /**
     * Create DownloadModelImpl.
     *
     */
    public DownloadModelImpl() {
        super();
    }

    
    /**
     * Download a release.
     * 
     * @param release
     *            A release.
     * @throws ParityException
     */
    public void download(final Release release) {
        logger.logApiId();
        logger.logVariable("variable", release);
        try {
            final List<Library> libraries = readLibraries(release.getId());
            final DownloadHelper helper = new DownloadHelper(release,
                    workspace.createTempDirectory());
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
    public Boolean isComplete(final Release release) {
        logger.logApiId();
        logger.logVariable("variable", release);
        try {
            // check to see if the latest release has already been downloaded
            final DownloadHelper download = new DownloadHelper(release,
                    workspace.createTempDirectory());
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
    public Release read() {
        logger.logApiId();
        try {
            final Release latest = readLatestRelease();
            final DownloadHelper helper = new DownloadHelper(latest,
                    workspace.createTempDirectory());
            if (helper.isComplete()) {
                return latest;
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#initializeModel(com.thinkparity.codebase.model.session.Environment, com.thinkparity.ophelia.model.workspace.Workspace)
     *
     */
    @Override
    protected void initializeModel(final Environment environment,
            final Workspace workspace) {
    }

    private Release readLatestRelease() {
        // raymond@thinkparity.com - 12-Jan-07 11:40:32 AM
        throw Assert.createNotYetImplemented("");
    }

    private List<Library> readLibraries(final Long releaseId) {
        // raymond@thinkparity.com - 12-Jan-07 11:40:37 AM
        throw Assert.createNotYetImplemented("");
    }
}

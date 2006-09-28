/*
 * Created On: 
 * $Id$
 */
package com.thinkparity.ophelia.model.download;

import java.io.IOException;
import java.util.List;

import com.thinkparity.codebase.model.migrator.Library;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.AbstractModelImpl;
import com.thinkparity.ophelia.model.Constants;
import com.thinkparity.ophelia.model.ParityErrorTranslator;
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
    void download(final Release release) throws ParityException {
        logger.logApiId();
        logger.logVariable("variable", release);
        final List<Library> libraries = readLibraries(release.getId());
        final DownloadHelper helper = new DownloadHelper(internalModelFactory, release);
        try {
            if(!helper.isStarted()) { helper.start(libraries); }
            helper.resume();
        }
        catch(final IOException iox) { throw ParityErrorTranslator.translate(iox); }
    }

    /**
     * Determine if the download for a release is complete.
     * 
     * @param release
     *            A release.
     * @return True the download for the latest version is complete.
     */
    Boolean isComplete(final Release release) throws ParityException {
        logger.logApiId();
        logger.logVariable("variable", release);
        // check to see if the latest release has already been downloaded
        final DownloadHelper download = new DownloadHelper(internalModelFactory, release);
        try { return download.isComplete(); }
        catch(final IOException iox) { throw ParityErrorTranslator.translate(iox); }
    }

    /**
     * Read the latest release that has been downloaded.
     * 
     * @return A release.
     */
    Release read() throws ParityException {
        final Release latest = readLatestRelease();
        final DownloadHelper helper = new DownloadHelper(internalModelFactory, latest);
        try { if(helper.isComplete()) { return latest; } else { return null; } }
        catch(final IOException iox) { throw ParityErrorTranslator.translate(iox); }
    }

    private Release readLatestRelease() {
        return getInternalReleaseModel().readLatest(
                Constants.Release.ARTIFACT_ID, Constants.Release.GROUP_ID);
    }

    private List<Library> readLibraries(final Long releaseId) {
        return getInternalReleaseModel().readLibraries(releaseId);
    }
}

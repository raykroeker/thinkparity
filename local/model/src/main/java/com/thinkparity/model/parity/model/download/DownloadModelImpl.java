/*
 * Created On: 
 * $Id$
 */
package com.thinkparity.model.parity.model.download;

import java.io.IOException;
import java.util.List;

import com.thinkparity.model.Constants;
import com.thinkparity.model.parity.ParityErrorTranslator;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.download.helper.DownloadHelper;
import com.thinkparity.model.parity.model.workspace.Workspace;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Release;

/**
 * The parity bootstrap download interface implementation.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
class DownloadModelImpl extends AbstractModelImpl {

    /** Create DownloadModelImpl. */
    DownloadModelImpl(final Workspace workspace) { super(workspace); }

    /**
     * Download a release.
     * 
     * @param release
     *            A release.
     * @throws ParityException
     */
    void download(final Release release) throws ParityException {
        logger.info("[LMODEL] [DOWNLOAD MODEL] [DOWNLOAD]");
        logger.debug(release);
        final List<Library> libraries = readLibraries(release.getId());
        final DownloadHelper helper = new DownloadHelper(getContext(), release);
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
        logger.info("[LMODEL] [DOWNLOAD MODEL] [IS COMPLETE]");
        logger.debug(release);
        // check to see if the latest release has already been downloaded
        final DownloadHelper download = new DownloadHelper(getContext(), release);
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
        final DownloadHelper helper = new DownloadHelper(getContext(), latest);
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

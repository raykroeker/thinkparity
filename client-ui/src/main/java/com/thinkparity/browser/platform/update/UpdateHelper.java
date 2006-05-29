/*
 * Created On: Sat May 27 2006 09:48 PDT
 * $Id$
 */
package com.thinkparity.browser.platform.update;

import com.thinkparity.browser.BrowserException;
import com.thinkparity.browser.Constants;
import com.thinkparity.browser.Version;
import com.thinkparity.browser.platform.BrowserPlatform;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.download.DownloadModel;
import com.thinkparity.model.parity.model.install.InstallModel;
import com.thinkparity.model.parity.model.release.ReleaseModel;

import com.thinkparity.migrator.Release;
import com.thinkparity.migrator.ReleaseDateComparator;

/**
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class UpdateHelper {

    /** The current release. */
    private Release currentRelease;

    /** The parity download interface. */
    private final DownloadModel dModel;

    /** The parity install interface. */
    private final InstallModel iModel;

    /** The latest release. */
    private Release latestRelease;

    /** The parity release interface. */
    private final ReleaseModel rModel;

    /** Create UpdateHelper. */
    public UpdateHelper(final BrowserPlatform platform) {
        super();
        this.dModel = platform.getModelFactory().getDownload(getClass());
        this.iModel = platform.getModelFactory().getInstall(getClass());
        this.rModel = platform.getModelFactory().getRelease(getClass());
    }

    /**
     * Determine whether or not an update is available.
     *
     * @return True if an update is available; false otherwise.
     */
    public Boolean isAvailable() {
        final Release current = readCurrentRelease();
        final Release latest = readLatestRelease();
        return new ReleaseDateComparator(latest).isAfter(current);
    }

    /** Update. */
    public void update() {
        final Release release = readLatestRelease();

        // if the download is not complete; download
        try { if(!dModel.isComplete(release)) { dModel.download(release); } }
        catch(final ParityException px) { throw new BrowserException("", px); }

        try { iModel.install(release); }
        catch(final ParityException px) { throw new BrowserException("", px); }
    }

    /**
     * Read the current release.
     * 
     * @return The current release.
     */
    private Release readCurrentRelease() {
        if(null == currentRelease) {
            currentRelease = rModel.read(
                    Constants.Release.ARTIFACT_ID, Constants.Release.GROUP_ID,
                    Version.getVersion());
        }
        return currentRelease;
    }

    /**
     * Read the latest release.
     * 
     * @return The latest release.
     */
    private Release readLatestRelease() {
        if(null == latestRelease) {
            latestRelease = rModel.readLatest(
                    Constants.Release.ARTIFACT_ID, Constants.Release.GROUP_ID);
        }
        return latestRelease;
    }
}

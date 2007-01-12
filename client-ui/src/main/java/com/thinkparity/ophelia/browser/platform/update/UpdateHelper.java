/*
 * Created On: Sat May 27 2006 09:48 PDT
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.update;

import java.util.Properties;

import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.migrator.ReleaseDateComparator;

import com.thinkparity.ophelia.model.ParityException;
import com.thinkparity.ophelia.model.download.DownloadModel;
import com.thinkparity.ophelia.model.install.InstallModel;
import com.thinkparity.ophelia.model.migrator.ReleaseModel;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.Constants;
import com.thinkparity.ophelia.browser.platform.Platform;

import org.apache.log4j.Logger;

/**
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class UpdateHelper {

    /** The current release. */
    private Release currentRelease;

    /** The parity download interface. */
    private final DownloadModel downloadModel;

    /** The parity install interface. */
    private final InstallModel installModel;

    /** The latest release. */
    private Release latestRelease;

    /** An apache logger. */
    private final Logger logger;

    /** The platform. */
    private Platform platform;

    /** The parity release interface. */
    private final ReleaseModel releaseModel;

    /**
     * Create UpdateHelper.
     * 
     * @param platform
     *            A thinkParity <code>Platform</code>.
     */
    public UpdateHelper(final Platform platform) {
        super();
        this.platform = platform;

        this.downloadModel = platform.getModelFactory().getDownloadModel(getClass());
        this.installModel = platform.getModelFactory().getInstallModel(getClass());
        this.releaseModel = platform.getModelFactory().getReleaseModel(getClass());
        this.logger = platform.getLogger(getClass());
    }

    /**
     * Determine whether or not an update is available.
     *
     * @return True if an update is available; false otherwise.
     */
    public Boolean isAvailable() {
        if(!isOnline()) { return Boolean.FALSE; }

        final Release current = readCurrentRelease();
        logger.debug(current);
        final Release latest = readLatestRelease();
        logger.debug(latest);
        if(null == latest) { return Boolean.FALSE; }
        if(null == current) { return Boolean.TRUE; }
        else { return new ReleaseDateComparator(latest).isAfter(current); }
    }

    /** Update. */
    public void update() {
        final Release release = readLatestRelease();

        // download
        try { if(!downloadModel.isComplete(release)) { downloadModel.download(release); } }
        catch(final ParityException px) { throw new BrowserException("", px); }

        // install
        installModel.install(release);

        // set property for restart
        final Properties properties = new Properties();
        properties.setProperty("parity.image.name", release.getVersion());
        platform.restart(properties);
    }

    /**
     * Determine if the user has online capability.
     * 
     * @return True if the user has online capability.
     */
    private Boolean isOnline() { return platform.isOnline(); }

    /**
     * Read the current release.
     * 
     * @return The current release.
     */
    private Release readCurrentRelease() {
        if(null == currentRelease) {
            currentRelease = releaseModel.read(
                    Constants.Release.ARTIFACT_ID,
                    Constants.Release.GROUP_ID,
                    Constants.Release.VERSION);
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
            latestRelease = releaseModel.readLatest(
                    Constants.Release.ARTIFACT_ID, Constants.Release.GROUP_ID);
        }
        return latestRelease;
    }
}

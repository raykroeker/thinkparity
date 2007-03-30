/*
 * Created On:  1-Feb-07 2:05:02 PM
 */
package com.thinkparity.ophelia.browser.platform.migrator;

import com.thinkparity.ophelia.model.migrator.MigratorModel;

import com.thinkparity.ophelia.browser.platform.Platform;

/**
 * <b>Title:</b>thinkParity OpheliaUI Platform Migrator Helper<br>
 * <b>Description:</b>Interacts with the migrator model to monitor and install
 * the latest release for the product.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class MigratorHelper {

    /** An instance of <code>MigratorModel</code>. */
    private final MigratorModel model;

    /**
     * Create MigratorHelper.
     * 
     * @param platform
     *            The <code>Platform</code>.
     */
    public MigratorHelper(final Platform platform) {
        super();
        this.model = platform.getModelFactory().getMigratorModel(getClass());
    }

    /**
     * Determine if this is the latest release.
     * 
     * @return True if this is the latest release.
     */
    public Boolean isLatestRelease() {
        return model.isLatestRelease();
    }

    /**
     * Start a thread to download the latest release.
     *
     */
    public void startDownloadLatestRelease() {
        model.startDownloadLatestRelease();
    }
}

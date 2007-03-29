/*
 * Created On:  1-Feb-07 2:05:02 PM
 */
package com.thinkparity.ophelia.browser.platform.migrator;

import com.thinkparity.codebase.model.migrator.Release;

import com.thinkparity.ophelia.model.migrator.MigratorModel;
import com.thinkparity.ophelia.model.util.ProcessMonitor;

import com.thinkparity.ophelia.browser.Constants.Directories;
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
     * Intialize the installed release.
     *
     */
    public void initializeRelease(final ProcessMonitor monitor) {
        model.initializeRelease(monitor, Directories.ThinkParity.DIR);
    }

    /**
     * Determine if the installed release is the latest release.
     * 
     * @return True if the installed release is the latest release.
     */
    public Boolean isLatestRelease() {
        return model.isLatestRelease();
    }

    /**
     * Determine if the installed release is initialized.
     * 
     * @return True if the installed release is initialized.
     */
    public Boolean isReleaseInitialized() {
        return model.isReleaseInitialized();
    }

    /**
     * Install the latest release.
     *
     */
    public void installRelease(final ProcessMonitor monitor) {
        model.installRelease(monitor, Directories.ThinkParity.DIR);
    }

    /**
     * Read the installed release.
     * 
     * @return A <code>Release</code>.
     */
    public Release readRelease() {
        return model.readRelease();
    }
}

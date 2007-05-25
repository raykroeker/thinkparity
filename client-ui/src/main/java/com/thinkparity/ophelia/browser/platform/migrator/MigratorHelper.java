/*
 * Created On:  1-Feb-07 2:05:02 PM
 */
package com.thinkparity.ophelia.browser.platform.migrator;

import com.thinkparity.ophelia.model.migrator.MigratorModel;
import com.thinkparity.ophelia.model.util.ProcessMonitor;

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
     * Initialize the installed release.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     */
    public void initializeRelease(final ProcessMonitor monitor) {
        model.initializeRelease(monitor);
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
     * Detemine if the downloaded release is installed.
     * 
     * @return True if the downloaded release is installed.
     */
    public Boolean isReleaseInstalled() {
        return model.isReleaseInstalled();
    }

    /**
     * Install the release.
     * @param monitor A <code>ProcessMonitor</cod
     */
    public void startInstallRelease(final ProcessMonitor monitor) {
        model.startInstallRelease(monitor);
    }
}

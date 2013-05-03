/*
 * Created On:  23-Jan-07 4:24:24 PM
 */
package com.thinkparity.ophelia.model.migrator;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.util.jta.TransactionType;

import com.thinkparity.ophelia.model.events.MigratorListener;
import com.thinkparity.ophelia.model.util.ProcessMonitor;

/**
 * <b>Title:</b>thinkParity Migrator Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface MigratorModel {

    /**
     * Add a migrator listener.
     * 
     * @param listener
     *            A migrator listener.
     */
    @ThinkParityTransaction(TransactionType.NEVER)
    public void addListener(final MigratorListener listener);

    /**
     * Initialize the installed release.
     *
     */
    public void initializeRelease(final ProcessMonitor monitor);

    /**
     * Determine whether or not the installed release has been initialized.
     * 
     * @return True if the installed release has been initialized.
     */
    public Boolean isReleaseInitialized();

    /**
     * Determine whether or not the downloaded release is installed.
     * 
     * @return True if the release installed.
     */
    public Boolean isReleaseInstalled();

    /**
     * Remove a migrator listener.
     * 
     * @param listener
     *            A migrator listener.
     */
    @ThinkParityTransaction(TransactionType.NEVER)
    public void removeListener(final MigratorListener listener);

    /**
     * Start the download of the latest release.
     *
     */
    public void startDownloadRelease();

    /**
     * Start the installation of the release.
     *
     */
    public void startInstallRelease(final ProcessMonitor monitor);
}

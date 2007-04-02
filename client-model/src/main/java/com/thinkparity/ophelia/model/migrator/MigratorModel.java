/*
 * Created On:  23-Jan-07 4:24:24 PM
 */
package com.thinkparity.ophelia.model.migrator;

import java.io.File;
import java.util.List;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.migrator.Resource;
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
     * Deploy. Take the product, release resources and deploy them within the
     * migrator. The file is a zip file containing the resources's content.
     * 
     * @param monitor
     *            A <code>DeployMonitor</code>.
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     * @param resources
     *            A <code>List</code> of <code>Resources</code>.
     * @param file
     *            A <code>File</code>.
     */
    public void deploy(final ProcessMonitor monitor, final Product product,
            final Release release, final List<Resource> resources,
            final File file);

    /**
     * Initialize the installed release.
     *
     */
    public void initializeInstalledRelease(final ProcessMonitor monitor);

    /**
     * Determine whether or not the installed release has been initialized.
     * 
     * @return True if the installed release has been initialized.
     */
    public Boolean isInstalledReleaseInitialized();

    /**
     * Determine if the installed release is the latest release.
     * 
     * @return True if the installed release is the latest release.
     */
    public Boolean isLatestRelease();

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
    public void startDownloadLatestRelease();
}

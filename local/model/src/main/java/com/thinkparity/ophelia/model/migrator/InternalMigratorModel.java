/*
 * Created On:  23-Jan-07 4:24:24 PM
 */
package com.thinkparity.ophelia.model.migrator;

import java.lang.reflect.Method;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.util.jta.TransactionType;
import com.thinkparity.codebase.model.util.xmpp.event.ProductReleaseDeployedEvent;

import com.thinkparity.ophelia.model.util.ProcessMonitor;

/**
 * <b>Title:</b>thinkParity Internal Migrator Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface InternalMigratorModel extends MigratorModel {

    /**
     * Handle the product release deployed remote event.
     * 
     * @param event
     *            A <code>ProductReleaseDeployedEvent</code>.
     */
    public void handleProductReleaseDeployed(
            final ProductReleaseDeployedEvent event);

    /**
     * Initialize the migrator model. We create a product and the release. A
     * check for the latest version is also made and a download/install process
     * started if we are not.
     * 
     */
    public void initialize(final ProcessMonitor monitor);

    /**
     * Log an error.
     * 
     * @param cause
     *            The <code>Throwable</code> cause of the error.
     * @param method
     *            The executing <code>Method</code>.
     * @param arguments
     *            The executing method arguments <code>Object[]</code>.
     */
    public void logError(final Throwable cause, final Method method,
            final Object[] arguments);

    public void updateDownloadedRelease(final String name);

    public void updateInstalledRelease(final String name);
}

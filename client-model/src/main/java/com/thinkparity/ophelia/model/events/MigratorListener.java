/*
 * Created On: Jun 29, 2006 8:58:17 AM
 * $Id$
 */
package com.thinkparity.ophelia.model.events;

import com.thinkparity.codebase.event.EventListener;

import com.thinkparity.ophelia.model.migrator.MigratorModel;

/**
 * <b>Title:</b>thinkParity Migrator Listener<br>
 * <b>Description:</b>A migrator listener is an interface used to notify any
 * and all clients of the local model about migration.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 * @see MigratorAdapter
 * @see MigratorEvent
 * @see MigratorModel#addListener
 */
public interface MigratorListener extends EventListener {

    /**
     * A product release was deployed.
     * 
     * @param e
     *            A <code>MigratorEvent</code>.
     */
    public void productReleaseDeployed(final MigratorEvent e);

    /**
     * A product release was downloaded.
     * 
     * @param e
     *            A <code>MigratorEvent</code>.
     */
    public void productReleaseDownloaded(final MigratorEvent e);

    /**
     * A product release has been installed.
     * 
     * @param e
     *            A <code>MigratorEvent</code>.
     */
    public void productReleaseInstalled(final MigratorEvent e);
}

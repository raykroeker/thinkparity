/*
 * Jun 5, 2005
 */
package com.thinkparity.ophelia.model.events;

/**
 * <b>Title:</b>thinkParity Migrator Listener Adapter<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class MigratorAdapter implements MigratorListener {

    /**
     * @see com.thinkparity.ophelia.model.events.MigratorListener#productReleaseDeployed(com.thinkparity.ophelia.model.events.MigratorEvent)
     *
     */
    public void productReleaseDeployed(final MigratorEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.MigratorListener#productReleaseDownloaded(com.thinkparity.ophelia.model.events.MigratorEvent)
     *
     */
    public void productReleaseDownloaded(final MigratorEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.MigratorListener#productReleaseInstalled(com.thinkparity.ophelia.model.events.MigratorEvent)
     *
     */
    public void productReleaseInstalled(final MigratorEvent e) {}
}

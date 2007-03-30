/*
 * Created On:  29-Mar-07 6:40:08 PM
 */
package com.thinkparity.ophelia.model.events;

/**
 * <b>Title:</b>thinkParity OpheliaModel Migrator Adpater<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class MigratorAdapater implements MigratorListener {

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

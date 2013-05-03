/*
 * Created On:  29-Mar-07 6:35:58 PM
 */
package com.thinkparity.ophelia.browser.platform;

import com.thinkparity.ophelia.model.events.MigratorAdapater;
import com.thinkparity.ophelia.model.events.MigratorEvent;
import com.thinkparity.ophelia.model.events.MigratorListener;

/**
 * <b>Title:</b>thinkParity OpheliaUI Platform Event Dispatcher<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class EventDispatcher {

    /** A <code>MigratorListener</code>. */
    private MigratorListener migratorListener;

    /** The <code>Platform</code>. */
    private final BrowserPlatform platform;

    /**
     * Create EventDispatcher.
     *
     */
    EventDispatcher(final BrowserPlatform platform) {
        super();
        this.platform = platform;
    }

    /**
     * End the platform event dispatcher.
     *
     */
    void end() {
        platform.getModelFactory().getMigratorModel(getClass()).removeListener(migratorListener);
        migratorListener = null;
    }

    /**
     * Start the platform event dispatcher.
     *
     */
    void start() {
        migratorListener = createMigratorListener();
        platform.getModelFactory().getMigratorModel(getClass()).addListener(migratorListener);
    }

    /**
     * Create a migrator listener.
     * 
     * @return A <code>MigratorListener</code>.
     */
    private MigratorListener createMigratorListener() {
        return new MigratorAdapater() {
            @Override
            public void productReleaseInstalled(final MigratorEvent e) {
                platform.fireProductReleaseInstalled(e);
            }
        };
    }
}

/*
 * Jan 19, 2006
 */
package com.thinkparity.ophelia.browser.plugin.archive;

import com.thinkparity.ophelia.browser.platform.plugin.PluginModelFactory;
import com.thinkparity.ophelia.browser.platform.plugin.PluginServices;
import com.thinkparity.ophelia.model.events.ContainerAdapter;
import com.thinkparity.ophelia.model.events.ContainerEvent;
import com.thinkparity.ophelia.model.events.ContainerListener;
import com.thinkparity.ophelia.model.events.SessionAdapter;
import com.thinkparity.ophelia.model.events.SessionListener;

/**
 * <b>Title:</b>thinkParity Archive Plugin Event Dispatcher<br>
 * <b>Description:</b>Handles local and remote events for the archive plugin.</br>
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
final class EventDispatcher {

	/** A thinkParity container interface listener. */
    private ContainerListener containerListener;

	/** A thinkParity session interface listener.*/
	private SessionListener sessionListener;

    /** A thinkParity <code>PluginModelFactory</code>. */
    private final PluginModelFactory modelFactory;

    /** The thinkParity <code>ArchivePlugin</code>. */
    private final ArchivePlugin plugin;

	/**
     * Create EventDispatcher.
     * 
     * @param services
     *            The thinkParity <code>PluginServices</code>.
     */
	EventDispatcher(final ArchivePlugin plugin, final PluginServices services) {
		super();
        this.plugin = plugin;
        this.modelFactory = services.getModelFactory();
	}

	/**
	 * End the event dispatcher.
	 *
	 */
	void end() {
        modelFactory.getContainerModel().removeListener(containerListener);
        containerListener = null;
        
        modelFactory.getSessionModel().removeListener(sessionListener);
		sessionListener = null;
	}

	/**
	 * Start the event dispatcher.
	 *
	 */
	void start() {
        containerListener = createContainerListener();
        modelFactory.getContainerModel().addListener(containerListener);

		sessionListener = createSessionListener();
        modelFactory.getSessionModel().addListener(sessionListener);
	}

    /**
     * Create a container event listener.
     * 
     * @return A <code>ContainerListener</code>.
     */
    private ContainerListener createContainerListener() {
        return new ContainerAdapter() {
            @Override
            public void containerArchived(final ContainerEvent e) {
                plugin.fireContainerArchived(e.getContainer().getUniqueId());
            }
            @Override
            public void containerRestored(final ContainerEvent e) {
                plugin.fireContainerRestored(e.getContainer().getUniqueId());
            }
        };     
    }

	/**
     * Create a session event listener.
     * 
     * @return A <code>SessionListener</code>.
     */
	private SessionListener createSessionListener() {
        return new SessionAdapter() {
            @Override
            public void sessionEstablished() {
                plugin.fireSessionEstablished();
            }
            @Override
            public void sessionTerminated() {
                plugin.fireSessionTerminated();
            }
            @Override
            public void sessionTerminated(final Throwable cause) {
                sessionTerminated();
            }
        };
    }
}

/*
 * Created On: Sep 20, 2006 12:35:53 PM
 */
package com.thinkparity.ophelia.browser.platform.plugin;

import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.session.SessionModel;

import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.util.ModelFactory;

/**
 * <b>Title:</b>thinkParity Browser Platform Plugin Model Factory<br>
 * <b>Description:</b>Gives the plugins access to the thinkParity model
 * interfaces.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PluginModelFactory {

    /** The platform's <code>ModelFactory</code>. */
    private final ModelFactory factory;

    /**
     * Create PluginModelFactory.
     * 
     * @param platform
     *            The thinkParity <code>Platform</code>.
     */
    PluginModelFactory(final Platform platform) {
        super();
        this.factory = platform.getModelFactory();
    }

    /**
     * Obtain A thinkParity <code>ContainerModel</code> interface.
     * 
     * @return A thinkParity <code>ContainerModel</code> interface.
     */
    public ContainerModel getContainerModel() {
        return factory.getContainerModel(getClass());
    }

    /**
     * Obtain A thinkParity <code>SessionModel</code> interface.
     * 
     * @return A thinkParity <code>SessionModel</code> interface.
     */
    public SessionModel getSessionModel() {
        return factory.getSessionModel(getClass());
    }
}

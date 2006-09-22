/*
 * Created On: Sep 20, 2006 12:35:53 PM
 */
package com.thinkparity.ophelia.browser.platform.plugin;

import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.util.ModelFactory;
import com.thinkparity.ophelia.model.archive.ArchiveModel;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class PluginModelFactory {

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
     * Obtain A thinkParity <code>ArchiveTabModel</code> interface.
     * 
     * @return A thinkParity <code>ArchiveTabModel</code> interface.
     */
    public ArchiveModel getArchiveModel() {
        return factory.getArchiveModel(getClass());
    }
}

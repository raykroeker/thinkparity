/*
 * Created On: Sep 21, 2006 2:24:16 PM
 */
package com.thinkparity.ophelia.browser.plugin.archive;

import java.util.UUID;

import com.thinkparity.ophelia.browser.platform.plugin.Plugin;
import com.thinkparity.ophelia.browser.platform.plugin.PluginId;
import com.thinkparity.ophelia.browser.platform.plugin.PluginRegistry;
import com.thinkparity.ophelia.browser.platform.plugin.PluginServices;
import com.thinkparity.ophelia.browser.plugin.archive.tab.ArchiveTab;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ArchivePlugin implements Plugin {

    /** The thinkParity archive plugin's <code>EventDispatcher</code>. */
    private EventDispatcher eventDispatcher;

    /**
     * Create ArchivePlugin.
     * 
     */
    public ArchivePlugin() {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.plugin.Plugin#end()
     */
    public void end() {
        eventDispatcher.end();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.plugin.Plugin#initialize(com.thinkparity.ophelia.browser.platform.plugin.PluginServices)
     * 
     */
    public void initialize(final PluginServices services) {
        this.eventDispatcher = new EventDispatcher(this, services);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.plugin.Plugin#start()
     */
    public void start() {
        eventDispatcher.start();
    }

    /**
     * Notify the plugin a container has been archived.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     */
    void fireContainerArchived(final UUID uniqueId) {
        getArchiveTab().fireContainerArchived(uniqueId);
    }

    /**
     * Notify the plugin a container has been restored.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     */
    void fireContainerRestored(final UUID uniqueId) {
        getArchiveTab().fireContainerRestored(uniqueId);
    }

    /**
     * Notify the plugin the session has been established.
     *
     */
    void fireSessionEstablished() {}

    /**
     * Notify the plugin the session has been terminated.
     *
     */
    void fireSessionTerminated() {}

    /**
     * Obtain the archive tab from the plugin registry.
     * 
     * @return An archive tab.
     */
    private ArchiveTab getArchiveTab() {
        return (ArchiveTab) new PluginRegistry().getTabExtension(PluginId.ARCHIVE, "ArchiveTab");
    }

}

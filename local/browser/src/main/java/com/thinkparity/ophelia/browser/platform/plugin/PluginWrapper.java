/*
 * Created On: Sep 20, 2006 11:54:49 AM
 */
package com.thinkparity.ophelia.browser.platform.plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <b>Title:</b>thinkParity Browser Plugin Wrapper<br>
 * <b>Description:</b>The plugin wrapper is a thin wrapper around the
 * instantiated plugins that loads the plugin; the plugin extensions and
 * provides the services to the plugin.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class PluginWrapper {

    /** A list of extension names and extensions. */
    private final Map<String, PluginExtension> extensions;

    /** The plugin meta info. */
    private PluginMetaInfo metaInfo;

    /** The plugin. */
    private Plugin plugin;

    PluginWrapper() {
        super();
        this.extensions = new HashMap<String, PluginExtension>();
    }

    PluginExtension addExtension(final String name,
            final PluginExtension extension) {
        return extensions.put(name, extension);
    }

    void clearExtensions() {
        extensions.clear();
    }

    PluginExtension getExtension(final String name) {
        return extensions.get(name);
    }

    List<PluginExtension> getExtensions() {
        final List<PluginExtension> extensions = new ArrayList<PluginExtension>();
        extensions.addAll(this.extensions.values());
        return Collections.unmodifiableList(extensions);
    }

    /**
     * Obtain the metaInfo
     *
     * @return The PluginMetaInfo.
     */
    PluginMetaInfo getMetaInfo() {
        return metaInfo;
    }

    /**
     * Obtain the plugin
     *
     * @return The Plugin.
     */
    Plugin getPlugin() {
        return plugin;
    }

    PluginExtension removeExtension(final String extensionName) {
        return extensions.remove(extensionName);
    }

    /**
     * Set metaInfo.
     *
     * @param metaInfo The PluginMetaInfo.
     */
    void setMetaInfo(PluginMetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }

    /**
     * Set plugin.
     *
     * @param plugin The Plugin.
     */
    void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }
}

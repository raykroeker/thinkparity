/*
 * Created On: Sep 21, 2006 8:18:23 AM
 */
package com.thinkparity.ophelia.browser.platform.plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class PluginRegistry {

    /** The plugins. */
    private static final Map<String, PluginWrapper> PLUGINS;

    static {
        PLUGINS = new HashMap<String, PluginWrapper>(2, 1.0F);
    }

    /**
     * Create PluginRegistry.
     * 
     */
    public PluginRegistry() {
        super();
    }

    /**
     * Obtain a specific plugin.
     * 
     * @param id
     *            A plugin id.
     * @return A <code>Plugin</code>.
     */
    public Plugin getPlugin(final PluginId id) {
        return (Plugin) PLUGINS.get(id.toId());
    }

    /**
     * Obtain a list of all of the plugins.
     * 
     * @param filter
     *            A plugin <code>Filter</code>.
     * @return A <code>List&lt;Plugin&gt;</code>.
     */
    public List<Plugin> getPlugins() {
        final List<Plugin> plugins = new ArrayList<Plugin>();
        for (final Object plugin : PLUGINS.values()) {
            plugins.add((Plugin) plugin);
        }
        return Collections.unmodifiableList(plugins);
    }

    public <T extends PluginExtension> T getExtension(final PluginId id,
            final String name) {
        return null;
    }

    /**
     * Eradicate a plugin from the registry.
     * 
     * @param plugin
     *            A <code>PluginWrapper</code>.
     */
    void eradicate(final PluginWrapper plugin) {
        synchronized (PLUGINS) {
            Assert.assertTrue(PLUGINS.containsValue(plugin),
                    "Registry does not contain {0}.", plugin);
            PLUGINS.remove(plugin.getId());
        }
    }

    /**
     * Obtain a list of the plugin wrappers.
     * 
     * @return A <code>List&lt;PluginWrapper&gt;</code>.
     */
    List<PluginWrapper> getPluginWrappers() {
        final List<PluginWrapper> wrappers = new ArrayList<PluginWrapper>(PLUGINS.size());
        wrappers.addAll(PLUGINS.values());
        return Collections.unmodifiableList(wrappers);
    }

    /**
     * Register a plugin with the registry.
     * 
     * @param plugin
     *            A <code>PluginWrapper</code>.
     */
    void register(final PluginWrapper plugin) {
        synchronized (PLUGINS) {
            Assert.assertNotTrue(PLUGINS.containsKey(plugin.getId()),
                    "Registry already contains plugin {0}.", plugin.getId());
            PLUGINS.put(plugin.getId(), plugin);
        }
    }
}

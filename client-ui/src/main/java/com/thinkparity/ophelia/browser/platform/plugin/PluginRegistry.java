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

import com.thinkparity.ophelia.browser.platform.plugin.extension.ActionExtension;
import com.thinkparity.ophelia.browser.platform.plugin.extension.TabExtension;

/**
 * <b>Title:</b>thinkParity Browser Platform Plugin Registry<br>
 * <b>Description:</b>Used to integrate the plugin extensions into the browser
 * platform.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class PluginRegistry {

    /** A list of plugin loaders. */
    private static final Map<String, PluginLoader> LOADERS;

    /** The plugins. */
    private static final Map<String, PluginWrapper> PLUGINS;

    static {
        LOADERS = new HashMap<String, PluginLoader>(2);
        PLUGINS = new HashMap<String, PluginWrapper>(2);
    }

    /**
     * Create PluginRegistry.
     * 
     */
    public PluginRegistry() {
        super();
    }

    public ActionExtension getActionExtension(final PluginId id, final String name) {
        if (PLUGINS.containsKey(toId(id))) {
            final PluginWrapper wrapper = PLUGINS.get(toId(id));
            return (ActionExtension) wrapper.getExtension(name);
        } else {
            return null;
        }
    }

    /**
     * Obtain a specific plugin.
     * 
     * @param id
     *            A plugin id.
     * @return A <code>Plugin</code>.
     */
    public Plugin getPlugin(final PluginId id) {
        final PluginWrapper wrapper = PLUGINS.get(toId(id));
        if (null == wrapper) {
            return null;
        } else {
            return wrapper.getPlugin();
        }
    }

    /**
     * Obtain a list of all of the plugins.
     * 
     * @return A <code>List&lt;Plugin&gt;</code>.
     */
    public List<Plugin> getPlugins() {
        final List<Plugin> plugins = new ArrayList<Plugin>();
        for (final PluginWrapper wrapper : PLUGINS.values()) {
            plugins.add(wrapper.getPlugin());
        }
        return Collections.unmodifiableList(plugins);
    }

    public TabExtension getTabExtension(final PluginId id, final String name) {
        if (PLUGINS.containsKey(toId(id))) {
            final PluginWrapper wrapper = PLUGINS.get(toId(id));
            return (TabExtension) wrapper.getExtension(name);
        } else {
            return null;
        }
    }

    /**
     * Register a <code>PluginWrapper</code>.
     * 
     * @param wrapper
     *            A <code>PluginWrapper</code>.
     */
    void add(final PluginWrapper wrapper, final PluginLoader loader) {
        synchronized (PLUGINS) {
            Assert.assertNotTrue(PLUGINS.containsKey(toId(wrapper)),
                    "Plugin {0} already registered.",
                    wrapper.getPlugin());
            Assert.assertNotTrue(LOADERS.containsKey(toId(wrapper)),
                    "Plugin {0}'s loader already registered.",
                    wrapper.getPlugin());
            PLUGINS.put(toId(wrapper), wrapper);
            LOADERS.put(toId(wrapper), loader);
        }
    }

    /**
     * Obtain the registered plugin loader.
     * @param wrapper A plugin wrapper.
     * @return
     */
    PluginLoader getLoader(final PluginWrapper wrapper) {
        synchronized (LOADERS) {
            Assert.assertTrue(LOADERS.containsKey(toId(wrapper)),
                    "Loader for plugin {0} does not exist.", wrapper.getPlugin());
            return LOADERS.get(toId(wrapper));
        }
    }

    /**
     * Obtain the list of registered plugin wrappers.
     * 
     * @return A <code>List&lt;PluginWrapper&gt;</code>.
     */
    List<PluginWrapper> getWrappers() {
        final List<PluginWrapper> wrappers = new ArrayList<PluginWrapper>();
        wrappers.addAll(PLUGINS.values());
        return Collections.unmodifiableList(wrappers);
    }

    /**
     * Remove plugin from the registry.
     * 
     * @param wrapper
     *            A <code>PluginWrapper</code>.
     */
    void remove(final PluginWrapper wrapper) {
        synchronized (PLUGINS) {
            Assert.assertTrue(PLUGINS.containsKey(toId(wrapper)),
                    "Plugin {0} not registered.",
                    wrapper.getPlugin());
            PLUGINS.remove(toId(wrapper));
        }
    }

    /**
     * Create a registry id from a plugin id.
     * 
     * @param id
     *            A plugin id.
     * @return A registry id.
     */
    private String toId(final PluginId id) {
        switch (id) {
        case ARCHIVE:
            return "com.thinkparity.archive";
        case CORE:
            return "com.thinkparity.core";
        default:
            throw Assert.createUnreachable("UNKNOWN PLUGIN ID");
        }
    }

    /**
     * Create a registry id from a plugin wrapper.
     * 
     * @param wrapper
     *            A plugin wrapper.
     * @return A registry id.
     */
    private String toId(final PluginWrapper wrapper) {
        return wrapper.getMetaInfo().getPluginId();
    }
}

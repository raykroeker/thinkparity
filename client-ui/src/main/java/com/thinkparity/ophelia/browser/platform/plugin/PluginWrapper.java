/*
 * Created On: Sep 20, 2006 11:54:49 AM
 */
package com.thinkparity.ophelia.browser.platform.plugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.JVMUniqueId;
import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class PluginWrapper {

    /** The plugin's <code>File</code>. */
    private final File file;

    /** An instance of the plugin. */
    private Plugin plugin;

    /** Instances of the plugin extensions. */
    private final List<PluginExtension> pluginExtensions;

    /** The plugin services. */
    private final PluginServices pluginServices;

    /**
     * Create PluginWrapper.
     * 
     * @param file
     *            The plugin's <code>File</code>.
     */
    PluginWrapper(final File file, final PluginServices pluginServices) {
        super();
        this.file = file;
        this.pluginExtensions = new ArrayList<PluginExtension>();
        this.pluginServices = pluginServices;
    }

    /**
     * End the plugin.
     *
     */
    void end() {
        Assert.assertNotNull(plugin, "Plugin not loaded.");
        plugin.end();
    }

    /**
     * Obtain the plugin wrapper id.
     * 
     * @return A unique plugin wrapper id <code>String</code>.
     */
    String getId() {
        return JVMUniqueId.nextId().toString();
    }

    /**
     * Initialize the plugin.
     *
     */
    void initialize() {
        Assert.assertNotNull(plugin, "Plugin not loaded.");
        plugin.initialize();
    }

    /**
     * Load the plugin. Check to see if the plugin has been extracted; and if
     * not extract the plugin. Create a class loader for the plugin and use it
     * to create an instance of the plugin.
     * 
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    void load() throws IOException, ClassNotFoundException,
            IllegalAccessException, InstantiationException,
            CloneNotSupportedException, NoSuchMethodException,
            InvocationTargetException {
        final PluginMetaInfo pluginMetaInfo = new PluginMetaInfo(file);
        final PluginExtractor extractor = new PluginExtractor(file);
        if (!extractor.isExtracted()) {
            extractor.extract();
        }
        final FileSystem pluginFileSystem = extractor.getFileSystem();
        final PluginServices pluginServices =
                (PluginServices) this.pluginServices.clone();
        pluginServices.setMetaInfo(pluginMetaInfo);
        
        final PluginClassLoader pluginClassLoader =
            new PluginClassLoader(pluginFileSystem);
        this.plugin = pluginClassLoader.loadPlugin(pluginMetaInfo, pluginServices);
        for (final String extension : pluginMetaInfo.getExtensions()) {
            pluginServices.setExtensionMetaInfo(
                    pluginMetaInfo.getExtensionMetaInfo(extension));
            this.pluginExtensions.add(
                    pluginClassLoader.loadExtension(
                            pluginMetaInfo.getExtensionMetaInfo(extension),
                            pluginServices));
        }
    }

    /**
     * Start the plugin.
     *
     */
    void start() {
        Assert.assertNotNull(plugin, "Plugin not loaded.");
        plugin.start();
    }
}

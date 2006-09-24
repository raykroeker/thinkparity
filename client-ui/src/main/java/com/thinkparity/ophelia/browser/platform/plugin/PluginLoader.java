/*
 * Created On: Sep 23, 2006 10:59:35 AM
 */
package com.thinkparity.ophelia.browser.platform.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.thinkparity.codebase.FileSystem;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class PluginLoader extends PluginUtility {

    /** The plugin <code>FileSystem</code>. */
    private final FileSystem pluginFileSystem;

    /** The plugin registry. */
    private final PluginRegistry pluginRegistry;

    /**
     * Create PluginLoader.
     * 
     * @param pluginFileSystem
     *            The plugin <code>FileSystem</code>.
     */
    PluginLoader(final FileSystem pluginFileSystem) {
        super();
        this.pluginFileSystem = pluginFileSystem;
        this.pluginRegistry = new PluginRegistry();
    }

    /**
     * Load a plugin.
     * 
     */
    void load() {
        try {
            final PluginMetaInfo metaInfo = readMetaInfo();
            final PluginClassLoader classLoader = new PluginClassLoader(pluginFileSystem);
            
            final PluginWrapper wrapper = new PluginWrapper();
            wrapper.setMetaInfo(metaInfo);
            wrapper.setPlugin(loadPlugin(metaInfo, classLoader));
            final List<String> platformExtensions = metaInfo.getPlatformExtensions();
            for (final String platformExtension : platformExtensions) {
                wrapper.addExtension(
                        metaInfo.getExtensionName(platformExtension),
                        loadExtension(platformExtension, metaInfo, classLoader));
            }
            pluginRegistry.add(wrapper);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Load the extension.
     * 
     * @param extension
     *            An extension.
     * @param metaInfo
     *            The plugin's meta info.
     * @param classLoader
     *            The plugin's <code>ClassLoader</code>.
     * @return An <code>PlatformExtension</code>.
     */
    private PluginExtension loadExtension(final String extension,
            final PluginMetaInfo metaInfo, final PluginClassLoader classLoader) {
        return classLoader.loadExtension(metaInfo.getExtensionClass(extension));
    }

    /**
     * Load the plugin.
     * 
     * @param metaInfo
     *            The plugin's meta info.
     * @param classLoader
     *            The plugin's <code>ClassLoader</code>.
     * @return A <code>PluginInstance</code>.
     */
    private Plugin loadPlugin(final PluginMetaInfo metaInfo,
            final PluginClassLoader classLoader) {
        return classLoader.loadPlugin(metaInfo.getPluginClass());
    }

    /**
     * Open an input stream representing the manifest file within the plugin
     * file system.
     * 
     * @return An input stream.
     */
    private InputStream openMetaInfoInputStream() throws IOException {
        final File manifestFile = pluginFileSystem.findFile("/META-INF/PLUGIN.MF");
        return new FileInputStream(manifestFile);
    }

    /**
     * Read the plugin's meta info.
     * 
     * @return The plugin's meta info.
     */
    private PluginMetaInfo readMetaInfo() throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = openMetaInfoInputStream();
            return readMetaInfo(inputStream);
        } finally {
            inputStream.close();
            inputStream = null;
        }
    }
}

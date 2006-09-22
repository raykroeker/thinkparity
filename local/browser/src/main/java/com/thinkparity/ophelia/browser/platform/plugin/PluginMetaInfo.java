/*
 * Created On: Sep 20, 2006 11:58:07 AM
 */
package com.thinkparity.ophelia.browser.platform.plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Properties;

import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.assertion.Assert;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PluginMetaInfo {

    /**
     * Assert that the meta info key is set.
     * 
     * @param metaInfoKey
     *            A meta info key <code>String</code>.
     * @param metaInfo
     *            The meta info <code>Properties</code>.
     */
    private static void assertIsSet(final String metaInfoKey,
            final Properties metaInfo) {
        Assert.assertNotNull(metaInfo.getProperty(metaInfoKey, null),
                "Meta-info key {0} does not exist.", metaInfoKey);
    }

    /** The plugin meta info's <code>ClassLoader</code>. */
    private final ClassLoader pluginClassLoader;

    /** The plugin meta info <code>Properties</code>. */
    private final Properties pluginMetaInfo;

    /**
     * Create PluginMetaInfo.
     * 
     * @param file
     *            The plugin's <code>File</code>.
     */
    PluginMetaInfo(final File file) throws IOException {
        super();
        this.pluginClassLoader = new URLClassLoader(new URL[] { file.toURL() });
        this.pluginMetaInfo = new Properties();
        readMetaInfo();
    }

    /**
     * Obtain the meta info for an extension.
     * 
     * @param name
     *            The extension name <code>String</code>.
     * @return The meta info <code>Properties</code> for an extension.
     */
    PluginExtensionMetaInfo getExtensionMetaInfo(final String name) {
        final Properties extensionMetaInfo = new Properties();
        final String keyStart = new StringBuffer(name).append("-").toString();
        for (final Object key : pluginMetaInfo.keySet()) {
            if (key.toString().startsWith(keyStart)) {
                extensionMetaInfo.put(key, pluginMetaInfo.get(key));
            }
        }
        return new PluginExtensionMetaInfo(name, extensionMetaInfo);
    }

    /**
     * Obtain a list of all of the extensions.
     * 
     * @return A list of all of the extensions.
     */
    List<String> getExtensions() {
        return StringUtil.tokenize(
                getMetaInfo(MetaInfoKey.PLATFORM_EXTENSIONS), ",");
    }

    /**
     * Obtain the maximum platform version this plugin can run in.
     * 
     * @return A platform version.
     */
    String getPlatformMaxVersion() {
        return getMetaInfo(MetaInfoKey.PLATFORM_MAX_VERSION);
    }

    /**
     * Obtain the minimum platform version this plugin can run in.
     * 
     * @return A platform version.
     */
    String getPlatformMinVersion() {
        return getMetaInfo(MetaInfoKey.PLATFORM_MIN_VERSION);
    }

    /**
     * Obtain the class name of the plugin.
     * 
     * @return A class name.
     */
    String getPluginClass() {
        return getMetaInfo(MetaInfoKey.PLUGIN_CLASS);
    }

    /**
     * Obtain the plugin's name.
     * 
     * @return A name <code>String</code>.
     */
    String getPluginName() {
        return getMetaInfo(MetaInfoKey.PLUGIN_NAME);
    }

    /**
     * Obtain the plugin's version.
     * 
     * @return A version <code>String</code>.
     */
    String getPluginVersion() {
        return getMetaInfo(MetaInfoKey.PLUGIN_VERSION);
    }
    
    /**
     * Read the plugin meta info from the class loader.
     * 
     * @throws IOException
     */
    void readMetaInfo() throws IOException {
        final InputStream metaInfoStream =
            pluginClassLoader.getResourceAsStream("META-INF/PLUGIN.MF");
        try {
            pluginMetaInfo.load(metaInfoStream);
            assertIsSet(MetaInfoKey.PLUGIN_NAME, pluginMetaInfo);
            assertIsSet(MetaInfoKey.PLUGIN_CLASS, pluginMetaInfo);
            assertIsSet(MetaInfoKey.PLUGIN_VERSION, pluginMetaInfo);
            assertIsSet(MetaInfoKey.PLATFORM_EXTENSIONS, pluginMetaInfo);
            assertIsSet(MetaInfoKey.PLATFORM_MAX_VERSION, pluginMetaInfo);
            assertIsSet(MetaInfoKey.PLATFORM_MAX_VERSION, pluginMetaInfo);
        } finally {
            metaInfoStream.close();
        }
    }

    /**
     * Obtain a meta info value.
     * 
     * @param metaInfoKey
     *            A meta info key.
     * @return A meta info value.
     */
    private String getMetaInfo(final String metaInfoKey) {
        return pluginMetaInfo.getProperty(metaInfoKey);
    }

    /** The meta info keys used. */
    private static final class MetaInfoKey {
        private static final String PLATFORM_EXTENSIONS = "Platform-Extensions";
        private static final String PLATFORM_MAX_VERSION = "Platform-MaximumVersion";
        private static final String PLATFORM_MIN_VERSION = "Platform-MinimumVersion";
        private static final String PLUGIN_CLASS = "Plugin-Class";
        private static final String PLUGIN_NAME = "Plugin-Name";
        private static final String PLUGIN_VERSION = "Plugin-Version";
    }
}

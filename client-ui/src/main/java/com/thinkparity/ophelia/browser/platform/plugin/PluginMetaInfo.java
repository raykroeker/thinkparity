/*
 * Created On: Sep 20, 2006 11:58:07 AM
 */
package com.thinkparity.ophelia.browser.platform.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.thinkparity.common.StringUtil;

import com.thinkparity.codebase.assertion.Assert;

/**
 * <b>Title:</b>thinkParity Browser Platform Plugin Meta Info<br>
 * <b>Description:</b>The plugin meta info is a properties manifestation of
 * PLUGIN.MF<br>
 * 
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

    /**
     * Assert that the meta info key is set.
     * 
     * @param metaInfoKeyPattern
     *            A meta info key patern <code>String</code>.
     * @param metaInfoKeyArgument
     *            A meta info key pattern argument <code>String</code>.
     * @param metaInfo
     *            The meta info <code>Properties</code>.
     */
    private static void assertIsSet(final String metaInfoKeyPattern,
            final String metaInfoKeyArgument, final Properties metaInfo) {
        assertIsSet(MessageFormat.format(metaInfoKeyPattern,
                metaInfoKeyArgument), metaInfo);
    }

    /** The plugin meta info <code>Properties</code>. */
    private final Properties metaInfo;

    /**
     * Create PluginMetaInfo.
     * 
     */
    PluginMetaInfo() {
        super();
        this.metaInfo = new Properties();
    }

    /**
     * Obtain the meta info for an extension.
     * 
     * @param extension
     *            The extension <code>String</code>.
     * @return The meta info <code>Properties</code> for an extension.
     */
    String getExtensionClass(final String extension) {
        return getExtensionMetaInfo(extension, ExtensionInfoKey.EXTENSION_CLASS_PATTERN);
    }

    /**
     * Obtain the extension's name.
     * 
     * @param extension
     *            The extension <code>String</code>.
     * @return The extension's name.
     */
    String getExtensionName(final String extension) {
        return getExtensionMetaInfo(extension, ExtensionInfoKey.EXTENSION_NAME_PATTERN);
    }

    /**
     * Obtain a list of all of the extensions.
     * 
     * @return A list of all of the extensions.
     */
    List<String> getPlatformExtensions() {
        return StringUtil.tokenize(
                getMetaInfo(InfoKey.PLATFORM_EXTENSIONS), ",",
                new ArrayList<String>(7));
    }

    /**
     * Obtain the maximum platform version this plugin can run in.
     * 
     * @return A platform version.
     */
    String getPlatformMaxVersion() {
        return getMetaInfo(InfoKey.PLATFORM_MAX_VERSION);
    }

    /**
     * Obtain the minimum platform version this plugin can run in.
     * 
     * @return A platform version.
     */
    String getPlatformMinVersion() {
        return getMetaInfo(InfoKey.PLATFORM_MIN_VERSION);
    }

    /**
     * Obtain the class name of the plugin.
     * 
     * @return A class name.
     */
    String getPluginClass() {
        return getMetaInfo(InfoKey.PLUGIN_CLASS);
    }

    /**
     * Obtain the id of the plugin.
     * 
     * @return An id.
     */
    String getPluginId() {
        return getMetaInfo(InfoKey.PLUGIN_ID);
    }

    /**
     * Obtain the plugin's name.
     * 
     * @return A name <code>String</code>.
     */
    String getPluginName() {
        return getMetaInfo(InfoKey.PLUGIN_NAME);
    }

    /**
     * Obtain the plugin's version.
     * 
     * @return A version <code>String</code>.
     */
    String getPluginVersion() {
        return getMetaInfo(InfoKey.PLUGIN_VERSION);
    }
    
    /**
     * Read the plugin meta info from the class loader.
     * 
     * @throws IOException
     */
    void readMetaInfo(final InputStream metaInfoStream) throws IOException {
        metaInfo.load(metaInfoStream);
        assertIsSet(InfoKey.PLUGIN_NAME, metaInfo);
        assertIsSet(InfoKey.PLUGIN_CLASS, metaInfo);
        assertIsSet(InfoKey.PLUGIN_ID, metaInfo);
        assertIsSet(InfoKey.PLUGIN_VERSION, metaInfo);
        assertIsSet(InfoKey.PLATFORM_EXTENSIONS, metaInfo);
        assertIsSet(InfoKey.PLATFORM_MAX_VERSION, metaInfo);
        assertIsSet(InfoKey.PLATFORM_MAX_VERSION, metaInfo);

        final List<String> extensions = getPlatformExtensions();
        for (final String extension : extensions) {
            assertIsSet(ExtensionInfoKey.EXTENSION_CLASS_PATTERN, extension, metaInfo);
            assertIsSet(ExtensionInfoKey.EXTENSION_NAME_PATTERN, extension, metaInfo);
        }
    }

    /**
     * Obtain an extension meta info value.
     * 
     * @param extension
     *            The extension.
     * @param metaInfoKeyPattern
     *            The meta info key pattern.
     * @return The meta info value.
     */
    private String getExtensionMetaInfo(final String extension,
            final String metaInfoKeyPattern) {
        return metaInfo.getProperty(MessageFormat.format(
                metaInfoKeyPattern, extension));
    }

    /**
     * Obtain a meta info value.
     * 
     * @param metaInfoKey
     *            A meta info key.
     * @return A meta info value.
     */
    private String getMetaInfo(final String metaInfoKey) {
        return metaInfo.getProperty(metaInfoKey);
    }

    /** The extension meta info property name patterns. */
    private static final class ExtensionInfoKey {
        private static final String EXTENSION_CLASS_PATTERN = "{0}-Class";
        private static final String EXTENSION_NAME_PATTERN = "{0}-Name";
    }

    /** The meta info property names. */
    private static final class InfoKey {
        private static final String PLATFORM_EXTENSIONS = "Platform-Extensions";
        private static final String PLATFORM_MAX_VERSION = "Platform-MaximumVersion";
        private static final String PLATFORM_MIN_VERSION = "Platform-MinimumVersion";
        private static final String PLUGIN_CLASS = "Plugin-Class";
        private static final String PLUGIN_ID = "Plugin-Id";
        private static final String PLUGIN_NAME = "Plugin-Name";
        private static final String PLUGIN_VERSION = "Plugin-Version";
    }
}

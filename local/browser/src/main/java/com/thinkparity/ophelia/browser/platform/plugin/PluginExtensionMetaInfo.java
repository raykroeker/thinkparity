/*
 * Created On: Sep 20, 2006 11:58:07 AM
 */
package com.thinkparity.ophelia.browser.platform.plugin;

import java.io.IOException;
import java.util.Properties;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final public class PluginExtensionMetaInfo {

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

    /** The plugin extension meta info <code>Properties</code>. */
    private final Properties metaInfo;

    /** The plugin extension's name. */
    private final String name;

    /**
     * Create PluginMetaInfo.
     * 
     */
    PluginExtensionMetaInfo(final String name, final Properties metaInfo) {
        super();
        this.metaInfo = metaInfo;
        this.name = name;
        parseMetaInfo();
    }

    /**
     * Obtain a list of all of the extension names.
     * 
     * @return A list of all of the extension names.
     */
    public String getExtensionClass() {
        return getMetaInfo(MetaInfoKey.EXTENSION_CLASS);
    }

    /**
     * Obtain a list of all of the extension names.
     * 
     * @return A list of all of the extension names.
     */
    public String getExtensionName() {
        return getMetaInfo(MetaInfoKey.EXTENSION_NAME);
    }

    /**
     * Obtain a meta info value.
     * 
     * @param metaInfoKey
     *            A meta info key.
     * @return A meta info value.
     */
    private String getMetaInfo(final String metaInfoKey) {
        return metaInfo.getProperty(
                new StringBuffer(name).append(metaInfoKey).toString());
    }

    /**
     * Read the plugin meta info from the class loader.
     * 
     * @throws IOException
     */
    private void parseMetaInfo() {
        assertIsSet(name + MetaInfoKey.EXTENSION_CLASS, metaInfo);
        assertIsSet(name + MetaInfoKey.EXTENSION_NAME, metaInfo);
    }

    /** The meta info keys used. */
    private static final class MetaInfoKey {
        private static final String EXTENSION_CLASS = "-Class";
        private static final String EXTENSION_NAME = "-Name";
    }
}

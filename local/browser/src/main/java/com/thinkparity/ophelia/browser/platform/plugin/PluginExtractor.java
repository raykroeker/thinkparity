/*
 * Created On: Sep 20, 2006 11:54:59 AM
 */
package com.thinkparity.ophelia.browser.platform.plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.StreamUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.browser.Constants.Directories;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class PluginExtractor {

    /** The plugin file. */
    private final File pluginFile;

    /** The plugin's extracted file system. */
    private final FileSystem pluginFileSystem;

    /** The plugin meta info. */
    private final PluginMetaInfo pluginMetaInfo;

    /**
     * Create PluginExtractor.
     * 
     * @param pluginFile
     *            The plugin file.
     */
    PluginExtractor(final File pluginFile)
            throws IOException {
        super();
        this.pluginFile = pluginFile;
        this.pluginMetaInfo = new PluginMetaInfo(pluginFile);

        final File pluginFileSystemRoot =
                new File(Directories.PARITY_PLUGIN_ROOT, createExtractedName());
        if (!pluginFileSystemRoot.exists()) {
            Assert.assertTrue(pluginFileSystemRoot.mkdir(),
                    "Could not create plugin extraction root {0}.",
                    pluginFileSystemRoot);
        }
        this.pluginFileSystem = new FileSystem(pluginFileSystemRoot);
    }

    /**
     * Extract the plugin file to the file system.
     * 
     * @throws IOException
     */
    void extract() throws IOException {
        Assert.assertTrue(pluginFileSystem.getRoot().mkdir(),
                "Could not create directory {0}.", pluginFileSystem);
        final JarFile jarFile = new JarFile(pluginFile);
        JarEntry entry;
        File entryFile;
        InputStream input;
        OutputStream output;
        final Enumeration entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            entry = (JarEntry) entries.nextElement();
            entryFile = new File(pluginFileSystem.getRoot(), entry.getName());
            if (!entry.isDirectory()) {
                Assert.assertTrue(entryFile.mkdirs(),
                        "Cannot create entry path {0}.", entryFile);
                input = jarFile.getInputStream(entry);
                output = new FileOutputStream(entryFile);
                try {
                    StreamUtil.copy(input, output);
                } finally {
                    input.close();
                    output.close();
                }
            }
        }
        jarFile.close();
    }

    /**
     * Obtain the file system for the extracted plugin.
     * 
     * @return A <code>FileSystem</code>.
     */
    FileSystem getFileSystem() {
        return pluginFileSystem;
    }

    /**
     * Determine if the plugin has been extracted.
     * 
     * @return A <code>List&lt;URL&gt;</code>.
     */
    Boolean isExtracted() {
        return pluginFileSystem.getRoot().exists();
    }

    /**
     * Create the name for the extracted plugin.
     * 
     * @return A name <code>String</code>.
     */
    private String createExtractedName() {
        return MessageFormat.format("{0} - {1}",
                pluginMetaInfo.getPluginName(),
                pluginMetaInfo.getPluginVersion());
    }
}

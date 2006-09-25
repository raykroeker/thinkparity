/*
 * Created On: Sep 23, 2006 11:31:27 AM
 */
package com.thinkparity.ophelia.browser.platform.plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.StreamUtil;
import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class PluginInstaller extends PluginUtility {

    /** The <code>FileSystem</code> to install to. */
    private final FileSystem installFileSystem;

    /** The plugin <code>File</code> to install. */
    private final JarFile pluginJarFile;

    /**
     * Create PluginInstaller.
     * 
     * @param installFileSystem
     *            The <code>FileSystem</code> to install to.
     * @param pluginFile
     *            The plugin <code>File</code> to install.
     */
    PluginInstaller(final FileSystem installFileSystem, final File pluginFile) {
        super();
        this.installFileSystem = installFileSystem;
        try {
            this.pluginJarFile = new JarFile(pluginFile);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Install the plugin.
     *
     */
    void install() {
        FileSystem pluginFileSystem = null;
        try {
            pluginFileSystem = getPluginFileSystem();
        } catch (final Throwable t) {
            throw translateError(t);
        }
        try {
            JarEntry pluginJarEntry;
            File entryFile;
            InputStream input;
            OutputStream output;
            final Enumeration pluginJarFileEntries = pluginJarFile.entries();
            File entryFileParent;
            while (pluginJarFileEntries.hasMoreElements()) {
                pluginJarEntry = (JarEntry) pluginJarFileEntries.nextElement();
                entryFile = new File(pluginFileSystem.getRoot(), pluginJarEntry.getName());
                if (!pluginJarEntry.isDirectory()) {
                    entryFileParent = entryFile.getParentFile();
                    if (!entryFileParent.exists()) {
                        Assert.assertTrue(entryFileParent.mkdirs(),
                                "Cannot create entry path {0}.", entryFileParent);
                    }
                    input = pluginJarFile.getInputStream(pluginJarEntry);
                    output = new FileOutputStream(entryFile);
                    try {
                        StreamUtil.copy(input, output);
                    } finally {
                        input.close();
                        output.close();
                    }
                }
            }
            pluginJarFile.close();
        } catch (final Throwable t) {
            FileUtil.deleteTree(pluginFileSystem.getRoot());
            throw translateError(t);
        }
    }

    /**
     * Determine if the plugin has been installed.
     * 
     * @return True if the plugin has already been installed.
     */
    Boolean isInstalled() {
        try {
            final String installMetaInfoPath = new StringBuffer(getInstallName())
                    .append("/META-INF/PLUGIN.MF").toString();
            final File installMetaInfoFile =
                    installFileSystem.findFile(installMetaInfoPath);
            return null != installMetaInfoFile && installMetaInfoFile.exists();
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Re-install the plugin.
     *
     */
    void reinstall() {
        FileSystem pluginFileSystem = null;
        try {
            pluginFileSystem = getPluginFileSystem();
        } catch (final Throwable t) {
            throw translateError(t);
        }
        FileUtil.deleteTree(pluginFileSystem.getRoot());
        install();
    }

    /**
     * Obtain the name of the install directory.
     * 
     * @return The install directory name.
     */
    private String getInstallName() throws IOException {
        final PluginMetaInfo metaInfo = readMetaInfo();
        return new StringBuffer(metaInfo.getPluginId()).toString();
    }

    /**
     * Obtain the plugin's installation file system.
     * 
     * @return A <code>FileSystem</code>.
     * @throws IOException
     */
    private FileSystem getPluginFileSystem() throws IOException {
        final String installPath = getInstallName();
        final File pluginFileSystemRoot = new File(installFileSystem.getRoot(), installPath);
        Assert.assertTrue(pluginFileSystemRoot.mkdir(),
                "Could not create install root {0}.", pluginFileSystemRoot);
        return installFileSystem.cloneChild(installPath);
    }

    /**
     * Open an input stream representing the manifest file within the plugin jar
     * file.
     * 
     * @return An input stream.
     */
    private InputStream openMetaInfoInputStream() throws IOException {
        final JarEntry manifestJarEntry = pluginJarFile.getJarEntry("META-INF/PLUGIN.MF");
        return pluginJarFile.getInputStream(manifestJarEntry);
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

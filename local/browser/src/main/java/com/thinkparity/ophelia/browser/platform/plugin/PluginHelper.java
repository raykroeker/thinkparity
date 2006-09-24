/*
 * Created On: Sep 20, 2006 10:55:45 AM
 */
package com.thinkparity.ophelia.browser.platform.plugin;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.log4j.Log4JHelper;

import com.thinkparity.ophelia.browser.Constants.Directories;
import com.thinkparity.ophelia.browser.Constants.FileExtensions;
import com.thinkparity.ophelia.browser.platform.Platform;

/**
 * <b>Title:</b>thinkParity Browser Platform Plugin Helper<br>
 * <b>Description:</b>Used by the platform to extract; initialize; start and
 * stop plugins.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PluginHelper {

    /** A file filter used to identify plugin files. */
    private static final FileFilter PLUGIN_FILE_FILTER;

    /** A file filter used to identify plugin directoris. */
    private static final FileFilter PLUGIN_DIRECTORY_FILTER;

    static {
        PLUGIN_FILE_FILTER = new FileFilter() {
            public boolean accept(final File pathname) {
                return pathname.getName().toLowerCase().endsWith(FileExtensions.PAR);
            }
        };
        PLUGIN_DIRECTORY_FILTER = new FileFilter() {
            public boolean accept(final File pathname) {
                if (pathname.isDirectory()) {
                    final File metaFile = new FileSystem(pathname).findFile("META-INF/PLUGIN.MF");
                    return null != metaFile && metaFile.exists();
                }
                return false;
            }
        };
    }

    /** An apache logger. */
    private final Logger logger;

    /** The thinkParity <code>Platform</code>. */
    private final Platform platform;

    /** The plugin registry. */
    private final PluginRegistry registry;

    /**
     * Create PluginManager.
     * 
     * @param platform
     *            The thinkParity <code>Platform</code>.
     */
    public PluginHelper(final Platform platform) {
        super();
        this.platform = platform;
        this.registry = new PluginRegistry();

        this.logger = this.platform.getLogger(getClass());
    }

    /**
     * End all plugins.
     *
     */
    public void end() {
        // grab a list of all registered plugins and kill -9 'em ;)
        for (PluginWrapper wrapper : registry.getWrappers()) {
            registry.remove(wrapper);
            try {
                wrapper.getPlugin().end();
            } catch (final Throwable t) {
                logError(t, "Could not end plugin:  {0}", wrapper);
            }
            wrapper = null;
        }
    }

    /**
     * Obtain the plugin registry.
     * 
     * @return A <code>PluginRegistry</code>.
     */
    public PluginRegistry getRegistry() {
        return registry;
    }

    /**
     * Start all plugins.
     *
     */
    public void start() {
        // install all plugin files
        if (Directories.PARITY_PLUGIN_ROOT.exists()) {
            final FileSystem pluginFileSystem = new FileSystem(Directories.PARITY_PLUGIN_ROOT);

            // install
            final File[] pluginFiles = pluginFileSystem.list("/", PLUGIN_FILE_FILTER);
            PluginInstaller installer;
            for (final File pluginFile : pluginFiles) {
                installer = new PluginInstaller(pluginFileSystem, pluginFile);
                if (!installer.isInstalled()) {
                    installer.install();
                }
            }

            // load
            final File[] pluginDirectories = pluginFileSystem.list("/", PLUGIN_DIRECTORY_FILTER);
            PluginLoader loader;
            for (final File pluginDirectory : pluginDirectories) {
                loader = new PluginLoader(new FileSystem(pluginDirectory));
                loader.load();
            }
        }

        // start
        final PluginServices services = new PluginServices(new PluginModelFactory(platform));
        final List<PluginWrapper> wrappers = registry.getWrappers();
        for (final PluginWrapper wrapper : wrappers) {
            try {
                wrapper.getPlugin().initialize(services);
                for (final PluginExtension extensions : wrapper.getExtensions()) {
                    extensions.initialize(services);
                }
            } catch (final Throwable t) {
                logError(t, "Could not initialize plugin {0}.", wrapper.getPlugin());
            }
            try {
                wrapper.getPlugin().start();
            } catch (final Throwable t) {
                logError(t, "Could not start plugin {0}.", wrapper.getPlugin());
            }
        }
    }

    /**
     * Log an error.
     * 
     * @param t
     *            The error.
     * @param errorPattern
     *            The error message pattern.
     * @param errorArguments
     *            The error message arguments.
     */
    private void logError(final Throwable t, final String errorPattern,
            final Object... errorArguments) {
        logger.error(Log4JHelper.renderAndFormat(logger, errorPattern,
                errorArguments), t);
    }
}

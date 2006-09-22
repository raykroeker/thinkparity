/*
 * Created On: Sep 20, 2006 10:55:45 AM
 */
package com.thinkparity.ophelia.browser.platform.plugin;

import java.io.File;
import java.io.FileFilter;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.log4j.Log4JHelper;

import com.thinkparity.ophelia.browser.Constants.Directories;
import com.thinkparity.ophelia.browser.Constants.FileExtensions;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.event.LifeCycleAdapter;
import com.thinkparity.ophelia.browser.platform.event.LifeCycleEvent;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class PluginHelper {

    /** A file filter used to identify plugin files. */
    private static final FileFilter PLUGIN_FILE_FILTER;

    static {
        PLUGIN_FILE_FILTER = new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.getName().toLowerCase().endsWith(FileExtensions.PAR);
            }
        };
    }

    /** An apache logger. */
    private final Logger logger;

    /** The thinkParity <code>Platform</code>. */
    private final Platform platform;

    /** The plugin file system. */
    private final FileSystem pluginFileSystem;

    /** The plugin registry. */
    private final PluginRegistry registry;

    /**
     * Create PluginManager.
     * 
     * @param platform
     *            The thinkParity <code>Platform</code>.
     * @param thinkParityPluginRoot
     *            The plugins root directory <code>File</code>.
     */
    public PluginHelper(final Platform platform) {
        super();
        this.platform = platform;
        this.pluginFileSystem = new FileSystem(Directories.PARITY_PLUGIN_ROOT);
        this.platform.addListener(new LifeCycleAdapter() {
            @Override
            public void ending(LifeCycleEvent e) {
                end();
            }
            @Override
            public void started(final LifeCycleEvent e) {
                start();
            }
        });
        this.registry = new PluginRegistry();

        this.logger = this.platform.getLogger(getClass());
    }

    /**
     * End all plugins.
     *
     */
    public void end() {
        // grab a list of all registered plugins and kill -9 'em ;)
        for (PluginWrapper plugin : registry.getPluginWrappers()) {
            registry.eradicate(plugin);
            try {
                plugin.end();
            } catch (final Throwable t) {
                logError(t, "Could not end plugin:  {0}", plugin);
            }
            plugin = null;
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
        // grab a list of all jar files in the root of the filesystem
        final File[] pluginFiles = pluginFileSystem.list("/", PLUGIN_FILE_FILTER);
        PluginWrapper plugin;
        PluginServices pluginServices;
        for (final File pluginFile : pluginFiles) {
            pluginServices = new PluginServices();
            pluginServices.setModelFactory(new PluginModelFactory(platform));
            plugin = new PluginWrapper(pluginFile, pluginServices);
            try {
                plugin.load();
                plugin.initialize();
                plugin.start();
                registry.register(plugin);
            } catch (final Throwable t) {
                logError(t, "Could not start plugin:  {0}", plugin);
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

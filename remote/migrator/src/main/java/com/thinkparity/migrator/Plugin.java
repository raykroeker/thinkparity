/*
 * May 11, 2006 4:47:07 PM
 * $Id$
 */
package com.thinkparity.migrator;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jivesoftware.messenger.IQRouter;
import org.jivesoftware.messenger.XMPPServer;
import org.jivesoftware.messenger.container.PluginManager;
import org.jivesoftware.messenger.handler.IQHandler;

import com.thinkparity.codebase.StringUtil.Separator;

/**
 * The migrator plugin.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public class Plugin implements org.jivesoftware.messenger.container.Plugin {

    /** An apache logger. */
    protected Logger logger;

    /** The migrator's controllers. */
    private final List<IQHandler> controllers;

    /** The jive query router. */
    private final IQRouter iqRouter;

    /** Create Plugin. */
    public Plugin() {
        super();
        this.controllers = new LinkedList<IQHandler>();
        this.iqRouter = XMPPServer.getInstance().getIQRouter();
    }

    /** @see org.jivesoftware.messenger.container.Plugin#destroyPlugin() */
    public void destroyPlugin() {
        destroyControllers();
        destroyPluginLogging();
    }

    /** @see org.jivesoftware.messenger.container.Plugin#initializePlugin(org.jivesoftware.messenger.container.PluginManager, java.io.File) */
    public void initializePlugin(final PluginManager manager,
            final File pluginDirectory) {
        initializePluginLogging(pluginDirectory);
        initializeControllers();

        final StringBuffer buffer = new StringBuffer()
            .append(Version.getBuildId()).append(Separator.FullColon)
            .append(Version.getMode()).append(Separator.FullColon)
            .append(Version.getName()).append(Separator.FullColon)
            .append(Version.getVersion()).append(Separator.FullColon);
        logger.info(buffer);
    }

    /** Destroy all of the migrator controllers. */
    private void destroyControllers() {
        synchronized(controllers) {
            for(IQHandler controller : controllers) {
                iqRouter.removeHandler(controller);
                controller = null;
            }
        }
    }

    /** Shutdown the logging framework. */
    private void destroyPluginLogging() { LogManager.shutdown(); }

    /**
     * Intialize the controller and add it to the route table.
     * 
     * @param controllerName
     *            The fully qualified controller name.
     */
    private void initializeController(final String controllerName) {
        try { controllers.add((IQHandler) Class.forName(controllerName).newInstance()); }
        catch(final ClassNotFoundException cnfx) {
            logger.fatal("[RMIGRATOR] [INIT] [INIT CONTROLLER]", cnfx);
        }
        catch(final IllegalAccessException iax) {
            logger.fatal("[RMIGRATOR] [INIT] [INIT CONTROLLER]", iax);
        }
        catch(final InstantiationException ix) {
            logger.fatal("[RMIGRATOR] [INIT] [INIT CONTROLLER]", ix);
        }
        iqRouter.addHandler(controllers.get(controllers.size() - 1));
    }

    /** Initialize all of the migrator controllers. */
    private void initializeControllers() {
        synchronized(controllers) {
            initializeController("com.thinkparity.migrator.controller.library.Create");
            initializeController("com.thinkparity.migrator.controller.library.CreateBytes");
            initializeController("com.thinkparity.migrator.controller.library.Read");
            initializeController("com.thinkparity.migrator.controller.library.ReadBytes");
            initializeController("com.thinkparity.migrator.controller.release.Create");
            initializeController("com.thinkparity.migrator.controller.release.Read");
        }
    }

    /**
     * Initialize the plugin logging framework.
     * 
     * @param pluginDirectory
     *            The plugin directory.
     */
    private void initializePluginLogging(final File pluginDirectory) {
        System.setProperty("rMigrator.pluginDirectory", pluginDirectory.getAbsolutePath());
        logger = LoggerFactory.getLogger(getClass());
    }
}

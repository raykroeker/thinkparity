/*
 * May 11, 2006 4:47:07 PM
 * $Id$
 */
package com.thinkparity.migrator;

import java.io.File;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.jivesoftware.messenger.IQRouter;
import org.jivesoftware.messenger.XMPPServer;
import org.jivesoftware.messenger.container.PluginManager;
import org.jivesoftware.messenger.handler.IQHandler;
import org.jivesoftware.util.JiveGlobals;

import com.thinkparity.migrator.Constants.CalpurniaPropertyNames;
import com.thinkparity.migrator.io.hsqldb.HypersonicSession;
import com.thinkparity.migrator.io.hsqldb.HypersonicSessionManager;

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
        destroyDatabase();
        destroyPluginLogging();
    }

    /** @see org.jivesoftware.messenger.container.Plugin#initializePlugin(org.jivesoftware.messenger.container.PluginManager, java.io.File) */
    public void initializePlugin(final PluginManager manager,
            final File pluginDirectory) {
        initializePluginLogging(pluginDirectory);
        initializePluginDatabase(pluginDirectory);
        initializeControllers();

        logger.info(Version.toInfo());
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

    /** Destroy the database. */
    private void destroyDatabase() { HypersonicSessionManager.shutdown(); }

    /** Shutdown the logging framework. */
    private void destroyPluginLogging() { LogManager.shutdown(); }

    /**
     * Import a jive property.
     * 
     * @param propertyName
     *            The local property name.
     * @return The previous local property value.
     */
    private String importProperty(final String propertyName) {
        final String jivePropertyValue = JiveGlobals.getProperty(propertyName);
        final String calpurniaPropertyValue = System.getProperty(propertyName);
        System.setProperty(propertyName, jivePropertyValue);
        return calpurniaPropertyValue;
    }

    /**
     * Intialize the controller and add it to the route table.
     * 
     * @param controllerName
     *            The fully qualified controller name.
     */
    private void initializeController(final String controllerName) {
        try { controllers.add((IQHandler) Class.forName(controllerName).newInstance()); }
        catch(final ClassNotFoundException cnfx) {
            logger.fatal("[RMIGRATOR] [PLUGIN] [INIT CONTROLLER]", cnfx);
        }
        catch(final IllegalAccessException iax) {
            logger.fatal("[RMIGRATOR] [PLUGIN] [INIT CONTROLLER]", iax);
        }
        catch(final InstantiationException ix) {
            logger.fatal("[RMIGRATOR] [PLUGIN] [INIT CONTROLLER]", ix);
        }
        final IQHandler controller = controllers.get(controllers.size() - 1);
        iqRouter.addHandler(controller);
        final String info = MessageFormat.format(
                "[RMIGRATOR] [PLUGIN] [INIT CONTROLLER] [{0} REGISTERED]",
                new Object[] {controller.getInfo().getNamespace()});
        logger.info(info);
    }

    /** Initialize all of the migrator controllers. */
    private void initializeControllers() {
        synchronized(controllers) {
            initializeController("com.thinkparity.migrator.controller.library.Create");
            initializeController("com.thinkparity.migrator.controller.library.CreateBytes");
            initializeController("com.thinkparity.migrator.controller.library.Delete");
            initializeController("com.thinkparity.migrator.controller.library.Read");
            initializeController("com.thinkparity.migrator.controller.library.ReadBytes");
            initializeController("com.thinkparity.migrator.controller.release.Create");
            initializeController("com.thinkparity.migrator.controller.release.Delete");
            initializeController("com.thinkparity.migrator.controller.release.Read");
            initializeController("com.thinkparity.migrator.controller.release.ReadAll");
            initializeController("com.thinkparity.migrator.controller.release.ReadLibraries");
            initializeController("com.thinkparity.migrator.controller.release.ReadLatest");
        }
    }

    private void initializePluginDatabase(final File pluginDirectory) {
        importProperty(CalpurniaPropertyNames.DB_DRIVER);
        importProperty(CalpurniaPropertyNames.DB_PASSWORD);
        importProperty(CalpurniaPropertyNames.DB_URL);
        importProperty(CalpurniaPropertyNames.DB_USERNAME);
        // opening and closing a session will cause the db layer to initialize
        final HypersonicSession session =
            HypersonicSessionManager.openSession();
        session.close();
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

/*
 * Created On: Nov 25, 2005
 * $Id$
 */
package com.thinkparity.server;

import java.io.File;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.jivesoftware.messenger.IQRouter;
import org.jivesoftware.messenger.XMPPServer;
import org.jivesoftware.messenger.container.Plugin;
import org.jivesoftware.messenger.container.PluginManager;
import org.jivesoftware.messenger.handler.IQHandler;

/**
 * thinkParity Remote Model Plugin
 * 
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class ParityServer implements Plugin {

	/** An apache logger. */
    protected Logger logger;

	/** The model's controllers. */
    private final List<IQHandler> controllers;

    /** The jive query router. */
	private final IQRouter iqRouter;

	/** Create ParityServer. */
	public ParityServer() {
		super();
        this.controllers = new LinkedList<IQHandler>();
		this.iqRouter = XMPPServer.getInstance().getIQRouter();
	}

	/**
	 * @see org.jivesoftware.messenger.container.Plugin#destroyPlugin()
	 */
	public void destroyPlugin() {
	    destroyControllers();
		destroyPluginLogging();
	}

	/**
	 * @see org.jivesoftware.messenger.container.Plugin#initializePlugin(org.jivesoftware.messenger.container.PluginManager, java.io.File)
	 */
	public void initializePlugin(PluginManager manager, File pluginDirectory) {
		initializePluginLogging(pluginDirectory);
		initializeControllers();

		logger.info(Version.toInfo());
	}

	/**
	 * Destroy the iq dispatcher.
	 * 
	 */
	private void destroyControllers() {
	    synchronized(controllers) {
            for(IQHandler controller : controllers) { 
                iqRouter.removeHandler(controller);
                controller = null;
            }
        }
	}

	/**
	 * Destroy the logging framework.
	 *
	 */
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
            logger.fatal("[RMODEL] [PLUGIN] [INIT CONTROLLER]", cnfx);
        }
        catch(final IllegalAccessException iax) {
            logger.fatal("[RMODEL] [PLUGIN] [INIT CONTROLLER]", iax);
        }
        catch(final InstantiationException ix) {
            logger.fatal("[RMODEL] [PLUGIN] [INIT CONTROLLER]", ix);
        }
        catch(final Throwable t) {
            logger.fatal("[RMODEL] [PLUGIN] [INIT CONTROLLER]", t);
        }
        final IQHandler controller = controllers.get(controllers.size() - 1);
        iqRouter.addHandler(controller);
        final String info = MessageFormat.format(
                "[RMODEL] [PLUGIN] [INIT CONTROLLER] [{0} REGISTERED]",
                new Object[] {controller.getInfo().getNamespace()});
        logger.info(info);
    }

    /**
	 * Initialize the iq dispatcher.
	 * 
	 */
	private void initializeControllers() {
        synchronized(controllers) {
            initializeController("com.thinkparity.server.handler.artifact.AcceptKeyRequest");
            initializeController("com.thinkparity.server.handler.artifact.CloseArtifact");
            initializeController("com.thinkparity.server.handler.artifact.ConfirmReceipt");
            initializeController("com.thinkparity.server.handler.artifact.CreateArtifact");
            initializeController("com.thinkparity.server.handler.artifact.DeleteArtifact");
            initializeController("com.thinkparity.server.handler.artifact.DenyKeyRequest");
            initializeController("com.thinkparity.server.handler.artifact.FlagArtifact");
            initializeController("com.thinkparity.server.handler.artifact.GetArtifactKeys");
            initializeController("com.thinkparity.server.handler.artifact.GetKeyHolder");
            initializeController("com.thinkparity.server.handler.artifact.GetSubscription");
            initializeController("com.thinkparity.server.handler.artifact.ReadContacts");
            initializeController("com.thinkparity.server.handler.artifact.RequestArtifactKey");
            initializeController("com.thinkparity.server.handler.contact.AcceptInvitation");
            initializeController("com.thinkparity.server.handler.contact.DeclineInvitation");
            initializeController("com.thinkparity.server.handler.contact.InviteContact");
            initializeController("com.thinkparity.server.handler.contact.ReadContacts");
            initializeController("com.thinkparity.server.handler.document.Reactivate");
            initializeController("com.thinkparity.server.handler.document.SendDocument");
            initializeController("com.thinkparity.server.handler.queue.ProcessOfflineQueue");
            initializeController("com.thinkparity.server.handler.user.ReadUsers");
            initializeController("com.thinkparity.server.handler.user.SubscribeUser");
            initializeController("com.thinkparity.server.handler.user.UnsubscribeUser");
        }
	}

    /**
	 * Initialize log4j for the parity server.
	 * 
	 * @param pluginDirectory
	 *            The plugin directory.
	 */
	private void initializePluginLogging(final File pluginDirectory) {
        System.setProperty("rModel.pluginDirectory", pluginDirectory.getAbsolutePath());
        logger = LoggerFactory.getLogger(getClass());
	}
}

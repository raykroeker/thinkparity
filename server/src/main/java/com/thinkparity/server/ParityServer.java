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
	public void initializePlugin(final PluginManager manager,
            final File pluginDirectory) {
		initLoggging(pluginDirectory);
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
            logger.fatal(cnfx);
        }
        catch(final IllegalAccessException iax) {
            logger.fatal(iax);
        }
        catch(final InstantiationException ix) {
            logger.fatal(ix);
        }
        catch(final Throwable t) {
            logger.fatal(t);
        }
        final IQHandler controller = controllers.get(controllers.size() - 1);
        iqRouter.addHandler(controller);
        logger.info(MessageFormat.format("[{0}]",
                controller.getInfo().getNamespace()));
    }

    /**
	 * Initialize the iq dispatcher.
	 * 
	 */
	private void initializeControllers() {
        synchronized(controllers) {
            initializeController("com.thinkparity.server.handler.artifact.AddTeamMember");
            initializeController("com.thinkparity.server.handler.artifact.ConfirmReceipt");
            initializeController("com.thinkparity.server.handler.artifact.CreateArtifact");
            initializeController("com.thinkparity.server.handler.artifact.CreateDraft");
            initializeController("com.thinkparity.server.handler.artifact.DeleteDraft");
            initializeController("com.thinkparity.server.handler.artifact.FlagArtifact");
            initializeController("com.thinkparity.server.handler.artifact.GetSubscription");
            initializeController("com.thinkparity.server.handler.artifact.ReadContacts");
            initializeController("com.thinkparity.server.handler.artifact.ReadKeyHolder");
            initializeController("com.thinkparity.server.handler.artifact.RemoveTeamMember");
            initializeController("com.thinkparity.server.handler.contact.AcceptInvitation");
            initializeController("com.thinkparity.server.handler.contact.DeclineInvitation");
            initializeController("com.thinkparity.server.handler.contact.Delete");
            initializeController("com.thinkparity.server.handler.contact.DeleteInvitation");
            initializeController("com.thinkparity.server.handler.contact.ExtendInvitation");
            initializeController("com.thinkparity.server.handler.contact.Read");
            initializeController("com.thinkparity.server.handler.contact.ReadIds");
            initializeController("com.thinkparity.server.handler.container.Publish");
            initializeController("com.thinkparity.server.handler.container.PublishArtifact");
            initializeController("com.thinkparity.server.handler.container.Send");
            initializeController("com.thinkparity.server.handler.container.SendArtifact");
            initializeController("com.thinkparity.server.handler.document.SendDocument");
            initializeController("com.thinkparity.server.handler.profile.AddEmail");
            initializeController("com.thinkparity.server.handler.profile.Read");
            initializeController("com.thinkparity.server.handler.profile.ReadEmails");
            initializeController("com.thinkparity.server.handler.profile.ReadSecurityQuestion");
            initializeController("com.thinkparity.server.handler.profile.RemoveEmail");
            initializeController("com.thinkparity.server.handler.profile.ResetPassword");
            initializeController("com.thinkparity.server.handler.profile.Update");
            initializeController("com.thinkparity.server.handler.profile.VerifyEmail");
            initializeController("com.thinkparity.server.handler.queue.ProcessOfflineQueue");
            initializeController("com.thinkparity.server.handler.user.ReadUsers");
        }
	}

    /**
	 * Initialize logging.
	 * 
	 * @param pluginDirectory
	 *            The plugin directory.
	 */
	private void initLoggging(final File pluginDirectory) {
        System.setProperty("thinkparity.log4j.file",
                new File(pluginDirectory, "desdemona.log4j").getAbsolutePath());
        logger = Logger.getLogger(getClass());
	}
}

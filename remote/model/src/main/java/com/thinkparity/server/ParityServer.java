/*
 * Nov 25, 2005
 */
package com.thinkparity.server;

import java.io.File;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jivesoftware.messenger.IQRouter;
import org.jivesoftware.messenger.XMPPServer;
import org.jivesoftware.messenger.container.Plugin;
import org.jivesoftware.messenger.container.PluginManager;
import org.jivesoftware.messenger.handler.IQHandler;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.server.handler.IQParityFlagHandler;
import com.thinkparity.server.handler.IQParitySubscriptionHandler;
import com.thinkparity.server.log4j.ServerLog4jConfigurator;
import com.thinkparity.server.log4j.ServerLoggerFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ParityServer implements Plugin {

	/**
	 * List of all of the iq handlers for the parity plugin.
	 * 
	 * @see ParityServer#initializeIQHandlers()
	 * @see ParityServer#destroyIQHandlers()
	 */
	private static final Collection<IQHandler> parityHandlers;

	static { parityHandlers = new Vector<IQHandler>(2); }

	/**
	 * Handle to the xmpp server's iq router.
	 */
	private final IQRouter iqRouter;

	/**
	 * Handle to an apache logger.
	 * 
	 * @see ParityServer#initializePluginLogging(File)
	 */
	private Logger logger;

	/**
	 * Create a ParityServer.
	 */
	public ParityServer() {
		super();
		this.iqRouter = XMPPServer.getInstance().getIQRouter();
	}

	/**
	 * @see org.jivesoftware.messenger.container.Plugin#destroyPlugin()
	 */
	public void destroyPlugin() { destroyIQHandlers(); }

	/**
	 * @see org.jivesoftware.messenger.container.Plugin#initializePlugin(org.jivesoftware.messenger.container.PluginManager, java.io.File)
	 */
	public void initializePlugin(PluginManager manager, File pluginDirectory) {
		initializePluginLogging(pluginDirectory);
		initializeIQHandlers();
	}

	/**
	 * Remove all parity handlers from the router's list.
	 * 
	 */
	private void destroyIQHandlers() {
		for(IQHandler parityHandler : parityHandlers) {
			logger.info(new StringBuffer("Removing:")
					.append(parityHandler.getClass().getName())
					.append("..."));
			iqRouter.removeHandler(parityHandler);
			parityHandler.destroy();
			logger.info(new StringBuffer(parityHandler.getClass().getName())
					.append(" removed."));
		}
		parityHandlers.clear();
	}

	/**
	 * Add all parity handlers to the router's list.
	 * 
	 */
	private void initializeIQHandlers() {
		parityHandlers.add(new IQParityFlagHandler());
		parityHandlers.add(new IQParitySubscriptionHandler());
		for(IQHandler parityHandler : parityHandlers) {
			logger.info(new StringBuffer("Adding:")
					.append(parityHandler.getClass().getName())
					.append("..."));
			iqRouter.addHandler(parityHandler);
			logger.info(new StringBuffer(parityHandler.getClass().getName())
					.append(" added."));
		}
	}

	/**
	 * Initialize log4j for the parity server.
	 * 
	 * @param pluginDirectory
	 *            The plugin directory.
	 */
	private void initializePluginLogging(final File pluginDirectory) {
		final File log4jDirectory = new File(
				pluginDirectory, ParityServerConstants.LOG4J_DIRECTORY_NAME);
		if(!log4jDirectory.exists()) {
			Assert.assertTrue(
					"initializePluginLogging(File)", log4jDirectory.mkdir());
		}
		ServerLog4jConfigurator.configure(log4jDirectory);
		logger = ServerLoggerFactory.getLogger(getClass());
	}
}

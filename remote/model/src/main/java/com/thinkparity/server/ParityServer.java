/*
 * Nov 25, 2005
 */
package com.thinkparity.server;

import java.io.File;

import org.apache.log4j.LogManager;
import org.jivesoftware.messenger.IQRouter;
import org.jivesoftware.messenger.Session;
import org.jivesoftware.messenger.XMPPServer;
import org.jivesoftware.messenger.container.Plugin;
import org.jivesoftware.messenger.container.PluginManager;
import org.jivesoftware.messenger.event.SessionEventDispatcher;
import org.jivesoftware.messenger.event.SessionEventListener;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.server.handler.IQDispatcher;
import com.thinkparity.server.log4j.ServerLog4jConfigurator;
import com.thinkparity.server.log4j.ServerLoggerFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ParityServer implements Plugin {

	/**
	 * IQ dispatcher for the parity server.
	 */
	private IQDispatcher iqDispatcher;

	/**
	 * Handle to the xmpp server's iq router.
	 */
	private final IQRouter iqRouter;

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
	public void destroyPlugin() {
		destroyPluginLogging();
		destroyIQDispatcher();
		destroyEventHandlers();
	}

	/**
	 * @see org.jivesoftware.messenger.container.Plugin#initializePlugin(org.jivesoftware.messenger.container.PluginManager, java.io.File)
	 */
	public void initializePlugin(PluginManager manager, File pluginDirectory) {
		initializePluginLogging(pluginDirectory);
		initializeIQDispatcher();
		initializeEventHandlers();
		ServerLoggerFactory.getLogger(getClass()).info(ParityServerConstants.SERVER_NAME + " initialized.");
	}

	/**
	 * Destroy the iq dispatcher.s
	 */
	private void destroyIQDispatcher() {
		// destroy the dispatcher
		iqRouter.removeHandler(iqDispatcher);
		iqDispatcher.destroy();
		iqDispatcher = null;
	}

	/**
	 * Destroy the logging framework.
	 *
	 */
	private void destroyPluginLogging() { LogManager.shutdown(); }

	/**
	 * Initialize the iq dispatcher.
	 */
	private void initializeIQDispatcher() {
		// create the dispatcher
		iqDispatcher = new IQDispatcher();
		iqRouter.addHandler(iqDispatcher);
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
	}

	private void initializeEventHandlers() {
		SessionEventDispatcher.addListener(new SessionEventListener() {
			public void anonymousSessionCreated(Session session) {}
			public void anonymousSessionDestroyed(Session session) {}
			public void sessionCreated(Session session) {
				
			}
			public void sessionDestroyed(Session session) {}
		});
	}

	private void destroyEventHandlers() {}
}

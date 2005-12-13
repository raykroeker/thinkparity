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

import com.thinkparity.server.handler.artifact.AcceptKeyRequest;
import com.thinkparity.server.handler.artifact.CreateArtifact;
import com.thinkparity.server.handler.artifact.DenyKeyRequest;
import com.thinkparity.server.handler.artifact.FlagArtifact;
import com.thinkparity.server.handler.artifact.RequestArtifactKey;
import com.thinkparity.server.handler.user.SubscribeUser;
import com.thinkparity.server.handler.user.UnsubscribeUser;
import com.thinkparity.server.org.apache.log4j.ServerLog4jConfigurator;
import com.thinkparity.server.org.apache.log4j.ServerLoggerFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ParityServer implements Plugin {

	private AcceptKeyRequest acceptKeyRequest;
	private CreateArtifact createArtifact;

	private DenyKeyRequest denyKeyRequest;

	private FlagArtifact flagArtifact;
	/**
	 * Handle to the xmpp server's iq router.
	 */
	private final IQRouter iqRouter;
	private RequestArtifactKey requestArtifactKey;
	private SubscribeUser subscribeUser;
	private UnsubscribeUser unsubscribeUser;

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
		destroyIQHandlers();
		destroyEventHandlers();
	}

	/**
	 * @see org.jivesoftware.messenger.container.Plugin#initializePlugin(org.jivesoftware.messenger.container.PluginManager, java.io.File)
	 */
	public void initializePlugin(PluginManager manager, File pluginDirectory) {
		initializePluginLogging(pluginDirectory);
		initializeIQHandlers();
		initializeEventHandlers();
		ServerLoggerFactory.getLogger(getClass()).info(ParityServerConstants.SERVER_NAME + " initialized.");
	}

	private void destroyEventHandlers() {}

	/**
	 * Destroy the iq dispatcher.s
	 */
	private void destroyIQHandlers() {
		iqRouter.removeHandler(createArtifact);
		createArtifact.destroy();
		createArtifact = null;

		iqRouter.removeHandler(unsubscribeUser);
		unsubscribeUser.destroy();
		unsubscribeUser = null;

		iqRouter.removeHandler(flagArtifact);
		flagArtifact.destroy();
		flagArtifact = null;

		iqRouter.removeHandler(acceptKeyRequest);
		acceptKeyRequest.destroy();
		acceptKeyRequest = null;

		iqRouter.removeHandler(requestArtifactKey);
		requestArtifactKey.destroy();
		requestArtifactKey = null;

		iqRouter.removeHandler(denyKeyRequest);
		denyKeyRequest.destroy();
		denyKeyRequest = null;

		iqRouter.removeHandler(subscribeUser);
		subscribeUser.destroy();
		subscribeUser = null;
	}

	/**
	 * Destroy the logging framework.
	 *
	 */
	private void destroyPluginLogging() { LogManager.shutdown(); }

	private void initializeEventHandlers() {
		SessionEventDispatcher.addListener(new SessionEventListener() {
			public void anonymousSessionCreated(Session session) {}
			public void anonymousSessionDestroyed(Session session) {}
			public void sessionCreated(Session session) {}
			public void sessionDestroyed(Session session) {}
		});
	}

	/**
	 * Initialize the iq dispatcher.
	 */
	private void initializeIQHandlers() {
		createArtifact = new CreateArtifact();
		iqRouter.addHandler(createArtifact);

		unsubscribeUser = new UnsubscribeUser();
		iqRouter.addHandler(unsubscribeUser);

		flagArtifact = new FlagArtifact();
		iqRouter.addHandler(flagArtifact);

		acceptKeyRequest = new AcceptKeyRequest();
		iqRouter.addHandler(acceptKeyRequest);

		requestArtifactKey = new RequestArtifactKey();
		iqRouter.addHandler(requestArtifactKey);
		
		denyKeyRequest = new DenyKeyRequest();
		iqRouter.addHandler(denyKeyRequest);

		subscribeUser = new SubscribeUser();
		iqRouter.addHandler(subscribeUser);
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
}

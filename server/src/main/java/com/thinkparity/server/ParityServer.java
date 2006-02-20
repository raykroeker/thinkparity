/*
 * Nov 25, 2005
 */
package com.thinkparity.server;

import java.io.File;

import org.apache.log4j.LogManager;
import org.jivesoftware.messenger.IQRouter;
import org.jivesoftware.messenger.XMPPServer;
import org.jivesoftware.messenger.container.Plugin;
import org.jivesoftware.messenger.container.PluginManager;

import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.server.handler.IQHandler;
import com.thinkparity.server.handler.artifact.*;
import com.thinkparity.server.handler.queue.ProcessOfflineQueue;
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
	private IQHandler closeArtifact;
	private CreateArtifact createArtifact;
	private IQHandler deleteArtifact;
	private DenyKeyRequest denyKeyRequest;
	private IQHandler flagArtifact;
	private IQHandler getArtifactKeys;
	private IQHandler getKeyHolder;
	private IQHandler getSubscription;
	private final IQRouter iqRouter;
	private IQHandler processOfflineQueue;

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
	}

	/**
	 * @see org.jivesoftware.messenger.container.Plugin#initializePlugin(org.jivesoftware.messenger.container.PluginManager, java.io.File)
	 */
	public void initializePlugin(PluginManager manager, File pluginDirectory) {
		initializePluginLogging(pluginDirectory);
		initializeIQHandlers();
		final StringBuffer infoBuffer = new StringBuffer()
			.append(Version.getName()).append(Separator.FullColon)
			.append(Version.getVersion()).append(Separator.FullColon)
			.append(Version.getBuildId());
		ServerLoggerFactory.getLogger(getClass()).info(infoBuffer);
	}

	/**
	 * Destroy the iq dispatcher.
	 * 
	 */
	private void destroyIQHandlers() {
		iqRouter.removeHandler(acceptKeyRequest);
		acceptKeyRequest.destroy();
		acceptKeyRequest = null;
		
		iqRouter.removeHandler(closeArtifact);
		closeArtifact.destroy();
		closeArtifact = null;

		iqRouter.removeHandler(createArtifact);
		createArtifact.destroy();
		createArtifact = null;

		iqRouter.removeHandler(deleteArtifact);
		deleteArtifact.destroy();
		deleteArtifact = null;

		iqRouter.removeHandler(denyKeyRequest);
		denyKeyRequest.destroy();
		denyKeyRequest = null;
		
		iqRouter.removeHandler(flagArtifact);
		flagArtifact.destroy();
		flagArtifact = null;
		
		iqRouter.removeHandler(getKeyHolder);
		getKeyHolder.destroy();
		getKeyHolder = null;

		iqRouter.removeHandler(getArtifactKeys);
		getArtifactKeys.destroy();
		getArtifactKeys = null;

		iqRouter.removeHandler(getSubscription);
		getSubscription.destroy();
		getSubscription = null;

		iqRouter.removeHandler(processOfflineQueue);
		processOfflineQueue.destroy();
		processOfflineQueue = null;

		iqRouter.removeHandler(requestArtifactKey);
		requestArtifactKey.destroy();
		requestArtifactKey = null;
		
		iqRouter.removeHandler(subscribeUser);
		subscribeUser.destroy();
		subscribeUser = null;
		
		iqRouter.removeHandler(unsubscribeUser);
		unsubscribeUser.destroy();
		unsubscribeUser = null;
	}

	/**
	 * Destroy the logging framework.
	 *
	 */
	private void destroyPluginLogging() { LogManager.shutdown(); }

	/**
	 * Initialize the iq dispatcher.
	 * 
	 */
	private void initializeIQHandlers() {
		acceptKeyRequest = new AcceptKeyRequest();
		iqRouter.addHandler(acceptKeyRequest);
		
		closeArtifact = new CloseArtifact();
		iqRouter.addHandler(closeArtifact);

		createArtifact = new CreateArtifact();
		iqRouter.addHandler(createArtifact);

		deleteArtifact = new DeleteArtifact();
		iqRouter.addHandler(deleteArtifact);

		denyKeyRequest = new DenyKeyRequest();
		iqRouter.addHandler(denyKeyRequest);
		
		flagArtifact = new FlagArtifact();
		iqRouter.addHandler(flagArtifact);

		getKeyHolder = new GetKeyHolder();
		iqRouter.addHandler(getKeyHolder);

		getArtifactKeys = new GetArtifactKeys();
		iqRouter.addHandler(getArtifactKeys);

		getSubscription = new GetSubscription();
		iqRouter.addHandler(getSubscription);

		processOfflineQueue = new ProcessOfflineQueue();
		iqRouter.addHandler(processOfflineQueue);

		requestArtifactKey = new RequestArtifactKey();
		iqRouter.addHandler(requestArtifactKey);

		subscribeUser = new SubscribeUser();
		iqRouter.addHandler(subscribeUser);

		unsubscribeUser = new UnsubscribeUser();
		iqRouter.addHandler(unsubscribeUser);
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

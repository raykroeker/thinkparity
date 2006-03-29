/*
 * Jan 16, 2006
 */
package com.thinkparity.browser.application.browser.display.provider;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.browser.display.provider.contact.ManageContactsProvider;
import com.thinkparity.browser.application.browser.display.provider.document.HistoryProvider;
import com.thinkparity.browser.application.browser.display.provider.main.InfoProvider;
import com.thinkparity.browser.application.browser.display.provider.main.MainProvider;
import com.thinkparity.browser.application.browser.display.provider.session.SendArtifactProvider;
import com.thinkparity.browser.application.browser.display.provider.session.SendVersionProvider;
import com.thinkparity.browser.model.ModelFactory;

import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.message.system.SystemMessageModel;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.JabberIdBuilder;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ProviderFactory {

	/**
	 * The parity preferences.
	 * 
	 */
	private static final Preferences preferences;

	/**
	 * Singleton instance.
	 * 
	 */
	private static final ProviderFactory singleton;

	static {
		final Workspace workspace = WorkspaceModel.getModel().getWorkspace();
		preferences = workspace.getPreferences();

		singleton = new ProviderFactory();
	}

	/**
	 * Obtain the history content provider.
	 * 
	 * @return The history content provider.
	 */
	public static ContentProvider getHistoryProvider() {
		return singleton.doGetHistoryProvider();
	}

	/**
	 * Obtain the info provider.
	 * 
	 * @return The info provider.
	 */
	public static ContentProvider getInfoProvider() {
		return singleton.doGetInfoProvider();
	}

	public static ContentProvider getMainProvider() {
		return singleton.doGetMainProvider();
	}

	/**
	 * Obtain the manage contacts provider.
	 * 
	 * @return The manage contacts provider.
	 */
	public static ContentProvider getManageContactsProvider() {
		return singleton.doGetManageContactsProvider();
	}

	public static ContentProvider getSendArtifactProvider() {
		return singleton.doGetSendArtifactProvider();
	}

	public static ContentProvider getSendVersionProvider() {
		return singleton.doGetSendVersionProvider();
	}

	/**
	 * The parity artifact interface.
	 * 
	 */
	protected final ArtifactModel artifactModel;

	/**
	 * Document model api.
	 * 
	 */
	protected final DocumentModel dModel;

	/**
	 * An apache logger.
	 * 
	 */
	protected final Logger logger;

	/**
	 * Session model api.
	 * 
	 */
	protected final SessionModel sessionModel;

	/**
	 * System message interface.
	 * 
	 */
	protected final SystemMessageModel systemMessageModel;

	/**
	 * The document history provider.
	 * 
	 */
	private final ContentProvider historyProvider;

	/**
	 * The info pane provider.
	 * 
	 */
	private final ContentProvider infoProvider;

	/**
	 * The user in the parity prefferences.
	 * 
	 */
	private final JabberId loggedInUser;

	/**
	 * The main provider. Is a composite provider consisting of a document
	 * list provider as well as a message list provider.
	 * 
	 */
	private final ContentProvider mainProvider;

	/**
	 * Manage contacts provider.
	 * 
	 */
	private final ContentProvider manageContactsProvider;

	/**
	 * Send artifact provider.
	 * 
	 */
	private final ContentProvider sendArtifactProvider;

	/**
	 * Send artifact version provider.
	 * 
	 */
	private final ContentProvider sendVersionProvider;

	/**
	 * Create a ProviderFactory.
	 * 
	 */
	private ProviderFactory() {
		super();
		final ModelFactory modelFactory = ModelFactory.getInstance();
		this.artifactModel = modelFactory.getArtifactModel(getClass());
		this.dModel = modelFactory.getDocumentModel(getClass());
		this.logger = ModelLoggerFactory.getLogger(getClass());
		this.loggedInUser =
			JabberIdBuilder.parseUsername(preferences.getUsername());
		this.sessionModel = modelFactory.getSessionModel(getClass());
		this.systemMessageModel = modelFactory.getSystemMessageModel(getClass());

		this.historyProvider = new HistoryProvider(dModel);
		this.infoProvider = new InfoProvider(dModel);
		this.mainProvider = new MainProvider(artifactModel, dModel, systemMessageModel);
		this.manageContactsProvider = new ManageContactsProvider(sessionModel);
		this.sendArtifactProvider = new SendArtifactProvider(dModel, sessionModel, loggedInUser);
		this.sendVersionProvider = new SendVersionProvider(dModel, sessionModel, loggedInUser);
	}

	/**
	 * Obtain the history content provider.
	 * 
	 * @return The history content provider.
	 */
	private ContentProvider doGetHistoryProvider() { return historyProvider; }

	/**
	 * Obtain the info content provider.
	 * 
	 * @return The info content provider.
	 */
	private ContentProvider doGetInfoProvider() { return infoProvider; }

	/**
	 * Obtain the main provider.
	 * 
	 * @return The main provider.
	 */
	private ContentProvider doGetMainProvider() { return mainProvider; }

	/**
	 * Obtain the manage contacts provider.
	 * 
	 * @return The manage contacts provider.
	 */
	private ContentProvider doGetManageContactsProvider() {
		return manageContactsProvider;
	}

	/**
	 * Obtain the user provider.
	 * 
	 * @return The user provider.
	 */
	private ContentProvider doGetSendArtifactProvider() {
		return sendArtifactProvider;
	}

	private ContentProvider doGetSendVersionProvider() {
		return sendVersionProvider;
	}
}

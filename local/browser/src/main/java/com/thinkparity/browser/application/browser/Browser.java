/*
 * Jan 18, 2006
 */
package com.thinkparity.browser.application.browser;

import java.awt.Point;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.AbstractApplication;
import com.thinkparity.browser.application.browser.display.DisplayId;
import com.thinkparity.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.browser.application.browser.display.avatar.AvatarRegistry;
import com.thinkparity.browser.application.browser.display.avatar.BrowserInfoAvatar;
import com.thinkparity.browser.application.browser.display.avatar.BrowserMainAvatar;
import com.thinkparity.browser.application.browser.display.avatar.session.SessionSendVersion;
import com.thinkparity.browser.application.browser.window.History2Window;
import com.thinkparity.browser.application.browser.window.WindowFactory;
import com.thinkparity.browser.application.browser.window.WindowId;
import com.thinkparity.browser.platform.Platform;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionFactory;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;
import com.thinkparity.browser.platform.action.artifact.*;
import com.thinkparity.browser.platform.action.document.Close;
import com.thinkparity.browser.platform.action.document.Create;
import com.thinkparity.browser.platform.action.document.Delete;
import com.thinkparity.browser.platform.action.document.Open;
import com.thinkparity.browser.platform.action.document.OpenVersion;
import com.thinkparity.browser.platform.action.session.AcceptInvitation;
import com.thinkparity.browser.platform.action.session.DeclineInvitation;
import com.thinkparity.browser.platform.action.system.message.DeleteSystemMessage;
import com.thinkparity.browser.platform.application.ApplicationId;
import com.thinkparity.browser.platform.application.ApplicationStatus;
import com.thinkparity.browser.platform.application.L18nContext;
import com.thinkparity.browser.platform.application.display.Display;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.application.window.Window;
import com.thinkparity.browser.platform.util.State;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.artifact.ArtifactState;
import com.thinkparity.model.parity.model.index.IndexHit;
import com.thinkparity.model.xmpp.contact.Contact;

/**
 * The controller is used to manage state as well as control display of the
 * parity browser.
 * 
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Browser extends AbstractApplication {

	/**
	 * Instance of the browser.
	 * 
	 * @see #Browser(Platform)
	 * @see #getInstance()
	 */
	private static Browser INSTANCE;

	/**
	 * Obtain the instance of the controller.
	 * 
	 * @return The instance of the controller.
	 */
	public static Browser getInstance() { return INSTANCE; }

	/**
	 * Apache logger.
	 * 
	 */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Cache of all of the actions.
	 * 
	 */
	private final Map<ActionId, Object> actionCache;

	/**
	 * Provides a map of all avatar input.
	 * 
	 */
	private final Map<AvatarId, Object> avatarInputMap;

	/**
	 * The avatar registry.
	 * 
	 */
	private final AvatarRegistry avatarRegistry;

	/**
	 * The browser's event dispatcher.
	 * 
	 */
	private EventDispatcher ed;

	private History2Window history2Window;

    /**
     * Flag indicating whether or not history is enabled.
     * 
     */
	private Boolean historyEnabled = Boolean.FALSE;

	/**
	 * The file chooser.
	 * 
	 * @see #getJFileChooser()
	 */
	private JFileChooser jFileChooser;
	
	/**
	 * Main window.
	 * 
	 */
	private BrowserWindow mainWindow;

	/**
	 * Contains the browser's session information.
	 * 
	 */
	private final BrowserSession session;

	/**
	 * The state information for the controller.
	 * 
	 */
	private final BrowserState state;

	/**
	 * Create a Browser [Singleton]
	 * 
	 */
	public Browser(final Platform platform) {
		super(platform, L18nContext.BROWSER2);
		Assert.assertIsNull("Cannot create a second browser.", INSTANCE);
		INSTANCE = this;
		this.actionCache = new Hashtable<ActionId, Object>(ActionId.values().length, 1.0F);
		this.avatarInputMap = new Hashtable<AvatarId, Object>(AvatarId.values().length, 1.0F);
		this.avatarRegistry = new AvatarRegistry();
		this.session= new BrowserSession(this);
		this.state = new BrowserState(this);
	}

    /**
     * Apply a key holder filter.
     * 
     * @param keyHolder
     *            True to filter by keys; false to filter by non-keys.
     * 
     * @see BrowserMainAvatar#applyKeyHolderFilter(Boolean)
     */
    public void applyKeyHolderFilter(final Boolean keyHolder) {
        getMainAvatar().applyKeyHolderFilter(keyHolder);
    }

    /**
     * Set the search results in the search filter and apply it to the document
     * list.
     * 
     * @param searchResult
     *            The search results.
     */
	public void applySearchFilter(final List<IndexHit> searchResult) {
	    getMainAvatar().applySearchFilter(searchResult);
	}

    /**
     * Apply an artifact state filter.
     * 
     * @param state
     *            The artifact state.
     * @see ArtifactState
     * @see BrowserMainAvatar#applyStateFilter(ArtifactState)
     */
    public void applyStateFilter(final ArtifactState state) {
        getMainAvatar().applyStateFilter(state);
    }

    /**
     * Clear the non-search filters for the main list.
     *
     * @see #removeSearchFilter()
     */
    public void clearFilters() { getMainAvatar().clearFilters(); }

    /**
	 * Close the main window.
	 *
	 */
	public void close() { getPlatform().hibernate(getId()); }

    /**
     * Debug the filter applied to the main list.
     *
     */
	public void debugFilter() { getMainAvatar().debugFilter(); }

    /**
     * Disable the ability to display the history. Also; if the history window
     * is currently displayed; close it.
     * 
     */
    public void disableHistory() {
        historyEnabled = Boolean.FALSE;
        if(isHistoryVisible()) { toggleHistory3Avatar(); }
        getInfoAvatar().reload();
    }

	public void displayContactSearch() {
		displayAvatar(WindowId.POPUP, AvatarId.SESSION_SEARCH_PARTNER);
	}

	/**
     * Display send version.
     * 
     */
	public void displaySendVersion(final Long artifactId, final Long versionId) {
		final Data data = new Data(2);
		data.set(SessionSendVersion.DataKey.ARTIFACT_ID, artifactId);
		data.set(SessionSendVersion.DataKey.VERSION_ID, versionId);
		displayAvatar(WindowId.POPUP, AvatarId.SESSION_SEND_VERSION, data);
	}

	/**
	 * Display the invite partner dialogue.
	 *
	 */
	public void displaySessionInvitePartner() {
		displayAvatar(WindowId.POPUP, AvatarId.SESSION_INVITE_PARTNER);
	}

	/**
	 * Display the manage contacts dialogue.
	 *
	 */
	public void displaySessionManageContacts() {
		displayAvatar(WindowId.POPUP, AvatarId.SESSION_MANAGE_CONTACTS);
	}

	/**
	 * Display the send form.
	 *
	 */
	public void displaySessionSendFormAvatar() {
		displayAvatar(WindowId.POPUP, AvatarId.SESSION_SEND_FORM);
	}

    /**
     * Enable the history.
     * 
     */
	public void enableHistory() {
        historyEnabled = Boolean.TRUE;
        getInfoAvatar().reload();
	}

	/**
	 * @see com.thinkparity.browser.platform.application.Application#end()
	 * 
	 */
	public void end(final Platform platform) {
		logger.info("[BROWSER2] [APP] [B2] [END]");
		assertStatusChange(ApplicationStatus.ENDING);

		ed.end();
		ed = null;

		if(isMainWindowOpen()) { closeMainWindow(); }

		setStatus(ApplicationStatus.ENDING);
		notifyEnd();
	}

	/**
	 * Notify the application that a document has been created.
	 * 
	 * @param documentId
	 *            The document id.
	 */
	public void fireDocumentCreated(final Long documentId) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				((BrowserInfoAvatar) avatarRegistry.get(AvatarId.BROWSER_INFO)).reload();
			}
		});
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				((BrowserMainAvatar) avatarRegistry.get(AvatarId.BROWSER_MAIN)).reloadDocument(documentId, Boolean.FALSE);
			}
		});
	}

	/**
	 * Notify the application that a document has been created.
	 * 
	 * @param documentId
	 *            The document id.
	 */
	public void fireDocumentDeleted(final Long documentId) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				((BrowserInfoAvatar) avatarRegistry.get(AvatarId.BROWSER_INFO)).reload();
			}
		});
		// refresh the document main list
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				((BrowserMainAvatar) avatarRegistry.get(AvatarId.BROWSER_MAIN)).reloadDocument(documentId, Boolean.FALSE);
			}
		});
	}

	/**
     * Notify the application that a document has been received.
     * 
     * @param documentId
     *            The document id.
     */
	public void fireDocumentReceived(final Long documentId) {
		// refresh the document main list
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				((BrowserMainAvatar) avatarRegistry.get(AvatarId.BROWSER_MAIN)).reloadDocument(documentId, Boolean.TRUE);
			}
		});
	}

	/**
	 * Notify the application that a document has in some way been updated.
	 * 
	 * @param documentId
	 *            The document that has changed.
	 */
	public void fireDocumentUpdated(final Long documentId) {
		fireDocumentUpdated(documentId, Boolean.FALSE);
	}

	public void fireDocumentUpdated(final Long documentId, final Boolean remoteReload) {
		// refresh the document in the main list
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				((BrowserMainAvatar) avatarRegistry.get(AvatarId.BROWSER_MAIN)).reloadDocument(documentId, remoteReload);
			}
		});
	}

	/**
     * Notify the application that a system message has been created.
     * 
     * @param systemMessageId
     *            The system message id.
     */
	public void fireSystemMessageCreated(final Long systemMessageId) {
		// refresh the system message in the main list
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
//				((BrowserMainAvatar) avatarRegistry.get(AvatarId.BROWSER_MAIN)).reloadSystemMessage(systemMessageId);
			}
		});
	}

	/**
     * Notify the application that a system message has been deleted.
     * 
     * @param systemMessageId
     *            The system message id.
     */
	public void fireSystemMessageDeleted(final Long systemMessageId) {
		// refresh the system message in the main list
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
//				((BrowserMainAvatar) avatarRegistry.get(AvatarId.BROWSER_MAIN)).reloadSystemMessage(systemMessageId);
			}
		});
	}

	/**
	 * @see com.thinkparity.browser.platform.application.Application#getId()
	 * 
	 */
	public ApplicationId getId() { return ApplicationId.BROWSER2; }

	/**
	 * Obtain the platform.
	 * 
	 * @return The platform the application is running on.
	 */
	public Platform getPlatform() { return super.getPlatform(); }

	/**
	 * Obtain the selected document id from the session.
	 * 
	 * @return A document id.
	 */
	public Long getSelectedDocumentId() { return session.getSelectedDocumentId(); }

	/**
	 * Obtain the selected system message.
	 * 
	 * @return The selected system message id.
	 */
	public Object getSelectedSystemMessage() { return null; }

    /**
	 * @see com.thinkparity.browser.platform.application.Application#hibernate()
	 * 
	 */
	public void hibernate(final Platform platform) {
		assertStatusChange(ApplicationStatus.HIBERNATING);

		closeMainWindow();

		setStatus(ApplicationStatus.HIBERNATING);
		notifyHibernate();
	}

    /**
     * Determine if the history is enabled.
     * 
     * @return True if the history is enabled; false otherwise.
     * 
     * @see #enableHistory()
     * @see #disableHistory()
     */
    public Boolean isHistoryEnabled() { return historyEnabled; }

    /**
     * Determine whether or not the main avatar's filter is enabled.
     * 
     * @return True if it is; false otherwise.
     */
    public Boolean isFilterEnabled() {
        if(null == getMainAvatar()) { return Boolean.FALSE; }
        return getMainAvatar().isFilterEnabled();
    }

    /**
     * Check if the history is currently visible.
     * 
     * @return True if the history is visible; false otherwise.
     */
    public Boolean isHistoryVisible() {
		return null != history2Window && history2Window.isVisible();
	}

	/**
	 * Minimize the browser application.
	 *
	 */
	public void minimize() {
		if(!isMinimized()) { mainWindow.setExtendedState(JFrame.ICONIFIED); }
	}

	/**
	 * Move the browser window.
	 * 
	 * @param l
	 *            The new relative location of the window.
	 */
	public void moveBrowserWindow(final Point l) {
		logger.info("moveMainWindow(Point)");
		logger.debug(l);
		final Point newL = mainWindow.getLocation();
		newL.x += l.x;
		newL.y += l.y;
		logger.debug(newL);
		mainWindow.setLocation(newL);
		if(null != history2Window && history2Window.isVisible()) {
			final Point hl = history2Window.getLocation();
			hl.x += l.x;
			hl.y += l.y;
			history2Window.setLocation(hl);
		}
	}

	/**
	 * Reload the main list.
	 *
	 */
	public void reloadMainList() {
		getPlatform().getAvatarRegistry().get(AvatarId.BROWSER_MAIN).reload();
	}

	/**
     * Remove the key holder filter.
     * 
     * @see BrowserMainAvatar#removeKeyHolderFilter()
     */
    public void removeKeyHolderFilter() {
        getMainAvatar().removeKeyHolderFilter();
    }

	/**
	 * Remove the search filter from the document list.
	 *
	 */
	public void removeSearchFilter() { getMainAvatar().removeSearchFilter(); }

	/**
     * Remove the artifact state filter.
     * 
     * @see BrowserMainAvatar#removeStateFilter()
     */
    public void removeStateFilter() { getMainAvatar().removeStateFilter(); }

    /**
	 * @see com.thinkparity.browser.platform.application.Application#restore(com.thinkparity.browser.platform.Platform)
	 * 
	 */
	public void restore(final Platform platform) {
		assertStatusChange(ApplicationStatus.RESTORING);

		openMainWindow();

		setStatus(ApplicationStatus.RESTORING);
		notifyRestore();

		assertStatusChange(ApplicationStatus.RUNNING);
		setStatus(ApplicationStatus.RUNNING);
	}

	/**
	 * @see com.thinkparity.browser.platform.Saveable#restoreState(com.thinkparity.browser.platform.util.State)
	 * 
	 */
	public void restoreState(final State state) {}

	/**
	 * Accept an invitation.
	 * 
	 * @param systemMessageId
	 *            The system message id.
	 */
	public void runAcceptContactInvitation(final Long systemMessageId) {
		final Data data = new Data(1);
		data.set(AcceptInvitation.DataKey.SYSTEM_MESSAGE_ID, systemMessageId);
		invoke(ActionId.SESSION_ACCEPT_INVITATION, data);
	}

	/**
	 * Run the accept key action.
	 * 
	 * @param keyRequestId
	 *            The key request id.
	 */
	public void runAcceptKeyRequest(final Long artifactId,
			final Long keyRequestId) {
		final Data data = new Data(2);
		data.set(AcceptKeyRequest.DataKey.ARTIFACT_ID, artifactId);
		data.set(AcceptKeyRequest.DataKey.KEY_REQUEST_ID, keyRequestId);
		invoke(ActionId.ARTIFACT_ACCEPT_KEY_REQUEST, data);
	}

	/**
	 * Run the close document action.
	 * 
	 * @param documentId
	 *            The document unique id.
	 */
	public void runCloseDocument(final Long documentId) { 
		final Data data = new Data(1);
		data.set(Close.DataKey.DOCUMENT_ID, documentId);
		invoke(ActionId.DOCUMENT_CLOSE, data);
	}

	/**
	 * Run the create document action.
	 *
	 */
	public void runCreateDocument() {
		if(JFileChooser.APPROVE_OPTION == getJFileChooser().showOpenDialog(mainWindow)) {
			final Data data = new Data(1);
			data.set(Create.DataKey.FILE, jFileChooser.getSelectedFile());
			invoke(ActionId.DOCUMENT_CREATE, data);
		}
	}

	/**
	 * Run the decline key action.
	 * 
	 * @param keyRequestId
	 *            The key request id.
	 */
	public void runDeclineAllKeyRequests(final Long artifactId) {
		final Data data = new Data(1);
		data.set(DeclineAllKeyRequests.DataKey.ARTIFACT_ID, artifactId);
		invoke(ActionId.ARTIFACT_DECLINE_ALL_KEY_REQUESTS, data);
	}

	public void runDeclineContactInvitation(final Long systemMessageId) {
		final Data data = new Data(1);
		data.set(DeclineInvitation.DataKey.SYSTEM_MESSAGE_ID, systemMessageId);
		invoke(ActionId.SESSION_DECLINE_INVITATION, data);
	}

	/**
	 * Run the decline key action.
	 * 
	 * @param keyRequestId
	 *            The key request id.
	 */
	public void runDeclineKeyRequest(final Long artifactId,
			final Long keyRequestId) {
		final Data data = new Data(2);
		data.set(DeclineKeyRequest.DataKey.ARTIFACT_ID, artifactId);
		data.set(DeclineKeyRequest.DataKey.KEY_REQUEST_ID, keyRequestId);
		invoke(ActionId.ARTIFACT_DECLINE_KEY_REQUEST, data);
	}

	/**
	 * Run the delete document action.
	 * 
	 * @param documentId
	 *            The document id.
	 */
	public void runDeleteDocument(final Long documentId) {
		final Data data = new Data(1);
		data.set(Delete.DataKey.DOCUMENT_ID, documentId);
		invoke(ActionId.DOCUMENT_DELETE, data);
	}

	public void runDeleteSystemMessage(final Long systemMessageId) {
		final Data data = new Data(1);
		data.set(DeleteSystemMessage.DataKey.SYSTEM_MESSAGE_ID, systemMessageId);
		invoke(ActionId.SYSTEM_MESSAGE_DELETE, data);
	}

	/**
	 * Run the open document action.
	 * 
	 * @param documentId
	 *            The document id.
	 */
	public void runOpenDocument(final Long documentId) {
		final Data data = new Data(1);
		data.set(Open.DataKey.DOCUMENT_ID, documentId);
		invoke(ActionId.DOCUMENT_OPEN, data);
	}

	/**
	 * Run the open document version action.
	 * 
	 * @param documentId
	 *            The document id.
	 * @param versionId
	 *            The document's version id.
	 */
	public void runOpenDocumentVersion(final Long documentId,
			final Long versionId) {
		final Data data = new Data(2);
		data.set(OpenVersion.DataKey.DOCUMENT_ID, documentId);
		data.set(OpenVersion.DataKey.VERSION_ID, versionId);
		invoke(ActionId.DOCUMENT_OPEN_VERSION, data);
	}

	/**
	 * Run the request key action.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 */
	public void runRequestKey(final Long artifactId) {
		final Data data = new Data(1);
		data.set(RequestKey.DataKey.ARTIFACT_ID, artifactId);
		invoke(ActionId.ARTIFACT_REQUEST_KEY, data);
	}

	/**
     * Run a search for an artifact on the criteria.
     * 
     * @param criteria
     *            The search criteria.
     */
	public void runSearchArtifact(final String criteria) {
		final Data data = new Data(1);
		data.set(Search.DataKey.CRITERIA, criteria);
		invoke(ActionId.ARTIFACT_SEARCH, data);
	}

    /**
     * Run the send artifact version action.
     * 
     * @param artifactId
     *            The artifact id.
     * @param contacts
     *            The contacts to send to.
     * @param versionId
     *            The version id.
     */
    public void runSendArtifactVersion(final Long artifactId,
            final List<Contact> contacts, final Long versionId) {
        final Data data = new Data(3);
        data.set(SendVersion.DataKey.ARTIFACT_ID, artifactId);
        data.set(SendVersion.DataKey.CONTACTS, contacts);
        data.set(SendVersion.DataKey.VERSION_ID, versionId);
        invoke(ActionId.ARTIFACT_SEND_VERSION, data);
    }

    /**
     * Run the send artifact version action.
     * 
     * @param artifactId
     *            The artifact id.
     * @param contact
     *            The contact to send to.
     * @param versionId
     *            The version id.
     * 
     * @see #runSendArtifactVersion(Long, List, Long)
     */
    public void runSendArtifactVersion(final Long artifactId,
            final Contact contact, final Long versionId) {
        final List<Contact> contacts = new LinkedList<Contact>();
        contacts.add(contact);
        runSendArtifactVersion(artifactId, contacts, versionId);
    }

	/**
	 * @see com.thinkparity.browser.platform.Saveable#saveState(com.thinkparity.browser.platform.util.State)
	 * 
	 */
	public void saveState(final State state) {}

	/**
	 * Select a document.
	 * 
	 * @param documentId
	 *            The document id.
	 */
	public void selectDocument(final Long documentId) {
		session.setSelectedDocumentId(documentId);
		setInput(AvatarId.DOCUMENT_HISTORY3, documentId);
		setInput(AvatarId.SESSION_SEND_FORM, documentId);
	}

	/**
	 * @see com.thinkparity.browser.platform.application.Application#start()
	 * 
	 */
	public void start(final Platform platform) {
		logger.info("[BROWSER2] [APP] [B2] [START]");

		assertStatusChange(ApplicationStatus.STARTING);
		setStatus(ApplicationStatus.STARTING);

		ed = new EventDispatcher(this);
		ed.start();

		assertStatusChange(ApplicationStatus.RUNNING);
		openMainWindow();

		setStatus(ApplicationStatus.RUNNING);
		notifyStart();
	}

	/**
	 * Toggle the history window.
	 *
	 */
	public void toggleHistory3Avatar() {
	    if(null == history2Window) {
			final Avatar avatar = getAvatar(AvatarId.DOCUMENT_HISTORY3);

			if(null != getAvatarInput(AvatarId.DOCUMENT_HISTORY3))
				avatar.setInput(getAvatarInput(AvatarId.DOCUMENT_HISTORY3));

			history2Window = new History2Window(mainWindow, avatar);
			history2Window.setVisible(true);

			runApplyFlagSeen(getSelectedDocumentId());
			fireDocumentUpdated(getSelectedDocumentId());
		}
		else {
			history2Window.dispose();
			history2Window = null;
		}
	}

	/**
	 * Display the document list.
	 *
	 */
	void displayDocumentListAvatar() {
		displayAvatar(DisplayId.CONTENT, AvatarId.BROWSER_MAIN);
	}

	/**
	 * Display the browser info.
	 *
	 */
	void displayInfoAvatar() {
		displayAvatar(DisplayId.INFO, AvatarId.BROWSER_INFO);
	}

	/**
	 * Display the browser's title avatar.
	 *
	 */
	void displayTitleAvatar() {
    	displayAvatar(DisplayId.TITLE, AvatarId.BROWSER_TITLE);
	}

	/**
	 * Reload the document history list.
	 *
	 */
	void reloadHistoryList() {
		final Avatar avatar = getPlatform().getAvatarRegistry().get(AvatarId.DOCUMENT_HISTORY3);
		if(null != avatar) { avatar.reload(); }
	}

	private void closeMainWindow() {
		Assert.assertNotNull(
				"Cannot close main window before it is open.", mainWindow);
		mainWindow.dispatchEvent(
				new WindowEvent(mainWindow, WindowEvent.WINDOW_CLOSING));
	}

	/**
	 * Display an avatar.
	 * 
	 * @param displayId
	 *            The display to use.
	 * @param avatarId
	 *            The avatar to display.
	 */
	private void displayAvatar(final DisplayId displayId, final AvatarId avatarId) {
		Assert.assertNotNull("Cannot display on a null display.", displayId);
		Assert.assertNotNull("Cannot display a null avatar.", avatarId);
		final Display display = mainWindow.getDisplay(displayId);

		final Avatar currentAvatar = display.getAvatar();
		state.saveAvatarState(currentAvatar);

		final Avatar nextAvatar = getAvatar(avatarId);
		state.loadAvatarState(nextAvatar);

		final Object input = getAvatarInput(avatarId);
		if(null == input) { logger.info("Null input:  " + avatarId); }
		else { nextAvatar.setInput(getAvatarInput(avatarId)); }

		display.setAvatar(nextAvatar);
		display.displayAvatar();
		display.revalidate();
		display.repaint();
	}

	/**
	 * Display an avatar.
	 * 
	 * @param displayId
	 *            The display to use.
	 * @param avatarId
	 *            The avatar to display.
	 */
	private void displayAvatar(final WindowId windowId, final AvatarId avatarId) {
		Assert.assertNotNull("Cannot display on a null window.", windowId);
		Assert.assertNotNull("Cannot display a null avatar.", avatarId);
		final Window window = WindowFactory.create(windowId, mainWindow);

		final Avatar avatar = getAvatar(avatarId);

		final Object input = getAvatarInput(avatarId);
		if(null == input) { logger.info("Null input:  " + avatarId); }
		else { avatar.setInput(getAvatarInput(avatarId)); }

		SwingUtilities.invokeLater(new Runnable() {
			public void run() { window.open(avatar); }
		});
	}

	private void displayAvatar(final WindowId windowId,
			final AvatarId avatarId, final Data input) {
		Assert.assertNotNull("Cannot display on a null window.", windowId);
		Assert.assertNotNull("Cannot display a null avatar.", avatarId);
		final Window window = WindowFactory.create(windowId, mainWindow);

		final Avatar avatar = getAvatar(avatarId);
		avatar.setInput(input);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() { window.open(avatar); }
		});
	}

	/**
	 * Obtain the action from the controller's cache. If the action does not
	 * exist in the cache it is created and stored.
	 * 
	 * @param actionId
	 *            The action id.
	 * @return The action.
	 * 
	 * @see ActionId
	 */
	private AbstractAction getActionFromCache(final ActionId actionId) {
		AbstractAction action = (AbstractAction) actionCache.get(actionId);
		if(null == action) { action = ActionFactory.createAction(actionId, this); }
		actionCache.put(actionId, action);
		return action;
	}

	/**
	 * Obtain the input for an avatar.
	 * 
	 * @param avatarId
	 *            The avatar id.
	 * @return The avatar input.
	 */
	private Object getAvatarInput(final AvatarId avatarId) {
		return avatarInputMap.get(avatarId);
	}

	/**
     * Convenience method to obtain the info avatar.
     * 
     * @return The info avatar.
     */
    private BrowserInfoAvatar getInfoAvatar() {
        return (BrowserInfoAvatar) avatarRegistry.get(AvatarId.BROWSER_INFO);
    }

	/**
	 * Obtain the file chooser.
	 * 
	 * @return The file chooser.
	 */
	private JFileChooser getJFileChooser() {
		if(null == jFileChooser) { jFileChooser = new JFileChooser(); }
		return jFileChooser;
	}

    /**
     * Convenience method to obtain the main avatar.
     * 
     * @return The main avatar.
     */
    private BrowserMainAvatar getMainAvatar() {
        return (BrowserMainAvatar) avatarRegistry.get(AvatarId.BROWSER_MAIN);
    }

	private void invoke(final ActionId actionId, final Data data) {
		try {
			final AbstractAction action = getActionFromCache(actionId);
			action.invoke(data);
		}
		catch(final Exception x) { throw new RuntimeException(x); }
	}

	private Boolean isMainWindowOpen() {
		return null != mainWindow && mainWindow.isVisible();
	}

	private Boolean isMinimized() {
		return JFrame.ICONIFIED == mainWindow.getExtendedState();
	}

	/**
	 * Open the main browser window.
	 *
	 */
	private void openMainWindow() {
		mainWindow = new BrowserWindow(this);
		mainWindow.open();
	}

	/**
	 * Run the ApplyFlagSeen action.
	 * 
	 * @param documentId
	 *            The document to apply the seen flag to.
	 */
	private void runApplyFlagSeen(final Long documentId) {
		final Data data = new Data(1);
		data.set(ApplyFlagSeen.DataKey.ARTIFACT_ID, documentId);
		invoke(ActionId.ARTIFACT_APPLY_FLAG_SEEN, data);
	}

	/**
	 * Set the input for an avatar. If the avatar is currently being displayed;
	 * it will be set immediately; otherwise it will be stored in the local
	 * hash; and set when the avatar is displayed.
	 * 
	 * @param avatarId
	 *            The avatar id.
	 * @param input
	 *            The avatar input.
	 */
	private void setInput(final AvatarId avatarId, final Object input) {
		final Avatar avatar = getAvatar(avatarId);
		if(null == avatar) {
			logger.warn("Avatar " + avatarId + " not yet available.");
			avatarInputMap.put(avatarId, input);
		}
		else {
			avatarInputMap.remove(avatarId);
			avatar.setInput(input);
		}
	}
}

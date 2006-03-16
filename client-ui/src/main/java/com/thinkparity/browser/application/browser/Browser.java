/*
 * Jan 18, 2006
 */
package com.thinkparity.browser.application.browser;

import java.awt.Point;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.browser.display.DisplayId;
import com.thinkparity.browser.application.browser.display.avatar.AvatarFactory;
import com.thinkparity.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.browser.application.browser.display.avatar.AvatarRegistry;
import com.thinkparity.browser.application.browser.display.avatar.BrowserInfoAvatar;
import com.thinkparity.browser.application.browser.window.History2Window;
import com.thinkparity.browser.application.browser.window.HistoryWindow;
import com.thinkparity.browser.application.browser.window.WindowFactory;
import com.thinkparity.browser.application.browser.window.WindowId;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionFactory;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;
import com.thinkparity.browser.platform.action.artifact.AcceptKeyRequest;
import com.thinkparity.browser.platform.action.artifact.DeclineKeyRequest;
import com.thinkparity.browser.platform.action.document.Close;
import com.thinkparity.browser.platform.action.document.Create;
import com.thinkparity.browser.platform.action.document.Delete;
import com.thinkparity.browser.platform.action.document.Open;
import com.thinkparity.browser.platform.action.document.OpenVersion;
import com.thinkparity.browser.platform.action.session.AcceptInvitation;
import com.thinkparity.browser.platform.action.session.DeclineInvitation;
import com.thinkparity.browser.platform.action.session.RequestKey;
import com.thinkparity.browser.platform.action.system.message.DeleteSystemMessage;
import com.thinkparity.browser.platform.application.Application;
import com.thinkparity.browser.platform.application.ApplicationId;
import com.thinkparity.browser.platform.application.display.Display;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.application.window.Window;
import com.thinkparity.browser.platform.application.window.WindowRegistry;
import com.thinkparity.browser.platform.util.State;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

import com.thinkparity.codebase.assertion.Assert;

/**
 * The controller is used to manage state as well as control display of the
 * parity browser.
 * 
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Browser implements Application {

	/**
	 * Singleton instance of the controller.
	 * 
	 */
	private static final Browser singleton;

	static { singleton = new Browser(); }

	/**
	 * Obtain the instance of the controller.
	 * 
	 * @return The instance of the controller.
	 */
	public static Browser getInstance() { return singleton; }

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
	 * Main window.
	 * 
	 */
	private BrowserWindow mainWindow;

	/**
	 * The state information for the controller.
	 * 
	 */
	private final BrowserState state;

	/**
	 * The window registry.
	 * 
	 */
	private final WindowRegistry windowRegistry;

	/**
	 * Create a Browser [Singleton]
	 * 
	 */
	private Browser() {
		super();
		this.avatarRegistry = new AvatarRegistry();
		this.actionCache = new Hashtable<ActionId, Object>(ActionId.values().length, 1.0F);
		this.avatarInputMap = new Hashtable<AvatarId, Object>(AvatarId.values().length, 1.0F);
		this.state = new BrowserState(this);
		this.windowRegistry = new WindowRegistry();
	}

	/**
	 * Toggle the document history display.
	 *
	 */
	public void toggleHistoryAvatar() {
		if(null == historyWindow) {
			if(!avatarRegistry.contains(AvatarId.DOCUMENT_HISTORY))
				AvatarFactory.create(AvatarId.DOCUMENT_HISTORY);
			final Avatar avatar = avatarRegistry.get(AvatarId.DOCUMENT_HISTORY);
			if(null != getAvatarInput(AvatarId.DOCUMENT_HISTORY))
				avatar.setInput(getAvatarInput(AvatarId.DOCUMENT_HISTORY));
			
			historyWindow = new HistoryWindow(mainWindow, avatar);
			historyWindow.setVisible(true);
		}
		else {
			historyWindow.dispose();
			historyWindow = null;
		}
	}

	public void toggleHistory3Avatar() {
	    if(null == history2Window) {
		final AvatarId id = AvatarId.DOCUMENT_HISTORY3;
		if(!avatarRegistry.contains(id))
		    AvatarFactory.create(id);
		final Avatar avatar = avatarRegistry.get(id);
		if(null != getAvatarInput(id))
		    avatar.setInput(getAvatarInput(id));

		history2Window = new History2Window(mainWindow, avatar);
		history2Window.setVisible(true);
	    }
	    else {
		history2Window.dispose();
		history2Window = null;
	    }
	}
	
	public void toggleHistory2Avatar() {
		if(null == history2Window) {
			if(!avatarRegistry.contains(AvatarId.DOCUMENT_HISTORY2))
				AvatarFactory.create(AvatarId.DOCUMENT_HISTORY2);
			final Avatar avatar = avatarRegistry.get(AvatarId.DOCUMENT_HISTORY2);
			if(null != getAvatarInput(AvatarId.DOCUMENT_HISTORY2))
				avatar.setInput(getAvatarInput(AvatarId.DOCUMENT_HISTORY2));
			
			history2Window = new History2Window(mainWindow, avatar);
			history2Window.setVisible(true);
		}
		else {
			history2Window.dispose();
			history2Window = null;
		}
	}

	public Boolean isHistoryVisible() {
		return null != historyWindow && historyWindow.isVisible();
	}

	private HistoryWindow historyWindow;

	private History2Window history2Window;

	/**
	 * Display the login avatar.
	 *
	 */
	public void displayLoginAvatar() {
		displayAvatar(DisplayId.CONTENT, AvatarId.SESSION_LOGIN);
	}

	/**
	 * Display the main browser avatar.
	 *
	 */
	public void displayMainBrowserAvatar() {
		displayAvatar(DisplayId.CONTENT, AvatarId.BROWSER_MAIN);
	}

	/**
	 * Display the invite contact form.
	 *
	 */
	public void displaySessionInviteContact() {
		displayAvatar(WindowId.POPUP, AvatarId.SESSION_INVITE_CONTACT);
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
		putClientProperty(AvatarId.SESSION_SEND_FORM, "doIncludeKey", Boolean.FALSE);
		displayAvatar(WindowId.POPUP, AvatarId.SESSION_SEND_FORM);
	}

	/**
	 * @see com.thinkparity.browser.platform.application.Application#end()
	 * 
	 */
	public void end() { closeMainWindow(); }

	/**
	 * @see com.thinkparity.browser.platform.application.Application#getId()
	 * 
	 */
	public ApplicationId getId() { return ApplicationId.BROWSER; }

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
	public void hibernate() {}

	/**
	 * @see com.thinkparity.browser.platform.application.Application#launch()
	 * 
	 */
	public void launch() {}

	/**
	 * Move the main window.
	 * 
	 * @param relativeLocation
	 *            The new relative location of the window.
	 */
	public void moveMainWindow(final Point relativeLocation) {
		logger.info("moveMainWindow(Point)");
		logger.debug(relativeLocation);
		final Point l = mainWindow.getLocation();
		l.x += relativeLocation.x;
		l.y += relativeLocation.y;
		logger.debug(l);
		mainWindow.setLocation(l);
		if(null != historyWindow && historyWindow.isVisible()) {
			final Point l2 = historyWindow.getLocation();
			l2.x += relativeLocation.x;
			l2.y += relativeLocation.y;
			historyWindow.setLocation(l2);
		}
		if(null != history2Window && history2Window.isVisible()) {
			final Point l2 = history2Window.getLocation();
			l2.x += relativeLocation.x;
			l2.y += relativeLocation.y;
			history2Window.setLocation(l2);
		}
	}

	/**
	 * Open the main window.
	 *
	 */
	public void openMainWindow() {
		Assert.assertIsNull("Cannot reopen main window.", mainWindow);

		// Schedule a job for the event-dispatching thread:  creating and
		// showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	mainWindow = BrowserWindow.open();
            	displayTitleAvatar();
            	displayInfoAvatar();
            	displayDocumentListAvatar();
            }
        });
	}

	/**
	 * Reload the main browser avatar.
	 * 
	 * @deprecated Use {@link #reloadMainList()}
	 */
	public void reloadMainBrowserAvatar() { reloadMainList(); }

	/**
	 * Reload the main list.
	 *
	 */
	public void reloadMainList() {
		AvatarFactory.create(AvatarId.BROWSER_MAIN).reload();
	}

	/**
	 * @see com.thinkparity.browser.platform.Saveable#restore(com.thinkparity.browser.platform.util.State)
	 * 
	 */
	public void restore(final State state) {}

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
		reloadMainList();
	}

	public void runAcceptKeyRequest(final Long systemMessageId) {
		final Data data = new Data(2);
		data.set(AcceptKeyRequest.DataKey.SYSTEM_MESSAGE_ID, systemMessageId);
		invoke(ActionId.ARTIFACT_ACCEPT_KEY_REQUEST, data);
		reloadMainList();
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
	 * The file chooser.
	 * 
	 * @see #getJFileChooser()
	 */
	private JFileChooser jFileChooser;

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
	 * Run the create document action.
	 *
	 */
	public void runCreateDocument() {
		if(JFileChooser.APPROVE_OPTION == getJFileChooser().showOpenDialog(mainWindow)) {
			final Data data = new Data(1);
			data.set(Create.DataKey.FILE, jFileChooser.getSelectedFile());
			invoke(ActionId.DOCUMENT_CREATE, data);
			reloadMainList();
		}
	}

	public void runDeclineContactInvitation(final Long systemMessageId) {
		final Data data = new Data(1);
		data.set(DeclineInvitation.DataKey.SYSTEM_MESSAGE_ID, systemMessageId);
		invoke(ActionId.SESSION_DECLINE_INVITATION, data);
	}

	public void runDeclineKeyRequest(final Long systemMessageId) {
		final Data data = new Data(2);
		data.set(DeclineKeyRequest.DataKey.SYSTEM_MESSAGE_ID, systemMessageId);
		invoke(ActionId.ARTIFACT_DECLINE_KEY_REQUEST, data);
		reloadMainBrowserAvatar();
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
		reloadMainBrowserAvatar();
	}

	/**
	 * Invite a contact.
	 *
	 */
	public void runInviteContact() {
		invoke(ActionId.SESSION_ADD_CONTACT);
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
		reloadMainList();
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

	public void runRequestArtifactKey(final Long artifactId) {
		final Data data = new Data(1);
		data.set(RequestKey.DataKey.ARTIFACT_ID, artifactId);
		invoke(ActionId.SESSION_REQUEST_KEY, data);
	}

	/**
	 * @see com.thinkparity.browser.platform.Saveable#save(com.thinkparity.browser.platform.util.State)
	 * 
	 */
	public void save(final State state) {}

	/**
	 * Select a document.
	 * 
	 * @param documentId
	 *            The document id.
	 */
	public void selectDocument(final Long documentId) {
		setInput(AvatarId.DOCUMENT_HISTORY, documentId);
		setInput(AvatarId.DOCUMENT_HISTORY2, documentId);
		setInput(AvatarId.DOCUMENT_HISTORY3, documentId);
		setInput(AvatarId.SESSION_SEND_FORM, documentId);
	}

	/**
	 * @see com.thinkparity.browser.platform.application.Application#start()
	 * 
	 */
	public void start() { openMainWindow(); }

	/**
	 * Unselect a document.
	 * 
	 * @param documentId
	 *            The document id.
	 */
	public void unselectDocument(final Long documentId) { /* NOTE Huh? */ }

	/**
	 * Unselect the system message.
	 *
	 */
	public void unselectSystemMessage() {}

	/**
	 * Close the main window.
	 *
	 */
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

		final Avatar nextAvatar = AvatarFactory.create(avatarId);
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

		final Avatar avatar;
		if(avatarRegistry.contains(avatarId)) {
			avatar = avatarRegistry.get(avatarId);
		}
		else { avatar = AvatarFactory.create(avatarId); }

		final Object input = getAvatarInput(avatarId);
		if(null == input) { logger.info("Null input:  " + avatarId); }
		else { avatar.setInput(getAvatarInput(avatarId)); }

		SwingUtilities.invokeLater(new Runnable() {
			public void run() { window.open(avatar); }
		});
	}

	private void displayDocumentListAvatar() {
		displayAvatar(DisplayId.CONTENT, AvatarId.BROWSER_MAIN);
	}

	private void displayInfoAvatar() {
		displayAvatar(DisplayId.INFO, AvatarId.BROWSER_INFO);
	}

	/**
	 * Display the browser's title avatar.
	 *
	 */
	private void displayTitleAvatar() {
    	displayAvatar(DisplayId.TITLE, AvatarId.BROWSER_TITLE);
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
		if(null == action) { action = ActionFactory.createAction(actionId); }
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

	private void invoke(final ActionId actionId) {
		invoke(actionId, new Data(0));
	}

	private void invoke(final ActionId actionId, final Data data) {
		try {
			final AbstractAction action = getActionFromCache(actionId);
			action.invoke(data);
		}
		catch(final Exception x) {
			logger.error("Cannot invoke action:  " + actionId, x);
			// NOTE Display Error
		}
	}

	/**
	 * Use the putClientProperty api of the swing component to add a key\value
	 * pair to the avatar.
	 * 
	 * @param avatarId
	 *            The avatar.
	 * @param key
	 *            The key.
	 * @param value
	 *            The value.
	 */
	private void putClientProperty(final AvatarId avatarId, final String key,
			final Object value) {
		final Avatar avatar = AvatarFactory.create(avatarId);
		avatar.putClientProperty(key, value);
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
		final Avatar avatar = avatarRegistry.get(avatarId);
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

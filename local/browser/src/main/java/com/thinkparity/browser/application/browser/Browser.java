/*
 * Jan 18, 2006
 */
package com.thinkparity.browser.application.browser;

import java.awt.Point;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import java.util.Map;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.browser.display.DisplayId;
import com.thinkparity.browser.application.browser.display.avatar.AvatarFactory;
import com.thinkparity.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.browser.model.tmp.system.message.MessageId;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionFactory;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;
import com.thinkparity.browser.platform.action.document.Close;
import com.thinkparity.browser.platform.action.document.Delete;
import com.thinkparity.browser.platform.action.document.Open;
import com.thinkparity.browser.platform.action.document.OpenVersion;
import com.thinkparity.browser.platform.action.session.RequestKey;
import com.thinkparity.browser.platform.application.Application;
import com.thinkparity.browser.platform.application.ApplicationId;
import com.thinkparity.browser.platform.application.display.Display;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
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
	 * Create a Browser [Singleton]
	 * 
	 */
	private Browser() {
		super();
		this.actionCache = new Hashtable<ActionId, Object>(ActionId.values().length, 1.0F);
		this.avatarInputMap = new Hashtable<AvatarId, Object>(AvatarId.values().length, 1.0F);
		this.state = new BrowserState(this);
	}

	/**
	 * Display the document history list.
	 *
	 */
	public void displayDocumentHistoryListAvatar() {
		displayAvatar(DisplayId.INFO, AvatarId.DOCUMENT_HISTORY_LIST);
	}

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
	 * Display the send form.
	 *
	 */
	public void displaySessionSendFormAvatar() {
		putClientProperty(AvatarId.SESSION_SEND_FORM, "doIncludeKey", Boolean.FALSE);
		displayAvatar(DisplayId.CONTENT, AvatarId.SESSION_SEND_FORM);
	}

	/**
	 * Display the send key form.
	 *
	 */
	public void displaySessionSendKeyFormAvatar() {
		putClientProperty(AvatarId.SESSION_SEND_KEY_FORM, "doIncludeKey", Boolean.TRUE);
		displayAvatar(DisplayId.CONTENT, AvatarId.SESSION_SEND_KEY_FORM);
	}

	/**
	 * Display the system message avatar.
	 *
	 */
	public void displaySystemMessageAvatar() {
		displayAvatar(DisplayId.INFO, AvatarId.SYSTEM_MESSAGE);
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
            	displayLogoAvatar();
            	displayMainBrowserAvatar();
            	displayDocumentHistoryListAvatar();
            }
        });
	}

	/**
	 * Reload the main browser avatar.
	 *
	 */
	public void reloadMainBrowserAvatar() {
		// NOTE Wierd
		AvatarFactory.create(AvatarId.BROWSER_MAIN).reload();
		// NOTE Super wierd
		AvatarFactory.create(AvatarId.DOCUMENT_HISTORY_LIST).reload();
	}

	/**
	 * @see com.thinkparity.browser.platform.Saveable#restore(com.thinkparity.browser.platform.util.State)
	 * 
	 */
	public void restore(final State state) {}

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
		invoke(ActionId.DOCUMENT_CREATE);
	}

	public void runAddContact() {
		invoke(ActionId.SESSION_ADD_CONTACT);
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
		setInput(AvatarId.DOCUMENT_HISTORY_LIST, documentId);
		setInput(AvatarId.SESSION_SEND_FORM, documentId);
		setInput(AvatarId.SESSION_SEND_KEY_FORM, documentId);
	}

	/**
	 * Select the system message.
	 * 
	 * @param messageId
	 *            The message id.
	 */
	public void selectSystemMessage(final MessageId messageId) {
		setInput(AvatarId.SYSTEM_MESSAGE, messageId);
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
		if(null != currentAvatar) { currentAvatar.setDisplay(null); }

		final Avatar nextAvatar = AvatarFactory.create(avatarId);
		state.loadAvatarState(nextAvatar);

		final Object input = getAvatarInput(avatarId);
		if(null == input) { logger.info("Null input:  " + avatarId); }
		else { nextAvatar.setInput(getAvatarInput(avatarId)); }
		nextAvatar.setDisplay(display);

		display.setAvatar(nextAvatar);
		display.displayAvatar();
		display.revalidate();
		display.repaint();
	}

	/**
	 * Display the browser's logo avatar.
	 *
	 */
	private void displayLogoAvatar() {
    	displayAvatar(DisplayId.LOGO, AvatarId.BROWSER_LOGO);
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

	/**
	 * Obtain a list of all of the displays.
	 * 
	 * @return A list of all of the displays.
	 */
	private Display[] getDisplays() { return mainWindow.getDisplays(); }

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
		Assert.assertNotNull("Cannot set the input for a null avatar.", avatarId);
		final Display[] displays = getDisplays();
		Avatar avatar;
		for(Display display : displays) {
			avatar = display.getAvatar();
			if(null == avatar) {
				logger.warn("No avatar available on display:  " + display.getId());
			}
			else {
				if(avatarId == avatar.getId()) { avatar.setInput(input); }
				else {
					if(null == input) { logger.warn("Null input being set."); }
					else { avatarInputMap.put(avatarId, input); }
				}
			}
		}
	}
}

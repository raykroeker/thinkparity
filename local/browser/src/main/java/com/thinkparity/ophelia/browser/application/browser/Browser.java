/*
 * Created On: Jan 18, 2006
 */
package com.thinkparity.ophelia.browser.application.browser;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.swing.AbstractJFrame;
import com.thinkparity.codebase.swing.ThinkParityJFileChooser;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.user.TeamMember;

import com.thinkparity.ophelia.browser.Constants.Keys;
import com.thinkparity.ophelia.browser.application.AbstractApplication;
import com.thinkparity.ophelia.browser.application.browser.display.DisplayId;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.MainStatusAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.MainTitleAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.MainTitleAvatar.TabId;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.ConfirmAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.FileChooserAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.StatusAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.contact.UserInfoAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container.ContainerVersionCommentAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container.CreateContainerAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container.PublishContainerAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container.RenameContainerAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container.RenameDocumentAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container.UpdateDraftCommentAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarFilterDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.contact.ContactTabAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container.ContainerTabAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.help.HelpTabAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabButtonActionDelegate;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.platform.action.*;
import com.thinkparity.ophelia.browser.platform.action.contact.*;
import com.thinkparity.ophelia.browser.platform.action.container.*;
import com.thinkparity.ophelia.browser.platform.action.document.Open;
import com.thinkparity.ophelia.browser.platform.action.document.OpenVersion;
import com.thinkparity.ophelia.browser.platform.action.platform.LearnMore;
import com.thinkparity.ophelia.browser.platform.action.profile.AddEmail;
import com.thinkparity.ophelia.browser.platform.action.profile.RemoveEmail;
import com.thinkparity.ophelia.browser.platform.action.profile.Update;
import com.thinkparity.ophelia.browser.platform.action.profile.UpdatePassword;
import com.thinkparity.ophelia.browser.platform.action.profile.UpgradeAccount;
import com.thinkparity.ophelia.browser.platform.action.profile.VerifyEmail;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationStatus;
import com.thinkparity.ophelia.browser.platform.application.L18nContext;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.plugin.extension.TabListExtension;
import com.thinkparity.ophelia.browser.platform.plugin.extension.TabPanelExtension;
import com.thinkparity.ophelia.browser.platform.util.persistence.Persistence;
import com.thinkparity.ophelia.browser.platform.util.persistence.PersistenceFactory;
import com.thinkparity.ophelia.browser.platform.window.Window;
import com.thinkparity.ophelia.browser.platform.window.WindowFactory;

import org.apache.log4j.Logger;

/**
 * The controller is used to manage state as well as control display of the
 * parity browser.
 * 
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Browser extends AbstractApplication {

    /** Action registry. */
	private final ActionRegistry actionRegistry;

    /**
	 * Provides a map of all avatar input.
	 * 
	 */
	private final Map<AvatarId, Object> avatarInputMap;

	/** The browser controller's display helper. */
    private final BrowserDisplayHelper displayHelper;

	/** The browser's event dispatcher. */
	private EventDispatcher ed;

    /** The browser controller's keyboard helper. */
    private final BrowserKeyboardHelper keyboardHelper;

    /** The thinkParity browser application window. */
	private BrowserWindow mainWindow;

	/** A persistence for browser settings. */
    private final Persistence persistence;

	/** The browser's session implementation. */
	private final BrowserSessionImpl sessionImpl;

    /**
	 * Create Browser.
	 * 
	 */
	public Browser(final Platform platform) {
		super(platform, L18nContext.BROWSER2);
		this.actionRegistry = new ActionRegistry();
		this.avatarInputMap = new Hashtable<AvatarId, Object>(AvatarId.values().length, 1.0F);
        this.displayHelper = new BrowserDisplayHelper(this);
        this.keyboardHelper = new BrowserKeyboardHelper(this);
        this.persistence = PersistenceFactory.getPersistence(getClass());
		this.sessionImpl = new BrowserSessionImpl();
	}

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.Application#applyBusyIndicator()
     *
     */
    public void applyBusyIndicator() {
        mainWindow.applyBusyIndicator();
    }

	/**
     * Apply a search expression to a given tab.
     * 
     * @param expression
     *            The search expression <code>String</code>.
     */
	public void applySearch(final String expression) {
	    final Data data = new Data(1);
	    if (null != expression) {
	        data.set(TabAvatar.DataKey.SEARCH_EXPRESSION, expression);
        }
	    setInput(AvatarId.TAB_CONTACT, data);
	    setInput(AvatarId.TAB_CONTAINER, data);
        setInput(AvatarId.TAB_HELP, data);
        switch(getMainTitleAvatarTab()) {
        case CONTACT:
            break;
        case CONTAINER:
            break;
        case HELP:
            break;
        default:
            Assert.assertUnreachable("Unknown main title tab id.");
        }
	}

    /**
     * Clear all status links.
     */
    public void clearStatusLink() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final Data input = new Data(1);
                input.set(MainStatusAvatar.DataKey.CLEAR_LINK, Boolean.TRUE);
                setInput(AvatarId.MAIN_STATUS, input);
            }
        });
    }

    /** Close the main window. */
    public void closeBrowserWindow() {
        Assert.assertNotNull(mainWindow, "Main window is null.");
        mainWindow.dispatchEvent(new WindowEvent(mainWindow, WindowEvent.WINDOW_CLOSING));
    }

    /**
     * Collapse the contact.
     *
     * @param contactId
     *            The contact id.      
     */
    public void collapseContact(final JabberId contactId) {
        getTabContactAvatar().collapseContact(contactId);
    }

    /**
     * Collapse the container.
     *
     * @param containerId
     *            The containerId.       
     */
    public void collapseContainer(final Long containerId) {
        getTabContainerAvatar().collapseContainer(containerId);
    }

    /**
     * Open a confirmation dialog.
     * 
     * @param messageKey
     *            The confirmation message localization key.
     * @return True if the user confirmed in the affirmative.
     */
    public Boolean confirm(final String messageKey) {
        final Data input = new Data(1);
        input.set(ConfirmAvatar.DataKey.MESSAGE_KEY, messageKey);
        return confirm(input);
    }

    /**
     * Open a confirmation dialog.
     * 
     * @param messageKey
     *            The confirmation message localization key.
     * @param messageArguments
     *            A list of confirmation message arguments.
     * @return True if the user confirmed in the affirmative.
     */
    public Boolean confirm(final String messageKey,
            final Object[] messageArguments) {
        final Data input = new Data(2);
        input.set(ConfirmAvatar.DataKey.MESSAGE_KEY, messageKey);
        input.set(ConfirmAvatar.DataKey.MESSAGE_ARGUMENTS, messageArguments);
        return confirm(input);
    }

    /**
     * Open a confirmation dialog.
     * 
     * @param confirmButtonKey
     *            The confirm button text key <code>String</code>.
     * @param denyButtonKey
     *            The deny button text key <code>String</code>.
     * @param messageKey
     *            The message key <code>String</code>.
     * @param messageArguments
     *            The message arguments <code>Object[]</code>.
     * @return True if the user confirms the response.
     */
    public Boolean confirm(final String confirmButtonKey,
            final String denyButtonKey, final String messageKey,
            final Object... messageArguments) {
        final Data input = new Data(4);
        input.set(ConfirmAvatar.DataKey.CONFIRM_BUTTON_KEY, confirmButtonKey);
        input.set(ConfirmAvatar.DataKey.DENY_BUTTON_KEY, denyButtonKey);
        input.set(ConfirmAvatar.DataKey.MESSAGE_KEY, messageKey);
        input.set(ConfirmAvatar.DataKey.MESSAGE_ARGUMENTS, messageArguments);
        return confirm(input);

    }

    /**
     * Open a confirm dialog using an already localized message.
     * 
     * @param localizedMessage
     *            A localized message.
     * @return True if the user confirmed in the affirmative.
     */
    public Boolean confirmLocalized(final String localizedMessage) {
        final Data input = new Data(1);
        input.set(ConfirmAvatar.DataKey.LOCALIZED_MESSAGE, localizedMessage);
        return confirm(input);
    }

    /**
     * Display the contact info dialogue.
     *
     * @param contactId
     *            A contact id.
     */
    public void displayContactInfoDialog(final JabberId contactId) {
        final Data input = new Data(1);
        input.set(UserInfoAvatar.DataKey.USER_ID, contactId);
        setInput(AvatarId.DIALOG_CONTACT_INFO, input);
        displayAvatar(AvatarId.DIALOG_CONTACT_INFO);        
    }

    /**
     * Display the contact tab avatar.
     *
     */
    public void displayContactTabAvatar() {
        displayTab(AvatarId.TAB_CONTACT);
    }

    /**
     * Display the container tab avatar.
     *
     */
    public void displayContainerTabAvatar() {
        displayTab(AvatarId.TAB_CONTAINER);
    }

    /**
     * Display the container version comment dialog.
     * 
     * @param containerId
     *            The container id.
     * @param versionId
     *            A version id.        
     */
    public void displayContainerVersionInfoDialog(final Long containerId, final Long versionId) {
        final Data input = new Data(2);
        input.set(ContainerVersionCommentAvatar.DataKey.CONTAINER_ID, containerId);
        input.set(ContainerVersionCommentAvatar.DataKey.VERSION_ID, versionId);
        setInput(AvatarId.DIALOG_CONTAINER_VERSION_COMMENT, input);
        displayAvatar(AvatarId.DIALOG_CONTAINER_VERSION_COMMENT);
    }

    /**
     * Display the "new container" dialog (to create new packages).
     * If the user presses OK, runCreateContainer() is called and
     * provided with the container name.
     * 
     */
    public void displayCreateContainerDialog() {
        final Integer numFiles = 0;
        final Data input = new Data(1);
        input.set(CreateContainerAvatar.DataKey.NUM_FILES, numFiles);
        setInput(AvatarId.DIALOG_CONTAINER_CREATE, input);
        displayAvatar(AvatarId.DIALOG_CONTAINER_CREATE);
    }
    
    /**
     * Display the "new container" dialog (to create new packages).
     * If the user presses OK, runCreateContainer() is called and
     * provided with the container name.
     * This version has a list of files that will be added after the
     * container is created.
     * 
     * @param files
     *          List of files that will be added later
     */    
    public void displayCreateContainerDialog(final List<File> files) {
        final Integer numFiles = files.size();
        final Data input = new Data(2);
        input.set(CreateContainerAvatar.DataKey.NUM_FILES, numFiles);
        input.set(CreateContainerAvatar.DataKey.FILES, files);        
        setInput(AvatarId.DIALOG_CONTAINER_CREATE, input);
        displayAvatar(AvatarId.DIALOG_CONTAINER_CREATE);
    }
    
    /**
     * Handle a user error (show an error dialog).
     * 
     * @param errorMessageKey
     *            The error message localization key.
     */
    public void displayErrorDialog(final String errorMessageKey) {
        displayErrorDialog(errorMessageKey, null, null);
    }

    /**
     * Display an error dialog
     * 
     * @param errorMessageKey
     *            The error message localization key.
     * @param errorMessageArguments
     *            The error message arguments.
     */
    public void displayErrorDialog(final String errorMessageKey,
            final Object[] errorMessageArguments) {
        displayErrorDialog(errorMessageKey, errorMessageArguments, null);
    }

    /**
     * Display an error dialog
     * 
     * @param errorMessageKey
     *            The error message localization key (optional).
     * @param errorMessageArguments
     *            The error message arguments (optional).
     * @param error
     *            An error (optional).     
     */
    public void displayErrorDialog(final String errorMessageKey,
            final Object[] errorMessageArguments, final Throwable error) {
        getPlatform().displayErrorDialog(getId(), error, errorMessageKey,
                errorMessageArguments);
    }

    /**
     * Display an error dialog.
     * 
     * @param error
     *            An error.
     */
    public void displayErrorDialog(final Throwable error) {
        displayErrorDialog(null, null, error);
    }

    /**
     * Display a file chooser dialog.
     * 
     * @param fileSelectionMode
     *            The file selection mode <code>int</code>. See {@link JFileChooser#getFileSelectionMode()}.
     * @param multiSelection
     *            Multi-selection <code>Boolean</code>.
     * @param currentDirectory
     *            The current directory <code>File</code> (optional).
     * @param source
     *            A <code>String</code> key used to get title and approve button strings. (optional).
     * @return A <code>ThinkParityJFileChooser</code>.
     */
    public ThinkParityJFileChooser displayFileChooser(final int fileSelectionMode,
            final Boolean multiSelection, final File currentDirectory,
            final String source) {
        final Data input = new Data(4);
        input.set(FileChooserAvatar.DataKey.FILE_SELECTION_MODE, fileSelectionMode);
        input.set(FileChooserAvatar.DataKey.MULTI_SELECTION, multiSelection);
        if (null != currentDirectory) {
            input.set(FileChooserAvatar.DataKey.CURRENT_DIRECTORY, currentDirectory);
        }
        if (null != source) {
            input.set(FileChooserAvatar.DataKey.SOURCE, source);
        }
        return displayFileChooser(input);
    }

    /**
     * Display the help tab avatar.
     *
     */
    public void displayHelpTabAvatar() {
        displayTab(AvatarId.TAB_HELP);
    }

    /**
     * Display the "publish container" dialog.
     * If the user presses OK, the CONTAINER_PUBLISH action is invoked.
     * 
     * @param containerId
     *            The container id.        
     */
    public void displayPublishContainerDialog(final Long containerId) {
        final Data input = new Data(3);
        input.set(PublishContainerAvatar.DataKey.PUBLISH_TYPE, PublishContainerAvatar.PublishType.PUBLISH);
        input.set(PublishContainerAvatar.DataKey.CONTAINER_ID, containerId);
        setInput(AvatarId.DIALOG_CONTAINER_PUBLISH, input);
        displayAvatar(AvatarId.DIALOG_CONTAINER_PUBLISH);
    }

    /**
     * Display the "publish container" dialog for a version.
     * If the user presses OK, the CONTAINER_PUBLISH_VERSION action is invoked.
     * 
     * @param containerId
     *            The container id.
     * @param versionId
     *            A version id.
     */
    public void displayPublishContainerDialog(final Long containerId,
            final Long versionId) {
        final Data input = new Data(4);
        input.set(PublishContainerAvatar.DataKey.PUBLISH_TYPE, PublishContainerAvatar.PublishType.PUBLISH_VERSION);
        input.set(PublishContainerAvatar.DataKey.CONTAINER_ID, containerId);
        input.set(PublishContainerAvatar.DataKey.VERSION_ID, versionId);
        setInput(AvatarId.DIALOG_CONTAINER_PUBLISH, input);
        displayAvatar(AvatarId.DIALOG_CONTAINER_PUBLISH);
    }
    
    /**
     * Display a container rename dialog.
     * 
     * @param containerId
     *            The container id.
     * @param containerName
     *            A container name.
     */
    public void displayRenameContainerDialog(final Long containerId,
            final String containerName) {
        final Data input = new Data(2);
        input.set(RenameContainerAvatar.DataKey.CONTAINER_ID, containerId);
        input.set(RenameContainerAvatar.DataKey.CONTAINER_NAME, containerName);
        setInput(AvatarId.DIALOG_CONTAINER_RENAME, input);
        displayAvatar(AvatarId.DIALOG_CONTAINER_RENAME);
    }

    /**
     * Display a document rename dialog.
     * 
     * @param containerId
     *            The container id.
     * @param documentId
     *            A document id.
     * @param documentName
     *            A document name.
     */
    public void displayRenameDocumentDialog(final Long containerId,
            final Long documentId, final String documentName) {
        final Data input = new Data(3);
        input.set(RenameDocumentAvatar.DataKey.CONTAINER_ID, containerId);
        input.set(RenameDocumentAvatar.DataKey.DOCUMENT_ID, documentId);
        input.set(RenameDocumentAvatar.DataKey.DOCUMENT_NAME, documentName);
        setInput(AvatarId.DIALOG_CONTAINER_RENAME_DOCUMENT, input);
        displayAvatar(AvatarId.DIALOG_CONTAINER_RENAME_DOCUMENT);
    }

    /**
     * Display a status dialog.
     * 
     * @param statusMessageKey
     *            The status message localization key <code>String</code>.
     */
    public void displayStatusDialog(final String statusMessageKey) {
        displayStatusDialog(statusMessageKey, null);
    }

    /**
     * Display a status dialog.
     * 
     * @param statusMessageKey
     *            The status message localization key <code>String</code>.
     * @param statusMessageArguments
     *            The status message arguments (optional).
     */
    public void displayStatusDialog(final String statusMessageKey,
            final Object[] statusMessageArguments) {
        final Data input = new Data(2);
        input.set(StatusAvatar.DataKey.STATUS_MESSAGE_KEY, statusMessageKey);
        if (null != statusMessageArguments)
            input.set(StatusAvatar.DataKey.STATUS_MESSAGE_ARGUMENTS, statusMessageArguments);
        open(AvatarId.DIALOG_STATUS, input);
    }

    /** Display a tab list extension. */
    public void displayTabExtension(final TabListExtension tabListExtension) {
        displayTab(tabListExtension);
    }

    /** Display a tab panel extension. */
    public void displayTabExtension(final TabPanelExtension tabPanelExtension) {
        displayTab(tabPanelExtension);
    }

    /**
     * Display the update account dialog.
     */
    public void displayUpdateAccountDialog() {
        displayAvatar(AvatarId.DIALOG_PROFILE_UPDATE_ACCOUNT);
    }

    /**
     * Display the update draft comment dialog.
     * 
     * @param containerId
     *            The container id.
     */
    public void displayUpdateDraftCommentDialog(final Long containerId) {
        final Data input = new Data(1);
        input.set(UpdateDraftCommentAvatar.DataKey.CONTAINER_ID, containerId);
        setInput(AvatarId.DIALOG_CONTAINER_UPDATE_DRAFT_COMMENT, input);
        displayAvatar(AvatarId.DIALOG_CONTAINER_UPDATE_DRAFT_COMMENT);
    }

    /**
     * Display the update password dialog.
     */
    public void displayUpdatePasswordDialog() {
        displayAvatar(AvatarId.DIALOG_PROFILE_UPDATE_PASSWORD);
    }
    
    /**
     * Display the update profile dialog.
     *
     */
    public void displayUpdateProfileDialog() {
        displayAvatar(AvatarId.DIALOG_PROFILE_UPDATE);
    }

    /**
     * Display the upgrade account dialog.
     */
    public void displayUpgradeAccountDialog() {
        displayAvatar(AvatarId.DIALOG_PROFILE_UPGRADE_ACCOUNT);
    }

    /**
     * Display the verify email dialog.
     */
    public void displayVerifyEmailDialog() {
        displayAvatar(AvatarId.DIALOG_PROFILE_VERIFY_EMAIL);        
    }

    /**
	 * @see com.thinkparity.ophelia.browser.platform.application.Application#end()
	 * 
	 */
	public void end(final Platform platform) {
		logApiId();
		assertStatusChange(ApplicationStatus.ENDING);

		ed.end();
		ed = null;

		if (isBrowserWindowOpen()) {
            closeBrowserWindow();
            disposeBrowserWindow();
		}

        setStatus(ApplicationStatus.ENDING);        
		notifyEnd();
	}

    /**
     * Expand the contact.
     *
     * @param contactId
     *            The contact id.       
     */
    public void expandContact(final JabberId contactId) {
        getTabContactAvatar().expandContact(contactId);
    }

    /**
     * Expand the container.
     * 
     * The panel is expanded (without animation), scrolled so it is visible,
     * and the container is selected.
     *
     * @param containerId
     *            The container id.        
     */
    public void expandContainer(final Long containerId) {
        getTabContainerAvatar().expandContainer(containerId);
    }

    /**
     * Expand the container.
     * 
     * The panel is expanded (without animation), scrolled so it is visible,
     * and the container and version are selected.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     */
    public void expandContainer(final Long containerId, final Long versionId) {
        getTabContainerAvatar().expandContainer(containerId, versionId);
    }

    /**
     * Expand the help topic.
     * 
     * @param helpTopicId
     *            The help topic id.   
     */
    public void expandHelpTopic(final Long helpTopicId) {
        getTabHelpAvatar().expandHelpTopic(helpTopicId);
    }

    /**
     * Notify the browser that a document draft has been updated (ie. documents changed)
     * 
     * @param documentId
     *            A document id.
     */
    public void fireDocumentDraftUpdated(final Long documentId) {
        syncDocumentTabContainer(documentId, Boolean.FALSE);
    }

    /**
     * Notify the application that a document has in some way been updated.
     *
     * @param containerId
     *            The container id.
     * @param documentId
     *            The document that has changed.
     */
    public void fireDocumentUpdated(final Long documentId, final Boolean remote) {
        syncDocumentTabContainer(documentId, remote);
    }

    /**
     * Obtain the browser's locales.
     * 
     * @return The browser's available <code>Locale[]</code>s.
     */
    public Locale[] getAvailableLocales() {
        return getPlatform().getAvailableLocales();
    }

    /**
     * Obtain the browser's locale.
     * 
     * @return The browser's <code>Locale</code>.
     */
    public TimeZone[] getAvailableTimeZones() {
        return getPlatform().getAvailableTimeZones();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.Application#getConnection()
     */
    public Connection getConnection() {
        return isOnline() ? Connection.ONLINE : Connection.OFFLINE;
    }

    /**
     * Get the filter delegate for the selected tab avatar.
     * 
     * @param tabId
     *            A <code>MainTitleAvatar.TabId</code>.
     * @return A <code>TabAvatarFilterDelegate</code>.
     */
    public TabAvatarFilterDelegate getFilterDelegate(final MainTitleAvatar.TabId tabId) {
        return getTabAvatar(tabId).getFilterDelegate();
    }

    /**
	 * @see com.thinkparity.ophelia.browser.platform.application.Application#getId()
	 * 
	 */
	public ApplicationId getId() { return ApplicationId.BROWSER; }

    /**
     * Obtain the browser's locale.
     * 
     * @return The browser's <code>Locale</code>.
     */
    public Locale getLocale() {
        return getPlatform().getLocale();
    }

    /**
     * Obtain a logger for the class from the applilcation.
     *
     *
     * @param clasz The class for which to obtain the logger.
     * @return An apache logger.
     */
    public Logger getLogger(final Class clasz) {
        return getPlatform().getLogger(clasz);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.Application#getMainWindow()
     *
     */
    public AbstractJFrame getMainWindow() {
        return mainWindow;
    }

    /**
	 * Obtain the platform.
	 * 
	 * @return The platform the application is running on.
	 */
	public Platform getPlatform() { return super.getPlatform(); }

    /**
	 * Obtain the selected system message.
	 * 
	 * @return The selected system message id.
	 */
	public Object getSelectedSystemMessage() { return null; }

    /**
     * Get the selected tab id.
     * 
     * @return The selected tab id <code>MainTitleAvatar.TabId</code>.
     */
    public MainTitleAvatar.TabId getSelectedTabId() {
        final Data data = (Data) ((Data) getMainTitleAvatar().getInput());
        return (TabId) data.get(MainTitleAvatar.DataKey.TAB_ID);
    }

    /**
     * Obtain a session for an avatar type; and if one does not already exist
     * for the type create one.
     * 
     * @param avatarId
     *            An <code>AvatarId</code>.
     * @return A <code>BrowserSession</code>.
     */
    public BrowserSession getSession(final AvatarId avatarId) {
        return getSession(new BrowserContext(avatarId), Boolean.TRUE);
    }

    /**
     * Obtain a session for an avatar type.
     * 
     * @param avatarId
     *            An <code>AvatarId</code>.
     * @param create
     *            Whether or not to create the session if it does not already
     *            exist.
     * @return A <code>BrowserSession</code>.
     */
    public BrowserSession getSession(final AvatarId avatarId, final Boolean create) {
        return getSession(new BrowserContext(avatarId), create);
    }

    /**
     * Get the tab button action delegate for the selected tab avatar.
     * 
     * @param tabId
     *            A <code>MainTitleAvatar.TabId</code>.
     * @return A <code>TabButtonActionDelegate</code>.
     */
    public TabButtonActionDelegate getTabButtonActionDelegate(final MainTitleAvatar.TabId tabId) {
        return getTabAvatar(tabId).getTabButtonActionDelegate();
    }

    /**
     * Obtain the browser's locales.
     * 
     * @return The browser's available <code>Locale[]</code>s.
     */
    public TimeZone getTimeZone() {
        return getPlatform().getTimeZone();
    }

    /**
	 * Close the main window.
	 *
	 */
	public void hibernate() { getPlatform().hibernate(getId()); }

    /**
	 * @see com.thinkparity.ophelia.browser.platform.application.Application#hibernate()
	 * 
	 */
	public void hibernate(final Platform platform) {
		assertStatusChange(ApplicationStatus.HIBERNATING);

		disposeBrowserWindow();

        setStatus(ApplicationStatus.HIBERNATING);
		notifyHibernate();
	}

    /**
     * Iconify (or uniconify) the browser application.
     * 
     * @param iconify
     *            A <code>Boolean</code> indicating to iconify or uniconify.
     */
    public void iconify(final Boolean iconify) {
        if (iconify != isBrowserWindowIconified()) {
            int state = mainWindow.getExtendedState();
            if (iconify) {
                state |= JFrame.ICONIFIED;
            } else {
                state &= ~JFrame.ICONIFIED;
            }
            mainWindow.setExtendedState(state);
        }
    }

    /**
     * Determine if the component is an ancestor of the avatar.
     * 
     * @param id
     *            An <code>AvatarId</code>.
     * @param component
     *            A <code>Component</code>.
     */
    public Boolean isAncestorOf(final AvatarId id, final java.awt.Component component) {
        return getAvatar(id).isAncestorOf(component);
    }

    /**
     * Determine whether or not the browser is busy.
     * 
     * @return True if the browser is busy.
     */
    public Boolean isBusy() {
        return mainWindow.isBusyIndicatorApplied();
    }

    /**
     * Determine if the browser window is maximized.
     * 
     * @return true if the browser window is maximized; false otherwise.
     */
    public Boolean isBrowserWindowMaximized() {
        return ((mainWindow.getExtendedState() & JFrame.MAXIMIZED_BOTH) > 0);
    }

    /** @see com.thinkparity.ophelia.browser.platform.application.Application#isDevelopmentMode() */
    public Boolean isDevelopmentMode() { 
        return getPlatform().isDevelopmentMode();
    }

    /**
     * Determine whether or not the platform is online.
     * 
     * @return True if the platform is online.
     */
    public Boolean isOnline() {
        return getPlatform().isOnline();
    }

	/**
     * Maximize (or unmaximize) the browser application.
     * 
     * @param maximize
     *            A <code>Boolean</code> indicating to maximize or unmaximize.
     */
    public void maximize(final Boolean maximize) {
        if (maximize != isBrowserWindowMaximized()) {
            int state = mainWindow.getExtendedState();
            if (maximize) {
                state |= JFrame.MAXIMIZED_BOTH;
            } else {
                state &= ~JFrame.MAXIMIZED_BOTH;
            }
            mainWindow.setExtendedState(state);
        }
    }

	/**
     * Move and resize the browser window.
     * (See moveBrowserWindow, resizeBrowserWindow)
     *
     * @param l
     *            The new relative location of the window.
     * @param s
     *            The new browser's relative size.
     * 
     */
    public void moveAndResizeBrowserWindow(final Point l, final Dimension s) {
        final Point newL = mainWindow.getLocation();
        newL.x += l.x;
        newL.y += l.y;
        final Dimension newS = mainWindow.getSize();
        newS.width += s.width;
        newS.height += s.height;
        mainWindow.setMainWindowSizeAndLocation(newL, newS);
    }

	/**
	 * Move the browser window.
	 * 
	 * @param l
	 *            The new relative location of the window.
	 */
	public void moveBrowserWindow(final Point l) {
		final Point newL = mainWindow.getLocation();
		newL.x += l.x;
		newL.y += l.y;
		mainWindow.setLocation(newL);
	}

	/**
     * Call <code>toFront()</code> on the browser's main window.
     *
     */
    public void moveToFront() { mainWindow.toFront(); }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.Application#removeBusyIndicator()
     *
     */
    public void removeBusyIndicator() {
        mainWindow.removeBusyIndicator();
    }

    /**
     * Request focus in the search control.
     */
    public void requestFocusInSearch() {
        getMainTitleAvatar().requestFocusInSearch();
    }

    /**
     * Request focus in the current tab.
     */
    public void requestFocusInTab() {
        getTabAvatar(getSelectedTabId()).requestFocusInTab();
    }

    /**
     * Resize the browser window.
     * 
     * @param s
     *            The new browser's relative size.
     */
    public void resizeBrowserWindow(final Dimension s) {
        final Dimension newS = mainWindow.getSize();
        newS.width += s.width;
        newS.height += s.height;
        mainWindow.setMainWindowSize(newS);
    }

    /**
	 * @see com.thinkparity.ophelia.browser.platform.application.Application#restore(com.thinkparity.ophelia.browser.platform.Platform)
	 * 
	 */
	public void restore(final Platform platform) {
		assertStatusChange(ApplicationStatus.RESTORING);
        
		reOpenMainWindow();

        setStatus(ApplicationStatus.RESTORING);
        notifyRestore();

        assertStatusChange(ApplicationStatus.RUNNING);
        setStatus(ApplicationStatus.RUNNING);
	}

    /**
     * Confirm an attempt to retry an invocation of an action with the user and
     * if positive retry.
     * 
     * @param action
     *            An <code>AbstractAction</code>.
     */
    public void retry(final AbstractAction action,
            final Object... retryMessageArguments) {
        final String messageKey = "Retry." + action.getId();
        if (confirm("Retry.Confirm", "Retry.Deny", messageKey,
                retryMessageArguments)) {
            action.retryInvokeAction();
        }
    }

    /**
     * Accept an e-mail invitation.
     * 
     * @param systemMessageId
     *            The system message id.
     */
    public void runAcceptContactIncomingEMailInvitation(final Long invitationId) {
        final Data data = new Data(1);
        data.set(AcceptIncomingEMailInvitation.DataKey.INVITATION_ID, invitationId);
        invoke(ActionId.CONTACT_ACCEPT_INCOMING_EMAIL_INVITATION, data);
    }

    /**
     * Accept an e-mail invitation.
     * 
     * @param systemMessageId
     *            The system message id.
     */
    public void runAcceptContactIncomingUserInvitation(final Long invitationId) {
        final Data data = new Data(1);
        data.set(AcceptIncomingUserInvitation.DataKey.INVITATION_ID, invitationId);
        invoke(ActionId.CONTACT_ACCEPT_INCOMING_USER_INVITATION, data);
    }

    /**
     * Run the add bookmark action.
     * 
     * @param containerId
     *            The container id.
     */
    public void runAddContainerBookmark(final Long containerId) {
        final Data data = new Data(1);
        data.set(AddBookmark.DataKey.CONTAINER_ID, containerId);
        invoke(ActionId.CONTAINER_ADD_BOOKMARK, data);
    }

	/**
     * Run the create document action, browse to select the document.
     * 
     * @param containerId
     *            The container id.
     */
    public void runAddContainerDocuments(final Long containerId) {
        final ThinkParityJFileChooser jFileChooser = displayFileChooser(
                JFileChooser.FILES_ONLY, Boolean.TRUE, 
                persistence.get(Keys.Persistence.CONTAINER_ADD_DOCUMENT_CURRENT_DIRECTORY,
                        (File) null),
                "AddDocuments");
        if (JFileChooser.APPROVE_OPTION == jFileChooser.getState()) {
            persistence.set(Keys.Persistence.CONTAINER_ADD_DOCUMENT_CURRENT_DIRECTORY,
                    jFileChooser.getCurrentDirectory());
            runAddContainerDocuments(containerId, jFileChooser.getSelectedFiles());
        }
    }
    
    /**
     * Run the add document action. This adds and/or updates documents.
     * 
     * @param containerId
     *            The container id.
     * @param file
     *            The document file.
     */
    public void runAddContainerDocuments(final Long containerId, final File[] files) {
        final Data data = new Data(2);
        data.set(AddDocument.DataKey.CONTAINER_ID, containerId);
        data.set(AddDocument.DataKey.FILES, files);
        invoke(ActionId.CONTAINER_ADD_DOCUMENT, data);
    }
    
    /**
     * Run the profile's add email action.
     *
     */
    public void runAddProfileEmail(final EMail email) {
        final Data data = new Data(1);
        data.set(AddEmail.DataKey.EMAIL, email);
        invoke(ActionId.PROFILE_ADD_EMAIL, data);
    }

    /**
     * Run the contact us action.
     *
     */
    public void runContactUs() {
        getPlatform().runContactUs();
    }

    /**
     * Run the create container (package) action. The user will
     * determine the container name.
     * 
     */
    public void runCreateContainer() {
        runCreateContainer(null, null);
    }

    /**
     * Create a container (package) with one or more new documents.
     * The user will determine the container name.
     * 
     * @param files
     *          List of files that will be added later      
     */
    public void runCreateContainer(final List<File> files) {
        runCreateContainer(null, files);
    }
    
    /**
     * Create a container (package) with a specified name.
     * 
     * @param name
     *            The container name.
     */
    public void runCreateContainer(final String name) {
        runCreateContainer(name, null);
    }
    
    /**
     * Run the create container action. If name and files are both not set; a
     * dialog will be used to prompt the user.
     * 
     * @param name
     *            The container name.
     * @param files
     *            A list of files to add to the container post creation.
     */
    public void runCreateContainer(final String name, final List<File> files) {
        final Data data = new Data(2);
        if(null != name)
            data.set(Create.DataKey.NAME, name);
        if(null != files)
            data.set(Create.DataKey.FILES, files);
        invoke(ActionId.CONTAINER_CREATE, data);
    }
  
    /**
     * Create a draft for the container.
     * 
     * @param containerId
     *            The container id.
     */
    public void runCreateContainerDraft(final Long containerId) {
        final Data data = new Data(1);
        data.set(CreateDraft.DataKey.CONTAINER_ID, containerId);
        invoke(ActionId.CONTAINER_CREATE_DRAFT, data);         
    }

    /**
     * Run the create outgoing user invitation action.
     * 
     * @param userId
     *            A user id <code>Long</code>.
     */
    public void runCreateOutgoingUserInvitation(final Long userId) {
        final Data data = new Data(1);
        data.set(CreateOutgoingUserInvitation.DataKey.USER_ID, userId);
        invoke(ActionId.CONTACT_CREATE_OUTGOING_USER_INVITATION, data);
    }

    /**
     * Decline an invitation.
     * 
     * @param systemMessageId
     *            The system message id.
     */
    public void runDeclineContactIncomingEMailInvitation(final Long invitationId) {
        final Data data = new Data(1);
        data.set(DeclineIncomingEMailInvitation.DataKey.INVITATION_ID, invitationId);
        invoke(ActionId.CONTACT_DECLINE_INCOMING_EMAIL_INVITATION, data);
    }

    /**
     * Decline an invitation.
     * 
     * @param systemMessageId
     *            The system message id.
     */
    public void runDeclineContactIncomingUserInvitation(final Long invitationId) {
        final Data data = new Data(1);
        data.set(DeclineIncomingUserInvitation.DataKey.INVITATION_ID, invitationId);
        invoke(ActionId.CONTACT_DECLINE_INCOMING_USER_INVITATION, data);
    }

    /**
     * Run the delete contact action.
     * 
     * @param contactId
     *            The contact id.
     */
    public void runDeleteContact(final JabberId contactId) {
        Assert.assertNotNull("Cannot delete null contact.", contactId);
        final Data data = new Data(1);
        data.set(com.thinkparity.ophelia.browser.platform.action.contact.Delete.DataKey.CONTACT_ID, contactId);
        invoke(ActionId.CONTACT_DELETE, data);        
    }

    /**
     * Run the delete container action.
     * 
     * @param containerId
     *            The container id.
     */
    public void runDeleteContainer(final Long containerId) {
        Assert.assertNotNull("Cannot delete null container.", containerId);
        final Data data = new Data(1);
        data.set(com.thinkparity.ophelia.browser.platform.action.container.Delete.DataKey.CONTAINER_ID, containerId);
        invoke(ActionId.CONTAINER_DELETE, data);        
    }

    /**
     * Run the delete outgoing email invitation action.
     * 
     * @param invitationId
     *            The invitation id <code>Long</code>.
     */
    public void runDeleteOutgoingEMailInvitation(final Long invitationId) {
        Assert.assertNotNull("Cannot delete null email invitation.", invitationId);
        final Data data = new Data(1);
        data.set(DeleteOutgoingEMailInvitation.DataKey.INVITATION_ID, invitationId);
        invoke(ActionId.CONTACT_DELETE_OUTGOING_EMAIL_INVITATION, data);   
    }

    /**
     * Run the delete outgoing user invitation action.
     * 
     * @param invitationId
     *            The invitation id <code>Long</code>.
     */
    public void runDeleteOutgoingUserInvitation(final Long invitationId) {
        Assert.assertNotNull("Cannot delete null user invitation.", invitationId);
        final Data data = new Data(1);
        data.set(DeleteOutgoingUserInvitation.DataKey.INVITATION_ID, invitationId);
        invoke(ActionId.CONTACT_DELETE_OUTGOING_USER_INVITATION, data);   
    }

    /**
     * Run the downgrade account action.
     */
    public void runDowngradeAccount() {
        invoke(ActionId.PROFILE_DOWNGRADE_ACCOUNT, Data.emptyData());
    }

    /**
     * Run the learn more action.
     * 
     * @param topic
     *            The action <code>LearnMore.Topic</code>.
     */
    public void runLearnMore(final LearnMore.Topic topic) {
        getPlatform().runLearnMore(topic);
    }

    /**
	 * Run the open document action.
	 *
     * @param containerId
     *            A container id.
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
        Assert.assertNotNull("Cannot open null document.", documentId);
        Assert.assertNotNull("Cannot open null document version.", versionId);
		final Data data = new Data(2);
		data.set(OpenVersion.DataKey.DOCUMENT_ID, documentId);
		data.set(OpenVersion.DataKey.VERSION_ID, versionId);
		invoke(ActionId.DOCUMENT_OPEN_VERSION, data);
	}

    /**
     * Run the publish container action.
     * 
     * @param monitor
     *            A <code>ThinkParitySwingMonitor</code> for updating the
     *            dialogue for the long-running action.
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionName
     *            The version name <code>String</code> (optional).
     * @param emails
     *            A <code>List</code> of <code>EMail</code> addresses.
     * @param contacts
     *            A <code>List</code> of <code>Contact</code>s.
     * @param teamMembers
     *            A <code>List</code> of <code>TeamMember</code>s.
     */
    public void runPublishContainer(final ThinkParitySwingMonitor monitor,
            final Long containerId, final String versionName,
            final List<EMail> emails, final List<Contact> contacts,
            final List<TeamMember> teamMembers) {
        final Data data = new Data(7);
        data.set(Publish.DataKey.DISPLAY_AVATAR, Boolean.FALSE);
        data.set(Publish.DataKey.CONTAINER_ID, containerId);
        data.set(Publish.DataKey.CONTACTS, contacts);
        data.set(Publish.DataKey.EMAILS, emails);
        data.set(Publish.DataKey.MONITOR, monitor);
        data.set(Publish.DataKey.TEAM_MEMBERS, teamMembers);
        if (null != versionName) {
            data.set(Publish.DataKey.VERSION_NAME, versionName);
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                invoke(ActionId.CONTAINER_PUBLISH, data);
            }
        });
    }    

    /**
     * Run the publish container version action.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @param emails
     *            A <code>List</code> of <code>EMail</code> addresses.
     * @param contacts
     *            A <code>List</code> of <code>Contact</code>s.
     * @param teamMembers
     *            A <code>List</code> of <code>TeamMember</code>s.
     */
    public void runPublishContainerVersion(final Long containerId,
            final Long versionId, final List<EMail> emails,
            final List<Contact> contacts, final List<TeamMember> teamMembers) {
        final Data data = new Data(7);
        data.set(PublishVersion.DataKey.DISPLAY_AVATAR, Boolean.FALSE);
        data.set(PublishVersion.DataKey.CONTACTS, contacts);
        data.set(PublishVersion.DataKey.CONTAINER_ID, containerId);
        data.set(PublishVersion.DataKey.EMAILS, emails);
        data.set(PublishVersion.DataKey.TEAM_MEMBERS, teamMembers);
        data.set(PublishVersion.DataKey.VERSION_ID, versionId);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                invoke(ActionId.CONTAINER_PUBLISH_VERSION, data);
            }
        });
    }

    /**
     * Run the open contact action.
     * 
     * @param contactId
     *            The contact id.
     */
    public void runReadContact(final JabberId contactId) {
        Assert.assertNotNull("Cannot open null contact.", contactId);
        final Data data = new Data(1);
        data.set(Read.DataKey.CONTACT_ID, contactId);
        invoke(ActionId.CONTACT_READ, data);
    }
    
    /**
     * Run the read container version action.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     */
    public void runReadContainerVersion(final Long containerId,
            final Long versionId) {
        final Data data = new Data(2);
        data.set(ReadVersion.DataKey.CONTAINER_ID, containerId);
        data.set(ReadVersion.DataKey.VERSION_ID, versionId);
        invoke(ActionId.CONTAINER_READ_VERSION, data);
    }
    
    /**
     * Run the remove bookmark action.
     * 
     * @param containerId
     *            The container id.
     */
    public void runRemoveContainerBookmark(final Long containerId) {
        final Data data = new Data(1);
        data.set(RemoveBookmark.DataKey.CONTAINER_ID, containerId);
        invoke(ActionId.CONTAINER_REMOVE_BOOKMARK, data);
    }

    /**
     * Run the remove flag seen action.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     */
    public void runRemoveContainerFlagSeen(final Long containerId,
            final Long versionId) {
        final Data data = new Data(2);
        data.set(RemoveFlagSeen.DataKey.CONTAINER_ID, containerId);
        data.set(RemoveFlagSeen.DataKey.VERSION_ID, versionId);
        invoke(ActionId.CONTAINER_REMOVE_FLAG_SEEN, data);         
    }

    /**
     * Run the profile's remove email action.
     *
     */
    public void runRemoveProfileEmail(final Long emailId) {
        final Data data = new Data(1);
        data.set(RemoveEmail.DataKey.EMAIL_ID, emailId);
        invoke(ActionId.PROFILE_REMOVE_EMAIL, data);
    }

    /**
     * Run the container rename action.
     * 
     * @param containerId
     *            A container id.
     * @param containerName
     *            A container name.
     */
    public void runRenameContainer(final Long containerId,
            final String containerName) {
        final Data data = new Data(2);
        data.set(Rename.DataKey.CONTAINER_ID, containerId);
        data.set(Rename.DataKey.CONTAINER_NAME, containerName);
        invoke(ActionId.CONTAINER_RENAME, data);
    }

    /**
     * Run the document rename action.
     * 
     * @param containerId
     *            A container id.
     * @param documentId
     *            A document id.
     * @param documentName
     *            An document name.
     */
    public void runRenameDocument(final Long containerId,
            final Long documentId, final String documentName) {
        final Data data = new Data(3);
        data.set(RenameDocument.DataKey.CONTAINER_ID, containerId);
        data.set(RenameDocument.DataKey.DOCUMENT_ID, documentId);
        data.set(RenameDocument.DataKey.DOCUMENT_NAME, documentName);
        invoke(ActionId.CONTAINER_RENAME_DOCUMENT, data);
    }

    /**
     * Update the draft comment.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param comment
     *            A comment <code>String</code>.
     */
    public void runUpdateDraftComment(final Long containerId, final String comment) {
        final Data data = new Data(3);
        data.set(UpdateDraftComment.DataKey.CONTAINER_ID, containerId);
        if (null != comment) {
            data.set(UpdateDraftComment.DataKey.COMMENT, comment);
        }
        data.set(UpdateDraftComment.DataKey.DISPLAY_AVATAR, Boolean.FALSE);
        invoke(ActionId.CONTAINER_UPDATE_DRAFT_COMMENT, data);
    }

    /**
     * Update the user's profile.
     *
     * @param name
     *            The user's name <code>String</code>.
     * @param address
     *            The user's address <code>String</code>. 
     * @param city
     *            The user's city <code>String</code>.   
     * @param country
     *            The user's country <code>String</code>.       
     * @param mobilePhone
     *            The user's mobile phone <code>String</code>.
     * @param organization
     *            The user's organization <code>String</code>.
     * @param phone
     *            The user's phone <code>String</code>.
     * @param postalCode
     *            The user's postal code <code>String</code>.
     * @param province
     *            The user's province <code>String</code>.
     * @param title
     *            The user's title <code>String</code>.
     */
    public void runUpdateProfile(final String name,
            final String address, final String city,
            final String country, final String mobilePhone,
            final String organization, final String phone,
            final String postalCode, final String province,
            final String title) {
        final Data data = new Data(11);
        data.set(Update.DataKey.DISPLAY_AVATAR, Boolean.FALSE);
        data.set(Update.DataKey.NAME, name);
        if (null != address)
            data.set(Update.DataKey.ADDRESS, address);
        if (null != city)
            data.set(Update.DataKey.CITY, city);
        if (null != country)
            data.set(Update.DataKey.COUNTRY, country);
        if (null != mobilePhone)
            data.set(Update.DataKey.MOBILE_PHONE, mobilePhone);
        if (null != organization)
            data.set(Update.DataKey.ORGANIZATION, organization);
        if (null != phone)
            data.set(Update.DataKey.PHONE, phone);
        if (null != postalCode)
            data.set(Update.DataKey.POSTAL_CODE, postalCode);
        if (null != province)
            data.set(Update.DataKey.PROVINCE, province);
        if (null != title)
            data.set(Update.DataKey.TITLE, title);
        invoke(ActionId.PROFILE_UPDATE, data);
    }

    /**
     * Update the user's profile.
     * 
     * @param credentials
     *            The user's <code>Credentials</code>.
     * @param newPassword
     *            The user's updated password <code>String</code>.
     * @param confirmNewPassword
     *            The user's updated password again <code>String</code>.
     */
    public void runUpdateProfilePassword(final Credentials credentials,
            final String newPassword, final String confirmNewPassword) {     
        final Data data = new Data(4); 
        data.set(UpdatePassword.DataKey.DISPLAY_AVATAR, Boolean.FALSE);
        data.set(UpdatePassword.DataKey.CREDENTIALS, credentials);
        data.set(UpdatePassword.DataKey.NEW_PASSWORD, newPassword);
        data.set(UpdatePassword.DataKey.NEW_PASSWORD_CONFIRM, confirmNewPassword);
        invoke(ActionId.PROFILE_UPDATE_PASSWORD, data);
    }

    /**
     * Run the upgrade account action.
     */
    public void runUpgradeAccount() {
        // NOCOMMIT Remove the "if" test
        if (isDevelopmentMode()) {
            final Data data = new Data(1);
            data.set(UpgradeAccount.DataKey.DISPLAY_AVATAR, Boolean.TRUE);
            invoke(ActionId.PROFILE_UPGRADE_ACCOUNT, data);
        }
    }

    /**
     * Run the verify email action.
     * 
     * @param emailId
     *            An email id <code>Long</code>.
     * @param key
     *            An email verification key <code>String</code>.
     */
    public void runVerifyEmail(final Long emailId, final String key) {
        final Data data = new Data(3);
        if (null == emailId || null == key) {
            data.set(VerifyEmail.DataKey.DISPLAY_AVATAR, Boolean.TRUE);
        } else {
            data.set(VerifyEmail.DataKey.DISPLAY_AVATAR, Boolean.FALSE);
            data.set(VerifyEmail.DataKey.EMAIL_ID, emailId);
            data.set(VerifyEmail.DataKey.KEY, key);
        }
        invoke(ActionId.PROFILE_VERIFY_EMAIL, data);
    }

    /**
     * Select a tab. This displays the tab and also causes the tab buttons to
     * update.
     * 
     * @param tabId
     *            A <code>TabId</code>.
     */
    public void selectTab(final MainTitleAvatar.TabId tabId) {
        final Data data = (Data) ((Data) getMainTitleAvatar().getInput()).clone();
        data.set(MainTitleAvatar.DataKey.TAB_ID, tabId);
        getMainTitleAvatar().setInput(data); 
    }

    /**
     * Show all tab panels, ie. clear filter and search on the tab.
     */
    public void showAllTabPanels() {
        getMainTitleAvatar().showAllTabPanels();
    }

    /**
     * Show the contact invitation.
     * 
     * @param invitationId
     *            The invitationId.
     */
    public void showContactIncomingEMailInvitation(
            final IncomingEMailInvitation incomingEMailInvitation) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getTabContactAvatar().showContactInvitation(
                        incomingEMailInvitation.getId());
            }
        });   
    }

    /**
     * Show the contact invitation.
     * 
     * @param invitationId
     *            The invitationId.
     */
    public void showContactIncomingUserInvitation(
            final IncomingUserInvitation incomingUserInvitation) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getTabContactAvatar().showContactInvitation(
                        incomingUserInvitation.getId());
            }
        });   
    }

    /**
     * Show the contact invitation.
     * 
     * @param invitationIds
     *            The list of invitationIds.
     * @param index
     *            The index of the invitation to show (0 indicates the invitation displayed at top).   
     */
    public void showContactInvitation(final List<Long> invitationIds, final int index) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getTabContactAvatar().showContactInvitation(invitationIds, index);
            }
        });   
    }

    /**
     * Show the contact invitation.
     * 
     * @param invitationIds
     *            The list of invitationIds.
     * @param index
     *            The index of the invitation to show (0 indicates the invitation displayed at top).   
     */
    public void showContactInvitation(final Long invitationId) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getTabContactAvatar().showContactInvitation(invitationId);
            }
        });   
    }

    /**
     * Show the topmost visible unread container version.
     * 
     * The panel is expanded (without animation), scrolled so it is visible,
     * and the container and version are selected.
     */
    public void showTopVisibleUnreadContainerVersion() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getTabContainerAvatar().showTopVisibleUnreadContainerVersion();
            }
        }); 
    }

    /**
	 * @see com.thinkparity.ophelia.browser.platform.application.Application#start()
	 * 
	 */
    public void start(final Platform platform) {
		logApiId();

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
     * Synchronize a contact on the contact tab.
     * 
     * @param contactId
     *            A contact id <code>JabberId</code>.
     * @param remote
     *            True if the synchronization is the result of a remote event.
     */
    public void syncContactTabContact(final JabberId contactId,
            final Boolean remote) {
        getTabContactAvatar().syncContact(contactId, remote);
    }

    /**
     * Synchronize an incoming e-mail invitation on the contact tab.
     * 
     * @param invitationId
     *            An invitation id <code>Long</code>.
     * @param remote
     *            True if the synchronization is the result of a remote event.
     */
    public void syncContactTabIncomingEMailInvitation(final Long invitationId,
            final Boolean remote) {
        getTabContactAvatar().syncIncomingEMailInvitation(invitationId, remote);
    }

	/**
     * Synchronize an incoming user invitation on the contact tab.
     * 
     * @param invitationId
     *            An invitation id <code>Long</code>.
     * @param remote
     *            True if the synchronization is the result of a remote event.
     */
    public void syncContactTabIncomingUserInvitation(final Long invitationId,
            final Boolean remote) {
        getTabContactAvatar().syncIncomingUserInvitation(invitationId, remote);
    }
        
    /**
     * Synchronize an outgoing e-mail invitation on the contact tab.
     * 
     * @param invitationId
     *            An invitation id <code>Long</code>.
     * @param remote
     *            True if the synchronization is the result of a remote event.
     */
    public void syncContactTabOutgoingEMailInvitation(final Long invitationId,
            final Boolean remote) {
        getTabContactAvatar().syncOutgoingEMailInvitation(invitationId, remote);
    }

    /**
     * Synchronize an outgoing user invitation on the contact tab.
     * 
     * @param invitationId
     *            An invitation id <code>Long</code>.
     * @param remote
     *            True if the synchronization is the result of a remote event.
     */
    public void syncContactTabOutgoingUserInvitation(final Long invitationId,
            final Boolean remote) {
        getTabContactAvatar().syncOutgoingUserInvitation(invitationId, remote);
    }

    public void toggleStatusImage() {
        ((com.thinkparity.ophelia.browser.application.browser.display.StatusDisplay) mainWindow.getDisplay(DisplayId.STATUS)).toggleImage();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.AbstractApplication#getAvatar(com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId)
     */
    @Override
    protected Avatar getAvatar(final AvatarId id) {
        return super.getAvatar(id);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.AbstractApplication#getAvatar(com.thinkparity.ophelia.browser.platform.plugin.extension.TabExtension)
     */
    @Override
    protected Avatar getAvatar(final TabListExtension tabListExtension) {
        return super.getAvatar(tabListExtension);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.AbstractApplication#getAvatar(com.thinkparity.ophelia.browser.platform.plugin.extension.TabExtension)
     */
    @Override
    protected Avatar getAvatar(final TabPanelExtension tabPanelExtension) {
        return super.getAvatar(tabPanelExtension);
    }
    
    /**
     * Display the status avatar.
     *
     */
    void displayMainStatusAvatar() {
        displayStatus(AvatarId.MAIN_STATUS);
    }

    /**
     * Display the title avatar.
     *
     */
	void displayMainTitleAvatar() {
        final Data input = new Data(2);
        input.set(MainTitleAvatar.DataKey.PROFILE, getProfile());
        input.set(MainTitleAvatar.DataKey.TAB_ID, MainTitleAvatar.TabId.CONTAINER);
        setInput(AvatarId.MAIN_TITLE, input);
        displayTitle(AvatarId.MAIN_TITLE);
	}

    /**
	 * Obtain the input for an avatar.
	 * 
	 * @param avatarId
	 *            The avatar id.
	 * @return The avatar input.
	 */
	Object getAvatarInput(final AvatarId id) {
		return avatarInputMap.get(id);
	}

    /**
     * Open a confirmation dialogue.
     * 
     * @param input
     *            The dialogue's input.
     * @return True if the user confirmed.
     */
    private Boolean confirm(final Data input) {
        open(AvatarId.DIALOG_CONFIRM, input);
        return getConfirmAvatar().didConfirm();
    }

    /**
	 * Display an avatar.
	 * 
	 * @param displayId
	 *            The display to use.
	 * @param avatarId
	 *            The avatar to display.
	 */
	private void displayAvatar(final AvatarId avatarId) {
		Assert.assertNotNull("Cannot display a null avatar.", avatarId);
		final Window window = WindowFactory.create(mainWindow);

		final Avatar avatar = getAvatar(avatarId);

		final Object input = getAvatarInput(avatarId);
		if (null == input) {
            logger.logInfo("Avatar {0}'s input is null.", avatarId);
		} else {
            avatar.setInput(getAvatarInput(avatarId));
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
                window.open(avatar);
			}
		});
	}

    /**
     * Open a file chooser dialogue.
     * 
     * @param input
     *            The dialogue's input.
     * @return A <code>ThinkParityJFileChooser</code>.
     */
    private ThinkParityJFileChooser displayFileChooser(final Data input) {
        open(AvatarId.DIALOG_FILE_CHOOSER, input);
        return getFileChooserAvatar().getFileChooser();
    }
    
    /**
     * Display an avatar on the status display.
     * 
     * @param id
     *            An avatar id.
     */
    private void displayStatus(final AvatarId id) {
        displayHelper.displayStatus(id);
    }

    /**
     * Display a tab.
     * 
     * @param id
     *            An avatar id.
     */
    private void displayTab(final AvatarId id) {
        displayHelper.displayTab(id);
    }

    /**
     * Display a tab.
     * 
     * @param tabListExtension
     *            A tab list extension.
     */
    private void displayTab(final TabListExtension tabListExtension) {
        displayHelper.displayTab(tabListExtension);
    }

    /**
     * Display a tab.
     * 
     * @param tabExtension
     *            A tab panel extension.
     */
    private void displayTab(final TabPanelExtension tabPanelExtension) {
        displayHelper.displayTab(tabPanelExtension);
    }

    /**
     * Display an avatar on the title display.
     * 
     * @param id
     *            An avatar id.
     */
    private void displayTitle(final AvatarId id) {
        displayHelper.displayTitle(id);
    }

    /** Dispose the main window. */
    private void disposeBrowserWindow() {
        Assert.assertNotNull(mainWindow, "Main window is null.");
        mainWindow.dispose();
    }

    /**
	 * Obtain the action from the controller's cache. If the action does not
	 * exist in the cache it is created and stored.
	 * 
	 * @param id
	 *            The action id.
	 * @return The action.
	 * 
	 * @see ActionId
	 */
	private ActionInvocation getAction(final ActionId id) {
        if (actionRegistry.contains(id)) {
            return actionRegistry.get(id);
        } else {
            return ActionFactory.create(id);
        }
	}

	/**
     * Obtain the confirmation avatar.
     * @return The confirmation avatar.
     */
    private ConfirmAvatar getConfirmAvatar() {
        return (ConfirmAvatar) getAvatar(AvatarId.DIALOG_CONFIRM);
    }

    /**
     * Obtain the file chooser avatar.
     * 
     * @return The file chooser avatar.
     */
    private FileChooserAvatar getFileChooserAvatar() {
        return (FileChooserAvatar) getAvatar(AvatarId.DIALOG_FILE_CHOOSER);
    }

    /**
     * Obtain the main title avatar.
     * 
     * @return The main title avatar.
     */
    private MainTitleAvatar getMainTitleAvatar() {
        return (MainTitleAvatar) getAvatar(AvatarId.MAIN_TITLE);
    }

    /**
     * Obtain the tab input from the main title avatar.
     * 
     * @return A tab.
     */
    private MainTitleAvatar.TabId getMainTitleAvatarTab() {
        return (MainTitleAvatar.TabId) ((Data) getMainTitleAvatar().getInput()).get(MainTitleAvatar.DataKey.TAB_ID);
    }

    /**
     * Obtain a session for a context.
     * 
     * @param context
     *            A <code>BrowserContext</code>.
     * @param create
     *            Whether or not to create the session if it does not already
     *            exist.
     * @return A <code>BrowserSession</code>.
     */
    private BrowserSession getSession(final BrowserContext context,
            final Boolean create) {
        return sessionImpl.getSession(context, create);
    }

    /**
     * Obtain a tab avatar.
     * 
     * @param tabId
     *            A <code>MainTitleAvatar.TabId</code>.
     * @return A <code>TabPanelAvatar</code>.
     */
    private TabPanelAvatar getTabAvatar(final MainTitleAvatar.TabId tabId) {
        switch(tabId) {
        case CONTACT:
            return getTabContactAvatar();
        case CONTAINER:
            return getTabContainerAvatar();
        case HELP:
            return getTabHelpAvatar();
        default:
            Assert.assertUnreachable("Unknown main title tab id.");
        }
        return null;
    }

    /**
     * Obtain the contact tab avatar.
     * 
     * @return The contact tab avatar.
     */
    private ContactTabAvatar getTabContactAvatar() {
        return (ContactTabAvatar) getAvatar(AvatarId.TAB_CONTACT);
    }

    /**
     * Obtain the container tab avatar.
     * 
     * @return A <code>ContainerAvatar</code>.
     */
    private ContainerTabAvatar getTabContainerAvatar() {
        return (ContainerTabAvatar) getAvatar(AvatarId.TAB_CONTAINER);
    }

    /**
     * Obtain the help tab avatar.
     * 
     * @return The help tab avatar.
     */
    private HelpTabAvatar getTabHelpAvatar() {
        return (HelpTabAvatar) getAvatar(AvatarId.TAB_HELP);
    }

    private void invoke(final ActionId actionId, final Data data) {
	    getAction(actionId).invokeAction(this, data);
	}

	private Boolean isBrowserWindowIconified() {
        return ((mainWindow.getExtendedState() & JFrame.ICONIFIED) > 0);
	}

    private Boolean isBrowserWindowOpen() {
		return null != mainWindow && mainWindow.isVisible();
	}

    /**
     * Open a window.
     * 
     * @param avatarId
     *            An <code>AvatarId</code>.
     * @param input
     *            Input <code>Data</code>.
     */
	private void open(final AvatarId avatarId, final Data input) {
        final Window window = WindowFactory.create(mainWindow);
        final Avatar avatar = getAvatar(avatarId);
        avatar.setInput(input);
        window.open(avatar);
    }

    /**
	 * Open the main browser window.
	 *
	 */
	private void openMainWindow() {
		mainWindow = new BrowserWindow(this);
		displayHelper.setBrowserWindow(mainWindow);
        keyboardHelper.bindKeys();
		mainWindow.open();
	}

	private void reOpenMainWindow() {
        mainWindow = new BrowserWindow(this);
        displayHelper.setBrowserWindow(mainWindow);
        keyboardHelper.bindKeys();
        mainWindow.reOpen();
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
			logger.logWarning("Avatar {0} does not exist.", avatarId);
			avatarInputMap.put(avatarId, input);
		}
		else {
			avatarInputMap.remove(avatarId);
			avatar.setInput(input);
		}
	}

    /**
     * Synchronize a container within the container tab.
     * 
     * @param containerId
     *            The container id.
     * @param remote
     *            The remote event indicator.
     * @param select
     *            The selection indicator.
     */
    private void syncDocumentTabContainer(final Long documentId,
            final Boolean remote) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getTabContainerAvatar().syncDocument(documentId, remote);
            }
        });        
    }
}

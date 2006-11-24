/*
 * Created On: Jan 18, 2006
 */
package com.thinkparity.ophelia.browser.application.browser;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.swing.JFileChooserUtil;

import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.contact.Contact;

import com.thinkparity.ophelia.model.artifact.ArtifactModel;
import com.thinkparity.ophelia.model.user.TeamMember;

import com.thinkparity.ophelia.browser.Constants.Keys;
import com.thinkparity.ophelia.browser.application.AbstractApplication;
import com.thinkparity.ophelia.browser.application.browser.display.DisplayId;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.MainStatusAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.MainTitleAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.ErrorAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.contact.ReadContactAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container.ContainerVersionCommentAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container.CreateContainerAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container.ExportAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container.PublishContainerAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container.RenameDocumentAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile.VerifyEMailAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabListAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.contact.ContactAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container.ContainerAvatar;
import com.thinkparity.ophelia.browser.application.browser.window.WindowFactory;
import com.thinkparity.ophelia.browser.application.browser.window.WindowId;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionFactory;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.ActionRegistry;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor;
import com.thinkparity.ophelia.browser.platform.action.artifact.ApplyFlagSeen;
import com.thinkparity.ophelia.browser.platform.action.contact.AcceptIncomingInvitation;
import com.thinkparity.ophelia.browser.platform.action.contact.CreateIncomingInvitation;
import com.thinkparity.ophelia.browser.platform.action.contact.DeclineIncomingInvitation;
import com.thinkparity.ophelia.browser.platform.action.contact.Delete;
import com.thinkparity.ophelia.browser.platform.action.contact.Read;
import com.thinkparity.ophelia.browser.platform.action.container.AddBookmark;
import com.thinkparity.ophelia.browser.platform.action.container.AddDocument;
import com.thinkparity.ophelia.browser.platform.action.container.Create;
import com.thinkparity.ophelia.browser.platform.action.container.CreateDraft;
import com.thinkparity.ophelia.browser.platform.action.container.Export;
import com.thinkparity.ophelia.browser.platform.action.container.ExportVersion;
import com.thinkparity.ophelia.browser.platform.action.container.Publish;
import com.thinkparity.ophelia.browser.platform.action.container.PublishVersion;
import com.thinkparity.ophelia.browser.platform.action.container.RemoveBookmark;
import com.thinkparity.ophelia.browser.platform.action.container.RenameDocument;
import com.thinkparity.ophelia.browser.platform.action.document.Open;
import com.thinkparity.ophelia.browser.platform.action.document.OpenVersion;
import com.thinkparity.ophelia.browser.platform.action.document.UpdateDraft;
import com.thinkparity.ophelia.browser.platform.action.profile.AddEmail;
import com.thinkparity.ophelia.browser.platform.action.profile.RemoveEmail;
import com.thinkparity.ophelia.browser.platform.action.profile.ResetPassword;
import com.thinkparity.ophelia.browser.platform.action.profile.Update;
import com.thinkparity.ophelia.browser.platform.action.profile.VerifyEmail;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationStatus;
import com.thinkparity.ophelia.browser.platform.application.L18nContext;
import com.thinkparity.ophelia.browser.platform.application.dialog.ConfirmDialog;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.application.window.Window;
import com.thinkparity.ophelia.browser.platform.plugin.extension.TabListExtension;
import com.thinkparity.ophelia.browser.platform.plugin.extension.TabPanelExtension;
import com.thinkparity.ophelia.browser.platform.util.State;
import com.thinkparity.ophelia.browser.platform.util.persistence.Persistence;
import com.thinkparity.ophelia.browser.platform.util.persistence.PersistenceFactory;

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

	/** The browser's connection. */
    private Connection connection;

	/** The browser controller's display helper. */
    private final BrowserDisplayHelper displayHelper;

    /** The browser's event dispatcher. */
	private EventDispatcher ed;
	
	/**
	 * The file chooser.
	 * 
	 * @see #getJFileChooser()
	 */
	private JFileChooser jFileChooser;

	/** The thinkParity browser application window. */
	private BrowserWindow mainWindow;

	/** A persistence for browser settings. */
    private final Persistence persistence;
    
    /** Contains the browser's session information. */
	private final BrowserSession session;

	/**
	 * Create Browser.
	 * 
	 */
	public Browser(final Platform platform) {
		super(platform, L18nContext.BROWSER2);
		this.actionRegistry = new ActionRegistry();
		this.avatarInputMap = new Hashtable<AvatarId, Object>(AvatarId.values().length, 1.0F);
        this.displayHelper = new BrowserDisplayHelper(this);
        this.persistence = PersistenceFactory.getPersistence(getClass());
		this.session= new BrowserSession(this);
	}

    /**
     * Apply a search expression to a given tab.
     * 
     * @param expression
     *            The search expression <code>String</code>.
     */
	public void applySearch(final String expression) {
	    final Data data = new Data(1);
	    if (null != expression)
	        data.set(TabListAvatar.DataKey.SEARCH_EXPRESSION, expression);
        switch(getMainTitleAvatarTab()) {
        case CONTACT:
            setInput(AvatarId.TAB_CONTACT, data);
            break;
        case CONTAINER:
            setInput(AvatarId.TAB_CONTAINER, data);
            break;
        default:
            Assert.assertUnreachable("UNKNOWN TAB");
        }
	}

    /** Close the main window. */
    public void closeBrowserWindow() {
        Assert.assertNotNull(mainWindow, "Main window is null.");
        mainWindow.dispatchEvent(new WindowEvent(mainWindow, WindowEvent.WINDOW_CLOSING));
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
        input.set(ConfirmDialog.DataKey.MESSAGE_KEY, messageKey);
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
        input.set(ConfirmDialog.DataKey.MESSAGE_KEY, messageKey);
        input.set(ConfirmDialog.DataKey.MESSAGE_ARGUMENTS, messageArguments);
        return confirm(input);
    }
    
    /**
     * Display the invite dialogue.
     *
     */   
    public void displayContactCreateInvitation() {
        displayAvatar(WindowId.POPUP, AvatarId.DIALOG_CONTACT_CREATE_OUTGOING_INVITATION);
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
        displayAvatar(WindowId.POPUP, AvatarId.DIALOG_CONTAINER_VERSION_COMMENT);
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
        displayAvatar(WindowId.POPUP, AvatarId.DIALOG_CONTAINER_CREATE);
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
        displayAvatar(WindowId.POPUP, AvatarId.DIALOG_CONTAINER_CREATE);
    }
    
    /**
     * Handle a user error (show an error dialog).
     * 
     * @param messageKey
     *            The error message localization key.
     */
    public void displayErrorDialog(final String errorMessageKey) {
        displayErrorDialog(errorMessageKey, null, null);
    }

    /**
     * Display an error dialog.
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
     * @param error
     *            An error (optional).
     * @param errorMessageKey
     *            An error message key.
     * @param errorMessageArguments
     *            Error message arguments (optional).
     */
    public void displayErrorDialog(final String errorMessageKey,
            final Object[] errorMessageArguments, final Throwable error) {
        final Data input = new Data(3);
        if (null != errorMessageKey)
            input.set(ErrorAvatar.DataKey.ERROR_MESSAGE_KEY, errorMessageKey);
        if(null != errorMessageArguments)
            input.set(ErrorAvatar.DataKey.ERROR_MESSAGE_ARGUMENTS, errorMessageArguments);
        if(null != error)
            input.set(ErrorAvatar.DataKey.ERROR, error);
        open(WindowId.ERROR, AvatarId.DIALOG_ERROR, input);
    }

    /**
     * Display an error dialog.
     * 
     * @param errorMessageKey
     *            An error message localization key.
     * @param error
     *            An error.
     */
    public void displayErrorDialog(final String errorMessageKey,
            final Throwable error) {
        displayErrorDialog(errorMessageKey, null, error);
    }
    
    /**
     * Display an export dialog for a container, ie. export all versions.
     * 
     * @param containerId
     *            The container id.
     */
    public void displayExportDialog(final Long containerId) {
        final Data input = new Data(2);
        input.set(ExportAvatar.DataKey.EXPORT_TYPE, ExportAvatar.ExportType.CONTAINER);
        input.set(ExportAvatar.DataKey.CONTAINER_ID, containerId);
        setInput(AvatarId.DIALOG_CONTAINER_EXPORT, input);
        displayAvatar(WindowId.POPUP, AvatarId.DIALOG_CONTAINER_EXPORT);
    } 
    
    /**
     * Display an export dialog for a version.
     * 
     * @param containerId
     *            The container id.
     * @param versionId
     *            A version id.           
     */
    public void displayExportDialog(final Long containerId, final Long versionId) {
        final Data input = new Data(3);
        input.set(ExportAvatar.DataKey.EXPORT_TYPE, ExportAvatar.ExportType.VERSION);
        input.set(ExportAvatar.DataKey.CONTAINER_ID, containerId);
        input.set(ExportAvatar.DataKey.VERSION_ID, versionId);
        setInput(AvatarId.DIALOG_CONTAINER_EXPORT, input);
        displayAvatar(WindowId.POPUP, AvatarId.DIALOG_CONTAINER_EXPORT);
    }
    
    /**
     * Display the info (or Help About) dialog.
     */
    public void displayInfoDialog() {
        displayAvatar(WindowId.POPUP, AvatarId.DIALOG_PLATFORM_DISPLAY_INFO);
    }

    /**
     * Display the "publish container" dialog.
     * If the user presses OK, the CONTAINER_PUBLISH action is invoked.
     * 
     * @param containerId
     *            The container id.
     */
    public void displayPublishContainerDialog(final Long containerId) {
        final Data input = new Data(2);
        input.set(PublishContainerAvatar.DataKey.PUBLISH_TYPE, PublishContainerAvatar.PublishType.PUBLISH);
        input.set(PublishContainerAvatar.DataKey.CONTAINER_ID, containerId);
        setInput(AvatarId.DIALOG_CONTAINER_PUBLISH, input);
        displayAvatar(WindowId.POPUP, AvatarId.DIALOG_CONTAINER_PUBLISH);
    }
    
    /**
     * Display the "publish container" dialog for a version.
     * If the user presses OK, the CONTAINER_PUBLISH action is invoked.
     * 
     * @param containerId
     *            The container id.
     * @param versionId
     *            A version id.        
     */
    public void displayPublishContainerDialog(final Long containerId, final Long versionId) {
        final Data input = new Data(3);
        input.set(PublishContainerAvatar.DataKey.PUBLISH_TYPE, PublishContainerAvatar.PublishType.PUBLISH_VERSION);
        input.set(PublishContainerAvatar.DataKey.CONTAINER_ID, containerId);
        input.set(PublishContainerAvatar.DataKey.VERSION_ID, versionId);
        setInput(AvatarId.DIALOG_CONTAINER_PUBLISH, input);
        displayAvatar(WindowId.POPUP, AvatarId.DIALOG_CONTAINER_PUBLISH);
    }
    
    /**
     * Display the contact info dialogue.
     *
     * @param contactId
     *            A contact id.
     */
    public void displayReadContactDialog(JabberId contactId) {
        final Data input = new Data(1);
        input.set(ReadContactAvatar.DataKey.CONTACT_ID, contactId);
        setInput(AvatarId.DIALOG_CONTACT_READ, input);
        displayAvatar(WindowId.POPUP, AvatarId.DIALOG_CONTACT_READ);        
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
        displayAvatar(WindowId.POPUP, AvatarId.DIALOG_CONTAINER_RENAME_DOCUMENT);
    }

    /**
     * Display the update profile dialog.
     *
     */
    public void displayResetProfilePasswordDialog() {
        displayAvatar(WindowId.POPUP, AvatarId.DIALOG_PROFILE_RESET_PASSWORD);
    }
    
    /** Display the contact avatar tab. */
    public void displayTabContactAvatar() {
        displayTab(AvatarId.TAB_CONTACT);
    }

    /** Display the container avatar tab. */
    public void displayTabContainerAvatar() {
        displayTab(AvatarId.TAB_CONTAINER);
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
     * Display the reset profile password dialog.
     *
     */
    public void displayUpdateProfileDialog() {
        displayAvatar(WindowId.POPUP, AvatarId.DIALOG_PROFILE_UPDATE);
    }

    /**
     * Display the verify profile email dialog.
     * 
     * @param emailId
     *            An email id <code>Long</code>.
     */
    public void displayVerifyProfileEmailDialog(final Long emailId) {
        final Data input = new Data(1);
        input.set(VerifyEMailAvatar.DataKey.EMAIL_ID, emailId);
        setInput(AvatarId.DIALOG_PROFILE_VERIFY_EMAIL, input);
        displayAvatar(WindowId.POPUP, AvatarId.DIALOG_PROFILE_VERIFY_EMAIL);        
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
     * Notify the application that a contact has been added.
     * 
     * @param contactId
     *            The contact id.
     * @param remote
     *            True if the action was the result of a remote event; false if
     *            the action was a local event.  
     */
    public void fireContactAdded(final JabberId contactId, final Boolean remote) {
        setStatus("ContactAdded");
        // refresh the contact list
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { getTabContactAvatar().syncContact(contactId, remote); }
        });
    }

    /**
     * 
     * Notify the application that a contact has been deleted.
     * 
     * @param contactId
     *           The contact id.
     */
    public void fireContactDeleted(final JabberId contactId) {
        setStatus("ContactDeleted");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getTabContactAvatar().syncContact(contactId, Boolean.FALSE);
            }
        });
    }

    /**
     * Notify the application a container confirmation has been received.
     *
     * @param containerId
     *            The container id.
     */
    public void fireContainerConfirmationReceived(final Long containerId) {
        syncContainerTabContainer(containerId, Boolean.FALSE);
    }

    /**
     * Notify the application that a container has been created.
     * 
     * @param containerId
     *            The container id.  
     * @param remote
     *            True if the action was the result of a remote event; false if
     *            the action was a local event.       
     */
    public void fireContainerCreated(final Long containerId, final Boolean remote) {
        setStatus("ContainerCreated");
        syncContainerTabContainer(containerId, remote);
    }

    /**
     * Notify the application that a container has been deleted.
     * 
     * @param containerId
     *            The container id.  
     * @param remote
     *            True if the action was the result of a remote event; false if
     *            the action was a local event.       
     */
    public void fireContainerDeleted(final Long containerId, final Boolean remote) {
        setStatus("ContainerDeleted");
        syncContainerTabContainer(containerId, remote);
    }

    /**
     * Notify the application that a document has been added.
     * 
     * @param documentId
     *            The document id.
     * @param remote
     *            True if the action was the result of a remote event; false if
     *            the action was a local event.
     */
    public void fireContainerDocumentAdded(final Long containerId,
            final Long documentId, final Boolean remote) {
        setStatus("DocumentCreated");
        syncContainerTabContainer(containerId, remote);
    }

    /**
     * Notify the application that a document has been deleted.
     * 
     * @param documentId
     *            The document id.
     * @param remote
     *            True if the action was the result of a remote event; false if
     *            the action was a local event.
     */
    public void fireContainerDocumentRemoved(final Long containerId,
            final Long documentId, final Boolean remote) {
        setStatus("DocumentDeleted");
        syncContainerTabContainer(containerId, remote);
    }
    
    /**
     * Notify the application that the draft has been added.
     * 
     * @param containerId
     *            The container id.
     * @param remote
     *            True if the action was the result of a remote event; false if
     *            the action was a local event.   
     */
    public void fireContainerDraftCreated(final Long containerId,
            final Boolean remote) {
        setStatus("ContainerDraftCreated");
        syncContainerTabContainer(containerId, remote);
    }

    /**
     * Notify the application that the draft has been deleted.
     * 
     * @param containerId
     *            The container id.
     * @param remote
     *            True if the action was the result of a remote event; false if
     *            the action was a local event.   
     */
    public void fireContainerDraftDeleted(final Long containerId, final Boolean remote) {
        setStatus("ContainerDraftDeleted");
        syncContainerTabContainer(containerId, remote);
    }

    /**
     * Notify the application that a container has been received (ie. package published)
     *
     * @param containerId
     *            The container id.
     */
    public void fireContainerReceived(final Long containerId) {
        setStatus("ContainerReceived");

        // flag it as not having been seen
        final ArtifactModel aModel = getArtifactModel();
        aModel.removeFlagSeen(containerId);

        syncContainerTabContainer(containerId, Boolean.TRUE);
    }
    
    /**
     * Notify the application a team member has been added to the container.
     *
     * @param containerId
     *            The container id.
     */
    public void fireContainerTeamMemberAdded(final Long containerId,
            final Boolean remote) {
        setStatus("ContainerTeamMemberAdded");
        syncContainerTabContainer(containerId, remote);
    }

    /**
     * Notify the application a team member has been removed from the document.
     *
     * @param containerId
     *            The container id.
     */
    public void fireContainerTeamMemberRemoved(final Long containerId,
            final Boolean remote) {
        setStatus("ContainerTeamMemberRemoved");
        syncContainerTabContainer(containerId, remote);
    }
    
    /**
     * Notify the application that a container has in some way been updated.
     *
     * @param containerId
     *            The container that has changed.
     * @param remote
     *            True if the action was the result of a remote event; false if
     *            the action was a local event.     
     */
    public void fireContainerUpdated(final Long containerId, final Boolean remote) {
        setStatus("ContainerUpdated");
        syncContainerTabContainer(containerId, remote);
    }

    /**
     * Notify the browser that a document draft has been updated (ie. documents changed)
     * 
     * @param documentId
     *            A document id.
     */
    public void fireDocumentDraftUpdated(final Long documentId) {
        setStatus("DocumentUpdated");
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
        setStatus("DocumentUpdated");
        syncDocumentTabContainer(documentId, remote);
    }

    /**
     * Notify the application that an incoming contact invitation has been
     * accepted.
     * 
     * @param contactId
     *            A contact id.
     * @param invitationId
     *            An invitation id.
     * @param remote
     *            A remote event indicator.
     */
    public void fireIncomingContactInvitationAccepted(final JabberId contactId,
            final Long invitationId, final Boolean remote) {
        setStatus("IncomingContactInvitationAccepted");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getTabContactAvatar().syncContact(contactId, remote);
                getTabContactAvatar().syncIncomingInvitation(invitationId, remote);
            }
        });
    }

    /**
     * Notify the application than an incoming contact invitation has been
     * created.
     * 
     * @param invitationId
     *            An invitation id.
     * @param remote
     *            True if the notification is the result of a remote event.
     */
    public void fireIncomingContactInvitationCreated(final Long invitationId,
            final Boolean remote) {
        setStatus("IncomingContactInvitationCreated");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getTabContactAvatar().syncIncomingInvitation(invitationId, remote);
            }
        });
    }

    /**
     * Notify the application that an incoming contact invitation has been
     * accepted.
     * 
     * @param contactId
     *            A contact id.
     * @param invitationId
     *            An invitation id.
     * @param remote
     *            A remote event indicator.
     */
    public void fireIncomingContactInvitationDeclined(final Long invitationId,
            final Boolean remote) {
        setStatus("IncomingContactInvitationDeclined");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getTabContactAvatar().syncIncomingInvitation(invitationId, remote);
            }
        });
    }

    /**
     * Notify the application that an outgoing invitation has been accepted.
     * 
     * @param contactId
     *            A contact id.
     * @param invitationId
     *            An invitation id.
     * @param remote
     *            A remote event indicator.
     */
    public void fireOutgoingContactInvitationAccepted(final JabberId contactId,
            final Long invitationId, final Boolean remote) {
        setStatus("OutgoingContactInvitationAccepted");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getTabContactAvatar().syncContact(contactId, remote);
                getTabContactAvatar().syncOutgoingInvitation(invitationId, remote);
            }
        });
    }

    /**
     * Notify the application than an outgoing contact invitation has been
     * created.
     * 
     * @param invitationId
     *            An invitation id.
     * @param remote
     *            True if the notification is the result of a remote event.
     */
    public void fireOutgoingContactInvitationCreated(final Long invitationId,
            final Boolean remote) {
        setStatus("OutgoingContactInvitationDeleted");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getTabContactAvatar().syncOutgoingInvitation(invitationId, remote);
            }
        });
    }

    /**
     * Notify the application that an outgoing contact invitation has been
     * deleted.
     * 
     * @param invitationId
     *            An invitation id.
     * @param remote
     *            True if the notification is the result of a remote event.
     */
    public void fireOutgoingContactInvitationDeleted(final Long invitationId,
            final Boolean remote) {
        setStatus("OutgoingContactInvitationDeleted");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getTabContactAvatar().syncOutgoingInvitation(invitationId, remote);
            }
        });
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.Application#getConnection()
     */
    public Connection getConnection() { return connection; }

	/**
	 * @see com.thinkparity.ophelia.browser.platform.application.Application#getId()
	 * 
	 */
	public ApplicationId getId() { return ApplicationId.BROWSER; }

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
	 * Obtain the platform.
	 * 
	 * @return The platform the application is running on.
	 */
	public Platform getPlatform() { return super.getPlatform(); }

	/**
     * Obtain the selected contact id from the session.
     * 
     * @return A contact id.
     */
    public JabberId getSelectedContactId() { return session.getSelectedContactId(); }

	/**
	 * Obtain the selected document id from the session.
	 * 
	 * @return A document id.
	 */
	public Long getSelectedContainerId() { return session.getSelectedContainerId(); }

	/**
	 * Obtain the selected system message.
	 * 
	 * @return The selected system message id.
	 */
	public Object getSelectedSystemMessage() { return null; }

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
    
    public Boolean isBrowserWindowMaximized() {
        return JFrame.MAXIMIZED_BOTH == mainWindow.getExtendedState();
    }

    /** @see com.thinkparity.ophelia.browser.platform.application.Application#isDevelopmentMode() */
    public Boolean isDevelopmentMode() { 
        return getPlatform().isDevelopmentMode();
    }
    
    /**
     * Maximize (or un-maximize) the browser application.
     */
    public void maximize() {
        mainWindow.maximizeMainWindow(!isBrowserWindowMaximized());
    }

	/**
	 * Minimize the browser application.
	 */
	public void minimize() {
		if(!isBrowserWindowMinimized()) { mainWindow.setExtendedState(JFrame.ICONIFIED); }
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
	 * @see com.thinkparity.ophelia.browser.platform.Saveable#restoreState(com.thinkparity.ophelia.browser.platform.util.State)
	 * 
	 */
	public void restoreState(final State state) {}
    
    /**
	 * Accept an invitation.
	 * 
	 * @param systemMessageId
	 *            The system message id.
	 */
	public void runAcceptContactIncomingInvitation(final Long invitationId) {
		final Data data = new Data(1);
		data.set(AcceptIncomingInvitation.DataKey.INVITATION_ID, invitationId);
		invoke(ActionId.CONTACT_ACCEPT_INCOMING_INVITATION, data);
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
     *
     */
    public void runAddContainerDocuments(final Long containerId) {
        if(JFileChooser.APPROVE_OPTION == getJFileChooserForFileSelection().showOpenDialog(mainWindow)) {
            persistence.set(Keys.Persistence.CONTAINER_ADD_DOCUMENT_CURRENT_DIRECTORY,
                    jFileChooser.getCurrentDirectory());
            runAddContainerDocuments(containerId, jFileChooser.getSelectedFiles());
        }
    }

    /**
     * Run the create document action.
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
     * Run the apply flag seen action.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     */
    public void runApplyContainerFlagSeen(final Long containerId) {
        final Data data = new Data(2);
        data.set(ApplyFlagSeen.DataKey.ARTIFACT_ID, containerId);
        data.set(ApplyFlagSeen.DataKey.ARTIFACT_TYPE, ArtifactType.CONTAINER);
        invoke(ActionId.ARTIFACT_APPLY_FLAG_SEEN, data);         
    }

    /**
     * Run the apply flag seen action.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     */
    public void runApplyDocumentFlagSeen(final Long documentId) {
        final Data data = new Data(2);
        data.set(ApplyFlagSeen.DataKey.ARTIFACT_ID, documentId);
        data.set(ApplyFlagSeen.DataKey.ARTIFACT_TYPE, ArtifactType.DOCUMENT);
        invoke(ActionId.ARTIFACT_APPLY_FLAG_SEEN, data);         
    }

    /**
     * Run the add contact action.
     *
     */
    public void runCreateContactInvitation() {
        runCreateContactInvitation(null);
    }

    /**
     * Run the add contact action.
     * 
     * @param newContactEmail
     *              New contact email.
     */
    public void runCreateContactInvitation(final EMail email) {
        final Data data = new Data(1);
        if(null != email)
            data.set(CreateIncomingInvitation.DataKey.CONTACT_EMAIL, email);
        invoke(ActionId.CONTACT_CREATE_INCOMING_INVITATION, data);
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
     * Decline an invitation.
     * 
     * @param systemMessageId
     *            The system message id.
     */
    public void runDeclineContactIncomingInvitation(final Long invitationId) {
        final Data data = new Data(1);
        data.set(DeclineIncomingInvitation.DataKey.INVITATION_ID, invitationId);
        invoke(ActionId.CONTACT_DECLINE_INCOMING_INVITATION, data);
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
        data.set(Delete.DataKey.CONTACT_ID, contactId);
        invoke(ActionId.CONTACT_DELETE, data);        
    }
    
    /**
     * Run the export action.
     * 
     * @param containerId
     *            The container id.
     * @param directory
     *            The directory.
     */
    public void runExport(final Long containerId, final File directory) {
        final Data data = new Data(2);
        data.set(Export.DataKey.CONTAINER_ID, containerId);
        data.set(Export.DataKey.DIRECTORY, directory);
        invoke(ActionId.CONTAINER_EXPORT, data);
    }
    
    /**
     * Run the export version action.
     * 
     * @param containerId
     *            The container id.
     * @param versionId
     *            The version id.              
     * @param directory
     *            The directory.
     */
    public void runExportVersion(final Long containerId, final Long versionId, final File directory) {
        final Data data = new Data(3);
        data.set(ExportVersion.DataKey.CONTAINER_ID, containerId);
        data.set(ExportVersion.DataKey.VERSION_ID, versionId);
        data.set(ExportVersion.DataKey.DIRECTORY, directory);
        invoke(ActionId.CONTAINER_EXPORT_VERSION, data);
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
     * Run the profile sign-up action.
     *
     */
    public void runProfileSignUp() {
        invoke(ActionId.PROFILE_SIGN_UP, Data.emptyData());
    }
    
    /**
     * Publish the container.
     * 
     *  @param containerId
     *              The container id.
     *  @param teamMembers
     *              The team members. 
     *  @param contacts
     *              The contacts. 
     *  @param comment
     *              The comment.                                          
     */
    public void runPublishContainer(final ThinkParitySwingMonitor monitor,
            final Long containerId, final List<TeamMember> teamMembers,
            final List<Contact> contacts, final String comment) {
        final Data data = new Data(4);
        data.set(Publish.DataKey.CONTAINER_ID, containerId);
        data.set(Publish.DataKey.CONTACTS, contacts);
        if (null != comment)
            data.set(Publish.DataKey.COMMENT, comment);
        data.set(Publish.DataKey.MONITOR, monitor);
        data.set(Publish.DataKey.TEAM_MEMBERS, teamMembers);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                invoke(ActionId.CONTAINER_PUBLISH, data);
            }
        });
    }
    
    /**
     * Publish the container version (ie. forward).
     * 
     *  @param containerId
     *              The container id.
     *  @param versionId
     *              The version id.            
     *  @param teamMembers
     *              The team members. 
     *  @param contacts
     *              The contacts.   
     */
    public void runPublishContainerVersion(final Long containerId, final Long versionId,
            final List<TeamMember> teamMembers, final List<Contact> contacts) {
        final Data data = new Data(4);
        data.set(PublishVersion.DataKey.CONTAINER_ID, containerId);
        data.set(PublishVersion.DataKey.VERSION_ID, versionId);
        data.set(PublishVersion.DataKey.TEAM_MEMBERS, teamMembers);
        data.set(PublishVersion.DataKey.CONTACTS, contacts);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { invoke(ActionId.CONTAINER_PUBLISH_VERSION, data); }
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
     * Run the profile's remove email action.
     *
     */
    public void runRemoveProfileEmail(final Long emailId) {
        final Data data = new Data(1);
        data.set(RemoveEmail.DataKey.EMAIL_ID, emailId);
        invoke(ActionId.PROFILE_REMOVE_EMAIL, data);
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
    public void runRenameDocument(final Long documentId,
            final String documentName) {
        final Data data = new Data(2);
        data.set(RenameDocument.DataKey.DOCUMENT_ID, documentId);
        data.set(RenameDocument.DataKey.DOCUMENT_NAME, documentName);
        invoke(ActionId.CONTAINER_RENAME_DOCUMENT, data);
    }

    /**
     * Run the reset profile password action.
     *
     */
    public void runResetProfilePassword() {
        runResetProfilePassword(null);
    }

    /**
     * Run the reset profile password action.
     * 
     * @param securityAnswer
     *            The answer to the profile's security question.
     */
    public void runResetProfilePassword(final String securityAnswer) {
        final Data data;
        if (null == securityAnswer) {
            data = new Data(1);
            data.set(ResetPassword.DataKey.DISPLAY_AVATAR, Boolean.TRUE);
        } else {
            data = new Data(2);
            data.set(ResetPassword.DataKey.DISPLAY_AVATAR, Boolean.FALSE);
            data.set(ResetPassword.DataKey.SECURITY_ANSWER, securityAnswer);
        }
        invoke(ActionId.PROFILE_RESET_PASSWORD, data);
    }

    /**
     * Update the document with the file.
     * 
     * @param documentId
     *            A document id.
     * @param file
     *            A file.
     */
    public void runUpdateDocumentDraft(final Long documentId, final File file) {
        final Data data = new Data(2);
        data.set(UpdateDraft.DataKey.DOCUMENT_ID, documentId);
        data.set(UpdateDraft.DataKey.FILE, file);
        invoke(ActionId.DOCUMENT_UPDATE_DRAFT, data);
    }
  
    /**
     * Update the user's profile.
     * 
     * @param name
     *            The user's name <code>String</code>.
     * @param organization
     *            The user's organization <code>String</code>.
     * @param title
     *            The user's title <code>String</code>.
     * @param password
     *            The user's password <code>String</code>.
     * @param newPassword
     *            The user's updated password <code>String</code>.
     * @param newPasswordConfirm
     *            The user's updated password again <code>String</code>.
     */
    public void runUpdateProfile(final String name, final String organization,
            final String title, final String password,
            final String newPassword, final String newPasswordConfirm) {
        final Data data = new Data(4);
        data.set(Update.DataKey.DISPLAY_AVATAR, Boolean.FALSE);
        data.set(Update.DataKey.NAME, name);
        if (null != organization)
            data.set(Update.DataKey.ORGANIZATION, organization);
        if (null != title)
            data.set(Update.DataKey.TITLE, title);
        if (null != password) {
            data.set(Update.DataKey.PASSWORD, password);
            data.set(Update.DataKey.NEW_PASSWORD, newPassword);
            data.set(Update.DataKey.NEW_PASSWORD_CONFIRM, newPasswordConfirm);
        }
        invoke(ActionId.PROFILE_UPDATE, data);
    }

    /**
     * Run the profile's verify email action.  Since no key is specified; this
     * will display a dialog.
     *
     */
    public void runVerifyProfileEmail(final Long emailId) {
        runVerifyProfileEmail(emailId, null);
    }

    /**
     * Run the profile's verify email action.
     * 
     * @param emailId
     *            An email id <code>Long</code>.
     * @param key
     *            An email verification key <code>String</code>.
     */
    public void runVerifyProfileEmail(final Long emailId, final String key) {
        final Data data = new Data(3);
        data.set(VerifyEmail.DataKey.EMAIL_ID, emailId);
        if (null == key) {
            data.set(VerifyEmail.DataKey.DISPLAY_AVATAR, Boolean.TRUE);
        } else {
            data.set(VerifyEmail.DataKey.DISPLAY_AVATAR, Boolean.FALSE);
            data.set(VerifyEmail.DataKey.KEY, key);
        }
        invoke(ActionId.PROFILE_VERIFY_EMAIL, data);
    }

    /*
     *

     */
    /**
	 * @see com.thinkparity.ophelia.browser.platform.Saveable#saveState(com.thinkparity.ophelia.browser.platform.util.State)
	 * 
	 */
	public void saveState(final State state) {}

    /**
     * Select a contact.
     * 
     * @param contactId
     *            The contact id.
     */
    public void selectContact(final JabberId contactId) {
        final JabberId oldSelection = session.getSelectedContactId();
        session.setSelectedContactId(contactId);

        if(null != oldSelection && !oldSelection.equals(contactId)) {
            clearStatus();
        }
    }

    /**
     * Select a container.
     * 
     * @param containerId
     *             The container id.
     */
    public void selectContainer(final Long containerId) {
        final Long oldSelection = session.getSelectedContainerId();
        session.setSelectedContainerId(containerId);

        if(null != oldSelection && !oldSelection.equals(containerId)) {
            clearStatus();
        }        
    }
    
	/**
     * Set the cursor.
     * 
     * @param cursor
     *          The cursor to use.
     */
    public void setCursor(Cursor cursor) {
        mainWindow.getContentPane().setCursor(cursor);
    }

    /**
	 * @see com.thinkparity.ophelia.browser.platform.application.Application#start()
	 * 
	 */
    public void start(final Platform platform) {
		logApiId();

		assertStatusChange(ApplicationStatus.STARTING);
		setStatus(ApplicationStatus.STARTING);

        connection = getSessionModel().isLoggedIn() ?
                Connection.ONLINE : Connection.OFFLINE;
        setStatus(connection);

		ed = new EventDispatcher(this);
		ed.start();

		assertStatusChange(ApplicationStatus.RUNNING);
		openMainWindow();

		setStatus(ApplicationStatus.RUNNING);
		notifyStart();
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

    /** Display the main status avatar. */
    void displayMainStatusAvatar() {
        final Data input = new Data(2);
        input.set(MainStatusAvatar.DataKey.PROFILE, getProfile());
        input.set(MainStatusAvatar.DataKey.CONNECTION, getConnection());
        setInput(AvatarId.MAIN_STATUS, input);
        displayStatus(AvatarId.MAIN_STATUS);
    }

    /** Display the main title avatar. */
	void displayMainTitleAvatar() {
        final Data input = new Data(2);
        input.set(MainTitleAvatar.DataKey.PROFILE, getProfile());
        input.set(MainTitleAvatar.DataKey.TAB_ID, MainTitleAvatar.TabId.CONTAINER);
        setInput(AvatarId.MAIN_TITLE, input);
        displayTitle(AvatarId.MAIN_TITLE);
	}

    /** Notify the session has been terminated. */
    void fireConnectionOffline() {
        connection = Connection.OFFLINE;
        setStatus(connection);
    }

    /** Notify the session has been established. */
    void fireConnectionOnline() {
        connection = Connection.ONLINE;
        setStatus(connection);
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
     * Clear any status messages.
     * 
     * @param area
     *            The status area.
     */
    private void clearStatus() {
        Data input = (Data) getMainStatusAvatar().getInput();
        if (null == input) {
            input = new Data(3);
        }
        input.unset(MainStatusAvatar.DataKey.CUSTOM_MESSAGE);
        input.unset(MainStatusAvatar.DataKey.CUSTOM_MESSAGE_ARGUMENTS);
        getMainStatusAvatar().setInput((Data) input.clone());
    }

    /**
     * Open a confirmation dialogue.
     * 
     * @param input
     *            The dialogue's input.
     * @return True if the user confirmed.
     */
    private Boolean confirm(final Data input) {
        open(WindowId.CONFIRM, AvatarId.DIALOG_CONFIRM, input);
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
	private void displayAvatar(final WindowId windowId, final AvatarId avatarId) {
		Assert.assertNotNull("Cannot display on a null window.", windowId);
		Assert.assertNotNull("Cannot display a null avatar.", avatarId);
		final Window window = WindowFactory.create(windowId, mainWindow);

		final Avatar avatar = getAvatar(avatarId);

		final Object input = getAvatarInput(avatarId);
		if (null == input) {
            logger.logInfo("Avatar {0}'s input is null.", avatarId);
		} else {
            avatar.setInput(getAvatarInput(avatarId));
		}
        
        setUpSemiTransparentLayer(window);
        
		SwingUtilities.invokeLater(new Runnable() {
			public void run() { window.open(avatar); }
		});
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
	private AbstractAction getAction(final ActionId id) {
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
    private ConfirmDialog getConfirmAvatar() {
        return (ConfirmDialog) getAvatar(AvatarId.DIALOG_CONFIRM);
    }

	/**
	 * Obtain the file chooser.
	 * 
	 * @return The file chooser.
	 */
	private JFileChooser getJFileChooserForFileSelection() {
        jFileChooser = JFileChooserUtil.getJFileChooser(JFileChooser.FILES_ONLY, Boolean.TRUE, null,
                persistence.get(Keys.Persistence.CONTAINER_ADD_DOCUMENT_CURRENT_DIRECTORY,
                        (File) null));
		return jFileChooser;
	}

    /**
     * Obtain the main status avatar.
     * 
     * @return The main status avatar.
     */
    private MainStatusAvatar getMainStatusAvatar() {
        return (MainStatusAvatar) getAvatar(AvatarId.MAIN_STATUS);
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
     * Obtain the contact tab avatar.
     * 
     * @return The contact tab avatar.
     */
    private ContactAvatar getTabContactAvatar() {
        return (ContactAvatar) getAvatar(AvatarId.TAB_CONTACT);
    }

    /**
     * Obtain the container tab avatar.
     * 
     * @return A <code>ContainerAvatar</code>.
     */
    private ContainerAvatar getTabContainerAvatar() {
        return (ContainerAvatar) getAvatar(AvatarId.TAB_CONTAINER);
    }

	private void invoke(final ActionId actionId, final Data data) {
		try {
			getAction(actionId).invoke(data);
		} catch(final Throwable t) {
            logger.logError(t, "Could not invoke action {0} with data {1}.", actionId, data);
            // TODO Provide meaningful error messages
            displayErrorDialog("", t);
		}
	}

    private Boolean isBrowserWindowMinimized() {
		return JFrame.ICONIFIED == mainWindow.getExtendedState();
	}
    
    private Boolean isBrowserWindowOpen() {
		return null != mainWindow && mainWindow.isVisible();
	}

	private void open(final WindowId windowId,
            final AvatarId avatarId, final Data input) {
        final Window window = WindowFactory.create(windowId, mainWindow);

        final Avatar avatar = getAvatar(avatarId);
        avatar.setInput(input);
        
        setUpSemiTransparentLayer(window);

        window.open(avatar);
    }

    /**
	 * Open the main browser window.
	 *
	 */
	private void openMainWindow() {
		mainWindow = new BrowserWindow(this);
		displayHelper.setBrowserWindow(mainWindow);
		mainWindow.open();
	}

	private void reOpenMainWindow() {
        mainWindow = new BrowserWindow(this);
        displayHelper.setBrowserWindow(mainWindow);
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
     * Set the connection status.
     * 
     * @param connection
     *            A platform connection.
     */
    private void setStatus(final Connection connection) {
        Data input = (Data) getMainStatusAvatar().getInput();
        if (null == input) {
            input = new Data(3);
        }
        input.set(MainStatusAvatar.DataKey.CONNECTION, connection);
        getMainStatusAvatar().setInput((Data) input.clone());
    }

    /**
     * Set a custom status message.
     * 
     * @param customMessage
     *            A status message.
     */
    private void setStatus(final String customMessage) {
        setStatus(customMessage, null);
    }

    /**
     * Set a custom status message.
     * 
     * @param message
     *            A status message.
     * @param arguments
     *            Status message arguments.
     */
    private void setStatus(final String customMessage, final Object[] customMessageArguments) {
        Data input = (Data) getMainStatusAvatar().getInput();
        if (null == input) {
            input = new Data(3);
        }
        input.set(MainStatusAvatar.DataKey.CUSTOM_MESSAGE, customMessage);
        if (null != customMessageArguments) {
            input.set(MainStatusAvatar.DataKey.CUSTOM_MESSAGE_ARGUMENTS, customMessageArguments);
        }
        else {
            input.unset(MainStatusAvatar.DataKey.CUSTOM_MESSAGE_ARGUMENTS);
        }
        getMainStatusAvatar().setInput((Data) input.clone());
    }

    /**
     * Set up the semi-transparent layer on the browser window.
     * It will turn on immediately and turn off when the window
     * is closed.
     * 
     * @param window
     *          A window.
     */
    private void setUpSemiTransparentLayer(final Window window) {
        mainWindow.enableSemiTransparentLayer(Boolean.TRUE);
        window.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                mainWindow.enableSemiTransparentLayer(Boolean.FALSE);
            }
        });
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
    private void syncContainerTabContainer(final Long containerId,
            final Boolean remote) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getTabContainerAvatar().syncContainer(containerId, remote);
            }
        });
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

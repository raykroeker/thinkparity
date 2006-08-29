/*
 * Created On: Jan 18, 2006
 */
package com.thinkparity.browser.application.browser;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;

import com.thinkparity.browser.Constants.Keys;
import com.thinkparity.browser.application.AbstractApplication;
import com.thinkparity.browser.application.browser.display.DisplayId;
import com.thinkparity.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.browser.application.browser.display.avatar.MainStatusAvatar;
import com.thinkparity.browser.application.browser.display.avatar.MainTitleAvatar;
import com.thinkparity.browser.application.browser.display.avatar.dialog.ErrorAvatar;
import com.thinkparity.browser.application.browser.display.avatar.dialog.RenameAvatar;
import com.thinkparity.browser.application.browser.display.avatar.dialog.contact.ReadContactAvatar;
import com.thinkparity.browser.application.browser.display.avatar.dialog.container.CreateContainerAvatar;
import com.thinkparity.browser.application.browser.display.avatar.dialog.profile.VerifyEMailAvatar;
import com.thinkparity.browser.application.browser.display.avatar.tab.contact.ContactAvatar;
import com.thinkparity.browser.application.browser.display.avatar.tab.container.ContainerAvatar;
import com.thinkparity.browser.application.browser.window.WindowFactory;
import com.thinkparity.browser.application.browser.window.WindowId;
import com.thinkparity.browser.platform.Platform;
import com.thinkparity.browser.platform.Platform.Connection;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionFactory;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.ActionRegistry;
import com.thinkparity.browser.platform.action.Data;
import com.thinkparity.browser.platform.action.contact.AcceptIncomingInvitation;
import com.thinkparity.browser.platform.action.contact.CreateIncomingInvitation;
import com.thinkparity.browser.platform.action.contact.DeclineIncomingInvitation;
import com.thinkparity.browser.platform.action.contact.Delete;
import com.thinkparity.browser.platform.action.contact.Read;
import com.thinkparity.browser.platform.action.container.AddDocument;
import com.thinkparity.browser.platform.action.container.Create;
import com.thinkparity.browser.platform.action.container.CreateDraft;
import com.thinkparity.browser.platform.action.container.Publish;
import com.thinkparity.browser.platform.action.document.Open;
import com.thinkparity.browser.platform.action.document.OpenVersion;
import com.thinkparity.browser.platform.action.document.Rename;
import com.thinkparity.browser.platform.action.document.UpdateDraft;
import com.thinkparity.browser.platform.action.profile.AddEmail;
import com.thinkparity.browser.platform.action.profile.RemoveEmail;
import com.thinkparity.browser.platform.action.profile.Update;
import com.thinkparity.browser.platform.action.profile.VerifyEmail;
import com.thinkparity.browser.platform.application.ApplicationId;
import com.thinkparity.browser.platform.application.ApplicationStatus;
import com.thinkparity.browser.platform.application.L18nContext;
import com.thinkparity.browser.platform.application.dialog.ConfirmDialog;
import com.thinkparity.browser.platform.application.display.Display;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.application.window.Window;
import com.thinkparity.browser.platform.util.State;
import com.thinkparity.browser.platform.util.persistence.Persistence;
import com.thinkparity.browser.platform.util.persistence.PersistenceFactory;

import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.index.IndexHit;
import com.thinkparity.model.xmpp.JabberId;

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
        this.persistence = PersistenceFactory.getPersistence(getClass());
		this.session= new BrowserSession(this);
	}

    /**
     * Set the search results in the search filter and apply it to the current
     * list.
     * 
     * @param searchResult
     *            The search results.
     */
	public void applySearchFilter(final List<IndexHit> searchResult) {
        switch(getMainTitleAvatarTab()) {
        case CONTAINER:
            getTabContainerAvatar().applySearchFilter(searchResult);            
            break;
        case CONTACT:
            getTabContactAvatar().applySearchFilter(searchResult);
            break;
        default:
            Assert.assertUnreachable("UNKNOWN TAB");
        }
	}

    /**
     * Clear the non-search filters for the containers list.
     *
     * @see #removeSearchFilter()
     */
    public void clearFilters() { getTabContainerAvatar().clearFilters(); }

    /** Close the main window. */
    public void closeBrowserWindow() {
        Assert.assertNotNull("[BROWSER WINDOW IS NULL]", mainWindow);
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
     * Debug the main list.
     *
     */
	public void debugMain() { getTabContainerAvatar().debug(); }
    
    /**
     * Display the invite dialogue.
     *
     */   
    public void displayContactCreateInvitation() {
        displayAvatar(WindowId.POPUP, AvatarId.DIALOG_CONTACT_CREATE_OUTGOING_INVITATION);
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
     * @param documentId
     *            A document id.
     * @param documentName
     *            A document name.
     */
    public void displayRenameDialog(final Long documentId,
            final String documentName) {
        final Data input = new Data(3);
        input.set(RenameAvatar.DataKey.DOCUMENT_ID, documentId);
        input.set(RenameAvatar.DataKey.DOCUMENT_NAME, documentName);
        setInput(AvatarId.DIALOG_RENAME, input);
        displayAvatar(WindowId.RENAME, AvatarId.DIALOG_RENAME);
    }

    /** Display the contact avatar tab. */
    public void displayTabContactAvatar() {
        displayAvatar(DisplayId.CONTENT, AvatarId.TAB_CONTACT);
    }
    
    /** Display the container avatar tab. */
    public void displayTabContainerAvatar() {
        displayAvatar(DisplayId.CONTENT, AvatarId.TAB_CONTAINER);
    }

	/**
     * Display the update profile dialog.
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
	 * @see com.thinkparity.browser.platform.application.Application#end()
	 * 
	 */
	public void end(final Platform platform) {
		logger.info("[LBROWSER] [APPLICATION] [BROWSER] [END]");
		assertStatusChange(ApplicationStatus.ENDING);

		ed.end();
		ed = null;

		if(isBrowserWindowOpen()) { disposeBrowserWindow(); }

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
     * Notify the application a container has been closed.
     * 
     * @param containerId
     *            The container id.
     * @param remote
     *            True if the closing was the result of a remote event; false if
     *            the closing was a local event.
     */
    public void fireContainerClosed(final Long containerId, final Boolean remote) {
        setStatus("ContainerClosed");
        final Boolean select = Boolean.FALSE;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { getTabContainerAvatar().syncContainer(containerId, remote, select); }
        });
    }
    
    /**
     * Notify the application a container confirmation has been received.
     *
     * @param containerId
     *            The container id.
     */
    public void fireContainerConfirmationReceived(final Long containerId) {
        final Boolean select = Boolean.FALSE;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { getTabContainerAvatar().syncContainer(containerId, Boolean.FALSE, select); }
        });
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
        final Boolean select = Boolean.TRUE;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getTabContainerAvatar().syncContainer(containerId, remote, select);
            }
        });
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
        final Boolean select = Boolean.FALSE;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getTabContainerAvatar().syncContainer(containerId, remote, select);
            }
        });
    }
    
    /**
     * Notify the application that a document has been added.
     * 
     * @param containerId
     *            The container id.
     * @param documentId
     *            The document id.
     * @param remote
     *            True if the action was the result of a remote event; false if
     *            the action was a local event.
     */
    public void fireContainerDocumentAdded(final Long containerId,
            final Long documentId) {
        setStatus("DocumentCreated");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getTabContainerAvatar().syncDocument(containerId, documentId, Boolean.FALSE);
            }
        });
    }
    
    /**
     * Notify the application that a document has been deleted.
     * 
     * @param containerId
     *            The container id.
     * @param documentId
     *            The document id.
     * @param remote
     *            True if the action was the result of a remote event; false if
     *            the action was a local event.
     */
    public void fireContainerDocumentRemoved(final Long documentId) {
        setStatus("DocumentDeleted");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getTabContainerAvatar().syncDocument(documentId, Boolean.FALSE);
            }
        });
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
    public void fireContainerDraftCreated(final Long containerId, final Boolean remote) {
        setStatus("ContainerDraftCreated");
        final Boolean select = Boolean.TRUE;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { getTabContainerAvatar().syncContainer(containerId, remote, select); }
        });         
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

        // refresh the container in the main list
        final Boolean select = Boolean.FALSE;
        SwingUtilities.invokeLater(new Runnable() {
                public void run() { getTabContainerAvatar().syncContainer(containerId, Boolean.TRUE, select); }
        });
    }

    /**
     * Notify the application a team member has been added to the container.
     *
     * @param containerId
     *            The container id.
     */
    public void fireContainerTeamMemberAdded(final Long containerId) {
        setStatus("ContainerTeamMemberAdded");
        final Boolean select = Boolean.FALSE;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { getTabContainerAvatar().syncContainer(containerId, Boolean.TRUE, select); }
        });
    }
    
    /**
     * Notify the application a team member has been removed from the document.
     *
     * @param containerId
     *            The container id.
     */
    public void fireContainerTeamMemberRemoved(final Long containerId) {
        setStatus("ContainerTeamMemberRemoved");
        final Boolean select = Boolean.FALSE;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { getTabContainerAvatar().syncContainer(containerId, Boolean.TRUE, select); }
        });
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
        final Boolean select = Boolean.FALSE;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getTabContainerAvatar().syncContainer(containerId, remote, select);
            }
        });
    }

    /**
     * Notify the browser that a document draft has been updated.
     * 
     * @param documentId
     *            A document id.
     */
    public void fireDocumentDraftUpdated(final Long documentId) {
        setStatus("DocumentUpdated");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getTabContainerAvatar().syncDocument(documentId, Boolean.FALSE);
            }
        });
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
        // refresh the document in the main list
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getTabContainerAvatar().syncDocument(documentId, remote);
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
     * @see com.thinkparity.browser.platform.application.Application#getConnection()
     */
    public Connection getConnection() { return connection; }

	/**
	 * @see com.thinkparity.browser.platform.application.Application#getId()
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
	 * @see com.thinkparity.browser.platform.application.Application#hibernate()
	 * 
	 */
	public void hibernate(final Platform platform) {
		assertStatusChange(ApplicationStatus.HIBERNATING);

		disposeBrowserWindow();

		setStatus(ApplicationStatus.HIBERNATING);
		notifyHibernate();
	}

    /** @see com.thinkparity.browser.platform.application.Application#isDevelopmentMode() */
    public Boolean isDevelopmentMode() { 
        return getPlatform().isDevelopmentMode();
    }

	/**
     * Determine whether or not the main avatar's filter is enabled.
     * 
     * @return True if it is; false otherwise.
     */
    public Boolean isFilterEnabled() {
        if(null == getTabContainerAvatar()) { return Boolean.FALSE; }
        return getTabContainerAvatar().isFilterEnabled();
    }

	/**
	 * Minimize the browser application.
	 *
	 */
	public void minimize() {
		if(!isBrowserWindowMinimized()) { mainWindow.setExtendedState(JFrame.ICONIFIED); }
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
     * Remove the search filter from the current list.
     *
     */
    public void removeSearchFilter() {
        switch(getMainTitleAvatarTab()) {
        case CONTACT:
            getTabContactAvatar().removeSearchFilter();
            break;
        case CONTAINER:
            getTabContainerAvatar().removeSearchFilter();
            break;
        default:
            Assert.assertUnreachable("UNKNOWN TAB");
        }
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
        mainWindow.setSize(newS);
    }    

    /**
	 * @see com.thinkparity.browser.platform.application.Application#restore(com.thinkparity.browser.platform.Platform)
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
	public void runAcceptContactIncomingInvitation(final Long invitationId) {
		final Data data = new Data(1);
		data.set(AcceptIncomingInvitation.DataKey.INVITATION_ID, invitationId);
		invoke(ActionId.CONTACT_ACCEPT_INCOMING_INVITATION, data);
	}

    /**
     * Run the create document action, browse to select the document.
     * 
     * @param containerId
     *            The container id.
     *
     */
    public void runAddContainerDocuments(final Long containerId) {
        if(JFileChooser.APPROVE_OPTION == getJFileChooser().showOpenDialog(mainWindow)) {
            persistence.set(
                    Keys.Persistence.JFILECHOOSER_CURRENT_DIRECTORY,
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
     *  Publish the selected container.
     *  
     *  @param containerId
     *              The container id.
     *              
     */
    public void runPublishContainer(final Long containerId) {
        final Data data = new Data(1);
        data.set(Publish.DataKey.CONTAINER_ID, containerId);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { invoke(ActionId.CONTAINER_PUBLISH, data); }
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
     */
    public void runRenameDocument(final Long documentId) {
        final Data data = new Data(1);
        data.set(Rename.DataKey.DOCUMENT_ID, documentId);
        invoke(ActionId.DOCUMENT_RENAME, data);
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
        final Data data = new Data(3);
        data.set(Rename.DataKey.DOCUMENT_ID, documentId);
        data.set(Rename.DataKey.DOCUMENT_NAME, documentName);
        invoke(ActionId.DOCUMENT_RENAME, data);
    }

    /**
     * Run a search for an artifact on the criteria.
     * 
     * @param criteria
     *            The search criteria.
     */
	public void runSearch(final String expression) {
	    final Data data = new Data(1);
        switch(getMainTitleAvatarTab()) {
        case CONTACT:
            data.set(com.thinkparity.browser.platform.action.contact.Search.DataKey.EXPRESSION, expression);
            runSearchContact(data);
            break;
        case CONTAINER:
            data.set(com.thinkparity.browser.platform.action.container.Search.DataKey.EXPRESSION, expression);
            runSearchContainer(data);
            break;
        default:
            Assert.assertUnreachable("UNKNOWN TAB");
        }
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
	 * @see com.thinkparity.browser.platform.Saveable#saveState(com.thinkparity.browser.platform.util.State)
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
	 * @see com.thinkparity.browser.platform.application.Application#start()
	 * 
	 */
	public void start(final Platform platform) {
		logger.info("[BROWSER2] [APP] [B2] [START]");

		assertStatusChange(ApplicationStatus.STARTING);
		setStatus(ApplicationStatus.STARTING);

        connection = getSessionModel().isLoggedIn() ?
                Connection.ONLINE : Connection.OFFLINE;

		ed = new EventDispatcher(this);
		ed.start();

		assertStatusChange(ApplicationStatus.RUNNING);
		openMainWindow();

		setStatus(ApplicationStatus.RUNNING);
		notifyStart();
	}
    
    public void toggleStatusImage() {
        ((com.thinkparity.browser.application.browser.display.StatusDisplay) mainWindow.getDisplay(DisplayId.STATUS)).toggleImage();
    }

    /** Display the main content avatar. */
    void displayMainContentAvatar() {
        displayAvatar(DisplayId.CONTENT, AvatarId.MAIN_CONTENT);
    }

    /** Display the main status avatar. */
    void displayMainStatusAvatar() {
        displayAvatar(DisplayId.STATUS, AvatarId.MAIN_STATUS);
    }

    /** Display the main title avatar. */
	void displayMainTitleAvatar() {
        final Data input = new Data(2);
        input.set(MainTitleAvatar.DataKey.PROFILE, getProfile());
        input.set(MainTitleAvatar.DataKey.TAB, MainTitleAvatar.Tab.CONTAINER);
        setInput(AvatarId.MAIN_TITLE, input);
    	displayAvatar(DisplayId.TITLE, AvatarId.MAIN_TITLE);
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
	private void displayAvatar(final DisplayId displayId, final AvatarId avatarId) {
		Assert.assertNotNull("Cannot display on a null display.", displayId);
		Assert.assertNotNull("Cannot display a null avatar.", avatarId);
		final Display display = mainWindow.getDisplay(displayId);

		final Avatar nextAvatar = getAvatar(avatarId);

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

	/** Dispose the main window. */
    private void disposeBrowserWindow() {
        Assert.assertNotNull(
                "[LBROWSER] [APPLICATION] [BROWSER] [DISPOSE BROWSER WINDOW] [BROWSER WINDOW IS NULL]",
                mainWindow);
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
	private JFileChooser getJFileChooser() {
		if(null == jFileChooser) {
            jFileChooser = new JFileChooser();
            jFileChooser.setMultiSelectionEnabled(Boolean.TRUE);
		}
        jFileChooser.setCurrentDirectory(persistence.get(
                Keys.Persistence.JFILECHOOSER_CURRENT_DIRECTORY,
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
    private MainTitleAvatar.Tab getMainTitleAvatarTab() {
        return (MainTitleAvatar.Tab) ((Data) getMainTitleAvatar().getInput()).get(MainTitleAvatar.DataKey.TAB);
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
     * Get the container tab avatar.
     * 
     * @return The container tab avatar.
     */
    private ContainerAvatar getTabContainerAvatar() {
        return (ContainerAvatar) getAvatar(AvatarId.TAB_CONTAINER);
    }

	private void invoke(final ActionId actionId, final Data data) {
		try {
			getAction(actionId).invoke(data);
		} catch(final Throwable t) {
            logger.error("[BROWSER] [INVOKE]", t);
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

        window.open(avatar);
    }

	/**
	 * Open the main browser window.
	 *
	 */
	private void openMainWindow() {
		mainWindow = new BrowserWindow(this);
		mainWindow.open();
	}

    private void reOpenMainWindow() {
        mainWindow = new BrowserWindow(this);
        mainWindow.reOpen();
    }


	/**
     * Run the search action for the contact tab.
     * 
     * @param data
     *            The action data.
     */
    private void runSearchContact(final Data data) {
        invoke(ActionId.CONTACT_SEARCH, data);
    }

    /**
     * Run the search action for the container tab.
     * 
     * @param data
     *            The action data.
     */
    private void runSearchContainer(final Data data) {
        invoke(ActionId.CONTAINER_SEARCH, data);
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
}

/*
 * Jan 18, 2006
 */
package com.thinkparity.browser.application.browser;

import java.awt.Point;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
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
import com.thinkparity.browser.application.browser.display.avatar.BrowserTitleAvatar;
import com.thinkparity.browser.application.browser.display.avatar.Status;
import com.thinkparity.browser.application.browser.display.avatar.document.RenameDialog;
import com.thinkparity.browser.application.browser.display.avatar.session.SessionSendVersion;
import com.thinkparity.browser.application.browser.window.WindowFactory;
import com.thinkparity.browser.application.browser.window.WindowId;
import com.thinkparity.browser.platform.Platform;
import com.thinkparity.browser.platform.Platform.Connection;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionFactory;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;
import com.thinkparity.browser.platform.action.artifact.AcceptKeyRequest;
import com.thinkparity.browser.platform.action.artifact.DeclineAllKeyRequests;
import com.thinkparity.browser.platform.action.artifact.DeclineKeyRequest;
import com.thinkparity.browser.platform.action.artifact.KeyRequested;
import com.thinkparity.browser.platform.action.artifact.RequestKey;
import com.thinkparity.browser.platform.action.artifact.Search;
import com.thinkparity.browser.platform.action.document.*;
import com.thinkparity.browser.platform.action.session.AcceptInvitation;
import com.thinkparity.browser.platform.action.session.DeclineInvitation;
import com.thinkparity.browser.platform.action.system.message.DeleteSystemMessage;
import com.thinkparity.browser.platform.application.ApplicationId;
import com.thinkparity.browser.platform.application.ApplicationStatus;
import com.thinkparity.browser.platform.application.L18nContext;
import com.thinkparity.browser.platform.application.dialog.ConfirmDialog;
import com.thinkparity.browser.platform.application.display.Display;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.application.window.Window;
import com.thinkparity.browser.platform.util.State;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.artifact.ArtifactState;
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

	/** An apache logger. */
	protected final Logger logger;

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
		this.logger = LoggerFactory.getLogger(getClass());
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

    /** Close the main window. */
    public void closeBrowserWindow() {
        Assert.assertNotNull(
                "[LBROWSER] [APPLICATION] [BROWSER] [CLOSE BROWSER WINDOW] [BROWSER WINDOW IS NULL]",
                mainWindow);
        mainWindow.dispatchEvent(new WindowEvent(mainWindow, WindowEvent.WINDOW_CLOSING));
    }

    /**
     * Open a confirmation dialog.
     * 
     * @param messageKey
     *            The confirmation mesage localization key.
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
     *            The confirmation mesage localization key.
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
	public void debugMain() { getMainAvatar().debug(); }

    public void displayAddNewDocumentTeamMember(final Long documentId) {
        setInput(AvatarId.ADD_TEAM_MEMBER, documentId);
        displayAvatar(WindowId.POPUP, AvatarId.ADD_TEAM_MEMBER);
    }

	/** Display the contact search dialogue. */
	public void displayContactSearch() {
		displayAvatar(WindowId.POPUP, AvatarId.SESSION_SEARCH_PARTNER);
	}

	/**
     * Display a document rename dialog.
     * 
     * @param documentId
     *            A document id.
     * @param documentName
     *            A document name.
     */
    public void displayRenameDocument(final Long documentId,
            final String documentName) {
        final Data input = new Data(2);
        input.set(RenameDialog.DataKey.DOCUMENT_ID, documentId);
        input.set(RenameDialog.DataKey.DOCUMENT_NAME, documentName);
        setInput(AvatarId.RENAME_DIALOGUE, input);
        displayAvatar(WindowId.RENAME, AvatarId.RENAME_DIALOGUE);
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
     * Notify the application a document has been closed.
     * 
     * @param documentId
     *            The document id.
     * @param remote
     *            True if the closing was the result of a remote event; false if
     *            the closing was a local event.
     */
    public void fireDocumentClosed(final Long documentId, final Boolean remote) {
        setCustomStatusMessage("DocumentClosed");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { getMainAvatar().syncDocument(documentId, remote); }
        });
    }

    /**
     * Notify the application a document confirmation has been received.
     *
     * @param documentId
     *      The document id.
     */
    public void fireDocumentConfirmationReceived(final Long documentId) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { getMainAvatar().syncDocument(documentId, Boolean.FALSE); }
        });
    }

    /**
	 * Notify the application that a document has been created.
	 * 
	 * @param documentId
	 *            The document id.
	 */
	public void fireDocumentCreated(final Long documentId, final Boolean remote) {
        setCustomStatusMessage("DocumentCreated");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() { getMainAvatar().syncDocument(documentId, remote); }
		});
	}

    /**
	 * Notify the application that a document has been created.
	 * 
	 * @param documentId
	 *            The document id.
	 */
	public void fireDocumentDeleted(final Long documentId) {
        setCustomStatusMessage("DocumentDeleted");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				((BrowserInfoAvatar) avatarRegistry.get(AvatarId.BROWSER_INFO)).reload();
			}
		});
		// refresh the document main list
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				((BrowserMainAvatar) avatarRegistry.get(AvatarId.BROWSER_MAIN)).syncDocument(documentId, Boolean.FALSE);
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
        setCustomStatusMessage("DocumentReceived");

        // flag it as not having been seen
        final ArtifactModel aModel = getArtifactModel();
        aModel.removeFlagSeen(documentId);

		// refresh the document main list
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				((BrowserMainAvatar) avatarRegistry.get(AvatarId.BROWSER_MAIN)).syncDocument(documentId, Boolean.TRUE);
			}
		});
	}

	/**
     * Notify the application that a set of documents has been created.
     * 
     * @param documentIds
     *            The document ids.
     */
    public void fireDocumentsCreated(final List<Long> documentIds) {
        if(documentIds.size() > 1) { setCustomStatusMessage("DocumentsCreated"); }
        else { setCustomStatusMessage("DocumentCreated"); }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ((BrowserInfoAvatar) avatarRegistry.get(AvatarId.BROWSER_INFO)).reload();
            }
        });
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ((BrowserMainAvatar) avatarRegistry.get(AvatarId.BROWSER_MAIN)).syncDocuments(documentIds, Boolean.FALSE);
            }
        });
    }

	/**
     * Notify the application a team member has been added to the document.
     *
     * @param documentId
     *      A document id.
     */
    public void fireDocumentTeamMemberAdded(final Long documentId) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { getMainAvatar().syncDocument(documentId, Boolean.TRUE); }
        });
        fireDocumentUpdated(documentId, Boolean.TRUE);
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
        setCustomStatusMessage("DocumentUpdated");
		// refresh the document in the main list
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				((BrowserMainAvatar) avatarRegistry.get(AvatarId.BROWSER_MAIN)).syncDocument(documentId, remoteReload);
			}
		});
	}

	/** Notify the application the filters are on. */
    public void fireFilterApplied() {
        setFilterStatusMessage("FilterOn");
    }

	/** Notify the application the filters are off. */
    public void fireFilterRevoked() {
        setFilterStatusMessage("FilterOff");
    }

	/** Notify the session has been established. */
    public void fireSessionEstablished() {
        getStatusAvatar().reloadStatusMessage(
                Status.Area.CONNECTION, "ConnectionOnline");
        getTitleAvatar().reloadConnectionStatus(Connection.ONLINE);
    }

	/** Notify the session has been terminated. */
    public void fireSessionTerminated() {
        getStatusAvatar().reloadStatusMessage(
                Status.Area.CONNECTION, "ConnectionOffline");
        getTitleAvatar().reloadConnectionStatus(Connection.OFFLINE);
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
        if(null == getMainAvatar()) { return Boolean.FALSE; }
        return getMainAvatar().isFilterEnabled();
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

	/** Add a team member to the selected document. */
    public void runAddNewDocumentTeamMember() {
        runAddNewDocumentTeamMember(session.getSelectedDocumentId(), (JabberId) null);
    }

    /**
     * Add a team member to the selected document.
     * 
     * @param documentId
     *            The document id.
     * @param jabberId
     *            A jabber id.
     */
    public void runAddNewDocumentTeamMember(final Long documentId,
            final JabberId jabberId) {
        final List<JabberId> jabberIds;
        if(null == jabberId) { jabberIds = null; }
        else {
            jabberIds = new ArrayList<JabberId>();
            jabberIds.add(jabberId);
        }
        runAddNewDocumentTeamMember(documentId, jabberIds);
    }

    /**
     * Add a team member to the selected document.
     * 
     * @param documentId
     *            The document id.
     * @param jabberIds
     *            The jabber ids.
     */
    public void runAddNewDocumentTeamMember(final Long documentId,
            final List<JabberId> jabberIds) {
        final Data data = new Data(2);
        data.set(AddNewTeamMember.DataKey.DOCUMENT_ID, documentId);
        if(null != jabberIds)
            data.set(AddNewTeamMember.DataKey.JABBER_IDS, jabberIds);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { invoke(ActionId.ADD_TEAM_MEMBER, data); }
        });
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
		    runCreateDocument(jFileChooser.getSelectedFile());
		}
	}

	/**
     * Create a document.
     * 
     * @param file
     *            The document file.
     */
    public void runCreateDocument(final File file) {
        final Data data = new Data(1);
        data.set(Create.DataKey.FILE, file);
        invoke(ActionId.DOCUMENT_CREATE, data);
    }

	/**
     * Create multiple documents.
     * 
     * @param files
     *            The files.
     */
    public void runCreateDocuments(final List<File> files) {
        final Data data = new Data(1);
        data.set(CreateDocuments.DataKey.FILES, files);
        invoke(ActionId.CREATE_DOCUMENTS, data);
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

	public void runKeyRequested(final Long artifactId) {
        final Data data = new Data(1);
        data.set(KeyRequested.DataKey.ARTIFACT_ID, artifactId);
        invoke(ActionId.ARTIFACT_KEY_REQUESTED, data);
    }

	/**
     * Run the move to front action.
     *
     */
    public void runMoveBrowserToFront() { mainWindow.toFront(); }

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

    /** Publish the selected document. */
    public void runPublishDocument() {
        final Data data = new Data(1);
        data.set(Publish.DataKey.DOCUMENT_ID, session.getSelectedDocumentId());
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { invoke(ActionId.PUBLISH_DOCUMENT, data); }
        });
    }

    /**
     * Run the document rename action.
     * 
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
     * @param documentId
     *            A document id.
     * @param documentName
     *            An document name.
     */
    public void runRenameDocument(final Long documentId,
            final String documentName) {
        final Data data = new Data(2);
        data.set(Rename.DataKey.DOCUMENT_ID, documentId);
        data.set(Rename.DataKey.DOCUMENT_NAME, documentName);
        invoke(ActionId.DOCUMENT_RENAME, data);
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
     * Run the send key action for a document.
     *
     * @param documentId
     *      A document id.
     * @param userId
     *      A user id.
     */
    public void runSendDocumentKey(final Long documentId, final JabberId userId) {
        final Data data = new Data(2);
        data.set(SendKey.DataKey.DOCUMENT_ID, documentId);
        data.set(SendKey.DataKey.USER_ID, userId);
        invoke(ActionId.DOCUMENT_SEND_KEY, data);
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
        final Long oldSelection = session.getSelectedDocumentId();
		session.setSelectedDocumentId(documentId);

        if(null != oldSelection && !oldSelection.equals(documentId)) {
            clearStatusMessage(Status.Area.CUSTOM);
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

    /** Display the browser's status. */
    void displayStatusAvatar() {
        displayAvatar(DisplayId.STATUS, AvatarId.STATUS);
    }

    /**
	 * Display the browser's title avatar.
	 *
	 */
	void displayTitleAvatar() {
    	displayAvatar(DisplayId.TITLE, AvatarId.BROWSER_TITLE);
	}

	private void clearStatusMessage(final Status.Area area) {
        final Status status = getStatusAvatar();
        if(null != status) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() { status.clearStatusMessage(area); }
            });
        }
    }

    /**
     * Open a confirmation dialogue.
     * 
     * @param input
     *            The dialogue's input.
     * @return True if the user confirmed.
     */
    private Boolean confirm(final Data input) {
        open(WindowId.CONFIRM, AvatarId.CONFIRM_DIALOGUE, input);
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
     * Obtain the confirmation avatar.
     * @return The confirmation avatar.
     */
    private ConfirmDialog getConfirmAvatar() {
        return (ConfirmDialog) avatarRegistry.get(AvatarId.CONFIRM_DIALOGUE);
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

	/**
     * Convenience method to obtain the status avatar.
     * 
     * @return The status avatar.
     */
    private Status getStatusAvatar() {
        return (Status) avatarRegistry.get(AvatarId.STATUS);
    }

    /**
     * Convenience method to obtain the title avatar.
     * 
     * @return The title avatar.
     */
    private BrowserTitleAvatar getTitleAvatar() {
        return (BrowserTitleAvatar) avatarRegistry.get(AvatarId.BROWSER_TITLE);
    }

	private void invoke(final ActionId actionId, final Data data) {
		try {
			final AbstractAction action = getActionFromCache(actionId);
			action.invoke(data);
		}
		catch(final Exception x) { throw new RuntimeException(x); }
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
     * Set a custom status message.
     * 
     * @param messageKey
     *            The status message key.
     */
    private void setCustomStatusMessage(final String messageKey) {
        setStatusMessage(Status.Area.CUSTOM, messageKey);
    }

    /**
     * Set a filter status message.
     * 
     * @param messageKey
     *            The filter message key.
     */
    private void setFilterStatusMessage(final String messageKey) {
        setStatusMessage(Status.Area.FILTER, messageKey);
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

    private void setStatusMessage(final Status.Area area, final String messageKey) {
        final Avatar avatar = getStatusAvatar();
        if(null != avatar) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ((Status) avatar).reloadStatusMessage(area, messageKey);
                }
            });
        }
    }
}

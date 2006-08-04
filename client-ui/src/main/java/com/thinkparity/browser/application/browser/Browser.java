/*
 * Jan 18, 2006
 */
package com.thinkparity.browser.application.browser;

import java.awt.Dimension;
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
import com.thinkparity.browser.application.browser.display.avatar.*;
import com.thinkparity.browser.application.browser.display.avatar.contact.ContactInfo;
import com.thinkparity.browser.application.browser.display.avatar.container.ManageTeam;
import com.thinkparity.browser.application.browser.display.avatar.container.NewContainerDialogue;
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
import com.thinkparity.browser.platform.action.contact.CreateInvitation;
import com.thinkparity.browser.platform.action.contact.DeleteContact;
import com.thinkparity.browser.platform.action.contact.OpenContact;
import com.thinkparity.browser.platform.action.container.CreateContainer;
import com.thinkparity.browser.platform.action.container.CreateDraft;
import com.thinkparity.browser.platform.action.container.ManageContainerTeam;
import com.thinkparity.browser.platform.action.document.*;
import com.thinkparity.browser.platform.action.session.AcceptInvitation;
import com.thinkparity.browser.platform.action.session.DeclineInvitation;
import com.thinkparity.browser.platform.action.system.message.DeleteSystemMessage;
import com.thinkparity.browser.platform.application.ApplicationId;
import com.thinkparity.browser.platform.application.ApplicationStatus;
import com.thinkparity.browser.platform.application.L18nContext;
import com.thinkparity.browser.platform.application.dialog.ConfirmDialog;
import com.thinkparity.browser.platform.application.dialog.ErrorDialog;
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
import com.thinkparity.model.xmpp.user.User;

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

	/** The browser's connection. */
    private Connection connection;

    /** The currently selected tab. */
	private BrowserTitleTab currentTab = BrowserTitleTab.NONE;
	
	/** The browser's event dispatcher. */
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
        getContainersAvatar().applyKeyHolderFilter(keyHolder);
    }

	/**
     * Set the search results in the search filter and apply it to the current
     * list.
     * 
     * @param searchResult
     *            The search results.
     */
    // TO DO Fix this.
	public void applySearchFilter(final List<IndexHit> searchResult) {
        if (getCurrentTab() == BrowserTitleTab.CONTAINERS) {
            getContainersAvatar().applySearchFilter(searchResult);            
        }
        else if (getCurrentTab() == BrowserTitleTab.CONTACTS) {
            getContactsAvatar().applySearchFilter(searchResult);
        }
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
        getContainersAvatar().applyStateFilter(state);
    }

    /**
     * Clear the non-search filters for the containers list.
     *
     * @see #removeSearchFilter()
     */
    public void clearFilters() { getContainersAvatar().clearFilters(); }

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
	public void debugMain() { getContainersAvatar().debug(); }
    
    // TO DO fix up
    public void displayAddNewDocumentTeamMember(final Long documentId) {
        setInput(AvatarId.ADD_TEAM_MEMBER, documentId);
        displayAvatar(WindowId.POPUP, AvatarId.ADD_TEAM_MEMBER);
    }

    /**
     * Display the manage team dialog.
     * 
     * @param containerId
     *            A container id.
     */
    public void displayManageTeam(final Long containerId) {
        final Data input = new Data(1);
        input.set(ManageTeam.DataKey.CONTAINER_ID, containerId);
        setInput(AvatarId.MANAGE_TEAM, input);
        displayAvatar(WindowId.POPUP, AvatarId.MANAGE_TEAM);        
    }

    /**
     * Display the invite dialogue.
     *
     */   
    public void displayContactCreateInvitation() {
        // TODO fix up where this is called from, delete displaySessionInvitePartner()
        // Use INVITE
        displayAvatar(WindowId.POPUP, AvatarId.CONTACT_ADD);
    }

    /**
     * Display the contact info dialogue.
     *
     * @param contactId
     *            A contact id.
     */
    public void displayContactInfoDialogue(JabberId contactId) {
        final Data input = new Data(1);
        input.set(ContactInfo.DataKey.CONTACT_ID, contactId);
        setInput(AvatarId.CONTACT_INFO_DIALOGUE, input);
        displayAvatar(WindowId.POPUP, AvatarId.CONTACT_INFO_DIALOGUE);        
    }

    /** Display the contact search dialogue. */
    // TO DO Does this go away?
	public void displayContactSearch() {
		displayAvatar(WindowId.POPUP, AvatarId.SESSION_SEARCH_PARTNER);
	}

    /**
     * Display the "new container" dialog (to create new packages).
     * If the user presses OK, runCreateContainer() is called and
     * provided with the container name.
     * 
     */
    public void displayNewContainerDialog() {
        final Integer numFiles = 0;
        final Data input = new Data(1);
        input.set(NewContainerDialogue.DataKey.NUM_FILES, numFiles);
        setInput(AvatarId.NEW_CONTAINER_DIALOGUE, input);
        displayAvatar(WindowId.POPUP, AvatarId.NEW_CONTAINER_DIALOGUE);
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
    public void displayNewContainerDialog(final List<File> files) {
        final Integer numFiles = files.size();
        final Data input = new Data(2);
        input.set(NewContainerDialogue.DataKey.NUM_FILES, numFiles);
        input.set(NewContainerDialogue.DataKey.FILES, files);        
        setInput(AvatarId.NEW_CONTAINER_DIALOGUE, input);
        displayAvatar(WindowId.POPUP, AvatarId.NEW_CONTAINER_DIALOGUE);
    }

    /**
     * Display a document rename dialog.
     * 
     * @param containerId
     *            A container id.
     * @param documentId
     *            A document id.
     * @param documentName
     *            A document name.
     */
    public void displayRenameDocument(final Long containerId, final Long documentId,
            final String documentName) {
        final Data input = new Data(3);
        input.set(RenameDialog.DataKey.CONTAINER_ID, containerId);
        input.set(RenameDialog.DataKey.DOCUMENT_ID, documentId);
        input.set(RenameDialog.DataKey.DOCUMENT_NAME, documentName);
        setInput(AvatarId.RENAME_DIALOGUE, input);
        displayAvatar(WindowId.RENAME, AvatarId.RENAME_DIALOGUE);
    }
    
    /**
     * Display send version.
     * 
     */
    // TO DO Does this go away? Or get used?
	public void displaySendVersion(final Long artifactId, final Long versionId) {
		final Data input = new Data(2);
		input.set(SessionSendVersion.DataKey.ARTIFACT_ID, artifactId);
		input.set(SessionSendVersion.DataKey.VERSION_ID, versionId);
		displayAvatar(WindowId.POPUP, AvatarId.SESSION_SEND_VERSION, input);
	}
    
    /**
	 * Display the invite partner dialogue.
	 *
	 */
	public void displaySessionInvitePartner() {
		displayAvatar(WindowId.POPUP, AvatarId.SESSION_INVITE_PARTNER);
	}
    
    /**
     * Display the containers (packages) tab. If "forced" is true then
     * force the tab to draw and force the correct tab icon to be drawn.
     * 
     * @param forced
     *            True to force drawing.
     */
    public void displayContainersTab(Boolean forced) {
        if (forced || (getCurrentTab() != BrowserTitleTab.CONTAINERS)) {
            setCurrentTab(BrowserTitleTab.CONTAINERS);
            displayContainerListAvatar();
        }
        
        getTitleAvatar().updateTabIcon(BrowserTitleTab.CONTAINERS);
    }
    
    /**
     * Display the containers (packages) tab.
     */
    public void displayContainersTab() {
        if (getCurrentTab() != BrowserTitleTab.CONTAINERS) {
            setCurrentTab(BrowserTitleTab.CONTAINERS);
            displayContainerListAvatar();            
        }
    }
    
    /**
     * Display the contacts tab
     */
    public void displayContactsTab() {
        if (getCurrentTab() != BrowserTitleTab.CONTACTS) {
            setCurrentTab(BrowserTitleTab.CONTACTS);
            displayContactListAvatar();            
        }
    }
    
    /**
	 * Display the manage contacts dialogue.
	 *
	 */
	public void displaySessionManageContacts() {
        // TO DO fix up! Change this method and set up tabs properly
        //displayAvatar(WindowId.POPUP, AvatarId.SESSION_MANAGE_CONTACTS);
        if (getCurrentTab() == BrowserTitleTab.CONTAINERS) {
            setCurrentTab(BrowserTitleTab.CONTACTS);
            displayContactListAvatar();
        }
        else {
            setCurrentTab(BrowserTitleTab.CONTAINERS);
            displayContainerListAvatar();
        }
        // DOCUMENTS_TAB disabled
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
        setCustomStatusMessage("ContactAdded");
        // refresh the contact list
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { getContactsAvatar().syncContact(contactId, remote); }
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
        setCustomStatusMessage("ContactDeleted");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ((BrowserInfoAvatar) avatarRegistry.get(AvatarId.BROWSER_INFO))
                        .reload();
            }
        });
        // refresh the contact list
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ((BrowserContactsAvatar) avatarRegistry
                        .get(AvatarId.BROWSER_CONTACTS)).syncContact(contactId,
                        Boolean.FALSE);
            }
        });
    }

	/**
     * Notify the application that a contact has been invited.
     * 
     * @param contactId
     *            The contact id.
     */
    public void fireContactInvitationCreated(final Long invitationId, final Boolean remote) {
        setCustomStatusMessage("ContactInvitationCreated");
        // refresh the contact list
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { getContactsAvatar().syncInvitation(invitationId, remote); }
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
        setCustomStatusMessage("ContainerClosed");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { getContainersAvatar().syncContainer(containerId, remote); }
        });
    }
   
    /**
     * Notify the application a container confirmation has been received.
     *
     * @param containerId
     *            The container id.
     */
    public void fireContainerConfirmationReceived(final Long containerId) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { getContainersAvatar().syncContainer(containerId, Boolean.FALSE); }
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
        setCustomStatusMessage("ContainerCreated");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { getContainersAvatar().syncContainer(containerId, remote); }
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
        setCustomStatusMessage("ContainerDeleted");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { getContainersAvatar().syncContainer(containerId, remote); }
        });
    }
    
    /**
     * Notify the application that a container has been received (ie. package published)
     *
     * @param containerId
     *            The container id.
     */
    public void fireContainerReceived(final Long containerId) {
        setCustomStatusMessage("ContainerReceived");

        // flag it as not having been seen
        final ArtifactModel aModel = getArtifactModel();
        aModel.removeFlagSeen(containerId);

        // refresh the container in the main list
        SwingUtilities.invokeLater(new Runnable() {
                public void run() { getContainersAvatar().syncContainer(containerId, Boolean.TRUE); }
        });
    }
    
    /**
     * Notify the application a team member has been added to the container.
     *
     * @param containerId
     *            The container id.
     */
    public void fireContainerTeamMemberAdded(final Long containerId) {
        setCustomStatusMessage("ContainerTeamMemberAdded");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { getContainersAvatar().syncContainer(containerId, Boolean.TRUE); }
        });
    }

    /**
     * Notify the application a team member has been removed from the document.
     *
     * @param containerId
     *            The container id.
     */
    public void fireContainerTeamMemberRemoved(final Long containerId) {
        setCustomStatusMessage("ContainerTeamMemberRemoved");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { getContainersAvatar().syncContainer(containerId, Boolean.TRUE); }
        });
    }
    
    /**
     * Notify the application that the team has changed.
     * 
     * @param containerId
     *            The container id.
     * @param remote
     *            True if the action was the result of a remote event; false if
     *            the action was a local event.   
     */
    public void fireContainerTeamChanged(final Long containerId, final Boolean remote) {
        setCustomStatusMessage("ContainerTeamChanged");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { getContainersAvatar().syncContainer(containerId, remote); }
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
        setCustomStatusMessage("ContainerDraftCreated");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { getContainersAvatar().syncContainer(containerId, remote); }
        });         
    }

    /**
     * Notify the application that a container has in some way been updated.
     *
     * @param containerId
     *            The container that has changed.
     */
    public void fireContainerUpdated(final long containerId) {
        fireContainerUpdated(containerId, Boolean.FALSE);
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
    public void fireContainerUpdated(final Long containerId, final Boolean remoteReload) {
        setCustomStatusMessage("ContainerUpdated");
        // refresh the container in the main list
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ((BrowserContainersAvatar) avatarRegistry.get(AvatarId.BROWSER_CONTAINERS)).syncContainer(containerId, remoteReload);
            }
        });        
    }
    
    /**
     * Notify the application that a document has been created.
     * 
     * @param containerId
     *            The container id.
     * @param documentId
     *            The document id.
     * @param remote
     *            True if the action was the result of a remote event; false if
     *            the action was a local event.
     */
    public void fireDocumentCreated(final long containerId, final Long documentId, final Boolean remote) {
        setCustomStatusMessage("DocumentCreated");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { getContainersAvatar().syncDocument(containerId, documentId, remote); }
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
    public void fireDocumentDeleted(final long containerId, final Long documentId, final Boolean remote) {
        setCustomStatusMessage("DocumentDeleted");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { getContainersAvatar().syncDocument(containerId, documentId, remote); }
        });
    }
    
    /**
	 * Notify the application that a document has been deleted.
	 * 
	 * @param documentId
	 *            The document id.
	 */
//  qqq
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
    
    //  qqq
    public void fireDocumentUpdated(final Long documentId) {
        fireDocumentUpdated(documentId, Boolean.FALSE);
    }    
       
    //  qqq
    public void fireDocumentUpdated(final Long documentId, final Boolean remoteReload) {
        setCustomStatusMessage("DocumentUpdated");
        // refresh the document in the main list
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ((BrowserMainAvatar) avatarRegistry.get(AvatarId.BROWSER_MAIN)).syncDocument(documentId, remoteReload);
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
    public void fireDocumentUpdated(final Long containerId, final Long documentId) {
        fireDocumentUpdated(containerId, documentId, Boolean.FALSE);
    }

    /**
     * Notify the application that a document has in some way been updated.
     *
     * @param containerId
     *            The container id.
     * @param documentId
     *            The document that has changed.
     * @param remote
     *            True if the action was the result of a remote event; false if
     *            the action was a local event. 
     */
    public void fireDocumentUpdated(final Long containerId, final Long documentId, final Boolean remoteReload) {
        setCustomStatusMessage("DocumentUpdated");
        // refresh the document in the main list
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getContainersAvatar().syncDocument(containerId, documentId, remoteReload);
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
/**
     * Notify the application that a system message has been created.
     * 
     * @param systemMessageId
     *            The system message id.
     */
	public void fireSystemMessageCreated(final Long systemMessageId) {
		// refresh the system message in the main list
/*		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				((BrowserMainAvatar) avatarRegistry.get(AvatarId.BROWSER_MAIN)).reloadSystemMessage(systemMessageId);
			}
		});
*/
	}

    /**
     * Notify the application that a system message has been deleted.
     * 
     * @param systemMessageId
     *            The system message id.
     */
	public void fireSystemMessageDeleted(final Long systemMessageId) {
		// refresh the system message in the main list
/*		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				((BrowserMainAvatar) avatarRegistry.get(AvatarId.BROWSER_MAIN)).reloadSystemMessage(systemMessageId);
			}
		});
*/
	}

    /**
     * @see com.thinkparity.browser.platform.application.Application#getConnection()
     */
    public Connection getConnection() { return connection; }

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
        if(null == getContainersAvatar()) { return Boolean.FALSE; }
        return getContainersAvatar().isFilterEnabled();
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
        getContainersAvatar().removeKeyHolderFilter();
    }

    /**
     * Remove the search filter from the current list.
     *
     */
    // TO DO Fix this.
    public void removeSearchFilter() {
        if (getCurrentTab() == BrowserTitleTab.CONTAINERS) {
            getContainersAvatar().removeSearchFilter();
        }
        else if (getCurrentTab() == BrowserTitleTab.CONTACTS) {
            getContactsAvatar().removeSearchFilter();
        }
    }

	/**
     * Remove the artifact state filter.
     * 
     * @see BrowserMainAvatar#removeStateFilter()
     */
    public void removeStateFilter() { getContainersAvatar().removeStateFilter(); }

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
	public void runAcceptContactInvitation(final Long systemMessageId) {
		final Data data = new Data(1);
		data.set(AcceptInvitation.DataKey.SYSTEM_MESSAGE_ID, systemMessageId);
		invoke(ActionId.SESSION_ACCEPT_INVITATION, data);
	}

	/**
	 * Run the accept key request action.
	 *
     * @param artifactId
     *            The artifact id.
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
     * Run the add contact action.
     *
     */
    public void runAddContact() {
        runAddContact(null);
    }
    
    /**
     * Run the add contact action.
     * 
     * @param newContactEmail
     *              New contact email.
     */
    public void runAddContact(final String newContactEmail) {
        final Data data = new Data(1);
        if(null != newContactEmail)
            data.set(CreateInvitation.DataKey.CONTACT_EMAIL, newContactEmail);
        invoke(ActionId.CONTACT_ADD, data);
    }

    /** Add a team member to the selected container. */
    // TO DO Remove dependency on selected container id?
    public void runAddNewDocumentTeamMember() {
        runAddNewDocumentTeamMember(session.getSelectedContainerId(), (JabberId) null);
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
     * Run the create container (package) action. The user will
     * determine the container name.
     * 
     */
    public void runCreateContainer() {
        // Note: passing null for data or null for NAME will crash.
        final int numFiles = 0;
        final Data data = new Data(2);
        data.set(CreateContainer.DataKey.NAME, "");
        data.set(CreateContainer.DataKey.NUM_FILES, numFiles);
        invoke(ActionId.CONTAINER_CREATE, data);
    }
    
    /**
     * Create a container (package) with one or more new documents.
     * The user will determine the container name.
     * 
     * @param files
     *          List of files that will be added later      
     */
    public void runCreateContainer(final List<File> files) {
        final Integer numFiles = files.size();
        final Data data = new Data(3);
        data.set(CreateContainer.DataKey.NAME, "");
        data.set(CreateContainer.DataKey.NUM_FILES, numFiles);        
        data.set(CreateContainer.DataKey.FILES, files);
        invoke(ActionId.CONTAINER_CREATE, data);
    }      
    
    /**
     * Create a container (package) with a specified name.
     * 
     * @param name
     *            The container name.
     */
    public void runCreateContainer(final String name) {
        final int numFiles = 0;
        final Data data = new Data(2);
        data.set(CreateContainer.DataKey.NAME, name);
        data.set(CreateContainer.DataKey.NUM_FILES, numFiles);
        invoke(ActionId.CONTAINER_CREATE, data);
    }

    /**
     * Create a container (package) with a specified name and with
     * one or more new documents.
     * 
     * @param name
     *            The container name.
     * @param files
     *          List of files that will be added later
     */
    public void runCreateContainer(final String name, final List<File> files) {
        final Integer numFiles = files.size();
        final Data data = new Data(3);
        data.set(CreateContainer.DataKey.NAME, name);
        data.set(CreateContainer.DataKey.NUM_FILES, numFiles);        
        data.set(CreateContainer.DataKey.FILES, files);
        invoke(ActionId.CONTAINER_CREATE, data);
    }
    
    /**
     * Manage the team for the container. The user will set team members
     * in a dialog.
     * 
     * @param containerId
     *            The container id.
     */
    public void runManageTeam(final Long containerId) {
        final Data data = new Data(1);
        data.set(ManageContainerTeam.DataKey.CONTAINER_ID, containerId);
        invoke(ActionId.MANAGE_TEAM, data);           
    }
    
    /**
     * Manage the team for the container. Team members are specified
     * as a parameter.
     *
     * @param containerId
     *            The container id.
     * @param teamMembers
     *            List of team members      
     */
    public void runManageTeam(final Long containerId, final List<User> teamMembers) {
        final Data data = new Data(2);
        data.set(ManageContainerTeam.DataKey.CONTAINER_ID, containerId);
        data.set(ManageContainerTeam.DataKey.TEAM_MEMBERS, teamMembers);
        invoke(ActionId.MANAGE_TEAM, data);
    }
    
    /**
     * Create a draft for the container.
     * 
     * @param containerId
     *            The container id.
     */
    public void runCreateDraft(final Long containerId) {
        final Data data = new Data(1);
        data.set(CreateDraft.DataKey.CONTAINER_ID, containerId);
        invoke(ActionId.CREATE_DRAFT, data);         
    }
    
    /**
     * Run the create document action, browse to select the document.
     * 
     * @param containerId
     *            The container id.
     *
     */
    public void runCreateDocument(final Long containerId) {
        if(JFileChooser.APPROVE_OPTION == getJFileChooser().showOpenDialog(mainWindow)) {
            runCreateDocument(containerId, jFileChooser.getSelectedFile());
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
    public void runCreateDocument(final Long containerId, final File file) {
        final Data data = new Data(2);
        data.set(Create.DataKey.FILE, file);
        data.set(Create.DataKey.CONTAINER_ID, containerId);
        invoke(ActionId.DOCUMENT_CREATE, data);        
    }    

    /**
     * Create multiple documents.
     * 
     * @param containerId
     *            The container id.
     * @param files
     *            The files.
     */
    public void runCreateDocuments(final Long containerId, final List<File> files) {
        final Data data = new Data(2);
        data.set(CreateDocuments.DataKey.FILES, files);
        data.set(CreateDocuments.DataKey.CONTAINER_ID, containerId);
        invoke(ActionId.CREATE_DOCUMENTS, data);
    }

    /**
     * Run the decline all key requests action.
     *
     * @param artifactId
     *            The artifact id.
     */
    public void runDeclineAllKeyRequests(final Long artifactId) {
        final Data data = new Data(1);
        data.set(DeclineAllKeyRequests.DataKey.ARTIFACT_ID, artifactId);
        invoke(ActionId.ARTIFACT_DECLINE_ALL_KEY_REQUESTS, data);
    }

	/**
     * Decline an invitation.
     * 
     * @param systemMessageId
     *            The system message id.
     */
    public void runDeclineContactInvitation(final Long systemMessageId) {
        final Data data = new Data(1);
        data.set(DeclineInvitation.DataKey.SYSTEM_MESSAGE_ID, systemMessageId);
        invoke(ActionId.SESSION_DECLINE_INVITATION, data);
    }

    /**
     * Run the decline key request action.
     * 
     * @param artifactId
     *            The artifact id.
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
     * Run the delete contact action.
     * 
     * @param contactId
     *            The contact id.
     */
    public void runDeleteContact(final JabberId contactId) {
        Assert.assertNotNull("Cannot delete null contact.", contactId);
        final Data data = new Data(1);
        data.set(DeleteContact.DataKey.CONTACT_ID, contactId);
        invoke(ActionId.CONTACT_DELETE, data);        
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
     * Run the delete system message action
     * 
     * @param systemMessageId
     *              The system message id.
	 */
	public void runDeleteSystemMessage(final Long systemMessageId) {
		final Data data = new Data(1);
		data.set(DeleteSystemMessage.DataKey.SYSTEM_MESSAGE_ID, systemMessageId);
		invoke(ActionId.SYSTEM_MESSAGE_DELETE, data);
	} 
    
    /**
     * Run the key requested action.
     * 
     * @param artifactId
     *            The artifact id.
     */
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
     * Run the open contact action.
     * 
     * @param contactId
     *            The contact id.
     */
    public void runOpenContact(final JabberId contactId) {
        Assert.assertNotNull("Cannot open null contact.", contactId);
        final Data data = new Data(1);
        data.set(OpenContact.DataKey.CONTACT_ID, contactId);
        invoke(ActionId.CONTACT_OPEN, data);
    }        
    
    /**
	 * Run the open document action.
	 * 
	 * @param documentId
	 *            The document id.
	 */
	public void runOpenDocument(final Long containerId, final Long documentId) {
        Assert.assertNotNull("Cannot open document in null container.", containerId);
        Assert.assertNotNull("Cannot open null document.", documentId);
		final Data data = new Data(2);
        data.set(Open.DataKey.CONTAINER_ID, containerId);
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
     * Run the document reactivate action.
     * 
     * @param documentId
     *            A document id.
     */
    public void runReactivateDocument(final Long documentId) {
        final Data data = new Data(1);
        data.set(Reactivate.DataKey.DOCUMENT_ID, documentId);
        invoke(ActionId.DOCUMENT_REACTIVATE, data);
    }

    /**
     * Run the document rename action.
     * 
     * @param containerId
     *            A container id.
     * @param documentId
     *            A document id.
     */
    public void runRenameDocument(final Long containerId, final Long documentId) {
        final Data data = new Data(2);
        data.set(Rename.DataKey.CONTAINER_ID, containerId);
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
    public void runRenameDocument(final Long containerId, final Long documentId,
            final String documentName) {
        final Data data = new Data(3);
        data.set(Rename.DataKey.CONTAINER_ID, containerId);        
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
     * Select a contact.
     * 
     * @param contactId
     *            The contact id.
     */
    public void selectContact(final JabberId contactId) {
        final JabberId oldSelection = session.getSelectedContactId();
        session.setSelectedContactId(contactId);

        if(null != oldSelection && !oldSelection.equals(contactId)) {
            clearStatus(BrowserStatusArea.CUSTOM);
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
            clearStatus(BrowserStatusArea.CUSTOM);
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

    /**
     * Handle a user error (show an error dialog).
     * 
     * @param messageKey
     *            The error message localization key.
     */
    public void userError(final String messageKey) {
        final Data input = new Data(1);
        input.set(ErrorDialog.DataKey.MESSAGE_KEY, messageKey);
        open(WindowId.ERROR, AvatarId.ERROR_DIALOGUE, input);
    }

    /**
     * Handle a user error (show an error dialog).
     *
     * @param messageKey
     *            The error message localization key.
     * @param messageArguments
     *            Message arguments.
     */
    public void userError(final String messageKey,
            final Object[] messageArguments) {
        final Data input = new Data(2);
        input.set(ErrorDialog.DataKey.MESSAGE_KEY, messageKey);
        input.set(ErrorDialog.DataKey.MESSAGE_ARGUMENTS, messageArguments);
        open(WindowId.ERROR, AvatarId.ERROR_DIALOGUE, input);
    }
    
    /**
     * Display the contact list.
     *
     */
    void displayContactListAvatar() {
        displayAvatar(DisplayId.CONTENT, AvatarId.BROWSER_CONTACTS);
    }

    /**
     * Display the container list.
     * 
     */
    void displayContainerListAvatar() {
        displayAvatar(DisplayId.CONTENT, AvatarId.BROWSER_CONTAINERS);
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
     * Display the content avatar.
     *
     */
    void displayContentAvatar() {
        displayAvatar(DisplayId.CONTENT, AvatarId.CONTENT);
    }

    /**
	 * Display the browser's title avatar.
	 *
	 */
	void displayTitleAvatar() {
    	displayAvatar(DisplayId.TITLE, AvatarId.BROWSER_TITLE);
	}

    /** Notify the session has been terminated. */
    void fireConnectionOffline() {
        connection = Connection.OFFLINE;
        setStatus(BrowserStatusArea.CONNECTION, "ConnectionOffline");
    }

    /** Notify the session has been established. */
    void fireConnectionOnline() {
        connection = Connection.ONLINE;
        setStatus(BrowserStatusArea.CONNECTION, "ConnectionOnline");
    }

    /**
     * Clear any status messages.
     * 
     * @param area
     *            The status area.
     */
    private void clearStatus(final BrowserStatusArea area) {
        final BrowserStatus browserStatus = getBrowserStatus();
        if(null != browserStatus) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() { browserStatus.clear(area); }
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
     * Convenience method to obtain the status avatar.
     * 
     * @return The status avatar.
     */
    private BrowserStatus getBrowserStatus() {
        return (BrowserStatus) avatarRegistry.get(AvatarId.STATUS);
    }

    /**
     * Obtain the confirmation avatar.
     * @return The confirmation avatar.
     */
    private ConfirmDialog getConfirmAvatar() {
        return (ConfirmDialog) avatarRegistry.get(AvatarId.CONFIRM_DIALOGUE);
    }
    
    /**
     * Convenience method to obtain the contacts avatar.
     * 
     * @return The contacts avatar.
     */
    private BrowserContactsAvatar getContactsAvatar() {
        return (BrowserContactsAvatar) avatarRegistry.get(AvatarId.BROWSER_CONTACTS);
    }

    /**
     * Convenience method to obtain the container list avatar.
     * 
     * @return The containers avatar.
     */
    private BrowserContainersAvatar getContainersAvatar() {
        return (BrowserContainersAvatar) avatarRegistry.get(AvatarId.BROWSER_CONTAINERS);
    }
    
    /**
     * Convenience method to obtain the browser title avatar.
     * 
     * @return The containers avatar.
     */
    private BrowserTitle getTitleAvatar() {
        return (BrowserTitle) avatarRegistry.get(AvatarId.BROWSER_TITLE);
    }

    /**
     * Convenience method to get the current tab.
     * 
     * @return The current tab.
     */
	private BrowserTitleTab getCurrentTab() {
        return currentTab;
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
     * Set the current tab.
     */
    private void setCurrentTab(BrowserTitleTab currentTab) {
        this.currentTab = currentTab;
    }

    /**
     * Set a custom status message.
     * 
     * @param messageKey
     *            The status message key.
     */
    private void setCustomStatusMessage(final String messageKey) {
        setStatus(BrowserStatusArea.CUSTOM, messageKey);
    }

    /**
     * Set a filter status message.
     * 
     * @param messageKey
     *            The filter message key.
     */
    private void setFilterStatusMessage(final String messageKey) {
        setStatus(BrowserStatusArea.FILTER, messageKey);
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
     * Set a status message.
     * 
     * @param area
     *            The status area.
     * @param messageKey
     *            The status message key.
     */
    private void setStatus(final BrowserStatusArea area,
            final String messageKey) {
        final BrowserStatus browserStatus = getBrowserStatus();
        if(null != browserStatus) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    browserStatus.setMessage(area, messageKey);
                }
            });
        }
    }
}

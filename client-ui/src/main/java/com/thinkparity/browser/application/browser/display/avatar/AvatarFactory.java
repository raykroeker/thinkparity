/*
 * Created On: Jan 20, 2006
 * $Id$
 */
package com.thinkparity.browser.application.browser.display.avatar;

import com.thinkparity.browser.application.browser.display.avatar.contact.AddContact;
import com.thinkparity.browser.application.browser.display.avatar.contact.ContactInfo;
import com.thinkparity.browser.application.browser.display.avatar.contact.InvitePartner;
import com.thinkparity.browser.application.browser.display.avatar.contact.Manage;
import com.thinkparity.browser.application.browser.display.avatar.contact.SearchPartner;
import com.thinkparity.browser.application.browser.display.avatar.container.ManageTeam;
import com.thinkparity.browser.application.browser.display.avatar.container.NewContainerDialogue;
import com.thinkparity.browser.application.browser.display.avatar.document.RenameDialog;
import com.thinkparity.browser.application.browser.display.avatar.session.SessionSendVersion;
import com.thinkparity.browser.application.browser.display.provider.ProviderFactory;
import com.thinkparity.browser.platform.application.dialog.ConfirmDialog;
import com.thinkparity.browser.platform.application.dialog.ErrorDialog;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class AvatarFactory {

	/**
	 * The avatar singleton factory.
	 * 
	 */
	private static final AvatarFactory SINGLETON;

	static { SINGLETON = new AvatarFactory(); }

	/**
	 * Create an avatar.
	 * 
	 * @param id
	 *            The avatar id.
	 * @return The avatar.
	 */
	public static Avatar create(final AvatarId id) {
		return SINGLETON.doCreate(id);
	}

	/**
	 * The avatar registry.
	 * 
	 */
	private final AvatarRegistry avatarRegistry;
    
    /**
     * The container list avatar.
     * 
     */
    private Avatar browserContainers;

	/**
	 * The document list avatar.
	 * 
	 */
	private Avatar browserMain;
    
    /**
     * The contact list avatar.
     * 
     */
    private Avatar browserContacts;

	/**
	 * The session send form avatar.
	 * 
	 */
	private Avatar sessionSendForm;

	/**
	 * Create a AvatarFactory [Singleton, Factory]
	 * 
	 */
	private AvatarFactory() {
		super();
		this.avatarRegistry = new AvatarRegistry();
	}

	/**
	 * Create the browser info avatar.
	 * 
	 * @return The browser info avatar.
	 */
	private Avatar createBrowserInfo() {
		final Avatar browserInfo = new BrowserInfoAvatar();
		browserInfo.setContentProvider(ProviderFactory.getInfoProvider());
		return browserInfo;
	}
    
    /**
     * Create the container list avatar.
     * 
     * @return The container list avatar.
     */
    private Avatar createBrowserContainers() {
        if(null == browserContainers) {
            browserContainers = new BrowserContainersAvatar();
            browserContainers.setContentProvider(ProviderFactory.getContainersProvider());            
        }
        return browserContainers;
    }

	/**
	 * Create the document list avatar.
	 * 
	 * @return The document list avatar.
	 */
	private Avatar createBrowserMain() {
		if(null == browserMain) {
			browserMain = new BrowserMainAvatar();
			browserMain.setContentProvider(ProviderFactory.getMainProvider());
		}
		return browserMain;
	}
    
    /**
     * Create the contact list avatar.
     * 
     * @return The contact list avatar.
     */
    private Avatar createBrowserContacts() {
        if(null == browserContacts) {
            browserContacts = new BrowserContactsAvatar();
            browserContacts.setContentProvider(ProviderFactory.getManageContactsProvider());
        }
        return browserContacts;
    }

    /** Create the confirmation dialogue avatar. */
    private Avatar createConfirmDialogue() {
        return new ConfirmDialog();
    }
    
    /** Create an error dialogue avatar. */
    private Avatar createErrorDialogue() {
        return new ErrorDialog();
    }

    /**
     * Create the add contact avatar. Note; no provider required.
     * 
     * @return The avatar.
     */
    private Avatar createContactAdd() {
        final Avatar avatar = new AddContact();
        return avatar;
    }

	/**
     * Create the contact info dialogue avatar.
     * 
     * @return The contact info dialogue avatar.
     */
    private Avatar createContactInfoDialogue() {
        final Avatar contactInfoAvatar = new ContactInfo();
        contactInfoAvatar.setContentProvider(ProviderFactory.getContactInfoProvider());
        return contactInfoAvatar;       
    }

    /**
     * Create the invite form avatar.
     * 
     * @return The invite form avatar.
     */
    private Avatar createInvite() {
        final Avatar inviteAvatar = new InvitePartner();
        return inviteAvatar;
    }

    private Avatar createRenameDialogue() {
        return new RenameDialog();
    }

	private Avatar createSendVersion() {
		final Avatar avatar = new SessionSendVersion();
		avatar.setContentProvider(ProviderFactory.getSendVersionProvider());

		return avatar;
	}

	/**
     * Create the session invite contact avatar.
     * 
     * @return The session invite contact avatar.
     */
    private Avatar createSessionInvitePartner() {
        final Avatar sessionInvitePartner = new InvitePartner();
        return sessionInvitePartner;
    }

	/**
     * Create the manage contacts avatar.
     * 
     * @return The manage contacts avatar.
     */
	private Avatar createSessionManageContacts() {
		final Avatar sessionManageContacts = new Manage();
		sessionManageContacts.setContentProvider(ProviderFactory.getManageContactsProvider());
		return sessionManageContacts;
	}
    
    private Avatar createSessionSearchPartner() {
        final Avatar avatar = new SearchPartner();
        return avatar;
    }

    /**
	 * Create the session send form avatar.
	 * 
	 * @return The session send form avatar.
	 */
	private Avatar createSessionSendForm() {
		if(null == sessionSendForm) {
			sessionSendForm = new SessionSendFormAvatar();
			sessionSendForm.setContentProvider(ProviderFactory.getSendArtifactProvider());
		}
		return sessionSendForm;
	}
    
    /**
     * Create the new container dialogue avatar.
     * 
     * @return The new container dialogue avatar.
     */
    private Avatar createNewContainerDialogue() {
        final Avatar newContainerAvatar = new NewContainerDialogue();
        // No content provider required
        return newContainerAvatar;
    }
    
    /**
     * Create the manage team avatar.
     * 
     * @return The manage team avatar.
     */
    private Avatar createManageTeam() {
        final Avatar manageTeamAvatar = new ManageTeam();
        manageTeamAvatar.setContentProvider(ProviderFactory.getManageTeamProvider());
        return manageTeamAvatar;
    }

	/**
	 * Create an avatar and register it.
	 * 
	 * @param id
	 *            The avatar id.
	 * @return The avatar.
	 */
	private Avatar doCreate(final AvatarId id) {
		final Avatar avatar;
		switch(id) {
        case ADD_TEAM_MEMBER:
            avatar = new com.thinkparity.browser.application.browser.display.avatar.document.AddNewTeamMember();
            avatar.setContentProvider(ProviderFactory.getSendArtifactProvider());
            break;
		case BROWSER_INFO:
			avatar = createBrowserInfo();
			break;
        case BROWSER_CONTAINERS:
            avatar = createBrowserContainers();
            break;
		case BROWSER_MAIN:
			avatar = createBrowserMain();
			break;
        case BROWSER_CONTACTS:
            avatar = createBrowserContacts();
            break;
		case BROWSER_TITLE:
			avatar = new BrowserTitle();
			break;
        case CONFIRM_DIALOGUE:
            avatar = createConfirmDialogue();
            break;
        case ERROR_DIALOGUE:
            avatar = createErrorDialogue();
            break;
        case CONTACT_ADD:
            avatar = createContactAdd();
            break;
        case CONTENT:
            avatar = new BrowserContent();
            break;
        case RENAME_DIALOGUE:
            avatar = createRenameDialogue();
            break;
		case SESSION_INVITE_PARTNER:
			avatar = createSessionInvitePartner();
			break;
		case SESSION_MANAGE_CONTACTS:
			avatar = createSessionManageContacts();
			break;
        case SESSION_SEARCH_PARTNER:
            avatar = createSessionSearchPartner();
            break;
		case SESSION_SEND_FORM:
			avatar = createSessionSendForm();
			break;
		case SESSION_SEND_VERSION:
			avatar = createSendVersion();
			break;
        case CONTACT_INFO_DIALOGUE:
            avatar = createContactInfoDialogue();
            break;
        case NEW_CONTAINER_DIALOGUE:
            avatar = createNewContainerDialogue();
            break;
        case MANAGE_TEAM:
            avatar = createManageTeam();
            break;
        case INVITE:
            avatar = createInvite();
            break;
        case STATUS:
            avatar = new BrowserStatus();
            break;
		default: throw Assert.createUnreachable("Unknown avatar:  " + id);
		}
		register(avatar);
		return avatar;
	}

	/**
	 * Register an avatar in the registry.
	 * 
	 * @param avatar
	 *            The avatar to register.
	 */
	private void register(final Avatar avatar) {
		Assert.assertIsNull(
				"Avatar " + avatar.getId() + " already registered.",
				avatarRegistry.put(avatar.getId(), avatar));
	}
}

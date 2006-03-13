/*
 * Jan 20, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar;

import com.thinkparity.browser.application.browser.display.provider.ProviderFactory;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class AvatarFactory {

	private static final AvatarFactory SINGLETON;

	static { SINGLETON = new AvatarFactory(); }

	public static Avatar create(final AvatarId id) {
		switch(id) {
		case BROWSER_INFO:
			return SINGLETON.createBrowserInfo();
		case BROWSER_LOGO:
			return SINGLETON.createBrowserLogo();
		case BROWSER_MAIN:
			return SINGLETON.createBrowserMain();
		case BROWSER_TITLE:
			return SINGLETON.createBrowserTitle();
		case DOCUMENT_HISTORY:
			return SINGLETON.createDocumentHistoryList();
		case SESSION_INVITE_CONTACT:
			return SINGLETON.createSessionInviteContact();
		case SESSION_LOGIN:
			return SINGLETON.createSessionLogin();
		case SESSION_MANAGE_CONTACTS:
			return SINGLETON.createSessionManageContacts();
		case SESSION_SEND_FORM:
			return SINGLETON.createSessionSendForm();
		case SESSION_SEND_KEY_FORM:
			return SINGLETON.createSessionSendKeyForm();
		default: throw Assert.createUnreachable("Unknown avatar:  " + id);
		}
	}

	/**
	 * The avatar registry.
	 * 
	 */
	private final AvatarRegistry avatarRegistry;

	/**
	 * The browser info avatar.
	 * 
	 */
	private Avatar browserInfo;

	/**
	 * Browser logo avatar.
	 * 
	 */
	private Avatar browserLogo;

	/**
	 * The message list avatar.
	 * 
	 */
	private Avatar browserMain;

	/**
	 * The browser title avatar.
	 * 
	 */
	private Avatar browserTitle;

	/**
	 * The document history list avatar.
	 * 
	 */
	private Avatar documentHistoryList;

	/**
	 * The invite contact avatar.
	 * 
	 */
	private Avatar sessionInviteContact;

	/**
	 * The session login avatar.
	 * 
	 */
	private Avatar sessionLogin;

	/**
	 * The manage contacts avatar.
	 * 
	 */
	private Avatar sessionManageContacts;

	/**
	 * The session send form avatar.
	 * 
	 */
	private Avatar sessionSendForm;

	/**
	 * The sesssion send key form.
	 * 
	 */
	private Avatar sessionSendKeyForm;

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
		if(null == browserInfo) {
			browserInfo = new BrowserInfoAvatar();
			register(browserInfo);
		}
		return browserInfo;
	}

	/**
	 * Create the browser logo avatar.
	 * @return The browser logo avatar.
	 */
	private Avatar createBrowserLogo() {
		if(null == browserLogo) {
			browserLogo = new BrowserLogoAvatar();
			register(browserLogo);
		}
		return browserLogo;
	}

	/**
	 * Create the message list avatar.
	 * 
	 * @return The message list avatar.
	 */
	private Avatar createBrowserMain() {
		if(null == browserMain) {
			browserMain = new BrowserMainAvatar();
			browserMain.setContentProvider(ProviderFactory.getMainProvider());
			register(browserMain);
		}
		return browserMain;
	}

	/**
	 * Create the browser title avatar.
	 * 
	 * @return The browser title avatar.
	 */
	private Avatar createBrowserTitle() {
		if(null == browserTitle) {
			browserTitle = new BrowserTitleAvatar();
			register(browserTitle);
		}
		return browserTitle;
	}
	
	/**
	 * Create the document history list avatar.
	 * 
	 * @return the document history list avatar.
	 */
	private Avatar createDocumentHistoryList() {
		if(null == documentHistoryList) {
			documentHistoryList = new DocumentHistoryAvatar();
			documentHistoryList.setContentProvider(ProviderFactory.getHistoryProvider());
			register(documentHistoryList);
		}
		return documentHistoryList;
	}

	/**
	 * Create the session invite contact avatar.
	 * 
	 * @return The session invite contact avatar.
	 */
	private Avatar createSessionInviteContact() {
		if(null == sessionInviteContact) {
			sessionInviteContact = new SessionInviteContactAvatar();
			register(sessionInviteContact);
		}
		return sessionInviteContact;
	}

	/**
	 * Create the session login avatar.
	 * 
	 * @return The session login avatar.
	 */
	private Avatar createSessionLogin() {
		if(null == sessionLogin) {
			sessionLogin = new SessionLoginAvatar();
			register(sessionLogin);
		}
		return sessionLogin;
	}

	/**
	 * Create the manage contacts avatar.
	 * 
	 * @return The manage contacts avatar.
	 */
	private Avatar createSessionManageContacts() {
		if(null == sessionManageContacts) {
			sessionManageContacts = new ManageContactsAvatar();
			sessionManageContacts.setContentProvider(ProviderFactory.getManageContactsProvider());
			register(sessionManageContacts);
		}
		return sessionManageContacts;
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
			register(sessionSendForm);
		}
		return sessionSendForm;
	}

	/**
	 * Create the session send key form avatar.  Note that we are using the
	 * same form as send.
	 * 
	 * @return The session send key form avatar.
	 */
	private Avatar createSessionSendKeyForm() {
		if(null == sessionSendKeyForm) {
			sessionSendKeyForm = new SessionSendFormAvatar();
			sessionSendKeyForm.setContentProvider(ProviderFactory.getSendArtifactProvider());
			register(sessionSendKeyForm);
		}
		return sessionSendKeyForm;
	}

	/**
	 * Register an avatar in the registry.
	 * 
	 * @param avatar
	 *            The avatar to register.
	 */
	private void register(final Avatar avatar) {
		Assert.assertIsNull(
				"Avatar " + avatar.getId() + " already registerd.",
				avatarRegistry.put(avatar.getId(), avatar));
	}
}

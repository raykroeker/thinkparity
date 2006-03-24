/*
 * Jan 20, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar;

import com.thinkparity.browser.application.browser.display.provider.ProviderFactory;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.login.ui.LoginAvatar;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
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
	 * The invite contact avatar.
	 * 
	 */
	private Avatar sessionInviteContact;

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
		final Avatar browserInfo = new BrowserInfoAvatar();
		browserInfo.setContentProvider(ProviderFactory.getInfoProvider());
		return browserInfo;
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
		}
		return browserTitle;
	}

	/**
	 * Create the document history list avatar.
	 * 
	 * @return The document history list avatar.
	 */
	private Avatar createDocumentHistory3List() {
	    final Avatar a = new DocumentHistoryAvatar3();
	    a.setContentProvider(ProviderFactory.getHistoryProvider());
	    return a;
	}

	/**
	 * Create the platform login avatar.
	 * 
	 * @return The platform login avatar.
	 */
	private Avatar createPlatformLogin() {
		final Avatar platformLogin = new LoginAvatar();
		return platformLogin;
	}

	/**
	 * Create the session invite contact avatar.
	 * 
	 * @return The session invite contact avatar.
	 */
	private Avatar createSessionInviteContact() {
		if(null == sessionInviteContact) {
			sessionInviteContact = new SessionInviteContactAvatar();
		}
		return sessionInviteContact;
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
		}
		return sessionSendKeyForm;
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
		case BROWSER_INFO:
			avatar = createBrowserInfo();
			break;
		case BROWSER_MAIN:
			avatar = createBrowserMain();
			break;
		case BROWSER_TITLE:
			avatar = createBrowserTitle();
			break;
		case DOCUMENT_HISTORY3:
			avatar = createDocumentHistory3List();
			break;
		case PLATFORM_LOGIN:
			avatar = createPlatformLogin();
			break;
		case SESSION_INVITE_CONTACT:
			avatar = createSessionInviteContact();
			break;
		case SESSION_MANAGE_CONTACTS:
			avatar = createSessionManageContacts();
			break;
		case SESSION_SEND_FORM:
			avatar = createSessionSendForm();
			break;
		case SESSION_SEND_KEY_FORM:
			avatar = createSessionSendKeyForm();
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
				"Avatar " + avatar.getId() + " already registerd.",
				avatarRegistry.put(avatar.getId(), avatar));
	}
}

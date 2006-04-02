/*
 * Jan 20, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar;

import com.thinkparity.browser.application.browser.display.avatar.contact.Manage;
import com.thinkparity.browser.application.browser.display.avatar.history.HistoryItems;
import com.thinkparity.browser.application.browser.display.avatar.contact.InvitePartner;
import com.thinkparity.browser.application.browser.display.avatar.contact.SearchPartner;
import com.thinkparity.browser.application.browser.display.avatar.session.SessionSendVersion;
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
	    final Avatar a = new HistoryItems();
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
	private Avatar createSessionInvitePartner() {
		final Avatar sessionInvitePartner = new InvitePartner();
		return sessionInvitePartner;
	}

        private Avatar createSessionSearchPartner() {
            final Avatar avatar = new SearchPartner();
            return avatar;
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

	private Avatar createSendVersion() {
		final Avatar avatar = new SessionSendVersion();
		avatar.setContentProvider(ProviderFactory.getSendVersionProvider());

		return avatar;
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

/*
 * Jan 20, 2006
 */
package com.thinkparity.browser.applications.browser.display.avatar;

import com.thinkparity.browser.applications.browser.Controller;
import com.thinkparity.browser.applications.browser.display.provider.ProviderFactory;
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
		case BROWSER_LOGO:
			return SINGLETON.createBrowserLogo();
		case BROWSER_MAIN:
			return SINGLETON.createBrowserMain();
		case BROWSER_TITLE:
			return SINGLETON.createBrowserTitle();
		case DOCUMENT_HISTORY_LIST:
			return SINGLETON.createDocumentHistoryList();
		case SESSION_LOGIN:
			return SINGLETON.createSessionLogin();
		case SESSION_SEND_FORM:
			return SINGLETON.createSessionSendForm();
		case SYSTEM_MESSAGE:
			return SINGLETON.createSystemMessage();
		default: throw Assert.createUnreachable("Unknown avatar:  " + id);
		}
	}

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
	 * Main controller.
	 * 
	 */
	private final Controller controller;

	/**
	 * The document history list avatar.
	 * 
	 */
	private Avatar documentHistoryList;

	/**
	 * The session login avatar.
	 * 
	 */
	private Avatar sessionLogin;

	/**
	 * The session send form avatar.
	 * 
	 */
	private Avatar sessionSendForm;

	/**
	 * The system message avatar.
	 * 
	 */
	private Avatar systemMessage;

	/**
	 * Create a AvatarFactory [Singleton, Factory]
	 * 
	 */
	private AvatarFactory() {
		super();
		this.controller = Controller.getInstance();
	}

	/**
	 * Create the browser logo avatar.
	 * @return The browser logo avatar.
	 */
	private Avatar createBrowserLogo() {
		if(null == browserLogo) {
			browserLogo = new BrowserLogoAvatar();
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
	 * @return the document history list avatar.
	 */
	private Avatar createDocumentHistoryList() {
		if(null == documentHistoryList) {
			documentHistoryList = new DocumentHistoryListAvatar(controller);
			documentHistoryList.setContentProvider(ProviderFactory.getHistoryProvider());
		}
		return documentHistoryList;
	}

	/**
	 * Create the session login avatar.
	 * 
	 * @return The session login avatar.
	 */
	private Avatar createSessionLogin() {
		if(null == sessionLogin) {
			sessionLogin = new SessionLoginAvatar();
		}
		return sessionLogin;
	}

	/**
	 * Create the session send form avatar.
	 * 
	 * @return The session send form avatar.
	 */
	private Avatar createSessionSendForm() {
		if(null == sessionSendForm) {
			sessionSendForm = new SessionSendFormAvatar();
		}
		return sessionSendForm;
	}

	/**
	 * Create the system message avatar.
	 * 
	 * @return The system message avatar.
	 */
	private Avatar createSystemMessage() {
		if(null == systemMessage) {
			systemMessage = new SystemMessageAvatar();
			systemMessage.setContentProvider(ProviderFactory.getSystemMessageProvider());
		}
		return systemMessage;
	}
}

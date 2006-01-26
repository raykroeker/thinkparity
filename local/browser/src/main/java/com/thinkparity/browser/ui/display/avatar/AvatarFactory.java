/*
 * Jan 20, 2006
 */
package com.thinkparity.browser.ui.display.avatar;

import com.thinkparity.browser.Controller;
import com.thinkparity.browser.ui.display.provider.ProviderFactory;

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
		case DOCUMENT_LIST:
			return SINGLETON.createDocumentList();
		case DOCUMENT_HISTORY_LIST:
			return SINGLETON.createDocumentHistoryList();
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
	 * The document list avatar.
	 * 
	 */
	private Avatar documentList;

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
			browserMain = new BrowserMainAvatar(controller);
			browserMain.setContentProvider(ProviderFactory.getMainProvider());
		}
		return browserMain;
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
	 * Create the document list avatar.
	 * 
	 * @return The document list avatar.
	 */
	private Avatar createDocumentList() {
		if(null == documentList) {
			documentList = new DocumentListAvatar(controller);
			documentList.setContentProvider(ProviderFactory.getDocumentProvider());
		}
		return documentList;
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

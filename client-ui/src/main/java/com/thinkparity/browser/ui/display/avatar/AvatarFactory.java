/*
 * Jan 20, 2006
 */
package com.thinkparity.browser.ui.display.avatar;

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
		case DOCUMENT_LIST:
			return SINGLETON.createDocumentList();
		case DOCUMENT_HISTORY_LIST:
			return SINGLETON.createDocumentHistoryList();
		default: throw Assert.createUnreachable("Unknown avatar:  " + id);
		}
	}

	/**
	 * Browser logo avatar.
	 * 
	 */
	private Avatar browserLogo;

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
	 * Create a AvatarFactory [Singleton, Factory]
	 * 
	 */
	private AvatarFactory() { super(); }

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
	 * Create the document history list avatar.
	 * 
	 * @return the document history list avatar.
	 */
	private Avatar createDocumentHistoryList() {
		if(null == documentHistoryList) {
			documentHistoryList = new DocumentHistoryListAvatar();
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
			documentList = new DocumentListAvatar();
			documentList.setContentProvider(ProviderFactory.getDocumentProvider());
		}
		return documentList;
	}
}

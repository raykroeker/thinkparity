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
		case DOCUMENT_LIST:
			return SINGLETON.createDocumentList();
		default: throw Assert.createUnreachable("Unknown avatar:  " + id);
		}
	}

	/**
	 * Create a AvatarFactory [Singleton, Factory]
	 * 
	 */
	private AvatarFactory() { super(); }

	/**
	 * The document list avatar.
	 * 
	 */
	private Avatar documentList;

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

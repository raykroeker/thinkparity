/*
 * Jan 25, 2006
 */
package com.thinkparity.browser.ui.display.avatar;

import java.awt.GridBagLayout;

import com.thinkparity.browser.util.State;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 * 
 * NOTE System Messages [Add contact request, request for ownership, response to ownership request, support response]
 */
public class BrowserMessageListAvatar extends Avatar {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Info avatar helper.
	 * 
	 */
	private final InfoAvatarHelper helper;

	/**
	 * Create a BrowserMessageListAvatar.
	 * @param l18nContext
	 */
	public BrowserMessageListAvatar() {
		super("BrowserMessageListAvatar");
		this.helper = new InfoAvatarHelper(this);
		setLayout(new GridBagLayout());
		helper.addHeading(getString("Messages"));
	}

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#getId()
	 * 
	 */
	public AvatarId getId() { return AvatarId.BROWSER_MESSAGE_LIST; }

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#getState()
	 * 
	 */
	public State getState() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#setState(com.thinkparity.browser.util.State)
	 * 
	 */
	public void setState(State state) {
		// TODO Auto-generated method stub
	}
}

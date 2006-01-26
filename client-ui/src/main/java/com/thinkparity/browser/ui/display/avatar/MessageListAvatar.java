/*
 * Jan 25, 2006
 */
package com.thinkparity.browser.ui.display.avatar;

import java.awt.GridBagLayout;

import com.thinkparity.browser.Controller;
import com.thinkparity.browser.util.State;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 * 
 * NOTE System Messages [Add contact request, request for ownership, response to ownership request, support response]
 */
public class MessageListAvatar extends Avatar {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * The main controller.
	 * 
	 */
	private final Controller controller;

	/**
	 * Info avatar helper.
	 * 
	 */
	private final InfoAvatarHelper helper;

	/**
	 * Create a MessageListAvatar.
	 * @param l18nContext
	 */
	public MessageListAvatar(final Controller controller) {
		super("MessageListAvatar");
		this.controller = controller;
		this.helper = new InfoAvatarHelper(this);
		setLayout(new GridBagLayout());
		helper.addHeading(getString("Messages"));
	}

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#getId()
	 * 
	 */
	public AvatarId getId() { return AvatarId.MESSAGE_LIST; }

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

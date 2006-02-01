/*
 * Jan 30, 2006
 */
package com.thinkparity.browser.ui.display.avatar;

import java.awt.Color;

import com.thinkparity.browser.util.State;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SessionSendFormAvatar extends Avatar {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Create a SessionSendFormAvatar.
	 * 
	 */
	public SessionSendFormAvatar() {
		super("SessionSendForm", ScrollPolicy.NONE, Color.WHITE);

		initSessionSendFormComponents();
	}

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#getId()
	 * 
	 */
	public AvatarId getId() { return AvatarId.SESSION_SEND_FORM; }

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#getState()
	 * 
	 */
	public State getState() { return null; }

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#setState(com.thinkparity.browser.util.State)
	 * 
	 */
	public void setState(final State state) {}

	/**
	 * Initialize the send form.
	 *
	 */
	private void initSessionSendFormComponents() {}
}

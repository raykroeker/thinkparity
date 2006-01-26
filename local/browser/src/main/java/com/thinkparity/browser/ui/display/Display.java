/*
 * Jan 20, 2006
 */
package com.thinkparity.browser.ui.display;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.ui.display.avatar.Avatar;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class Display extends AbstractJPanel {

	/**
	 * The avatar currently being displayed.
	 * 
	 */
	protected Avatar avatar;

	/**
	 * Create a Display.
	 * 
	 * @param l18nContext
	 *            The localization context for the display.
	 */
	protected Display(final String l18nContext, final Color background) {
		super(l18nContext, background);
		setLayout(new GridBagLayout());
	}

	/**
	 * Display the avatar.
	 * 
	 */
	public void displayAvatar() {
		removeAll();
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(avatar, c.clone());
	}

	/**
	 * Obtain the avatar currently being displayed.
	 * 
	 * @return The avatar currently being displayed.
	 * 
	 * @see #setAvatar()
	 * @see #displayAvatar()
	 */
	public Avatar getAvatar() { return avatar; }

	/**
	 * Obtain the display id.
	 * 
	 * @return The display id.
	 */
	public abstract DisplayId getId();

	/**
	 * Set the avatar to display.
	 * 
	 * @param avatar
	 *            The avatar to display.
	 * @see #getAvatar()
	 * @see #displayAvatar()
	 */
	public void setAvatar(final Avatar avatar) { this.avatar = avatar; }
}

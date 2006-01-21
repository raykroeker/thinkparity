/*
 * Jan 20, 2006
 */
package com.thinkparity.browser.ui.display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

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
	}

	/**
	 * Display the avatar.
	 * 
	 */
	public abstract void displayAvatar();

	/**
	 * Obtain the avatar currently being displayed.
	 * 
	 * @return The avatar currently being displayed.
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
	 * Paint the display header.
	 * 
	 * @param g2
	 *            The graphics.
	 */
	public abstract void paintDisplayHeader(final Graphics2D g2);

	/**
	 * Set the avatar to display.
	 * 
	 * @param avatar
	 *            The avatar to display.
	 * @see #getAvatar()
	 * @see #displayAvatar()
	 */
	public void setAvatar(final Avatar avatar) { this.avatar = avatar; }

	/**
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		final Graphics2D g2 = (Graphics2D) g.create();
		try { paintDisplayHeader(g2); }
		finally { g2.dispose(); }
	}
}

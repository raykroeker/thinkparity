/*
 * Jan 20, 2006
 */
package com.thinkparity.browser.ui.display;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class ContentDisplay extends Display {

	/**
	 * Color of the line at the top of the content area.
	 * 
	 */
	private static final Color HEADER_COLOR;

	/**
	 * @see java.io.Serialiable
	 */
	private static final long serialVersionUID = 1;

	static { HEADER_COLOR = new Color(180, 180, 180, 255); }

	/**
	 * Create a ContentDisplay.
	 * 
	 */
	ContentDisplay() {
		super("ContentDisplay", new Color(255, 255, 255, 255));
		setLayout(new GridBagLayout());
	}

	/**
	 * @see com.thinkparity.browser.ui.display.Display#displayAvatar()
	 * 
	 */
	public void displayAvatar() {
		final GridBagConstraints avatarConstraints = new GridBagConstraints();
		avatarConstraints.fill = GridBagConstraints.BOTH;
		avatarConstraints.weightx = 1.0;
		avatarConstraints.weighty = 1.0;
		add(avatar, avatarConstraints);
		revalidate();
		repaint();
	}

	/**
	 * @see com.thinkparity.browser.ui.display.Display#getId()
	 * 
	 */
	public DisplayId getId() { return DisplayId.CONTENT; }

	/**
	 * @see com.thinkparity.browser.ui.display.Display#paintDisplayHeader(java.awt.Graphics2D)
	 * 
	 */
	public void paintDisplayHeader(Graphics2D g2) {
		g2.setColor(HEADER_COLOR);
		g2.drawLine(0, 0, getWidth() - 1, 0);
	}
}

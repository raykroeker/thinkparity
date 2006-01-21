/*
 * Jan 20, 2006
 */
package com.thinkparity.browser.ui.display;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class LogoDisplay extends Display {

	/**
	 * @see java.io.Serialiable
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Header line color.
	 * 
	 */
	private static final Color HEADER_COLOR;

	static { HEADER_COLOR = new Color(105, 105, 105, 255); }

	/**
	 * Create a LogoDisplay.
	 * 
	 */
	LogoDisplay() {
		super("LogoDisplay", new Color(255, 255, 255,255));
	}

	/**
	 * @see com.thinkparity.browser.ui.display.Display#displayAvatar()
	 * 
	 */
	public void displayAvatar() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see com.thinkparity.browser.ui.display.Display#getId()
	 */
	public DisplayId getId() { return DisplayId.LOGO; }

	/**
	 * @see com.thinkparity.browser.ui.display.Display#paintDisplayHeader(java.awt.Graphics2D)
	 * 
	 */
	public void paintDisplayHeader(Graphics2D g2) {
		g2.setColor(HEADER_COLOR);
		g2.drawLine(0, 0, getWidth() - 1, 0);
	}
}

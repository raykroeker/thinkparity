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
class InfoDisplay extends Display {

	private static final Color HEADER_COLOR_1;

	private static final Color HEADER_COLOR_2;

	/**
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1;

	static {
		HEADER_COLOR_1 = new Color(180, 180, 180, 255);
		HEADER_COLOR_2 = new Color(187, 197, 206, 255);
	}

	/**
	 * Create an InfoDisplay.
	 * 
	 */
	InfoDisplay() { super("InfoDisplay", new Color(235, 240, 246, 255)); }

	/**
	 * @see com.thinkparity.browser.ui.display.Display#displayAvatar()
	 * 
	 */
	public void displayAvatar() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see com.thinkparity.browser.ui.display.Display#getId()
	 * 
	 */
	public DisplayId getId() { return DisplayId.INFO; }

	/**
	 * @see com.thinkparity.browser.ui.display.Display#paintDisplayHeader(java.awt.Graphics2D)
	 * 
	 */
	public void paintDisplayHeader(Graphics2D g2) {
		g2.setColor(HEADER_COLOR_1);
		g2.drawLine(0, 0, getWidth() - 1, 0);
		g2.setColor(HEADER_COLOR_2);
		g2.drawLine(0, 1, getWidth() - 1, 1);
	}
}

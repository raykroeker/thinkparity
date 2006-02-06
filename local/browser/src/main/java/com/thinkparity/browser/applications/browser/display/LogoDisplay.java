/*
 * Jan 20, 2006
 */
package com.thinkparity.browser.applications.browser.display;

import java.awt.Color;
import java.awt.Graphics2D;

import com.thinkparity.browser.javax.swing.border.TopBorder;
import com.thinkparity.browser.platform.application.display.Display;

/**
 * Logo display section.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class LogoDisplay extends Display {

	/**
	 * @see java.io.Serialiable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Create a LogoDisplay.
	 * 
	 */
	LogoDisplay() {
		// COLOR WHITE
		super("LogoDisplay", Color.WHITE);
		// COLOR 180, 180, 180, 255
		setBorder(new TopBorder(new Color(180, 180, 180, 255)));
	}

	/**
	 * @see com.thinkparity.browser.platform.application.display.Display#getId()
	 */
	public DisplayId getId() { return DisplayId.LOGO; }

	/**
	 * @see com.thinkparity.browser.platform.application.display.Display#paintHeading(java.awt.Graphics2D)
	 * 
	 */
	public void paintHeading(Graphics2D g2) {}
}

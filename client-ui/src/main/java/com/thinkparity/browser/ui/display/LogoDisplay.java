/*
 * Jan 20, 2006
 */
package com.thinkparity.browser.ui.display;

import java.awt.Color;
import java.awt.Graphics2D;

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
		// NOTE Color
		super("LogoDisplay", new Color(255, 255, 255,255));
		// NOTE Color
		setBorder(new DefaultBorder(new Color(180, 180, 180, 255)));
	}

	/**
	 * @see com.thinkparity.browser.ui.display.Display#getId()
	 */
	public DisplayId getId() { return DisplayId.LOGO; }

	/**
	 * @see com.thinkparity.browser.ui.display.Display#paintHeading(java.awt.Graphics2D)
	 * 
	 */
	public void paintHeading(Graphics2D g2) {}
}

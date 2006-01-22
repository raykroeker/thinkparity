/*
 * Jan 20, 2006
 */
package com.thinkparity.browser.ui.display;

import java.awt.Color;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class InfoDisplay extends Display {

	/**
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Create an InfoDisplay.
	 * 
	 */
	InfoDisplay() {
		super("InfoDisplay", new Color(235, 240, 246, 255));
		setBorder(new MultiLineBorder(new Color[] {
				new Color(180, 180, 180, 255),
				new Color(187, 197, 206, 255) }));
	}

	/**
	 * @see com.thinkparity.browser.ui.display.Display#getId()
	 * 
	 */
	public DisplayId getId() { return DisplayId.INFO; }
}

/*
 * Jan 20, 2006
 */
package com.thinkparity.browser.ui.display;

import java.awt.Color;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class ContentDisplay extends Display {

	/**
	 * @see java.io.Serialiable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Create a ContentDisplay.
	 * 
	 */
	ContentDisplay() {
		super("ContentDisplay", new Color(255, 255, 255, 255));
		setBorder(new MultiLineBorder(new Color[] { new Color(180, 180, 180, 255), Color.WHITE}));
	}

	/**
	 * @see com.thinkparity.browser.ui.display.Display#getId()
	 * 
	 */
	public DisplayId getId() { return DisplayId.CONTENT; }
}

/*
 * Jan 20, 2006
 */
package com.thinkparity.browser.application.browser.display;

import java.awt.Color;

import com.thinkparity.codebase.swing.border.MultiLineBorder;

import com.thinkparity.browser.platform.application.display.Display;

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
		// COLOR ContentDisplayBackground WHITE
		super("ContentDisplay", Color.WHITE);
		// BORDER ContentDisplay Multiline 153,153,153,255, WHITE
		setBorder(new MultiLineBorder(new Color[] { new Color(153, 153, 153, 255), Color.WHITE}));
	}

	/**
	 * @see com.thinkparity.browser.platform.application.display.Display#getId()
	 * 
	 */
	public DisplayId getId() { return DisplayId.CONTENT; }
}

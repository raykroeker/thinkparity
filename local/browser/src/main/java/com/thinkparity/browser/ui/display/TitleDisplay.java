/*
 * Jan 27, 2006
 */
package com.thinkparity.browser.ui.display;

import java.awt.Color;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class TitleDisplay extends Display {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Create a TitleDisplay.
	 * 
	 */
	public TitleDisplay() {
		super("TitleDisplay", new Color(0, 0, 0, 255));
	}

	/**
	 * @see com.thinkparity.browser.ui.display.Display#getId()
	 * 
	 */
	public DisplayId getId() { return DisplayId.TITLE; }
}

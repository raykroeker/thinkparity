/*
 * Jan 27, 2006
 */
package com.thinkparity.browser.applications.browser.display;

import java.awt.Color;

import com.thinkparity.browser.platform.application.display.Display;

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
	 * @see com.thinkparity.browser.platform.application.display.Display#getId()
	 * 
	 */
	public DisplayId getId() { return DisplayId.TITLE; }
}

/*
 * Jan 20, 2006
 */
package com.thinkparity.browser.applications.browser.display;

import java.awt.Color;

import com.thinkparity.browser.javax.swing.border.MultiLineBorder;
import com.thinkparity.browser.platform.application.display.Display;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class InfoDisplay extends Display {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Create an InfoDisplay.
	 * 
	 */
	InfoDisplay() {
		super("InfoDisplay", new Color(235, 240, 246, 255));
		setBorder(new MultiLineBorder(new Color[] {
				new Color(180, 180, 180, 255), Color.WHITE }));
	}

	/**
	 * @see com.thinkparity.browser.platform.application.display.Display#getId()
	 * 
	 */
	public DisplayId getId() { return DisplayId.INFO; }
}

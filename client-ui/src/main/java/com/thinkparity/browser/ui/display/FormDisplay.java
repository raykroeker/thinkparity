/*
 * Jan 30, 2006
 */
package com.thinkparity.browser.ui.display;

import java.awt.Color;

import com.thinkparity.browser.javax.swing.border.MultiLineBorder;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class FormDisplay extends Display {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Create a FormDisplay.
	 * 
	 */
	FormDisplay() {
		// COLOR WHITE
		super("FormDisplay", Color.WHITE);
		// COLOR 180, 180, 180, 255, WHITE
		setBorder(new MultiLineBorder(new Color[] {
				new Color(180, 180, 180, 255), Color.WHITE}));
	}

	/**
	 * @see com.thinkparity.browser.ui.display.Display#getId()
	 * 
	 */
	public DisplayId getId() { return DisplayId.FORM; }
}

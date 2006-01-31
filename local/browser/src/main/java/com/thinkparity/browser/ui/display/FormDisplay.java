/*
 * Jan 30, 2006
 */
package com.thinkparity.browser.ui.display;

import java.awt.Color;


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
		// NOTE Color
		super("FormDisplay", Color.WHITE);
		// NOTE Color
		setBorder(new MultiLineBorder(new Color[] {
				new Color(180, 180, 180, 255), Color.WHITE }));
	}

	/**
	 * @see com.thinkparity.browser.ui.display.Display#getId()
	 * 
	 */
	public DisplayId getId() { return DisplayId.FORM; }
}

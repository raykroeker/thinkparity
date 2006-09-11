/*
 * Jan 20, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.display;

import java.awt.Color;

import com.thinkparity.ophelia.browser.platform.application.display.Display;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class ContentDisplay extends Display {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

	/**
	 * Create a ContentDisplay.
	 * 
	 */
	ContentDisplay() {
		super("ContentDisplay", Color.WHITE);
	}

	/**
	 * @see com.thinkparity.ophelia.browser.platform.application.display.Display#getId()
	 * 
	 */
	public DisplayId getId() { return DisplayId.CONTENT; }
}

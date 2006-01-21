/*
 * Jan 20, 2006
 */
package com.thinkparity.browser;

import com.thinkparity.browser.ui.display.avatar.Avatar;

/**
 * Manages all of the state information for the controller.
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ControllerState {

	/**
	 * The main controller.
	 * 
	 */
	private final Controller controller;

	/**
	 * Create a ControllerState.
	 * 
	 */
	ControllerState(final Controller controller) {
		super();
		this.controller = controller;
	}

	void loadAvatarState(final Avatar avatar) {}

	void saveAvatarState(final Avatar avatar) {}
}

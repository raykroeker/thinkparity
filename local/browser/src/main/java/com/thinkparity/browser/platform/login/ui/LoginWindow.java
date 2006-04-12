/*
 * Feb 3, 2006
 */
package com.thinkparity.browser.platform.login.ui;

import com.thinkparity.browser.application.browser.window.WindowId;
import com.thinkparity.browser.platform.application.window.Window;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class LoginWindow extends Window  {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Create a LoginWindow.
	 */
	public LoginWindow() {
        super(null, Boolean.FALSE, "LoginWindow");
        applyEscapeListener();
	}

	/**
	 * @see com.thinkparity.browser.platform.application.window.Window#getId()
	 * 
	 */
	public WindowId getId() { return WindowId.PLATFORM_LOGIN; }
}

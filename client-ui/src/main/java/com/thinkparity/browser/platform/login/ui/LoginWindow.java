/*
 * Feb 3, 2006
 */
package com.thinkparity.browser.platform.login.ui;

import com.thinkparity.browser.application.browser.window.WindowId;
import com.thinkparity.browser.platform.application.window.Window;

import java.awt.*;

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

    /**
	 * Calculate the centre screen location for the window.
	 * 
	 * @return The location of the window.
	 */
	protected Point calculateLocation() {
		final Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
		final Dimension ws = getSize();

        final Point l = getLocation();
        l.x = (ss.width - ws.width) / 2;
        l.y = (ss.height - ws.height) / 2;

        if(l.x + ws.width > (ss.width)) { l.x = ss.width - ws.width; }
        if(l.y + ws.height > (ss.height)) { l.y = ss.height - ws.height; }

        if(l.x < 0) { l.x = 0; }
        if(l.y < 0) { l.y = 0; }
        return l;
	}
}

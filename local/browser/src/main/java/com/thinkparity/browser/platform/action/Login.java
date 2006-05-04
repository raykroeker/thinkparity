/*
 * Sun Apr 30 11:34:18 PDT 2006
 */
package com.thinkparity.browser.platform.action;

import javax.swing.ImageIcon;

import com.thinkparity.browser.platform.Platform;
import com.thinkparity.browser.platform.login.LoginHelper;

/**
 * The platform login action.
 *
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public class Login extends AbstractAction {

    private static final ImageIcon ICON;

    private static final ActionId ID;

    private static final String NAME;

    static {
        ICON = null;
        ID = ActionId.PLATFORM_LOGIN;
        NAME = "Login";
    }

    /** The platform. */
    private final Platform platform;

    /**
     * Create a Logout.
     *
     * @param platform
     *      The platform.
     */
    public Login(final Platform platform) {
        super("Login", ID, NAME, ICON);
        this.platform = platform;
    }

    /** @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data) */
    public void invoke(final Data data) throws Exception {
        new LoginHelper(platform).login();
    }
}

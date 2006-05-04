/*
 * Sun Apr 30 11:34:18 PDT 2006
 */
package com.thinkparity.browser.platform.action;

import javax.swing.ImageIcon;

import com.thinkparity.browser.platform.Platform;

/**
 * The platform logout action.
 *
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public class Logout extends AbstractAction {

    private static final ImageIcon ICON;

    private static final ActionId ID;

    private static final String NAME;

    static {
        ICON = null;
        ID = ActionId.PLATFORM_LOGOUT;
        NAME = "Logout";
    }

    /** The platform. */
    private final Platform platform;

    /**
     * Create a Logout.
     *
     * @param platform
     *      The platform.
     */
    public Logout(final Platform platform) {
        super("Logout", ID, NAME, ICON);
        this.platform = platform;
    }

    /** @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data) */
    public void invoke(final Data data) throws Exception {
        getSessionModel().logout();
    }
}

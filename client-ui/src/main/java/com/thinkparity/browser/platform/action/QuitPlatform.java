/*
 * Apr 26, 2006
 */
package com.thinkparity.browser.platform.action;

import javax.swing.ImageIcon;

import com.thinkparity.browser.platform.Platform;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class QuitPlatform extends AbstractAction {

    private static final ImageIcon ICON;

    private static final ActionId ID;

    private static final String NAME;

    static {
        ICON = null;
        ID = ActionId.PLATFORM_QUIT;
        NAME = "Quit";
    }

    /** The browser platform. */
    private final Platform platform;

    /**
     * Create a QuitApplication.
     * 
     * @param platform
     *            The browser platform.
     */
    public QuitPlatform(final Platform platform) {
        super("QuitPlatform", ID, NAME, ICON);
        this.platform = platform;
    }

    /**
     * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) throws Exception { platform.end(); }
}

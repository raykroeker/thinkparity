/*
 * Apr 26, 2006
 */
package com.thinkparity.browser.platform.action.platform;

import com.thinkparity.browser.platform.Platform;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Quit extends AbstractAction {

    /** The browser platform. */
    private final Platform platform;

    /**
     * Create a QuitApplication.
     * 
     * @param platform
     *            The browser platform.
     */
    public Quit(final Platform platform) {
        super(ActionId.PLATFORM_QUIT);
        this.platform = platform;
    }

    /**
     * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) { platform.end(); }
}

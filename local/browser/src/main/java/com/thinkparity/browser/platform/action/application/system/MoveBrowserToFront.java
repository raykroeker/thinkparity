/*
 * Apr 28, 2006
 */
package com.thinkparity.browser.platform.action.application.system;

import javax.swing.ImageIcon;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.Platform;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;
import com.thinkparity.browser.platform.application.ApplicationId;
import com.thinkparity.browser.platform.application.ApplicationRegistry;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class MoveBrowserToFront extends AbstractAction {

    private static final ImageIcon ICON;

    private static final ActionId ID;

    private static final String NAME;

    static {
        ICON = null;
        ID = ActionId.MOVE_BROWSER_TO_FRONT;
        NAME = "MoveBrowserToFront";
    }

    /**
     * Create a MoveBrowserToFront.
     * 
     */
    public MoveBrowserToFront(final Platform platform) {
        super("MoveBrowserToFront", ID, NAME, ICON);
    }

    /**
     * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) throws Exception {
        ((Browser) new ApplicationRegistry().get(ApplicationId.BROWSER2)).runMoveBrowserToFront();
    }
}

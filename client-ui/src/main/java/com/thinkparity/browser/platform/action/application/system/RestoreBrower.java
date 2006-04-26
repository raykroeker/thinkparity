/*
 * Apr 26, 2006
 */
package com.thinkparity.browser.platform.action.application.system;

import javax.swing.ImageIcon;

import com.thinkparity.browser.platform.Platform;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;
import com.thinkparity.browser.platform.application.ApplicationId;
import com.thinkparity.browser.platform.application.ApplicationRegistry;
import com.thinkparity.browser.platform.application.ApplicationStatus;

/**
 * Restore the browser application.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class RestoreBrower extends AbstractAction {

    private static final ImageIcon ICON;

    private static final ActionId ID;

    private static final String NAME;
   
    static {
        ICON = null;
        ID = ActionId.RESTORE_BROWSER;
        NAME = "RestoreBrowser";
    }

    /** The browser platform. */
    private final Platform platform;

    /** Create a RestoreBrower. */
    public RestoreBrower(final Platform platform) {
        super("RestoreBrowser", ID, NAME, ICON);
        this.platform = platform;
    }

    /**
     * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) throws Exception {
        if(ApplicationStatus.HIBERNATING ==
            new ApplicationRegistry().getStatus(ApplicationId.BROWSER2)) {
            platform.restore(ApplicationId.BROWSER2);
        }
        else { platform.getLogger(getClass()).warn("[LBROWSER] [PLATFORM] [RESTORE BROWSER ACTION] [BROWSER NOT HIBERNATING]"); }
    }
}

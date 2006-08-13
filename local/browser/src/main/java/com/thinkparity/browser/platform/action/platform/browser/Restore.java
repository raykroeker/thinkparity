/*
 * Apr 26, 2006
 */
package com.thinkparity.browser.platform.action.platform.browser;

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
public class Restore extends AbstractAction {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** The browser platform. */
    private final Platform platform;

    /** Create a RestoreBrower. */
    public Restore(final Platform platform) {
        super(ActionId.PLATFORM_BROWSER_RESTORE);
        this.platform = platform;
    }

    /**
     * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        if (ApplicationStatus.HIBERNATING ==
            new ApplicationRegistry().getStatus(ApplicationId.BROWSER)) {
            platform.restore(ApplicationId.BROWSER);
        } else {
            platform.getLogger(getClass()).warn("[LBROWSER] [PLATFORM] [RESTORE BROWSER ACTION] [BROWSER NOT HIBERNATING]");
        }
    }
}

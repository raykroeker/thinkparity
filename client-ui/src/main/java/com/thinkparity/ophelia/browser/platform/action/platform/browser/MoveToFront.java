/*
 * Apr 28, 2006
 */
package com.thinkparity.ophelia.browser.platform.action.platform.browser;


import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationRegistry;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class MoveToFront extends AbstractAction {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** Create MoveBrowserToFront. */
    public MoveToFront(final Platform platform) {
        super(ActionId.PLATFORM_BROWSER_MOVE_TO_FRONT);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        ((Browser) new ApplicationRegistry().get(ApplicationId.BROWSER)).moveToFront();
    }
}

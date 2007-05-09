/*
 * Created On:  5-May-07 12:13:33 PM
 */
package com.thinkparity.ophelia.browser.application.browser;

import com.thinkparity.ophelia.browser.platform.action.DefaultPopupDelegate;
import com.thinkparity.ophelia.browser.platform.application.Application;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationRegistry;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class DefaultBrowserPopupDelegate extends DefaultPopupDelegate {

    /**
     * Create DefaultBrowserPopupDelegate.
     *
     */
    public DefaultBrowserPopupDelegate() {
        super(getApplication());
    }

    /**
     * Obtain the popup delegate application.
     * 
     * @return An <code>Application</code>.
     */
    private static Application getApplication() {
        return getBrowser();
    }

    /**
     * Obtain the browser from the application registry.
     * 
     * @return A <code>Browser</code> application.
     */
    private static Browser getBrowser() {
        return (Browser) new ApplicationRegistry().get(ApplicationId.BROWSER);
    }
}

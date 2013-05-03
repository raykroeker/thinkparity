/*
 * Created On:  5-May-07 10:21:04 AM
 */
package com.thinkparity.ophelia.browser.application.browser;

import com.thinkparity.ophelia.browser.platform.action.DefaultActionDelegate;
import com.thinkparity.ophelia.browser.platform.application.Application;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationRegistry;

/**
 * <b>Title:</b>thinkParity OpheliaUI Default Browser Action Delegate<br>
 * <b>Description:</b>A default action delegate which can be used to invoke
 * browser application actions.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class DefaultBrowserActionDelegate extends
        DefaultActionDelegate {

    /** The <code>ApplicationRegistry</code>. */
    private final ApplicationRegistry applicationRegistry;

    /**
     * Create DefaultBrowserActionDelegate.
     *
     */
    public DefaultBrowserActionDelegate() {
        super();
        this.applicationRegistry = new ApplicationRegistry();
    }

    /**
     * Obtain the action delegate application.
     * 
     * @return An <code>Application</code>.
     */
    protected Application getApplication() {
        return getBrowser();
    }

    /**
     * Obtain the browser application from the registry.
     * 
     * @return The <code>Browser</code>.
     */
    private Browser getBrowser() {
        return (Browser) applicationRegistry.get(ApplicationId.BROWSER);
    }
}

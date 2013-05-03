/*
 * Created On: Sep 24, 2006 11:24:06 AM
 */
package com.thinkparity.ophelia.browser.application.browser;

import com.thinkparity.codebase.log4j.Log4JWrapper;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
abstract class BrowserHelper {

    /** The thinkParity <code>Browser</code>. */
    protected final Browser browserApplication;

    /** An apache logger. */
    protected final Log4JWrapper logger;

    /**
     * Create BrowserHelper.
     * 
     * @param browserApplication
     *            The thinkParity <code>Browser</code>.
     */
    BrowserHelper(final Browser browserApplication) {
        super();
        this.browserApplication = browserApplication;
        this.logger = new Log4JWrapper();
    }
}

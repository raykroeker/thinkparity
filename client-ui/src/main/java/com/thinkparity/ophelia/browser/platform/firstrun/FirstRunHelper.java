/*
 * Created On: Jun 10, 2006 10:26:01 AM
 */
package com.thinkparity.ophelia.browser.platform.firstrun;

import com.thinkparity.ophelia.browser.platform.Platform;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.7
 */
public final class FirstRunHelper {

    /**
     * Create FirstRunHelper.
     * 
     * @param platform
     *            A thinkParity <code>Platform</code>.
     */
    public FirstRunHelper(final Platform platform) {
        super();
    }

    /**
     * Execute first run functionality for the browser platform.
     */
    public void firstRun() {
        final LoginWindow window = new LoginWindow();
        window.setVisibleAndWait();
    }
}

/*
 * Created On: Jun 10, 2006 10:26:01 AM
 */
package com.thinkparity.ophelia.browser.platform.firstrun;

import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.util.swing.OpheliaJFrame;

/**
 * <b>Title:</b>thinkParity Ophelia UI Platform First Run Helper<br>
 * <b>Description:</b>The entry point into running the initial wizard.<br>
 * 
 * @author raymond@thinkparity.com
 * @version $1.1.2.1$
 */
public final class FirstRunHelper {

    /** A signup window. */
    private OpheliaJFrame signupWindow;

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
     * 
     */
    public void firstRun() {
        getSignupWindow().setVisibleAndWait();
    }

    /**
     * Obtain the signup window.
     * 
     * @return A <code>SignupWindow</code>.
     */
    private OpheliaJFrame getSignupWindow() {
        if (null == signupWindow) {
            signupWindow = new SignupWindow();
        }
        return signupWindow;
    }
}

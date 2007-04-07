/*
 * Created On: Jun 10, 2006 10:26:01 AM
 */
package com.thinkparity.ophelia.browser.platform.firstrun;

import com.thinkparity.ophelia.model.workspace.InitializeMediator;

import com.thinkparity.ophelia.browser.platform.Platform;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.7
 */
public final class FirstRunHelper {

    /** A <code>SignupWindow</code>. */
    private SignupWindow signupWindow;

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
        final InitializeMediator initializeMediator = new ConfirmSynchronizeWindow();
        final LoginWindow loginWindow = new LoginWindow();
        loginWindow.setInitializeMediator(initializeMediator);
        boolean doneLogin = false;
        while (!doneLogin) {
            loginWindow.setVisibleAndWait();
            if (loginWindow.isSignup()) {
                getSignupWindow().setVisibleAndWait();
                if (getSignupWindow().isSignupCompleted()) {
                    loginWindow.setCredentials(getSignupWindow().getCredentials());
                    loginWindow.enableSignup(Boolean.FALSE);
                }
            } else {
                doneLogin = true;
            }
        }
    }

    /**
     * Get a SignupWindow
     * 
     * @return A <code>SignupWindow</code>.
     */
    private SignupWindow getSignupWindow() {
        if (null == signupWindow) {
            signupWindow = new SignupWindow();
        }
        return signupWindow;
    }
}

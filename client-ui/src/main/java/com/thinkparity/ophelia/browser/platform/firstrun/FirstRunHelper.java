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

    /** A <code>LoginWindow</code>. */
    private LoginWindow loginWindow;

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
        final SignupWindow signupWindow = getSignupWindow();
        signupWindow.setVisibleAndWait();
        if (!signupWindow.isCancelled()) {
            final LoginWindow loginWindow = getLoginWindow();
            if (signupWindow.isSetCredentials()) {
                loginWindow.setCredentials(signupWindow.getCredentials());
                loginWindow.setVisibleAndWait(new Runnable() {
                    public void run() {
                        loginWindow.login();
                    }
                });
            } else {
                loginWindow.setVisibleAndWait();
            }
        }
    }

    /**
     * Get a LoginWindow
     * 
     * @return A <code>LoginWindow</code>.
     */
    private LoginWindow getLoginWindow() {
        if (null == loginWindow) {
            loginWindow = new LoginWindow();
            loginWindow.setInitializeMediator(new InitializeMediator() {
                public Boolean confirmRestorePremium() {
                    return new ConfirmSynchronizeWindow().confirmRestorePremium();
                }
                public Boolean confirmRestoreStandard() {
                    return new ConfirmSynchronizeWindow().confirmRestoreStandard();
                }
            });
        }
        return loginWindow;
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

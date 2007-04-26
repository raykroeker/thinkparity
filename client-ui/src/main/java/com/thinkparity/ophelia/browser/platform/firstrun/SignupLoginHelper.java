/**
 * Created On: 24-Apr-07 2:33:53 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.firstrun;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class SignupLoginHelper {

    /** A singleton instance. */
    private static SignupLoginHelper SINGLETON;

    static {
        SINGLETON = new SignupLoginHelper();
    }

    /** The <code>LoginSwingDisplay</code>. */
    private LoginSwingDisplay loginSwingDisplay;

    /** The <code>LoginCredentialsDisplay</code>. */
    private LoginCredentialsDisplay loginCredentialsDisplay;

    /**
     * Create SignupLoginHelper.
     */
    private SignupLoginHelper() {
        super();
    }

    /**
     * Obtain the SignupLoginHelper instance.
     * 
     * @return The SignupLoginHelper.
     */
    public static SignupLoginHelper getInstance() {
        Assert.assertNotNull("The signup login helper has not yet been created.", SINGLETON);
        return SINGLETON;
    }

    /**
     * Get the login swing display.
     * 
     * @return The <code>LoginSwingDisplay</code>.
     */
    public LoginSwingDisplay getLoginSwingDisplay() {
        return loginSwingDisplay;
    }

    /**
     * Get the login credentials display.
     * 
     * @return The <code>LoginCredentialsDisplay</code>.
     */
    public LoginCredentialsDisplay getLoginCredentialsDisplay() {
        return loginCredentialsDisplay;
    }

    /**
     * Set the login swing display.
     * 
     * @param loginSwingDisplay
     *            The <code>LoginSwingDisplay</code>.
     */
    public void setLoginSwingDisplay(final LoginSwingDisplay loginSwingDisplay) {
        this.loginSwingDisplay = loginSwingDisplay;
    }

    /**
     * Set the login credentials display.
     * 
     * @param loginCredentialsDisplay
     *            The <code>LoginCredentialsDisplay</code>.
     */
    public void setLoginCredentialsDisplay(final LoginCredentialsDisplay loginCredentialsDisplay) {
        this.loginCredentialsDisplay = loginCredentialsDisplay;
    }
}

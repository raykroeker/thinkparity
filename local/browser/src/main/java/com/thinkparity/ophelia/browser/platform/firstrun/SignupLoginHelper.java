/**
 * Created On: 24-Apr-07 2:33:53 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.firstrun;

import java.util.List;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.migrator.Feature;

import com.thinkparity.ophelia.model.workspace.InitializeMediator;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class SignupLoginHelper implements InitializeMediator {

    /** A singleton instance. */
    private static SignupLoginHelper SINGLETON;
    
    static {
        SINGLETON = new SignupLoginHelper();
    }

    /** The <code>SignupDelegate</code>. */
    private SignupDelegate signupDelegate;
    
    private SignupCredentialsAvatar signupCredentialsAvatar;
    private SignupLoginAvatar signupLoginAvatar;

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

    public void setSignupCredentialsAvatar(final SignupCredentialsAvatar signupCredentialsAvatar) {
        this.signupCredentialsAvatar = signupCredentialsAvatar;
    }
    
    public void setSignupLoginAvatar(final SignupLoginAvatar signupLoginAvatar) {
        this.signupLoginAvatar = signupLoginAvatar;
    }

    public void setSignupDelegate(final SignupDelegate signupDelegate) {
        this.signupDelegate = signupDelegate;
    }

    public void setValidCredentials(final Boolean validCredentials) {
        signupCredentialsAvatar.setValidCredentials(validCredentials);
    }

    public ThinkParitySwingMonitor createMonitor() {
        Assert.assertNotNull("The login helper signup login avatar is null.", signupLoginAvatar);
        return new LoginSwingMonitor(signupLoginAvatar);
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.InitializeMediator#confirmRestore(java.util.List)
     */
    public Boolean confirmRestore(final List<Feature> features) {
        Assert.assertNotNull("The login helper signup delegate is null.", signupDelegate);
        Assert.assertNotNull("The login helper signup credentials avatar is null.", signupCredentialsAvatar);
        signupCredentialsAvatar.loginPhaseOneDone();
        signupDelegate.setNextPage();
        synchronized (signupDelegate) {
            try {
                signupDelegate.wait();
            } catch (final Throwable t) {
                throw new BrowserException(
                        "Error opening the thinkParity signup delegate.", t);
            }
            return !signupDelegate.isCancelled();
        }
    }
}

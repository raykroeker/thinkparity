/**
 * Created On: 24-Aug-06 2:23:52 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.profile;

import com.thinkparity.codebase.assertion.Assert;


import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class SignUp extends AbstractAction {

    /**
     * Create a Create.
     * 
     * @param browser
     *            The browser application.
     */
    public SignUp(final Browser browser) {
        super(ActionId.PROFILE_SIGN_UP);
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        Assert.assertNotYetImplemented("Sign-Up");               
    }
}

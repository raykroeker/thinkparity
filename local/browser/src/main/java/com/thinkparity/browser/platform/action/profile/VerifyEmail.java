/*
 * Created On: Aug 28, 2006 8:34:09 AM
 */
package com.thinkparity.browser.platform.action.profile;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class VerifyEmail extends AbstractAction {

    /**
     * Create VerifyEmail.
     * 
     * @param browser
     *            The thinkParity browser application.
     */
    public VerifyEmail(final Browser browser) {
        super(ActionId.PROFILE_VERIFY_EMAIL);
    }

    /**
     * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        throw Assert.createNotYetImplemented("VerifyEmail#invoke");
    }
}

/*
 * Created On: Aug 29, 2006 8:38:00 AM
 */
package com.thinkparity.browser.platform.action.profile;

import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.parity.model.profile.ProfileModel;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ResetPassword extends AbstractAction {

    /** Create ResetPassword. */
    public ResetPassword() {
        super(ActionId.PROFILE_RESET_PASSWORD);
    }

    /**
     * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final ProfileModel profileModel = getProfileModel();
        profileModel.resetPassword();
    }
}

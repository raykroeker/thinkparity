/*
 * Created On: Aug 28, 2006 8:34:01 AM
 */
package com.thinkparity.browser.platform.action.profile;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.parity.model.profile.ProfileModel;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class RemoveEmail extends AbstractAction {

    /**
     * Create RemoveEmail.
     * 
     * @param browser
     *            The thinkParity browser application.
     */
    public RemoveEmail(final Browser browser) {
        super(ActionId.PROFILE_REMOVE_EMAIL);
    }

    /**
     * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Long emailId = (Long) data.get(DataKey.EMAIL_ID);

        final ProfileModel profileModel = getProfileModel();
        profileModel.removeEmail(emailId);
    }

    public enum DataKey { EMAIL_ID }
}

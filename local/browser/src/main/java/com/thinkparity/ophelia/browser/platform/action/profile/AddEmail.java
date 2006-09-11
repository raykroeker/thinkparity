/*
 * Created On: Aug 28, 2006 8:33:52 AM
 */
package com.thinkparity.ophelia.browser.platform.action.profile;

import com.thinkparity.codebase.email.EMail;


import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.model.profile.ProfileModel;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class AddEmail extends AbstractAction {

    /**
     * Create AddEmail.
     * 
     * @param browser
     *            The thinkParity browser application.
     */
    public AddEmail(final Browser browser) {
        super(ActionId.PROFILE_ADD_EMAIL);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final EMail email = (EMail) data.get(DataKey.EMAIL);

        final ProfileModel profileModel = getProfileModel();
        profileModel.addEmail(email);
    }

    public enum DataKey { EMAIL }
}

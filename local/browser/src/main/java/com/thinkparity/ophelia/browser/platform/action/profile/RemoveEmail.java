/*
 * Created On: Aug 28, 2006 8:34:01 AM
 */
package com.thinkparity.ophelia.browser.platform.action.profile;


import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.model.profile.ProfileModel;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class RemoveEmail extends AbstractBrowserAction {

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
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Long emailId = (Long) data.get(DataKey.EMAIL_ID);

        final ProfileModel profileModel = getProfileModel();
        profileModel.removeEmail(emailId);
    }

    public enum DataKey { EMAIL_ID }
}

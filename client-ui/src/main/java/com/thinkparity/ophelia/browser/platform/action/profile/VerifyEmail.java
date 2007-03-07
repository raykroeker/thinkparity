/*
 * Created On: Aug 28, 2006 8:34:09 AM
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
public class VerifyEmail extends AbstractBrowserAction {

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
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Boolean displayAvatar = (Boolean) data.get(DataKey.DISPLAY_AVATAR);
        final Long emailId = (Long) data.get(DataKey.EMAIL_ID);

        if (displayAvatar) {
            getBrowserApplication().displayVerifyProfileEmailDialog(emailId);
        } else {
            final String key = (String) data.get(DataKey.KEY);
            final ProfileModel profileModel = getProfileModel();
            profileModel.verifyEmail(emailId, key);
        }
    }

    public enum DataKey { DISPLAY_AVATAR, EMAIL_ID, KEY }
}

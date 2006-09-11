/*
 * Created On: Aug 29, 2006 8:38:00 AM
 */
package com.thinkparity.ophelia.browser.platform.action.profile;


import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.model.profile.ProfileModel;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ResetPassword extends AbstractAction {

    /** The thinkParity browser application. */
    private final Browser browser;

    /** Create ResetPassword. */
    public ResetPassword(final Browser browser) {
        super(ActionId.PROFILE_RESET_PASSWORD);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Boolean displayAvatar = (Boolean) data.get(DataKey.DISPLAY_AVATAR);

        if (displayAvatar) {
            browser.displayResetProfilePasswordDialog();
        } else {
            final String securityAnswer = (String) data.get(DataKey.SECURITY_ANSWER);

            final ProfileModel profileModel = getProfileModel();
            profileModel.resetPassword(securityAnswer);
        }
    }

    public enum DataKey { DISPLAY_AVATAR, SECURITY_ANSWER }
}

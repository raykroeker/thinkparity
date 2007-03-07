/*
 * Created On: Aug 24, 2006 8:53:41 AM
 */
package com.thinkparity.ophelia.browser.platform.action.profile;

import com.thinkparity.codebase.model.session.InvalidCredentialsException;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.model.profile.ProfileModel;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class UpdatePassword extends AbstractBrowserAction {

    /** The thinkParity browser application. */
    private final Browser browser;

    /**
     * Create Update.
     * 
     * @param browser
     *            The thinkParity browser application.
     */
    public UpdatePassword(final Browser browser) {
        super(ActionId.PROFILE_UPDATE_PASSWORD);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Boolean displayAvatar = (Boolean) data.get(DataKey.DISPLAY_AVATAR);
        if (displayAvatar) {
            browser.displayUpdatePasswordDialog();
        } else {
            final ProfileModel profileModel = getProfileModel();

            // update password
            final String password = (String) data.get(DataKey.PASSWORD);
            final String newPassword = (String) data.get(DataKey.NEW_PASSWORD);
            try {
                profileModel.updatePassword(password, newPassword);
            } catch (final InvalidCredentialsException icx) {}
        }
    }

    /** Data keys. */
    public enum DataKey {
        DISPLAY_AVATAR, NEW_PASSWORD, NEW_PASSWORD_CONFIRM, PASSWORD
    }
}

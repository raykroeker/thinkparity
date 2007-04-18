/*
 * Created On: Aug 28, 2006 8:34:09 AM
 */
package com.thinkparity.ophelia.browser.platform.action.profile;


import com.thinkparity.ophelia.model.profile.ProfileModel;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class VerifyEmail extends AbstractBrowserAction {

    /** The browser application. */
    private final Browser browser;

    /**
     * Create VerifyEmail.
     * 
     * @param browser
     *            The thinkParity browser application.
     */
    public VerifyEmail(final Browser browser) {
        super(ActionId.PROFILE_VERIFY_EMAIL);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Boolean displayAvatar = (Boolean) data.get(DataKey.DISPLAY_AVATAR);

        if (displayAvatar) {
            getBrowserApplication().displayVerifyEmailDialog();
        } else {
            final Long emailId = (Long) data.get(DataKey.EMAIL_ID);
            final String key = (String) data.get(DataKey.KEY);
            final ProfileModel profileModel = getProfileModel();
            try {
                profileModel.verifyEmail(emailId, key);
            } catch (final Throwable t) {
                logger.logError(t, "Could not verify e-mail address {0} with key {1}.",
                        emailId, key);
                // HACK Should handle incorrect key in a better way. This is a temporary fix for the beta to avoid crash.
                browser.displayErrorDialog("VerifyEmail.ErrorVerifyKeyNotCorrect");
            }
        }
    }

    public enum DataKey { DISPLAY_AVATAR, EMAIL_ID, KEY }
}

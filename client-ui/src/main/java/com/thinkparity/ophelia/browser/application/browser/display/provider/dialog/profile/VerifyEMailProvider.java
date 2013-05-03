/*
 * Created On: Aug 24, 2006 1:14:41 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.profile;

import com.thinkparity.codebase.model.profile.ProfileEMail;

import com.thinkparity.ophelia.model.profile.ProfileModel;
import com.thinkparity.ophelia.model.session.SessionModel;

import com.thinkparity.ophelia.browser.application.browser.display.provider.ContentProvider;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class VerifyEMailProvider extends ContentProvider {

    /** An instance of <code>SessionModel</code>. */
    private final SessionModel sessionModel;

    /**
     * Create VerifyEMailProvider.
     * 
     * @param profileModel
     *            An instance of <code>ProfileModel</code>.
     * @param sessionModel
     *            An instance of <code>SessionModel</code>.
     */
    public VerifyEMailProvider(final ProfileModel profileModel,
            final SessionModel sessionModel) {
        super(profileModel);
        this.sessionModel = sessionModel;
    }

    /**
     * Determine if the model is online.
     * 
     * @return True if the model is online.
     */
    public Boolean isOnline() {
        return sessionModel.isOnline();
    }

    /**
     * Read the profile e-mail address.
     * 
     * @return A <code>ProfileEMail</code>.
     */
    public ProfileEMail readEMail() {
        return profileModel.readEMail();
    }
}

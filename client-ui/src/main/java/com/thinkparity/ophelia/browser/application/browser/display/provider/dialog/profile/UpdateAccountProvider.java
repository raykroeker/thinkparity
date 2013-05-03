/**
 * Created On: 25-Jul-07 10:19:55 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.profile;

import com.thinkparity.ophelia.model.profile.ProfileModel;
import com.thinkparity.ophelia.model.session.SessionModel;

import com.thinkparity.ophelia.browser.application.browser.display.provider.ContentProvider;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class UpdateAccountProvider extends ContentProvider {

    /** An instance of <code>SessionModel</code>. */
    private final SessionModel sessionModel;

    /**
     * Create UpdateAccountProvider.
     * 
     * @param profileModel
     *            A profile model interface.
     * @param sessionModel
     *            An instance of <code>SessionModel</code>.
     */
    public UpdateAccountProvider(final ProfileModel profileModel,
        final SessionModel sessionModel) {
        super(profileModel);
        this.sessionModel = sessionModel;
    }

    /**
     * Determine if the user is online.
     * 
     * @return True if the user is online.
     */
    public Boolean isOnline() {
        return sessionModel.isOnline();
    }
}

/*
 * Created On: Aug 24, 2006 1:14:41 PM
 */
package com.thinkparity.browser.application.browser.display.provider.dialog.profile;

import com.thinkparity.browser.application.browser.display.provider.SingleContentProvider;

import com.thinkparity.model.parity.model.profile.Profile;
import com.thinkparity.model.parity.model.profile.ProfileModel;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class UpdateProvider extends SingleContentProvider {

    /** A thinkParity profile interface. */
    private final ProfileModel profileModel;

    /**
     * Create UpdateProvider.
     * 
     * @param profile
     *            The user's profile.
     */
    public UpdateProvider(final Profile profile, final ProfileModel profileModel) {
        super(profile);
        this.profileModel = profileModel;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider#getElement(java.lang.Integer, java.lang.Object)
     */
    @Override
    public Object getElement(final Object input) {
        return profileModel.read();
    }
}

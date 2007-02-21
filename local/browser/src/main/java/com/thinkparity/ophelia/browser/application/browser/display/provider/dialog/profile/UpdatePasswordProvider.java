/**
 * Created On: Feb 20, 2007 10:38:12 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.profile;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.session.Credentials;

import com.thinkparity.ophelia.browser.application.browser.display.provider.CompositeSingleContentProvider;
import com.thinkparity.ophelia.model.profile.ProfileModel;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class UpdatePasswordProvider extends CompositeSingleContentProvider {

    /** A profile model interface. */
    private final ProfileModel profileModel;

    /**
     * Create UpdatePasswordProvider.
     * 
     * @param profile
     *            The local user profile.
     * @param profileModel
     *            A profile model interface.
     */
    public UpdatePasswordProvider(final Profile profile, final ProfileModel profileModel) {
        super(profile);
        this.profileModel = profileModel;
    }

    @Override
    public Object getElement(Integer index, Object input) {
        throw Assert.createNotYetImplemented("UpdatePasswordProvider#getElement");
    }

    /**
     * Read the user's credentials.
     * 
     * @return The user's credentials.
     */
    public Credentials readCredentials() {
        return profileModel.readCredentials();
    }
}

/**
 * Created On: Feb 20, 2007 10:38:12 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.profile;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;

import com.thinkparity.ophelia.model.profile.ProfileModel;

import com.thinkparity.ophelia.browser.application.browser.display.provider.CompositeSingleContentProvider;

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

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.provider.CompositeSingleContentProvider#getElement(java.lang.Integer,
     *      java.lang.Object)
     * 
     */
    @Override
    public Object getElement(final Integer index, final Object input) {
        throw Assert.createNotYetImplemented("UpdatePasswordProvider#getElement");
    }

    /**
     * Obtain the user's username.
     * 
     * @return A username <code>String</code>.
     */
    public String getSimpleUsername() {
        return profile.getSimpleUsername();
    }

    /**
     * Validate the user's credentials.
     * 
     * @param credentials
     *            The user's <code>Credentials</code>.
     */
    public void validateCredentials(final Credentials credentials)
            throws InvalidCredentialsException {
        profileModel.validateCredentials(credentials);
    }
}

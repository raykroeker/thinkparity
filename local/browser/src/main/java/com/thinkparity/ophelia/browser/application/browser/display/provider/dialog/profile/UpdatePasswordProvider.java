/**
 * Created On: Feb 20, 2007 10:38:12 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.profile;

import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;

import com.thinkparity.ophelia.model.profile.ProfileModel;

import com.thinkparity.ophelia.browser.application.browser.display.provider.ContentProvider;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class UpdatePasswordProvider extends ContentProvider {

    /**
     * Create UpdatePasswordProvider.
     * 
     * @param profileModel
     *            A profile model interface.
     */
    public UpdatePasswordProvider(final ProfileModel profileModel) {
        super(profileModel);
    }

    /**
     * Obtain the user's username.
     * 
     * @return A username <code>String</code>.
     */
    public String getSimpleUsername() {
        return profileModel.read().getSimpleUsername();
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

/**
 * Created On: Feb 1, 2007 4:36:25 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.profile;

import java.util.List;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;

import com.thinkparity.ophelia.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.ophelia.model.profile.ProfileModel;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class UpdateProfileProvider extends CompositeFlatSingleContentProvider {

    /** A profile model interface. */
    private final ProfileModel profileModel;

    /**
     * Create UpdateProfileProvider.
     * 
     * @param profile
     *            The local user profile.
     * @param profileModel
     *            A profile model interface.
     */
    public UpdateProfileProvider(final Profile profile, final ProfileModel profileModel) {
        super(profile);
        this.profileModel = profileModel;
    }

    @Override
    public Object getElement(Integer index, Object input) {
        throw Assert.createNotYetImplemented("UpdateProfileProvider#getElement");
    }

    @Override
    public Object[] getElements(Integer index, Object input) {
        throw Assert.createNotYetImplemented("UpdateProfileProvider#getElements");
    }

    /**
     * Read the profile.
     * 
     * @return A <code>Profile</code>.  
     */
    public Profile readProfile() {
        return profileModel.read();
    }

    /**
     * Read emails.
     * 
     * @return A list of email addresses.
     */
    public List<ProfileEMail> readEmails() {
        return profileModel.readEmails();
    }
}

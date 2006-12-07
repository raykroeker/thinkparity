/**
 * Created On: 6-Dec-06 2:56:53 PM
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
public class EditProfileProvider extends CompositeFlatSingleContentProvider {
    
    /** A profile model interface. */
    private final ProfileModel profileModel;
    
    /**
     * Create EditProfileProvider.
     * 
     * @param profile
     *            The local user profile.
     * @param profileModel
     *            A profile model interface.
     */
    public EditProfileProvider(final Profile profile, final ProfileModel profileModel) {
        super(profile);
        this.profileModel = profileModel;
    }
    
    @Override
    public Object getElement(Integer index, Object input) {
        throw Assert.createNotYetImplemented("EditProfileProvider#getElement");
    }

    @Override
    public Object[] getElements(Integer index, Object input) {
        throw Assert.createNotYetImplemented("EditProfileProvider#getElements");
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

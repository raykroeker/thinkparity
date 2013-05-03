/*
 * Created On: Aug 31, 2006 6:17:22 PM
 */
package com.thinkparity.ophelia.model.index.profile;

import java.util.Collections;
import java.util.List;

import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ProfileIndexEntry {

    private final List<ProfileEMail> emails;

    private final Profile profile;
    
    public ProfileIndexEntry(final Profile profile,
            final List<ProfileEMail> emails) {
        super();
        this.profile = profile;
        this.emails = emails;
    }

    /**
     * Obtain emails.
     *
     * @return A List<ProfileEMail>.
     */
    public List<ProfileEMail> getEmails() {
        return Collections.unmodifiableList(emails);
    }

    /**
     * Obtain profile.
     *
     * @return A Profile.
     */
    public Profile getProfile() {
        return profile;
    }

    
}

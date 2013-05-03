/*
 * Created On 2007-01-27 09:22
 */
package com.thinkparity.ophelia.model.profile;

import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;

import com.thinkparity.ophelia.model.events.ProfileEvent;

/**
 * <b>Title:</b>thinkParity Profile Event Generator<br>
 * <b>Description:</b><br>
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class ProfileEventGenerator {

    /** The event <code>Source</code>. */
    private final ProfileEvent.Source source;

    /**
     * Create ProfileEventGenerator.
     *
     * @param source
     *      The event <code>Source</code>.
     */
    ProfileEventGenerator(final ProfileEvent.Source source) {
        super();
        this.source = source;
    }

    /**
     * Generate a profile event.
     *
     * @param profile
     *      A <code>Profile</code>.
     */
    ProfileEvent generate(final Profile profile) {
        return generate(profile, null);
    }

    /**
     * Generate a profile event.
     *
     * @param profile
     *      A <code>Profile</code>.
     * @param email
     *      A <code>ProfileEMail</code> address.
     */
    ProfileEvent generate(final Profile profile, final ProfileEMail email) {
        return new ProfileEvent(source, profile, email);
    }
}

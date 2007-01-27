/*
 * Created On 2007-01-27 09:22
 */
package com.thinkparity.ophelia.model.events;

import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;

/**
 * <b>Title:</b>thinkParity Profile Event<br>
 * <b>Description:</b><br>
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ProfileEvent {

    /** A <code>Profile</code>. */
    private final Profile profile;

    /** A <code>ProfileEMail</code> address. */
    private final ProfileEMail email;

    /** An event <code>Source</code>. */
    private final Source source;

    /**
     * Create ProfileEvent.
     *
     * @param source
     *      The event <code>Source</code>.
     * @param profile
     *      The <code>Profile</code>.
     * @param email
     *      The <code>ProfileEMail</code>.
     */
    public ProfileEvent(final Source source, final Profile profile, final ProfileEMail email) {
        super();
        this.source = source;
        this.email = email;
        this.profile = profile;
    }

    /**
     * Obtain the profile.
     *
     * @return A <code>Profile</code>.
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Obtain the profile email.
     *
     * @return A <code>ProfileEMail</code>.
     */
    public ProfileEMail getEmail() {
        return email;
    }

    /**
     * Determine whether or not the event is a remote event.
     *
     * @return True if the event is a remote event.
     */
    public Boolean isRemote() {
        return Source.REMOTE == source;
    }

    /**
     * Determine whether or not the event is a local event.
     *
     * @return True if the event is a local event.
     */
    public Boolean isLocal() {
        return Source.LOCAL == source;
    }

    /**
     * <b>Title:</b>Profile Event Source<br>
     * <b>Description:</b><br>
     */
    public enum Source { LOCAL, REMOTE }
}

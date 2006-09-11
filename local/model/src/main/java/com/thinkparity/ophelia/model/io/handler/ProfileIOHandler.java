/*
 * Created On: Jul 17, 2006 12:37:07 PM
 */
package com.thinkparity.ophelia.model.io.handler;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;

import com.thinkparity.ophelia.model.io.IOHandler;

/**
 * @author raymond@thinkparity.com
 * @version
 */
public interface ProfileIOHandler extends IOHandler {

    /**
     * Create a profile.
     * 
     * @param profile
     *            A <code>Profile</code>.
     */
    public void create(final Profile profile);

    /**
     * Create a profile email.
     * 
     * @param profileId
     *            A profile id <code>Long</code>.
     * @param email
     *            A <code>ProfileEMail</code>.
     */
    public void createEmail(final Long profileId, final ProfileEMail email);

    /**
     * Delete a profile email.
     * 
     * @param profileId
     *            A profile id <code>Long</code>.
     * @param emailId
     *            An email id <code>Long</code>.
     */
    public void deleteEmail(final Long profileId, final Long emailId);

    /**
     * Read a profile.
     * 
     * @param profileId
     *            A profile id <code>JabberId</code>.
     * @return A <code>Profile</code>.
     */
    public Profile read(final JabberId profileId);

    /**
     * Read a profile email.
     * 
     * @param profileId
     *            A profile id <code>Long</code>.
     * @param emailId
     *            An email id <code>Long</code>.
     * @return A <code>ProfileEMail</code>.
     */
    public ProfileEMail readEmail(final Long profileId, final Long emailId);

    /**
     * Read the profile emails.
     * 
     * @param profileId
     *            A profile id <code>Long</code>.
     * @return A <code>List&lt;ProfileEMail&gt;</code>.
     */
    public List<ProfileEMail> readEmails(final Long profileId);

    /**
     * Update a profile.
     * 
     * @param profile
     *            A <code>Profile</code>.
     */
    public void update(final Profile profile);

    /**
     * Verify a profile email.
     * 
     * @param profileId
     *            A profile id <code>Long</code>.
     * @param emailId
     *            An email id <code>Long</code>.
     * @param verified
     *            The verification <code>Boolean</code> flag.
     */
    public void verifyEmail(final Long profileId, final Long emailId,
            final Boolean verified);
}

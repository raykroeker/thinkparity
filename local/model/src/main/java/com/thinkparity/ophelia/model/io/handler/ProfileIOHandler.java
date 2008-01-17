/*
 * Created On: Jul 17, 2006 12:37:07 PM
 */
package com.thinkparity.ophelia.model.io.handler;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;

import com.thinkparity.ophelia.model.io.IOHandler;

/**
 * <b>Title:</b>thinkParity OpheliaModel Profile IO<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.2
 */
public interface ProfileIOHandler extends IOHandler {

    /**
     * Create a profile.
     * 
     * @param profile
     *            A <code>Profile</code>.
     */
    void create(Profile profile);

    /**
     * Create a profile email.
     * 
     * @param profileId
     *            A profile id <code>Long</code>.
     * @param email
     *            A <code>ProfileEMail</code>.
     */
    void createEmail(Long profileId, final ProfileEMail email);

    /**
     * Delete the profile.
     * 
     * @param profile
     *            A <code>Profile</code>.
     */
    void delete(Profile profile);

    /**
     * Delete a profile email.
     * 
     * @param profile
     *            A <code>Profile</code>.
     * @param email
     *            A <code>ProfileEMail</code>.
     */
    void delete(Profile profile, ProfileEMail email);

    /**
     * Determine if the profile is active.
     * 
     * @param profile
     *            A <code>Profile</code>.
     * @return True if it is active.
     */
    Boolean isActive(Profile profile);

    /**
     * Read a profile.
     * 
     * @param profileId
     *            A profile id <code>JabberId</code>.
     * @return A <code>Profile</code>.
     */
    Profile read(JabberId profileId);

    /**
     * Read the disk usage of the profile.
     * 
     * @return A number of bytes <code>Long</code>.
     */
    Long readDiskUsage();

    /**
     * Read a profile email.
     * 
     * @param profileId
     *            A profile id <code>Long</code>.
     * @param emailId
     *            An email id <code>Long</code>.
     * @return A <code>ProfileEMail</code>.
     */
    ProfileEMail readEmail(Long profileId, final Long emailId);

    /**
     * Read the profile emails.
     * 
     * @param profileId
     *            A profile id <code>Long</code>.
     * @return A <code>List&lt;ProfileEMail&gt;</code>.
     */
    List<ProfileEMail> readEmails(Long profileId);

    /**
     * Read the features for the profile.
     * 
     * @param profileId
     *            A profile id <code>Long</code>.
     * @return A <code>List</code> of <code>Feature</code>s.
     */
    List<Feature> readFeatures(Long profileId);

    /**
     * Update a profile.
     * 
     * @param profile
     *            A <code>Profile</code>.
     */
    void update(Profile profile);

    /**
     * Update the profile's active state.
     * 
     * @param profile
     *            A <code>Profile</code>.
     */
    void updateActive(Profile profile);

    /**
     * Update the profile features.
     * 
     * @param profile
     *            A <code>Profile</code>.
     */
    void updateFeatures(Profile profile);

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
    void verifyEmail(Long profileId, final Long emailId,
            final Boolean verified);
}

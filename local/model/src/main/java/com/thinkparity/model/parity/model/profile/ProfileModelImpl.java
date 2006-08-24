/*
 * Generated On: Jul 17 06 11:52:34 AM
 */
package com.thinkparity.model.parity.model.profile;

import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.io.IOFactory;
import com.thinkparity.model.parity.model.io.handler.ProfileIOHandler;
import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Profile Model Implementation</br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1.1.1
 */
class ProfileModelImpl extends AbstractModelImpl {

    /** The profile db io. */
    private final ProfileIOHandler profileIO;

    /**
     * Create ProfileModelImpl.
     *
     * @param workspace
     *      The thinkParity workspace.
     */
    ProfileModelImpl(final Workspace workspace) {
        super(workspace);
        this.profileIO = IOFactory.getDefault().createProfileHandler();
    }

    /**
     * Read the logged in user's profile. Note that the user's profile will
     * always exist remotely. If it does not exist locally (in the form of a row
     * in the user table) create it.
     * 
     * @return A profile.
     */
    Profile read() {
        logApiId();
        Profile profile = profileIO.read(localUserId());
        if (null == profile) {
            profile = getInternalSessionModel().readProfile();
            profileIO.create(profile);
        }
        return profile;
    }

    /**
     * Update the logged in user's profile.
     * 
     * @param profile
     *            A profile.
     */
    void update(final Profile profile) {
        logApiId();
        logVariable("profile", profile);
        // update local data
        profileIO.update(profile);
        getInternalSessionModel().updateProfile(profile);
    }
}

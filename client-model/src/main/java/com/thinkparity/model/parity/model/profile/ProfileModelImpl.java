/*
 * Generated On: Jul 17 06 11:52:34 AM
 */
package com.thinkparity.model.parity.model.profile;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.io.IOFactory;
import com.thinkparity.model.parity.model.io.handler.ProfileIOHandler;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.user.User;

/**
 * <b>Title:</b>thinkParity Profile Model Implementation</br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1.1.1
 */
class ProfileModelImpl extends AbstractModelImpl {

   /**
     * Obtain an apache api log id.
     * 
     * @param api
     *            The api.
     * @return The log id.
     */
    private static StringBuffer getApiId(final String api) {
        return getModelId("[PROFILE]").append(" ").append(api);
    }

    /** The profile db io. */
    private final ProfileIOHandler profileIO;

    /**
     * Create ProfileModelImpl.
     *
     * @param workspace
     *		The thinkParity workspace.
     */
    ProfileModelImpl(final Workspace workspace) {
        super(workspace);
        this.profileIO = IOFactory.getDefault().createProfileHandler();
    }

    /**
     * Create the logged in user's profile.
     * 
     * @return A profile.
     */
    Profile create() {
        logger.info(getApiId("[CREATE]"));
        assertOnline(getApiId("[CREATE] [USER NOT ONLINE]"));
        assertIsNull(getApiId("[CREATE] [USER PROFILE EXISTS]"));

        final Profile profile = new Profile();
        profileIO.create(profile);
        return profileIO.read();
    }

    /**
     * Read the logged in user's profile.
     * 
     * @return A profile.
     */
    Profile read() {
        logger.info(getApiId("[READ]"));
        final JabberId currentUserId = currentUserId();
        if(null == currentUserId) { return null; }
        else {
            final User user = getInternalUserModel().read(currentUserId);
            if(null == user) { return null; }
            else {
                final Profile profile = new Profile();
                profile.setJabberId(user.getId());
                return profile;
            }
        }
    }

    /**
     * Update the logged in user's profile.
     * 
     * @param profile
     *            A profile.
     */
    void update(final Profile profile) {
        logger.info(getApiId("[UPDATE]"));
        throw Assert.createNotYetImplemented("ProfileModelImpl#update(Profile)");
    }

    /**
     * Assert that the profile is null.
     * 
     * @param assertion
     *            The assertion.
     */
    private void assertIsNull(final Object assertion) {
        Assert.assertIsNull(assertion, read());
    }
}

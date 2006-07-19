/*
 * Generated On: Jul 17 06 11:52:34 AM
 */
package com.thinkparity.model.parity.model.profile;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.ParityErrorTranslator;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.io.IOFactory;
import com.thinkparity.model.parity.model.io.handler.ProfileIOHandler;
import com.thinkparity.model.parity.model.user.InternalUserModel;
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
     * Read the logged in user's profile. Note that the user's profile will
     * always exist remotely. If it does not exist locally (in the form of a row
     * in the user table) create it.
     * 
     * @return A profile.
     */
    Profile read() {
        logger.info(getApiId("[READ]"));
        assertOnline(getApiId("[READ] [USER NOT ONLINE]"));
        final InternalUserModel uModel = getInternalUserModel();
        final JabberId currentUserId = currentUserId();
        final User user = uModel.read(currentUserId);
        if(null == user) {
            try { uModel.create(currentUserId); }
            catch(final ParityException px) {
                throw ParityErrorTranslator.translateUnchecked(px);
            }
            return read();
        }
        else {
            final Profile profile = new Profile();
            profile.addEmail("user@domain.com");
            profile.setId(user.getId());
            profile.setLocalId(user.getLocalId());
            profile.setName(user.getName());
            profile.setOrganization(user.getOrganization());
            return profile;
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
}

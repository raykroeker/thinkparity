/**
 * Created On: Mar 25, 2007 10:56:46 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.contact;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.user.UserModel;

import com.thinkparity.ophelia.browser.application.browser.display.provider.ContentProvider;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class UserInfoProvider extends ContentProvider {

    /** A contact model */
    private final ContactModel contactModel;

    /** A thinkParity user model. */
    private final UserModel userModel;

    /**
     * Create UserInfoProvider.
     * 
     * @param profile
     *            The user's <code>Profile</code>.
     * @param userModel
     *            An instance of <code>UserModel</code>.
     * @param contactModel
     *            An instance of <code>ContactModel</code>.
     */
    public UserInfoProvider(final Profile profile,
            final UserModel userModel,
            final ContactModel contactModel) {
        super(profile);
        this.contactModel = contactModel;
        this.userModel = userModel;
    }

    /**
     * Determine if the user is a contact.
     * 
     * @param userId
     *            A user id <code>Long</code>.
     * @return True if the user is a contact.
     */
    public Boolean readDoesExistContact(final Long userId) {
        return contactModel.doesExist(userId);
    }

    /**
     * Determine if there exists an outgoing user invitation.
     * 
     * @param userId
     *            A user id <code>Long</code>.
     * @return True if there exists an outgoing user invitation.
     */
    public Boolean readDoesExistOutgoingUserInvitationForUser(final Long userId) {
        return contactModel.doesExistOutgoingUserInvitationForUser(userId);
    }

    /**
     * Determine if the specified user is the local user.
     * 
     * @param user
     *            A <code>User</code>.
     * @return True if this is the local user; false otherwise.
     */
    public Boolean readIsLocalUser(final User user) {
        return user.getId().equals(profile.getId());
    }

    /**
     * Read the user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>User</code>.
     */
    public User readUser(final JabberId userId) {
        return userModel.read(userId);
    }
}

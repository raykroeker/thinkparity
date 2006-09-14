/*
 * Apr 20, 2006
 */
package com.thinkparity.ophelia.model.user;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.AbstractModelImpl;
import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.UserIOHandler;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class UserModelImpl extends AbstractModelImpl {

    /** The user db io. */
    private final UserIOHandler userIO;

    /**
     * Create a UserModelImpl.
     */
    UserModelImpl(final Workspace workspace) {
        super(workspace);
        this.userIO = IOFactory.getDefault(workspace).createUserIOHandler();
    }

    User create(final JabberId userId) {
        logApiId();
        logVariable("userId", userId);
        final User remoteUser = getInternalSessionModel().readUser(userId);
        userIO.create(remoteUser);
        return read(userId);
    }

    User read() {
        logApiId();
        // NOTE User has not yet logged in.
        final JabberId localUserId = localUserId();
        if (null == localUserId) {
            return null;
        } else {
            return userIO.read(localUserId);
        }
    }

    User read(final JabberId userId) {
        logApiId();
        logVariable("userId", userId);
        return userIO.read(userId);
    }

    User read(final Long userId) {
        logApiId();
        logVariable("userId", userId);
        return userIO.read(userId);
    }

    /**
     * Read a user. If the user does not exist then create the user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>User</code>.
     */
    User readLazyCreate(final JabberId userId) {
        logApiId();
        logVariable("userId", userId);
        final User localUser = read(userId);
        if (null == localUser) {
            return create(userId);
        } else {
            return localUser;
        }
    }
}

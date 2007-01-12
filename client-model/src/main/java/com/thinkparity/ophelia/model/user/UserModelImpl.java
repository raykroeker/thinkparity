/*
 * Apr 20, 2006
 */
package com.thinkparity.ophelia.model.user;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.AbstractModelImpl;
import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.UserIOHandler;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity User Model Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.6
 */
public final class UserModelImpl extends AbstractModelImpl implements UserModel,
        InternalUserModel {

    /** The user db io. */
    private UserIOHandler userIO;

    /**
     * Create UserModelImpl.
     *
     */
    public UserModelImpl() {
        super();
    }

    public User create(final JabberId userId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        final User remoteUser = getSessionModel().readUser(userId);
        userIO.create(remoteUser);
        return read(userId);
    }

    public User read() {
        logger.logApiId();
        // NOTE User has not yet logged in.
        final JabberId localUserId = localUserId();
        if (null == localUserId) {
            return null;
        } else {
            return userIO.read(localUserId);
        }
    }

    public User read(final JabberId userId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        return userIO.read(userId);
    }

    public User read(final Long userId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        return userIO.read(userId);
    }

    /**
     * Read a user. If the user does not exist then create the user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>User</code>.
     */
    public User readLazyCreate(final JabberId userId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        final User localUser = read(userId);
        if (null == localUser) {
            return create(userId);
        } else {
            return localUser;
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.AbstractModelImpl#initializeModel(com.thinkparity.codebase.model.session.Environment, com.thinkparity.ophelia.model.workspace.Workspace)
     *
     */
    @Override
    protected void initializeModel(final Environment environment,
            final Workspace workspace) {
        this.userIO = IOFactory.getDefault(workspace).createUserIOHandler();
    }
}

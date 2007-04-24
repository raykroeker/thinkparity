/*
 * Apr 20, 2006
 */
package com.thinkparity.ophelia.model.user;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.user.UserFlag;

import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.UserIOHandler;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity OpheliaModel User Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.8
 */
public final class UserModelImpl extends Model implements UserModel,
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

    /**
     * @see com.thinkparity.ophelia.model.user.UserModel#read(com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public User read(final JabberId userId) {
        try {
            return userIO.read(userId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.user.InternalUserModel#readLazyCreate(com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public User readLazyCreate(final JabberId userId) {
        try {
            final User user = read(userId);
            if (null == user) {
                return create(userId);
            } else {
                return user;
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#initializeModel(com.thinkparity.codebase.model.session.Environment, com.thinkparity.ophelia.model.workspace.Workspace)
     *
     */
    @Override
    protected void initializeModel(final Environment environment,
            final Workspace workspace) {
        this.userIO = IOFactory.getDefault(workspace).createUserIOHandler();
    }

    /**
     * Apply a flag to a user.
     * 
     * @param userId
     *            A user id <code>Long</code>.
     * @param flag
     *            The <code>UserFlag</code>.
     */
    private void applyFlag(final Long userId, final UserFlag flag) {
        final List<UserFlag> flags = userIO.readFlags(userId);
        if (flags.contains(flag)) {
            logger.logWarning("User {0} is already flagged as {1}.", userId,
                    flag);
        }
        else {
            flags.add(flag);
            userIO.applyFlags(userId, flags);
        }
    }

    /**
     * Apply the bookmark flag.
     * 
     * @param artifactId
     *            An artifact id.
     */
    private void applyFlagContainerPublishRestricted(final Long userId) {
        applyFlag(userId, UserFlag.CONTAINER_PUBLISH_RESTRICTED);
    }

    /**
     * Create a user. All of the user information will be downloaded remotely
     * then created locally.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>User</code>.
     */
    private User create(final JabberId userId) {
        final com.thinkparity.desdemona.model.user.InternalUserModel serverUserModel =
            com.thinkparity.desdemona.model.user.UserModel.getInternalModel(new Context());
        final User user = serverUserModel.read(userId);
        final com.thinkparity.desdemona.model.rules.InternalRulesModel serverRulesModel =
            com.thinkparity.desdemona.model.rules.RulesModel.getInternalModel(new Context());
        final Boolean isRestricted = serverRulesModel.isPublishRestricted(
                localUserId(), userId);
        userIO.create(user);
        if (isRestricted.booleanValue())
            applyFlagContainerPublishRestricted(user.getLocalId());
        return read(userId);
    }
}

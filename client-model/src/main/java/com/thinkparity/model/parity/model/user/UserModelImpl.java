/*
 * Apr 20, 2006
 */
package com.thinkparity.model.parity.model.user;

import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.io.IOFactory;
import com.thinkparity.model.parity.model.io.handler.UserIOHandler;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class UserModelImpl extends AbstractModelImpl {

    private static final StringBuffer getApiId(final String api) {
        return getModelId("[USER]").append(" ").append(api);
    }

    private final UserIOHandler userIO;

    /**
     * Create a UserModelImpl.
     */
    UserModelImpl(final Workspace workspace) {
        super(workspace);
        this.userIO = IOFactory.getDefault().createUserIOHandler();
    }

    User create(final JabberId jabberId) {
        logger.info(getApiId("[CREATE]"));
        logger.debug(jabberId);
        final User remoteUser = getInternalSessionModel().readUser(jabberId);
        userIO.create(remoteUser);
        return read(jabberId);
    }

    User read() {
        logger.info(getApiId("[READ]"));
        // NOTE User has not yet logged in.
        final JabberId currentUserId = localUserId();
        if(null == currentUserId) { return null; }
        else { return userIO.read(currentUserId); }
    }

    User read(final JabberId jabberId) {
        logger.info(getApiId("[READ]"));
        logger.debug(jabberId);
        return userIO.read(jabberId);
    }

    User read(final Long userId) {
        logger.info(getApiId("[READ]"));
        logger.debug(userId);
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

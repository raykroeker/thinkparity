/*
 * Apr 20, 2006
 */
package com.thinkparity.model.parity.model.user;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.io.IOFactory;
import com.thinkparity.model.parity.model.io.handler.UserIOHandler;
import com.thinkparity.model.parity.model.session.InternalSessionModel;
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

    User create(final String name, final String email, final String organization)
            throws ParityException {
        logger.info(getApiId("[CREATE]"));
        logger.debug(name);
        logger.debug(email);
        logger.debug(organization);
        final InternalSessionModel isModel = getInternalSessionModel();
        final User remoteUser = isModel.readUser(currentUserId());
        remoteUser.setName(name);
        remoteUser.setEmail(email);
        remoteUser.setOrganization(organization);
        userIO.create(remoteUser);
        isModel.updateUser(name, email, organization);
        return read();
    }

    User create(final JabberId jabberId) throws ParityException {
        logger.info(getApiId("[CREATE]"));
        logger.debug(jabberId);
        final User remoteUser = getInternalSessionModel().readUser(jabberId);
        userIO.create(remoteUser);
        return read(jabberId);
    }

    User read() {
        logger.info(getApiId("[READ]"));
        // NOTE User has not yet logged in.
        final JabberId currentUserId = currentUserId();
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
}

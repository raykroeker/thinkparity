/*
 * Apr 20, 2006
 */
package com.thinkparity.model.parity.model.user;

import com.thinkparity.model.parity.ParityException;
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

    private final UserIOHandler userIO;

    /**
     * Create a UserModelImpl.
     */
    UserModelImpl(final Workspace workspace) {
        super(workspace);
        this.userIO = IOFactory.getDefault().createUserIOHandler();
    }

    User read(final Long userId) {
        return userIO.read(userId);
    }

    User read(final JabberId jabberId) {
        return userIO.read(jabberId);
    }

    User create(final JabberId jabberId) throws ParityException {
        final User remoteUser = getInternalSessionModel().readUser(jabberId);
        userIO.create(remoteUser);
        return read(jabberId);
    }
}

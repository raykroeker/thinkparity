/*
 * Apr 20, 2006
 */
package com.thinkparity.model.parity.model.user;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class InternalUserModel extends UserModel {

    /**
     * Create a InternalUserModel.
     */
    InternalUserModel(final Workspace workspace, final Context context) {
        super(workspace);
        context.assertContextIsValid();
    }

    public User read(final Long userId) {
        synchronized(getImplLock()) { return getImpl().read(userId); }
    }

    public User read(final JabberId jabberId) {
        synchronized(getImplLock()) { return getImpl().read(jabberId); }
    }

    public User create(final JabberId jabberId) throws ParityException {
        synchronized(getImplLock()) { return getImpl().create(jabberId); }
    }
}

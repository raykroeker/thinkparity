/*
 * Apr 20, 2006
 */
package com.thinkparity.model.parity.model.user;

import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.InternalModel;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class InternalUserModel extends UserModel implements InternalModel {

    /**
     * Create a InternalUserModel.
     */
    InternalUserModel(final Workspace workspace, final Context context) {
        super(workspace);
        context.assertContextIsValid();
    }

    /**
     * Create a user.  This api will download the user's info as set
     * on the server and save it to the database.
     *
     * @param jabberId
     *      The user's jabber id.
     * @return The user.
     */
    public User create(final JabberId jabberId) {
        synchronized(getImplLock()) { return getImpl().create(jabberId); }
    }

    /**
     * Read a user.
     *
     * @param userId
     *      A user id.
     * @return A user.
     */
    public User read(final Long userId) {
        synchronized(getImplLock()) { return getImpl().read(userId); }
    }

    /**
     * Read a user. If the user does not exist then create the user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>User</code>.
     */
    public User readLazyCreate(final JabberId userId) {
        synchronized (getImplLock()) {
            return getImpl().readLazyCreate(userId);
        }
    }

}

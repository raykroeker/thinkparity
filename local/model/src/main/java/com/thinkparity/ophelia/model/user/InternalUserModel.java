/*
 * Apr 20, 2006
 */
package com.thinkparity.ophelia.model.user;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.user.User;


import com.thinkparity.ophelia.model.Context;
import com.thinkparity.ophelia.model.InternalModel;
import com.thinkparity.ophelia.model.workspace.Workspace;

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

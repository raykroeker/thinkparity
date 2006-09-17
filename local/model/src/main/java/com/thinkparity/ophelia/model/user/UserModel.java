/*
 * Apr 20, 2006
 */
package com.thinkparity.ophelia.model.user;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.AbstractModel;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class UserModel extends AbstractModel<UserModelImpl> {

    /**
     * Obtain the parity internal user interface.
     *
     * @param workspace
     *      A thinkParity <code>Workspace</code>.
     * @param context
     *      The parity calling context.
     * @return The parity internal user interface.
     */
    public static InternalUserModel getInternalModel(final Context context,
            final Workspace workspace) {
        return new InternalUserModel(workspace, context);
    }

    /**
     * Obtain the parity user interface.
     *
     * @param workspace
     *      A thinkParity <code>Workspace</code>.
     * @return The parity user interface.
     */
    public static UserModel getModel(final Workspace workspace) {
        return new UserModel(workspace);
    }

    /**
     * Create a UserModel.
     *
     * @param workspace
     *      The parity workspace.
     */
    protected UserModel(final Workspace workspace) {
        super(new UserModelImpl(workspace));
    }

    /**
     * Read a user.
     *
     * @param jabberId
     *      The user's jabber id.
     * @return The user.
     */
    public User read(final JabberId jabberId) {
        synchronized(getImplLock()) { return getImpl().read(jabberId); }
    }
}

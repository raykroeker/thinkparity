/*
 * Apr 20, 2006
 */
package com.thinkparity.model.parity.model.user;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractModel;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class UserModel extends AbstractModel {

    /**
     * Obtain the parity internal user interface.
     *
     * @param context
     *      The parity calling context.
     * @return The parity internal user interface.
     */
    public static InternalUserModel getInternalModel(final Context context) {
        final Workspace workspace = WorkspaceModel.getModel().getWorkspace();
        return new InternalUserModel(workspace, context);
    }

    /**
     * Obtain the parity user interface.
     *
     * @return The parity user interface.
     */
    public static UserModel getModel() {
        final Workspace workspace = WorkspaceModel.getModel().getWorkspace();
        return new UserModel(workspace);
    }

    /** The model implementation. */
    private final UserModelImpl impl;

    /** The impl synchronization lock. */
    private final Object implLock;

    /**
     * Create a UserModel.
     *
     * @param workspace
     *      The parity workspace.
     */
    protected UserModel(final Workspace workspace) {
        super();
        this.impl = new UserModelImpl(workspace);
        this.implLock = new Object();
    }

    /**
     * Create the user locally as well as save the information remotely.
     * 
     * @param name
     *            The user's name.
     * @param email
     *            The users's email.
     * @param organization
     *            The users's organization.
     * @return A new user.
     */
    public User create(final String name, final String email,
            final String organization) throws ParityException {
        synchronized(implLock) { return impl.create(name, email, organization); }
    }

    /**
     * Read the current user.
     * 
     * @return A user.
     */
    public User read() {
        synchronized(getImplLock()) { return getImpl().read(); }
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

    /**
     * Obtain the model implementation.
     *
     * @return The model implementation.
     */
    protected UserModelImpl getImpl() { return impl; }

    /**
     * Obtain the impl synchronization lock.
     *
     * @return The impl synchronization lock.
     */
    protected Object getImplLock() { return implLock; }
}

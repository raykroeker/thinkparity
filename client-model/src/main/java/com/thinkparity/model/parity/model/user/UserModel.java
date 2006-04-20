/*
 * Apr 20, 2006
 */
package com.thinkparity.model.parity.model.user;

import com.thinkparity.model.parity.model.AbstractModel;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class UserModel extends AbstractModel {

    public static UserModel getModel() {
        final Workspace workspace = WorkspaceModel.getModel().getWorkspace();
        return new UserModel(workspace);
    }

    public static InternalUserModel getInternalModel(final Context context) {
        final Workspace workspace = WorkspaceModel.getModel().getWorkspace();
        return new InternalUserModel(workspace, context);
    }

    private final UserModelImpl impl;

    private final Object implLock;

    /**
     * Create a UserModel.
     */
    protected UserModel(final Workspace workspace) {
        super();
        this.impl = new UserModelImpl(workspace);
        this.implLock = new Object();
    }

    protected UserModelImpl getImpl() { return impl; }

    protected Object getImplLock() { return implLock; }
}

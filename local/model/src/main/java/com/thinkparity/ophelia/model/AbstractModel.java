/*
 * Aug 6, 2005
 */
package com.thinkparity.ophelia.model;

import com.thinkparity.ophelia.model.workspace.WorkspaceModel;

/**
 * AbstractModel
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractModel<T extends AbstractModelImpl> extends
        com.thinkparity.codebase.model.AbstractModel<T> {

    /**
	 * Obtain the workspace model.
	 * 
	 * @return The parity workspace model.
	 */
	protected static WorkspaceModel getWorkspaceModel() {
		final WorkspaceModel workspaceModel = WorkspaceModel.getModel();
		return workspaceModel;
	}

    /**
     * Create a AbstractModel.
     */
    protected AbstractModel(final T impl) {
        super(impl);
    }
}

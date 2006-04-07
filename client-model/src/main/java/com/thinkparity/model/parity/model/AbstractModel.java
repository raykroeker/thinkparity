/*
 * Aug 6, 2005
 */
package com.thinkparity.model.parity.model;

import com.thinkparity.model.parity.model.workspace.WorkspaceModel;

/**
 * AbstractModel
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractModel {

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
	 * Create a AbstractModel
	 */
	protected AbstractModel() { super(); }
}

/*
 * Jul 23, 2005
 */
package com.thinkparity.model.parity.model.workspace;

import com.thinkparity.model.parity.model.AbstractModelImpl;

/**
 * WorkspaceModelImpl
 * @author raykroeker@gmail.com
 * @version 1.1
 * @see com.thinkparity.model.parity.model.workspace.WorkspaceModel
 */
class WorkspaceModelImpl extends AbstractModelImpl {

	/**
	 * Handle to the parity model workspace.
	 */
	private static final Workspace cachedWorkspace;

	static {
		cachedWorkspace = new WorkspaceHelper().openWorkspace();
	}

	/**
	 * Create a WorkspaceModelImpl
	 */
	WorkspaceModelImpl() { super(null); }

	/**
	 * Obtain the workspace for the parity model software.
	 * 
	 * @return The workspace.
	 */
	Workspace getWorkspace() { return WorkspaceModelImpl.cachedWorkspace; }
}


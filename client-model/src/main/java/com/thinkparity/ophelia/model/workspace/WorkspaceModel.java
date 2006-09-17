/*
 * Jul 23, 2005
 */
package com.thinkparity.ophelia.model.workspace;

import java.io.File;

import com.thinkparity.codebase.model.Context;

import com.thinkparity.ophelia.model.AbstractModel;

/**
 * WorkspaceModel
 * The workspace structure is as follows:
 * Win32:
 * The root of the workspace in a win32 environment is the %APPDATA% environment
 * variable followed by the corporation property followed by the product
 * property.  An example is as follows:
 * C:\Documents and Settings\Joe Blow\Application Data\
 * 	+>Parity Software
 * 		+>Parity
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class WorkspaceModel extends AbstractModel<WorkspaceModelImpl> {

    /**
     * Obtain a handle to a workspace model.
     * 
     * @return The workspace model interface.
     */
    public static InternalWorkspaceModel getInternalModel(final Context context) {
        return new InternalWorkspaceModel(context);
    }

    /**
     * Obtain a handle to a workspace model.
     * 
     * @return The workspace model interface.
     */
    public static WorkspaceModel getModel() {
        final WorkspaceModel workspaceModel = new WorkspaceModel();
        return workspaceModel;
    }

    /**
	 * Create a WorkspaceModel
	 */
	protected WorkspaceModel() {
		super(new WorkspaceModelImpl());
	}

    /**
	 * Obtain the handle to a workspace.
	 * 
	 * @return The handle to the workspace.
	 */
	public Workspace getWorkspace(final File workspace) {
		synchronized (getImplLock()) {
            return getImpl().getWorkspace(workspace);
		}
	}

	/**
     * Determine if this is the first run of the workspace.
     * 
     * @return True if this is the first run of the workspace; false otherwise.
     */
    public Boolean isFirstRun(final Workspace workspace) {
        synchronized (getImplLock()) {
            return getImpl().isFirstRun(workspace);
        }
    }
}

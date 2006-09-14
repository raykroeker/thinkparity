/*
 * Jul 23, 2005
 */
package com.thinkparity.ophelia.model.workspace;

import java.io.File;

import com.thinkparity.ophelia.model.AbstractModel;
import com.thinkparity.ophelia.model.Context;

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
public class WorkspaceModel extends AbstractModel {

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
	 * Internal implementation of the workspace model.
	 * @see WorkspaceModel#implLock
	 */
	private final WorkspaceModelImpl impl;

	/**
	 * Synchronization lock for the impl.
	 * @see WorkspaceModel#impl
	 */
	private final Object implLock;

    /**
	 * Create a WorkspaceModel
	 */
	protected WorkspaceModel() {
		super();
		impl = new WorkspaceModelImpl();
		implLock = new Object();
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

	/**
     * Obtain the workspace model implementation.
     * 
     * @return The workspace model implementation.
     */
    protected WorkspaceModelImpl getImpl() {
        return impl;
    }

    /**
     * Obtain the workspace model implementation synchronization lock.
     * 
     * @return The workspace model implementation synchronization lock.
     */
    protected Object getImplLock() {
        return implLock;
    }
}

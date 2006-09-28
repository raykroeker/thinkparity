/*
 * Jul 23, 2005
 */
package com.thinkparity.ophelia.model.workspace;

import java.io.File;

import com.thinkparity.codebase.model.Context;

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
public class WorkspaceModel {

    /** The workspace implementation synchronization lock. */
    private static final Object IMPL_LOCK;

    static {
        IMPL_LOCK = new Object();
    }

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

    /** The thinkParity workspace interface implementation. */
    private final WorkspaceModelImpl impl;

    /**
	 * Create a WorkspaceModel
	 */
	protected WorkspaceModel() {
		super();
        this.impl = new WorkspaceModelImpl();
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
     * Obtain the impl
     *
     * @return The WorkspaceModelImpl.
     */
    protected WorkspaceModelImpl getImpl() {
        return impl;
    }

    /**
     * Obtain the implementation synchroinization lock.
     * 
     * @return An implementation synchroinization lock.
     */
	protected Object getImplLock() {
        return IMPL_LOCK;
    }
}

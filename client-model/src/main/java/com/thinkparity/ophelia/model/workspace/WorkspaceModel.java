/*
 * Jul 23, 2005
 */
package com.thinkparity.ophelia.model.workspace;

import java.io.File;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;

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
    public static InternalWorkspaceModel getInternalModel(
            final Context context, final Environment environment) {
        return new InternalWorkspaceModel(context, environment);
    }

    /**
     * Obtain a handle to a workspace model.
     * 
     * @return The workspace model interface.
     */
    public static WorkspaceModel getModel(final Environment environment) {
        return new WorkspaceModel(environment);
    }

    /** The thinkParity workspace interface implementation. */
    private final WorkspaceModelImpl impl;

    /**
	 * Create a WorkspaceModel
	 */
	protected WorkspaceModel(final Environment environment) {
		super();
        this.impl = new WorkspaceModelImpl(environment);
	}

    /**
     * Delete a workspace.
     * 
     * @param workspace
     *            A thinkParity <code>Workspace</code>.
     */
    public void delete(final Workspace workspace) {
        synchronized (getImplLock()) {
            getImpl().delete(workspace);
        }
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
     * Initialize a workspace.
     * 
     * @param workspace
     *            A thinkParity <code>Workspace</code>.
     * @param credentials
     *            A user's <code>Credentials</code>.
     */
    public void initialize(final Workspace workspace,
            final Credentials credentials) {
        synchronized (getImplLock()) {
            getImpl().initialize(workspace, credentials);
        }
    }

    /**
     * Determine if the workspace has been initialized.
     * 
     * @param workspace
     *            A thinkParity <code>Workspace</code>.
     * @return True if the workspace has been initialized.
     */
    public Boolean isInitialized(final Workspace workspace) {
        synchronized (getImplLock()) {
            return getImpl().isInitialized(workspace);
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

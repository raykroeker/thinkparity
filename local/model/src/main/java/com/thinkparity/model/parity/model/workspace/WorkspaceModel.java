/*
 * Jul 23, 2005
 */
package com.thinkparity.model.parity.model.workspace;

import com.thinkparity.model.parity.model.AbstractModel;

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
	 * Lock used for synch operations.
	 */
	private static final Object LOCK = new Object();

	/**
	 * Create a WorkspaceModel.
	 * @return WorkspaceModel
	 */
	public static WorkspaceModel getModel() {
		synchronized(LOCK) { return new WorkspaceModel(); }
	}

	/**
	 * Internal implementation of the workspace model.
	 */
	private final WorkspaceModelImpl impl;

	/**
	 * Create a WorkspaceModel
	 */
	private WorkspaceModel() {
		super();
		impl = new WorkspaceModelImpl();
	}

	/**
	 * Obtain the workspace.
	 * @return <code>
	 */
	public Workspace getWorkspace() { return impl.getWorkspace(); }
}

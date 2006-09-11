/*
 * Created On: Mar 6, 2006
 */
package com.thinkparity.ophelia.model.index;


import com.thinkparity.ophelia.model.AbstractModel;
import com.thinkparity.ophelia.model.Context;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1.2.5
 */
public class IndexModel extends AbstractModel {

	/**
	 * Obtain an internal index model.
	 * 
	 * @param context
	 *            The parity context.
	 * @return An internal index model.
	 */
	public static InternalIndexModel getInternalModel(final Context context) {
		final Workspace workspace = getWorkspaceModel().getWorkspace();
		return new InternalIndexModel(workspace, context);
	}

	/**
	 * Obtain an index model.
	 * 
	 * @return The index model.
	 */
	public static IndexModel getModel() {
		final Workspace workspace = getWorkspaceModel().getWorkspace();
		return new IndexModel(workspace);
	}

	/** Implementation. */
	private final IndexModelImpl impl;

	/** Implementation synchronization lock. */
	private final Object implLock;

	/**
	 * Create a IndexModel.
	 */
	protected IndexModel(final Workspace workspace) {
		super();
		this.impl = new IndexModelImpl(workspace);
		this.implLock = new Object();
	}

	/**
	 * Obtain the implementation.
	 * 
	 * @return The implementation.
	 */
	protected IndexModelImpl getImpl() { return impl; }

	/**
	 * Obtain the implementation synchronization lock.
	 * 
	 * @return The implementation synchronization lock.
	 */
	protected Object getImplLock() { return implLock; }
}

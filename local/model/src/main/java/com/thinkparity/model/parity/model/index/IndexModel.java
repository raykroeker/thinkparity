/*
 * Mar 6, 2006
 */
package com.thinkparity.model.parity.model.index;

import java.util.List;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractModel;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
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

	/**
	 * Implementation.
	 * 
	 */
	private final IndexModelImpl impl;

	/**
	 * Implementation synchronization lock.
	 * 
	 */
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
	 * Search the index for hits containing the expression.
	 * 
	 * @param expression
	 *            The search expression.
	 * @return A list of index hits.
	 * @throws ParityException
	 */
	public List<IndexHit> search(final String expression)
			throws ParityException {
		synchronized(implLock) { return impl.search(expression); }
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

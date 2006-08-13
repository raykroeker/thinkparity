/*
 * Created On: Mar 6, 2006
 */
package com.thinkparity.model.parity.model.index;

import java.util.List;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractModel;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1.2.4
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
     * @deprecated => [{@link IndexModel#searchContainers(String)}|{@link IndexModel#searchDocuments(String)}]
     * 
     */
    @Deprecated
    public List<IndexHit> searchArtifact(final String expression) {
        throw Assert.createUnreachable("IndexModel#searchArtifact(java.lang.String) => [IndexModel.searchContainers(java.lang.String) | IndexModel.searchDocuments(java.lang.String)]");
    }

    /**
     * Search the index for containers containing the expression.
     * 
     * @param expression
     *            The search expression.
     * @return A list of index hits.
     * @throws ParityException
     */
    public List<IndexHit> searchContainers(final String expression) {
        synchronized(implLock) { return impl.searchContainers(expression); }
    }

    /**
     * Search the index for documents containing the expression.
     * 
     * @param expression
     *            The search expression.
     * @return A list of index hits.
     * @throws ParityException
     */
    public List<IndexHit> searchDocuments(final String expression)
            throws ParityException {
        synchronized(implLock) { return impl.searchDocuments(expression); }
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

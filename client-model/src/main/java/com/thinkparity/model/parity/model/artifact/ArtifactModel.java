/*
 * Mar 2, 2006
 */
package com.thinkparity.model.parity.model.artifact;

import com.thinkparity.model.parity.model.AbstractModel;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ArtifactModel extends AbstractModel {

	/**
	 * Obtain an internal artifact model.
	 * 
	 * @param context
	 *            The parity context.
	 * @return An internal artifact model.
	 */
	public static InternalArtifactModel getInternalModel(final Context context) {
		final Workspace workspace = getWorkspaceModel().getWorkspace();
		return new InternalArtifactModel(context, workspace);
	}

	/**
	 * Obtain an artifact model.
	 * 
	 * @return An artifact model.
	 */
	public static ArtifactModel getModel() {
		final Workspace workspace = getWorkspaceModel().getWorkspace();
		return new ArtifactModel(workspace);
	}

	/**
	 * The implementation.
	 * 
	 */
	private final ArtifactModelImpl impl;

	/**
	 * The implementation syncrhonization lock.
	 * 
	 */
	private final Object implLock;

	/**
	 * Create a ArtifactModel.
	 * 
	 */
	protected ArtifactModel(final Workspace workspace) {
		super();
		this.impl = new ArtifactModelImpl(workspace);
		this.implLock = new Object();
	}

	/**
	 * Determine whether or not an artifact has a flag applied.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @param flag
	 *            The artifact flag.
	 * @return True if the flag is applied; false otherwise.
	 */
	public Boolean isFlagApplied(final Long artifactId, final ArtifactFlag flag) {
		synchronized(implLock) { return impl.isFlagApplied(artifactId, flag); }
	}

	/**
	 * Obtain the implemenatation.
	 * 
	 * @return The implementation.
	 */
	protected ArtifactModelImpl getImpl() { return impl; }

	/**
	 * Obtain the implementation synchronization lock.
	 * 
	 * @return The implementation synchronization lock.
	 */
	protected Object getImplLock() { return implLock; }
}

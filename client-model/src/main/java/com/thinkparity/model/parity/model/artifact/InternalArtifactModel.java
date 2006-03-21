/*
 * Mar 2, 2006
 */
package com.thinkparity.model.parity.model.artifact;

import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class InternalArtifactModel extends ArtifactModel {

	/**
	 * Create a InternalArtifactModel.
	 * 
	 * @param context
	 *            The parity context.
	 * @param workspace
	 *            The workspace.
	 */
	InternalArtifactModel(final Context context, final Workspace workspace) {
		super(workspace);
		context.assertContextIsValid();
	}

	public void applyFlagKey(final Long artifactId) {
		synchronized(getImplLock()) { getImpl().applyFlagKey(artifactId); }
	}

	public void removeFlagKey(final Long artifactId) {
		synchronized(getImplLock()) { getImpl().removeFlagKey(artifactId); }
	}
}

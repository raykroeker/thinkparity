/*
 * Feb 13, 2006
 */
package com.thinkparity.model.parity.model.session;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.InternalModel;
import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class InternalSessionModel extends SessionModel implements InternalModel {

	/**
	 * Create a InternalSessionModel.
	 * 
	 * @param context
	 *            The model context.
	 */
	InternalSessionModel(final Workspace workspace, final Context context) {
		super(workspace);
		context.assertContextIsValid();
	}

	/**
	 * Obtain the artifact key holder.
	 * 
	 * @param artifactId
	 *            The artifact key holder.
	 * @return The artifact key holder.
	 * @throws ParityException
	 */
	public User getArtifactKeyHolder(final Long artifactId) throws ParityException {
		synchronized(getImplLock()) {
			return getImpl().getArtifactKeyHolder(artifactId);
		}
	}

	/**
	 * Obtain the currently logged in user.
	 * 
	 * @return The logged in user.
	 * @throws ParityException
	 */
	public User getLoggedInUser() throws ParityException {
		synchronized(getImplLock()) { return getImpl().getLoggedInUser(); }
	}

	/**
	 * Send a creation packet to the parity server.
	 * 
	 * @param document
	 *            The document.
	 * @throws ParityException
	 */
	public void sendCreate(final Artifact artifact) throws ParityException {
		synchronized(getImplLock()) { getImpl().sendCreate(artifact); }
	}
}

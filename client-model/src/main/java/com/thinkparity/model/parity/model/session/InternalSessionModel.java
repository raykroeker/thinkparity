/*
 * Feb 13, 2006
 */
package com.thinkparity.model.parity.model.session;

import java.util.List;

import com.thinkparity.codebase.assertion.NotTrueAssertion;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.InternalModel;
import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.xmpp.JabberId;
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

	public List<User> readUsers(final List<JabberId> jabberIds)
			throws ParityException {
		synchronized(getImplLock()) { return getImpl().readUsers(jabberIds); }
	}

	/**
	 * Send a close packet to the parity server.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @throws NotTrueAssertion
	 *             <ul>
	 *             <li>If the user is offline.
	 *             <li>If the logged in user is not the key holder.
	 *             </ul>
	 * @throws ParityException
	 */
	public void sendClose(final Long artifactId) throws ParityException {
		synchronized(getImplLock()) { getImpl().sendClose(artifactId); }
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

	/**
	 * Send a delete packet to the parity server.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @throws ParityException
	 * @throws NotTrueAssertion
	 *             <ul>
	 *             <li>If the user is offline.
	 *             </ul>
	 */
	public void sendDelete(final Long artifactId) throws ParityException {
		synchronized(getImplLock()) { getImpl().sendDelete(artifactId); }
	}
}

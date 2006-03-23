/*
 * Feb 24, 2006
 */
package com.thinkparity.model.parity.model.message.system;

import java.util.List;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class InternalSystemMessageModel extends SystemMessageModel {

	/**
	 * Create an InternalSystemMessageModel.
	 * 
	 * @param workspace
	 *            The parity workspace.
	 * @param context
	 *            The parity context.
	 */
	InternalSystemMessageModel(final Workspace workspace, final Context context) {
		super(workspace);
		context.assertContextIsValid();
	}

	public void createContactInvitation(final JabberId invitedBy) {
		synchronized(getImplLock()) {
			getImpl().createContactInvitation(invitedBy);
		}
	}

	public void createContactInvitationResponse(final JabberId responseBy,
			final Boolean didAcceptInvitation) {
		synchronized(getImplLock()) {
			getImpl().createContactInvitationResponse(responseBy, didAcceptInvitation);
		}
	}

	public void createKeyRequest(final Long artifactId, final JabberId requestedBy) {
		synchronized(getImplLock()) {
			getImpl().createKeyRequest(artifactId, requestedBy);
		}
	}

	public void createKeyResponse(final Long artifactId,
			final Boolean didAcceptRequest, final JabberId responseFrom) {
		synchronized(getImplLock()) {
			getImpl().createKeyResponse(artifactId, didAcceptRequest, responseFrom);
		}
	}

	/**
	 * Read all system messages for an artifact.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @return A list of system messages.
	 * @throws ParityException
	 */
	public List<SystemMessage> readForArtifact(final Long artifactId,
			final SystemMessageType type) throws ParityException {
		synchronized(getImplLock()) {
			return getImpl().readForArtifact(artifactId, type);
		}
	}
}

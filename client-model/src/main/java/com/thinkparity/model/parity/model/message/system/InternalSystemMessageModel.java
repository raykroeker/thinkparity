/*
 * Feb 24, 2006
 */
package com.thinkparity.model.parity.model.message.system;

import java.util.List;

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
		synchronized (getImplLock()) {
			getImpl().createContactInvitation(invitedBy);
		}
	}

	/**
	 * @deprecated The accept\decline contact invitation system messages are
	 * no longer required.
	 */
	public void createContactInvitationResponse(final JabberId responseBy,
			final Boolean didAcceptInvitation) {
		synchronized(getImplLock()) {
			getImpl().createContactInvitationResponse(responseBy, didAcceptInvitation);
		}
	}

	public SystemMessage createKeyRequest(final Long artifactId,
            final JabberId requestedBy) {
		synchronized(getImplLock()) {
			return getImpl().createKeyRequest(artifactId, requestedBy);
		}
	}

	public void createKeyResponse(final Long artifactId,
			final Boolean didAcceptRequest, final JabberId responseFrom) {
		synchronized(getImplLock()) {
			getImpl().createKeyResponse(artifactId, didAcceptRequest, responseFrom);
		}
	}

	/**
     * Delete a message.
     * 
     * @param messageId
     *            The message id.
     */
    @Override
    public void delete(final Long messageId) {
        synchronized(getImplLock()) { getImpl().delete(messageId); }
    }

	/**
     * Read the system messages of a given type for an artifact.
     * 
     * @param artifactId
     *            The artifact id.
     * @return A list of system messages.
     */
	public List<SystemMessage> readForArtifact(final Long artifactId,
            final SystemMessageType type) {
		synchronized(getImplLock()) {
			return getImpl().readForArtifact(artifactId, type);
		}
	}
}

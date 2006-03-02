/*
 * Feb 24, 2006
 */
package com.thinkparity.model.parity.model.message.system;

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
}

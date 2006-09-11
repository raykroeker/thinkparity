/*
 * Feb 24, 2006
 */
package com.thinkparity.ophelia.model.io.handler;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;


import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.ophelia.model.message.ContactInvitationMessage;
import com.thinkparity.ophelia.model.message.ContactInvitationResponseMessage;
import com.thinkparity.ophelia.model.message.KeyRequestMessage;
import com.thinkparity.ophelia.model.message.KeyResponseMessage;
import com.thinkparity.ophelia.model.message.SystemMessage;
import com.thinkparity.ophelia.model.message.SystemMessageType;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface SystemMessageIOHandler {
	public void create(final ContactInvitationMessage contactInvitation)
		throws HypersonicException;
	public void create(
			final ContactInvitationResponseMessage contactInvitationResponse)
			throws HypersonicException;
	public void create(final KeyRequestMessage keyRequestMessage)
		throws HypersonicException;
	public void create(final KeyResponseMessage keyResponseMessage)
		throws HypersonicException;
	public void delete(final Long messageId) throws HypersonicException;
	public List<SystemMessage> read() throws HypersonicException;
	public SystemMessage read(final Long messageId) throws HypersonicException;
	public ContactInvitationMessage readContactInvitation(final JabberId invitedBy)
		throws HypersonicException;
	public ContactInvitationResponseMessage readContactInvitationResponse(
			final JabberId responseBy) throws HypersonicException;
	public List<SystemMessage> readForArtifact(final Long artifactId)
		throws HypersonicException;
	public List<SystemMessage> readForArtifact(final Long artifactId,
			final SystemMessageType type) throws HypersonicException;
}

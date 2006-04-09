/*
 * Mar 30, 2006
 */
package com.thinkparity.model.xmpp.events;

import java.util.UUID;

import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.contact.Contact;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface XMPPArtifactListener {

    /**
     * Confirmation receipt that an artifact was received.
     * 
     * @param receivedBy
     *            By whom the artifact was received.
     */
    public void confirmReceipt(final UUID uniqueId, final JabberId receivedBy);

	public void teamMemberAdded(final UUID artifactUniqueId,
			final Contact teamMember);
	public void teamMemberRemoved(final UUID artifactUniqueId,
			final Contact teamMember);
}

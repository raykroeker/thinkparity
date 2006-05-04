/*
 * Created On:  Mar 30, 2006
 * $Id$
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
     * Confirm that an artifact was received.
     *
     * @param uniqueId
     *      The artifact unique id.
     * @param versionId
     *      The artifact version id.
     * @param receivedFrom
     *      From whom the the confirmation was sent.
     */
    public void confirmReceipt(final UUID uniqueId, final Long verionId,
            final JabberId receivedFrom);

	public void teamMemberAdded(final UUID artifactUniqueId,
			final Contact teamMember);
	public void teamMemberRemoved(final UUID artifactUniqueId,
			final Contact teamMember);
}

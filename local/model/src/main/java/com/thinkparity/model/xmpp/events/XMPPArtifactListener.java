/*
 * Mar 30, 2006
 */
package com.thinkparity.model.xmpp.events;

import java.util.UUID;

import com.thinkparity.model.xmpp.contact.Contact;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface XMPPArtifactListener {
	public void teamMemberAdded(final UUID artifactUniqueId,
			final Contact teamMember);
	public void teamMemberRemoved(final UUID artifactUniqueId,
			final Contact teamMember);
}

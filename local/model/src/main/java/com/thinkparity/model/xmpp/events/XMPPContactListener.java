/*
 * May 15, 2005
 */
package com.thinkparity.model.xmpp.events;

import com.thinkparity.model.xmpp.JabberId;

public interface XMPPContactListener {
	public void invitationExtended(final JabberId invitedBy);
	public void invitationAccepted(final JabberId acceptedBy);
	public void invitationDeclined(final JabberId declinedBy);
}

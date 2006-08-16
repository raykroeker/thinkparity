/*
 * May 15, 2005
 */
package com.thinkparity.model.xmpp.events;

import java.util.Calendar;

import com.thinkparity.model.xmpp.JabberId;

public interface XMPPContactListener {
	public void handleInvitationExtended(final JabberId invitedBy,
            final Calendar invitedOn);

    public void handleInvitationAccepted(final JabberId acceptedBy,
            final Calendar acceptedOn);

    public void handleInvitationDeclined(final JabberId declinedBy,
            final Calendar declinedOn);
}

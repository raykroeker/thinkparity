/*
 * May 15, 2005
 */
package com.thinkparity.ophelia.model.util.xmpp.events;

import java.util.Calendar;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.event.EventListener;
import com.thinkparity.codebase.jabber.JabberId;


public interface ContactListener extends EventListener {
    public void handleContactDeleted(final JabberId deletedBy,
            final Calendar deletedOn);
    public void handleContactUpdated(final JabberId contactId,
            final Calendar updatedOn);
	public void handleInvitationAccepted(final JabberId acceptedBy,
            final Calendar acceptedOn);
    public void handleInvitationDeclined(final EMail invitedAs,
            final JabberId declinedBy, final Calendar declinedOn);
    public void handleInvitationDeleted(final EMail invitedAs,
            final JabberId deletedBy, final Calendar deletedOn);
    public void handleInvitationExtended(final EMail invitedAs,
            final JabberId invitedBy, final Calendar invitedOn);
}

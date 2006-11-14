/*
 * May 15, 2005
 */
package com.thinkparity.ophelia.model.util.xmpp.event;

import com.thinkparity.codebase.event.EventListener;

import com.thinkparity.codebase.model.util.xmpp.event.ContactDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationAcceptedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationDeclinedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationExtendedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactUpdatedEvent;

/**
 * <b>Title:</b>thinkParity XMPP Contact Listener<br>
 * <b>Description:</b>An event listener monitoring for remote contact xmpp
 * events.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface ContactListener extends EventListener {
    public void handleDeleted(final ContactDeletedEvent event);
    public void handleInvitationAccepted(final ContactInvitationAcceptedEvent event);
	public void handleInvitationDeclined(final ContactInvitationDeclinedEvent event);
    public void handleInvitationDeleted(final ContactInvitationDeletedEvent event);
    public void handleInvitationExtended(final ContactInvitationExtendedEvent event);
    public void handleUpdated(final ContactUpdatedEvent event);
}

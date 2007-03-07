/*
 * May 15, 2005
 */
package com.thinkparity.ophelia.model.util.xmpp.event;

import com.thinkparity.codebase.model.util.xmpp.event.ContactDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactEMailInvitationDeclinedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactEMailInvitationDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactEMailInvitationExtendedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationAcceptedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactUpdatedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactUserInvitationDeclinedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactUserInvitationDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactUserInvitationExtendedEvent;

/**
 * <b>Title:</b>thinkParity XMPP Contact Listener<br>
 * <b>Description:</b>An event listener monitoring for remote contact xmpp
 * events.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface ContactListener extends XMPPEventListener {
    public void handleDeleted(final ContactDeletedEvent event);
    public void handleEMailInvitationDeclined(final ContactEMailInvitationDeclinedEvent event);
	public void handleEMailInvitationDeleted(final ContactEMailInvitationDeletedEvent event);
    public void handleEMailInvitationExtended(final ContactEMailInvitationExtendedEvent event);
    public void handleInvitationAccepted(final ContactInvitationAcceptedEvent event);
    public void handleUpdated(final ContactUpdatedEvent event);
    public void handleUserInvitationDeclined(final ContactUserInvitationDeclinedEvent event);
    public void handleUserInvitationDeleted(final ContactUserInvitationDeletedEvent event);
    public void handleUserInvitationExtended(final ContactUserInvitationExtendedEvent event);
}

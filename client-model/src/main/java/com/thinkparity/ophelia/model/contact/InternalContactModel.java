/*
 * Generated On: Jun 27 06 04:14:53 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.contact;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.util.jta.TransactionType;
import com.thinkparity.codebase.model.util.xmpp.event.ContactDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationAcceptedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationDeclinedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationExtendedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactUpdatedEvent;

import com.thinkparity.ophelia.model.util.ProcessMonitor;

/**
 * <b>Title:</b>thinkParity Internal Contact Model<br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.5
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface InternalContactModel extends ContactModel {

    /**
     * Download the contacts from the server and create local contacts.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     */
    public void download(final ProcessMonitor monitor);

    /**
     * Handle the remote event generated when a contact is deleted.
     * 
     * @param deletedBy
     *            By whom the contact was deleted <code>JabberId</code>.
     * @param deletedOn
     *            When the contact was deleted <code>Calendar</code>.
     */
    public void handleContactDeleted(final ContactDeletedEvent event);

    /**
     * Handle the remote event generated when a contact is updated.
     * 
     * @param contactId
     *            A contact id <code>JabberId</code>.
     * @param updatedOn
     *            When the contact was updated <code>Calendar</code>.
     */
    public void handleContactUpdated(final ContactUpdatedEvent event);

    /**
     * Handle the invitation extended remote event.
     * 
     * @param acceptedBy
     *            By whom the invitation was accepted.
     * @param acceptedOn
     *            When the invitation was accepted.
     */
    public void handleInvitationAccepted(
            final ContactInvitationAcceptedEvent event);

    /**
     * Handle the invitation extended remote event.
     * 
     * @param invitedAs
     *            The original invitation e-mail address.
     * @param declinedBy
     *            By whom the invitation was declined.
     * @param declinedOn
     *            When the invitation was declined.
     */
    public void handleInvitationDeclined(
            final ContactInvitationDeclinedEvent event);

    /**
     * Handle the remote contact invitation deleted remote event.
     * 
     * @param invitedAs
     *            The original invitation e-mail address.
     * @param deletedBy
     *            By whom the invitation was deleted.
     * @param deletedOn
     *            When the invitation was deleted.
     */
    public void handleInvitationDeleted(
            final ContactInvitationDeletedEvent event);

    /**
     * Handle the invitation extended remote event.
     * 
     * @param invitedBy
     *            By whom the invitation was extended.
     * @param invitedOn
     *            When the invitation was extended.
     */
    public void handleInvitationExtended(
            final ContactInvitationExtendedEvent event);
}

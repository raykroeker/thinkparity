/*
 * Generated On: Jun 27 06 04:14:53 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.contact;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.util.jta.TransactionType;
import com.thinkparity.codebase.model.util.xmpp.event.ContactDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactEMailInvitationDeclinedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactEMailInvitationDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactEMailInvitationExtendedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationAcceptedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactUpdatedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactUserInvitationDeclinedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactUserInvitationDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactUserInvitationExtendedEvent;

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
     * Handle the e-mail invitation extended remote event.
     * 
     * @param event
     *            A <code>ContactEmailInvitationDeclinedEvent</code>.
     */
    public void handleEMailInvitationDeclined(
            final ContactEMailInvitationDeclinedEvent event);

    /**
     * Handle the remote contact invitation deleted remote event.
     * 
     * @param event
     *            A <code>ContactEMailInvitationDeletedEvent</code>.
     */
    public void handleEMailInvitationDeleted(
            final ContactEMailInvitationDeletedEvent event);

    /**
     * Handle the invitation extended remote event.
     * 
     * @param event
     *            A <code>ContactEmailInvitationEvent</code>.
     */
    public void handleEMailInvitationExtended(
            final ContactEMailInvitationExtendedEvent event);

    /**
     * Handle the invitation extended remote event.
     * 
     * @param event
     *            A <code>ContactInvitationAcceptedEvent</code>.
     */
    public void handleInvitationAccepted(
            final ContactInvitationAcceptedEvent event);

    /**
     * Handle the user invitation declined remote event.
     * 
     * @param event
     *            A <code>ContactEmailInvitationDeclinedEvent</code>.
     */
    public void handleUserInvitationDeclined(
            final ContactUserInvitationDeclinedEvent event);

    /**
     * Handle the remote contact invitation deleted remote event.
     * 
     * @param event
     *            A <code>ContactUserInvitationDeletedEvent</code>.
     */
    public void handleUserInvitationDeleted(
            final ContactUserInvitationDeletedEvent event);

    /**
     * Handle the invitation extended remote event.
     * 
     * @param event
     *            A <code>ContactUserInvitationExtendedEvent</code>.
     */
    public void handleUserInvitationExtended(
            final ContactUserInvitationExtendedEvent event);
}

/*
 * Generated On: Jun 27 06 04:14:53 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.contact;

import java.util.Comparator;
import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.util.jta.TransactionType;

import com.thinkparity.ophelia.model.events.ContactListener;
import com.thinkparity.ophelia.model.util.filter.Filter;

/**
 * <b>Title:</b>thinkParity Contact Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.5
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface ContactModel {

	/**
     * Accept the incoming invitation.
     * 
     * @param invitationId
     *            An invitation id.
     */
    public void acceptIncomingInvitation(final Long invitationId);

    /**
     * Add a contact listener.
     * 
     * @param listener
     *            A contact listener.
     */
    @ThinkParityTransaction(TransactionType.NEVER)
    public void addListener(final ContactListener listener);

    /**
     * Create an e-mail contact invitation.
     * 
     * @param email
     *            An e-mail address.
     * @return The new contact invitation.
     */
    public OutgoingInvitation createOutgoingInvitation(final EMail email);

	/**
     * Accept the incoming invitation.
     * 
     * @param invitationId
     *            An invitation id.
     */
    public void declineIncomingInvitation(final Long invitationId);

	/**
     * Delete a contact.
     * 
     * @param contactId
     *            A contact jabber id.
     * @return A contact.
     */
    public void delete(final JabberId contactId);
	/**
     * Delete an outgoing invitation.
     * 
     * @param invitationId
     *            An invitation id.
     */
    public void deleteOutgoingInvitation(final Long invitationId);

    /**
     * Download the contacts from the server and create local contacts.
     *
     */
    public void download();

    /**
     * Read a list of contacts.
     * 
     * @return A list of contacts.
     */
    public List<Contact> read();

    /**
     * Read a list of contacts.
     * 
     * @param comparator
     *            A contact comparator to sort the contacts.
     * @return A list of contacts.
     */
    public List<Contact> read(final Comparator<Contact> comparator);

    /**
     * Read a list of contacts.
     * 
     * @param comparator
     *            A contact comparator to sort the contacts.
     * @param filter
     *            A contact filter to scope the contacts.
     * @return A list of contacts.
     */
    public List<Contact> read(final Comparator<Contact> comparator,
            final Filter<? super Contact> filter);

    /**
     * Read a list of contacts.
     * 
     * @param filter
     *            A contact filter to scope the contacts.
     * @return A list of contacts.
     */
    public List<Contact> read(final Filter<? super Contact> filter);

    /**
     * Read a contact.
     * 
     * @param contactId
     *            A contact jabber id.
     * @return A contact.
     */
    public Contact read(final JabberId contactId);

    /**
     * Read an outgoing invitation.
     * 
     * @param invitationId
     *            An invitation id.
     * @return An outgoing invitation.
     */
    public IncomingInvitation readIncomingInvitation(final Long invitationId);

    /**
     * Read a list of incoming invitations.
     * 
     * @return A list of incoming invitations.
     */
    public List<IncomingInvitation> readIncomingInvitations();

    /**
     * Read a list of incoming invitations.
     * 
     * @return A list of incoming invitations.
     */
    public List<IncomingInvitation> readIncomingInvitations(
            final Comparator<ContactInvitation> comparator);

    /**
     * Read a list of incoming invitations.
     * 
     * @return A list of incoming invitations.
     */
    public List<IncomingInvitation> readIncomingInvitations(
            final Comparator<ContactInvitation> comparator,
            final Filter<? super ContactInvitation> filter);

    /**
     * Read a list of incoming invitations.
     * 
     * @return A list of incoming invitations.
     */
    public List<IncomingInvitation> readIncomingInvitations(
            final Filter<? super ContactInvitation> filter);


    /**
     * Read an outgoing invitation.
     * 
     * @param invitationId
     *            An invitation id.
     * @return An outgoing invitation.
     */
    public OutgoingInvitation readOutgoingInvitation(final Long invitationId);

    /**
     * Read a list of outgoing invitations.
     * 
     * @return A list of outgoing invitations.
     */
    public List<OutgoingInvitation> readOutgoingInvitations();

    /**
     * Read a list of outgoing invitations.
     * 
     * @return A list of outgoing invitations.
     */
    public List<OutgoingInvitation> readOutgoingInvitations(
            final Comparator<ContactInvitation> comparator) ;

    /**
     * Read a list of outgoing invitations.
     * 
     * @return A list of outgoing invitations.
     */
    public List<OutgoingInvitation> readOutgoingInvitations(
            final Comparator<ContactInvitation> comparator,
            final Filter<? super ContactInvitation> filter);


    /**
     * Read a list of outgoing invitations.
     * 
     * @return A list of outgoing invitations.
     */
    public List<OutgoingInvitation> readOutgoingInvitations(
            final Filter<? super ContactInvitation> filter);

    /**
     * Remove a contact listener.
     * 
     * @param listener
     *            A contact listener.
     */
    @ThinkParityTransaction(TransactionType.NEVER)
    public void removeListener(final ContactListener listener);

    /**
     * Search for contacts.
     * 
     * @param expression
     *            A search expression.
     * @return A <code>List&lt;JabberId&gt;</code>.
     */
    public List<JabberId> search(final String expression);
}

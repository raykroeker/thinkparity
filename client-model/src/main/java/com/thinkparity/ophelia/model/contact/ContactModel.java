/*
 * Generated On: Jun 27 06 04:14:53 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.contact;

import java.util.Comparator;
import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;
import com.thinkparity.codebase.model.util.jta.TransactionType;

import com.thinkparity.ophelia.model.events.ContactListener;

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
     * Accept the incoming e-mail invitation.
     * 
     * @param invitationId
     *            An invitation id.
     */
    public void acceptIncomingEMailInvitation(final Long invitationId);

    /**
     * Accept the incoming user invitation.
     * 
     * @param invitationId
     *            An invitation id.
     */
    public void acceptIncomingUserInvitation(final Long invitationId);

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
     * @return The new <code>OutgoingEMailInvitation</code>.
     */
    public OutgoingEMailInvitation createOutgoingEMailInvitation(
            final EMail email);

    /**
     * Create a contact invitation.
     * 
     * @param userId
     *            A user id <code>Long</code>.
     * @return The new <code>OutgoingUserInvitation</code>.
     */
    public OutgoingUserInvitation createOutgoingUserInvitation(final Long userId);

    /**
     * Accept the incoming invitation.
     * 
     * @param invitationId
     *            An invitation id.
     */
    public void declineIncomingEMailInvitation(final Long invitationId);
    public void declineIncomingUserInvitation(final Long invitationId);

	/**
     * Delete a contact.
     * 
     * @param contactId
     *            A contact jabber id.
     * @return A contact.
     */
    public void delete(final JabberId contactId);

	/**
     * Delete an outgoing e-mail invitation.
     * 
     * @param invitationId
     *            An invitation id.
     */
    public void deleteOutgoingEMailInvitation(final Long invitationId);

    /**
     * Delete an outgoing user invitation.
     * 
     * @param invitationId
     *            An invitation id.
     */
    public void deleteOutgoingUserInvitation(final Long invitationId);

    /**
     * Determine if the contact exists.
     * 
     * @param contactId
     *            A contact id <code>Long</code>.
     * @return True if the contact exists; false otherwise.
     */
    public Boolean doesExist(final Long contactId);

    /**
     * Determine if the outgoing user invitation exists.
     * 
     * @param userId
     *            A user id <code>Long</code>.
     * @return True if there exists an outgoing user invitation for the user.
     */
    public Boolean doesExistOutgoingUserInvitationForUser(final Long userId);

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
     * Read a list of container publish to contacts.
     * 
     * @return A <code>List</code> of <code>Contact</code>s.
     */
    public List<Contact> readContainerPublishTo();

    public IncomingEMailInvitation readIncomingEMailInvitation(
            final Long invitationId);
    public List<IncomingEMailInvitation> readIncomingEMailInvitations();
    public List<IncomingUserInvitation> readIncomingUserInvitations();
    public IncomingUserInvitation readIncomingUserInvitation(
            final Long invitationId);

    /**
     * Read an outgoing email invitation.
     * 
     * @param invitationId
     *            An invitation id.
     * @return An outgoing invitation.
     */
    public OutgoingEMailInvitation readOutgoingEMailInvitation(
            final Long invitationId);

    /**
     * Read a list of outgoing e-mail invitations.
     * 
     * @return A list of outgoing invitations.
     */
    public List<OutgoingEMailInvitation> readOutgoingEMailInvitations();

    /**
     * Read an outgoing email invitation.
     * 
     * @param invitationId
     *            An invitation id.
     * @return An outgoing user invitation.
     */
    public OutgoingUserInvitation readOutgoingUserInvitation(
            final Long invitationId);

    
    /**
     * Read a list of outgoing user invitations.
     * 
     * @return A list of outgoing invitations.
     */
    public List<OutgoingUserInvitation> readOutgoingUserInvitations();

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

    /**
     * Search for incoming e-mail invitations.
     * 
     * @param expression
     *            A search expression.
     * @return A <code>List&lt;Long&gt;</code>.
     */
    public List<Long> searchIncomingEMailInvitations(final String expression);

    /**
     * Search for incoming e-mail invitations.
     * 
     * @param expression
     *            A search expression.
     * @return A <code>List&lt;Long&gt;</code>.
     */
    public List<Long> searchIncomingUserInvitations(final String expression);

    /**
     * Search for outgoing invitations.
     * 
     * @param expression
     *            A search expression.
     * @return A <code>List&lt;Long&gt;</code>.
     */
    public List<Long> searchOutgoingEMailInvitations(final String expression);

    /**
     * Search for outgoing invitations.
     * 
     * @param expression
     *            A search expression.
     * @return A <code>List&lt;Long&gt;</code>.
     */
    public List<Long> searchOutgoingUserInvitations(final String expression);
}

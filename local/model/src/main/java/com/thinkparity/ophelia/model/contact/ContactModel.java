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

import com.thinkparity.codebase.model.annotation.ThinkParityConcurrency;
import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;
import com.thinkparity.codebase.model.util.concurrent.Lock;
import com.thinkparity.codebase.model.util.jta.TransactionType;

import com.thinkparity.ophelia.model.annotation.ThinkParityOnline;
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
     * Accept the incoming user invitation.
     * 
     * @param invitation
     *            An <code>IncomingUserInvitation</code>.
     */
    public void acceptInvitation(final IncomingUserInvitation invitation);

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
    @ThinkParityOnline
    public OutgoingEMailInvitation createOutgoingEMailInvitation(
            final EMail email);

    /**
     * Create a contact invitation.
     * 
     * @param userId
     *            A user id <code>Long</code>.
     * @return The new <code>OutgoingUserInvitation</code>.
     */
    @ThinkParityOnline
    public OutgoingUserInvitation createOutgoingUserInvitation(final Long userId);

	/**
     * Decline an incoming e-mail invitation.
     * 
     * @param invitation
     *            An <code>IncomingEMailInvitation</code>.
     */
    public void declineInvitation(final IncomingEMailInvitation invitation);

    /**
     * Decline an incoming user invitation.
     * 
     * @param invitation
     *            An <code>IncomingUserInvitation</code>.
     */
    public void declineInvitation(final IncomingUserInvitation invitation);

    /**
     * Determine if a contact exists for an e-mail address.
     * 
     * @param email
     *            An <code>EMail</code>.
     * @return True if a contact exists.
     */
    public Boolean doesExist(final EMail email);

    /**
     * Determine if the contact exists.
     * 
     * @param contactId
     *            A contact id <code>Long</code>.
     * @return True if the contact exists; false otherwise.
     */
    @ThinkParityConcurrency(Lock.LOCAL_READ)
    public Boolean doesExist(final Long contactId);

    /**
     * Determine if the outgoing user invitation exists.
     * 
     * @param userId
     *            A user id <code>Long</code>.
     * @return True if there exists an outgoing user invitation for the user.
     */
    @ThinkParityConcurrency(Lock.LOCAL_READ)
    public Boolean doesExistOutgoingUserInvitationForUser(final Long userId);

	/**
     * Read a list of contacts.
     * 
     * @return A list of contacts.
     */
    @ThinkParityConcurrency(Lock.LOCAL_READ)
    public List<Contact> read();

	/**
     * Read a list of contacts.
     * 
     * @param comparator
     *            A contact comparator to sort the contacts.
     * @return A list of contacts.
     */
    @ThinkParityConcurrency(Lock.LOCAL_READ)
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
    @ThinkParityConcurrency(Lock.LOCAL_READ)
    public List<Contact> read(final Comparator<Contact> comparator,
            final Filter<? super Contact> filter);

    /**
     * Read a contact.
     * 
     * @param email
     *            An <code>EMail</code> address.
     * @return A <code>Contact</code>.
     */
    public Contact read(final EMail email);

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
    @ThinkParityConcurrency(Lock.LOCAL_READ)
    public Contact read(final JabberId contactId);

    /**
     * Read a contact.
     * 
     * @param contactId
     *            A contact id <code>Long</code>.
     * @return A contact.
     */
    @ThinkParityConcurrency(Lock.LOCAL_READ)
    public Contact read(final Long contactId);

    /**
     * Read a list of container publish to contacts.
     * 
     * @return A <code>List</code> of <code>Contact</code>s.
     */
    public List<Contact> readContainerPublishTo();

    @ThinkParityConcurrency(Lock.LOCAL_READ)
    public IncomingEMailInvitation readIncomingEMailInvitation(
            final Long invitationId);

    @ThinkParityConcurrency(Lock.LOCAL_READ)
    public List<IncomingEMailInvitation> readIncomingEMailInvitations();

    @ThinkParityConcurrency(Lock.LOCAL_READ)
    public IncomingUserInvitation readIncomingUserInvitation(
            final Long invitationId);

    @ThinkParityConcurrency(Lock.LOCAL_READ)
    public List<IncomingUserInvitation> readIncomingUserInvitations();

    /**
     * Read an outgoing email invitation.
     * 
     * @param invitationId
     *            An invitation id.
     * @return An outgoing invitation.
     */
    @ThinkParityConcurrency(Lock.LOCAL_READ)
    public OutgoingEMailInvitation readOutgoingEMailInvitation(
            final Long invitationId);

    /**
     * Read a list of outgoing e-mail invitations.
     * 
     * @return A list of outgoing invitations.
     */
    @ThinkParityConcurrency(Lock.LOCAL_READ)
    public List<OutgoingEMailInvitation> readOutgoingEMailInvitations();

    /**
     * Read an outgoing email invitation.
     * 
     * @param invitationId
     *            An invitation id.
     * @return An outgoing user invitation.
     */
    @ThinkParityConcurrency(Lock.LOCAL_READ)
    public OutgoingUserInvitation readOutgoingUserInvitation(
            final Long invitationId);

    /**
     * Read a list of outgoing user invitations.
     * 
     * @return A list of outgoing invitations.
     */
    @ThinkParityConcurrency(Lock.LOCAL_READ)
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
    public List<Long> search(final String expression);

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

    /**
     * Accept the incoming e-mail invitation.
     * 
     * @param invitation
     *            An <code>IncomingEMailInvitation</code>.
     */
    void acceptInvitation(IncomingEMailInvitation invitation);

    /**
     * Delete a contact.
     * 
     * @param contact
     *            A <code>Contact</code>.
     */
    void delete(Contact contact);

    /**
     * Delete an outgoing e-mail invitation.
     * 
     * @param invitation
     *            An <code>OutgoingEMailInvitation</code>.
     */
    @ThinkParityOnline
    void deleteInvitation(OutgoingEMailInvitation invitation);

    /**
     * Delete an outgoing user invitation.
     * 
     * @param invitation
     *            An <code>OutgoingUserInvitation</code>.
     */
    void deleteInvitation(OutgoingUserInvitation invitation);

    /**
     * Determine whether or not creating an invitation is restricted to any of
     * the e-mail addresses.
     * 
     * @param emails
     *            A <code>List<EMail></code>.
     * @return True if invite is restricted.
     */
    Boolean isInviteRestricted(final List<EMail> emailList);
}

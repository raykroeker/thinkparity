/*
 * Created On: Jul 7, 2006 2:18:59 PM
 */
package com.thinkparity.ophelia.model.io.handler;

import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.ContactInvitation;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;
import com.thinkparity.codebase.model.user.User;

/**
 * <b>Title:</b>thinkParity OpheliaModel Contact IO Handler<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface ContactIOHandler {

    /**
     * Create a contact.
     * 
     * @param contact
     *            A contact.
     */
    public void create(final Contact contact);

    /**
     * Create an incoming invitation.
     * 
     * @param incoming
     *            An incoming invitation.
     */
    public void createInvitation(final IncomingEMailInvitation invitation);

    /**
     * Create an incoming invitation.
     * 
     * @param incoming
     *            An incoming invitation.
     */
    public void createInvitation(final IncomingUserInvitation invitation);

    /**
     * Create an outgoing invitation.
     * 
     * @param outgoing
     *            An <code>OutgoingEMailInvitation</code>.
     */
    public void createInvitation(final OutgoingEMailInvitation invitation);

    /**
     * Create an outgoing invitation.
     * 
     * @param outgoing
     *            An <code>OutgoingUserInvitation</code>.
     */
    public void createInvitation(final OutgoingUserInvitation invitation);

    /**
     * Delete a contact.
     * 
     * @param contact
     *            A <code>Contact</code>.
     */
    public void delete(final Contact contact);

    /**
     * Delete an incoming e-mail invitation.
     * 
     * @param invitation
     *            An <code>IncomingEMailInvitation</code>.
     */
    public void deleteInvitation(final IncomingEMailInvitation invitation);

    /**
     * Delete an incoming user invitation.
     * 
     * @param invitation
     *            An <code>IncomingUserInvitation</code>.
     */
    public void deleteInvitation(final IncomingUserInvitation invitation);

    /**
     * Delete an outgoing e-mail invitation.
     * 
     * @param invitation
     *            An <code>OutgoingEMailInvitation</code>.
     */
    public void deleteInvitation(final OutgoingEMailInvitation invitation);

    /**
     * Delete an outgoing user invitation.
     * 
     * @param invitation
     *            An <code>OutgoingUserInvitation</code>.
     */
    public void deleteInvitation(final OutgoingUserInvitation invitation);

    /**
     * Determine if a contact exists for an e-mail address.
     * 
     * @param email
     *            An <code>EMail</code> address.
     * @return True if a contact exists.
     */
    public Boolean doesExist(final EMail email);

    /**
     * Determine whether or not a contact exists.
     * 
     * @param contactId
     *            A contact id <code>Long</code>.
     * @return True if the contact does exist.
     */
    public Boolean doesExist(final Long contactId);
    
    /**
     * Determine whether or not an invitation exists.
     * 
     * @param invitation
     *            A <code>ContactInvitation</code>.
     * @return True if it exists.
     */
    public Boolean doesExistInvitation(final ContactInvitation invitation);

    /**
     * Determine whether or not an outgoing e-mail invitation exists.
     * 
     * @param email
     *            An <code>EMail</code> address.
     * @return True if the invitation does exist.
     */
    public Boolean doesExistOutgoingEMailInvitation(final EMail email);

    /**
     * Determine whether or not an outgoing user invitation exists.
     * 
     * @param userId
     *            A user id <code>Long</code>.
     * @return True if the invitation does exist.
     */
    public Boolean doesExistOutgoingUserInvitation(final Long userId);

    /**
     * Read a list of contacts.
     * 
     * @return A list of contacts.
     */
    public List<Contact> read();

    /**
     * Read a contact.
     * 
     * @param email
     *            An <code>EMail</code> address.
     * @return A <code>Contact</code>.
     */
    public Contact read(final EMail email);

    /**
     * Read a contact.
     * 
     * @param contactId
     *            A contact id.
     * @return A contact.
     */
    public Contact read(final JabberId contactId);

    /**
     * Read a contact.
     * 
     * @param contactId
     *            A contact id.
     * @return A contact.
     */
    public Contact read(final Long contactId);

    /**
     * Read an incoming e-mail invitation.
     * 
     * @param email
     *            The <code>EMail</code> address the invitation was sent to.
     * @param extendedBy
     *            The <code>User</code> the invitation was extended by.
     * @return An <code>IncomingEMailInvitation</code>.
     */
    public IncomingEMailInvitation readIncomingEMailInvitation(
            final EMail email, final User extendedBy);

    /**
     * Read a specific incoming e-mail invitation.
     * 
     * @param invitationId
     *            An invitation id <code>Long</code>.
     * @return An <code>IncomingEMailInvitation</code>.
     */
    public IncomingEMailInvitation readIncomingEMailInvitation(
            final Long invitationId);

    /**
     * Read all incoming e-mail invitations.
     * 
     * @return A <code>List</code> of <code>IncomingEMailInvitation</code>s.
     */
    public List<IncomingEMailInvitation> readIncomingEMailInvitations();

    /**
     * Read a series of incoming e-mail invitations.
     * 
     * @param extendedBy
     *            The extended by <code>User</code>.
     * @return A <code>List</code> of <code>IncomingEMailInvitation</code>s.
     */
    public List<IncomingEMailInvitation> readIncomingEMailInvitations(
            final User extendedBy);

    /**
     * Read a specific incoming user invitation.
     * 
     * @param invitationId
     *            An invitation id <code>Long</code>.
     * @return An <code>IncomingUserInvitation</code>.
     */
    public IncomingUserInvitation readIncomingUserInvitation(
            final Long invitationId);

    /**
     * Read a specific incoming user invitation
     * 
     * @param extendedBy
     *            An extended by <code>User</code>.
     * @return An <code>IncomingUserInvitation</code>.
     */
    public IncomingUserInvitation readIncomingUserInvitation(
            final User extendedBy);

    /**
     * Read all incoming user invitations.
     * 
     * @return A <code>List</code> of <code>IncomingUserInvitation</code>s.
     */
    public List<IncomingUserInvitation> readIncomingUserInvitations();

    /**
     * Read an outgoing invitation.
     * 
     * @param email
     *            An e-mail address.
     * @return An outgoing invitation.
     */
    public OutgoingEMailInvitation readOutgoingEMailInvitation(final EMail email);

    /**
     * Read an outgoing invitation.
     * 
     * @param email
     *            An e-mail address.
     * @return An outgoing invitation.
     */
    public OutgoingEMailInvitation readOutgoingEMailInvitation(
            final Long invitationId);

    /**
     * Read outgoing e-mail invitations.
     * 
     * @return A list of outgoing invitation.
     */
    public List<OutgoingEMailInvitation> readOutgoingEMailInvitations();

    /**
     * Read a series of outgoing e-mail invitations for a list of e-mail
     * addresses.
     * 
     * @param emails
     *            A <code>List</code> of <code>EMail</code> addresses.
     * @return A <code>List</code> of <code>OutgoingEMailInvitation</code>s.
     */
    public List<OutgoingEMailInvitation> readOutgoingEMailInvitations(
            final List<EMail> emails);

    /**
     * Read a specific outgoing invitation.
     * 
     * @param invitationId
     *            An invitation id <code>Long</code>.
     * @return An <code>OutgoingUserInvitation</code>.
     */
    public OutgoingUserInvitation readOutgoingUserInvitation(
            final Long invitationId);

    /**
     * Read outgoing user invitations for the invitation user.
     * 
     * @param invitationUser
     *            A <code>User</code>.
     * @return A <code>List</code> of <code>OutgoingUserInvitation</code>s.
     */
    public OutgoingUserInvitation readOutgoingUserInvitation(
            final User invitationUser);

    /**
     * Read all outgoing user invitations.
     * 
     * @return A <code>List</code> of <code>OutgoingUserInvitation</code>s.
     */
    public List<OutgoingUserInvitation> readOutgoingUserInvitations();

    /**
     * Update a contact.
     * 
     * @param contact
     *            A <code>Contact</code>.
     */
    public void update(final Contact contact);
}

/*
 * Created On: Jul 7, 2006 2:18:59 PM
 */
package com.thinkparity.ophelia.model.io.handler;

import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.contact.IncomingInvitation;
import com.thinkparity.ophelia.model.contact.OutgoingEMailInvitation;
import com.thinkparity.ophelia.model.contact.OutgoingUserInvitation;

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
     * Create an email.
     * 
     * @param contactId
     *            A contact id <code>Long</code>.
     * @param email
     *            An <code>EMail</code>.
     */
    public void createEmail(final Long contactId, final EMail email);

    /**
     * Create an incoming invitation.
     * 
     * @param incoming
     *            An incoming invitation.
     */
    public void createIncomingInvitation(final IncomingInvitation incoming,
            final User user);

    /**
     * Create an outgoing invitation.
     * 
     * @param outgoing
     *            An <code>OutgoingEMailInvitation</code>.
     */
    public void createOutgoingInvitation(final OutgoingEMailInvitation outgoing);

    /**
     * Create an outgoing invitation.
     * 
     * @param outgoing
     *            An <code>OutgoingUserInvitation</code>.
     */
    public void createOutgoingInvitation(final OutgoingUserInvitation outgoing);

    /**
     * Delete a contact.
     * 
     * @param contactId
     *            A contact id <code>JabberId</code>.
     */
    public void delete(final Long contactId);

    /**
     * Delete an email.
     * 
     * @param contactId
     *            A contact id <code>Long</code>.
     * @param email
     *            An <code>EMail</code>.
     */
    public void deleteEmail(final Long contactId, final EMail email);

    /**
     * Delete an email.
     * 
     * @param email
     *            An <code>EMail</code>.
     */
    public void deleteEMail(final EMail email);

    /**
     * Delete an incoming invitation.
     * 
     * @param invitationId
     *            An invitation id.
     */
    public void deleteIncomingInvitation(final Long invitationId);

    /**
     * Delete an outgoing invitation.
     * 
     * @param invitationId
     *            An invitation id.
     */
    public void deleteOutgoingEMailInvitation(final Long invitationId);

    /**
     * Delete an outgoing invitation.
     * 
     * @param invitationId
     *            An invitation id.
     */
    public void deleteOutgoingUserInvitation(final Long invitationId);

    /**
     * Determine whether or not a contact exists.
     * 
     * @param contactId
     *            A contact id <code>Long</code>.
     * @return True if the contact does exist.
     */
    public Boolean doesExist(final Long contactId);

    /**
     * Determine whether or not a contact exists.
     * 
     * @param userId
     *            A user id <code>Long</code>.
     * @return True if the contact does exist.
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
     * @param contactId
     *            A contact id.
     * @return A contact.
     */
    public Contact read(final JabberId contactId);

    // TODO-javadoc ContactIOHandler#readEmails()
    public List<EMail> readEmails(final Long contactId);

    /**
     * Read an incoming invitation.
     * 
     * @param invitationId
     *            An invitation id.
     * @return An incoming invitation.
     */
    public IncomingInvitation readIncomingInvitation(final JabberId invitedBy);

    /**
     * Read an incoming invitation.
     * 
     * @param invitationId
     *            An invitation id.
     * @return An incoming invitation.
     */
    public IncomingInvitation readIncomingInvitation(final Long invitationId);

    /**
     * Read incoming invitations.
     * 
     * @return A list of incoming invitation.
     */
    public List<IncomingInvitation> readIncomingInvitations();

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
     * Read an outgoing invitation.
     * 
     * @param userId
     *            A user id <code>Long</code>.
     * @return An <code>OutgoingUserInvitation</code>.
     */
    public OutgoingUserInvitation readOutgoingUserInvitation(
            final JabberId userId);

    /**
     * Read an outgoing invitation.
     * 
     * @param invitationId
     *            An invitation id <code>Long</code>.
     * @return An <code>OutgoingUserInvitation</code>.
     */
    public OutgoingUserInvitation readOutgoingUserInvitation(
            final Long invitationId);

    /**
     * Read outgoing user invitations.
     * 
     * @return A list of outgoing invitation.
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

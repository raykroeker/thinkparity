/*
 * Created On: Jul 7, 2006 2:18:59 PM
 */
package com.thinkparity.model.parity.model.io.handler;

import java.util.List;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.model.parity.model.contact.IncomingInvitation;
import com.thinkparity.model.parity.model.contact.OutgoingInvitation;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raymond@thinkparity.com
 * @version
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
    public void createIncomingInvitation(final IncomingInvitation incoming,
            final User user);

    /**
     * Create an outgoing invitation.
     * 
     * @param outgoing
     *            An outgoing invitation.
     */
    public void createOutgoingInvitation(final OutgoingInvitation outgoing);

    /**
     * Delete a contact.
     * 
     * @param contactId
     *            A contact id <code>JabberId</code>.
     */
    public void delete(final Long contactId);

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
    public void deleteOutgoingInvitation(final Long invitationId);

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

    /**
     * Read an outgoing invitation.
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
    public OutgoingInvitation readOutgoingInvitation(final EMail email);

    /**
     * Read an outgoing invitation.
     * 
     * @param email
     *            An e-mail address.
     * @return An outgoing invitation.
     */
    public OutgoingInvitation readOutgoingInvitation(final Long invitationId);

    /**
     * Read outgoing invitations.
     * 
     * @return A list of outgoing invitation.
     */
    public List<OutgoingInvitation> readOutgoingInvitations();
}

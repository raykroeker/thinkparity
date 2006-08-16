/*
 * Created On: Jul 7, 2006 2:18:59 PM
 */
package com.thinkparity.model.parity.model.io.handler;

import java.util.List;

import com.thinkparity.model.parity.model.contact.ContactInvitation;
import com.thinkparity.model.parity.model.contact.IncomingInvitation;
import com.thinkparity.model.parity.model.contact.OutgoingInvitation;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raymond@thinkparity.com
 * @version
 */
public interface ContactIOHandler {

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

    public void delete();
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

    public List<Contact> read();

    public Contact read(final String email);

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

    public List<ContactInvitation> readInvitations();

    /**
     * Read an outgoing invitation.
     * 
     * @param email
     *            An e-mail address.
     * @return An outgoing invitation.
     */
    public OutgoingInvitation readOutgoingInvitation(final Long invitationId);

    /**
     * Read an outgoing invitation.
     * 
     * @param email
     *            An e-mail address.
     * @return An outgoing invitation.
     */
    public OutgoingInvitation readOutgoingInvitation(final String email);

    /**
     * Read outgoing invitations.
     * 
     * @return A list of outgoing invitation.
     */
    public List<OutgoingInvitation> readOutgoingInvitations();
}

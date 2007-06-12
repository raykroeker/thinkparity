/*
 * Nov 29, 2005
 */
package com.thinkparity.desdemona.model.contact;

import java.util.Calendar;
import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;

import com.thinkparity.desdemona.model.annotation.ThinkParityAuthenticate;
import com.thinkparity.desdemona.util.AuthenticationType;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Contact Model<br>
 * <b>Description:</b>A public contact interface.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.7
 */
public interface ContactModel {

    /**
     * Accept the incoming invitation.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitationId
     *            An invitation id.
     * @param acceptedOn
     *            An accceptance timestamp <code>Calendar</code>.
     */
    public void acceptInvitation(final IncomingEMailInvitation invitation,
            final Calendar acceptedOn);

    /**
     * Accept the incoming invitation.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitationId
     *            An invitation id.
     * @param acceptedOn
     *            An accceptance timestamp <code>Calendar</code>.
     */
    public void acceptInvitation(final IncomingUserInvitation invitation,
            final Calendar acceptedOn);

	/**
     * Create an e-mail contact invitation.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitation
     *            An <code>OutgoingEMailInvitation</code>.
     */
    @ThinkParityAuthenticate(AuthenticationType.USER)
    public void createInvitation(final OutgoingEMailInvitation invitation);

    /**
     * Create a contact invitation.
     * 
     * @param userId
     *            A user id <code>Long</code>.
     * @param invitation
     *            An <code>OutgoingUserInvitation</code>.
     */
    public void createInvitation(final OutgoingUserInvitation invitation);

    /**
     * Decline an e-mail invitation. Delete the invitation and send a
     * notification (which will delete that invitation).
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitedBy
     *            Who invited the user.
     * @param invitedAs
     *            To which <code>EMail</code> the invitation was sent.
     * @param declinedOn
     *            When the invitation was declined.
     */
    public void declineInvitation(final IncomingEMailInvitation invitation,
            final Calendar declinedOn);

    /**
     * Decline an e-mail invitation. Delete the invitation and send a
     * notification (which will delete that invitation).
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitedBy
     *            Who invited the user.
     * @param invitedAs
     *            To which <code>EMail</code> the invitation was sent.
     * @param declinedOn
     *            When the invitation was declined.
     */
    public void declineInvitation(final IncomingUserInvitation invitation,
            final Calendar declinedOn);

    /**
     * Delete a contact for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param contactId
     *            A contact id <code>JabberId</code>.
     */
    public void delete(final JabberId id);

    /**
     * Delete a user's invitation.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitedAs
     *            The <code>Email</code> the invitation was created for.
     */
    public void deleteInvitation(final OutgoingEMailInvitation invitation,
            final Calendar deletedOn);

    /**
     * Delete a user's invitation.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitedAs
     *            The <code>Email</code> the invitation was created for.
     */
    public void deleteInvitation(final OutgoingUserInvitation invitation,
            final Calendar deletedOn);

    /**
     * Read a user's contacts.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>List&lt;Contact&gt;</code>.
     */
	public List<Contact> read();

    /**
     * Read a user's contact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param contactId
     *            A contact id <code>JabberId</code>.
     * @return The contact info.
     */
	public Contact read(final JabberId contactId);

    public List<IncomingEMailInvitation> readIncomingEMailInvitations();

    public List<IncomingUserInvitation> readIncomingUserInvitations();

    public List<OutgoingEMailInvitation> readOutgoingEMailInvitations();

	public List<OutgoingUserInvitation> readOutgoingUserInvitations();
}

/*
 * Created On:  4-Apr-07 10:04:49 AM
 */
package com.thinkparity.desdemona.model.contact;

import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.ContactInvitation;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.annotation.ThinkParityAuthenticate;
import com.thinkparity.desdemona.model.contact.invitation.Attachment;
import com.thinkparity.desdemona.util.AuthenticationType;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Internal Contact Model<br>
 * <b>Description:</b>An internal contact interface.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface InternalContactModel extends ContactModel {

    /**
     * Create a contact between two users.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param user
     *            A <code>User</code>.
     * @param contact
     *            A <code>Contact</code>.
     */
    @ThinkParityAuthenticate(AuthenticationType.USER)
    public void create(final JabberId userId, final User user,
            final User contact);

    /**
     * Create an incoming e-mail invitation.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitationUserId
     *            The invitation user id <code>JabberId</code>.
     * @param invitation
     *            An <code>IncomingEMailInvitation</code>.
     */
    @ThinkParityAuthenticate(AuthenticationType.USER)
    public void createInvitation(final JabberId userId,
            final JabberId invitationUserId,
            final IncomingEMailInvitation invitation);

    @ThinkParityAuthenticate(AuthenticationType.USER)
    public void createInvitationAttachment(final JabberId userId,
            final Attachment attachment);

    @ThinkParityAuthenticate(AuthenticationType.USER)
    public List<Attachment> readInvitationAttachments(final JabberId userId,
            final ContactInvitation invitation);

    /**
     * Read the outgoing e-mail invitations for the e-mail address.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            An <code>EMail</code> address.
     */
    @ThinkParityAuthenticate(AuthenticationType.USER)
    public List<OutgoingEMailInvitation> readOutgoingEMailInvitations(
            final JabberId userId, final EMail email);
}

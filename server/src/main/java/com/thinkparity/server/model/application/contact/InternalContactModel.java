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

import com.thinkparity.desdemona.model.annotation.ThinkParityAuthenticate;
import com.thinkparity.desdemona.model.contact.invitation.Attachment;
import com.thinkparity.desdemona.model.contact.invitation.ContainerVersionAttachment;
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

    /**
     * Create a container version attachment for an outgoing e-mail invitation.
     * 
     * @param invitation
     *            An <code>OutgoingEMailInvitation</code>.
     * @param attachment
     *            A <code>ContainerVersionAttachment</code>.
     */
    public void createInvitationAttachment(
            final OutgoingEMailInvitation invitation,
            final ContainerVersionAttachment attachment);

    /**
     * Delete an invitation attachment.
     * 
     * @param attachment
     *            An <code>Attachment</code>.
     */
    public void deleteInvitationAttachment(final Attachment attachment);

    @ThinkParityAuthenticate(AuthenticationType.USER)
    public List<ContainerVersionAttachment> readContainerVersionInvitationAttachments(
            final JabberId userId, final ContactInvitation invitation);

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

    /**
     * Read the outgoing e-mail address invitations as a proxy user.
     * 
     * @param proxyId
     *            A proxy user id <code>JabberId</code>.
     */
    public List<OutgoingEMailInvitation> readProxyOutgoingEMailInvitations(
            final JabberId proxyId);
}

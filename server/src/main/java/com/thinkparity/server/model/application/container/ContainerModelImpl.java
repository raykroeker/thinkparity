/*
 * Generated On: Jul 06 06 09:02:36 AM
 */
package com.thinkparity.desdemona.model.container;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactPublishedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContainerPublishedEvent;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.artifact.InternalArtifactModel;
import com.thinkparity.desdemona.model.contact.invitation.Attachment;
import com.thinkparity.desdemona.model.container.contact.invitation.ContainerVersionAttachment;
import com.thinkparity.desdemona.model.io.sql.ArtifactSql;
import com.thinkparity.desdemona.model.session.Session;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Container Model Implementation</br>
 * <b>Description:</b>The desdemona model container implementation.<br>
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.31
 */
public final class ContainerModelImpl extends AbstractModelImpl implements
        ContainerModel, InternalContainerModel {

    /** Artifact database io. */
    private ArtifactSql artifactSql;

    /**
     * Create ContainerModelImpl.
     *
     */
    public ContainerModelImpl() {
        super();
    }

    /**
     * @see com.thinkparity.desdemona.model.container.ContainerModel#publish(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.container.ContainerVersion,
     *      com.thinkparity.codebase.model.container.ContainerVersion,
     *      java.util.Map, java.util.List,
     *      com.thinkparity.codebase.jabber.JabberId, java.util.Calendar,
     *      java.util.List, java.util.List)
     * 
     */
    public void publish(final JabberId userId, final ContainerVersion version,
            final ContainerVersion latestVersion,
            final Map<DocumentVersion, String> documentVersions,
            final List<TeamMember> teamMembers,
            final List<ArtifactReceipt> receivedBy, final JabberId publishedBy,
            final Calendar publishedOn, final List<EMail> publishedToEMails,
            final List<User> publishedToUsers) {
        try {
            // create invitations for the e-mails
            for (final EMail email : publishedToEMails)
                createInvitation(userId, email, publishedOn, version);

            final List<JabberId> publishedToIds = new ArrayList<JabberId>();
            final List<JabberId> teamMemberIds = new ArrayList<JabberId>();
            for (final User publishedToUser : publishedToUsers)
                publishedToIds.add(publishedToUser.getId());
            for (final TeamMember teamMember : teamMembers)
                teamMemberIds.add(teamMember.getId());

            final ContainerPublishedEvent publishedEvent = new ContainerPublishedEvent();
            publishedEvent.setDocumentVersions(documentVersions);
            publishedEvent.setPublishedBy(publishedBy);
            publishedEvent.setPublishedOn(publishedOn);
            publishedEvent.setPublishedTo(publishedToUsers);
            publishedEvent.setReceivedBy(receivedBy);
            publishedEvent.setVersion(version);
            enqueueEvent(session.getJabberId(), publishedToIds, publishedEvent);

            final ArtifactPublishedEvent artifactPublishedEvent = new ArtifactPublishedEvent();
            artifactPublishedEvent.setLatestVersion(version.equals(latestVersion));
            artifactPublishedEvent.setPublishedBy(publishedBy);
            artifactPublishedEvent.setPublishedOn(publishedOn);
            artifactPublishedEvent.setUniqueId(version.getArtifactUniqueId());
            artifactPublishedEvent.setVersionId(version.getVersionId());
            final List<JabberId> enqueueTo = new ArrayList<JabberId>(teamMembers.size() + publishedToUsers.size());
            for (final TeamMember teamMember : teamMembers) {
                enqueueTo.add(teamMember.getId());
            }
            for (final JabberId publishedToId : publishedToIds) {
                if (!enqueueTo.contains(publishedToId))
                    enqueueTo.add(publishedToId);
            }
            artifactPublishedEvent.setTeamUserIds(enqueueTo);
            enqueueEvent(session.getJabberId(), enqueueTo, artifactPublishedEvent);
            // add only publisher to the team
            final InternalArtifactModel artifactModel = getArtifactModel();
            final Artifact artifact = artifactModel.read(version.getArtifactUniqueId());
            final List<TeamMember> localTeam = artifactModel.readTeam(userId, artifact.getId());
            final User publishedByUser = getUserModel().read(publishedBy);
            if (!contains(localTeam, publishedByUser))
                artifactModel.addTeamMember(userId, artifact.getId(), publishedByUser.getLocalId());
            artifactSql.updateDraftOwner(artifact.getId(),
                    User.THINKPARITY.getId(), publishedBy, publishedOn);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    protected final <T extends User, U extends User> void assertNotContains(
            final List<T> list, final U element, final String message,
            final Object... messageArguments) {
        Assert.assertNotTrue(contains(list, element), message, messageArguments);
    }

    /**
     * @see com.thinkparity.desdemona.model.AbstractModelImpl#initializeModel(com.thinkparity.desdemona.model.session.Session)
     *
     */
    @Override
    protected void initializeModel(final Session session) {
        artifactSql = new ArtifactSql();
    }

    /**
     * Read a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>User</code>.
     */
    protected final User readUser(final JabberId userId) {
        return getUserModel().read(userId);
    }

    /**
     * Create an e-mail invitation. An outgoing e-mail invitation from the user
     * to the e-mail address will be created and the version will be attached to
     * it. This api can be executed many times for the same e-mail/version so
     * checks are made to ensure no data duplication.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            An <code>EMail</code> address to invite.
     * @param createdOn
     *            A created on <code>Calendar</code>.
     */
    private void createInvitation(final JabberId userId, final EMail email,
            final Calendar createdOn, final ContainerVersion version) {
        final List<OutgoingEMailInvitation> invitations =
            getContactModel().readOutgoingEMailInvitations(userId);
        OutgoingEMailInvitation existing = null;
        for (final OutgoingEMailInvitation invitation : invitations) {
            if (invitation.getInvitationEMail().equals(email)) {
                existing = invitation;
                break;
            }
        }
        // create invitation
        final OutgoingEMailInvitation invitation;
        if (null == existing) {
            invitation = new OutgoingEMailInvitation();
            invitation.setCreatedBy(readUser(userId));
            invitation.setCreatedOn(createdOn);
            invitation.setInvitationEMail(email);
            getContactModel().createInvitation(userId, invitation);
        } else {
            invitation = existing;
        }
        final List<Attachment> attachments =
            getContactModel().readInvitationAttachments(userId, invitation);
        final ContainerVersionAttachment attachment = new ContainerVersionAttachment();
        attachment.setInvitationId(invitation.getId());
        attachment.setUniqueId(version.getArtifactUniqueId());
        attachment.setVersionId(version.getVersionId());
        // create attachment
        if (!attachments.contains(attachment)) {
            getContactModel().createInvitationAttachment(userId, attachment);
        }
    }
}

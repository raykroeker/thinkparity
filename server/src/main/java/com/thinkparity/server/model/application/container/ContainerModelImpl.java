/*
 * Generated On: Jul 06 06 09:02:36 AM
 */
package com.thinkparity.desdemona.model.container;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.container.PublishedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.container.PublishedNotificationEvent;
import com.thinkparity.codebase.model.util.xmpp.event.container.VersionPublishedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.container.VersionPublishedNotificationEvent;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.artifact.InternalArtifactModel;
import com.thinkparity.desdemona.model.contact.InternalContactModel;
import com.thinkparity.desdemona.model.contact.invitation.Attachment;
import com.thinkparity.desdemona.model.container.contact.invitation.ContainerVersionAttachment;
import com.thinkparity.desdemona.model.io.sql.ArtifactSql;
import com.thinkparity.desdemona.model.user.InternalUserModel;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Container Model Implementation</br>
 * <b>Description:</b>The desdemona model container implementation.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.33
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
     * @see com.thinkparity.desdemona.model.container.ContainerModel#publish(com.thinkparity.codebase.model.container.ContainerVersion,
     *      java.util.List, java.util.List, java.util.Calendar, java.util.List,
     *      java.util.List)
     * 
     */
    public void publish(final ContainerVersion version,
            final List<DocumentVersion> documentVersions,
            final List<TeamMember> team, final Calendar publishedOn,
            final List<EMail> publishToEMails, final List<User> publishToUsers) {
        try {
            // enqueue invitation events
            createInvitations(user.getId(), version, publishToEMails, publishedOn);

            // enqueue container published events
            enqueueContainerPublished(version, documentVersions, user.getId(),
                    publishedOn, publishToUsers);

            // enqueue container published notification events
            enqueueContainerPublishedNotification(version, team, user.getId(),
                    publishedOn, publishToUsers);

            // add session user to the team
            addTeamMember(version);

            // update the latest version
            updateLatestVersion(version, user.getId(), publishedOn);

            // update draft owner back to the system
            updateDraftOwner(version, user.getId(), publishedOn);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.container.ContainerModel#publishVersion(com.thinkparity.codebase.model.container.ContainerVersion,
     *      java.util.List, java.util.List, java.util.List, java.util.Calendar,
     *      java.util.List, java.util.List)
     * 
     */
    public void publishVersion(final ContainerVersion version,
            final List<DocumentVersion> documentVersionStreamIds,
            final List<TeamMember> team,
            final List<ArtifactReceipt> receivedBy, final Calendar publishedOn,
            final List<EMail> publishToEMails, final List<User> publishToUsers) {
        try {
            // create requisite incoming/outgoing e-mail invitations
            createInvitations(user.getId(), version, publishToEMails, publishedOn);

            // enqueue container version published events
            enqueueContainerVersionPublished(version, documentVersionStreamIds,
                    receivedBy, user.getId(), publishedOn, publishToUsers);

            // enqueue container version published notification events
            enqueueContainerVersionPublishedNotification(version, team,
                    user.getId(), publishedOn, publishToUsers);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }


    /**
     * @see com.thinkparity.desdemona.model.AbstractModelImpl#initialize()
     *
     */
    @Override
    protected void initialize() {
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
     * Add the published by user to the team.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param version
     *            A <code>ContainerVersion</code>.
     * @param publishedBy
     *            The published by user id <code>JabberId</code>.
     */
    private void addTeamMember(final ContainerVersion version) {
        final InternalArtifactModel artifactModel = getArtifactModel();
        final Artifact artifact = artifactModel.read(version.getArtifactUniqueId());
        final List<TeamMember> localTeam = artifactModel.readTeam(artifact.getId());
        if (!contains(localTeam, user)) {
            artifactModel.addTeamMember(artifact.getId());
        }
    }

    /**
     * Create the required e-mail address invitations. An outgoing e-mail
     * invitation from the user to the e-mail address will be created and the
     * version will be attached to it. This api can be executed many times for
     * the same e-mail/version so checks are made to ensure no data duplication.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param emails
     *            A <code>List</code> of <code>EMail</code> addresses to
     *            invite.
     * @param createdOn
     *            A created on <code>Calendar</code>.
     * @param version
     *            The <code>ContainerVersion</code>.
     */
    private void createInvitations(final JabberId userId,
            final ContainerVersion version, final List<EMail> publishToEMails,
            final Calendar publishedOn) {
        final InternalContactModel contactModel = getContactModel();
        final InternalUserModel userModel = getUserModel();
        for (final EMail publishToEMail : publishToEMails) {
            final List<OutgoingEMailInvitation> invitations =
                contactModel.readProxyOutgoingEMailInvitations(userId);
            OutgoingEMailInvitation existing = null;
            for (final OutgoingEMailInvitation invitation : invitations) {
                if (invitation.getInvitationEMail().equals(publishToEMail)) {
                    existing = invitation;
                    break;
                }
            }
            final OutgoingEMailInvitation invitation;
            if (null == existing) {
                /* if the recipient user does not exist and the user is publish
                 * restricted; do nothing */
                final User publishToUser = userModel.read(publishToEMail);
                if (null == publishToUser &&
                        getRuleModel().isPublishRestricted()) {
                    logger.logInfo("Publish is restricted from {0} to {1}.",
                            userId, publishToEMail);
                    continue;
                }
                /* if the recipient exists and the pair are publish restricted;
                 * do nothing */
                if (null != publishToUser &&
                        getRuleModel(publishToUser).isPublishRestrictedFrom(userId)) {
                    logger.logInfo("Publish is restricted from {0} to {1}.",
                            userId, publishToUser.getId());
                    continue;
                }
                // create invitation
                invitation = new OutgoingEMailInvitation();
                invitation.setCreatedBy(readUser(userId));
                invitation.setCreatedOn(publishedOn);
                invitation.setInvitationEMail(publishToEMail);
                contactModel.createInvitation(invitation);
            } else {
                // reference existing invitation
                invitation = existing;
            }
            final List<Attachment> attachments =
                contactModel.readInvitationAttachments(userId, invitation);
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

    /**
     * Enqueue a container published event. All of the publish to users will
     * receive an event.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     * @param documentVersions
     *            A <code>Map</code> of <code>DocumentVersion</code>s and
     *            their stream id <code>String</code>s.
     * @param publishedBy
     *            The published by user id <code>JabberId</code>.
     * @param publishedOn
     *            The published on <code>Calendar</code>.
     * @param publishToUsers
     *            A <code>List</code> of <code>User</code>s to publish to.
     */
    private void enqueueContainerPublished(final ContainerVersion version,
            final List<DocumentVersion> documentVersions,
            final JabberId publishedBy, final Calendar publishedOn,
            final List<User> publishToUsers) {
        final PublishedEvent event = new PublishedEvent();
        event.setDocumentVersions(documentVersions);
        event.setPublishedBy(publishedBy);
        event.setPublishedOn(publishedOn);
        event.setPublishedTo(localize(publishToUsers));
        event.setVersion(version);
        // enqueue to all publish to users
        final List<JabberId> enqueueTo = new ArrayList<JabberId>();
        for (final User publishToUser : publishToUsers)
            enqueueTo.add(publishToUser.getId());
        enqueueEvents(enqueueTo, event);
    }

    /**
     * Enqueue a container published notification event. All of the existing
     * team members as well as the publish to users will receive the event. As
     * well the team portrayed by the event will be the combination of the
     * existing team and any new users listed by publish to users.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     * @param teamMembers
     *            A <code>List</code> of existing <code>TeamMember</code>s.
     * @param publishedBy
     *            A published by user id <code>JabberId</code>.
     * @param publishedOn
     *            A published on <code>Calendar</code>.
     * @param publishToUsers
     *            A publish to <code>List</code> of <code>User</code>s.
     */
    private void enqueueContainerPublishedNotification(
            final ContainerVersion version, final List<TeamMember> teamMembers,
            final JabberId publishedBy, final Calendar publishedOn,
            final List<User> publishToUsers) {
        final PublishedNotificationEvent event =
            new PublishedNotificationEvent();
        event.setPublishedBy(publishedBy);
        event.setPublishedOn(publishedOn);
        event.setVersion(version);
        // set the team as the existing team members and the publish to users
        final List<User> newTeam = localize(teamMembers);
        final InternalUserModel userModel = getUserModel();
        for (final User publishToUser : publishToUsers) {
            if (!contains(newTeam, publishToUser)) {
                newTeam.add(userModel.read(publishToUser.getId()));
            }
        }
        event.setTeam(newTeam);
        // enqueue to the new team
        enqueueEvents(getIds(newTeam, new ArrayList<JabberId>()), event);
    }

    /**
     * Enqueue a container version published event. All of the publish to users
     * will received an event.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     * @param documentVersions
     *            A <code>Map</code> of <code>DocumentVersion</code>s and
     *            their stream id <code>String</code>s.
     * @param receivedBy
     *            A <code>List</code> of <code>ArtifactReceipt</code> for
     *            previously sent to users.
     * @param publishedBy
     *            The published by user id <code>JabberId</code>.
     * @param publishedOn
     *            The published on <code>Calendar</code>.
     * @param publishToUsers
     *            A <code>List</code> of <code>User</code>s to publish to.
     */
    private void enqueueContainerVersionPublished(
            final ContainerVersion version,
            final List<DocumentVersion> documentVersions,
            final List<ArtifactReceipt> receivedBy, final JabberId publishedBy,
            final Calendar publishedOn, final List<User> publishToUsers) {
        final InternalArtifactModel artifactModel = getArtifactModel();
        final Artifact artifact = artifactModel.read(version.getArtifactUniqueId());

        final VersionPublishedEvent event = new VersionPublishedEvent();
        event.setDocumentVersions(documentVersions);
        event.setLatestVersion(artifactSql.readLatestVersionId(
                artifact.getId()).equals(version.getVersionId()));
        event.setPublishedBy(publishedBy);
        event.setPublishedOn(publishedOn);
        event.setPublishedTo(localize(publishToUsers));
        event.setReceivedBy(receivedBy);
        event.setVersion(version);
        // enqueue to all publish to users
        final List<JabberId> enqueueTo = new ArrayList<JabberId>();
        for (final User publishToUser : publishToUsers)
            enqueueTo.add(publishToUser.getId());
        enqueueEvents(enqueueTo, event);
    }

    /**
     * Convert a list of users to local users.
     * 
     * @param <T>
     *            A type of user.
     * @param users
     *            A <code>List<User></code>.
     * @return A <code>List<User></code>.
     */
    private <T extends User> List<User> localize(final List<T> users) {
        final List<User> localUsers = new ArrayList<User>(users.size());
        final InternalUserModel userModel = getUserModel();
        for (final T user : users) {
            localUsers.add(userModel.read(user.getId()));
        }
        return localUsers;
    }

    /**
     * Enqueue a container version published notification event. All of the existing
     * team members as well as the publish to users will receive the event. As
     * well the team portrayed by the event will be the combination of the
     * existing team and any new users listed by publish to users.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     * @param teamMembers
     *            A <code>List</code> of existing <code>TeamMember</code>s.
     * @param publishedBy
     *            A published by user id <code>JabberId</code>.
     * @param publishedOn
     *            A published on <code>Calendar</code>.
     * @param publishToUsers
     *            A publish to <code>List</code> of <code>User</code>s.
     */
    private void enqueueContainerVersionPublishedNotification(
            final ContainerVersion version, final List<TeamMember> teamMembers,
            final JabberId publishedBy, final Calendar publishedOn,
            final List<User> publishToUsers) {
        final List<User> teamUsers = new ArrayList<User>(teamMembers.size());
        final List<JabberId> teamUserIds = new ArrayList<JabberId>(teamMembers.size());
        for (final TeamMember teamMember : teamMembers) {
            teamUsers.add(teamMember);
            teamUserIds.add(teamMember.getId());
        }

        final VersionPublishedNotificationEvent event =
            new VersionPublishedNotificationEvent();
        event.setPublishedBy(publishedBy);
        event.setPublishedOn(publishedOn);
        event.setVersion(version);
        // set the team as the existing team members and the publish to users
        final List<User> newTeam = localize(teamMembers);
        final InternalUserModel userModel = getUserModel();
        for (final User publishToUser : publishToUsers) {
            if (!contains(newTeam, publishToUser)) {
                newTeam.add(userModel.read(publishToUser.getId()));
            }
        }
        event.setTeam(newTeam);
        // enqueue to the new team
        enqueueEvents(getIds(newTeam, new ArrayList<JabberId>()), event);
    }

    /**
     * Update the draft owner back to the system.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     * @param publishedBy
     *            The published by user id <code>JabberId</code>.
     * @param publishedOn
     *            The published on <code>Calendar</code>.
     */
    private void updateDraftOwner(final ContainerVersion version,
            final JabberId publishedBy, final Calendar publishedOn) {
        final InternalArtifactModel artifactModel = getArtifactModel();
        final Artifact artifact = artifactModel.read(version.getArtifactUniqueId());
        // HACK - ContainerModelImpl#updateDraftOwner - should call the model
        final InternalUserModel userModel = getUserModel();
        final User currentOwner = userModel.read(publishedBy);
        final User newOwner = userModel.read(User.THINKPARITY.getId());
        artifactSql.updateDraftOwner(artifact, currentOwner, newOwner, publishedOn);
    }

    /**
     * Update the latest version id.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     * @param publishedBy
     *            The published by user id <code>JabberId</code>.
     * @param publishedOn
     *            The published on <code>Calendar</code>.
     */
    private void updateLatestVersion(final ContainerVersion version,
            final JabberId publishedBy, final Calendar publishedOn) {
        final InternalArtifactModel artifactModel = getArtifactModel();
        final Artifact artifact = artifactModel.read(version.getArtifactUniqueId());
        final InternalUserModel userModel = getUserModel();
        final User publishedByUser = userModel.read(publishedBy);
        // HACK - ContainerModelImpl#updateLatestVersion - should call the model
        artifactSql.updateLatestVersionId(artifact.getId(),
                version.getVersionId(), publishedByUser.getLocalId(), publishedOn);
    }
}

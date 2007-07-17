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
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactReceivedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.container.PublishedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.container.PublishedNotificationEvent;
import com.thinkparity.codebase.model.util.xmpp.event.container.VersionPublishedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.container.VersionPublishedNotificationEvent;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.artifact.InternalArtifactModel;
import com.thinkparity.desdemona.model.contact.InternalContactModel;
import com.thinkparity.desdemona.model.contact.invitation.Attachment;
import com.thinkparity.desdemona.model.container.contact.invitation.ContainerVersionAttachment;
import com.thinkparity.desdemona.model.user.InternalUserModel;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Container Model Implementation</br>
 * <b>Description:</b>The desdemona model container implementation.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.42
 */
public final class ContainerModelImpl extends AbstractModelImpl implements
        ContainerModel, InternalContainerModel {

    /**
     * Create ContainerModelImpl.
     *
     */
    public ContainerModelImpl() {
        super();
    }

    /**
     * @see com.thinkparity.desdemona.model.container.ContainerModel#confirmReceipt(com.thinkparity.codebase.model.container.ContainerVersion,
     *      java.util.Calendar, java.util.Calendar)
     * 
     */
    public void confirmReceipt(final ContainerVersion version,
            final Calendar publishedOn, final Calendar receivedOn) {
        try {
            final ArtifactReceivedEvent event = new ArtifactReceivedEvent();
            event.setUniqueId(version.getArtifactUniqueId());
            event.setVersionId(version.getVersionId());
            event.setPublishedOn(publishedOn);
            event.setReceivedBy(user.getId());
            event.setReceivedOn(receivedOn);

            final List<ArtifactReceipt> publishedToReceipts =
                getBackupModel().readPublishedTo(version.getArtifactUniqueId(),
                        version.getVersionId());
            final List<JabberId> userIds = new ArrayList<JabberId>();
            for (final ArtifactReceipt publishedToReceipt : publishedToReceipts) {
                if (userIds.contains(publishedToReceipt.getUser().getId())) {
                    logger.logInfo("User id list contains {0}.", publishedToReceipt.getUser().getId());
                } else {
                    logger.logInfo("Adding user id {0}.", publishedToReceipt.getUser().getId());
                    userIds.add(publishedToReceipt.getUser().getId());
                }
            }
            userIds.add(version.getCreatedBy());
            enqueueEvents(userIds, event);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.container.ContainerModel#delete(com.thinkparity.codebase.model.container.Container, java.util.Calendar)
     *
     */
    public void delete(final Container container, final Calendar deletedOn) {
        try {
            final Artifact artifact = localize(container);
            final InternalArtifactModel artifactModel = getArtifactModel();
            if (artifactModel.isDraftOwner(artifact)) {
                artifactModel.deleteDraft(artifact, deletedOn);
            }
            artifactModel.removeTeamMember(artifact);
            getBackupModel().delete(artifact.getUniqueId());
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.container.ContainerModel#publish(com.thinkparity.codebase.model.container.ContainerVersion,
     *      java.util.List, java.util.List, java.util.List)
     * 
     */
    public void publish(final ContainerVersion version,
            final List<DocumentVersion> documentVersions,
            final List<EMail> publishToEMails, final List<User> publishToUsers) {
        try {
            // create if required
            handleResolution(version);

            // delete draft
            deleteDraft(version);

            // enqueue container published events
            enqueueContainerPublished(version, documentVersions, publishToUsers);

            // add new team members as required
            handleTeamResolution(version, publishToUsers);

            // enqueue container published notification events
            enqueueContainerPublishedNotification(version);

            // update the latest version
            updateLatestVersion(version);

            // enqueue invitation events
            createInvitations(user.getId(), version, publishToEMails,
                    version.getCreatedOn());
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.container.ContainerModel#publishVersion(com.thinkparity.codebase.model.container.ContainerVersion,
     *      java.util.List, java.util.List, java.util.Calendar, java.util.List,
     *      java.util.List)
     * 
     */
    public void publishVersion(final ContainerVersion version,
            final List<DocumentVersion> documentVersions,
            final List<ArtifactReceipt> receivedBy, final Calendar publishedOn,
            final List<EMail> publishToEMails, final List<User> publishToUsers) {
        try {
            // enqueue container version published events
            enqueueContainerVersionPublished(version, documentVersions,
                    receivedBy, publishToUsers);

            // add new team members as required
            handleTeamResolution(version, publishToUsers);

            // enqueue container version published notification events
            enqueueContainerVersionPublishedNotification(version, user.getId(),
                    publishedOn, publishToUsers);

            // create requisite incoming/outgoing e-mail invitations
            createInvitations(user.getId(), version, publishToEMails, publishedOn);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.AbstractModelImpl#initialize()
     *
     */
    @Override
    protected void initialize() {}

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
     * Delete a draft.  The draft ownership is reverted back to the system user;
     * and all team members are sent a "draft deleted" event.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     */
    private void deleteDraft(final ContainerVersion version) {
        final InternalArtifactModel artifactModel = getArtifactModel();
        final Artifact artifact = artifactModel.read(version.getArtifactUniqueId());
        getArtifactModel().deleteDraft(artifact, version.getCreatedOn());
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
     * @param publishToUsers
     *            A <code>List</code> of <code>User</code>s to publish to.
     */
    private void enqueueContainerPublished(final ContainerVersion version,
            final List<DocumentVersion> documentVersions,
            final List<User> publishToUsers) {
        final Artifact localArtifact = localize(version);

        final PublishedEvent event = new PublishedEvent();
        event.setDocumentVersions(documentVersions);
        event.setPublishedBy(version.getCreatedBy());
        event.setPublishedOn(version.getCreatedOn());
        event.setPublishedTo(localize(publishToUsers));
        event.setTeam(getArtifactModel().readTeam(localArtifact.getId()));
        event.setVersion(version);
        // enqueue to all publish to users
        final List<JabberId> enqueueTo = new ArrayList<JabberId>();
        for (final User publishToUser : publishToUsers)
            enqueueTo.add(publishToUser.getId());
        enqueueEvents(enqueueTo, event);
    }

    /**
     * Enqueue a container published notification event. All of the existing
     * team members will receive the event.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     */
    private void enqueueContainerPublishedNotification(
            final ContainerVersion version) {
        final PublishedNotificationEvent event =
            new PublishedNotificationEvent();
        event.setPublishedBy(version.getCreatedBy());
        event.setPublishedOn(version.getCreatedOn());
        event.setVersion(version);
        // enqueue to the team
        final Artifact localArtifact = localize(version);
        final List<TeamMember> team = getArtifactModel().readTeam(localArtifact.getId());
        enqueueEvents(getIds(team, new ArrayList<JabberId>()), event);
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
     * @param publishToUsers
     *            A <code>List</code> of <code>User</code>s to publish to.
     */
    private void enqueueContainerVersionPublished(
            final ContainerVersion version,
            final List<DocumentVersion> documentVersions,
            final List<ArtifactReceipt> receivedBy,
            final List<User> publishToUsers) {
        final Artifact localArtifact = localize(version);
        final Long latestVersionId = readLatestVersionId(localArtifact);

        final VersionPublishedEvent event = new VersionPublishedEvent();
        event.setDocumentVersions(documentVersions);
        event.setLatestVersion(latestVersionId.equals(version.getVersionId()));
        event.setPublishedBy(user.getId());
        event.setPublishedOn(version.getCreatedOn());
        event.setPublishedTo(localize(publishToUsers));
        event.setReceivedBy(receivedBy);
        event.setTeam(getArtifactModel().readTeam(localArtifact.getId()));
        event.setVersion(version);
        // enqueue to all publish to users
        final List<JabberId> enqueueTo = new ArrayList<JabberId>();
        for (final User publishToUser : publishToUsers)
            enqueueTo.add(publishToUser.getId());
        enqueueEvents(enqueueTo, event);
    }

    /**
     * Enqueue a container version published notification event. All of the team
     * members will receive the event. Note that in this case the version
     * created by/on are different from the published by/on.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     * @param publishedBy
     *            A published by user id <code>JabberId</code>.
     * @param publishedOn
     *            A published on <code>Calendar</code>.
     * @param publishToUsers
     *            A publish to <code>List</code> of <code>User</code>s.
     */
    private void enqueueContainerVersionPublishedNotification(
            final ContainerVersion version, final JabberId publishedBy,
            final Calendar publishedOn, final List<User> publishToUsers) {
        final VersionPublishedNotificationEvent event = new VersionPublishedNotificationEvent();
        event.setPublishedBy(publishedBy);
        event.setPublishedOn(publishedOn);
        event.setVersion(version);
        
        // enqueue to the team
        final Artifact localArtifact = localize(version);
        final List<TeamMember> team = getArtifactModel().readTeam(localArtifact.getId());
        // enqueue to the new team
        enqueueEvents(getIds(team, new ArrayList<JabberId>()), event);
    }

    /**
     * Handle a container version resolution. If the container does not exist;
     * create it.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     */
    private void handleResolution(final ContainerVersion version) {
        final InternalArtifactModel artifactModel = getArtifactModel();
        if (artifactModel.doesExist(version.getArtifactUniqueId())) {
            logger.logInfo("Artifact {0} exists.", version.getArtifactName());
        } else {
            artifactModel.create(version.getArtifactUniqueId(),
                    version.getCreatedOn());
        }
    }

    /**
     * Handle the team resolution. Any publish to users not on the team; are
     * added and a "team member added" event is distributed.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     * @param publishToUsers
     *            A <code>List<User></code>.
     */
    private void handleTeamResolution(final ContainerVersion version,
            final List<User> publishToUsers) {
        final Artifact localArtifact = localize(version);
        final InternalArtifactModel artifactModel = getArtifactModel();
        final List<TeamMember> team = artifactModel.readTeam(localArtifact.getId());

        // build the list of new members
        final List<User> newMembers = new ArrayList<User>();
        if (contains(team, user)) {
            logger.logDebug("Team contains user {0}.", user.getId());
        } else {
            logger.logDebug("Team does not contain user {0}.", user.getId());
            newMembers.add(user);
        }
        for (int i = 0; i < publishToUsers.size(); i++) {
            if (contains(team, publishToUsers.get(i))) {
                logger.logDebug("Team contains user {0}.", publishToUsers.get(i).getId());
            } else {
                logger.logDebug("Team does not contain user {0}.", publishToUsers.get(i).getId());
                newMembers.add(localize(publishToUsers.get(i)));
            }
        }
        artifactModel.addTeamMembers(localArtifact, newMembers);
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
     * Obtain a local reference for a user.
     * 
     * @param <T>
     *            A user type.
     * @param user
     *            A <code>T</code>.
     * @return A <code>User</code>.
     */
    private <T extends User> User localize(final T user) {
        return getUserModel().read(user.getId());
    }

    /**
     * Obtain a local reference for an artifact.
     * 
     * @param <T>
     *            An artifact type.
     * @param artifact
     *            A <code>T</code>.
     * @return An <code>Artifact</code>.
     */
    private <T extends Artifact> Artifact localize(final T artifact) {
        return getArtifactModel().read(artifact.getUniqueId());
    }

    /**
     * Obtain a local reference for an artifact version.
     * 
     * @param <T>
     *            An artifact version type.
     * @param version
     *            A <code>T</code>.
     * @return An <code>Artifact</code>.
     */
    private <T extends ArtifactVersion> Artifact localize(final T version) {
        return getArtifactModel().read(version.getArtifactUniqueId());
    }

    /**
     * Read the latest version id for a version.
     * 
     * @param artifact
     *            An <code>Artifact</code>.
     * @return The latest version id.
     */
    private Long readLatestVersionId(final Artifact artifact) {
        return getArtifactModel().readLatestVersionId(artifact);
    }

    /**
     * Update the latest version reference.
     * 
     * @param version
     *            An <code>ArtifactVersion</code>.
     */
    private void updateLatestVersion(final ContainerVersion version) {
        final Artifact localArtifact = localize(version);
        getArtifactModel().updateLatestVersionId(localArtifact,
                version.getVersionId(), version.getCreatedOn());
    }
}

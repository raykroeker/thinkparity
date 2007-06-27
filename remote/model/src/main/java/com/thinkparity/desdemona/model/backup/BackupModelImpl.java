/*
 * Generated On: Oct 04 06 10:14:14 AM
 */
package com.thinkparity.desdemona.model.backup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.artifact.PublishedToEMail;
import com.thinkparity.codebase.model.backup.Statistics;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.BackupStatisticsUpdatedEvent;

import com.thinkparity.ophelia.model.InternalModelFactory;
import com.thinkparity.ophelia.model.document.CannotLockException;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.Constants.Product.Ophelia;
import com.thinkparity.desdemona.model.artifact.InternalArtifactModel;
import com.thinkparity.desdemona.model.contact.InternalContactModel;
import com.thinkparity.desdemona.model.container.contact.invitation.ContainerVersionAttachment;
import com.thinkparity.desdemona.model.io.sql.ArtifactSql;
import com.thinkparity.desdemona.model.io.sql.BackupSql;
import com.thinkparity.desdemona.model.queue.InternalQueueModel;

/**
 * <b>Title:</b>thinkParity Backup Model Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class BackupModelImpl extends AbstractModelImpl implements BackupModel,
        InternalBackupModel {

    /** An artifact sql interface */
    private ArtifactSql artifactSql;

    /** A backup sql interface. */
    private BackupSql backupSql;

    /**
     * Create BackupModelImpl.
     * 
     */
    public BackupModelImpl() {
        super();
    }

    /**
     * @see com.thinkparity.desdemona.model.backup.BackupModel#archive(com.thinkparity.codebase.jabber.JabberId, java.util.UUID)
     *
     */
    public void archive(final UUID uniqueId) {
        try {
            if (isBackupEnabled(user)) {
                archiveImpl(user, uniqueId);
            } else {
                logger.logWarning("User {0} has no backup feature.",
                        user.getId());
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.backup.BackupModel#delete(com.thinkparity.codebase.jabber.JabberId,
     *      java.util.UUID)
     * 
     */
    public void delete(final UUID uniqueId) {
        try {
            if (isBackupEnabled(user)) {
                deleteImpl(user, uniqueId);
            } else {
                logger.logWarning("User {0} has no backup feature.",
                        user.getId());
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.backup.InternalBackupModel#enqueueBackupEvent(com.thinkparity.codebase.jabber.JabberId, com.thinkparity.codebase.jabber.JabberId)
     *
     */
    public void enqueueBackupEvent() {
        try {
            if (isBackupEnabled(user)) {
                enqueueBackupEventImpl(user);
            } else {
                logger.logWarning("User {0} has no backup feature.",
                        user.getId());
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.backup.InternalBackupModel#readArtifactTeam(java.lang.Long)
     *
     */
    public List<TeamMember> readArtifactTeam(final UUID uniqueId) {
        try {
            return readArtifactTeamImpl(uniqueId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.backup.BackupModel#readContainer(java.util.UUID)
     * 
     */
    public Container readContainer(final UUID uniqueId) {
        try {
            return readContainerImpl(uniqueId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.backup.InternalBackupModel#readContainerDocumentVersions(java.lang.Long, java.lang.Long)
     *
     */
    public List<DocumentVersion> readContainerDocumentVersions(
            final UUID uniqueId, final Long versionId) {
        try {
            return readContainerDocumentVersionsImpl(uniqueId, versionId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.backup.InternalBackupModel#readContainerLatestVersion(java.lang.Long)
     *
     */
    public ContainerVersion readContainerLatestVersion(final UUID uniqueId) {
        try {
            return readContainerLatestVersionImpl(uniqueId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.backup.BackupModel#readContainers(com.thinkparity.codebase.jabber.JabberId)
     *
     */
    public List<Container> readContainers() {
        try {
            if (isBackupEnabled(user)) {
                return readContainersImpl(user);
            } else {
                logger.logWarning("User {0} has no backup feature.",
                        user.getId());
                return Collections.emptyList();
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.backup.InternalBackupModel#readContainerVersion(java.util.UUID, java.lang.Long)
     *
     */
    public ContainerVersion readContainerVersion(final UUID uniqueId,
            final Long versionId) {
        try {
            return readContainerVersionImpl(uniqueId, versionId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.backup.BackupModel#readContainerVersions(com.thinkparity.codebase.jabber.JabberId, java.util.UUID)
     *
     */
    public List<ContainerVersion> readContainerVersions(final UUID uniqueId) {
        try {
            if (isBackupEnabled(user)) {
                return readContainerVersionsImpl(user, uniqueId);
            } else {
                logger.logWarning("User {0} has no backup feature.",
                        user.getId());
                return Collections.emptyList();
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.backup.BackupModel#readDocuments(java.util.UUID,
     *      java.lang.Long)
     * 
     */
    public List<Document> readDocuments(final UUID containerUniqueId,
            final Long containerVersionId) {
        try {
            if (isBackupEnabled(user)) {
                return readContainerDocumentsImpl(user, containerUniqueId,
                        containerVersionId);
            } else {
                logger.logWarning("User {0} has no backup feature.",
                        user.getId());
                return Collections.emptyList();
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.backup.BackupModel#readContainerDocumentVersions(com.thinkparity.codebase.jabber.JabberId, java.util.UUID, java.lang.Long)
     *
     */
    public List<DocumentVersion> readDocumentVersions(
            final UUID containerUniqueId, final Long containerVersionId) {
        try {
            if (isBackupEnabled(user)) {
                return readContainerDocumentVersionsImpl(user, containerUniqueId, containerVersionId);
            } else {
                logger.logWarning("User {0} has no backup feature.",
                        user.getId());
                return Collections.emptyList();
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.backup.BackupModel#readContainerPublishedTo(com.thinkparity.codebase.jabber.JabberId, java.util.UUID, java.lang.Long)
     *
     */
    public List<ArtifactReceipt> readPublishedTo(final UUID containerUniqueId,
            final Long containerVersionId) {
        try {
            if (isBackupEnabled(user)) {
                return readContainerPublishedToImpl(user, containerUniqueId,
                        containerVersionId);
            } else {
                logger.logWarning("User {0} has no backup feature.",
                        user.getId());
                return Collections.emptyList();
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.backup.BackupModel#readPublishedToEMails(java.util.UUID,
     *      java.lang.Long)
     * 
     */
    public List<PublishedToEMail> readPublishedToEMails(
            final UUID containerUniqueId, final Long containerVersionId) {
        try {
            if (isBackupEnabled(user)) {
                return readContainerPublishedToEMailsImpl(user,
                        containerUniqueId, containerVersionId);
            } else {
                logger.logWarning("User {0} has no backup feature.",
                        user.getId());
                return Collections.emptyList();
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.backup.BackupModel#readStatistics(com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public Statistics readStatistics() {
        try {
            if (isBackupEnabled(user)) {
                return readStatisticsImpl(user);
            } else {
                logger.logWarning("User {0} has no backup feature.",
                        user.getId());
                return null;
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.backup.BackupModel#readTeamIds(com.thinkparity.codebase.jabber.JabberId,
     *      java.util.UUID)
     * 
     */
    public List<JabberId> readTeamIds(final UUID uniqueId) {
        try {
            if (isBackupEnabled(user)) {
                return readTeamIdsImpl(user, uniqueId);
            } else {
                logger.logWarning("User {0} has no backup feature.",
                        user.getId());
                return Collections.emptyList();
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.backup.BackupModel#restore(com.thinkparity.codebase.jabber.JabberId,
     *      java.util.UUID)
     * 
     */
    public void restore(final UUID uniqueId) {
        try {
            if (isBackupEnabled(user)) {
                restoreImpl(user, uniqueId);
            } else {
                logger.logWarning("User {0} has no backup feature.",
                        user.getId());
            }
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
        this.artifactSql = new ArtifactSql();
        this.backupSql = new BackupSql();
    }

    /**
     * If the container is archived; apply the flag.
     * 
     * @param user
     *            A <code>User</code>.
     * @param container
     *            A <code>Container</code>.
     */
    private void applyFlagArchived(final User user, final Container container) {
        if (isArchived(user, container.getUniqueId())) {
            container.add(ArtifactFlag.ARCHIVED);
        }
    }

    /**
     * If a container is archived; apply the archived flag.
     * 
     * @param user
     *            A <code>User</code>.
     * @param containers
     *            A <code>List</code> of <code>Container</code>s.
     */
    private void applyFlagArchived(final User user,
            final List<Container> containers) {
        for (final Container container : containers)
            applyFlagArchived(user, container);
    }

    /**
     * Achive implementation.
     * 
     * @param user
     *            A <code>User</code>.
     * @param uniqueId
     *            A unique id <code>UUID</code>.
     */
    private void archiveImpl(final User user, final UUID uniqueId) {
        final Artifact artifact = getArtifactModel().read(uniqueId);
        backupSql.createArchive(user, artifact);
    }

    /**
     * Delete implementation.
     * 
     * @param user
     *            A <code>User</code>.
     * @param uniqueId
     *            A unique id <code>UUID</code>.
     */
    private void deleteImpl(final User user, final UUID uniqueId)
            throws CannotLockException {
        final Artifact artifact = getArtifactModel().read(uniqueId);
        if (isArchived(user, uniqueId))
            backupSql.deleteArchive(user, artifact);
        if (!isContainerBackedUp(uniqueId)) {
            final com.thinkparity.ophelia.model.container.InternalContainerModel
                    containerModel = getModelFactory(user).getContainerModel();
            final com.thinkparity.ophelia.model.artifact.InternalArtifactModel
                    artifactModel = getModelFactory(user).getArtifactModel();
            final Long containerId = artifactModel.readId(uniqueId);
            containerModel.delete(containerId);
        }
        if (0 == artifactSql.readTeamRelCount(artifact.getId())) {
            artifactSql.delete(artifact.getId());
        }
        // fire backup event
        enqueueBackupEventImpl(user);
    }

    /**
     * Enqueue a backup statistics updated event for a user.
     * 
     * @param user
     *            A user <code>User</code>.
     */
    private void enqueueBackupEventImpl(final User user) {
        logger.logTrace("Entry");
        logger.logVariable("user", user);
        final BackupStatisticsUpdatedEvent bsue = new BackupStatisticsUpdatedEvent();
        bsue.setStatistics(readStatisticsImpl(user));
        logger.logVariable("bsue", bsue);
        final InternalQueueModel queueModel = getQueueModel(user);
        queueModel.enqueueEvent(bsue);
        queueModel.flush();
        logger.logTrace("Exit");
    }

    /**
     * Obtain the archive's model factory.
     * 
     * @param archiveId
     *            An archive id <code>JabberId</code>.
     * @return An archive's <code>ClientModelFactory</code>.
     */
    private InternalModelFactory getModelFactory() {
        return BackupService.getInstance().getModelFactory();
    }

    /**
     * Obtain the archive's model factory.
     * 
     * @param archiveId
     *            An archive id <code>JabberId</code>.
     * @return An archive's <code>ClientModelFactory</code>.
     */
    private InternalModelFactory getModelFactory(final User user) {
        if (isBackupEnabled(user)) {
            return BackupService.getInstance().getModelFactory();
        } else {
            throw new BackupException("User {0} has no backup feature.",
                    user.getId());
        }
    }

    /**
     * Determine if the artifact has been archived for the user.
     * 
     * @param user
     *            A <code>User</code>.
     * @param artifact
     *            An <code>Artifact</code>.
     * @return True if the artifact has been archived by the user.
     */
    private boolean isArchived(final User user, final UUID uniqueId) {
        final Artifact artifact = getArtifactModel().read(uniqueId);
        return backupSql.doesExistArchive(user, artifact).booleanValue();
    }

    /**
     * Determine if the unique id has been archived.
     * 
     * @param uniqueId
     *            An artifact unique <code>UUID</code>.
     * @return True if the artifact has been archived by any user.
     */
    private boolean isArchived(final UUID uniqueId) {
        final Artifact artifact = getArtifactModel().read(uniqueId);
        return backupSql.doesExistArchive(artifact).booleanValue();
    }

    /**
     * Determine whether or not backup is enabled for the user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return True if backup is enabled.
     */
    private boolean isBackupEnabled(final User user) {
        /* NOTE the product id/name should be read from the interface once the
         * migrator code is complete */
        final List<Feature> features = getUserModel(user).readFeatures(Ophelia.PRODUCT_ID);
        for (final Feature feature : features) {
            if (Ophelia.Feature.BACKUP.equals(feature.getName())) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Determine if the user has backed up the container. We check the server
     * team membership as well as the server archive flag.
     * 
     * @param user
     *            A <code>User</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return True if the user is either a team member; or has archived the
     *         artifact.
     */
    private boolean isContainerBackedUp(final User user, final UUID uniqueId) {
        return isTeamMember(user, uniqueId) || isArchived(user, uniqueId);
    }

    /**
     * Determine if the container is backed up. We check the server team
     * membership as well as the server archive flag.
     * 
     * @param artifact
     *            An <code>Artifact</code>.
     * @return True if the user is either a team member; or has archived the
     *         artifact.
     */
    private boolean isContainerBackedUp(final UUID uniqueId) {
        final Artifact artifact = getArtifactModel().read(uniqueId);
        return 0 < artifactSql.readTeamRelCount(artifact.getId())
                || isArchived(uniqueId);
    }

    /**
     * Determine if the user is a team member.
     * 
     * @param user
     *            A <code>User</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return True if the user is a team member.
     */
    private boolean isTeamMember(final User user, final UUID uniqueId) {
        final InternalArtifactModel artifactModel = getArtifactModel();
        final Artifact artifact = artifactModel.read(uniqueId);
        final List<TeamMember> team = artifactModel.readTeam(artifact.getId());
        for (final TeamMember teamMember : team) {
            if (teamMember.getLocalId().equals(user.getLocalId()))
                return true;
        }
        return false;
    }

    private List<TeamMember> readArtifactTeamImpl(final UUID uniqueId) {
        if (isContainerBackedUp(uniqueId)) {
            final Long containerId = getModelFactory().getArtifactModel().readId(uniqueId);
            return getModelFactory().getArtifactModel().readTeam2(containerId);
        } else {
            logger.logWarning("Container {0} is not backed up.", uniqueId);
            return Collections.emptyList();
        }
    }

    /**
     * Read a list of backed up container ids for a user.
     * 
     * @param user
     *            A <code>User</code>.
     * @return A <code>List</code> of container <code>UUID</code>s.
     */
    private List<UUID> readBackedUpContainerIds(final User user) {
        final List<UUID> backedUpContainerIds = backupSql.readArchive(user);
        backedUpContainerIds.addAll(getArtifactModel().readTeamArtifactIds(user));
        return backedUpContainerIds;
    }

    /**
     * Read the container documents implementation. If the user is a team member
     * read the documents from the backup.
     * 
     * @param user
     *            A <code>User</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return A <code>List</code> of <code>Document</code>s.
     */
    private List<Document> readContainerDocumentsImpl(final User user,
            final UUID uniqueId, final Long versionId) {
        if (isContainerBackedUp(user, uniqueId)) {
            final InternalModelFactory modelFactory = getModelFactory(user);
            final Long containerId = modelFactory.getArtifactModel().readId(uniqueId);
            return modelFactory.getContainerModel().readDocuments(containerId, versionId);
        } else {
            logger.logWarning("Container {0} is not backed up for user {1}.",
                    uniqueId, user.getId());
            return Collections.emptyList();
        }
    }

    /**
     * Read the container document versions implementation. If the user is a
     * team member read the document versions from the backup.
     * 
     * @param user
     *            A <code>User</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return A <code>List</code> of <code>DocumentVersion</code>s.
     */
    private List<DocumentVersion> readContainerDocumentVersionsImpl(
            final User user, final UUID uniqueId, final Long versionId) {
        if (isContainerBackedUp(user, uniqueId)) {
            final InternalModelFactory modelFactory = getModelFactory(user);
            final Long containerId = modelFactory.getArtifactModel().readId(uniqueId);
            return modelFactory.getContainerModel().readDocumentVersions(containerId, versionId);
        } else {
            logger.logWarning("Container {0} is not backed up for user {1}.",
                    uniqueId, user.getId());
            return Collections.emptyList();
        }
    }

    private List<DocumentVersion> readContainerDocumentVersionsImpl(
            final UUID uniqueId, final Long versionId) {
        if (isContainerBackedUp(uniqueId)) {
            final Long containerId = getModelFactory().getArtifactModel().readId(uniqueId);
            return getModelFactory().getContainerModel().readDocumentVersions(containerId, versionId);
        } else {
            logger.logWarning("Container {0} is not backed up.", uniqueId);
            return Collections.emptyList();
        }
    }

    private Container readContainerImpl(final UUID uniqueId) {
        if (isContainerBackedUp(uniqueId)) {
            final Long containerId = getModelFactory().getArtifactModel().readId(uniqueId);
            return getModelFactory().getContainerModel().read(containerId);
        } else {
            logger.logWarning("Container {0} is not backed up.", uniqueId);
            return null;
        }
    }

    /**
     * Read the latest container version implementation. Read the container from
     * the backup.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A <code>ContainerVersion</code>.
     */
    private ContainerVersion readContainerLatestVersionImpl(final UUID uniqueId) {
        if (isContainerBackedUp(uniqueId)) {
            final InternalModelFactory modelFactory = getModelFactory();
            final Long containerId = modelFactory.getArtifactModel().readId(uniqueId);
            return modelFactory.getContainerModel().readLatestVersion(containerId);
        } else {
            logger.logWarning("Container {0} is not backed up.", uniqueId);
            return null;
        }
    }

    /**
     * Read the container version published to e-mails implementation. If the
     * user is a member of the team read the published to e-mail list is
     * interpolated from the user's outgoing e-mail invitation attachments.
     * 
     * @param user
     *            A <code>User</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return A <code>List<PublishedToEMail></code>.
     */
    private List<PublishedToEMail> readContainerPublishedToEMailsImpl(
            final User user, final UUID uniqueId, final Long versionId) {
        if (isContainerBackedUp(user, uniqueId)) {
            final List<PublishedToEMail> publishedTo = new ArrayList<PublishedToEMail>();
            final InternalContactModel contactModel = getContactModel();
            final List<OutgoingEMailInvitation> invitations =
                getContactModel().readOutgoingEMailInvitations();
            final List<ContainerVersionAttachment> attachments =
                new ArrayList<ContainerVersionAttachment>();
            for (final OutgoingEMailInvitation invitation : invitations) {
                attachments.clear();
                attachments.addAll(
                        contactModel.readContainerVersionInvitationAttachments(
                                user.getId(), invitation));
                for (final ContainerVersionAttachment attachment : attachments) {
                    if (attachment.getUniqueId().equals(uniqueId)) {
                        final PublishedToEMail pte = new PublishedToEMail();
                        pte.setEMail(invitation.getInvitationEMail());
                        pte.setPublishedOn(invitation.getCreatedOn());
                        publishedTo.add(pte);
                    }
                }
            }
            return publishedTo;
        } else {
            logger.logWarning("Container {0} is not backed up for user {1}.",
                    uniqueId, user.getId());
            return Collections.emptyList();
        }
    }

    /**
     * Read the container version published to implementation. If the user is a
     * member of the team read the published to list from the backup.
     * 
     * @param user
     *            A <code>User</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return A <code>List</code> of <code>ArtifactReceipt</code>s.
     */
    private List<ArtifactReceipt> readContainerPublishedToImpl(final User user,
            final UUID uniqueId, final Long versionId) {
        if (isContainerBackedUp(user, uniqueId)) {
            final InternalModelFactory modelFactory = getModelFactory(user);
            final Long containerId = modelFactory.getArtifactModel().readId(uniqueId);
            return modelFactory.getContainerModel().readPublishedTo(containerId, versionId);
        } else {
            logger.logWarning("Container {0} is not backed up for user {1}.",
                    uniqueId, user.getId());
            return Collections.emptyList();
        }

    }

    /**
     * Read the containers implementation. Read the containers that are being
     * backed up for the user.
     * 
     * @param user
     *            A <code>User</code>.
     * @return A <code>List</code> of <code>Container</code>s.
     */
    private List<Container> readContainersImpl(final User user) {
        final List<UUID> backedUpContainerIds = readBackedUpContainerIds(user);
        final List<Container> backedUpContainers = new ArrayList<Container>(
                backedUpContainerIds.size());
        final com.thinkparity.ophelia.model.container.InternalContainerModel
                containerModel = getModelFactory(user).getContainerModel();
        final com.thinkparity.ophelia.model.artifact.InternalArtifactModel
                artifactModel = getModelFactory(user).getArtifactModel();
        Long containerId;
        Container container;
        for (final UUID backedUpContainerId : backedUpContainerIds) {
            containerId = artifactModel.readId(backedUpContainerId);
            if (null == containerId) {
                logger.logWarning("Container {0} no longer exists in the backup.", backedUpContainerId);
            } else {
                container = containerModel.read(containerId);
                if (null == container) {
                    logger.logWarning("Container {0} no longer exists in the backup.", containerId);
                } else {
                    backedUpContainers.add(containerModel.read(containerId));
                }
            }
        }
        applyFlagArchived(user, backedUpContainers);
        return backedUpContainers;
    }

    /**
     * Read a container version implementation. Read the container from the
     * backup.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return A <code>ContainerVersion</code>.
     */
    private ContainerVersion readContainerVersionImpl(final UUID uniqueId,
            final Long versionId) {
        if (isContainerBackedUp(uniqueId)) {
            final InternalModelFactory modelFactory = getModelFactory();
            final Long containerId = modelFactory.getArtifactModel().readId(uniqueId);
            return modelFactory.getContainerModel().readVersion(containerId, versionId);
        } else {
            logger.logWarning("Container {0} is not backed up.", uniqueId);
            return null;
        }
    }

    /**
     * Read the container versions implementation. If the user is a member of
     * the team read the versions from the backup.
     * 
     * @param user
     *            A <code>User</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A <code>List</code> of <code>ContainerVersion</code>s.
     */
    private List<ContainerVersion> readContainerVersionsImpl(final User user,
            final UUID uniqueId) {
        if (isContainerBackedUp(user, uniqueId)) {
            final InternalModelFactory modelFactory = getModelFactory(user);
            final Long containerId = modelFactory.getArtifactModel().readId(uniqueId);
            return modelFactory.getContainerModel().readVersions(containerId);
        } else {
            logger.logWarning("Container {0} is not backed up for user {1}.",
                    uniqueId, user.getId());
            return Collections.emptyList();
        }
    }

    /**
     * Read statistics implementation.
     * 
     * @param user
     *            A <code>User</code>.
     * @return An instance of <code>Statistics</code>.
     */
    private Statistics readStatisticsImpl(final User user) {
        logger.logTrace("Entry");
        final List<UUID> backedUpContainerIds = readBackedUpContainerIds(user);
        logger.logVariable("backedUpContainerIds", backedUpContainerIds);
        final com.thinkparity.ophelia.model.container.InternalContainerModel
                containerModel = getModelFactory(user).getContainerModel();
        final com.thinkparity.ophelia.model.document.InternalDocumentModel
                documentModel = getModelFactory(user).getDocumentModel();
        final com.thinkparity.ophelia.model.artifact.InternalArtifactModel
                artifactModel = getModelFactory(user).getArtifactModel();
        final List<ContainerVersion> versions = new ArrayList<ContainerVersion>();
        final List<Document> documents = new ArrayList<Document>();
        final List<Document> allDocuments = new ArrayList<Document>();
        for (final UUID backedUpContainerId : backedUpContainerIds) {
            versions.clear();
            versions.addAll(containerModel.readVersions(artifactModel.readId(
                    backedUpContainerId)));
            logger.logVariable("versions", versions);
            for (final ContainerVersion version : versions) {
                documents.clear();
                documents.addAll(containerModel.readDocuments(
                        version.getArtifactId(), version.getVersionId()));
                logger.logVariable("documents", documents);
                for (final Document document : documents) {
                    if (!allDocuments.contains(document))
                        allDocuments.add(document);
                }
            }
        }
        logger.logVariable("allDocuments", allDocuments);
        final List<DocumentVersion> allDocumentVersions = new ArrayList<DocumentVersion>();
        long diskUsage = 0;
        for (final Document document : allDocuments) {
            allDocumentVersions.clear();
            allDocumentVersions.addAll(documentModel.readVersions(document.getId()));
            for (final DocumentVersion documentVersion : allDocumentVersions) {
                diskUsage += documentVersion.getSize().longValue();
            }
        }
        logger.logVariable("allDocumentVersions", allDocumentVersions);
        final Statistics statistics = new Statistics();
        statistics.setDiskUsage(Long.valueOf(diskUsage));
        logger.logVariable("statistics", statistics);
        logger.logTrace("Exit");
        return statistics;
    }

    /**
     * Read the team ids implementation. If the user is a member of the team,
     * read the team ids from the backup.
     * 
     * @param user
     *            A <code>User</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return A <code>List</code> of <code>JabberId</code>s.
     */
    private List<JabberId> readTeamIdsImpl(final User user, final UUID uniqueId) {
        if (isContainerBackedUp(user, uniqueId)) {
            final InternalModelFactory modelFactory = getModelFactory(user);
            final Long artifactId = modelFactory.getArtifactModel().readId(uniqueId);
            return modelFactory.getArtifactModel().readTeamIds(artifactId);
        } else {
            logger.logWarning("Container {0} is not backed up for user {1}.",
                    uniqueId, user.getId());
            return Collections.emptyList();
        }
    }

    /**
     * Restore implementation.
     * 
     * @param user
     *            A <code>User</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     */
    private void restoreImpl(final User user, final UUID uniqueId) {
        final Artifact artifact = getArtifactModel().read(uniqueId);
        backupSql.deleteArchive(user, artifact);
    }
}

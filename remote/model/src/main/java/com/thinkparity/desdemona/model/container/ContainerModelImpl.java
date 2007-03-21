/*
 * Generated On: Jul 06 06 09:02:36 AM
 */
package com.thinkparity.desdemona.model.container;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactPublishedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContainerPublishedEvent;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.artifact.InternalArtifactModel;
import com.thinkparity.desdemona.model.io.sql.ArtifactSql;
import com.thinkparity.desdemona.model.session.Session;


/**
 * <b>Title:</b>thinkParity Container Model Implementation</br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1
 */
class ContainerModelImpl extends AbstractModelImpl {

    /** Artifact database io. */
    private final ArtifactSql artifactSql;

    /**
     * Create ContainerModelImpl.
     *
     * @param session
     *		The user's session.
     */
    ContainerModelImpl(final Session session) {
        super(session);
        this.artifactSql = new ArtifactSql();
    }

    protected final <T extends User, U extends User> void assertNotContains(
            final List<T> list, final U element, final String message,
            final Object... messageArguments) {
        Assert.assertNotTrue(contains(list, element), message, messageArguments);
    }

    // TODO-javadoc ContainerModelImpl#publish
    void publish(final JabberId userId, final ContainerVersion version,
            final ContainerVersion latestVersion,
            final Map<DocumentVersion, String> documentVersions,
            final List<TeamMember> teamMembers, final JabberId publishedBy,
            final Calendar publishedOn, final List<User> publishedTo) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("version", version);
        logger.logVariable("latestVersion", latestVersion);
        logger.logVariable("documentVersions", documentVersions);
        logger.logVariable("teamMembers", teamMembers);
        logger.logVariable("publishedBy", publishedBy);
        logger.logVariable("publishedOn", publishedOn);
        logger.logVariable("publishedTo", publishedTo);
        try {
            final List<JabberId> publishedToIds = new ArrayList<JabberId>();
            final List<JabberId> teamMemberIds = new ArrayList<JabberId>();
            for (final User publishedToUser : publishedTo)
                publishedToIds.add(publishedToUser.getId());
            for (final TeamMember teamMember : teamMembers)
                teamMemberIds.add(teamMember.getId());

            final ContainerPublishedEvent publishedEvent = new ContainerPublishedEvent();
            publishedEvent.setDocumentVersions(documentVersions);
            publishedEvent.setPublishedBy(publishedBy);
            publishedEvent.setPublishedOn(publishedOn);
            publishedEvent.setPublishedTo(publishedTo);
            publishedEvent.setVersion(version);
            enqueueEvent(session.getJabberId(), publishedToIds, publishedEvent);

            final ArtifactPublishedEvent artifactPublishedEvent = new ArtifactPublishedEvent();
            artifactPublishedEvent.setLatestVersion(version.equals(latestVersion));
            artifactPublishedEvent.setPublishedBy(publishedBy);
            artifactPublishedEvent.setPublishedOn(publishedOn);
            artifactPublishedEvent.setUniqueId(version.getArtifactUniqueId());
            artifactPublishedEvent.setVersionId(version.getVersionId());
            final List<JabberId> enqueueTo = new ArrayList<JabberId>(teamMembers.size() + publishedTo.size());
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
            final Artifact artifact = getArtifactModel().read(version.getArtifactUniqueId());
            final List<TeamMember> localTeam = artifactModel.readTeam(userId, artifact.getId());
            final User publishedByUser = getUserModel().read(publishedBy);
            if (!contains(localTeam, publishedByUser))
                artifactModel.addTeamMember(userId, artifact.getId(), publishedByUser.getLocalId());
            artifactSql.updateDraftOwner(artifact.getId(),
                    User.THINK_PARITY.getId(), publishedBy, publishedOn);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the archived containers for user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>List&lt;Container&gt;</code>.
     */
    List<Container> readArchive(final JabberId userId) {
        logApiId();
        logVariable("userId", userId);
        try {
            assertIsAuthenticatedUser(userId);
            return getArchiveModel().getContainerReader(userId).read();
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read an archive container for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A <code>Container</code>.
     */
    Container readArchive(final JabberId userId, final UUID uniqueId) {
        logApiId();
        logVariable("userId", userId);
        logVariable("unqiueId", uniqueId);
        try {
            assertIsAuthenticatedUser(userId);
            return getArchiveModel().getContainerReader(userId).read(uniqueId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the archived documents for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A unique id <code>UUID</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @return A <code>List&lt;Document&gt;</code>.
     */
    List<Document> readArchiveDocuments(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        logApiId();
        logVariable("userId", userId);
        logVariable("uniqueId", uniqueId);
        logVariable("versionId", versionId);
        try {
            assertIsAuthenticatedUser(userId);
            return getArchiveModel().getDocumentReader(userId, uniqueId,
                    versionId).read();
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    DocumentVersion readArchiveDocumentVersion(final JabberId userId,
            final UUID uniqueId, final UUID documentUniqueId,
            final Long documentVersionId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("documentUniqueId", documentUniqueId);
        logger.logVariable("documentVersionId", documentVersionId);
        try {
            assertIsAuthenticatedUser(userId);
            return getArchiveModel().getDocumentReader(userId, uniqueId)
                    .readVersion(documentUniqueId, documentVersionId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    Map<DocumentVersion, Delta> readArchiveDocumentVersionDeltas(
            final JabberId userId, final UUID uniqueId, final Long compareVersionId) {
        logApiId();
        logVariable("userId", userId);
        logVariable("uniqueId", uniqueId);
        logVariable("compareVersionId", compareVersionId);
        try {
            assertIsAuthenticatedUser(userId);
            return getArchiveModel().getDocumentReader(userId, uniqueId,
                    compareVersionId).readVersionDeltas(null);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }
    Map<DocumentVersion, Delta> readArchiveDocumentVersionDeltas(
            final JabberId userId, final UUID uniqueId,
            final Long compareVersionId, final Long compareToVersionId) {
        logApiId();
        logVariable("userId", userId);
        logVariable("uniqueId", uniqueId);
        logVariable("compareVersionId", compareVersionId);
        logVariable("compareToVersionId", compareToVersionId);
        try {
            assertIsAuthenticatedUser(userId);
            return getArchiveModel().getDocumentReader(userId, uniqueId, compareVersionId)
                    .readVersionDeltas(null, compareToVersionId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }
    /**
     * Read the archived document versions for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A unique id </code>UUID </code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @param documentUniqueId
     *            A document unique id <code>UUID</code>.
     * @return A <code>List&lt;DocumentVersion&gt;</code>.
     */
    List<DocumentVersion> readArchiveDocumentVersions(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        logApiId();
        logVariable("userId", userId);
        logVariable("uniqueId", uniqueId);
        logVariable("versionId", versionId);
        try {
            assertIsAuthenticatedUser(userId);
            return getArchiveModel().getDocumentReader(userId, uniqueId, versionId)
                    .readVersions(null);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    List<ArtifactReceipt> readArchivePublishedTo(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        try {
            assertIsAuthenticatedUser(userId);
            return getArchiveModel().getContainerReader(userId)
                    .readPublishedTo(uniqueId, versionId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the archived container versions for user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>List&lt;ContainerVersion&gt;</code>.
     */
    List<ContainerVersion> readArchiveVersions(final JabberId userId,
            final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        try {
            assertIsAuthenticatedUser(userId);
            return getArchiveModel().getContainerReader(userId).readVersions(
                    uniqueId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the backup containers for user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>List&lt;Container&gt;</code>.
     */
    List<Container> readBackup(final JabberId userId) {
        logApiId();
        logVariable("userId", userId);
        try {
            assertIsAuthenticatedUser(userId);
            return getBackupModel().getContainerReader(userId).read();
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read a backup container for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A <code>Container</code>.
     */
    Container readBackup(final JabberId userId, final UUID uniqueId) {
        logApiId();
        logVariable("userId", userId);
        logVariable("unqiueId", uniqueId);
        try {
            assertIsAuthenticatedUser(userId);
            return getBackupModel().getContainerReader(userId).read(uniqueId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the backup documents for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A unique id <code>UUID</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @return A <code>List&lt;Document&gt;</code>.
     */
    List<Document> readBackupDocuments(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        logApiId();
        logVariable("userId", userId);
        logVariable("uniqueId", uniqueId);
        logVariable("versionId", versionId);
        try {
            assertIsAuthenticatedUser(userId);
            return getBackupModel().getDocumentReader(userId, uniqueId,
                    versionId).read();
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the backup document versions for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A unique id </code>UUID </code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @param documentUniqueId
     *            A document unique id <code>UUID</code>.
     * @return A <code>List&lt;DocumentVersion&gt;</code>.
     */
    List<DocumentVersion> readBackupDocumentVersions(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        logApiId();
        logVariable("userId", userId);
        logVariable("uniqueId", uniqueId);
        logVariable("versionId", versionId);
        try {
            assertIsAuthenticatedUser(userId);
            return getBackupModel().getDocumentReader(userId, uniqueId, versionId)
                    .readVersions(null);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    List<ArtifactReceipt> readBackupPublishedTo(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        try {
            assertIsAuthenticatedUser(userId);
            return getBackupModel().getContainerReader(userId)
                    .readPublishedTo(uniqueId, versionId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the backup container versions for user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>List&lt;ContainerVersion&gt;</code>.
     */
    List<ContainerVersion> readBackupVersions(final JabberId userId,
            final UUID uniqueId) {
        logApiId();
        logVariable("userId", userId);
        try {
            assertIsAuthenticatedUser(userId);
            return getBackupModel().getContainerReader(userId).readVersions(
                    uniqueId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }
}

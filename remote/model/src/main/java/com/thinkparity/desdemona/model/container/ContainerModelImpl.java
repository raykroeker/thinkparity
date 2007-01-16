/*
 * Generated On: Jul 06 06 09:02:36 AM
 */
package com.thinkparity.desdemona.model.container;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactPublishedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContainerArtifactPublishedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContainerPublishedEvent;

import com.thinkparity.desdemona.model.AbstractModelImpl;
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

    /**
     * Publish the container version.
     * 
     * @param uniqueId
     *            A container unique id.
     * @param versionId
     *            A container version id.
     * @param name
     *            A container name.
     * @param artifactCount
     *            The number of artifacts in the container version.
     * @param publishedBy
     *            By whom the container was published.
     * @param publishedTo
     *            To whom to container was published.
     * @param publishedOn
     *            When the container was published.
     */
    void publish(final UUID uniqueId, final Long versionId, final String name,
            final String comment, final Integer artifactCount,
            final List<JabberId> team, final JabberId publishedBy,
            final List<JabberId> publishedTo, final Calendar publishedOn) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("name", name);
        logger.logVariable("comment", comment);
        logger.logVariable("artifactCount", artifactCount);
        logger.logVariable("team", team);
        logger.logVariable("publishedBy", publishedBy);
        logger.logVariable("publishedTo", publishedTo);
        logger.logVariable("publishedOn", publishedOn);
        try {
            final ContainerPublishedEvent publishedEvent = new ContainerPublishedEvent();
            publishedEvent.setArtifactCount(artifactCount);
            publishedEvent.setComment(comment);
            publishedEvent.setName(name);
            publishedEvent.setPublishedBy(publishedBy);
            publishedEvent.setPublishedOn(publishedOn);
            publishedEvent.setPublishedTo(publishedTo);
            publishedEvent.setUniqueId(uniqueId);
            publishedEvent.setVersionId(versionId);
            enqueueEvent(session.getJabberId(), publishedTo, publishedEvent);

            final ArtifactPublishedEvent artifactPublishedEvent = new ArtifactPublishedEvent();
            artifactPublishedEvent.setPublishedBy(publishedBy);
            artifactPublishedEvent.setPublishedOn(publishedOn);
            artifactPublishedEvent.setUniqueId(uniqueId);
            artifactPublishedEvent.setVersionId(versionId);
            artifactPublishedEvent.setTeamUserIds(team);
            final List<JabberId> enqueueTo = new ArrayList<JabberId>(team.size() + publishedTo.size());
            for (final JabberId jabberId : team)
                enqueueTo.add(jabberId);
            for (final JabberId jabberId : publishedTo) {
                if (!enqueueTo.contains(jabberId))
                    enqueueTo.add(jabberId);
            }
            enqueueEvent(session.getJabberId(), enqueueTo, artifactPublishedEvent);

            final Artifact artifact = getArtifactModel().read(uniqueId);
            artifactSql.updateKeyHolder(artifact.getId(),
                    User.THINK_PARITY.getId().getUsername(), publishedBy);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Publish a container version artifact version to a subset of team members.
     * 
     * @param uniqueId
     *            A container unique id.
     * @param versionId
     *            A container version id.
     * @param name
     *            A container name.
     * @param artifactCount
     *            An artifact count for the container version.
     * @param artifactIndex
     *            The artifact's index in the container version.
     * @param artifactUniqueId
     *            An artifact unique id.
     * @param artifactVersionId
     *            An artifact version id.
     * @param artifactName
     *            An artifact name.
     * @param artifactType
     *            An artifact type.
     * @param artifactChecksum
     *            An artifact checksum.
     * @param artifactStreamId
     *            A stream id.
     * @param publishTo
     *            To whom the container was published.
     * @param publishedBy
     *            By whom the artifact was published.
     * @param publishedOn
     *            When the artifact was published.
     */
    void publishArtifact(final UUID uniqueId, final Long versionId,
            final String name, final Integer artifactCount,
            final Integer artifactIndex, final UUID artifactUniqueId,
            final Long artifactVersionId, final String artifactName,
            final ArtifactType artifactType, final String artifactChecksum,
            final String artifactStreamId, final List<JabberId> publishTo,
            final JabberId publishedBy, final Calendar publishedOn) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        logVariable("versionId", versionId);
        logVariable("name", name);
        logVariable("artifactCount", artifactCount);
        logVariable("artifactIndex", artifactIndex);
        logVariable("artifactUniqueId", artifactUniqueId);
        logVariable("artifactVersionId", artifactVersionId);
        logVariable("artifactName", artifactName);
        logVariable("artifactType", artifactType);
        logVariable("artifactChecksum", artifactChecksum);
        logVariable("artifactStreamId", artifactStreamId);
        logVariable("publishTo", publishTo);
        logVariable("publishedBy", publishedBy);
        logVariable("publishedOn", publishedOn);
        try {
            final ContainerArtifactPublishedEvent publishArtifactEvent = new ContainerArtifactPublishedEvent();
            publishArtifactEvent.setArtifactChecksum(artifactChecksum);
            publishArtifactEvent.setArtifactName(artifactName);
            publishArtifactEvent.setArtifactStreamId(artifactStreamId);
            publishArtifactEvent.setArtifactType(artifactType);
            publishArtifactEvent.setArtifactUniqueId(artifactUniqueId);
            publishArtifactEvent.setArtifactVersionId(artifactVersionId);
            publishArtifactEvent.setArtifactCount(artifactCount);
            publishArtifactEvent.setArtifactIndex(artifactIndex);
            publishArtifactEvent.setName(name);
            publishArtifactEvent.setUniqueId(uniqueId);
            publishArtifactEvent.setVersionId(versionId);
            publishArtifactEvent.setPublishedBy(publishedBy);
            publishArtifactEvent.setPublishedOn(publishedOn);
            enqueueEvent(session.getJabberId(), publishTo, publishArtifactEvent);
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

    Map<User, ArtifactReceipt> readArchivePublishedTo(final JabberId userId, final UUID uniqueId, final Long versionId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        try {
            assertIsAuthenticatedUser(userId);
            return getArchiveModel().getContainerReader(userId)
                    .readPublishedTo(uniqueId, versionId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    Map<User, ArtifactReceipt> readBackupPublishedTo(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        try {
            assertIsAuthenticatedUser(userId);
            return getBackupModel().getContainerReader(userId)
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

/*
 * Generated On: Jul 06 06 09:02:36 AM
 */
package com.thinkparity.desdemona.model.container;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.AbstractModel;
import com.thinkparity.desdemona.model.session.Session;


/**
 * <b>Title:</b>thinkParity Container Model<br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1
 */
public class ContainerModel extends AbstractModel<ContainerModelImpl> {

	/**
	 * Create a Container interface.
	 * 
	 * @return The Container interface.
	 */
	public static ContainerModel getModel(final Session session) {
		return new ContainerModel(session);
	}

	/**
	 * Create ContainerModel.
	 *
	 * @param workspace
	 *		The thinkParity workspace.
	 */
	protected ContainerModel(final Session session) {
		super(new ContainerModelImpl(session));
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
     *            Whom to container was published to.
     * @param publishedOn
     *            When the container was published.
     */
    public void publish(final UUID uniqueId, final Long versionId,
            final String name, final String comment, final List<JabberId> team,
            final Integer artifactCount, final JabberId publishedBy,
            final List<JabberId> publishedTo, final Calendar publishedOn) {
        synchronized (getImplLock()) {
            getImpl().publish(uniqueId, versionId, name, comment, team,
                    artifactCount, publishedBy, publishedTo, publishedOn);
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
     *            A stream id <code>String</code>.
     * @param publishTo
     *            To whom the container was published.
     * @param publishedBy
     *            By whom the artifact was published.
     * @param publishedOn
     *            When the artifact was published.
     */
    public void publishArtifact(final UUID uniqueId, final Long versionId,
            final String name, final Integer artifactCount,
            final Integer artifactIndex, final UUID artifactUniqueId,
            final Long artifactVersionId, final String artifactName,
            final ArtifactType artifactType, final String artifactChecksum,
            final String artifactStreamId, final List<JabberId> publishTo,
            final JabberId publishedBy, final Calendar publishedOn) {
        synchronized (getImplLock()) {
            getImpl().publishArtifact(uniqueId, versionId, name, artifactCount,
                    artifactIndex, artifactUniqueId, artifactVersionId,
                    artifactName, artifactType, artifactChecksum,
                    artifactStreamId, publishTo, publishedBy, publishedOn);
        }
    }

    /**
     * Read the archived containers for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>List&lt;Container&gt;</code>.
     */
    public List<Container> readArchive(final JabberId userId) {
        synchronized (getImplLock()) {
            return getImpl().readArchive(userId);
        }
    }

    /**
     * Read an archive container.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A <code>Container</code>.
     */
    public Container readArchive(final JabberId userId, final UUID uniqueId) {
        synchronized (getImplLock()) {
            return getImpl().readArchive(userId, uniqueId);
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
    public List<Document> readArchiveDocuments(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        synchronized (getImplLock()) {
            return getImpl().readArchiveDocuments(userId, uniqueId, versionId);
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
    public List<DocumentVersion> readArchiveDocumentVersions(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        synchronized (getImplLock()) {
            return getImpl().readArchiveDocumentVersions(userId, uniqueId,
                    versionId);
        }
    }


    /**
     * Read the archived container versions for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A list of container versions.
     */
    public List<ContainerVersion> readArchiveVersions(final JabberId userId,
            final UUID uniqueId) {
        synchronized (getImplLock()) {
            return getImpl().readArchiveVersions(userId, uniqueId);
        }
    }

    public Map<User, ArtifactReceipt> readArchivePublishedTo(final JabberId userId, final UUID uniqueId, final Long versionId) {
        synchronized (getImplLock()) {
            return getImpl().readArchivePublishedTo(userId, uniqueId, versionId);
        }
    }

    /**
     * Read the backup containers for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>List&lt;Container&gt;</code>.
     */
    public List<Container> readBackup(final JabberId userId) {
        synchronized (getImplLock()) {
            return getImpl().readBackup(userId);
        }
    }


    /**
     * Read a backup container.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A <code>Container</code>.
     */
    public Container readBackup(final JabberId userId, final UUID uniqueId) {
        synchronized (getImplLock()) {
            return getImpl().readBackup(userId, uniqueId);
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
    public List<Document> readBackupDocuments(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        synchronized (getImplLock()) {
            return getImpl().readBackupDocuments(userId, uniqueId, versionId);
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
    public List<DocumentVersion> readBackupDocumentVersions(
            final JabberId userId, final UUID uniqueId, final Long versionId) {
        synchronized (getImplLock()) {
            return getImpl().readBackupDocumentVersions(userId, uniqueId, versionId);
        }
    }


    /**
     * Read the backup container versions for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A list of container versions.
     */
    public List<ContainerVersion> readBackupVersions(final JabberId userId,
            final UUID uniqueId) {
        synchronized (getImplLock()) {
            return getImpl().readBackupVersions(userId, uniqueId);
        }
    }
}

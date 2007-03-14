/*
 * Generated On: Jul 06 06 09:02:36 AM
 */
package com.thinkparity.desdemona.model.container;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.TeamMember;
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

    // TODO-javadoc ContainerModel#publish
    public void publish(final JabberId userId, final ContainerVersion version,
            final Map<DocumentVersion, String> documentVersions,
            final List<TeamMember> teamMembers, final JabberId publishedBy,
            final Calendar publishedOn, final List<User> publishedTo) {
        synchronized (getImplLock()) {
            getImpl().publish(userId, version, documentVersions, teamMembers,
                    publishedBy, publishedOn, publishedTo);
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

    public DocumentVersion readArchiveDocumentVersion(final JabberId userId,
            final UUID uniqueId, final UUID documentUniqueId,
            final Long documentVersionId) {
        synchronized (getImplLock()) {
            return getImpl().readArchiveDocumentVersion(userId, uniqueId,
                    documentUniqueId, documentVersionId);
        }
    }

    public Map<DocumentVersion, Delta> readArchiveDocumentVersionDeltas(
            final JabberId userId, final UUID uniqueId,
            final Long compareVersionId) {
        synchronized (getImplLock()) {
            return getImpl().readArchiveDocumentVersionDeltas(userId, uniqueId,
                    compareVersionId);
        }
    }
    public Map<DocumentVersion, Delta> readArchiveDocumentVersionDeltas(
            final JabberId userId, final UUID uniqueId,
            final Long compareVersionId, final Long compareToVersionId) {
        synchronized (getImplLock()) {
            return getImpl().readArchiveDocumentVersionDeltas(userId, uniqueId,
                    compareVersionId, compareToVersionId);
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

    public List<ArtifactReceipt> readArchivePublishedTo(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        synchronized (getImplLock()) {
            return getImpl().readArchivePublishedTo(userId, uniqueId, versionId);
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

    public List<ArtifactReceipt> readBackupPublishedTo(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        synchronized (getImplLock()) {
            return getImpl().readBackupPublishedTo(userId, uniqueId, versionId);
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


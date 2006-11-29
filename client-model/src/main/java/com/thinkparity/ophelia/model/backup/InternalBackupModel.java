/*
 * Generated On: Oct 04 06 09:40:46 AM
 */
package com.thinkparity.ophelia.model.backup;

import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.InternalModel;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Backup Internal Model<br>
 * <b>Description:</b><br>
 *
 * @author CreateModel.groovy
 * @version 1.1.2.1
 */
public class InternalBackupModel extends BackupModel implements InternalModel {

    /**
     * Create InternalBackupModel
     *
     * @param context
     *		A thinkParity model <code>Context</code>.
     * @param environment
     *      A thinkParity <code>Environment</code>.
     * @param workspace
     *		A thinkParity <code>Workspace</code>.
     */
    InternalBackupModel(final Context context,
            final Environment environment, final Workspace workspace) {
        super(environment, workspace);
    }

    /**
     * @see com.thinkparity.ophelia.model.archive.ArchiveReader#openDocumentVersion(java.util.UUID, java.lang.Long)
     */
    public InputStream openDocumentVersion(final UUID uniqueId,
            final Long versionId) {
        synchronized (getImplLock()) {
            return getImpl().openDocumentVersion(uniqueId, versionId);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.archive.ArchiveReader#readContainer(java.util.UUID)
     */
    public Container readContainer(final UUID uniqueId) {
        synchronized (getImplLock()) {
            return getImpl().readContainer(uniqueId);
        }
    }

    /**
     * Read the containers from the backup.
     * 
     * @return A <code>List&lt;Container&gt;</code>.
     */
    public List<Container> readContainers() {
        synchronized (getImplLock()) {
            return getImpl().readContainers();
        }
    }

    /**
     * Read the containers from the backup.
     * 
     * @param comparator
     *            A <code>Comparator&lt;Artifact&gt;</code>.
     * @return A <code>List&lt;Container&gt;</code>.
     */
    public List<Container> readContainers(final Comparator<Artifact> comparator) {
        synchronized (getImplLock()) {
            return getImpl().readContainers(comparator);
        }
    }

    /**
     * Read the containers from the backup.
     * 
     * @param comparator
     *            A <code>Comparator&lt;Artifact&gt;</code>.
     * @param filter
     *            A <code>Filter&lt;? super Artifact&gt;</code>.
     * @return A <code>List&lt;Container&gt;</code>.
     */
    public List<Container> readContainers(final Comparator<Artifact> comparator, final Filter<? super Artifact> filter) {
        synchronized (getImplLock()) {
            return getImpl().readContainers(comparator, filter);
        }
    }

    /**
     * Read the containers from the backup.
     * 
     * @param filter
     *            A <code>Filter&lt;Artifact&gt;</code>.
     * @return A <code>List&lt;Container&gt;</code>.
     */
    public List<Container> readContainers(final Filter<? super Artifact> filter) {
        synchronized (getImplLock()) {
            return getImpl().readContainers(filter);
        }
    }

    public List<ContainerVersion> readContainerVersions(final UUID uniqueId) {
        synchronized (getImplLock()) {
            return getImpl().readContainerVersions(uniqueId);
        }
    }

    public List<ContainerVersion> readContainerVersions(final UUID uniqueId,
            final Comparator<ArtifactVersion> comparator) {
        synchronized (getImplLock()) {
            return getImpl().readContainerVersions(uniqueId, comparator);
        }
    }

    public List<ContainerVersion> readContainerVersions(final UUID uniqueId,
            final Comparator<ArtifactVersion> comparator,
            final Filter<? super ArtifactVersion> filter) {
        synchronized (getImplLock()) {
            return getImpl().readContainerVersions(uniqueId, comparator,
                    filter);
        }
    }

    public List<ContainerVersion> readContainerVersions(final UUID uniqueId,
            final Filter<? super ArtifactVersion> filter) {
        synchronized (getImplLock()) {
            return getImpl().readContainerVersions(uniqueId, filter);
        }
    }

    public List<Document> readDocuments(final UUID uniqueId,
            final Long versionId) {
        synchronized (getImplLock()) {
            return getImpl().readDocuments(uniqueId, versionId);
        }
    }

    public List<Document> readDocuments(final UUID uniqueId,
            final Long versionId, final Comparator<Artifact> comparator) {
        synchronized (getImplLock()) {
            return getImpl().readDocuments(uniqueId, versionId, comparator);
        }
    }

    public List<Document> readDocuments(final UUID uniqueId,
            final Long versionId, final Comparator<Artifact> comparator,
            final Filter<? super Artifact> filter) {
        synchronized (getImplLock()) {
            return getImpl().readDocuments(uniqueId, versionId, comparator,
                    filter);
        }
    }

    public List<Document> readDocuments(final UUID uniqueId,
            final Long versionId, final UUID documentUniqueId,
            final Filter<? super Artifact> filter) {
        synchronized (getImplLock()) {
            return getImpl().readDocuments(uniqueId, versionId,
                    documentUniqueId, filter);
        }
    }

    public List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId) {
        synchronized (getImplLock()) {
            return getImpl().readDocumentVersions(uniqueId, versionId);
        }
    }

    public List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId, final UUID documentUniqueId,
            final Comparator<ArtifactVersion> comparator) {
        synchronized (getImplLock()) {
            return getImpl().readDocumentVersions(uniqueId, versionId,
                    comparator);
        }
    }

    public List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId, final UUID documentUniqueId,
            final Comparator<ArtifactVersion> comparator,
            final Filter<? super ArtifactVersion> filter) {
        synchronized (getImplLock()) {
            return getImpl().readDocumentVersions(uniqueId, versionId,
                    comparator, filter);
        }
    }

    public List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId, final UUID documentUniqueId,
            final Filter<? super ArtifactVersion> filter) {
        synchronized (getImplLock()) {
            return getImpl().readDocumentVersions(uniqueId, versionId, filter);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.archive.ArchiveReader#readTeamIds(java.util.UUID)
     */
    public List<JabberId> readTeamIds(UUID uniqueId) {
        synchronized (getImplLock()) {
            return getImpl().readTeamIds(uniqueId);
        }
    }
}

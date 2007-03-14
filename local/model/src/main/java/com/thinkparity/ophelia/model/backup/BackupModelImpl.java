/*
 * Generated On: Oct 04 06 09:40:46 AM
 */
package com.thinkparity.ophelia.model.backup;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.filter.FilterManager;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.DownloadMonitor;
import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.util.xmpp.event.BackupStatisticsUpdatedEvent;

import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.session.InternalSessionModel;
import com.thinkparity.ophelia.model.util.sort.ComparatorBuilder;
import com.thinkparity.ophelia.model.util.sort.ModelSorter;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Backup Model Implementation</br>
 * <b>Description:</b><br>
 *
 * @author CreateModel.groovy
 * @version 1.1.2.1
 */
public final class BackupModelImpl extends Model implements
        BackupModel, InternalBackupModel {

    /** A default artifact comparator. */
    private final Comparator<Artifact> defaultComparator;

    /** A default artifact filter. */
    private final Filter<? super Artifact> defaultFilter;

    /** The default artifact receipt comparator. */
    private final Comparator<ArtifactReceipt> defaultReceiptComparator;

    /** The default artifact receipt filter. */
    private final Filter<? super ArtifactReceipt> defaultReceiptFilter;

    /** A default artifact version comparator. */
    private final Comparator<ArtifactVersion> defaultVersionComparator;

    /** A default artifact version filter. */
    private final Filter<ArtifactVersion> defaultVersionFilter;

    /**
     * Create BackupModelImpl.
     *
     */
    public BackupModelImpl() {
        super();
        this.defaultComparator = new ComparatorBuilder().createByName();
        this.defaultFilter = FilterManager.createDefault();
        this.defaultReceiptComparator = new ComparatorBuilder().createArtifactReceiptByReceivedOnAscending();
        this.defaultReceiptFilter = FilterManager.createDefault();
        this.defaultVersionComparator = new ComparatorBuilder().createVersionById(Boolean.TRUE);
        this.defaultVersionFilter = FilterManager.createDefault();
    }

    /**
     * @see com.thinkparity.ophelia.model.backup.InternalBackupModel#archive(java.lang.Long)
     *
     */
    public void archive(final Long artifactId) {
        try {
            assertBackupOnline();

            final UUID uniqueId = getArtifactModel().readUniqueId(artifactId);
            getSessionModel().archiveArtifact(localUserId(), uniqueId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.backup.InternalBackupModel#handleStatisticsUpdated(com.thinkparity.codebase.model.util.xmpp.event.BackupStatisticsUpdatedEvent)
     *
     */
    public void handleStatisticsUpdated(final BackupStatisticsUpdatedEvent event) {
    }

    /**
     * Open a document version from the backup.
     * 
     * @param uniqueId
     *            A unique id <code>UUID</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @return An <code>InputStream</code>.
     */
    public InputStream openDocumentVersion(final UUID uniqueId,
            final Long versionId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        try {
            assertBackupOnline();
            final InternalSessionModel sessionModel = getSessionModel();
            final StreamSession session = sessionModel.createStreamSession();
            final String streamId = sessionModel.createStream(session);
            sessionModel.createBackupStream(localUserId(), streamId, uniqueId,
                    versionId);
            try {
                final File file = downloadStream(new DownloadMonitor() {
                    public void chunkDownloaded(final int chunkSize) {
                        logger.logApiId();
                        logger.logVariable("chunkSize", chunkSize);
                    }}, streamId);
                logger.logVariable("file.getName()", file.getName());
                logger.logVariable("file.length()", file.length());
                return new FileInputStream(file);
            } finally {
                sessionModel.deleteStreamSession(session);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    public Container readContainer(final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        try {
            assertBackupOnline();
            return getSessionModel().readBackupContainer(localUserId(), uniqueId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the containers from the backup.
     * 
     * @return A <code>List&lt;Container&gt;</code>.
     */
    public List<Container> readContainers() {
        try {
            return readContainers(defaultComparator);
        } catch (final Throwable t) {
            throw panic(t);
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
        try {
            return readContainers(comparator, defaultFilter);
        } catch (final Throwable t) {
            throw panic(t);
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
        try {
            assertBackupOnline();
            final List<Container> containers =
                getSessionModel().readBackupContainers(localUserId());
            FilterManager.filter(containers, filter);
            ModelSorter.sortContainers(containers, comparator);
            return containers;
        } catch (final Throwable t) {
            throw panic(t);
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
        logger.logApiId();
        return readContainers(defaultComparator, filter);
    }

    public List<ContainerVersion> readContainerVersions(final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        return readContainerVersions(uniqueId, defaultVersionComparator,
                defaultVersionFilter);
    }

    public List<ContainerVersion> readContainerVersions(final UUID uniqueId,
            final Comparator<ArtifactVersion> comparator) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("comparator", comparator);
        return readContainerVersions(uniqueId, comparator,
                defaultVersionFilter);
    }

    public List<ContainerVersion> readContainerVersions(final UUID uniqueId,
            final Comparator<ArtifactVersion> comparator,
            final Filter<? super ArtifactVersion> filter) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("comparator", comparator);
        logger.logVariable("filter", filter);
        try {
            assertBackupOnline();
            final List<ContainerVersion> versions =
                getSessionModel().readBackupContainerVersions(
                        localUserId(), uniqueId);
            FilterManager.filter(versions, filter);
            ModelSorter.sortContainerVersions(versions, comparator);
            return versions;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    public List<ContainerVersion> readContainerVersions(final UUID uniqueId,
            final Filter<? super ArtifactVersion> filter) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("filter", filter);
        return readContainerVersions(uniqueId, defaultVersionComparator,
                filter);
    }

    public List<Document> readDocuments(final UUID uniqueId,
            final Long versionId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        return readDocuments(uniqueId, versionId, defaultComparator,
                defaultFilter);
    }

    public List<Document> readDocuments(final UUID uniqueId,
            final Long versionId, final Comparator<Artifact> comparator) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("comparator", comparator);
        return readDocuments(uniqueId, versionId, comparator, defaultFilter);
    }

    public List<Document> readDocuments(final UUID uniqueId,
            final Long versionId, final Comparator<Artifact> comparator,
            final Filter<? super Artifact> filter) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("filter", filter);
        try {
            assertBackupOnline();
            final List<Document> documents =
                getSessionModel().readBackupDocuments(localUserId(),
                        uniqueId, versionId);
            FilterManager.filter(documents, filter);
            ModelSorter.sortDocuments(documents, comparator);
            return documents;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    public List<Document> readDocuments(final UUID uniqueId, final Long versionId,
            final UUID documentUniqueId, final Filter<? super Artifact> filter) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("filter", filter);
        return readDocuments(uniqueId, versionId, defaultComparator, filter);
    }

    /**
     * @see com.thinkparity.ophelia.model.backup.InternalBackupModel#readDocumentVersions(java.util.UUID,
     *      java.lang.Long)
     * 
     */
    public List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        return readDocumentVersions(uniqueId, versionId,
                defaultVersionComparator, defaultVersionFilter);
    }

    public List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId, final Comparator<ArtifactVersion> comparator) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("comparator", comparator);
        return readDocumentVersions(uniqueId, versionId, comparator,
                defaultVersionFilter);
    }

    public List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId, final Comparator<ArtifactVersion> comparator,
            final Filter<? super ArtifactVersion> filter) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("comparator", comparator);
        try {
            assertBackupOnline();
            final List<DocumentVersion> versions =
                getSessionModel().readBackupDocumentVersions(
                        localUserId(), uniqueId, versionId);
            FilterManager.filter(versions, filter);
            ModelSorter.sortDocumentVersions(versions, comparator);
            return versions;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    public List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId, final Filter<? super ArtifactVersion> filter) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("filter", filter);
        return readDocumentVersions(uniqueId, versionId,
                defaultVersionComparator, filter);
    }

    /**
     * @see com.thinkparity.ophelia.model.backup.InternalBackupModel#readPublishedTo(java.util.UUID,
     *      java.lang.Long)
     * 
     */
    public List<ArtifactReceipt> readPublishedTo(final UUID uniqueId,
            final Long versionId) {
        try {
            return readPublishedTo(uniqueId, versionId, defaultReceiptComparator,
                    defaultReceiptFilter);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.backup.InternalBackupModel#readPublishedTo(java.util.UUID,
     *      java.lang.Long, java.util.Comparator)
     * 
     */
    public List<ArtifactReceipt> readPublishedTo(final UUID uniqueId,
            final Long versionId, final Comparator<ArtifactReceipt> comparator) {
        try {
            return readPublishedTo(uniqueId, versionId, comparator,
                    defaultReceiptFilter);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.backup.InternalBackupModel#readPublishedTo(java.util.UUID,
     *      java.lang.Long, java.util.Comparator,
     *      com.thinkparity.codebase.filter.Filter)
     * 
     */
    public List<ArtifactReceipt> readPublishedTo(final UUID uniqueId,
            final Long versionId, final Comparator<ArtifactReceipt> comparator,
            final Filter<? super ArtifactReceipt> filter) {
        try {
            final List<ArtifactReceipt> publishedTo =
                getSessionModel().readBackupPublishedTo(localUserId(),
                        uniqueId, versionId);
            FilterManager.filter(publishedTo, filter);
            ModelSorter.sortReceipts(publishedTo, comparator);
            return publishedTo;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Read a list of team members the container version was published to.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @param filter
     *            A <code>Filter&lt;? super User&gt;</code>.
     * @return A <code>List&lt;User&gt;</code>.
     */
    public List<ArtifactReceipt> readPublishedTo(final UUID uniqueId,
            final Long versionId, final Filter<? super ArtifactReceipt> filter) {
        try {
            return readPublishedTo(uniqueId, versionId,
                    defaultReceiptComparator, filter); 
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    public List<JabberId> readTeamIds(final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        try {
            assertBackupOnline();
            return getSessionModel().readBackupTeamIds(localUserId(),
                    uniqueId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }


    /**
     * @see com.thinkparity.ophelia.model.backup.InternalBackupModel#restore(java.lang.Long)
     *
     */
    public void restore(final Long artifactId) {
        try {
            assertBackupOnline();

            final UUID uniqueId = getArtifactModel().readUniqueId(artifactId);
            getSessionModel().restoreArtifact(localUserId(), uniqueId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#initializeModel(com.thinkparity.codebase.model.session.Environment, com.thinkparity.ophelia.model.workspace.Workspace)
     *
     */
    @Override
    protected void initializeModel(final Environment environment,
            final Workspace workspace) {}

    /**
     * Assert that the backup server is online.
     *
     */
    private void assertBackupOnline() {
        Assert.assertTrue(isBackupOnline(),
                "The backup server is not online.");
    }

    /**
     * Determine whether or not the backup is online or not.
     * 
     * @return True if the backup is online; false otherwise.
     */
    private Boolean isBackupOnline() {
        return isOnline();
    }
}

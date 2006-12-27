/*
 * Generated On: Sep 01 06 10:06:21 AM
 */
package com.thinkparity.ophelia.model.archive;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Map.Entry;

import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.filter.FilterManager;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.AbstractModelImpl;
import com.thinkparity.ophelia.model.DownloadMonitor;
import com.thinkparity.ophelia.model.archive.monitor.OpenMonitor;
import com.thinkparity.ophelia.model.archive.monitor.OpenStage;
import com.thinkparity.ophelia.model.session.InternalSessionModel;
import com.thinkparity.ophelia.model.util.Opener;
import com.thinkparity.ophelia.model.util.sort.ComparatorBuilder;
import com.thinkparity.ophelia.model.util.sort.ModelSorter;
import com.thinkparity.ophelia.model.util.sort.user.UserComparatorFactory;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Archive Model Implementation</br>
 * <b>Description:</b><br>
 *
 * @author CreateModel.groovy
 * @version 1.1.2.1
 */
class ArchiveModelImpl extends AbstractModelImpl {

    private static final int STEP_SIZE = 1024;

    /** A default artifact comparator. */
    private final Comparator<Artifact> defaultComparator;

    /** A default artifact filter. */
    private final Filter<? super Artifact> defaultFilter;

    /** A default user comparator. */
    private final Comparator<User> defaultUserComparator;

    /** A default user filter. */
    private final Filter<? super User> defaultUserFilter;

    /** A default artifact version comparator. */
    private final Comparator<ArtifactVersion> defaultVersionComparator;

    /** A default artifact version filter. */
    private final Filter<ArtifactVersion> defaultVersionFilter;

    /**
     * Create ArchiveModelImpl.
     *
     * @param workspace
     *		The thinkParity workspace.
     */
    ArchiveModelImpl(final Environment environment, final Workspace workspace) {
        super(environment, workspace);
        this.defaultComparator = new ComparatorBuilder().createByName();
        this.defaultFilter = FilterManager.createDefault();
        this.defaultUserComparator = UserComparatorFactory.createOrganizationAndName(Boolean.TRUE);
        this.defaultUserFilter = FilterManager.createDefault();
        this.defaultVersionComparator = new ComparatorBuilder().createVersionById(Boolean.FALSE);
        this.defaultVersionFilter = FilterManager.createDefault();
    }

    void archive(final Long artifactId) {
        logger.logApiId();
        logger.logVariable("artifactId", artifactId);
        try {
            assertArchiveOnline();
            final UUID uniqueId = getArtifactModel().readUniqueId(artifactId);
            getSessionModel().archiveArtifact(localUserId(), uniqueId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    void openDocumentVersion(final OpenMonitor monitor, final UUID uniqueId,
            final Long versionId, final String versionName,
            final long versionSize, final Opener opener) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        try {
            assertArchiveOnline();
            final InternalSessionModel sessionModel = getSessionModel();
            final StreamSession session = sessionModel.createStreamSession();
            final String streamId = sessionModel.createStream(session);
            final File streamFile;
            if (isStreamDownloadComplete(streamId, versionSize)) {
                streamFile = locateStreamFile(streamId);
                final File archiveFile = new File(streamFile.getParent(),
                        streamFile.getName() +
                        FileUtil.getExtension(versionName));
                if (!archiveFile.exists())
                    Assert.assertTrue(streamFile.renameTo(archiveFile),
                            "Could not open document version.");
                opener.open(archiveFile);
            } else {
                sessionModel.createArchiveStream(localUserId(), streamId,
                        uniqueId, versionId);
                try {
                    streamFile = downloadStream(new DownloadMonitor() {
                        long totalChunks = versionSize;
                        public void chunkDownloaded(final int chunkSize) {
                            totalChunks += chunkSize;
                            if (totalChunks >= STEP_SIZE) {
                                totalChunks -= STEP_SIZE;
                                fireStageEnd(monitor, OpenStage.DownloadStream);
                            }
                        }
                    }, streamId);
                    final File archiveFile = new File(streamFile.getParent(),
                            streamFile.getName() +
                            FileUtil.getExtension(versionName));
                    Assert.assertTrue(streamFile.renameTo(archiveFile),
                            "Could not open document version.");
                    opener.open(archiveFile);
                } finally {
                    sessionModel.deleteStreamSession(session);
                }
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    InputStream openDocumentVersion(final UUID uniqueId, final Long versionId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        try {
            assertArchiveOnline();
            final InternalSessionModel sessionModel = getSessionModel();
            final StreamSession session = sessionModel.createStreamSession();
            final String streamId = sessionModel.createStream(session);
            sessionModel.createArchiveStream(localUserId(), streamId, uniqueId,
                    versionId);
            try {
                return new FileInputStream(downloadStream(new DownloadMonitor() {
                    public void chunkDownloaded(final int chunkSize) {
                        logger.logApiId();
                        logger.logVariable("chunkSize", chunkSize);
                    }
                }, streamId));
            } finally {
                sessionModel.deleteStreamSession(session);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    Container readContainer(final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        try {
            assertArchiveOnline();
            // HACK A quck'n'dirty check to see if the container exists locally
            if (null != getArtifactModel().readId(uniqueId)) {
                return null;
            } else {
                return getSessionModel().readArchiveContainer(
                        localUserId(), uniqueId);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    List<Container> readContainers() {
        logger.logApiId();
        return readContainers(defaultComparator);
    }

    List<Container> readContainers(final Comparator<Artifact> comparator) {
        logger.logApiId();
        logger.logVariable("comparator", comparator);
        return readContainers(comparator, defaultFilter);
    }

    List<Container> readContainers(final Comparator<Artifact> comparator,
            final Filter<? super Artifact> filter) {
        logger.logApiId();
        logger.logVariable("comparator", comparator);
        logger.logVariable("filter", filter);
        try {
            assertArchiveOnline();
            final List<Container> containers =
                getSessionModel().readArchiveContainers(localUserId());
            FilterManager.filter(containers, filter);
            ModelSorter.sortContainers(containers, comparator);
            return containers;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    List<Container> readContainers(final Filter<? super Artifact> filter) {
        logger.logApiId();
        logger.logVariable("filter", filter);
        return readContainers(defaultComparator, filter);
    }

    List<ContainerVersion> readContainerVersions(final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        return readContainerVersions(uniqueId, defaultVersionComparator,
                defaultVersionFilter);
    }

    List<ContainerVersion> readContainerVersions(final UUID uniqueId,
            final Comparator<ArtifactVersion> comparator) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("comparator", comparator);
        return readContainerVersions(uniqueId, comparator,
                defaultVersionFilter);
    }

    List<ContainerVersion> readContainerVersions(final UUID uniqueId,
            final Comparator<ArtifactVersion> comparator,
            final Filter<? super ArtifactVersion> filter) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("comparator", comparator);
        logger.logVariable("filter", filter);
        try {
            assertArchiveOnline();
            final List<ContainerVersion> versions =
                getSessionModel().readArchiveContainerVersions(
                        localUserId(), uniqueId);
            ModelSorter.sortContainerVersions(versions, comparator);
            FilterManager.filter(versions, filter);
            return versions;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    List<ContainerVersion> readContainerVersions(final UUID uniqueId,
            final Filter<? super ArtifactVersion> filter) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("filter", filter);
        return readContainerVersions(uniqueId, defaultVersionComparator,
                filter);
    }

    List<Document> readDocuments(final UUID uniqueId,
            final Long versionId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        return readDocuments(uniqueId, versionId, defaultComparator,
                defaultFilter);
    }

    List<Document> readDocuments(final UUID uniqueId,
            final Long versionId, final Comparator<Artifact> comparator) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("comparator", comparator);
        return readDocuments(uniqueId, versionId, comparator, defaultFilter);
    }

    List<Document> readDocuments(final UUID uniqueId,
            final Long versionId, final Comparator<Artifact> comparator,
            final Filter<? super Artifact> filter) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("filter", filter);
        try {
            assertArchiveOnline();
            final List<Document> documents =
                getSessionModel().readArchiveDocuments(localUserId(),
                        uniqueId, versionId);
            ModelSorter.sortDocuments(documents, comparator);
            FilterManager.filter(documents, filter);
            return documents;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    List<Document> readDocuments(final UUID uniqueId, final Long versionId,
            final UUID documentUniqueId, final Filter<? super Artifact> filter) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("filter", filter);
        return readDocuments(uniqueId, versionId, defaultComparator, filter);
    }

    DocumentVersion readDocumentVersion(final UUID uniqueId,
            final UUID documentUniqueId, final Long documentVersionId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("documentUniqueId", documentUniqueId);
        logger.logVariable("documentVersionId", documentVersionId);
        try {
            assertArchiveOnline();
            return getSessionModel().readArchiveDocumentVersion(localUserId(),
                    uniqueId, documentUniqueId, documentVersionId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    Map<DocumentVersion, Delta> readDocumentVersionDeltas(final UUID uniqueId,
            final Long compareVersionId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("compareVersionId", compareVersionId);
        try {
            assertArchiveOnline();
            final Map<DocumentVersion, Delta> deltas = new TreeMap<DocumentVersion, Delta>(
                    new ComparatorBuilder().createVersionByName());
            deltas.putAll(getSessionModel().readArchiveDocumentVersionDeltas(
                        localUserId(), uniqueId, compareVersionId));
            return deltas;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    Map<DocumentVersion, Delta> readDocumentVersionDeltas(final UUID uniqueId,
            final Long compareVersionId, final Long compareToVersionId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("compareVersionId", compareVersionId);
        logger.logVariable("compareToVersionId", compareToVersionId);
        try {
            assertArchiveOnline();
            final Map<DocumentVersion, Delta> deltas = new TreeMap<DocumentVersion, Delta>(
                    new ComparatorBuilder().createVersionByName());
            deltas.putAll(getSessionModel().readArchiveDocumentVersionDeltas(
                    localUserId(), uniqueId, compareVersionId,
                    compareToVersionId));
            return deltas;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }


    List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        return readDocumentVersions(uniqueId, versionId,
                defaultVersionComparator, defaultVersionFilter);
    }

    List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId, final Comparator<ArtifactVersion> comparator) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("comparator", comparator);
        return readDocumentVersions(uniqueId, versionId, comparator,
                defaultVersionFilter);
    }

    List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId, final Comparator<ArtifactVersion> comparator,
            final Filter<? super ArtifactVersion> filter) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("comparator", comparator);
        try {
            assertArchiveOnline();
            final List<DocumentVersion> versions =
                getSessionModel().readArchiveDocumentVersions(
                        localUserId(), uniqueId, versionId);
            FilterManager.filter(versions, filter);
            ModelSorter.sortDocumentVersions(versions, comparator);
            return versions;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId, final Filter<? super ArtifactVersion> filter) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("filter", filter);
        return readDocumentVersions(uniqueId, versionId,
                defaultVersionComparator, filter);
    }

    /**
     * Read a list of team members the container version was published to.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @return A <code>List&lt;User&gt;</code>.
     */
    Map<User, ArtifactReceipt> readPublishedTo(final UUID uniqueId,
            final Long versionId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        return readPublishedTo(uniqueId, versionId, defaultUserComparator,
                defaultUserFilter);
    }

    /**
     * Read a list of team members the container version was published to.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @param comparator
     *            A <code>Comparator&lt;User&gt;</code>.
     * @return A <code>List&lt;User&gt;</code>.
     */
    Map<User, ArtifactReceipt> readPublishedTo(final UUID uniqueId,
            final Long versionId, final Comparator<User> comparator) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("comparator", comparator);
        return readPublishedTo(uniqueId, versionId, comparator,
                defaultUserFilter);
    }

    /**
     * Read a list of team members the container version was published to.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @param comparator
     *            A <code>Comparator&lt;User&gt;</code>.
     * @param filter
     *            A <code>Filter&lt;? super User&gt;</code>.
     * @return A <code>List&lt;User&gt;</code>.
     */
    Map<User, ArtifactReceipt> readPublishedTo(final UUID uniqueId,
            final Long versionId, final Comparator<User> comparator,
            final Filter<? super User> filter) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("comparator", comparator);
        logger.logVariable("filter", filter);
        final Map<User, ArtifactReceipt> publishedTo =
            getSessionModel().readArchivePublishedTo(localUserId(), uniqueId, versionId);
        final List<User> users = new ArrayList<User>(publishedTo.size());
        for (final Entry<User, ArtifactReceipt> entry : publishedTo.entrySet()) {
            users.add(entry.getKey());
        }
        FilterManager.filter(users, filter);
        final Map<User, ArtifactReceipt> sortedFilteredPublishedTo =
            new TreeMap<User, ArtifactReceipt>(comparator);
        sortedFilteredPublishedTo.putAll(publishedTo);
        return sortedFilteredPublishedTo;
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
    Map<User, ArtifactReceipt> readPublishedTo(final UUID uniqueId,
            final Long versionId, final Filter<? super User> filter) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("filter", filter);
        return readPublishedTo(uniqueId, versionId, defaultUserComparator,
                filter); 
    }

    List<TeamMember> readTeam(final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        try {
            return getSessionModel().readArchiveTeam(localUserId(), uniqueId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    List<JabberId> readTeamIds(final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        try {
            assertArchiveOnline();
            return getSessionModel().readArchiveTeamIds(localUserId(),
                    uniqueId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    void restore(final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        try {
            assertArchiveOnline();
            getSessionModel().restoreArtifact(localUserId(), uniqueId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    private void assertArchiveOnline() {
        Assert.assertTrue(isArchiveOnline(), "Archive is not online.");
    }

    private void fireStageEnd(final OpenMonitor monitor, final OpenStage stage) {
        monitor.stageEnd(stage);
    }

    private Boolean isArchiveOnline() {
        // TODO Determine if the archive is actually online or not.
        return Boolean.TRUE;
    }
}

/*
 * Generated On: Sep 01 06 10:06:21 AM
 */
package com.thinkparity.ophelia.model.archive;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.filter.FilterManager;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.stream.StreamSession;

import com.thinkparity.ophelia.model.AbstractModelImpl;
import com.thinkparity.ophelia.model.session.InternalSessionModel;
import com.thinkparity.ophelia.model.util.sort.ComparatorBuilder;
import com.thinkparity.ophelia.model.util.sort.ModelSorter;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Archive Model Implementation</br>
 * <b>Description:</b><br>
 *
 * @author CreateModel.groovy
 * @version 1.1.2.1
 */
class ArchiveModelImpl extends AbstractModelImpl {

    /** A default artifact comparator. */
    private final Comparator<Artifact> defaultComparator;

    /** A default artifact filter. */
    private final Filter<? super Artifact> defaultFilter;

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
        this.defaultVersionComparator = new ComparatorBuilder().createVersionById(Boolean.TRUE);
        this.defaultVersionFilter = FilterManager.createDefault();
    }

    void archive(final Long artifactId) {
        logger.logApiId();
        logger.logVariable("artifactId", artifactId);
        try {
            assertArchiveOnline();
            final UUID uniqueId = getInternalArtifactModel().readUniqueId(artifactId);
            getSessionModel().archiveArtifact(localUserId(), uniqueId);
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
                return new FileInputStream(downloadStream(streamId));
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
            if (null != getInternalArtifactModel().readId(uniqueId)) {
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

    List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId, final UUID documentUniqueId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("documentUniqueId", documentUniqueId);
        return readDocumentVersions(uniqueId, versionId, documentUniqueId,
                defaultVersionComparator, defaultVersionFilter);
    }

    List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId, final UUID documentUniqueId,
            final Comparator<ArtifactVersion> comparator) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("documentUniqueId", documentUniqueId);
        logger.logVariable("comparator", comparator);
        return readDocumentVersions(uniqueId, versionId, documentUniqueId,
                comparator, defaultVersionFilter);
    }

    List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId, final UUID documentUniqueId,
            final Comparator<ArtifactVersion> comparator,
            final Filter<? super ArtifactVersion> filter) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("documentUniqueId", documentUniqueId);
        logger.logVariable("comparator", comparator);
        try {
            assertArchiveOnline();
            final List<DocumentVersion> versions =
                getSessionModel().readArchiveDocumentVersions(
                        localUserId(), uniqueId, versionId, documentUniqueId);
            FilterManager.filter(versions, filter);
            ModelSorter.sortDocumentVersions(versions, comparator);
            return versions;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId, final UUID documentUniqueId,
            final Filter<? super ArtifactVersion> filter) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("documentUniqueId", documentUniqueId);
        logger.logVariable("filter", filter);
        return readDocumentVersions(uniqueId, versionId, documentUniqueId,
                defaultVersionComparator, filter);
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

    private Boolean isArchiveOnline() {
        // TODO Determine if the archive is actually online or not.
        return Boolean.TRUE;
    }
}

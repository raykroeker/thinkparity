/*
 * Generated On: Sep 01 06 10:06:21 AM
 */
package com.thinkparity.ophelia.model.archive;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.filter.FilterManager;
import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.ophelia.model.AbstractModelImpl;
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
    ArchiveModelImpl(final Workspace workspace) {
        super(workspace);
        this.defaultComparator = new ComparatorBuilder().createByName();
        this.defaultFilter = FilterManager.createDefault();
        this.defaultVersionComparator = new ComparatorBuilder().createVersionById(Boolean.TRUE);
        this.defaultVersionFilter = FilterManager.createDefault();
    }

    List<Container> readContainers() {
        logApiId();
        return readContainers(defaultComparator);
    }

    List<Container> readContainers(final Comparator<Artifact> comparator) {
        logApiId();
        logVariable("comparator", comparator);
        return readContainers(comparator, defaultFilter);
    }

    List<Container> readContainers(final Comparator<Artifact> comparator,
            final Filter<? super Artifact> filter) {
        logApiId();
        logVariable("comparator", comparator);
        logVariable("filter", filter);
        final List<Container> containers =
            getInternalSessionModel().readArchiveContainers(localUserId());
        return containers;
    }

    List<Container> readContainers(final Filter<? super Artifact> filter) {
        logApiId();
        logVariable("filter", filter);
        return readContainers(defaultComparator, filter);
    }

    List<ContainerVersion> readContainerVersions(final UUID uniqueId) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        return readContainerVersions(uniqueId, defaultVersionComparator,
                defaultVersionFilter);
    }

    List<ContainerVersion> readContainerVersions(final UUID uniqueId,
            final Comparator<ArtifactVersion> comparator) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        logVariable("comparator", comparator);
        return readContainerVersions(uniqueId, comparator,
                defaultVersionFilter);
    }

    List<ContainerVersion> readContainerVersions(final UUID uniqueId,
            final Comparator<ArtifactVersion> comparator,
            final Filter<? super ArtifactVersion> filter) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        logVariable("comparator", comparator);
        logVariable("filter", filter);
        try {
            final List<ContainerVersion> versions =
                getInternalSessionModel().readArchiveContainerVersions(
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
        logApiId();
        logVariable("uniqueId", uniqueId);
        logVariable("filter", filter);
        return readContainerVersions(uniqueId, defaultVersionComparator,
                filter);
    }

    List<Document> readDocuments(final UUID uniqueId,
            final Long versionId) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        logVariable("versionId", versionId);
        return readDocuments(uniqueId, versionId, defaultComparator,
                defaultFilter);
    }

    List<Document> readDocuments(final UUID uniqueId,
            final Long versionId, final Comparator<Artifact> comparator) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        logVariable("versionId", versionId);
        logVariable("comparator", comparator);
        return readDocuments(uniqueId, versionId, comparator, defaultFilter);
    }

    List<Document> readDocuments(final UUID uniqueId,
            final Long versionId, final Comparator<Artifact> comparator,
            final Filter<? super Artifact> filter) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        logVariable("versionId", versionId);
        logVariable("filter", filter);
        try {
            final List<Document> documents =
                getInternalSessionModel().readArchiveDocuments(localUserId(),
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
        logApiId();
        logVariable("uniqueId", uniqueId);
        logVariable("versionId", versionId);
        logVariable("filter", filter);
        return readDocuments(uniqueId, versionId, defaultComparator, filter);
    }

    List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId, final UUID documentUniqueId) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        logVariable("versionId", versionId);
        logVariable("documentUniqueId", documentUniqueId);
        return readDocumentVersions(uniqueId, versionId, documentUniqueId,
                defaultVersionComparator, defaultVersionFilter);
    }

    List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId, final UUID documentUniqueId,
            final Comparator<ArtifactVersion> comparator) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        logVariable("versionId", versionId);
        logVariable("documentUniqueId", documentUniqueId);
        logVariable("comparator", comparator);
        return readDocumentVersions(uniqueId, versionId, documentUniqueId,
                comparator, defaultVersionFilter);
    }

    List<DocumentVersion> readDocumentVersions(final UUID uniqueId,
            final Long versionId, final UUID documentUniqueId,
            final Comparator<ArtifactVersion> comparator,
            final Filter<? super ArtifactVersion> filter) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        logVariable("versionId", versionId);
        logVariable("documentUniqueId", documentUniqueId);
        logVariable("comparator", comparator);
        try {
            final List<DocumentVersion> versions =
                getInternalSessionModel().readArchiveDocumentVersions(
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
        logApiId();
        logVariable("uniqueId", uniqueId);
        logVariable("versionId", versionId);
        logVariable("documentUniqueId", documentUniqueId);
        logVariable("filter", filter);
        return readDocumentVersions(uniqueId, versionId, documentUniqueId,
                defaultVersionComparator, filter);
    }
}
